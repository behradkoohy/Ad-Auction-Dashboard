package models;

import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.Click;

import java.util.ArrayList;
import java.util.List;

public class HistogramModel {
    private String campaign;
    private ClickDao clickDao;

    // for testing purposes
    private int band_length = 1;

    public HistogramModel(String campaign, ClickDao clickDao, ImpressionDao impressionDao, ServerEntryDao serverEntryDao) {
        //Not using other two daos atm
        this.clickDao = clickDao;
        this.campaign = campaign;
    }

    public List<Integer> getData(){
        List<Integer> chartData = new ArrayList<Integer>();
        chartData.add(0);

        List<Click> data = clickDao.getFromCampaign(this.campaign);

        for( Click click : data){
            double cost = click.getClickCost();
            if( cost > chartData.size() - 1 ){
                chartData.add(1);
            }else{
                chartData = this.addToBand(chartData, cost);
            }
        }

        return chartData;
    }

    private List<Integer> addToBand(List<Integer> chartData, double clickCost) {
        int band = (int) (clickCost / band_length);
        chartData.set(band, chartData.get(band) + 1);
        return chartData;
    }


}
