package views;

import javafx.scene.chart.*;

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
    private int unitDifference;
    private boolean impressions;
    private boolean conversions;
    private boolean clicks;
    private boolean unique;
    private boolean bounces;

    private Random r;

    public ChartHandler(LineChart chart, CategoryAxis xAxis, NumberAxis yAxis,
                        String metric, int unitDifference, boolean impressions, boolean conversions, boolean clicks,
                        boolean unique, boolean bounces){

        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;

        this.metric = metric;
        this.unitDifference = unitDifference;
        this.impressions = impressions;
        this.conversions = conversions;
        this.clicks = clicks;
        this.unique = unique;
        this.bounces = bounces;

        r = new Random();

        this.init();

    }

    public void init(){

        chart.getData().clear();

        //Disregard the time for now and just test chart function using 10 points on x axis
        unitDifference = 10;

        /*
        xAxis.setLabel(metric);
        yAxis.setLabel("Number");*/

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

        System.out.println("finished loading the data, axis values are:");
        System.out.println("x axis:" + xAxis);
        System.out.println("y axis:" + yAxis);

        xAxis.setLabel(metric);
        yAxis.setLabel("Number");

    }

    private void addImpressions(){

        XYChart.Series series = new XYChart.Series();
        series.setName("Impressions");

        for(int x = 1; x <= unitDifference; x++){

            series.getData().add(new XYChart.Data(String.valueOf(x), r.nextInt(100)));

        }

        chart.getData().add(series);

    }

    private void addConversions(){

        XYChart.Series series = new XYChart.Series();
        series.setName("Conversions");


        for(int x = 1; x <= unitDifference; x++){

            series.getData().add(new XYChart.Data(String.valueOf(x), r.nextInt(100)));

        }

        chart.getData().add(series);

    }

    private void addClicks(){

        XYChart.Series series = new XYChart.Series();
        series.setName("Clicks");

        for(int x = 1; x <= unitDifference; x++){

            series.getData().add(new XYChart.Data(String.valueOf(x), r.nextInt(100)));

        }

        chart.getData().add(series);

    }

    private void addUniques(){

        XYChart.Series series = new XYChart.Series();
        series.setName("Unique users");

        for(int x = 1; x <= unitDifference; x++){

            series.getData().add(new XYChart.Data(String.valueOf(x), r.nextInt(100)));

        }

        chart.getData().add(series);

    }

    private void addBounces(){

        XYChart.Series series = new XYChart.Series();
        series.setName("Bounces");

        for(int x = 1; x <= unitDifference; x++){

            series.getData().add(new XYChart.Data(String.valueOf(x), r.nextInt(1000)));

        }

        chart.getData().add(series);

    }

}