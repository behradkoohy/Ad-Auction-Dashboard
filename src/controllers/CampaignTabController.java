package controllers;

import java.io.File;

import javafx.fxml.FXML;
import com.jfoenix.controls.*;
import javafx.scene.control.Label;
import javafx.stage.FileChooser;

import daos.*;
//import models.ReaderCSV;


public class CampaignTabController{
    private Controller controller;

    @FXML private JFXComboBox campaignChooser;
    @FXML private JFXTextField campaignName;
    @FXML private Label clickLabel;
    @FXML private Label impressionLabel;
    @FXML private Label serverLabel;

    private File clickLog;
    private File impressionLog;
    private File serverLog;

    private ClickDao clickDao = new ClickDao();
    private ImpressionDao impressionDao = new ImpressionDao();
    private ServerEntryDao serverEntryDao = new ServerEntryDao();

    public void init(Controller controller){
        this.controller = controller;
    }

    public void initialize() {
        // load previous campaigns
        // TODO : change method to use a campaign Entity and DAO
        this.loadPreviousCampaigns();
    }

    // TODO : refactor using campaign Entity
    private void loadPreviousCampaigns(){
        try {
            campaignChooser.getItems().addAll(clickDao.getCampaigns());
        } catch (Exception e) {
            System.out.println("No data loaded!");
        }
    }

    public void loadSelectedCampaign() {
        String campaignName = (String)campaignChooser.getValue();
        if( campaignName != null ){
            this.controller.success("Selected campaign " + campaignName + ". Importing data..." );
            // TODO : import data
            // TODO : add loader
            this.controller.loadCampaignData(campaignName);
            this.controller.goToMainPage();
        }else{
            this.controller.error("Please select a campaign");
        }
    }

    /**
     * Initiates the file chooser to choose the click log file
     */
    public void chooseClick(){
        FileChooser chooser = new FileChooser();
        clickLog = chooser.showOpenDialog(null);

        if(clickLog.equals(impressionLog) || clickLog.equals(serverLog)){
            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
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

        if(impressionLog.equals(clickLog) || impressionLog.equals(serverLog)){
            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique impression log CSV file");
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

        if(serverLog.equals(clickLog) || serverLog.equals(impressionLog)){
            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique server log csv file");
            serverLog = null;
        }

        serverLabel.setText(serverLog.getName());
    }

    /**
     * Called by the create campaign button

     * Called when the user clicks the "create campaign" button
     *
     * This method should call an appropriate method from the
     * CampaignHandler class

     */
    public void loadNewCampaign(){
        // check that the campaign has a name
        String newCampaignName = campaignName.getText();
        if( newCampaignName.equals("")){
            this.controller.error("Please specify a campaign name");
            return;
        }

        // check that all 3 csvs have been uploaded
        if(clickLog == null || impressionLog == null || serverLog == null){
            this.controller.error("Please make sure you have selected the 3 unique csv log files!");
            return;
        }

        // if it reaches this point, there is a name and files are loaded
        try{
            // TODO : uncomment
//            ReaderCSV.readCSV(clickLog.getAbsolutePath(), newCampaignName);
//            ReaderCSV.readCSV(impressionLog.getAbsolutePath(), newCampaignName);
//            ReaderCSV.readCSV(serverLog.getAbsolutePath(), newCampaignName);

            this.controller.success("Files successfully uploaded, please click \"OK\" to begin loading data");

            campaignChooser.getItems().add(campaignName.getText());

            // refresh UI
            campaignName.setText("");
            clickLabel.setText("");
            impressionLabel.setText("");
            serverLabel.setText("");

            // TODO : import data
            // TODO : add loader

        }catch (Exception e) {
            this.controller.error("Error reading files...");
        }

    }



}
