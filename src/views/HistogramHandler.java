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
                            NumberAxis yAxis, String metric){

        this.chart = chart;
        this.xAxis = xAxis;
        this.yAxis = yAxis;

        //TODO these are just test numbers, it has not been implemented yet
        interval = 10;
        max = 100;

        step = (int) Math.floor(max / interval);

        this.init();

    }

    private void init(){

        chart.getData().clear();
        chart.setLegendVisible(false);
        chart.setAnimated(false);
        chart.setBarGap(0);
        chart.setCategoryGap(0);

        List<Integer> testList = new ArrayList<Integer>();

        //TODO test first time with 10 groups
        for(int i = 0; i < 10; i++){

            testList.add(0);

        }

        Random r = new Random();

        //TODO for testing
        for(int i = 1; i <= 100; i++){

            int num = r.nextInt(100);

            if(num <= 10){

                testList.set(0, testList.get(0) + 1);

            } else if(num <= 20){

                testList.set(1, testList.get(1) + 1);

            } else if(num <= 30){

                testList.set(2, testList.get(2) + 1);

            } else if(num <= 40){

                testList.set(3, testList.get(3) + 1);

            } else if(num <= 50){

                testList.set(4, testList.get(4) + 1);

            } else if(num <= 60){

                testList.set(5, testList.get(5) + 1);

            } else if(num <= 70){

                testList.set(6, testList.get(6) + 1);

            } else if(num <= 80){

                testList.set(7, testList.get(7) + 1);

            } else if(num <= 90){

                testList.set(8, testList.get(8) + 1);

            } else if(num <= 100){

                testList.set(9, testList.get(9) + 1);

            }

        }

        XYChart.Series series = new XYChart.Series();

        for(int i = 0; i < 10; i++){

            series.getData().add(new XYChart.Data(String.valueOf(i * 10), testList.get(i)));

        }

        chart.getData().add(series);

        xAxis.setLabel("Click range");
        yAxis.setLabel("Number of clicks");

    }

}