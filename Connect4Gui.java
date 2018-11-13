
package uiconnect4;


import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.*;
import javafx.scene.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.scene.control.*;
import javafx.scene.input.MouseEvent;
import uiconnect4.GameLogic.GameLogic;
import uiconnect4.Connect4ComputerPlayer.Connect4ComputerPlayer;

/**
 *
 * @author David
 */
public class Connect4Gui extends Application{
    
    private int playerNum;
    private GameLogic game;
    private Connect4ComputerPlayer ai;
    private DataInputStream input;
    private DataOutputStream output;
    private boolean player1;
    final private Character[] playerChar = new Character[]{'X', 'O'};
    
    @Override
    public void start(Stage primaryStage) {
        TextUi textGame = new TextUi();
        GuiQuestion question1 = new GuiQuestion();
        GuiQuestion question2 = new GuiQuestion();
        GuiGrid grid = new GuiGrid();
        
        // create the scenes
        // create grid scenes
        Scene gridScene = grid.createGrid();
        // create question scenes
        Scene q1 = question1.createQuestion("Do use want to use a Graphic "
                + "or Text interface?", "Graphic", "Text");
        Scene q2 = question2.createQuestion("Do you want to play against "
                + "an AI or Human?", "AI", "Human");
        
        
        
        // Question 1 events
        question1.buttons.get(0).setOnAction((ActionEvent e) -> {
            primaryStage.setScene(q2);
        });
        
        question1.buttons.get(1).setOnAction((ActionEvent e) -> {
            try {
                textGame.startGame();
            } catch (IOException ex) {
                Logger.getLogger(Connect4Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
        });
        
        
        // Question 2 events
        
        // no need to connect if you're playing against the ai
        question2.buttons.get(0).setOnAction((ActionEvent e) -> {
            game = new GameLogic();
            ai = new Connect4ComputerPlayer(game);
            setLayerEvents(grid, playerChar, true);
            grid.bindLayerEvents();
            primaryStage.setScene(gridScene);
        });
        
        // if not against ai try and start a game with another player
        question2.buttons.get(1).setOnAction((ActionEvent e) -> {
            Socket socket;
            
            try{
                // network stuff
                socket = new Socket("localhost", 8000);
                input = new DataInputStream(socket.getInputStream());
                output = new DataOutputStream(socket.getOutputStream());
                
                
                player1 = input.readBoolean();
                grid.setPlayer(0);
                
                if(player1){
                    playerNum = 0; // determines if the player is first or second
                    setLayerEvents(grid, playerChar, false);
                    grid.bindLayerEvents();
                }
                else{
                    playerNum = 1;
                    setLayerEvents(grid, playerChar, false); 
                    new Thread(new Client(grid, input, playerChar[(playerNum+1)%2])).start();
                }
                
            }
            catch(IOException f){
                System.out.println("Connection Failed");
            }
            primaryStage.setScene(gridScene);
        });
        
        
        
        primaryStage.setScene(q1);
        primaryStage.show();
        
    }
    
    /**
     * Description: creates and stores the events that the event layer will use
     * @param grid
     * @param playerChar
     * @param isAi 
     */
    public void setLayerEvents(GuiGrid grid, Character[] playerChar, boolean isAi){
        // set grid layer events
        for(int i = 0; i < 7; i++){
            if(isAi){
                grid.setHandler(i, 0, new ClickHandlerAi(i, grid, game, ai));
            }
            else
                grid.setHandler(i, 0, new ClickHandlerHuman(i, grid, playerChar[playerNum], input, output));
            
            grid.setHandler(i, 1, new EnterHandler(i, grid, playerChar[playerNum]));
            grid.setHandler(i, 2, new ExitHandler(i, grid));
        }
    }
    
    /**
     * Description: Handles a human move
     */
    class ClickHandlerHuman implements EventHandler<MouseEvent>{
        int column;
        GuiGrid grid;
        Character player;
        DataInputStream input;
        DataOutputStream output;
        boolean win;
        boolean draw;
        
        public ClickHandlerHuman(int column, GuiGrid grid, Character player, 
                DataInputStream input, DataOutputStream output){
            this.column = column;
            this.grid = grid;
            this.player = player;
            this.input = input;
            this.output = output;
        }
        
        @Override
        public void handle(MouseEvent e){
            int row = 0;
            try {
                // tell the server what you did
                output.writeInt(column);
                // recieve the row you your value will land in
                row = input.readInt();
                
                // determine if the player has won
                win = input.readBoolean();
                draw = input.readBoolean();
            } catch (IOException ex) {
                Logger.getLogger(Connect4Gui.class.getName()).log(Level.SEVERE, null, ex);
            }
            grid.setElement(new Label(player.toString()), column, row);
            grid.unbindLayerEvents();
            
            
            if(win){
                grid.setWin(playerNum);
            }
            else if(draw){
                grid.setWin(-1);
            }
            else{
                grid.setPlayer(((playerNum + 1)%2)+1);
                new Thread(new Client(grid, input, playerChar[(playerNum+1)%2])).start(); 
            }
        }
    }
    
    /**
     * Description: Handles player move when one of the players is an ai
     */
    class ClickHandlerAi implements EventHandler<MouseEvent>{
        int column;
        GuiGrid grid;
        Connect4ComputerPlayer ai;
        GameLogic game;
        
        public ClickHandlerAi(int column, GuiGrid grid, GameLogic game, Connect4ComputerPlayer ai){
            this.column = column;
            this.grid = grid;
            this.ai = ai;
            this.game = game;
        }
        
        @Override
        public void handle(MouseEvent e){
            int error = 0;
            boolean win = false;
            boolean draw = false;
            int aiMove;
            error = game.addVal(column, game.playerChar[game.getTurn()%2]);

            // needed to skip turn if player tries to enter more than the column size
            if(error >= 0){
                grid.setElement(new Label(game.playerChar[game.getTurn()%2].
                        toString()), column, game.getColumnSize(column));
                win = game.checkWin(column, game.getColumnSize(column)-1, game.playerChar[game.getTurn()%2]);
                draw = game.checkDraw();
                game.incrementTurn();


                if(win == false && draw == false){
                    aiMove = ai.aiMove();
                    game.addVal(aiMove, game.playerChar[game.getTurn()%2]);
                    grid.setElement(new Label(game.playerChar[game.getTurn()%2].
                            toString()), aiMove, game.getColumnSize(aiMove));
                    game.incrementTurn();
                    win = game.checkWin(aiMove, game.getColumnSize(aiMove)-1, 'O');
                }
                
                
                if(win){
                    System.out.println("player " + game.getTurn()%2 + " won");
                    grid.unbindLayerEvents(); // prevents player from making a move after game is finished
                    grid.setWin((game.getTurn()+1)%2); // Tell the user who won
                }
                else if(draw){
                    grid.unbindLayerEvents();
                    grid.setWin(-1);
                }
            }
        }
    }
    
    /**
     * Description: removes the token from the Gui when the player leaves the event square
     */
    class ExitHandler implements EventHandler<MouseEvent>{
        int column;
        GuiGrid grid;
        
        public ExitHandler(int column, GuiGrid grid){
            this.column = column;
            this.grid = grid;
        }
        
        @Override
        public void handle(MouseEvent e){
            grid.clearElement(column, 0);
        }
    }
    
    /**
     * Description: adds a token when the player enters the event square
     */
    class EnterHandler implements EventHandler<MouseEvent>{
        int column;
        GuiGrid grid;
        Character player;
        
        public EnterHandler(int column, GuiGrid grid, Character player){
            this.column = column;
            this.grid = grid;
            this.player = player;
        }
        
        @Override
        public void handle(MouseEvent e){
            grid.setElement(new Label(player.toString()), column, 0);
        }
    }
}

