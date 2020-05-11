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
import models.Metrics;
import models.ReaderCSV;

import javax.naming.ldap.Control;
import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class CampaignManagerController {

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

    public CampaignManagerController(){



    }

    @FXML
    public void initialize(){

        ControllerInjector.associateCampaignManager(this);
        initComboBox();

    }

    public void initComboBox(){

        new Thread(() -> {

            try {

                List<String> l = clickDao.getCampaigns();
                RootController.doGUITask(() -> populateComboBox(l));

            } catch (Exception e) {

                System.out.println("No data loaded!");

            }

        }).start();

    }

    public void populateComboBox(List<String> list){

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
            RootController.error("Please specify a campaign name, or make sure the campaign name is not empty");
            return;
        }

        // check that all 3 csvs have been uploaded
        if(clickLog == null || impressionLog == null || serverLog == null){
            RootController.error("Please make sure you have selected the 3 unique csv log files!");
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
                    RootController.error("The CSV reader timed out! Please try again");
                } else {

                    //RootController.success("CSV files successfully loaded");

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
            System.out.println("Finished importing data for new campaign: " + newCampaignName);

            //this.controller.success("Files successfully uploaded, please click \"OK\" to begin loading data");

            RootController.doGUITask(() -> {

                //Refresh the UI
                campaignComboBox.getItems().add(newCampaignName);
                newCampaignField.setText("");
                clickButton.setText("Choose file");
                impressionButton.setText("Choose file");
                serverButton.setText("Choose file");

            });

            ControllerInjector.getRootController().loadCampaignData(newCampaignName);

            //controller.unGreyOtherTabs();
            //this.controller.goToMainPage();

        } catch(Exception e){

            e.printStackTrace();
            RootController.error("error reading files...");

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

            return;

        } else if((impressionLog != null && clickLog.equals(impressionLog)) || (serverLog != null && clickLog.equals(serverLog))){

            RootController.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            clickLog = null;

        } else {

            String str = clickLog.getName();

            //Once a file has been loaded then set the button text to display its name
            RootController.doGUITask(() -> clickButton.setText(str));

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
        if(impressionLog == null){

            return;

        } else if((clickLog != null && impressionLog.equals(clickLog)) || (serverLog != null && impressionLog.equals(serverLog))){

            RootController.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique impression log CSV file");
            impressionLog = null;

        } else {

            String str = impressionLog.getName();
            RootController.doGUITask(() -> impressionButton.setText(str));

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

            return;

        } else if((impressionLog != null && serverLog.equals(impressionLog)) || (clickLog != null && serverLog.equals(clickLog))){

            RootController.error("You cannot have the same file for two inputs! Please make sure you have chosen the unique click log CSV file");
            serverLog = null;

        } else {

            String str = serverLog.getName();
            RootController.doGUITask(() -> serverButton.setText(str));

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
            if(campaignName != null){

                ControllerInjector.getRootController().updateData();

                /*
                Metrics metricsModel = ControllerData.getMetrics();
                ControllerData.setCurrentCampaign(campaignName);
                LocalDateTime start = getFromDateForCampaign(campaignName);
                LocalDateTime end = getToDateForCampaign(campaignName);
                ControllerData.setDateTimeFrom(start);
                ControllerData.setDateTimeTo(end);
                ControllerData.setNumImpressions(String.valueOf(String.valueOf(to2DP(metricsModel.getNumImpressions(start, end)))));
                ControllerData.setNumClicks(String.valueOf(to2DP(metricsModel.getNumClicks(start, end))));
                ControllerData.setNumUniques(String.valueOf(to2DP(metricsModel.getNumUniqs(start, end))));
                ControllerData.setNumBounces(String.valueOf(to2DP(metricsModel.getNumBounces(start, end))));
                ControllerData.setNumConversions(String.valueOf(to2DP(metricsModel.getConversions(start, end))));
                ControllerData.setTotalCost(String.valueOf(to2DP(metricsModel.getTotalCost(start, end))));
                ControllerData.setCtr(String.valueOf(to2DP(metricsModel.getCTR(start, end))));
                ControllerData.setCpa(String.valueOf(to2DP(metricsModel.getCPA(start, end))));
                ControllerData.setCpc(String.valueOf(to2DP(metricsModel.getCPC(start, end))));
                ControllerData.setCpm(String.valueOf(to2DP(metricsModel.getCPM(start, end))));
                ControllerData.setBounceRate(String.valueOf(to2DP(metricsModel.getBounceRate(start, end))));
                ControllerData.setBasicChartData(ControllerData.getUpdatedBasicChartData());*/

                //Once all the data has been loaded then the campaign manager can be closed and the main GUI can be shown
                ControllerInjector.getHandler().closeCampaignManager();
                ControllerInjector.getHandler().showMainGUI();

            }

        }).start();
    }

    public LocalDateTime getFromDateForCampaign(String campaignName) {
        ArrayList<LocalDateTime> mins = new ArrayList<>();
        mins.add(clickDao.getMinDateFromCampaign(campaignName));
        mins.add(impressionDao.getMinDateFromCampaign(campaignName));
        mins.add(serverEntryDao.getMinDateFromCampaign(campaignName));
        Collections.sort(mins);
        return mins.get(0);
    }

    public LocalDateTime getToDateForCampaign(String campaignName) {

        ArrayList<LocalDateTime> maxs = new ArrayList<>();
        maxs.add(clickDao.getMaxDateFromCampaign(campaignName));
        maxs.add(impressionDao.getMaxDateFromCampaign(campaignName));
        maxs.add(serverEntryDao.getMaxDateFromCampaign(campaignName));
        Collections.sort(maxs);
        return maxs.get(maxs.size() - 1);

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

        RootController.doGUITask(() -> {

            spinner.setDisable(false);
            spinner.setVisible(true);
            anchorPane.setEffect(new GaussianBlur(30));

        });

    }

    /**
     * End the loading indicator for the campaign manager
     */
    public void endLoadingIndicator(){

        RootController.doGUITask(() -> {

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