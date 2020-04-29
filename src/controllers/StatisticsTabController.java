package controllers;

import javafx.application.Platform;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import models.Metrics;
import models.PieChartModel;

import java.time.LocalDateTime;
import java.util.*;


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
    @FXML private PieChart contextPie;

    private boolean firstLoad;

    public void init(Controller controller){
        this.controller = controller;
        this.metricsModel = this.controller.getMetrics();
        this.pieChartModel = new PieChartModel();
    }

    @FXML
    public void initialize(){

        firstLoad = true;

        //Setting up the look of the pie charts
        genderPie.setTitle("Gender");
        genderPie.setLegendVisible(false);
        genderPie.setStyle("-fx-font-size: 10");
        agePie.setTitle("Age");
        agePie.setLegendVisible(false);
        agePie.setStyle("-fx-font-size: 10");
        incomePie.setTitle("Income");
        incomePie.setLegendVisible(false);
        incomePie.setStyle("-fx-font-size: 10");
        contextPie.setTitle("Context");
        contextPie.setLegendVisible(false);
        contextPie.setStyle("-fx-font-size: 10");

    }

    public void setFirstLoad(){

        firstLoad = false;

    }

    public void initialize(Metrics metricsModel){

        this.metricsModel = metricsModel;

    }

    public void loadData(String campaignName) {
        //TODO very messy, just have each model fetch the data once

        /*
        if(!firstLoad){

            controller.startLoadingIndicator();

        }*/

        this.metricsModel.setCampaign(campaignName);

        LocalDateTime start = this.controller.getStart();
        LocalDateTime end = this.controller.getEnd();

        String numImpressionsStr = String.valueOf(String.valueOf(to2DP(this.metricsModel.getNumImpressions(start, end))));
        String numClicksStr = String.valueOf(to2DP(this.metricsModel.getNumClicks(start, end)));
        String numUniqueStr = String.valueOf(to2DP(this.metricsModel.getNumUniqs(start, end)));
        String numBouncesStr = String.valueOf(to2DP(this.metricsModel.getNumBounces(start, end)));
        String numConversionsStr = String.valueOf(to2DP(this.metricsModel.getConversions(start, end)));
        String totalCostStr = String.valueOf(to2DP(this.metricsModel.getTotalCost(start, end)));
        String CTRstr = String.valueOf(to2DP(this.metricsModel.getCTR(start, end)));
        String CPAstr = String.valueOf(to2DP(this.metricsModel.getCPA(start, end)));
        String CPCstr = String.valueOf(to2DP(this.metricsModel.getCPC(start, end)));
        String CPMstr = String.valueOf(to2DP(this.metricsModel.getCPM(start, end)));
        String bounceRateStr = String.valueOf(to2DP(this.metricsModel.getBounceRate(start, end)));

        this.pieChartModel.setCampaign(campaignName);
        this.pieChartModel.setStart(this.controller.getStart());
        this.pieChartModel.setEnd(this.controller.getEnd());
        HashMap<String, Integer> pieChartData =  this.pieChartModel.getDistributions();

        //ADD CONTEXT DATA
        List<List<PieChart.Data>> newPieChartData = getNewPieChartData(
                pieChartData.get("men"), pieChartData.get("women"),
                pieChartData.get("lt25"), pieChartData.get("btwn2534"), pieChartData.get("btwn3544"), pieChartData.get("btwn4554"), pieChartData.get("gt55"),
                pieChartData.get("low"), pieChartData.get("medium"), pieChartData.get("high")
        );

        populateLabelsEXT(campaignName, numImpressionsStr, numClicksStr, numUniqueStr, numBouncesStr, numConversionsStr,
                totalCostStr, CTRstr, CPAstr, CPCstr, CPMstr, bounceRateStr);

        //Possibly a bit of a hardcoding/hacky way of doing it but its fine for now
        populatePieChartsEXT(newPieChartData.get(0), newPieChartData.get(1), newPieChartData.get(2), newPieChartData.get(3));

        /*
        if(!firstLoad){

            controller.endLoadingIndicator();

        }*/

    }

    /**
     * Lets a thread an external thread call this GUI method
     */
    public void populateLabelsEXT(String campaignName, String numImpressions, String numClicks, String numUnique,
                                  String numBounces, String numConversions, String totalCost, String CTR, String CPA,
                                  String CPC, String CPM, String bounceRate){

        if(Platform.isFxApplicationThread()){

            populateLabels(campaignName, numImpressions, numClicks, numUnique, numBounces, numConversions, totalCost,
                    CTR, CPA, CPC, CPM, bounceRate);

        } else {

            Platform.runLater(() -> populateLabels(campaignName, numImpressions, numClicks, numUnique, numBounces,
                    numConversions, totalCost, CTR, CPA, CPC, CPM, bounceRate));

        }

    }

    /**
     * Updates all the labels with the values of the strings given
     *
     * @param numImpressions
     * @param numClicks
     * @param numUnique
     * @param numBounces
     * @param numConversions
     * @param totalCost
     * @param CTR
     * @param CPA
     * @param CPC
     * @param CPM
     */
    private void populateLabels(String campaignName, String numImpressions, String numClicks, String numUnique,
                               String numBounces, String numConversions, String totalCost, String CTR, String CPA,
                               String CPC, String CPM, String bounceRate){

        try {

            //Always make sure that GUI update methods are being called on the application thread
            controller.verifyIsFXThread("populateLabels");

        } catch (Exception e){

            controller.error(e.getMessage());
            return;

        }

        this.statsCampaignNameLabel.setText(campaignName);
        this.numImpressions.setText(numImpressions);
        this.numClicks.setText(numClicks);
        this.numUnique.setText(numUnique);
        this.numBounces.setText(numBounces);
        this.numConversions.setText(numConversions);
        this.totalCost.setText(totalCost);
        this.CTR.setText(CTR);
        this.CPA.setText(CPA);
        this.CPC.setText(CPC);
        this.CPM.setText(CPM);
        this.bounceRate.setText(bounceRate);

    }

    /**
     * Allows updating the pie charts from an external thread
     *
     * @param genderData
     * @param ageData
     * @param incomeData
     * @param contextData
     */
    public void populatePieChartsEXT(List<PieChart.Data> genderData, List<PieChart.Data> ageData,
                                     List<PieChart.Data> incomeData, List<PieChart.Data> contextData){

        if(Platform.isFxApplicationThread()){

            populatePieCharts(genderData, ageData, incomeData, contextData);

        } else {

            Platform.runLater(() -> populatePieCharts(genderData, ageData, incomeData, contextData));

        }

    }

    /**
     * Updates pie charts with the new data specified
     * @param genderData
     * @param ageData
     * @param incomeData
     * @param contextData
     */
    private void populatePieCharts(List<PieChart.Data> genderData, List<PieChart.Data> ageData,
                                  List<PieChart.Data> incomeData, List<PieChart.Data> contextData) {

        try {

            controller.verifyIsFXThread("populatePieCharts");

        } catch (Exception e){

            controller.error(e.getMessage());
            return;

        }

        genderPie.getData().clear();
        genderPie.getData().addAll(genderData);
        agePie.getData().clear();
        agePie.getData().addAll(ageData);
        incomePie.getData().clear();
        incomePie.getData().addAll(incomeData);
        contextPie.getData().clear();
        contextPie.getData().addAll(contextData);

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
    public List<List<PieChart.Data>> getNewPieChartData(int men, int women, int lt25,
                                   int btwn2534, int btwn3544,
                                   int btwn4554, int gt55, int lowIncome,
                                   int medIncome, int highIncome){

        List out = new ArrayList<List<PieChart.Data>>();

        //ADD CONTEXT DATA

        PieChart.Data gender1 = new PieChart.Data("Men", men);
        PieChart.Data gender2 = new PieChart.Data("Women", women);
        List<PieChart.Data> genderData = Arrays.asList(new PieChart.Data[]{gender1, gender2});

        PieChart.Data age1 = new PieChart.Data("<25", lt25);
        PieChart.Data age2 = new PieChart.Data("25-34", btwn2534);
        PieChart.Data age3 = new PieChart.Data("35-44", btwn3544);
        PieChart.Data age4 = new PieChart.Data("45-54", btwn4554);
        PieChart.Data age5 = new PieChart.Data(">54", gt55);
        List<PieChart.Data> ageData = Arrays.asList(new PieChart.Data[]{age1, age2, age3, age4, age5});

        PieChart.Data income1 = new PieChart.Data("Low", lowIncome);
        PieChart.Data income2 = new PieChart.Data("Medium", medIncome);
        PieChart.Data income3 = new PieChart.Data("High", highIncome);
        List<PieChart.Data> incomeData = Arrays.asList(new PieChart.Data[]{income1, income2, income3});

        //TODO FOR TESTING CHANGE TO ACTUAL CONTEXT DATA
        Random r = new Random();

        //Fill with random data for now just to show where it is on UI
        PieChart.Data context1 = new PieChart.Data("News", r.nextInt(10));
        PieChart.Data context2 = new PieChart.Data("Shopping", r.nextInt(10));
        PieChart.Data context3 = new PieChart.Data("Social Media", r.nextInt(10));
        PieChart.Data context4 = new PieChart.Data("Blog", r.nextInt(10));
        PieChart.Data context5 = new PieChart.Data("Hobbies", r.nextInt(10));
        PieChart.Data context6 = new PieChart.Data("Travel", r.nextInt(10));
        List<PieChart.Data> contextData = Arrays.asList(new PieChart.Data[]{context1, context2, context3,
                context4, context5, context6});

        out.add(genderData);
        out.add(ageData);
        out.add(incomeData);
        out.add(contextData);

        return out;

    }



    //TODO for use with JUNIT test
    public Metrics getMetrics(){

        return metricsModel;

    }

    //TODO for use with JUNIT test
    public String[] getLabelVals(){

        return new String[]{numImpressions.getText(),
        numClicks.getText(), numUnique.getText(), numBounces.getText(),
        numConversions.getText(), totalCost.getText(), CTR.getText(),
        CPA.getText(), CPC.getText(), CPM.getText(), bounceRate.getText()};

    }

    //TODO for use with JUNIT test
    public Map<String, Integer> getPieChartModelData(){

        return this.pieChartModel.getDistributions();

    }

    //TODO for use with JUNIT test
    public List[] getPieChartData(){

        return new List[]{genderPie.getData(), agePie.getData(),
        incomePie.getData(), contextPie.getData()};

    }

}
