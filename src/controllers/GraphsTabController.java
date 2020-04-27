package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
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
    private List data = new ArrayList(10);

    private List<String> campaigns = new ArrayList<String>(4);

    public void init(Controller controller){
        lineChart.setAnimated(false);
        this.controller = controller;
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

    public void loadData(String campaignName){
        campaigns.add(campaignName);


        this.updateChart();
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

    public void updateChart(){
        this.start = controller.getStart();
        this.end = controller.getEnd();
        this.duration = calcDuration();
        //TODO something with multiple charts
        for (String campaignName : campaigns) {
            ChartHandler handler = new ChartHandler(campaignName, lineChart, lineChartXAxis,
                    lineChartYAxis, this.controller.calcMetric(), this.data, this.controller.unitsDifference, impressions,
                    conversions, clicks, uniqueUsers, bounces, totalCostB, CTRB, CPAB,
                    CPCB, CPMB, bounceRateB, start, end, duration, this.controller.getMetrics());
        }
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

}
