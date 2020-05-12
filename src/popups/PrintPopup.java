package popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controllers.RootController;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class PrintPopup {

    private RootController controller;

    private Stage popup;
    private JFXComboBox pageSelection;
    private JFXComboBox printSelection;

    public PrintPopup(RootController controller){

        System.out.println("print popup made");

        this.controller = controller;
        controller.doGUITask(this::init);

    }

    private void init(){

        popup = new Stage();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0,10,0,10));
        popup.setScene(new Scene(vBox));
        vBox.setMaxHeight(700);
        vBox.setMaxWidth(200);
        Label l = new Label("Please choose the page you would like to print");
        l.setPadding(new Insets(10,0,0,0));
        l.setWrapText(true);
        vBox.getChildren().add(l);
        pageSelection = new JFXComboBox();
        pageSelection.setPrefWidth(175);
        pageSelection.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(pageSelection);
        Label l2 = new Label("Please choose the print device you wish to use");
        l2.setPadding(new Insets(10,0,0,0));
        l2.setWrapText(true);
        vBox.getChildren().add(l2);
        printSelection = new JFXComboBox();
        printSelection.setPrefWidth(175);
        printSelection.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(printSelection);
        JFXButton button = new JFXButton("PRINT");
        button.setOnAction(e -> printSelection());
        vBox.setMargin(button, new Insets(10,0,20,0));
        button.setStyle("-fx-text-fill: white;" + " -fx-background-color:  #9575cd");
        vBox.getChildren().add(button);
        popup.show();

        System.out.println("print stage shown");

    }

    /**
     * Called by the button
     */
    public void printSelection(){

        //Get the selected page and selected print device

        //At the end of the method close the print popup
        popup.close();

    }

}