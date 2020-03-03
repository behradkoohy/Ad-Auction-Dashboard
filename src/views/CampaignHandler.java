package views;


import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import models.ReaderCSV;

import java.io.File;
import java.util.HashSet;
import java.util.Set;


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

    private String clickLoc;
    private String impressionLoc;
    private String serverLoc;

    private Controller c;



    private ReaderCSV rcsv = new ReaderCSV();

    /*
    * we need to connect this class to alex's csv handler
    *
    * */

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

        if(clickLog.equals(impressionLog) || clickLog.equals(serverLog)){

            error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            clickLog = null;

        }

        clickLabel.setText(clickLog.getName());
        clickLoc = clickLog.getAbsolutePath();
    }

    /**
     * Initiates the file chooser to choose the impression log file
     */
    public void chooseImpression(){

        FileChooser chooser = new FileChooser();
        impressionLog = chooser.showOpenDialog(null);

        if(impressionLog.equals(clickLog) || impressionLog.equals(serverLog)){

            error("You cannot have the same file for two inputs! Please make sure you have chosen the unique impression log CSV file");
            impressionLog = null;

        }

        impressionLabel.setText(impressionLog.getName());
        impressionLoc = impressionLog.getAbsolutePath();
    }

    /**
     * Initiates a file chooser to choose the server log file
     */
    public void chooseServer(){
        FileChooser chooser = new FileChooser();
        serverLog = chooser.showOpenDialog(null);

        if(serverLog.equals(clickLog) || serverLog.equals(impressionLog)){

            error("You cannot have the same file for two inputs! Please make sure you have chosen the unique server log csv file");

            c.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique server log csv file");


        }

        serverLabel.setText(serverLog.getName());
        serverLoc = serverLog.getAbsolutePath();
    }

    public void importCampaign(String campaignName){
        System.out.println("serverLog = " + serverLog);
        if(clickLog == null || impressionLog == null || serverLog == null){

            error("Please make sure you have selected the 3 unique csv log files!");

        }
        // check if all 3 variables are unique
        Set<String> filesSubmit = new HashSet<>();
        filesSubmit.add(clickLoc);
        filesSubmit.add(impressionLoc);
        filesSubmit.add(serverLoc);
        if (filesSubmit.size() < 3){
            error("Please make sure all 3 CSV files are unique!");
        } else {
            ReaderCSV.readCSV(clickLoc, campaignName);
            ReaderCSV.readCSV(impressionLoc, campaignName);
            ReaderCSV.readCSV(serverLoc, campaignName);
            /*
            new Thread(() -> ReaderCSV.readCSV(clickLoc)).start();
            new Thread(() -> ReaderCSV.readCSV(impressionLoc)).start();
            new Thread(() -> ReaderCSV.readCSV(serverLoc)).start();

             */
            success("shdhfhdsjsjdhdjjsajsjdfhjdkaskdjf");
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
    public void success(String message){

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }


}