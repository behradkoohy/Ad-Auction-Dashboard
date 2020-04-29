package controllers;

import daos.DaoInjector;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import models.ChartHandler;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

import static java.time.temporal.ChronoUnit.*;

public class GraphsTabController {

    @FXML private LineChart<?,?> lineChart;
    @FXML private CategoryAxis lineChartXAxis;
    @FXML private NumberAxis lineChartYAxis;

    private Controller controller;
    private ChartHandler handler;

    //Whether or not these metrics should be displayed on the graph
    private boolean impressions;
    private boolean conversions;
    private boolean clicks;
    private boolean uniqueUsers;
    private boolean bounces;
    private boolean totalCostB;
    private boolean CTRB;
    private boolean CPAB;
    private boolean CPCB;
    private boolean CPMB;
    private boolean bounceRateB;

    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;
    private List<String> campaigns = new ArrayList<String>(4);

    @FXML
    public void initialize(){

        lineChartYAxis.setLabel("Number");
        lineChart.setAnimated(false);

        //Initial state of checkboxes below the chart
        impressions = true;
        conversions = true;
        clicks = false;
        uniqueUsers = false;
        bounces = false;
        totalCostB = false;
        CTRB = false;
        CPAB = false;
        CPCB = false;
        CPMB = false;
        bounceRateB = false;

    }

    public void init(Controller controller){

        this.controller = controller;
        handler = new ChartHandler(controller.getMetrics());

    }

    /**
     * Called whenever a new campaign is loaded
     * @param campaignName
     */
    public void loadData(String campaignName){

        campaigns.add(campaignName);
        this.updateChart();

    }

    public void updateChart(){

        this.start = controller.getStart();
        this.end = controller.getEnd();
        this.duration = calcDuration();

        List<XYChart.Series> newChartData = handler.getChartDataAccordingTo(controller.getCurrentCampaignName(), start, end,
                duration, impressions, conversions, clicks, uniqueUsers, bounces, CTRB, CPAB, CPCB, CPMB, bounceRateB);

        populateGraphEXT(newChartData);

        /*
        TODO something with multiple charts
        for (String campaignName : campaigns) {



        }*/
    }

    /**
     * Allows updating of graph from an external thread
     * @param data
     */
    public void populateGraphEXT(List<XYChart.Series> data){

        if(Platform.isFxApplicationThread()){

            populateGraph(data);

        } else {

            Platform.runLater(() -> populateGraph(data));

        }

    }

    /**
     * Populates the graph with the data specified
     * @param data
     */
    private void populateGraph(List<XYChart.Series> data){

        try {

            controller.verifyIsFXThread("populateGraph");

        } catch (Exception e){

            controller.error(e.getMessage());
            return;

        }

        lineChart.getData().clear();

        for(XYChart.Series s: data){

            lineChart.getData().add(s);

        }

        lineChartXAxis.setLabel(controller.calcMetric());

    }

    private Duration calcDuration() {

        int digits = this.controller.getGranDigits();
        ChronoUnit unit = this.controller.getGranUnit();
        Duration dur;

        switch (unit) {
            case HOURS:
                dur = Duration.of(digits, HOURS);
                break;

            case DAYS:
                dur = Duration.of(digits, DAYS);
                break;

            case WEEKS:
                dur = Duration.of(digits, DAYS).multipliedBy(7);
                break;

            default:
                dur = Duration.ofDays(1);
        }

        return dur;

    }

    /*
        Methods corresponding to the checkboxes for the chart
        Each time one checkbox value is changed the chart will
        automatically update
    */
    @FXML
    private void toggleImpressions(){

        impressions = !impressions;
        updateChart();

    }

    @FXML
    private void toggleConversions(){

        conversions = !conversions;
        updateChart();

    }

    @FXML
    private void toggleClicks(){

        clicks = !clicks;
        updateChart();

    }

    @FXML
    private void toggleUnique(){

        uniqueUsers = !uniqueUsers;
        updateChart();

    }

    @FXML
    private void toggleBounces(){

        bounces = !bounces;
        updateChart();

    }

    @FXML
    private void toggleTotal(){

        totalCostB = !totalCostB;
        updateChart();

    }

    @FXML
    private void toggleCTR(){

        CTRB = !CTRB;
        updateChart();

    }

    @FXML
    private void toggleCPA(){

        CPAB = !CPAB;
        updateChart();

    }

    @FXML
    private void toggleCPC(){

        CPCB = !CPCB;
        updateChart();

    }

    @FXML
    private void toggleCPM(){

        CPMB = !CPMB;
        updateChart();

    }

    @FXML
    private void toggleBounceRate(){

        bounceRateB = !bounceRateB;
        updateChart();

    }

}
