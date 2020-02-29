package views;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXTimePicker;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import javax.swing.*;
import java.io.File;

/**
 * Window that pops up when the user wants to load
 * the data for a new campaign from 3 CSV files
 *
 * If the campaign has already been loaded then there
 * should be an appropriate error message stating this
 */
public class CampaignPopup {

    private File clickLog;
    private File impressionLog;
    private File serverLog;

    private Label clickLabel;
    private Label impressionLabel;
    private Label serverLabel;

    private Controller c;

    public CampaignPopup(Controller c){

        this.c = c;
        this.init();

    }

    private void init(){

        Stage popUp = new Stage();
        popUp.setTitle("Load Campaign");

        FileChooser chooser = new FileChooser();

        VBox vBox = new VBox();

        GridPane gridPane = new GridPane();
        vBox.getChildren().add(gridPane);
        popUp.setScene(new Scene(vBox));

        gridPane.setPadding(new Insets(10,10,10,10));
        gridPane.setHgap(10);

        gridPane.add(new Label("Click Log: "), 0, 0);
        gridPane.add(new Label("Impression Log: "), 0, 1);
        gridPane.add(new Label("Server Log: "), 0, 2);

        clickLabel = new Label("No file selected");
        gridPane.add(clickLabel, 2, 0);
        impressionLabel = new Label("No file selected");
        gridPane.add(impressionLabel, 2, 1);
        serverLabel = new Label("No file selected");
        gridPane.add(serverLabel, 2, 2);

        Button clickButton = new Button("Choose file");
        //clickButton.setStyle("-fx-background-color: #9575cd");
        gridPane.add(clickButton, 1, 0);
        clickButton.setOnAction(e -> chooseClick());

        Button impressionButton = new Button("Choose file");
        //impressionButton.setStyle("-fx-background-color: #9575cd");
        gridPane.add(impressionButton, 1, 1);
        impressionButton.setOnAction(e -> chooseImpression());

        Button serverButton = new Button("Choose file");
        //serverButton.setStyle("-fx-background-color: #9575cd");
        gridPane.add(serverButton, 1, 2);
        serverButton.setOnAction(e -> chooseServer());

        GridPane bottom = new GridPane();
        vBox.getChildren().add(bottom);

        Button load = new Button("Load");
        //load.setStyle("-fx-background-color: #9575cd");
        bottom.add(load,0,0);
        load.setOnAction(new EventHandler(){

            @Override
            public void handle(Event event) {

                if(clickLog == null || impressionLog == null || serverLog == null){

                    error("Please make sure you have chosen all 3 of the required CSV files!");

                } else {

                    /*
                    Haven't implemented this yet but will work something like this

                    The drop down menu will display all campaigns loaded

                    The controller will pass the file arguments to the csv loader
                    which will check if the campaign has already been loaded,
                    if it has not then it will load it and add it the drop down menu of campaigns

                    c.addCampaign(clickLog, impressionLog, serverLog)
                    */

                    //Then close the popup window after as the file has been chosen
                    popUp.close();

                }

            }

        });

        Button cancel = new Button( "Cancel");
        //cancel.setStyle("-fx-background-color: #9575cd");
        bottom.add(cancel, 1,0);
        cancel.setOnAction(e -> popUp.close());

        bottom.setPadding(new Insets(0,0,10,0));

        GridPane.setHgrow(load, Priority.ALWAYS);
        GridPane.setHalignment(load, HPos.CENTER);
        GridPane.setHgrow(cancel, Priority.ALWAYS);
        GridPane.setHalignment(cancel, HPos.CENTER);
        GridPane.setHgrow(bottom, Priority.ALWAYS);

        //This is NOT working for some reason?! it was and now it isnt? Confused?
        popUp.setResizable(false);
        popUp.show();

    }

    /**
     * Initiates the file chooser to choose the click log file
     */
    public void chooseClick(){

        FileChooser chooser = new FileChooser();
        clickLog = chooser.showOpenDialog(null);

        if(impressionLog == null && serverLog == null){

            return;

        } else if(clickLog.equals(impressionLog) || clickLog.equals(serverLog)){

            error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            clickLog = null;

        }

        clickLabel.setText(clickLog.getName());

    }

    /**
     * Initiates the file chooser to choose the impression log file
     */
    public void chooseImpression(){

        FileChooser chooser = new FileChooser();
        impressionLog = chooser.showOpenDialog(null);

        if(clickLog == null && serverLog == null){

            return;

        } else if(impressionLog.equals(clickLog) || impressionLog.equals(serverLog)){

            error("You cannot have the same file for two inputs! Please make sure you have chosen the unique impression log CSV file");
            impressionLog = null;

        }

        impressionLabel.setText(impressionLog.getName());

    }

    /**
     * Initiates a file chooser to choose the server log file
     */
    public void chooseServer(){

        FileChooser chooser = new FileChooser();
        serverLog = chooser.showOpenDialog(null);

        if(clickLog == null && impressionLog == null){

            return;

        } else if(serverLog.equals(clickLog) || serverLog.equals(impressionLog)){

            error("You cannot have the same file for two inputs! Please make sure you have chosen the unique server log csv file");

        }

        serverLabel.setText(serverLog.getName());

    }

    /**
     * Displays and shows an error dialog window with the given window
     * @param message
     */
    public void error(String message){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

}