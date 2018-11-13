

package uiconnect4;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import uiconnect4.GameLogic.GameLogic;

/**
 * Assignment #:
 * Name: David Plotzke
 * Date: Nov 3, 2018
 * Description:
 */

public class UiServer implements Runnable {

    private Socket[] players = new Socket[2];
    private DataInputStream[] inputs = new DataInputStream[2];
    private DataOutputStream[] outputs = new DataOutputStream[2];
    
    public UiServer(Socket player1, Socket player2){
        this.players[0] = player1;
        this.players[1] = player2;
    }
    
    @Override
    public void run(){
        
        boolean win = false;
        boolean draw = false;
        GameLogic game = new GameLogic();
        int move;
        
        try {
            System.out.println("thread on");
            inputs[0] = new DataInputStream(players[0].getInputStream());
            outputs[0] = new DataOutputStream(players[0].getOutputStream());
            inputs[1] = new DataInputStream(players[1].getInputStream());
            outputs[1] = new DataOutputStream(players[1].getOutputStream());
            
            // tell the clients which is first
            outputs[0].writeBoolean(true);
            outputs[1].writeBoolean(false);
            
            while(!win || !draw){
                // get the move of the player whose turn it is
                move = inputs[game.getTurn()%2].readInt();
                game.addVal(move, game.playerChar[game.getTurn()%2]);
                
                // tell the player what the column size is
                outputs[game.getTurn()%2].writeInt(game.getColumnSize(move));
                game.incrementTurn();
                
                // check if the player has won yet
                win = game.checkWin(move, game.getColumnSize(move)-1, game.playerChar[(game.getTurn()-1)%2]);
                draw = game.checkDraw();
                
                // give the inactive player the other players move
                outputs[game.getTurn()%2].writeInt(move);
                outputs[game.getTurn()%2].writeInt(game.getColumnSize(move));
                outputs[game.getTurn()%2].writeInt(game.getTurn()%2);
                
                // tell the clients if someone won
                outputs[0].writeBoolean(win);
                outputs[0].writeBoolean(draw);
                outputs[1].writeBoolean(win);
                outputs[1].writeBoolean(draw);
                
            }

            System.out.println(inputs[0].readInt());

        } 
        catch (IOException ex) {
            Logger.getLogger(UiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        
    }
    
    public static void main(String[] args) {
        
        ServerSocket serverSocket;
        Socket socket1;
        Socket socket2;
        UiServer task;
        
        try{
            serverSocket = new ServerSocket(8000); 
            
            while(true){
                System.out.println("Getting First Player");
                socket1 = serverSocket.accept();
                System.out.println("Getting Second Player");
                socket2 = serverSocket.accept();
                task = new UiServer(socket1, socket2);
                new Thread(task).start();
            }  
        }
        catch (IOException ex) {
            Logger.getLogger(UiServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

}
