package views;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;

import java.io.File;

public class CampaignHandler {

    private File clickLog;
    private File impressionLog;
    private File serverLog;

    private Label clickLabel;
    private Label impressionLabel;
    private Label serverLabel;

    private Controller c;

    public CampaignHandler(Controller c, Label clickLabel,
                           Label impressionLabel, Label serverLabel){

        this.clickLabel = clickLabel;
        this.impressionLabel = impressionLabel;
        this.serverLabel = serverLabel;

        this.c = c;

    }

    public void createCampaign(){

        if(clickLog == null || impressionLog == null || serverLog == null){

            c.error("Please make sure you have chosen all 3 of the required CSV files!");

        } else {

            c.goToMainPage();

        }

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

            c.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
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

            c.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique impression log CSV file");
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

            c.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique server log csv file");

        }

        serverLabel.setText(serverLog.getName());

    }

}