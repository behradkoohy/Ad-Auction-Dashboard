package models;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.Click;
import entities.Impression;
import entities.ServerEntry;

import java.beans.IntrospectionException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.ArrayList;
import java.util.HashSet;
import javafx.scene.chart.*;


public class Metrics {

    //false = pages -- true = time spent -- change
    private boolean bounceDef = false;
    private int bouncePages = 1;
    private Duration bounceTime = Duration.ofSeconds(5);

    private ClickDao clickDao;
    private ImpressionDao impressionsDao;
    private ServerEntryDao serverDao;


    public int getNumImpressions(String campaign) {
        return impressionsDao.getFromCampaign(campaign).size();
    }

    public int getNumClicks(String campaign) {
        return clickDao.getFromCampaign(campaign).size();
    }


    public int getNumUniqs(String campaign) {

        HashSet set = new HashSet<>();

        for (Click click : clickDao.getFromCampaign(campaign)) {
            set.add(click.getId());
        }

        return set.size();

    }

    public void setBounceTime(Duration duration) {
        bounceDef = true;

        bounceTime = duration;
    }

    public void setBouncePages(int bouncePages) {
        bounceDef = false;
        this.bouncePages = bouncePages;
    }


    public int getNumBounces(String campaign) {

        int num = 0;
        //time spent defines bounce
        if (bounceDef) {

            for (ServerEntry server : serverDao.getFromCampaign(campaign)) {

                if (server.getExitDate() != null) {
                    Duration duration = Duration.between(server.getEntryDate(), server.getExitDate());
                    if (duration.compareTo(bounceTime) <= 0) {
                        num ++;
                    }
                }


            }

        }
        //pages viewed defines bounce
        else {
            for (ServerEntry server : serverDao.getFromCampaign(campaign)) {
                if (server.getPageViews() < bouncePages) {
                    num ++;
                }
            }
        }

        return num;
    }

    public int getConversions(String campaign) {
        int num = 0;
        for (ServerEntry server : serverDao.getFromCampaign(campaign)) {
            if (server.getConversion()) {
                num++;
            }
        }
        return num;
    }

    public double getCTR(String campaign) {
        return (this.getNumClicks(campaign) / this.getNumImpressions(campaign));

    }

    //TODO do we use total cost or just click cost or just impressions cost
    public double getCPA(String campaign) {
        return (this.getTotalCost(campaign) / this.getConversions(campaign));
    }


    private double getTotalCost(String campaign) {

        return (this.getTotalClickCost(campaign) + this.getTotalImpressionsCost(campaign));

    }

    private ArrayList getTotalCostPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        ArrayList<Double> clickCosts = this.getTotalClickCostPerTime(campaign, start, end, duration);
        ArrayList<Double> impressionCosts = this.getTotalImpressionsCostPerTime(campaign, start, end, duration);
        ArrayList totalCosts = new ArrayList();

        for (int i = 0; i < clickCosts.size(); i++) {
            totalCosts.add(clickCosts.get(i) + impressionCosts.get(i));
        }

