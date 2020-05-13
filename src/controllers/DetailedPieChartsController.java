package controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import models.PieChartModel;

import javax.persistence.criteria.Root;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
public class DetailedPieChartsController {

    @FXML private PieChart clickGender;
    @FXML private PieChart clickAge;
    @FXML private PieChart clickIncome;
    @FXML private PieChart clickContext;
    @FXML private PieChart impressionGender;
    @FXML private PieChart impressionAge;
    @FXML private PieChart impressionIncome;
    @FXML private PieChart impressionContext;
    @FXML private PieChart serverGender;
    @FXML private PieChart serverAge;
    @FXML private PieChart serverIncome;
    @FXML private PieChart serverContext;

    //This needs to be initialized
    private RootController controller;
    private PieChartModel pieChartModel;

    @FXML
    public void initialize(){

        clickGender.getData().add(new PieChart.Data("test",10));

    }

    public void init(RootController controller) {
        this.controller = controller;
        this.pieChartModel = new PieChartModel();





    }

    public void updateData() {
        String campaignName = controller.getCurrentCampaign();

        LocalDateTime start = controller.getPeriodStart();
        LocalDateTime end = controller.getPeriodEnd();

        pieChartModel.setCampaign(campaignName);
        pieChartModel.setStart(start);
        pieChartModel.setEnd(end);

        //IMPRESSIONS
        HashMap<String, Integer> pieChartDataImpress = pieChartModel.getDistributions("impressions");
        List<PieChart.Data> genderPieDataI = pieChartModel.getGenderPieData(pieChartDataImpress.get("men"), pieChartDataImpress.get("women"));
        List<PieChart.Data> agePieDataI = pieChartModel.getAgePieData(pieChartDataImpress.get("lt25"), pieChartDataImpress.get("btwn2534"),
                pieChartDataImpress.get("btwn3544"), pieChartDataImpress.get("btwn4554"), pieChartDataImpress.get("gt55"));
        List<PieChart.Data> incomePieDataI = pieChartModel.getIncomePieData(pieChartDataImpress.get("low"), pieChartDataImpress.get("medium"),
                pieChartDataImpress.get("high"));
        List<PieChart.Data> contextPieDataI = pieChartModel.getContextPieData(pieChartDataImpress.get("blog"), pieChartDataImpress.get("news"),
                pieChartDataImpress.get("socialmedia"), pieChartDataImpress.get("shopping"), pieChartDataImpress.get("hobbies"),
                pieChartDataImpress.get("travel"));

        updatePieCharts("impression", genderPieDataI, agePieDataI, incomePieDataI, contextPieDataI);

        //CLICKS
        HashMap<String, Integer> pieChartDataClick = pieChartModel.getDistributions("click");
        List<PieChart.Data> genderPieDataC = pieChartModel.getGenderPieData(pieChartDataClick.get("men"), pieChartDataClick.get("women"));
        List<PieChart.Data> agePieDataC = pieChartModel.getAgePieData(pieChartDataClick.get("lt25"), pieChartDataClick.get("btwn2534"),
                pieChartDataClick.get("btwn3544"), pieChartDataClick.get("btwn4554"), pieChartDataClick.get("gt55"));
        List<PieChart.Data> incomePieDataC = pieChartModel.getIncomePieData(pieChartDataClick.get("low"), pieChartDataClick.get("medium"),
                pieChartDataClick.get("high"));
        List<PieChart.Data> contextPieDataC = pieChartModel.getContextPieData(pieChartDataClick.get("blog"), pieChartDataClick.get("news"),
                pieChartDataClick.get("socialmedia"), pieChartDataClick.get("shopping"), pieChartDataClick.get("hobbies"),
                pieChartDataClick.get("travel"));

        updatePieCharts("click", genderPieDataC, agePieDataC, incomePieDataC, contextPieDataC);

        //SERVER
        HashMap<String, Integer> pieChartDataServer = pieChartModel.getDistributions("server");
        List<PieChart.Data> genderPieDataS = pieChartModel.getGenderPieData(pieChartDataServer.get("men"), pieChartDataServer.get("women"));
        List<PieChart.Data> agePieDataS = pieChartModel.getAgePieData(pieChartDataServer.get("lt25"), pieChartDataServer.get("btwn2534"),
                pieChartDataServer.get("btwn3544"), pieChartDataServer.get("btwn4554"), pieChartDataServer.get("gt55"));
        List<PieChart.Data> incomePieDataS = pieChartModel.getIncomePieData(pieChartDataServer.get("low"), pieChartDataServer.get("medium"),
                pieChartDataServer.get("high"));
        List<PieChart.Data> contextPieDataS = pieChartModel.getContextPieData(pieChartDataServer.get("blog"), pieChartDataServer.get("news"),
                pieChartDataServer.get("socialmedia"), pieChartDataServer.get("shopping"), pieChartDataServer.get("hobbies"),
                pieChartDataServer.get("travel"));

        updatePieCharts("server", genderPieDataS, agePieDataS, incomePieDataS, contextPieDataS);

    }



    /**
     * Specify which set of pie charts you want to update ie either
     * "click", "impression" or "server" followed by the new data
     * for each pie chart
     * @param which
     * @param genderData
     * @param ageData
     * @param incomeData
     * @param contextData
     */
    public void updatePieCharts(String which, List<PieChart.Data> genderData, List<PieChart.Data> ageData, List<PieChart.Data> incomeData,
                                List<PieChart.Data> contextData){

        PieChart[] pies = null;


        switch(which){

            case "click": pies = new PieChart[]{clickGender, clickAge, clickIncome, clickContext};
                break;
            case "impression": pies = new PieChart[]{impressionGender, impressionAge, impressionIncome, impressionContext};
                break;
            case "server": pies = new PieChart[]{serverGender, serverAge, serverIncome, serverContext};
                break;
            default: throw new Error("Invalid argument to update pie charts, please use only one of 'click', 'impression' or 'server'");

        }


        //Arrays of generics are not allowed so first do objects and then cast
        final Object[] args = new Object[]{genderData, ageData, incomeData, contextData};
        final PieChart[] forLambda = pies;

        this.controller.doGUITask(() -> {

            for(int i = 0; i < forLambda.length; i++) {

                ObservableList<PieChart.Data> o = FXCollections.observableArrayList((List<PieChart.Data>) args[i]);

//                forLambda[i].getData().clear();
//                forLambda[i].getData().addAll((List<PieChart.Data>) args[i]);
                forLambda[i].setData(o);


            }

        });



    }

}