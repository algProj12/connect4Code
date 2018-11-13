

package uiconnect4;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import java.util.LinkedList;

/**
 * Name: David Plotzke
 * Date: Oct 28, 2018
 * Assignment:
 * Description:
 */


public class GuiQuestion {
    
    LinkedList<Button> buttons = new LinkedList<>(); // allows you to specify events for the buttons
    
    /**
     * Description: Creates a Question with two buttons to answer with
     * @param title
     * @param button1
     * @param button2
     * @return 
     */
    public Scene createQuestion(String title, String button1, String button2){
        // create scene and root
        VBox main = new VBox();
        main.setStyle("-fx-alignment: top-center");
        Scene questionScene = new Scene(main, 600, 60);
        
        // create the Buttons
        HBox question1 = new HBox(8);
        buttons.add(new Button(button1));
        buttons.add(new Button(button2));
        question1.getChildren().add(buttons.get(0));
        question1.getChildren().add(buttons.get(1));
        question1.setStyle("-fx-alignment: center");
        
        // Create the Question
        Label temp;
        temp = new Label(title);
        temp.setStyle("-fx-underline: true; -fx-font-size: 20;");
        
        // add the elements to the VBox
        main.getChildren().add(temp);
        main.getChildren().add(question1);
        return questionScene;
    }
    
    
}