        return totalCosts;
    }

    private double getTotalClickCost(String campaign) {
        double cost = 0;
        for (Click click : clickDao.getFromCampaign(campaign)) {
            cost += click.getClickCost();
        }
        return cost;
    }

    private ArrayList<Double> getTotalClickCostPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        ArrayList costs = new ArrayList();

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            double total = 0;
            for (Click click : clickDao.getByDateAndCampaign(campaign, current, nextTime)) {
                total += click.getClickCost();
            }

            costs.add(total);
            current = nextTime;
        }

        return costs;

    }

    private double getTotalImpressionsCost(String campaign) {
        double cost = 0;
        for (Impression impression : impressionsDao.getFromCampaign(campaign)) {
            cost += impression.getImpressionCost();
        }

        return cost;
    }

    private ArrayList<Double> getTotalImpressionsCostPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        ArrayList costs = new ArrayList();

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            double total = 0;
            for (Impression impression : impressionsDao.getByDateAndCampaign(campaign, current, nextTime)) {
                total += impression.getImpressionCost();
            }

            costs.add(total);
            current = nextTime;
        }

        return costs;
    }


    public double getCPC(String campaign) {
        return (this.getTotalClickCost(campaign) / this.getNumClicks(campaign));
    }

    public double getCPM(String campaign) {
        return (this.getTotalCost(campaign) / (this.getNumImpressions(campaign)/1000));
    }

    public double getBounceRate(String campaign) {
        return (this.getNumBounces(campaign) / this.getNumClicks(campaign));
    }

    /**
     *
     * @param campaign which campaign to select data from
     * @param start the start date
     * @param end the end date
     * @param duration the time interval for each data point e.g. 1 hour
     * @return
     */
    public XYChart.Series getImpressionsPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Impressions");

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            series.getData().add(new XYChart.Data(current.toString(), impressionsDao.getByDateAndCampaign(campaign, current, nextTime).size()));
            current = nextTime;
        }

        return series;
    }

    private ArrayList getImpressionsPerTimeList(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {
        ArrayList impress = new ArrayList();
        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            impress.add(impressionsDao.getByDateAndCampaign(campaign, current, nextTime).size());
            current = nextTime;
        }

        return impress;
    }

    public XYChart.Series getClicksPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Clicks");

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            series.getData().add(new XYChart.Data(current.toString(), clickDao.getByDateAndCampaign(campaign, current, nextTime).size()));
            current = nextTime;
        }

        return series;
    }

    private ArrayList getClicksPerTimeList(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {
        LocalDateTime current = start;
        ArrayList<Integer> clicks = new ArrayList<>();

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            clicks.add(clickDao.getByDateAndCampaign(campaign, current, nextTime).size());
            current = nextTime;
        }

        return clicks;
    }

    public XYChart.Series getUniquesPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Uniques");

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            HashSet set = new HashSet<>();

            for (Click click : clickDao.getByDateAndCampaign(campaign, current, nextTime)) {
                set.add(click.getId());
            }

            series.getData().add(new XYChart.Data(current.toString(), set.size()));

            current = nextTime;
        }

        return series;
    }

    public XYChart.Series getBouncesPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Bounces");

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            int num = 0;
            //time spent defines bounce
            if (bounceDef) {

                for (ServerEntry server : serverDao.getByDateAndCampaign(campaign, current, nextTime)) {
                    //exitDate can be null!!!! - check if this actually works
                    if (server.getExitDate() != null) {
                        Duration timeSpent = Duration.between(server.getEntryDate(), server.getExitDate());
                        if (timeSpent.compareTo(bounceTime) <= 0) {
                            num ++;
                        }
                    }



                }

            }
            //pages viewed defines bounce
            else {
                for (ServerEntry server : serverDao.getByDateAndCampaign(campaign, current, nextTime)) {
                    if (server.getPageViews() < bouncePages) {
                        num ++;
                    }
                }
            }

            series.getData().add(new XYChart.Data(current.toString(), num));

            current = nextTime;
        }

        return series;
    }

    private ArrayList getBouncesPerTimeList (String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {
        LocalDateTime current = start;
        ArrayList bounces = new ArrayList();

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            int num = 0;
            //time spent defines bounce
            if (bounceDef) {

                for (ServerEntry server : serverDao.getByDateAndCampaign(campaign, current, nextTime)) {

                    if (server.getExitDate() != null) {
                        Duration timeSpent = Duration.between(server.getEntryDate(), server.getExitDate());
                        if (timeSpent.compareTo(bounceTime) <= 0) {
                            num ++;
                        }
                    }


                }

            }
            //pages viewed defines bounce
            else {
                for (ServerEntry server : serverDao.getByDateAndCampaign(campaign, current, nextTime)) {
                    if (server.getPageViews() < bouncePages) {
                        num ++;
                    }
                }
            }

            bounces.add(num);

            current = nextTime;
        }

        return bounces;
    }

    private ArrayList getConversionsPerTimeList(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {
        ArrayList conversions = new ArrayList();
        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            int num = 0;
            for (ServerEntry server : serverDao.getByDateAndCampaign(campaign, current, nextTime)) {
                if (server.getConversion()) {
                    num++;
                }
            }

            conversions.add(num);

            current = nextTime;
        }

        return conversions;
    }

    public XYChart.Series getConversionsPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Conversions");

        ArrayList con = this.getConversionsPerTimeList(campaign, start, end, duration);
        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.toString(), con.get(index)));

            current = nextTime;
            index ++;
        }

        return series;
    }

    public XYChart.Series getCTRPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("CTR");

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.toString(), clickDao.getByDateAndCampaign(campaign, current, nextTime).size()/impressionsDao.getByDateAndCampaign(campaign, current, nextTime).size()));

            current = nextTime;
        }

        return series;

    }

    public XYChart.Series getCPAPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("CPA");

        ArrayList<Double> costs = this.getTotalCostPerTime(campaign, start, end, duration);
        ArrayList<Integer> cons = this.getConversionsPerTimeList(campaign, start, end, duration);

        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.toString(), costs.get(index)/cons.get(index)));

            index ++;
            current = nextTime;
        }

        return series;

    }

    public XYChart.Series getCPCPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("CPC");

        ArrayList<Double> costs = this.getTotalCostPerTime(campaign, start, end, duration);
        ArrayList<Integer> clicks = this.getClicksPerTimeList(campaign, start, end, duration);

        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.toString(), costs.get(index)/clicks.get(index)));

            index ++;
            current = nextTime;
        }

        return series;

    }

    public XYChart.Series getCPMPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("CPM");

        ArrayList<Double> costs = this.getTotalCostPerTime(campaign, start, end, duration);
        ArrayList<Integer> impress = this.getImpressionsPerTimeList(campaign, start, end, duration);

        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.toString(), costs.get(index)/impress.get(index)));

            index ++;
            current = nextTime;
        }

        return series;

    }

    public XYChart.Series getBouncePerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Bounce");

        ArrayList<Integer> bounces = this.getBouncesPerTimeList(campaign, start, end, duration);
        ArrayList<Integer> clicks = this.getClicksPerTimeList(campaign, start, end, duration);

        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.toString(), bounces.get(index)/clicks.get(index)));

            index ++;
            current = nextTime;
        }

        return series;

    }





}