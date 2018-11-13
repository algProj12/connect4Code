

package uiconnect4;

import uiconnect4.GameLogic.GameLogic;
import uiconnect4.Connect4ComputerPlayer.Connect4ComputerPlayer;
import java.io.*;

/**
 * Name: David Plotzke
 * Date: Oct 29, 2018
 * Assignment:
 * Description:
 */
public class TextUi {
    
    /**\
     * Description: Runs a console version of connect 4
     * @throws IOException 
     */
    public void startGame() throws IOException{
        
        boolean victory = false;
        char[] players = new char[2];
        players[0] = 'X';
        players[1] = 'O';
        int turn = 0;
        int playersTurn = 0;
        int ai = 0;
        int parsedInput;
        boolean validation = false;
        String input;
        GameLogic game = new GameLogic();
        Connect4ComputerPlayer player = new Connect4ComputerPlayer(game);
        BufferedReader stdin = new BufferedReader 
                (new InputStreamReader(System.in));
        do{
            System.out.println("Human: 0 | AI: 1");
            input = stdin.readLine();
            parsedInput = checkInput(input, 0, 1);
            if(parsedInput >= 0){
                ai = parsedInput;
                validation = true;
            }
        }while(validation == false);

        System.out.println("Begin Game.");
        while(victory == false){
            input = "";
            parsedInput = 0;

            if(ai == 0 || playersTurn == 0){
                System.out.println("Enter an Input player " + players[playersTurn] + ": ");
                input = stdin.readLine();
                parsedInput = checkInput(input, 1, 7);
                if(parsedInput > 0){
                    game.addVal(parsedInput-1, players[playersTurn]);
                    if(parsedInput > 0){
                        turn++;
                        playersTurn = turn%2;
                    }
                }
            }
            else{
                parsedInput = player.aiMove()+1;
                game.addVal(parsedInput-1, 'O');
                turn++;
                playersTurn = turn%2;
                System.out.println();
            }

            printBoard(game.getBoard());
            if(turn > 4){
                if(game.checkWin(parsedInput-1, game.getColumnSize(parsedInput-1)-1, players[(playersTurn+1)%2])){
                    victory = true;
                    System.out.println("Player " + players[(playersTurn+1)%2] + " Won");
                }
                else if(game.checkDraw()){
                    victory = true;
                    System.out.println("This Game has Ended in a Draw");
                }
            }
        }
        
        
        /*
        GameLogic test = new GameLogic();
        
        test.addVal(0, 'X');
        test.addVal(1, 'O');
        test.addVal(2, 'X');
        test.addVal(3, 'X');
        test.addVal(5, 'X');
        test.addVal(4, 'X');
        
        System.out.println(test.checkWin(4, test.getColumnSize(0)-1));
        printBoard(test.getBoard());
        */
        /*
        GameLogic test = new GameLogic();
        test.addVal(0, 'X');
        test.addVal(1, 'X');
        test.addVal(1, 'X');
        test.addVal(2, 'X');
        test.addVal(2, 'X');
        test.addVal(2, 'X');
        test.addVal(3, 'O');
        test.addVal(3, 'X');
        test.addVal(3, 'X');
        test.addVal(3, 'X');
        
        System.out.println(test.checkWin(0, test.getColumnSize(0)-1));
        printBoard(test.getBoard());
        */
        
        /*
        GameLogic test = new GameLogic();
        test.addVal(0, 'X');
        test.addVal(0, 'O');
        test.addVal(0, 'X');
        test.addVal(0, 'O');
        test.addVal(0, 'X');
        test.addVal(0, 'O');
        
        System.out.println(test.checkWin(0, test.getColumnSize(0)-1, 'O'));
        printBoard(test.getBoard());
        */
    }

    /**
     * 
     * @param board 
     */
    public static void printBoard(char[][] board){
        for(int i = 5; i > -1; i--){
            System.out.printf("|%c|%c|%c|%c|%c|%c|%c|%n", 
                    board[0][i], board[1][i], board[2][i], board[3][i], 
                    board[4][i], board[5][i], board[6][i]);
        }
    }
    
    /**
     * Description: parses the string input and checks if the resulting integer
     *              value is between the inputed min and max
     * @param input
     * @param min
     * @param max
     * @return int
     */
    public static int checkInput (String input, int min, int max){
        int parsedInput = 0;
        try{
            parsedInput = Integer.parseInt(input);
            }
        catch(NumberFormatException ex){
            System.out.println("Please Enter a Number");
            parsedInput = -1;
            }
        
        if(parsedInput < min || parsedInput > max){
            System.out.println("Please Enter a Value Between " + min + " and " + max);
            parsedInput = -2;
        }
        return parsedInput;
    }
}
