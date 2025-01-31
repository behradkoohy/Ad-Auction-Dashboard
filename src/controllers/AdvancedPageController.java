package controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import models.ChartHandler;
import models.HistogramModel;
import models.MetricsModel;
import models.PieChartModel;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class AdvancedPageController {

    //ADVANCED PAGE
    @FXML private LineChart advancedChart;
    @FXML private BarChart histogram;
    @FXML private Label ctrLabel;
    @FXML private Label cpaLabel;
    @FXML private Label cpcLabel;
    @FXML private Label cpmLabel;
    @FXML private Label bounceRateLabel;
    @FXML private PieChart contextPie;

    //HISTOGRAM STUFF
    @FXML private JFXTextField minValue;
    @FXML private JFXTextField maxValue;
    @FXML private JFXTextField bandLength;

    private RootController controller;
    private PieChartModel pieChartModel;
    private MetricsModel metricsModel;
    private ChartHandler chartHandler;
    private HistogramModel histogramModel;

    private boolean ctr;
    private boolean cpa;
    private boolean cpc;
    private boolean cpm;
    private boolean bounceRate;

    public void init(RootController controller) {
        this.controller = controller;
        this.metricsModel = new MetricsModel();
        this.pieChartModel = new PieChartModel();
        this.chartHandler = new ChartHandler();
        this.chartHandler.setMetricsModel(metricsModel);
        this.histogramModel = new HistogramModel();
    }

    @FXML
    public void initialize() {
        advancedChart.setAnimated(false);

        //Update fxml too!!!
        this.ctr = true;
        this.cpa = false;
        this.cpc = false;
        this.cpm = false;
        this.bounceRate = false;

        /* set some styling properties for the histogram */
        this.controller.doGUITask(() -> {
            histogram.getData().clear();
            histogram.setLegendVisible(false);
            histogram.setAnimated(false);
            histogram.setBarGap(0);
            histogram.setCategoryGap(0);
        });

    }

    public void updateData(){
        String campaignName = controller.getCurrentCampaign();

        LocalDateTime start = controller.getPeriodStart();
        LocalDateTime end = controller.getPeriodEnd();
        Duration dur = controller.calcDuration();

        metricsModel.setCampaign(campaignName);
        pieChartModel.setCampaign(campaignName);
        pieChartModel.setStart(start);
        pieChartModel.setEnd(end);
        metricsModel.setFilter(controller.getMainFilter());

        String ctrStr = String.valueOf(RootController.to2DP(metricsModel.getCTR(start, end)));
        String cpaStr = String.valueOf(RootController.to2DP(metricsModel.getCPA(start, end)));
        String cpcStr = String.valueOf(RootController.to2DP(metricsModel.getCPC(start, end)));
        String cpmStr = String.valueOf(RootController.to2DP(metricsModel.getCPM(start, end)));
        String bounceRateStr = String.valueOf(RootController.to2DP(metricsModel.getBounceRate(start, end)));

        this.updateLabels(ctrStr, cpaStr, cpcStr,  cpmStr, bounceRateStr);

        HashMap<String, Integer> pieChartData = pieChartModel.getContextDistributions();
        List<PieChart.Data> contextPieData = pieChartModel.getContextPieData(pieChartData.get("blog"), pieChartData.get("news"),
                pieChartData.get("socialmedia"), pieChartData.get("shopping"), pieChartData.get("hobbies"),
                pieChartData.get("travel"));

        updateContextPieChart(contextPieData);

        updateAdvancedChart();

        this.histogramModel.setCampaign(campaignName);
        HashMap<Double, Integer> barChartData = this.histogramModel.getData(0, null, 1);
        updateHistogram(barChartData);

    }

    private void updateAdvancedChart() {
        List<XYChart.Series> newChartData = chartHandler.getAdvancedChartDataAccordingTo(controller.getCurrentCampaign(),
                controller.getPeriodStart(), controller.getPeriodEnd(), controller.calcDuration(), ctr, cpa,
                cpc, cpm, bounceRate);

        updateAdvancedChartGUI(newChartData);
    }

    private void updateLabels(String ctr, String cpa, String cpc, String cpm, String bounceRate){

        this.controller.doGUITask(() -> {
            ctrLabel.setText(ctr);
            cpaLabel.setText(cpa);
            cpcLabel.setText(cpc);
            cpmLabel.setText(cpm);
            bounceRateLabel.setText(bounceRate);
        });
    }



    /**
     * Updates the context pie on the advanced stats page
     * @param contextData
     */
    public void updateContextPieChart(List<PieChart.Data> contextData){

        this.controller.doGUITask(() -> {

            contextPie.getData().clear();
            contextPie.getData().addAll(contextData);

        });

    }

    /**
     * Make the advanced line chart show the new data
     *
     * @param data
     */
    public void updateAdvancedChartGUI(List<XYChart.Series> data){

        this.controller.doGUITask(() -> {

            advancedChart.getData().clear();

            for(XYChart.Series s: data){

                advancedChart.getData().add(s);

            }

            advancedChart.getXAxis().setLabel(controller.calcMetric());


        });

    }

    /**
     * Update the histogram to show the new data specified,
     * note that the histogram only ever has one series of data
     */
    public void updateHistogram(HashMap<Double, Integer> data){

        XYChart.Series series = new XYChart.Series();
        List<Double> keys = new ArrayList<Double>(data.keySet());
        Collections.sort(keys);

        for(double categoryIndex : keys){
            series.getData().add(new XYChart.Data(String.valueOf(categoryIndex), data.get(categoryIndex)));
        }

        this.controller.doGUITask(() -> {

            updateHistogramGraphics();
            histogram.getData().clear();
            histogram.getData().add(series);

        });

    }

    private void updateAdvancedChartWithLoader() {
        new Thread(() -> {
            controller.startLoadingIndicator();
            this.updateAdvancedChart();
            controller.endLoadingIndicator();
        }).start();
    }

    public MetricsModel getMetricsModel() {

        return metricsModel;

    }
    /* Histogram helper method */

    public void updateHistogramGraphics(){
        histogram.setLegendVisible(false);
        histogram.setAnimated(false);
        histogram.setBarGap(0);
        histogram.setCategoryGap(0);
    }

    @FXML
    /**
     * Histogram method
     */
    public void refreshData(){
        try{
            double minCost = Double.valueOf(minValue.getText());
            Double maxCost = Double.valueOf(maxValue.getText());
            double bandLengthValue = Double.valueOf(bandLength.getText());

            HashMap<Double, Integer> barChartData = this.histogramModel.getData(minCost, maxCost, bandLengthValue);
            updateHistogram(barChartData);

        }catch(NumberFormatException e){
            this.controller.error("Please only input numbers");
        }
    }

    @FXML
    /**
     *Histogram method
     */
    public void redrawDefault(){
        HashMap<Double, Integer> barChartData = this.histogramModel.getData(0, null, 1);
        updateHistogram(barChartData);

        this.controller.doGUITask(() -> {

            minValue.setText("");
            maxValue.setText("");
            bandLength.setText("");

        });
    }

    @FXML
    public void toggleCTR(){

        ctr = !ctr;
        updateAdvancedChartWithLoader();

    }

    @FXML
    public void toggleCPA(){

        cpa = !cpa;
        updateAdvancedChartWithLoader();

    }

    @FXML
    public void toggleCPC(){

        cpc = !cpc;
        updateAdvancedChartWithLoader();

    }

    @FXML
    public void toggleCPM(){

        cpm = !cpm;
        updateAdvancedChartWithLoader();

    }

    @FXML
    public void toggleBounceRate(){

        bounceRate = !bounceRate;
        updateAdvancedChartWithLoader();

    }

}