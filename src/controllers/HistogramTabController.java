package controllers;

import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import models.HistogramModel;

import java.util.List;

public class HistogramTabController {
    @FXML private BarChart barChart;
    @FXML private CategoryAxis barChartXAxis;
    @FXML private NumberAxis barChartYAxis;

    private Controller controller;
    private HistogramModel histogramModel;

    private int interval;
    private int max;

    public void init(Controller controller){
        this.controller = controller;
        interval = 10;
        max = 100;
    }

    public void loadData( String campaignName ){
        this.histogramModel = new HistogramModel();
        this.histogramModel.setCampaign(campaignName);
        List<Integer> data = this.histogramModel.getData();
        updateHistogram(data);
    }
    public void updateHistogram(List<Integer> data){

        barChart.getData().clear();
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setBarGap(0);
        barChart.setCategoryGap(0);

        XYChart.Series series = new XYChart.Series();

        for(int i = 0; i < data.size(); i++){

            series.getData().add(new XYChart.Data(String.valueOf(i * 1), data.get(i)));

        }

        barChart.getData().add(series);

        barChartXAxis.setLabel("Cost of clicks");
        barChartYAxis.setLabel("Clicks");

    }

}
