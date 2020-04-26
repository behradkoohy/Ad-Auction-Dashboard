package controllers;

import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXTextField;
import daos.ClickDao;
import daos.DaoInjector;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.stage.FileChooser;
import models.ReaderCSV;
import popups.ImportPopup;
import popups.LoadPopup;

import java.io.File;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


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

    private ClickDao clickDao = DaoInjector.newClickDao();
    private ImpressionDao impressionDao = DaoInjector.newImpressionDao();
    private ServerEntryDao serverEntryDao = DaoInjector.newServerEntryDao();

    public void init(Controller controller){
        this.controller = controller;
    }

    @FXML
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

        //The file choosing was cancelled so do nothing
        if(clickLog == null){

            return;

        } else if((impressionLog != null && clickLog.equals(impressionLog)) || (serverLog != null && clickLog.equals(serverLog))){

            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            clickLog = null;

        } else {

            clickLabel.setText(clickLog.getName());

        }

        /*
        if((impressionLog != null && serverLog != null) && clickLog.equals(impressionLog) || clickLog.equals(serverLog)){
            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            clickLog = null;
        }

        clickLabel.setText(clickLog.getName());*/
    }

    /**
     * Initiates the file chooser to choose the impression log file
     */
    public void chooseImpression(){
        FileChooser chooser = new FileChooser();
        impressionLog = chooser.showOpenDialog(null);

        //The file choosing was cancelled so do nothing
        if(impressionLog == null){

            return;

        } else if((clickLog != null && impressionLog.equals(clickLog)) || (serverLog != null && impressionLog.equals(serverLog))){

            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique impression log CSV file");
            impressionLog = null;

        } else {

            impressionLabel.setText(impressionLog.getName());

        }

        /*
        if(impressionLog.equals(clickLog) || impressionLog.equals(serverLog)){
            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique impression log CSV file");
            impressionLog = null;
        }

        impressionLabel.setText(impressionLog.getName());*/
    }

    /**
     * Initiates a file chooser to choose the server log file
     */
    public void chooseServer(){
        FileChooser chooser = new FileChooser();
        serverLog = chooser.showOpenDialog(null);

        //The file choosing was cancelled so do nothing
        if(serverLog == null){

            return;

        } else if((impressionLog != null && serverLog.equals(impressionLog)) || (clickLog != null && serverLog.equals(clickLog))){

            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            serverLog = null;

        } else {

            serverLabel.setText(serverLog.getName());

        }

        /*
        if(serverLog.equals(clickLog) || serverLog.equals(impressionLog)){
            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique server log csv file");
            serverLog = null;
        }

        serverLabel.setText(serverLog.getName());*/
    }

    /**
     * Called by the create campaign button

     * Called when the user clicks the "create campaign" button
     *
     * This method should call an appropriate method from the
     * CampaignHandler class

     */
    @FXML
    public void loadNewCampaign(){
        // check that the campaign has a name
        String newCampaignName = campaignName.getText();
        if( newCampaignName.trim().equals("")){
            this.controller.error("Please specify a campaign name, or make sure the campaign name is not empty");
            return;
        }

        // check that all 3 csvs have been uploaded
        if(clickLog == null || impressionLog == null || serverLog == null){
            this.controller.error("Please make sure you have selected the 3 unique csv log files!");
            return;
        }

        ImportPopup i = new ImportPopup();

        ProgressBar clickProgress = i.getClickProgress();
        ProgressBar serverProgress = i.getServerProgress();
        ProgressBar impressionProgress = i.getImpressionProgress();

        // if it reaches this point, there is a name and files are loaded
        try{

            /*

            Trying to do the same thing but updates the progress bars cant get it working at the moment
            so leave it commented out for now

            Task clickTask = ReaderCSV.getReaderTask(clickLog.getAbsolutePath(), newCampaignName);
            clickProgress.progressProperty().bind(clickTask.progressProperty());

            Task impressionTask = ReaderCSV.getReaderTask(impressionLog.getAbsolutePath(), newCampaignName);
            impressionProgress.progressProperty().bind(impressionTask.progressProperty());

            Task serverTask = ReaderCSV.getReaderTask(serverLog.getAbsolutePath(), newCampaignName);
            serverProgress.progressProperty().bind(serverTask.progressProperty());

            ExecutorService readerService = Executors.newCachedThreadPool();
            readerService.execute(clickTask);
            readerService.execute(impressionTask);
            readerService.execute(serverTask);
            readerService.shutdown();

            try {
                if (!readerService.awaitTermination(60, TimeUnit.SECONDS)) {
                    readerService.shutdownNow();
                    controller.error("The CSV reader timed out! Please try again");
                } else {

                    controller.success("CSV files successfully loaded");

                }
            } catch (InterruptedException ex) {
                readerService.shutdownNow();
                Thread.currentThread().interrupt();
            }*/


            //Concurrency offers small benefit for this test set but may have much better improvements for other sets
            //TODO technically daos not thread safe but since atm each executes on different dao alright
            ExecutorService readerService = Executors.newCachedThreadPool();
            readerService.execute(() -> ReaderCSV.readCSV(clickLog.getAbsolutePath(), newCampaignName));
            readerService.execute(() -> ReaderCSV.readCSV(impressionLog.getAbsolutePath(), newCampaignName));
            readerService.execute(() -> ReaderCSV.readCSV(serverLog.getAbsolutePath(), newCampaignName));
            readerService.shutdown();

            try {
                if (!readerService.awaitTermination(60, TimeUnit.SECONDS)) {
                    readerService.shutdownNow();
                    controller.error("The CSV reader timed out! Please try again");
                } else {

                    controller.success("CSV files successfully loaded");

                }
            } catch (InterruptedException ex) {
                readerService.shutdownNow();
                Thread.currentThread().interrupt();
            }
            /*
            ReaderCSV.readCSV(clickLoc, campaignName, clickDao, impressionDao, serverEntryDao);
            ReaderCSV.readCSV(impressionLoc, campaignName, clickDao, impressionDao, serverEntryDao);
            ReaderCSV.readCSV(serverLoc, campaignName, clickDao, impressionDao, serverEntryDao);
             */
            System.out.println("Finished importing data for new campaign: " + campaignName);

            this.controller.success("Files successfully uploaded, please click \"OK\" to begin loading data");

            campaignChooser.getItems().add(campaignName.getText());

            // refresh UI
            campaignName.setText("");
            clickLabel.setText("");
            impressionLabel.setText("");
            serverLabel.setText("");

            this.controller.loadCampaignData(newCampaignName);
            this.controller.goToMainPage();

        }catch (Exception e) {
            this.controller.error("Error reading files...");
        }

    }



}
