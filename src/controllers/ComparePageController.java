package controllers;

import com.jfoenix.controls.JFXComboBox;
import daos.DaoInjector;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import models.ChartHandler;
import models.MetricsModel;
import javafx.scene.paint.Color;

import java.time.LocalDateTime;
import java.util.List;

public class ComparePageController {

    @FXML private JFXComboBox secondCampaignComboBox;
    @FXML private LineChart firstChart;
    @FXML private LineChart secondChart;

    //Labels for each campaign in the comparison grid
    @FXML private Label firstNumImpressionsLabel;
    @FXML private Label firstNumClicksLabel;
    @FXML private Label firstNumUniquesLabel;
    @FXML private Label firstNumBouncesLabel;
    @FXML private Label firstNumConversionsLabel;
    @FXML private Label firstTotCostLabel;
    @FXML private Label firstCtrLabel;
    @FXML private Label firstCpaLabel;
    @FXML private Label firstCpcLabel;
    @FXML private Label firstCpmLabel;
    @FXML private Label firstBounceRateLabel;
    @FXML private Label secondNumImpressionsLabel;
    @FXML private Label secondNumClicksLabel;
    @FXML private Label secondNumUniquesLabel;
    @FXML private Label secondNumBouncesLabel;
    @FXML private Label secondNumConversionsLabel;
    @FXML private Label secondTotCostLabel;
    @FXML private Label secondCtrLabel;
    @FXML private Label secondCpaLabel;
    @FXML private Label secondCpcLabel;
    @FXML private Label secondCpmLabel;
    @FXML private Label secondBounceRateLabel;

    @FXML private Label campaignLabel;

    //Checkbox values for the compare page
    private boolean impressions;
    private boolean clicks;
    private boolean uniques;
    private boolean bounces;
    private boolean conversions;
    private boolean totCost;
    private boolean ctr;
    private boolean cpa;
    private boolean cpc;
    private boolean cpm;
    private boolean bounceRate;

    private RootController controller;
    private MetricsModel metricsModelFirst;
    private ChartHandler chartHandlerFirst;
    private MetricsModel metricsModelSecond;
    private ChartHandler chartHandlerSecond;

    public void init(RootController controller) {
        this.controller = controller;
        this.metricsModelFirst = new MetricsModel();
        this.chartHandlerFirst = new ChartHandler();
        this.chartHandlerFirst.setMetricsModel(metricsModelFirst);
        this.metricsModelSecond = new MetricsModel();
        this.chartHandlerSecond = new ChartHandler();
        this.chartHandlerSecond.setMetricsModel(metricsModelSecond);

        this.loadOtherCampaignsCombo();
    }

    @FXML
    public void initialize(){
        firstChart.setAnimated(false);
        secondChart.setAnimated(false);
        impressions = true;
        clicks = true;
        uniques = true;
        bounces = true;
        conversions = true;
        totCost = true;
        ctr = true;
        cpa = true;
        cpc = true;
        cpm = true;
        bounceRate = true;

    }

    /**
     * Does nothing for now until compare page is implemented
     */
    public void updateData() {
        String campaignNameFirst = controller.getCurrentCampaign();
        String campaignNameSecond = "";
        if(secondCampaignComboBox.getValue() != null) {
             campaignNameSecond = secondCampaignComboBox.getValue().toString();
        }

        metricsModelFirst.setCampaign(campaignNameFirst);
        metricsModelFirst.setFilter(controller.getFilter());
        metricsModelSecond.setCampaign(campaignNameSecond);
        metricsModelSecond.setFilter(controller.getFilter());

        updateFirstChartGUI(getChartXYData(campaignNameFirst, chartHandlerFirst));
        updateSecondChartGUI(getChartXYData(campaignNameSecond, chartHandlerSecond));

    }

    private List<XYChart.Series> getChartXYData(String campaignName, ChartHandler chartHandler) {
        List<XYChart.Series> simpleChartData = chartHandler.getBasicChartDataAccordingTo(campaignName,
                controller.getPeriodStart(), controller.getPeriodEnd(), controller.calcDuration(), impressions,
                conversions, clicks, uniques, bounces);
        List<XYChart.Series> advancedChartData = chartHandler.getAdvancedChartDataAccordingTo(campaignName,
                controller.getPeriodStart(), controller.getPeriodEnd(), controller.calcDuration(), ctr, cpa,
                cpc, cpm, bounceRate);

        simpleChartData.addAll(advancedChartData);
        return simpleChartData;
    }

    private void updateFirstChartGUI(List<XYChart.Series> data){

        this.controller.doGUITask(() -> {

            firstChart.getData().clear();

            for(XYChart.Series s: data){

                firstChart.getData().add(s);

            }

            firstChart.getXAxis().setLabel(controller.calcMetric());

        });

    }

    private void updateSecondChartGUI(List<XYChart.Series> data){

        this.controller.doGUITask(() -> {

            secondChart.getData().clear();

            for(XYChart.Series s: data){

                secondChart.getData().add(s);

            }

            secondChart.getXAxis().setLabel(controller.calcMetric());

        });

    }


    public void setFirstChartLabel(String campaignName) {
        controller.doGUITask(() -> campaignLabel.setText(campaignName));
        controller.doGUITask(() -> campaignLabel.setTextFill(Color.web("#000000")));
    }

    //Called on combobox change
    @FXML
    public void updateSecond() {
        new Thread(() -> {
            controller.startLoadingIndicator();
            this.updateData();
            controller.endLoadingIndicator();
        }).start();
    }

    public void loadOtherCampaignsCombo() {

        new Thread(() -> {

            try {

                List<String> l = DaoInjector.newClickDao().getCampaigns();
                controller.doGUITask(() -> populateCampaignChooser(l));

            } catch (Exception e) {

                System.out.println("No data loaded!");

            }

        }).start();

    }

    private void populateCampaignChooser(List<String> list){

        list.remove(controller.getCurrentCampaign());
        secondCampaignComboBox.getItems().clear();
        secondCampaignComboBox.getItems().addAll(list);

    }

    private void updateDataWithLoader() {
        new Thread(() -> {
            controller.startLoadingIndicator();
            this.updateData();
            controller.endLoadingIndicator();
        }).start();
    }

    @FXML
    public void toggleImpressions(){

        impressions = !impressions;
        updateDataWithLoader();

    }

    @FXML
    public void toggleClicks(){

        clicks = !clicks;
        updateDataWithLoader();

    }

    @FXML
    public void toggleUniques(){

        uniques = !uniques;
        updateDataWithLoader();

    }

    @FXML
    public void toggleBounces(){

        bounces = !bounces;
        updateDataWithLoader();

    }

    @FXML
    public void toggleConversions(){

        conversions = !conversions;
        updateDataWithLoader();

    }

    @FXML
    public void toggleCtr(){

        ctr = !ctr;
        updateDataWithLoader();

    }

    @FXML
    public void toggleCpa(){

        cpa = !cpa;
        updateDataWithLoader();

    }

    @FXML
    public void toggleCpc(){

        cpc = !cpc;
        updateDataWithLoader();

    }

    @FXML
    public void toggleCpm(){

        cpm = !cpm;
        updateDataWithLoader();

    }

    @FXML
    public void toggleBounceRate(){

        bounceRate = !bounceRate;
        updateDataWithLoader();

    }

}