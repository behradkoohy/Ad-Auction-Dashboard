package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Label;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class BasicPageController {

    //BASIC PAGE
    @FXML
    private LineChart basicChart;
    @FXML private PieChart genderPie;
    @FXML private PieChart agePie;
    @FXML private PieChart incomePie;

    private boolean impressions;
    private boolean conversions;
    private boolean clicks;
    private boolean uniques;
    private boolean bounces;

    @FXML
    public void initialize(){

        test();
        impressionsLabel.setText("Testing 123");

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
     * Updates the pie charts on the basic stats page
     * @param genderData
     * @param ageData
     * @param incomeData
     */
    public void updateBasicPieCharts(List<PieChart.Data> genderData,
                                     List<PieChart.Data> ageData,
                                     List<PieChart.Data> incomeData){
        /*
        if(genderData.size() != 2 || ageData.size() != 5 || incomeData.size() != 5){

            error("One of the pie chart data sets on the basic statistics page has the wrong" +
                    "amount of members! This should not be happening");

        }*/

        genderPie.getData().clear();
        agePie.getData().clear();
        incomePie.getData().clear();

        genderPie.getData().addAll(genderData);
        agePie.getData().addAll(ageData);
        incomePie.getData().addAll(incomeData);

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

    @FXML
    public void toggleImpressions(){}
    @FXML
    public void toggleConversions(){}
    @FXML
    public void toggleClicks(){}
    @FXML
    public void toggleUniques(){}
    @FXML
    public void toggleBounces(){}

    @FXML private Label impressionsLabel;
    @FXML private Label clicksLabel;
    @FXML private Label uniquesLabel;
    @FXML private Label bouncesLabel;
    @FXML private Label conversionsLabel;
    @FXML private Label totCostLabel;
    //END BASIC PAGE

}