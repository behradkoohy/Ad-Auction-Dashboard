package controllers;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import com.jfoenix.controls.JFXSpinner;
import com.jfoenix.controls.JFXTextField;
import daos.ClickDao;
import daos.DaoInjector;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import javafx.fxml.FXML;
import javafx.scene.control.ProgressBar;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import models.ReaderCSV;

import java.io.File;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CampaignManagerController {
    private RootController controller;

    @FXML private JFXTextField newCampaignField;
    @FXML private JFXButton clickButton;
    @FXML private JFXButton impressionButton;
    @FXML private JFXButton serverButton;
    @FXML private JFXComboBox campaignComboBox;
    @FXML private JFXTextField editCampaignField;

    //For the campaign loading indicator
    @FXML private AnchorPane anchorPane;
    @FXML private JFXSpinner spinner;
    @FXML private ProgressBar progressBar;

    private ClickDao clickDao = DaoInjector.newClickDao();
    private ImpressionDao impressionDao = DaoInjector.newImpressionDao();
    private ServerEntryDao serverEntryDao = DaoInjector.newServerEntryDao();

    //Store the files here when importing the data for a new campaign
    private File clickLog;
    private File impressionLog;
    private File serverLog;

    public void init(RootController controller){
        this.controller = controller;
    }

    @FXML
    public void initialize(){

        this.loadPreviousCampaignsCombo();

    }

    private void loadPreviousCampaignsCombo(){

        new Thread(() -> {

            try {

                List<String> l = clickDao.getCampaigns();
                controller.doGUITask(() -> populateCampaignChooser(l));

            } catch (Exception e) {

                System.out.println("No data loaded!");

            }

        }).start();

    }

    private void populateCampaignChooser(List<String> list){

        campaignComboBox.getItems().clear();
        campaignComboBox.getItems().addAll(list);

    }

    @FXML
    /**
     * Called when the user pressed the create
     * new campaign button
     */
    public void createNewCampaign(){

        String newCampaignName = newCampaignField.getText();
        if( newCampaignName.trim().equals("")){
            this.controller.error("Please specify a campaign name, or make sure the campaign name is not empty");
            return;
        }

        // check that all 3 csvs have been uploaded
        if(clickLog == null || impressionLog == null || serverLog == null){
            this.controller.error("Please make sure you have selected the 3 unique csv log files!");
            return;
        }

        try {
            //Concurrency offers small benefit for this test set but may have much better improvements for other sets
            //TODO technically daos not thread safe but since atm each executes on different dao alright
            ExecutorService readerService = Executors.newCachedThreadPool();
            readerService.execute(() -> ReaderCSV.readCSV(clickLog.getAbsolutePath(), newCampaignName));
            readerService.execute(() -> ReaderCSV.readCSV(impressionLog.getAbsolutePath(), newCampaignName));
            readerService.execute(() -> ReaderCSV.readCSV(serverLog.getAbsolutePath(), newCampaignName));
            readerService.shutdown();
            //Pre-fetches to store in cache
            //clickDao.getFromCampaign(newCampaignName);
            //serverEntryDao.getFromCampaign(newCampaignName);

            try {
                if (!readerService.awaitTermination(60, TimeUnit.SECONDS)) {
                    readerService.shutdownNow();
                    this.controller.error("The CSV reader timed out! Please try again");
                } else {

                    this.controller.success("CSV files successfully loaded");

                }
            } catch (InterruptedException ex) {
                readerService.shutdownNow();
                Thread.currentThread().interrupt();
            }

            System.out.println("Finished importing data for new campaign: " + newCampaignName);

            //this.controller.success("Files successfully uploaded, please click \"OK\" to begin loading data");

            this.controller.doGUITask(() -> {

                //Refresh the UI
                campaignComboBox.getItems().add(newCampaignName);
                newCampaignField.setText("");
                clickButton.setText("Choose file");
                impressionButton.setText("Choose file");
                serverButton.setText("Choose file");

            });

            this.controller.loadCampaignData(newCampaignName);

            //controller.unGreyOtherTabs();
            //this.controller.goToMainPage();

        } catch(Exception e){

            e.printStackTrace();
            this.controller.error("error reading files...");

        }

    }

    /**
     * Initiates the file chooser to choose the click log file
     */
    @FXML
    public void chooseClick(){

        FileChooser chooser = new FileChooser();
        clickLog = chooser.showOpenDialog(null);

        //The file choosing was cancelled so do nothing
        if(clickLog == null){

            System.out.println("Click selection cancelled");

        } else if((clickLog.equals(impressionLog)) || (clickLog.equals(serverLog))){

            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            clickLog = null;

        } else {

            String str = clickLog.getName();

            //Once a file has been loaded then set the button text to display its name
            this.controller.doGUITask(() -> clickButton.setText(str));

        }

    }

    /**
     * Initiates the file chooser to choose the impression log file
     */
    @FXML
    public void chooseImpression(){

        FileChooser chooser = new FileChooser();
        impressionLog = chooser.showOpenDialog(null);

        //The file choosing was cancelled so do nothing
        if(impressionLog == null) {
            System.out.println("Impression selection cancelled");

        } else if((impressionLog.equals(clickLog)) || (impressionLog.equals(serverLog))){

            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique impression log CSV file");
            impressionLog = null;

        } else {

            String str = impressionLog.getName();
            this.controller.doGUITask(() -> impressionButton.setText(str));

        }

    }

    /**
     * Initiates a file chooser to choose the server log file
     */
    public void chooseServer(){

        FileChooser chooser = new FileChooser();
        serverLog = chooser.showOpenDialog(null);

        //The file choosing was cancelled so do nothing
        if(serverLog == null){

            System.out.println("Server log selection cancelled");

        } else if((serverLog.equals(impressionLog)) || (serverLog.equals(clickLog))){

            this.controller.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            serverLog = null;

        } else {

            String str = serverLog.getName();
            this.controller.doGUITask(() -> serverButton.setText(str));

        }

    }

    @FXML
    public void loadPreviousCampaign(){
        /*
        Once all the data has loaded ie at the end of all statements in
        the thread, then the main gui can startup
        */
        new Thread(() -> {

            String campaignName = (String) campaignComboBox.getValue();
            if(campaignName != null) {
                this.controller.loadCampaignData(campaignName);
            }

        }).start();
    }

    /**
     * Returns the double rounded to 2 decimal places, used
     * for presenting data in the stats tab
     * @return
     */
    public double to2DP(double x){

        x = (double) Math.round(x * 100);
        x /= 100;
        return x;

    }

    /**
     * Start the loading indicator for the campaign manager
     */
    public void startLoadingIndicator(){

        this.controller.doGUITask(() -> {

            spinner.setDisable(false);
            spinner.setVisible(true);
            anchorPane.setEffect(new GaussianBlur(30));

        });

    }

    /**
     * End the loading indicator for the campaign manager
     */
    public void endLoadingIndicator(){

        this.controller.doGUITask(() -> {

            spinner.setDisable(true);
            spinner.setVisible(false);
            anchorPane.setEffect(null);

        });

    }

    @FXML
    /**
     * Called when the user clicks the "save changes"
     * button ie they have edited the name of the campaign
     */
    public void updateCampaign(){

    }

    @FXML
    /**
     * Called when the user deletes a selected campaign
     */
    public void deleteCampaign(){

    }


}