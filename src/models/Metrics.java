package models;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.Click;
import entities.Impression;
import entities.ServerEntry;
import javafx.scene.chart.XYChart;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;


public class Metrics {

    //false = pages -- true = time spent -- change
    private boolean bounceDef = false;
    private int bouncePages = 1;
    private Duration bounceTime = Duration.ofSeconds(5);

    private ClickDao clickDao;
    private ImpressionDao impressionDao;
    private ServerEntryDao serverEntryDao;

    private HashMap<Twople, Double> cacheSingle;

    private static int NUMIMPRESS = 0;
    private static int NUMCLICKS = 1;
    private static int NUMUNIQ = 2;
    private static int CONVER = 3;
    private static int CTR = 4;
    private static int CPA = 5;
    private static int TOTALCOST = 6;
    private static int TOTALCLICK = 7;
    private static int TOTALIMPRESS = 8;
    private static int CPC = 9;
    private static int CPM = 10;
    private static int BOUNCE = 11;


    public Metrics(ClickDao clickDao, ImpressionDao impressionDao, ServerEntryDao serverEntryDao) {
        this.clickDao = clickDao;
        this.impressionDao = impressionDao;
        this.serverEntryDao = serverEntryDao;
        this.cacheSingle = new HashMap<>();
    }

    public Double getNumImpressions(String campaign) {
        Twople twople = new Twople(NUMIMPRESS, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }

        Double num = (double) impressionDao.getFromCampaign(campaign).size();
        cacheSingle.put(twople, num);

        return num;
    }

    public Double getNumClicks(String campaign) {
        Twople twople = new Twople(NUMCLICKS, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        Double num = (double) clickDao.getFromCampaign(campaign).size();
        cacheSingle.put(twople, num);
        return num;
    }


    public Double getNumUniqs(String campaign) {
        Twople twople = new Twople(NUMUNIQ, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }

        HashSet set = new HashSet<>();

        for (Click click : clickDao.getFromCampaign(campaign)) {
            set.add(click.getId());
        }
        Double num = (double) set.size();
        cacheSingle.put(twople, num);

        return (double) set.size();

    }

    public void setBounceTime(Duration duration) {
        bounceDef = true;

        bounceTime = duration;
    }

    public void setBouncePages(int bouncePages) {
        bounceDef = false;
        this.bouncePages = bouncePages;
    }


    public Double getNumBounces(String campaign) {
        Twople twople = new Twople(BOUNCE, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }

        Double num = 0.0;
        //time spent defines bounce
        if (bounceDef) {

            for (ServerEntry server : serverEntryDao.getFromCampaign(campaign)) {

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
            for (ServerEntry server : serverEntryDao.getFromCampaign(campaign)) {
                if (server.getPageViews() <= bouncePages) {
                    num ++;
                }
            }
        }
        cacheSingle.put(twople, num);
        return num;
    }

    public Double getConversions(String campaign) {
        Twople twople = new Twople(CONVER, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        Double num = 0.0;
        for (ServerEntry server : serverEntryDao.getFromCampaign(campaign)) {
            if (server.getConversion()) {
                num++;
            }
        }
        cacheSingle.put(twople, num);
        return num;
    }

    public Double getCTR(String campaign) {
        Twople twople = new Twople(CTR, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        Double num = this.getNumClicks(campaign)/this.getNumImpressions(campaign);
        cacheSingle.put(twople, num);
        return num;

    }

    //TODO do we use total cost or just click cost or just impressions cost
    public Double getCPA(String campaign) {
        Twople twople = new Twople(CPA, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        Double num = this.getTotalCost(campaign) / this.getConversions(campaign);
        cacheSingle.put(twople, num);
        return num;
    }


    public Double getTotalCost(String campaign) {
        Twople twople = new Twople(TOTALCOST, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        Double num = this.getTotalClickCost(campaign) + this.getTotalImpressionsCost(campaign);
        cacheSingle.put(twople, num);

        return num;

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

    private Double getTotalClickCost(String campaign) {
        Twople twople = new Twople(TOTALCLICK, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        double cost = 0;
        for (Click click : clickDao.getFromCampaign(campaign)) {
            cost += click.getClickCost();
        }
        cacheSingle.put(twople, cost);
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

    private Double getTotalImpressionsCost(String campaign) {
        Twople twople = new Twople(TOTALIMPRESS, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        double cost = 0;

        for (Impression impression : impressionDao.getFromCampaign(campaign)) {
            cost += impression.getImpressionCost();
        }
        cacheSingle.put(twople, cost);
        return cost;
    }

    private ArrayList<Double> getTotalImpressionsCostPerTime(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {

        ArrayList costs = new ArrayList();

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            double total = 0;
            for (Impression impression : impressionDao.getByDateAndCampaign(campaign, current, nextTime)) {
                total += impression.getImpressionCost();
            }

            costs.add(total);
            current = nextTime;
        }

        return costs;
    }


    public Double getCPC(String campaign) {
        Twople twople = new Twople(CPC, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        Double num = this.getTotalClickCost(campaign) / this.getNumClicks(campaign);
        cacheSingle.put(twople, num);
        return num;
    }

    public Double getCPM(String campaign) {
        Twople twople = new Twople(CPM, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        Double num = this.getTotalCost(campaign) / (this.getNumImpressions(campaign)/1000);
        cacheSingle.put(twople, num);
        return num;
    }

    public Double getBounceRate(String campaign) {
        Twople twople = new Twople(BOUNCE, campaign);
        if (cacheSingle.containsKey(twople)) {
            return cacheSingle.get(twople);
        }
        Double num = this.getNumBounces(campaign)/this.getNumClicks(campaign);
        cacheSingle.put(twople, num);
        return num;
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
            series.getData().add(new XYChart.Data(current.toString(), impressionDao.getByDateAndCampaign(campaign, current, nextTime).size()));
            current = nextTime;
        }

        return series;
    }

    private ArrayList getImpressionsPerTimeList(String campaign, LocalDateTime start, LocalDateTime end, Duration duration) {
        ArrayList impress = new ArrayList();
        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            impress.add(impressionDao.getByDateAndCampaign(campaign, current, nextTime).size());
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

                for (ServerEntry server : serverEntryDao.getByDateAndCampaign(campaign, current, nextTime)) {
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
                for (ServerEntry server : serverEntryDao.getByDateAndCampaign(campaign, current, nextTime)) {
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

                for (ServerEntry server : serverEntryDao.getByDateAndCampaign(campaign, current, nextTime)) {

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
                for (ServerEntry server : serverEntryDao.getByDateAndCampaign(campaign, current, nextTime)) {
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
            for (ServerEntry server : serverEntryDao.getByDateAndCampaign(campaign, current, nextTime)) {
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

            series.getData().add(new XYChart.Data(current.toString(), clickDao.getByDateAndCampaign(campaign, current, nextTime).size()/impressionDao.getByDateAndCampaign(campaign, current, nextTime).size()));

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

    public class Twople {
        private int name;
        private String camp;

        public Twople(int name, String camp) {
            this.name = name;
            this.camp = camp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Twople twople = (Twople) o;
            return name == twople.name &&
                    camp.equals(twople.camp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, camp);
        }
    }







}