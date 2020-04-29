package controllers;

import com.jfoenix.controls.JFXSlider;
import com.jfoenix.controls.JFXTabPane;
import daos.ClickDao;
import daos.DaoInjector;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;
import models.Metrics;
import popups.LoadPreviousPopup;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

public class Controller {
    /*
    Corresponding UI components that can be found in scene builder with these identifiers,
    this is what the "@FXML" annotation means

    When the application starts the FXML loader will inject the actual
    UI component references in scene builder with these variables
     */
    @FXML private JFXTabPane LHS;
    @FXML private GridPane gridPane;

    @FXML AnchorPane campaignTab;
    @FXML AnchorPane statisticsTab;
    @FXML AnchorPane histogramTab;
    @FXML AnchorPane graphsTab;
    @FXML StackPane filterTab;
    @FXML AnchorPane accessibilityTab;
    @FXML AnchorPane printTab;

    @FXML private CampaignTabController campaignTabController;
    @FXML private StatisticsTabController statisticsTabController;
    @FXML private HistogramTabController histogramTabController;
    @FXML private GraphsTabController graphsTabController;
    @FXML private FilterTabController filterTabController;
    @FXML private AccessibilityTabController accessibilityTabController;
    @FXML private PrintTabController printTabController;

    //The number of "units" that will be displayed along the x axis
    public int unitsDifference;

    //BOUNCE CONTROLLER
    @FXML private Label bouncePagesLabel;
    @FXML private Label bounceDurationLabel;
    @FXML private JFXSlider bouncePageSlider;
    @FXML private JFXSlider bounceDurationSlider;
    //BOUNCE CONTROLLER

    private Metrics metrics;

    private String currentCampaignName;

    //Should I do this - or put in in campaign tab controller???
    ClickDao clickDao = DaoInjector.newClickDao();
    ImpressionDao impressionDao = DaoInjector.newImpressionDao();
    ServerEntryDao serverEntryDao = DaoInjector.newServerEntryDao();

    @FXML
    /**
     * This method is called when the FXML loader has finished injecting
     * references, so whenever you need to call a method on a UI component
     * when the program opens do it from here so you know for
     * certain it won't be null
     *
     * Do all initialization steps in this method
     *
     * Also used to link other the other controllers to this one
     */
    public void initialize(){
        //TODO dont think this is needed as I can toggle in scene builder, keep here for now
        //lineChart.setAnimated(false);

        metrics = new Metrics();

        campaignTabController.init(this);
        statisticsTabController.init(this);
        histogramTabController.init(this);
        graphsTabController.init(this);
        filterTabController.init(this);
        accessibilityTabController.init(this);
        printTabController.init(this);

        greyOutOtherTabs();

    }


    public void blurWindow() {

        Platform.runLater(() -> gridPane.getParent().setEffect(new GaussianBlur(10)));

    }

    public  void unblurWindow() {

        Platform.runLater(() -> gridPane.getParent().setEffect(null));

    }

    /**
     * Starts the loading indicator on the filter tab panel
     */
    public void startLoadingIndicator(){

        filterTabController.startLoadingIndicator();

    }

    /**
     * Ends the laoding indicator on the filter tab panel
     */
    public void endLoadingIndicator(){

        filterTabController.endLoadingIndicator();

    }

    /**
     * Greys out all the tabs apart from campaign management
     */
    public void greyOutOtherTabs(){

        LHS.getTabs().get(1).setDisable(true);
        LHS.getTabs().get(2).setDisable(true);
        LHS.getTabs().get(3).setDisable(true);
        LHS.getTabs().get(4).setDisable(true);

    }

    /**
     * Ungreys all the other tabs
     */
    public void unGreyOtherTabs(){

        LHS.getTabs().get(1).setDisable(false);
        LHS.getTabs().get(2).setDisable(false);
        LHS.getTabs().get(3).setDisable(false);
        LHS.getTabs().get(4).setDisable(false);

    }

    public Metrics getMetrics() {
        return this.metrics;
    }

    /**
    * General error / success functions
    * */
    public void error(String message){

        if(Platform.isFxApplicationThread()){

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();

        } else {

            Platform.runLater(() -> {

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();

            });

        }

    }

    public void success(String message){

        if(Platform.isFxApplicationThread()){

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();

        } else {

            Platform.runLater(() -> {

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Success");
                alert.setHeaderText(null);
                alert.setContentText(message);
                alert.showAndWait();

            });

        }

    }

