package views;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Class for selecting which campaign is in use and providing the ability to load a new
 * campaign
 */
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

        this.c = c;

        this.clickLabel = clickLabel;
        this.impressionLabel = impressionLabel;
        this.serverLabel = serverLabel;

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

    public void importCampaign(){

        if(clickLog == null || impressionLog == null || serverLog == null){

            error("Please make sure you have selected the 3 unique csv log files!");
            return;

        }

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