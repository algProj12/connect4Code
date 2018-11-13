

package uiconnect4.GameLogic;

/**
 * @author  David Plotzke
 * Date: Sep 24, 2018
 * Assignment:
 * Description:
 */
public class GameLogic {

    private final int COLNUM = 7;
    private final int ROWNUM = 6;
    private char[][] gameBoard = new char[COLNUM][ROWNUM];
    private int [] filledSquares = new int[COLNUM];
    final public Character[] playerChar = new Character[]{'X', 'O'};
    private int turn = 0;

    /**
     * Description: Checks if the inputed player has a winning connect 4 at the
     *              specified point
     * @param x
     * @param y
     * @param val
     * @return valid
     */
    public boolean checkWin(int x, int y, char val){
        int magnitude = -1;
        int direction = 0;
        int[] coord = new int[2];
        int xVal;
        int yVal;
        int count = 0;
        int count2;
        int count3;
        int count4;
        char player = val;
        coord[0] = -1;
        coord[1] = -1;
        
        do{
            count2 = 0;
            count3 = 0;
            count4 = 0;
            if(count%2 == 0){
                direction = (direction+1)%2;
            }
            if(count%4 == 0){
                magnitude *= -1;
            }
            coord[direction] += magnitude;
            xVal = x;
            yVal = y;
            
            while(count2 < 3 && inRange(xVal+coord[0], yVal+coord[1]) && gameBoard[xVal+coord[0]][yVal+coord[1]] != 0){
                xVal += coord[0];
                yVal += coord[1];
                count2++;
            }
            coord[0] *= -1;
            coord[1] *= -1;
            
            count2 += 3;
            if(xVal == x && yVal == y){
                count3++;
                xVal += coord[0];
                yVal += coord[1];
            }
            
            while(inRange(xVal, yVal) && gameBoard[xVal][yVal] != 0 && count3 != 4 && count4 < count2){
                count4++;
                if(player == gameBoard[xVal][yVal]){
                    count3++;
                }
                else{
                    count3 = 0;
                }
                if(xVal+coord[0] == x && yVal+coord[1] == y){
                    xVal += coord[0];
                    yVal += coord[1];
                    count3++;
                }
                
                xVal += coord[0];
                yVal += coord[1];
            }
            
            coord[0] *= -1;
            coord[1] *= -1;
            
            count++;
        }while(count < 4 && count3 != 4);
        return count3 == 4;
    }
    
    /**
     * 
     * @param column
     * @param val
     * @return error
     */
    public int addVal(int column, char val){
        int error = 0;
        if(filledSquares[column] < ROWNUM){
            gameBoard[column][filledSquares[column]] = val;
            filledSquares[column]++;
        }
        else{
            System.out.println("column is full, please choose another");
            error = -1;
        }
        return error;
    }
    
    /**
     * 
     * @param x
     * @param y
     * @return valid
     */
    public boolean inRange(int x, int y){
        boolean valid = false;
        if(x < COLNUM && x >= 0 && y < ROWNUM && y >= 0){
            valid = true;
        }
        else
            valid = false;
        return valid;
    }
    
    /**
     * 
     * @return gameBoard
     */
    public char[][] getBoard(){
        return gameBoard;
    }
    
    /**
     * 
     * @param column
     * @return filledSquares
     */
    public int getColumnSize(int column){
        return filledSquares[column];
    }
    
    /**
     * 
     * @return aggregation
     */
    public boolean checkDraw(){
        int aggregation = 0;
        for(int i = 0; i < filledSquares.length; i++){
            aggregation += filledSquares[i];
        }
        return aggregation == 42;
    }
    
    
    public void incrementTurn(){
        turn++;
    }
    
    public int getTurn(){
        return turn;
    }
}

/*
public boolean checkWin(int x, int y){
        boolean win = false;
        boolean valid = false;
        int magnitude = -1;
        int direction = 0;
        int[] coord = new int[2];
        int xVal;
        int yVal;
        int count = 0;
        char player = gameBoard[x][y];
        coord[0] = -1;
        coord[1] = -1;
        
        do{
            valid = true;
            if(count%2 == 0){
                direction = (direction+1)%2;
            }
            if(count%4 == 0){
                magnitude *= -1;
            }
            coord[direction] += magnitude;
            xVal = x;
            yVal = y;
            
            if(inRange(x+coord[0]*3, y+coord[1]*3)){
                for(int j = 0; j < 3 && valid == true; j++){
                    xVal += coord[0];
                    yVal += coord[1];
                    valid = player == gameBoard[xVal][yVal];
                }
            }
            else
                valid = false;
            count++;
        }while(count < 9 && valid == false);
        return valid;
    }
*/