    public Task getLoadCampaignTask(String campaignName, LoadPreviousPopup p){

        return new Task (){

            public Task<Void> call(){

                currentCampaignName = campaignName;

                //The number of things that have to be done
                int total = 5;

                int done = 0;

                filterTabController.setDateTimeFrom(getFromDateForCampaign(campaignName));
                updateProgress(++done, total);

                filterTabController.setDateTimeTo(getToDateForCampaign(campaignName));
                updateProgress(++done, total);

                statisticsTabController.loadData(campaignName);
                statisticsTabController.setFirstLoad();
                updateProgress(++done, total);

                histogramTabController.loadData(campaignName);
                updateProgress(++done, total);

                graphsTabController.loadData(campaignName);
                updateProgress(++done, total);

                unGreyOtherTabs();
                goToMainPage();
                updateProgress(++done, total);

                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

                p.close();
                unblurWindow();

                return null;

            }

        };

    }

    /**
     * Used to pass data to the other controllers when something changes
     *
     *
     * */
    public void loadCampaignData(String campaignName) {
        this.currentCampaignName = campaignName;
        blurWindow();
        LoadPreviousPopup p = new LoadPreviousPopup("Loading new campaign: " + campaignName);
        Task t = getLoadCampaignTask(campaignName, p);
        p.bind(t.progressProperty());
        new Thread(t).start();
        /*
        filterTabController.setDateTimeFrom(getFromDateForCampaign(campaignName));
        filterTabController.setDateTimeTo(getToDateForCampaign(campaignName));
        statisticsTabController.loadData(campaignName);
        histogramTabController.loadData(campaignName);
        graphsTabController.loadData(campaignName);
        
         */
    }

    //Takes toggled bools for filter as params
    public void reloadCampaignData() {

        startLoadingIndicator();
        statisticsTabController.loadData(this.currentCampaignName);
        histogramTabController.loadData(this.currentCampaignName);
        graphsTabController.loadData(this.currentCampaignName);
        endLoadingIndicator();

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
     * Used by the campaign manager to go to next page after loading/creating a campaign
     */
    public void goToMainPage(){
        SingleSelectionModel<Tab> model = LHS.getSelectionModel();

        if(Platform.isFxApplicationThread()){

            model.select(1);

        } else {

            Platform.runLater(() -> model.select(1));

        }

    }

    public Controller(){
        //True/false assignments correspond to whether the checkboxes are selected
        unitsDifference=0;
    }

    /**
     * Calculates the time and date difference specified by the user,
     * and returns the string of the unit that should be used as a metric
     *
     * @return metric whether the metric is minutes, hours, days or weeks
     */
    public String calcMetric(){

        LocalDateTime before = LocalDateTime.of(filterTabController.dFrom, filterTabController.tFrom);
        LocalDateTime after = LocalDateTime.of(filterTabController.dTo, filterTabController.tTo);

        long days = before.until(after, ChronoUnit.DAYS);
        long hours = before.until(after, ChronoUnit.HOURS);

        if(days >= 30){

            unitsDifference = Math.round(before.until(after, ChronoUnit.WEEKS));
            return "Weeks";

        } else if(days >= 1){

            unitsDifference = Math.round(days);
            return "Days";

        } else if(hours >= 1){

            unitsDifference = Math.round(hours);
            return "Hours";

        } else {

            unitsDifference = Math.round(before.until(after, ChronoUnit.MINUTES));
            return "Minutes";

        }

    }

    public String getCurrentCampaignName(){

        return currentCampaignName;

    }

    /**
     * Can be used to check if the thread that called this method is the javaFX thread,
     * if not then throw an exception
     * @param methodName
     * @throws Exception
     */
    public void verifyIsFXThread(String methodName) throws Exception{

        if(!Platform.isFxApplicationThread()){

            throw new Exception(methodName + " was called on a thread other than the javaFX thread! \n Make sure " +
                    "you use: " + methodName + "EXT if you want to update the GUI from an external thread");

        }

    }

    /**
     *
     * @return gives current start date
     */
    public LocalDateTime getStart() {

        LocalDateTime before = LocalDateTime.of(filterTabController.dFrom, filterTabController.tFrom);
        return before;

    }

    /**
     *
     * @return gives current end date
     */
    public LocalDateTime getEnd(){
        LocalDateTime after = LocalDateTime.of(filterTabController.dTo, filterTabController.tTo);
        return after;
    }

    //BOUNCE CONTROLLER
    @FXML
    public void updateBouncePageLabel(){

        bouncePagesLabel.setText(String.valueOf(Math.round(bouncePageSlider.getValue())));
        metrics.setBouncePages((int) Math.round(bouncePageSlider.getValue()));

    }

    @FXML
    public void updateBounceDurationLabel(){

        bounceDurationLabel.setText(String.valueOf(Math.round(bounceDurationSlider.getValue())));
        metrics.setBounceTime(Duration.ofSeconds(Math.round(bounceDurationSlider.getValue())));

    }

    //BOUNCE CONTROLLER

    public JFXTabPane getLHS(){
        return this.LHS;
    }


    public int getGranDigits() {
        return filterTabController.getGranDigit();
    }

    public ChronoUnit getGranUnit() {
        return filterTabController.getGranTimeUnit();
    }
}