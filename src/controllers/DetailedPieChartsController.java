package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;

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

    @FXML
    public void initialize(){

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

        controller.doGUITask(() -> {

            for(int i = 0; i < forLambda.length; i++) {

                forLambda[i].getData().clear();
                forLambda[i].getData().addAll((List<PieChart.Data>) args[i]);

            }

        });

    }

}