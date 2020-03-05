package views;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;
import models.ReaderCSV;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    private Controller controller;
    private ReaderCSV rcsv = new ReaderCSV();

    private ClickDao clickDao;
    private ImpressionDao impressionDao;
    private ServerEntryDao serverEntryDao;

    public CampaignHandler(Controller controller, Label clickLabel, Label impressionLabel, Label serverLabel
            , ClickDao clickDao, ImpressionDao impressionDao, ServerEntryDao serverEntryDao) {
        this.clickLabel = clickLabel;
        this.impressionLabel = impressionLabel;
        this.serverLabel = serverLabel;
        this.controller = controller;
        this.clickDao = clickDao;
        this.impressionDao = impressionDao;
        this.serverEntryDao = serverEntryDao;

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

        } else {
            clickLabel.setText("");
            clickLog.getName();
            clickLabel.setText(clickLog.getName());
            clickLoc = clickLog.getAbsolutePath();
        }


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
        } else {
            impressionLabel.setText(impressionLog.getName());
            impressionLoc = impressionLog.getAbsolutePath();
        }


    }

    /**
     * Initiates a file chooser to choose the server log file
     */
    public void chooseServer(){
        FileChooser chooser = new FileChooser();
        serverLog = chooser.showOpenDialog(null);

        if(serverLog.equals(clickLog) || serverLog.equals(impressionLog)){
            error("You cannot have the same file for two inputs! Please make sure you have chosen the unique server log csv file");
            serverLog = null;
        } else {
            serverLabel.setText(serverLog.getName());
            serverLoc = serverLog.getAbsolutePath();
        }

    }

    public void importCampaign(String campaignName){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
        System.out.println(dtf.format(LocalDateTime.now()));
        System.out.println("Importing data for new campaign: " + campaignName);

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
            ReaderCSV.readCSV(clickLoc, campaignName, clickDao, impressionDao, serverEntryDao);
            ReaderCSV.readCSV(impressionLoc, campaignName, clickDao, impressionDao, serverEntryDao);
            ReaderCSV.readCSV(serverLoc, campaignName, clickDao, impressionDao, serverEntryDao);
            System.out.println(dtf.format(LocalDateTime.now()));
            System.out.println("Finished importing data for new campaign: " + campaignName);
            success("Files successfully uploaded, please click \"OK\" to begin populating data");
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