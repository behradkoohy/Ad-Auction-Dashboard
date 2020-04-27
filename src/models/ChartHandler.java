package models;

import javafx.scene.chart.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;

/**
 * Seperate class for handling all the data that will be displayed
 * on the chart
 */
public class ChartHandler {

    private LineChart chart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    private String metric;

    /*
    The list of data that will populate the chart,
    the list items are the y values and the list indices
    are the x values
    */
    private List data;

    private int unitDifference;
    private boolean impressions;
    private boolean conversions;
    private boolean clicks;
    private boolean unique;
    private boolean bounces;
    private boolean totalCost;
    private boolean CTR;
    private boolean CPA;
    private boolean CPC;
    private boolean CPM;
    private boolean bounceRate;

    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;

    private String campaignName;
    private Metrics metricsModel;
    //TODO remove this it is just for testing
    private Random r;

    public ChartHandler(String campaignName, LineChart chart, CategoryAxis xAxis, NumberAxis yAxis, String metric,
                        List<Integer> data, int unitDifference,
                        boolean impressions, boolean conversions,
                        boolean clicks, boolean unique, boolean bounces, boolean totalCost, boolean CTR,
                        boolean CPA, boolean CPC, boolean CPM, boolean bounceRate,
                        LocalDateTime start, LocalDateTime end, Duration duration,
                        Metrics metricsModel){

        this.campaignName = campaignName;
        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;
        this.data = data;
        this.metric = metric;
        this.unitDifference = unitDifference;

        this.impressions = impressions;
        this.conversions = conversions;
        this.clicks = clicks;
        this.unique = unique;
        this.bounces = bounces;
        this.totalCost = totalCost;
        this.CTR = CTR;
        this.CPA = CPA;
        this.CPC = CPC;
        this.CPM = CPM;
        this.bounceRate = bounceRate;

        this.start = start;
        this.end = end;
        this.duration = duration;

        this.metricsModel = metricsModel;
        this.metricsModel.setCampaign(campaignName);


        this.init();
    }

    /**
     * Updates the chart to have the latest data
     */
    private void init(){

        chart.getData().clear();

        //TODO Disregard the time for now and just test chart function using 10 points on x axis
        //unitDifference = 10;

        xAxis.setLabel(metric);
        yAxis.setLabel("Number");

        if(xAxis == null){

            System.out.println("x axis null");

        }

        if(yAxis == null){

            System.out.println("y axis null");

        }

        if(impressions){

            addImpressions();

        }

        if(conversions){

            addConversions();

        }

        if(clicks){

            addClicks();

        }

        if(unique){

            addUniques();

        }

        if(bounces){

            addBounces();

        }


        if(CTR){

            addCTR();

        }

        if(CPA){

            addCPA();

        }

        if(CPC){

            addCPC();

        }

        if(CPM){

            addCPM();

        }

        if(bounceRate){

            addBounceRate();

        }

    }

    /*
    At the moment this is dummy data, can change the constructor e.g. add the controller as
    an argument so that the updated data sets from the database to go on the chart can be accessed
    */
    private void addImpressions(){

        XYChart.Series series = metricsModel.getImpressionsPerTime(start, end, duration);

        chart.getData().add(series);

    }

    private void addConversions(){

        XYChart.Series series = metricsModel.getConversionsPerTime(start, end, duration);

        chart.getData().add(series);

    }

    private void addClicks(){

        XYChart.Series series = metricsModel.getClicksPerTime(start, end, duration);

        chart.getData().add(series);

    }

    private void addUniques(){

        XYChart.Series series = metricsModel.getUniquesPerTime(start, end, duration);

        chart.getData().add(series);

    }

    private void addBounces(){

        XYChart.Series series = metricsModel.getBouncesPerTime(start, end, duration);

        chart.getData().add(series);

    }


    private void addCTR(){
        XYChart.Series series = metricsModel.getCTRPerTime(start, end, duration);

        chart.getData().add(series);
    }

    private void addCPA(){
        XYChart.Series series = metricsModel.getCPAPerTime(start, end, duration);

        chart.getData().add(series);
    }

    private void addCPC(){
        XYChart.Series series = metricsModel.getCPCPerTime(start, end, duration);

        chart.getData().add(series);
    }

    private void addCPM(){
        XYChart.Series series = metricsModel.getCPMPerTime(start, end, duration);

        chart.getData().add(series);
    }

    private void addBounceRate(){
        XYChart.Series series = metricsModel.getBouncePerTime(start, end, duration);

        chart.getData().add(series);
    }

}