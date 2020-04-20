package controllers;

import com.jfoenix.controls.JFXTextField;
import javafx.fxml.FXML;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.input.MouseEvent;
import models.HistogramModel;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class HistogramTabController {
    @FXML private BarChart barChart;
    @FXML private CategoryAxis barChartXAxis;
    @FXML private NumberAxis barChartYAxis;
    @FXML private JFXTextField minValue;
    @FXML private JFXTextField maxValue;
    @FXML private JFXTextField bandLength;

    private Controller controller;
    private HistogramModel histogramModel;

    private HashMap<Double, Integer> barChartData;

    public void init(Controller controller){
        this.controller = controller;
        barChartXAxis.setLabel("Cost of clicks");
        barChartYAxis.setLabel("Clicks");
    }

    public void loadData( String campaignName ){
        this.histogramModel = new HistogramModel();
        this.histogramModel.setCampaign(campaignName);
        this.getData(0, null, 1);
    }

    public void refreshData(){
        try{
            double minCost = Double.valueOf(minValue.getText());
            Double maxCost = Double.valueOf(maxValue.getText());
            double bandLengthValue = Double.valueOf(bandLength.getText());
            this.getData(minCost, maxCost, bandLengthValue);
        }catch(NumberFormatException e){
            this.controller.error("Please only input numbers");
        }
    }

    public void redrawDefault(){
        this.getData(0, null, 1);
    }

    private void getData(double minCost, Double maxCost, double bandLength){
        this.barChartData = this.histogramModel.getData(minCost, maxCost, bandLength);
        updateHistogramGraphics();
        updateHistogramData();
    }

    public void updateHistogramGraphics(){
        barChart.getData().removeAll(barChart.getData());
        barChart.setLegendVisible(false);
        barChart.setAnimated(false);
        barChart.setBarGap(0);
        barChart.setCategoryGap(0);
    }

    public void updateHistogramData(){
        XYChart.Series series = new XYChart.Series();
        List<Double> keys = new ArrayList<Double>(this.barChartData.keySet());
        Collections.sort(keys);

        for(double categoryIndex : keys){
            series.getData().add(new XYChart.Data(String.valueOf(categoryIndex), barChartData.get(categoryIndex)));
        }
        barChart.getData().addAll(series);
    }

}
