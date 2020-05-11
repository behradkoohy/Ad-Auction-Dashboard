package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;
import models.PieChartModel;

import java.util.ArrayList;
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

    /*
    At the moment this is an extra instance than
    the one in the basic page controller, perhaps we
    only need one shared one? look into changing this later
     */
    private RootController controller;
    private PieChartModel pieChartModel;

    private boolean ctr;
    private boolean cpa;
    private boolean cpc;
    private boolean cpm;
    private boolean bounceRate;

    public void init(RootController controller) {
        this.controller = controller;
        this.pieChartModel = new PieChartModel();
    }

    @FXML
    public void initialize(){

        //Styling init here

    }

    public void updateData(String campaignName){
        /*
        LocalDateTime start = ControllerInjector.getRootController().getPeriodStart();
        LocalDateTime end = ControllerInjector.getRootController().getPeriodEnd();

        String ctrStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getCTR(start, end)));
        String cpaStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getCPA(start, end)));
        String cpcStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getCPC(start, end)));
        String cpmStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getCPM(start, end)));
        String bounceRateStr = String.valueOf(RootController.to2DP(ControllerInjector.getRootController().getMetrics().getBounceRate(start, end)));

        updateLabels(ctrStr, cpaStr, cpcStr,  cpmStr, bounceRateStr);
        */
        /*
        Not sure how the pie chart model works for context stuff at the moment

        pieChartModel.setCampaign(campaignName);
        pieChartModel.setStart(start);
        pieChartModel.setEnd(end);
        */
    }

    public void updateLabels(String ctr, String cpa, String cpc,
                             String cpm, String bounceRate){

        this.controller.doGUITask(() -> {

            ctrLabel.setText(ctr);
            cpaLabel.setText(cpa);
            cpcLabel.setText(cpc);
            cpmLabel.setText(cpm);
            bounceRateLabel.setText(bounceRate);

        });

    }

    /**
     * Generate the context pie chart data that will populate the context pie chart
     * update. Make sure you add the arguments in the order they are listed
     * @param news
     * @param shopping
     * @param socialMedia
     * @param blogs
     * @param hobbies
     * @param travel
     */
    public List<PieChart.Data> getContextPieData(int news, int shopping, int socialMedia, int blogs,
                                                 int hobbies, int travel){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        String[] tags = new String[]{"News","Shopping","Social Media", "Blogs", "Hobbies", "Travel"};
        int[] vals = new int[]{news, shopping, socialMedia, blogs, hobbies, travel};

        for(int i = 0; i < tags.length; i++){

            out.add(new PieChart.Data(tags[i],vals[i]));

        }

        return out;

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
    public void updateAdvancedChart(List<XYChart.Series> data){

        this.controller.doGUITask(() -> {

            advancedChart.getData().clear();
            advancedChart.getData().addAll(data);

        });

    }

    /**
     * Update the histogram to show the new data specified,
     * note that the histogram only ever has one series of data
     */
    public void updateHistogram(XYChart.Series data){

        this.controller.doGUITask(() -> {

            histogram.getData().clear();
            histogram.getData().add(data);

        });

    }

    @FXML
    public void toggleCTR(){

        ctr = !ctr;

    }

    @FXML
    public void toggleCPA(){

        cpa = !cpa;

    }

    @FXML
    public void toggleCPC(){

        cpc = !cpc;

    }

    @FXML
    public void toggleCPM(){

        cpm = !cpm;

    }

    @FXML
    public void toggleBounceRate(){

        bounceRate = !bounceRate;

    }

}