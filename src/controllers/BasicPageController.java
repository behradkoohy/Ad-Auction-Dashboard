package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import models.ChartHandler;
import models.PieChartModel;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

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

    /*
    Again, not sure how handlers and models are
    going to work now we have multiple seperate graphs?
    So I will add a seperate instance for each controller
    at the moment, this can be changed if its not right
     */
    private PieChartModel pieChartModel;
    private ChartHandler chartHandler;

    private boolean impressions;
    private boolean conversions;
    private boolean clicks;
    private boolean uniques;
    private boolean bounces;

    @FXML
    public void initialize(){

        pieChartModel = new PieChartModel();
        //System.out.println("value of root controller: " + ControllerInjector.getRootController());
        //System.out.println("And the value of its metrics: " + ControllerInjector.getRootController().getMetrics());
        //test();
        //impressionsLabel.setText("Testing 123");
    }

    //TODO for testing delete later
    public void test(){

        new Thread(() -> {

            System.out.println("Started test method");
            Random r = new Random();
            List<PieChart.Data> genderData = getGenderPieData(r.nextInt(10),r.nextInt(10));
            System.out.println("Got gender data");
            List<PieChart.Data> ageData = getAgePieData(r.nextInt(10), r.nextInt(10),r.nextInt(10),r.nextInt(10),r.nextInt(10));
            System.out.println("Got age data");
            List<PieChart.Data> incomeData = getIncomePieData(r.nextInt(10), r.nextInt(10), r.nextInt(10));
            System.out.println("Got income data");

            //List<PieChart.Data> contextData = getContextPieData(r.nextInt(10), r.nextInt(10), r.nextInt(10), r.nextInt(10), r.nextInt(10), r.nextInt(10));
            System.out.println("Got context data");

            RootController.doGUITask(()  -> updateBasicPieCharts(genderData, ageData, incomeData));
            System.out.println("Updated pie charts");

            //RootController.doGUITask(() -> updateContextPieChart(contextData));
            System.out.println("Updated context data");

            XYChart.Series basicData = new XYChart.Series();
            XYChart.Series advancedData = new XYChart.Series();
            XYChart.Series lhsData = new XYChart.Series();
            XYChart.Series rhsData = new XYChart.Series();

            for(int i = 1; i <= 10; i++){

                basicData.getData().add(new XYChart.Data(String.valueOf(i), r.nextInt(10)));
                advancedData.getData().add(new XYChart.Data(String.valueOf(i), r.nextInt(10)));
                lhsData.getData().add(new XYChart.Data(String.valueOf(i), r.nextInt(10)));
                rhsData.getData().add(new XYChart.Data(String.valueOf(i), r.nextInt(10)));

            }

            RootController.doGUITask(() -> {

                basicChart.getData().addAll(basicData);
                //advancedChart.getData().addAll(advancedData);
                //firstChart.getData().addAll(lhsData);
                //secondChart.getData().addAll(rhsData);

            });

            System.out.println("ended test method");


        }).start();

    }

    /**
     * Called by the root controller, calls all relevant methods to update
     * all information shown on the basic page
     */
    public void updateData(String campaignName){

        if(chartHandler == null){

            chartHandler = new ChartHandler(ControllerInjector.getRootController().getMetrics());

        }

        LocalDateTime start = ControllerInjector.getRootController().getPeriodStart();
        //System.out.println("start is: " + start.getDayOfYear());
        LocalDateTime end = ControllerInjector.getRootController().getPeriodEnd();
        System.out.println("end is: " + start.getDayOfYear());
        java.time.Duration dur = ControllerInjector.getRootController().calcDuration();

        String numImpressionsStr = String.valueOf(String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getNumImpressions(start, end))));
        String numClicksStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getNumClicks(start, end)));
        String numUniqueStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getNumUniqs(start, end)));
        String numBouncesStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getNumBounces(start, end)));
        String numConversionsStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getConversions(start, end)));
        String totalCostStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getTotalCost(start, end)));

        updateLabels(numImpressionsStr, numClicksStr, numUniqueStr, numBouncesStr,
                numConversionsStr, totalCostStr);

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

        updateBasicGraph(newChartData);

    }

    public void updateLabels(String impressions, String clicks, String uniques,
                             String bounces, String conversions, String totCost){

        RootController.doGUITask(() -> {

            impressionsLabel.setText(impressions);
            clicksLabel.setText(clicks);
            uniquesLabel.setText(uniques);
            bouncesLabel.setText(bounces);
            conversionsLabel.setText(conversions);
            totCostLabel.setText(totCost);

        });

    }

    /**
     * Updates the pie charts on the basic stats page
     * @param genderData
     * @param ageData
     * @param incomeData
     */
    public void updateBasicPieCharts(List<PieChart.Data> genderData,
                                     List<PieChart.Data> ageData,
                                     List<PieChart.Data> incomeData){

        RootController.doGUITask(() -> {

            genderPie.getData().clear();
            agePie.getData().clear();
            incomePie.getData().clear();
            genderPie.getData().addAll(genderData);
            agePie.getData().addAll(ageData);
            incomePie.getData().addAll(incomeData);

        });

    }

    public List<PieChart.Data> getGenderPieData(int men, int women){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        out.add(new PieChart.Data("Men", men));
        out.add(new PieChart.Data("Women", women));
        return out;

    }

    public List<PieChart.Data> getAgePieData(int lt25, int btwn2534, int btwn3544,
                                             int btwn4554, int gt55){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        String[] tags = new String[]{"<25","25-34","35-44","45-54",">55"};
        int[] vals = new int[]{lt25, btwn2534,btwn3544,btwn4554,gt55};

        for(int i = 0; i < tags.length; i++){
            out.add(new PieChart.Data(tags[i], vals[i]));
        }

        return out;
    }

    public List<PieChart.Data> getIncomePieData(int high, int medium, int low){

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
    public void updateBasicGraph(List<XYChart.Series> data){

        RootController.doGUITask(() -> {

            basicChart.getData().clear();

            for(XYChart.Series s: data){

                basicChart.getData().add(s);

            }

            basicChart.getXAxis().setLabel(ControllerInjector.getRootController().calcMetric());

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