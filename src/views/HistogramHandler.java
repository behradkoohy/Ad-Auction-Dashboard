package views;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class HistogramHandler {

    private BarChart chart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;
    private int interval;
    private int max;
    private int step;

    public HistogramHandler(BarChart chart, CategoryAxis xAxis,
                            NumberAxis yAxis, String metric, List<Integer> data){

        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;

        //TODO these are just test numbers, it has not been implemented yet
        interval = 10;
        max = 100;

        step = (int) Math.floor(max / interval);

        this.init( data);

    }

    private void init(List<Integer> data){

        //This sets up the bar chart to look like a histogram
        chart.getData().clear();
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setBarGap(0);
        chart.setCategoryGap(0);

        XYChart.Series series = new XYChart.Series();

        for(int i = 0; i < data.size(); i++){

            series.getData().add(new XYChart.Data(String.valueOf(i * 1), data.get(i)));

        }

        chart.getData().add(series);

        xAxis.setLabel("Cost of clicks");
        yAxis.setLabel("Clicks");

    }

}