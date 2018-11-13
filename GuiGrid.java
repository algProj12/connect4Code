

package uiconnect4;

import javafx.beans.binding.Bindings;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;

/**
 * Name: David Plotzke
 * Date: Oct 25, 2018
 * Assignment:
 * Description:
 */



public class GuiGrid {
    
    private DoubleProperty textSize = new SimpleDoubleProperty();
    private VBox board;
    private StackPane[] eventLayer = new StackPane[7];
    private Label[] players = new Label[]{new Label("Player X's Turn"), new Label("Player O's Turn")};
    private Label[] winner = new Label[]{new Label("Player X is the Winner"), new Label("Player O is the Winner")};
    private EventHandler[][] handlers = new EventHandler[7][3];
    private GridPane grid;
    private VBox top;
    
    
    /**
     * Description: creates a grid scene whose events and details can be
     *              manipulated through this object
     * @return 
     */
    public Scene createGrid(){
        // create the root and scene
        board = new VBox();
        Scene gridScene = new Scene(board, 600, 600);
        
        // fill out the scene
        grid = new GridPane();
        top = new VBox();
        board.getChildren().add(top);
        board.getChildren().add(grid);
        
        // define player 1 or 2
        for(int i = 0; i < 2; i++){
            players[i].setStyle("-fx-underline: true; -fx-font-size: 20;");
            top.getChildren().add(players[i]);
            players[i].managedProperty().bind(players[i].visibleProperty());
        }
        players[1].setVisible(false);
        top.setStyle("-fx-alignment: top-center;");
        
        // define winning view
        
        
        // fill out the grid
        StackPane[] tempRow = new StackPane[7];
        for(int i = 0; i < 7; i++){
            for(int j = 0; j < 7; j++){
                tempRow[j] = new StackPane();
                
                // defines the "event" layer
                if(i == 0){
                    tempRow[j].setStyle("-fx-alignment: center; -fx-padding: 2px;");
                    eventLayer[j] = tempRow[j];
                }
                // the actual "grid"
                else{
                    tempRow[j].setStyle("-fx-border-style: solid; -fx-border-width: 1px; -fx-border-color: black; -fx-alignment: center; -fx-padding: 2px;");
                }
                tempRow[j].setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
            }
            grid.addRow(i, tempRow[0], tempRow[1], tempRow[2], tempRow[3], tempRow[4], tempRow[5], tempRow[6]);
        }
        
        // set the bounds of the grid and title
        grid.prefHeightProperty().bind(board.heightProperty().multiply(0.90));
        grid.prefWidthProperty().bind(board.widthProperty());
        top.prefHeightProperty().bind(board.heightProperty().multiply(0.10));
        
        // set column constraints
        grid.setStyle("-fx-alignment: center;");
        ColumnConstraints columnRules = new ColumnConstraints();
        columnRules.setPercentWidth(100/7.0);
        columnRules.setHalignment(HPos.CENTER);
        grid.getColumnConstraints().addAll(columnRules, columnRules, columnRules, columnRules, columnRules, columnRules, columnRules);
        
        // set row constraints
        RowConstraints rowRules = new RowConstraints();
        rowRules.setPercentHeight(100/7.0);
        rowRules.setValignment(VPos.CENTER);
        grid.getRowConstraints().addAll(rowRules, rowRules, rowRules, rowRules, rowRules, rowRules, rowRules);
        
        return gridScene;
    }
    
    /**
     * Description: places the given label at the given position in the matrix.
     * @param label
     * @param x
     * @param y
     * @return 
     */
    public boolean setElement(Label label, int x, int y){
        Node found = null;
        boolean set = false;
        
        // reverses the matrix so the stack starts at the bottom
        if(y != 0){
            y = 7 - y;
        }
        
        for(Node node : grid.getChildren()){
            if(GridPane.getRowIndex(node) == y && GridPane.getColumnIndex(node) == x){
                found = node;
            }
        }
        if(found != null){
            StackPane pane = (StackPane) found;
            pane.getChildren().clear();
            textSize.bind(board.widthProperty().add(board.heightProperty()).divide(45));
            label.styleProperty().bind(Bindings.concat("-fx-font-size: ", textSize.asString(), ";"));
            pane.getChildren().add(label);
            set = true;
        }
        return set;
    }
    
    /**
     * Description: removes an element from the specified (x, y) position in the
     *              list
     * @param x
     * @param y 
     */
    public void clearElement(int x, int y){
        Node found = null;
        
        for(Node node : grid.getChildren()){
            if(GridPane.getRowIndex(node) == y && GridPane.getColumnIndex(node) == x){
                found = node;
            }
        }
        if(found != null){
            StackPane pane = (StackPane) found;
            pane.getChildren().clear();
        }
    }
    
    /**
     * Description: controls the player title displayed in the Ui
     * @param player 
     */
    public void setPlayer(int player){
        if(player == 1){
            players[0].setVisible(true);
            players[1].setVisible(false);
        }
        else if(player == 2){
            players[0].setVisible(false);
            players[1].setVisible(true);
        }
    }
    
    /**
     * Description: sets a handler in the array of handlers
     * @param x
     * @param y
     * @param handler 
     */
    public void setHandler(int x, int y, EventHandler handler){
        handlers[x][y] = handler;
    }
    
    /**
     * Description: binds the created events to the event layer
     */
    public void bindLayerEvents(){
        for(int i = 0; i < 7; i++){
            eventLayer[i].setOnMouseClicked(handlers[i][0]);
            eventLayer[i].setOnMouseEntered(handlers[i][1]);
            eventLayer[i].setOnMouseExited(handlers[i][2]);
        }
    }
    
    /**
     * Description: prevents the user from making a move
     */
    public void unbindLayerEvents(){
        for(int i = 0; i < 7; i++){
            eventLayer[i].setOnMouseClicked(null);
        }
    }
    
    /**
     * Description: Switches the Ui title to display the winner
     * @param player 
     */
    public void setWin(int player){
        top.getChildren().clear();
        Label temp;
        if(player == 0){
            temp = new Label("Player X is the Winner");
            temp.setStyle("-fx-underline: true; -fx-font-size: 20; -fx-font-color: red");
            top.getChildren().add(temp);
        }
        else if(player == 1){
            temp = new Label("Player O is the Winner");
            temp.setStyle("-fx-underline: true; -fx-font-size: 20; -fx-font-color: black");
            top.getChildren().add(temp);
        }
        else if(player == -1){
            temp = new Label("This Game has Ended in a Draw");
            temp.setStyle("-fx-underline: true; -fx-font-size: 20; -fx-font-color: black");
            top.getChildren().add(temp);
        }
    }
}
