package models;

import daos.ClickDao;
import daos.DaoInjector;
import entities.Click;

import java.util.*;

public class HistogramModel {

    private String campaign = null;
    private ClickDao clickDao = DaoInjector.newClickDao();
    private HashMap<Double, Integer> histogramData = new HashMap<Double, Integer>();

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public HashMap<Double, Integer> getData(double minCost, Double maxCost, double bandLength ) {

        List<Click> data = clickDao.getFromCampaign(this.campaign);
        this.histogramData.clear();

        // if no max cost specified, get the hightest cost of a click
        Double maxClickCost = (maxCost != null) ? maxCost : (Math.floor(Collections.max(data, Comparator.comparing(c -> c.getClickCost())).getClickCost()) + 2*bandLength);

        // create the hashmap to hold the data
        this.createBands(minCost, maxClickCost, bandLength);

        // for each click, add it to the band
        for( Click click : data){
            Double cost = click.getClickCost();
            // if click between the values of interest
            if( cost >= minCost && cost <= maxClickCost ){
                // get the cost with respect to its band
                List<Double> keys = new ArrayList<Double>(this.histogramData.keySet());
                Collections.sort(keys);

                for( int i =0; i < keys.size(); i++ ){
                    double key = keys.get(i);
                    if( i == keys.size() - 1 ){
                        // last band, edge case
                        this.histogramData.put( key, this.histogramData.get(key) + 1);
                        break;
                    }else{
                        if( cost >= key && cost < keys.get(i+1) ){
                            this.histogramData.put( key, this.histogramData.get(key) + 1);
                            break;
                        }
                    }
                }
            }
        }
        return this.histogramData;
    }

    private void createBands(double min, double max, double bandLength) {
        // first band is the min value
        double currentBand = min;

        // as long as max not reached, iterate through intermediate values and create bands
        while (currentBand < max) {
            this.histogramData.put(currentBand, 0);
            currentBand = Math.round((currentBand + bandLength) * 1000) / 1000.0;
        }
    }
}
