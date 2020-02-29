package views;

import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;

public class HistogramHandler {

    private BarChart chart;
    private CategoryAxis xAxis;
    private NumberAxis yAxis;

    public HistogramHandler(BarChart chart, CategoryAxis xAxis,
                            NumberAxis yAxis, String metric){

        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;

        this.init();

    }

    private void init(){



    }

}