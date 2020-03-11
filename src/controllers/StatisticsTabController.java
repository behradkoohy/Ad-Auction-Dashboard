package controllers;
import java.util.HashMap;
import java.util.List;

import entities.Impression;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;


import models.Metrics;
import models.PieChartModel;


public class StatisticsTabController{
    private Controller controller;
    private Metrics metricsModel;
    private PieChartModel pieChartModel;

    //This can be changed each time the user switches to a new/different campaign
    @FXML private Label statsCampaignNameLabel;
    @FXML private Label numImpressions;
    @FXML private Label numClicks;
    @FXML private Label numUnique;
    @FXML private Label numBounces;
    @FXML private Label numConversions;
    @FXML private Label totalCost;
    @FXML private Label CTR;
    @FXML private Label CPA;
    @FXML private Label CPC;
    @FXML private Label CPM;
    @FXML private Label bounceRate;
    @FXML private PieChart genderPie;
    @FXML private PieChart agePie;
    @FXML private PieChart incomePie;

    public void init(Controller controller){
        this.controller = controller;
        this.metricsModel = new Metrics();
    }

    public void initialize(){
        //Setting up the look of the pie charts
        genderPie.setTitle("Gender");
        genderPie.setLegendVisible(false);
        genderPie.setStyle("-fx-font-size: " + 10 + "px;");
        agePie.setTitle("Age");
        agePie.setLegendVisible(false);
        agePie.setStyle("-fx-font-size: " + 10 + "px;");
        incomePie.setTitle("Income");
        incomePie.setLegendVisible(false);
        incomePie.setStyle("-fx-font-size: " + 10 + "px;");
    }

    public void loadData(String campaignName) {
        //TODO very messy, just have each model fetch the data once

        statsCampaignNameLabel.setText(campaignName);
        Double nrImpressions = this.metricsModel.getNumImpressions(campaignName);
        List<Impression> impressions = this.metricsModel.getImpressions(campaignName);
        numImpressions.setText(String.valueOf(nrImpressions));
        numClicks.setText(String.valueOf(this.metricsModel.getNumClicks(campaignName)));
        numUnique.setText(String.valueOf(this.metricsModel.getNumUniqs(campaignName)));
        numBounces.setText(String.valueOf(this.metricsModel.getNumBounces(campaignName)));
        numConversions.setText(String.valueOf(this.metricsModel.getConversions(campaignName)));
        totalCost.setText(String.valueOf(this.metricsModel.getTotalCost(campaignName)));
        CTR.setText(String.valueOf(this.metricsModel.getCTR(campaignName)));
        CPA.setText(String.valueOf(this.metricsModel.getCPA(campaignName)));
        CPC.setText(String.valueOf(this.metricsModel.getCPC(campaignName)));
        CPM.setText(String.valueOf(this.metricsModel.getCPM(campaignName)));
        bounceRate.setText(String.valueOf(this.metricsModel.getBounceRate(campaignName)));

        this.pieChartModel = new PieChartModel(campaignName, impressions);
        HashMap<String, Integer> pieChartData =  this.pieChartModel.getDistributions();

        updatePieChartData(
                pieChartData.get("men"), pieChartData.get("women"),
                pieChartData.get("lt25"), pieChartData.get("btwn2534"), pieChartData.get("btwn3544"), pieChartData.get("btwn4554"), pieChartData.get("gt55"),
                pieChartData.get("low"), pieChartData.get("medium"), pieChartData.get("high")
        );

    }

    /**

     * Update the pie charts to show that some data has changed
     *
     * All values are the number of users there are for each attribute
     *

     * @param men
     * @param women
     * @param lt25
     * @param btwn2534
     * @param btwn3544
     * @param btwn4554
     * @param gt55
     * @param lowIncome
     * @param medIncome
     * @param highIncome
     */
    public void updatePieChartData(int men, int women, int lt25,
                                   int btwn2534, int btwn3544,
                                   int btwn4554, int gt55, int lowIncome,
                                   int medIncome, int highIncome){


        genderPie.getData().clear();
        agePie.getData().clear();
        incomePie.getData().clear();

        PieChart.Data gender1 = new PieChart.Data("Men", men);
        PieChart.Data gender2 = new PieChart.Data("Women", women);
        genderPie.getData().addAll(gender1, gender2);

        PieChart.Data age1 = new PieChart.Data("<25", lt25);
        PieChart.Data age2 = new PieChart.Data("25-34", btwn2534);
        PieChart.Data age3 = new PieChart.Data("35-44", btwn3544);
        PieChart.Data age4 = new PieChart.Data("45-54", btwn4554);
        PieChart.Data age5 = new PieChart.Data(">54", gt55);
        agePie.getData().addAll(age1, age2, age3, age4, age5);

        PieChart.Data income1 = new PieChart.Data("Low", lowIncome);
        PieChart.Data income2 = new PieChart.Data("Medium", medIncome);
        PieChart.Data income3 = new PieChart.Data("High", highIncome);
        incomePie.getData().addAll(income1, income2, income3);

    }

}
