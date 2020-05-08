package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;

public class AdvancedPageController {

    //ADVANCED PAGE
    @FXML
    private LineChart advancedChart;
    @FXML private BarChart histogram;

    private boolean ctr;
    private boolean cpa;
    private boolean cpc;
    private boolean cpm;
    private boolean bounceRate;

    @FXML
    public void toggleCTR(){}
    @FXML
    public void toggleCPA(){}
    @FXML
    public void toggleCPC(){}
    @FXML
    public void toggleCPM(){}
    @FXML
    public void toggleBounceRate(){}

    @FXML private Label ctrLabel;
    @FXML private Label cpaLabel;
    @FXML private Label cpcLabel;
    @FXML private Label cpmLabel;
    @FXML private Label bounceRateLabel;
    @FXML private PieChart contextPie;

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

        contextPie.getData().clear();
        contextPie.getData().addAll(contextData);

    }

    //END ADVANCED PAGE

}