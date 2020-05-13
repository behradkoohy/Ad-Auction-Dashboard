package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import models.ChartHandler;
import models.MetricsModel;
import models.PieChartModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BasicPageController {

    @FXML private LineChart basicChart;
    @FXML private PieChart genderPie;
    @FXML private PieChart agePie;
    @FXML private PieChart incomePie;
    @FXML private Label impressionsLabel;
    @FXML private Label clicksLabel;
    @FXML private Label uniquesLabel;
    @FXML private Label bouncesLabel;
    @FXML private Label conversionsLabel;
    @FXML private Label totCostLabel;

    private RootController controller;
    private PieChartModel pieChartModel;
    private MetricsModel metricsModel;
    private ChartHandler chartHandler;

    private boolean impressions;
    private boolean conversions;
    private boolean clicks;
    private boolean uniques;
    private boolean bounces;

    public void init(RootController controller) {
        this.controller = controller;
        this.metricsModel = new MetricsModel();
        this.pieChartModel = new PieChartModel();
        this.chartHandler = new ChartHandler();
        this.chartHandler.setMetricsModel(metricsModel);
    }

    @FXML
    public void initialize() {

        basicChart.setAnimated(false);
        //Change this in the fxml file to match, too lazy to auto do it
        this.impressions = true;
        this.conversions = false;
        this.clicks = false;
        this.uniques = false;
        this.bounces = false;

    }

    /**
     * Called by the root controller, calls all relevant methods to update
     * all information shown on the basic page
     */
    public void updateData(){
        String campaignName = controller.getCurrentCampaign();

        LocalDateTime start = controller.getPeriodStart();
        LocalDateTime end = controller.getPeriodEnd();

        metricsModel.setCampaign(campaignName);
        pieChartModel.setCampaign(campaignName);
        pieChartModel.setStart(start);
        pieChartModel.setEnd(end);
        metricsModel.setFilter(controller.getFilter());

        System.out.println(metricsModel.getNumImpressions(start, end));
        String numImpressionsStr = String.valueOf(String.valueOf(RootController.to2DP(
                metricsModel.getNumImpressions(start, end))));
        String numClicksStr = String.valueOf(RootController.to2DP(metricsModel.getNumClicks(start, end)));
        String numUniqueStr = String.valueOf(RootController.to2DP(metricsModel.getNumUniqs(start, end)));
        String numBouncesStr = String.valueOf(RootController.to2DP(metricsModel.getNumBounces(start, end)));
        String numConversionsStr = String.valueOf(RootController.to2DP(metricsModel.getConversions(start, end)));
        String totalCostStr = String.valueOf(RootController.to2DP(metricsModel.getTotalCost(start, end)));

        this.updateLabels(numImpressionsStr, numClicksStr, numUniqueStr, numBouncesStr, numConversionsStr, totalCostStr);

        HashMap<String, Integer> pieChartData = pieChartModel.getDistributions("impressions");
        List<PieChart.Data> genderPieData = pieChartModel.getGenderPieData(pieChartData.get("men"), pieChartData.get("women"));
        List<PieChart.Data> agePieData = pieChartModel.getAgePieData(pieChartData.get("lt25"), pieChartData.get("btwn2534"),
                pieChartData.get("btwn3544"), pieChartData.get("btwn4554"), pieChartData.get("gt55"));
        List<PieChart.Data> incomePieData = pieChartModel.getIncomePieData(pieChartData.get("low"), pieChartData.get("medium"),
                pieChartData.get("high"));

        updateBasicPieCharts(genderPieData, agePieData, incomePieData);

        updateBasicChart();
    }

    private void updateBasicChart() {
        List<XYChart.Series> newChartData = chartHandler.getBasicChartDataAccordingTo(controller.getCurrentCampaign(),
                controller.getPeriodStart(), controller.getPeriodEnd(), controller.calcDuration(), impressions,
                conversions, clicks, uniques, bounces);

        updateBasicChartGUI(newChartData);
    }

    private void updateLabels(String impressions, String clicks, String uniques, String bounces, String conversions,
                              String totCost) {

        this.controller.doGUITask(() -> {
            impressionsLabel.setText(impressions);
            clicksLabel.setText(clicks);
            uniquesLabel.setText(uniques);
            bouncesLabel.setText(bounces);
            conversionsLabel.setText(conversions);
            totCostLabel.setText(totCost);
        });
    }

    private void updateBasicPieCharts(List<PieChart.Data> genderData,
                                     List<PieChart.Data> ageData,
                                     List<PieChart.Data> incomeData){

        this.controller.doGUITask(() -> {

            genderPie.getData().clear();
            agePie.getData().clear();
            incomePie.getData().clear();
            genderPie.getData().addAll(genderData);
            agePie.getData().addAll(ageData);
            incomePie.getData().addAll(incomeData);

        });

    }



    /**
     * Update the basic chart to show the given data
     * @param data
     */
    private void updateBasicChartGUI(List<XYChart.Series> data){

        this.controller.doGUITask(() -> {

            basicChart.getData().clear();

            for(XYChart.Series s: data){

                basicChart.getData().add(s);

            }

            basicChart.getXAxis().setLabel(controller.calcMetric());

        });

    }

    private void updateBasicChartWithLoader() {
        new Thread(() -> {
            controller.startLoadingIndicator();
            this.updateBasicChart();
            controller.endLoadingIndicator();
        }).start();
    }

    public MetricsModel getMetricsModel(){

        return metricsModel;

    }

    @FXML
    public void toggleImpressions(){

        impressions = !impressions;
        updateBasicChartWithLoader();
    }

    @FXML
    public void toggleConversions(){

        conversions = !conversions;
        updateBasicChartWithLoader();
    }

    @FXML
    public void toggleClicks(){

        clicks = !clicks;
        updateBasicChartWithLoader();
    }

    @FXML
    public void toggleUniques(){

        uniques = !uniques;
        updateBasicChartWithLoader();
    }

    @FXML
    public void toggleBounces(){

        bounces = !bounces;
        updateBasicChartWithLoader();

    }

}