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
    }

    @FXML
    public void initialize() {
        //Do any setup of styling here
    }

    /**
     * Called by the root controller, calls all relevant methods to update
     * all information shown on the basic page
     */
    public void updateData(String campaignName){

        if(chartHandler == null){
            chartHandler = new ChartHandler(metricsModel);
        }

        LocalDateTime start = controller.getPeriodStart();
        LocalDateTime end = controller.getPeriodEnd();
        Duration dur = controller.calcDuration();

        metricsModel.setCampaign(campaignName);
        System.out.println(metricsModel.getNumImpressions(start, end));
        String numImpressionsStr = String.valueOf(String.valueOf(RootController.to2DP(
                metricsModel.getNumImpressions(start, end))));
        String numClicksStr = String.valueOf(RootController.to2DP(metricsModel.getNumClicks(start, end)));
        String numUniqueStr = String.valueOf(RootController.to2DP(metricsModel.getNumUniqs(start, end)));
        String numBouncesStr = String.valueOf(RootController.to2DP(metricsModel.getNumBounces(start, end)));
        String numConversionsStr = String.valueOf(RootController.to2DP(metricsModel.getConversions(start, end)));
        String totalCostStr = String.valueOf(RootController.to2DP(metricsModel.getTotalCost(start, end)));

        this.updateLabels(numImpressionsStr, numClicksStr, numUniqueStr, numBouncesStr, numConversionsStr, totalCostStr);

        pieChartModel.setCampaign(campaignName);
        pieChartModel.setStart(start);
        pieChartModel.setEnd(end);
        HashMap<String, Integer> pieChartData = pieChartModel.getDistributions();
        List<PieChart.Data> genderPieData = getGenderPieData(pieChartData.get("men"), pieChartData.get("women"));
        List<PieChart.Data> agePieData = getAgePieData(pieChartData.get("lt25"), pieChartData.get("btwn2534"),
                pieChartData.get("btwn3544"), pieChartData.get("btwn4554"), pieChartData.get("gt55"));
        List<PieChart.Data> incomePieData = getIncomePieData(pieChartData.get("low"), pieChartData.get("medium"),
                pieChartData.get("high"));

        updateBasicPieCharts(genderPieData, agePieData, incomePieData);

        List<XYChart.Series> newChartData = chartHandler.getBasicChartDataAccordingTo(campaignName, start, end,
                dur, impressions, conversions, clicks, uniques, bounces);
        System.out.println(newChartData);
        updateBasicGraph(newChartData);

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

    private List<PieChart.Data> getGenderPieData(int men, int women){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        out.add(new PieChart.Data("Men", men));
        out.add(new PieChart.Data("Women", women));
        return out;

    }

    private List<PieChart.Data> getAgePieData(int lt25, int btwn2534, int btwn3544,
                                             int btwn4554, int gt55){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        String[] tags = new String[]{"<25","25-34","35-44","45-54",">55"};
        int[] vals = new int[]{lt25, btwn2534,btwn3544,btwn4554,gt55};

        for(int i = 0; i < tags.length; i++){
            out.add(new PieChart.Data(tags[i], vals[i]));
        }

        return out;
    }

    private List<PieChart.Data> getIncomePieData(int high, int medium, int low){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        out.add(new PieChart.Data("High", high));
        out.add(new PieChart.Data("Medium", medium));
        out.add(new PieChart.Data("Low", low));
        return out;

    }

    /**
     * Update the basic chart to show the given data
     * @param data
     */
    private void updateBasicGraph(List<XYChart.Series> data){

        this.controller.doGUITask(() -> {

            basicChart.getData().clear();

            for(XYChart.Series s: data){

                basicChart.getData().add(s);

            }

            basicChart.getXAxis().setLabel(controller.calcMetric());

        });

    }

    @FXML
    public void toggleImpressions(){

        impressions = !impressions;

    }

    @FXML
    public void toggleConversions(){

        conversions = !conversions;

    }

    @FXML
    public void toggleClicks(){

        clicks = !clicks;

    }

    @FXML
    public void toggleUniques(){

        uniques = !uniques;

    }

    @FXML
    public void toggleBounces(){

        bounces = !bounces;

    }

}