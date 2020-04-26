package controllers;

import com.jfoenix.controls.*;
import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.Impression;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import models.HistogramModel;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;


import controllers.*;
import models.Metrics;

public class Controller {
    /*
    Corresponding UI components that can be found in scene builder with these identifiers,
    this is what the "@FXML" annotation means

    When the application starts the FXML loader will inject the actual
    UI component references in scene builder with these variables
     */
    @FXML private JFXTabPane LHS;

    @FXML AnchorPane campaignTab;
    @FXML AnchorPane statisticsTab;
    @FXML AnchorPane histogramTab;
    @FXML AnchorPane graphsTab;
    @FXML AnchorPane filterTab;
    @FXML AnchorPane accessibilityTab;

    @FXML private CampaignTabController campaignTabController;
    @FXML private StatisticsTabController statisticsTabController;
    @FXML private HistogramTabController histogramTabController;
    @FXML private GraphsTabController graphsTabController;
    @FXML private FilterTabController filterTabController;
    @FXML private AccessibilityTabController accessibilityTabController;

    //The number of "units" that will be displayed along the x axis
    public int unitsDifference;

    //BOUNCE CONTROLLER
    @FXML private Label bouncePagesLabel;
    @FXML private Label bounceDurationLabel;
    @FXML private JFXSlider bouncePageSlider;
    @FXML private JFXSlider bounceDurationSlider;
    //BOUNCE CONTROLLER

    private Metrics metrics;

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

    }

    /**
    * General error / success functions
    * */
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

    /**
     * Used to pass data to the other controllers when something changes
     *
     *
     * */
    public void loadCampaignData(String campaignName){
        statisticsTabController.loadData(campaignName);
        histogramTabController.loadData(campaignName);
        graphsTabController.loadData(campaignName);
    }



    /**
     * Used by the campaign manager to go to next page after loading/creating a campaign
     */
    public void goToMainPage(){
        SingleSelectionModel<Tab> model = LHS.getSelectionModel();
        model.select(1);
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


    /*
     *
     * @return gives current start date

    public LocalDateTime getStart(){
        LocalDateTime before = LocalDateTime.of(filterTabController.dFrom, filterTabController.tFrom);
        return before;

    }

    
     *
     * @return gives current end date

    public LocalDateTime getEnd(){
        LocalDateTime after = LocalDateTime.of(filterTabController.dTo, filterTabController.tTo);
        return after;
    }


     * Triggered from the FilterTabController via the "reload data"
     * button. This method is responsible for updating the data on the
     * stats page and the graph according to the filters just specified
     * in the filter tab controller

    public void reloadData(){


        Can use getCheckBoxVals and getDateTime methods in filter
        tab controller to get the filter information required


    }*/

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

}