

package uiconnect4;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.scene.control.Label;

/**
 * Assignment #:
 * Name: David Plotzke
 * Date: Nov 5, 2018
 * Description:
 */


public class Client implements Runnable{
    
    GuiGrid grid;
    DataInputStream input;
    Character player;
    int playerNum;
    
    public Client(GuiGrid grid, DataInputStream input, Character player){
        this.grid = grid;
        this.input = input;
        this.player = player;
    }
    
    @Override
    public void run(){
        
        try {
            // get the move of the player whose turn it currently is
            int moveX = input.readInt();
            int moveY = input.readInt();
            int playerNum = input.readInt();
            
            // Update the Gui with the other players move
            Platform.runLater(() -> {
                grid.setElement(new Label(player.toString()), moveX, moveY);
            });
            
            // Determine if the other player won or created a draw
            boolean win = input.readBoolean();
            boolean draw = input.readBoolean();
            
            if(win){
                Platform.runLater(() -> {
                    grid.setWin((playerNum+1)%2);
                });
            }
            else if(draw){
                Platform.runLater(() -> {
                    grid.setWin(-1);
                });
            }
            else{
                grid.bindLayerEvents();
                grid.setPlayer(playerNum+1); 
            }
        } 
        catch (IOException ex) {
            Logger.getLogger(Client.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
