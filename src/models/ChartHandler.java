package models;

import javafx.scene.chart.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Seperate class for handling all the data that will be displayed
 * on the chart
 */
public class ChartHandler {

    private Metrics metricsModel;

    private String campaignName;
    private LocalDateTime start;
    private LocalDateTime end;
    private Duration duration;

    public ChartHandler(Metrics metricsModel){

        this.metricsModel = metricsModel;

    }

    public List<XYChart.Series> getChartDataAccordingTo(String campaignName, LocalDateTime start, LocalDateTime end,
                                                        Duration duration, boolean impressions, boolean conversions, boolean clicks,
                                                        boolean unique, boolean bounces, boolean CTR, boolean CPA, boolean CPC,
                                                        boolean CPM, boolean bounceRate){

        metricsModel.setCampaign(campaignName);
        this.start = start;
        this.end = end;
        this.duration = duration;

        List<XYChart.Series> seriesList = new ArrayList<XYChart.Series>();

        if(impressions) {
            seriesList.add(getImpressions());
        }

        if(conversions) {
            seriesList.add(getConversions());
        }

        if(clicks) {
            seriesList.add(getClicks());
        }

        if(unique) {
            seriesList.add(getUniques());
        }

        if(bounces) {
            seriesList.add(getBounces());
        }

        if(CTR) {
            seriesList.add(getCTR());
        }

        if(CPA) {
            seriesList.add(getCPA());
        }

        if(CPC) {
            seriesList.add(getCPC());
        }

        if(CPM){
            seriesList.add(getCPM());
        }

        if(bounceRate){
            seriesList.add(getBounceRate());
        }

        return seriesList;

    }

    private XYChart.Series getImpressions(){

        return metricsModel.getImpressionsPerTime(start, end, duration);

    }

    private XYChart.Series getConversions(){

        return metricsModel.getConversionsPerTime(start, end, duration);

    }

    private XYChart.Series getClicks(){

        return metricsModel.getClicksPerTime(start, end, duration);

    }

    private XYChart.Series getUniques(){

        return metricsModel.getUniquesPerTime(start, end, duration);

    }

    private XYChart.Series getBounces(){

        return metricsModel.getBouncesPerTime(start, end, duration);

    }


    private XYChart.Series getCTR(){

        return  metricsModel.getCTRPerTime(start, end, duration);

    }

    private XYChart.Series getCPA(){

        return metricsModel.getCPAPerTime(start, end, duration);

    }

    private XYChart.Series getCPC(){

        return metricsModel.getCPCPerTime(start, end, duration);

    }

    private XYChart.Series getCPM(){

        return metricsModel.getCPMPerTime(start, end, duration);

    }

    private XYChart.Series getBounceRate(){

        return metricsModel.getBouncePerTime(start, end, duration);

    }

}