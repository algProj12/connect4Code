

package uiconnect4.Connect4ComputerPlayer;

/**
 * @author David Plotzke
 * Date: Oct 10, 2018
 * Assignment:
 * Description:
 */
import uiconnect4.GameLogic.GameLogic;
import java.util.Random;
public class Connect4ComputerPlayer {
    
    GameLogic game;
    
    public Connect4ComputerPlayer(GameLogic logic){
        game = logic;
    }
    /**
     * @return int
     * Description: aiMove: returns the column that the ai will choose as their
     *              move
     */
    public int aiMove(){
        boolean done = false;
        int column = -1;
        
        
        for(int i = 0; i < 7 && done == false; i++){
            if(game.inRange(i, game.getColumnSize(i))){
                column = i;
                done = game.checkWin(i, game.getColumnSize(i), 'X');
            }
        }
        if(done == false){
            Random test = new Random();
            column = test.nextInt(7);
        }
        return column;
    }
}
