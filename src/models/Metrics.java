package models;


import daos.ClickDao;
import daos.DaoInjector;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.Click;
import entities.EntityAbstract;
import entities.Impression;
import entities.ServerEntry;
import javafx.scene.chart.XYChart;

import java.beans.IntrospectionException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.function.Predicate;


public class Metrics {

    //false = pages -- true = time spent -- change
    private String campaign = null;

    private boolean bounceDef = false;
    private int bouncePages = 3;
    private Duration bounceTime = Duration.ofSeconds(5);

    private static DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm dd/MM/yy");


    private ClickDao clickDao = DaoInjector.newClickDao();
    private ImpressionDao impressionDao = DaoInjector.newImpressionDao();
    private ServerEntryDao serverEntryDao = DaoInjector.newServerEntryDao();

    private Filter filter;

    //Potentially dont need to cache at this point since implemented dao caching
    private HashMap<Tuple, Double> cacheSingle = new HashMap<>();

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

    public Metrics() {
        this.filter = new Filter(true, true,
                true, true, true, true, true,
                true, true, true);
    }

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }

    public Double getNumImpressions() {
        Tuple tuple = new Tuple(NUMIMPRESS, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }

        Double num = (double) impressionDao.getFromCampaign(campaign).size();
        cacheSingle.put(tuple, num);

        return num;
    }

    public Double getNumClicks() {
        Tuple tuple = new Tuple(NUMCLICKS, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        Double num = (double) clickDao.getFromCampaign(campaign).size();
        cacheSingle.put(tuple, num);
        return num;
    }


    public Double getNumUniqs() {
        Tuple tuple = new Tuple(NUMUNIQ, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }

        HashSet set = new HashSet<>();

        for (Click click : clickDao.getFromCampaign(campaign)) {
            set.add(click.getId());
        }
        Double num = (double) set.size();
        cacheSingle.put(tuple, num);

        return (double) set.size();

    }

    public void setBounceTime(Duration duration) {

        bounceDef = true;
        bounceTime = duration;

    }

    public void setBouncePages(int bouncePages) {

        bounceDef = false;
        this.bouncePages = bouncePages;
        System.out.println("Bounce pages is now: " + this.bouncePages);

    }


    public Double getNumBounces() {
        Tuple tuple = new Tuple(BOUNCE, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
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
        cacheSingle.put(tuple, num);
        return num;
    }

    public Double getConversions() {
        Tuple tuple = new Tuple(CONVER, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        Double num = 0.0;
        for (ServerEntry server : serverEntryDao.getFromCampaign(campaign)) {
            if (server.getConversion()) {
                num++;
            }
        }
        cacheSingle.put(tuple, num);
        return num;
    }

    public Double getCTR() {
        Tuple tuple = new Tuple(CTR, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        Double num = this.getNumClicks()/this.getNumImpressions();
        cacheSingle.put(tuple, num);
        return num;

    }

    //TODO do we use total cost or just click cost or just impressions cost
    public Double getCPA() {
        Tuple tuple = new Tuple(CPA, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        Double num = this.getTotalCost() / this.getConversions();
        cacheSingle.put(tuple, num);
        return num;
    }


    public Double getTotalCost() {
        Tuple tuple = new Tuple(TOTALCOST, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        Double num = this.getTotalClickCost() + this.getTotalImpressionsCost();
        cacheSingle.put(tuple, num);

        return num;

    }

    private ArrayList<Double> getTotalCostPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        ArrayList<Double> clickCosts = this.getTotalClickCostPerTime(start, end, duration);
        ArrayList<Double> impressionCosts = this.getTotalImpressionsCostPerTime(start, end, duration);
        ArrayList<Double> totalCosts = new ArrayList();

        for (int i = 0; i < clickCosts.size(); i++) {
            totalCosts.add(clickCosts.get(i) + impressionCosts.get(i));
        }

        return totalCosts;
    }

    private Double getTotalClickCost() {
        Tuple tuple = new Tuple(TOTALCLICK, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        double cost = 0;
        for (Click click : clickDao.getFromCampaign(campaign)) {
            cost += click.getClickCost();
        }
        cacheSingle.put(tuple, cost);
        return cost;
    }

    private ArrayList<Double> getTotalClickCostPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        ArrayList<Double> costs = new ArrayList();

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

    private Double getTotalImpressionsCost() {
        Tuple tuple = new Tuple(TOTALIMPRESS, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        double cost = 0;

        for (Impression impression : impressionDao.getFromCampaign(campaign)) {
            cost += impression.getImpressionCost();
        }
        cacheSingle.put(tuple, cost);
        return cost;
    }

    private ArrayList<Double> getTotalImpressionsCostPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        ArrayList<Double> costs = new ArrayList();

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


    public Double getCPC() {
        Tuple tuple = new Tuple(CPC, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        Double num = this.getTotalClickCost() / this.getNumClicks();
        cacheSingle.put(tuple, num);
        return num;
    }

    public Double getCPM() {
        Tuple tuple = new Tuple(CPM, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        Double num = this.getTotalCost() / (this.getNumImpressions()/1000);
        cacheSingle.put(tuple, num);
        return num;
    }

    public Double getBounceRate() {
        Tuple tuple = new Tuple(BOUNCE, campaign);
        if (cacheSingle.containsKey(tuple)) {
            return cacheSingle.get(tuple);
        }
        Double num = this.getNumBounces()/this.getNumClicks();
        cacheSingle.put(tuple, num);
        return num;
    }

    /**
     *
     * @param start the start date
     * @param end the end date
     * @param duration the time interval for each data point e.g. 1 hour
     * @return
     */
    public XYChart.Series getImpressionsPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Impressions");

//        LocalDateTime current = start;
//
//        while (current.isBefore(end)) {
//            LocalDateTime nextTime = current.plus(duration);
//            series.getData().add(new XYChart.Data(current.format(formatter), impressionDao.getByDateAndCampaign(campaign, current, nextTime).size()));
//            current = nextTime;
//        }
//
        List impress = impressionDao.getByDateAndCampaign(campaign, start, end);

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            LocalDateTime finalCurrent = current;
            Predicate<EntityAbstract> pred = n -> n.getDate().isAfter(finalCurrent) && n.getDate().isBefore(nextTime);

            series.getData().add(new XYChart.Data(current.format(formatter), (impress.stream().filter(pred.and(filter)).count())));
            current = nextTime;
        }


        return series;
    }

    private ArrayList<Integer> getImpressionsPerTimeList(LocalDateTime start, LocalDateTime end, Duration duration) {
        ArrayList<Integer> impress = new ArrayList();
        List impressData = impressionDao.getByDateAndCampaign(campaign, start, end);
        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            LocalDateTime finalCurrent = current;
            Predicate<EntityAbstract> pred = n -> n.getDate().isAfter(finalCurrent) && n.getDate().isBefore(nextTime);

            impress.add((int) impressData.stream().filter(pred.and(filter)).count());
            current = nextTime;
        }

        return impress;
    }

    public XYChart.Series getClicksPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Clicks");

        LocalDateTime current = start;
        List data = clickDao.getByDateAndCampaign(campaign, start, end);



        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            LocalDateTime finalCurrent = current;
            Predicate<EntityAbstract> pred = n -> n.getDate().isAfter(finalCurrent) && n.getDate().isBefore(nextTime);

            series.getData().add(new XYChart.Data(current.format(formatter), data.stream().filter(pred.and(filter)).count()));
            current = nextTime;
        }

        return series;
    }

    private ArrayList<Integer> getClicksPerTimeList(LocalDateTime start, LocalDateTime end, Duration duration) {
        LocalDateTime current = start;
        ArrayList<Integer> clicks = new ArrayList<>();
        List data = clickDao.getByDateAndCampaign(campaign, start, end);

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            LocalDateTime finalCurrent = current;
            Predicate<EntityAbstract> pred = n -> n.getDate().isAfter(finalCurrent) && n.getDate().isBefore(nextTime);

            clicks.add((int) data.stream().filter(pred.and(filter)).count());
            current = nextTime;
        }

        return clicks;
    }

    public static <T> Predicate<T> distinctBy(Function<? super T, ?> keyFunc) {
        Set set = ConcurrentHashMap.newKeySet();
        return a -> set.add(keyFunc.apply(a));
    }

    
    public XYChart.Series getUniquesPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Uniques");

        List data = clickDao.getByDateAndCampaign(campaign, start, end);

        LocalDateTime current = start;



        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            LocalDateTime finalCurrent = current;
            Predicate<EntityAbstract> timePred = n -> n.getDate().isAfter(finalCurrent) && n.getDate().isBefore(nextTime);

//            HashSet set = new HashSet<>();
//
//            for (Click click : clickDao.getByDateAndCampaign(campaign, current, nextTime)) {
//                set.add(click.getId());
//            }


            series.getData().add(new XYChart.Data(current.format(formatter), data.stream().filter(timePred.and(distinctBy(EntityAbstract::getId)).and(filter)).count()));

            current = nextTime;
        }

        return series;
    }

    /**
     * gives number of bounces
     * @param start
     * @param end
     * @param duration
     * @return
     */
    public XYChart.Series getBouncesPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Bounces");
        ArrayList bouncey = getBouncesPerTimeList(start, end, duration);
        int index = 0;

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.format(formatter), bouncey.get(index)));

            current = nextTime;
            index++;
        }

        return series;
    }

    private ArrayList<Integer> getBouncesPerTimeList (LocalDateTime start, LocalDateTime end, Duration duration) {
        LocalDateTime current = start;
        ArrayList<Integer> bounces = new ArrayList();
        List data = serverEntryDao.getByDateAndCampaign(campaign, start, end);

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            LocalDateTime finalCurrent = current;

            Predicate<EntityAbstract> pred = n -> n.getDate().isAfter(finalCurrent) && n.getDate().isBefore(nextTime);
            Predicate<ServerEntry> serverNullPred = n -> n.getExitDate() != null;
            Predicate<ServerEntry> serverDurationPred = n -> Duration.between(n.getEntryDate(), n.getExitDate()).compareTo(bounceTime) < 0;
            Predicate<ServerEntry> serverPagePred = n -> n.getPageViews() < bouncePages;


            int num = 0;
            //time spent defines bounce
            if (bounceDef) {
                //have to remove entries with no exit date first
                bounces.add((int) data.stream().filter(pred).filter(filter).filter(serverNullPred).filter(serverDurationPred).count());


//                for (ServerEntry server : serverEntryDao.getByDateAndCampaign(campaign, current, nextTime)) {
//
//                    if (server.getExitDate() != null) {
//                        Duration timeSpent = Duration.between(server.getEntryDate(), server.getExitDate());
//                        if (timeSpent.compareTo(bounceTime) <= 0) {
//                            num ++;
//                        }
//                    }
//
//
//                }

            }
            //pages viewed defines bounce
            else {

                bounces.add((int) data.stream().filter(pred).filter(serverPagePred).filter(filter).count());
//                for (ServerEntry server : serverEntryDao.getByDateAndCampaign(campaign, current, nextTime)) {
//                    if (server.getPageViews() < bouncePages) {
//                        num ++;
//                    }
//                }
//            }
//
//            bounces.add(num);


            }
            current = nextTime;
        }
        return bounces;
    }

    private ArrayList<Integer> getConversionsPerTimeList(LocalDateTime start, LocalDateTime end, Duration duration) {
        ArrayList<Integer> conversions = new ArrayList();
        LocalDateTime current = start;
        List data = serverEntryDao.getByDateAndCampaign(campaign, start, end);

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            LocalDateTime finalCurrent = current;
            Predicate<EntityAbstract> pred = n -> n.getDate().isAfter(finalCurrent) && n.getDate().isBefore(nextTime);
            Predicate<ServerEntry> converPred = n -> n.getConversion();

            conversions.add((int) data.stream().filter(pred).filter(filter).filter(converPred).count());

//            int num = 0;
//            for (ServerEntry server : serverEntryDao.getByDateAndCampaign(campaign, current, nextTime)) {
//                if (server.getConversion()) {
//                    num++;
//                }
//            }
//
//            conversions.add(num);

            current = nextTime;
        }

        return conversions;
    }

    public XYChart.Series getConversionsPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Conversions");

        ArrayList con = this.getConversionsPerTimeList(start, end, duration);
        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.format(formatter), con.get(index)));

            current = nextTime;
            index ++;
        }

        return series;
    }

    public XYChart.Series getCTRPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("CTR");

        List clickData = clickDao.getByDateAndCampaign(campaign, start, end);
        List impressData = impressionDao.getByDateAndCampaign(campaign, start, end);

        LocalDateTime current = start;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);
            LocalDateTime finalCurrent = current;
            Predicate<EntityAbstract> pred = n -> n.getDate().isAfter(finalCurrent) && n.getDate().isBefore(nextTime);

            series.getData().add(new XYChart.Data(current.format(formatter), (double) clickData.stream().filter(pred.and(filter)).count()/(impressData.stream().filter(pred.and(filter)).count()+1)));

            current = nextTime;
        }

        return series;

    }

    public XYChart.Series getCPAPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("CPA");

        ArrayList<Double> costs = this.getTotalCostPerTime(start, end, duration);
        ArrayList<Integer> cons = this.getConversionsPerTimeList(start, end, duration);

        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.format(formatter),  costs.get(index)/(cons.get(index)+1)));

            current = nextTime;
            index++;
        }

        return series;

    }

    public XYChart.Series getCPCPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("CPC");

        ArrayList<Double> costs = this.getTotalCostPerTime(start, end, duration);
        ArrayList<Integer> clicks = this.getClicksPerTimeList(start, end, duration);

        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.format(formatter), Double.valueOf(costs.get(index))/(clicks.get(index)+1)));

            index ++;
            current = nextTime;
        }

        return series;

    }

    public XYChart.Series getCPMPerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("CPM");

        ArrayList<Double> costs = this.getTotalCostPerTime(start, end, duration);
        ArrayList<Integer> impress = this.getImpressionsPerTimeList(start, end, duration);

        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);


            series.getData().add(new XYChart.Data(current.format(formatter), costs.get(index)/(impress.get(index)+1)));

            index ++;
            current = nextTime;
        }

        return series;

    }

    /**
     * bounce RATE
     * @param start
     * @param end
     * @param duration
     * @return
     */
    public XYChart.Series getBouncePerTime(LocalDateTime start, LocalDateTime end, Duration duration) {

        XYChart.Series series = new XYChart.Series();
        series.setName("Bounce");

        ArrayList<Integer> bounces = this.getBouncesPerTimeList(start, end, duration);
        ArrayList<Integer> clicks = this.getClicksPerTimeList(start, end, duration);

        LocalDateTime current = start;
        int index = 0;

        while (current.isBefore(end)) {
            LocalDateTime nextTime = current.plus(duration);

            series.getData().add(new XYChart.Data(current.format(formatter), Double.valueOf(bounces.get(index))/(clicks.get(index)+1)));

            index ++;
            current = nextTime;
        }

        return series;

    }

    public void setFilter(Filter filter) {
        System.out.println(this.filter);
        System.out.println("Change filter");
        this.filter = filter;
        System.out.println(this.filter);
    }

    private static class Tuple {
        private int name;
        private String camp;

        private Tuple(int name, String camp) {
            this.name = name;
            this.camp = camp;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Tuple tuple = (Tuple) o;
            return name == tuple.name &&
                    camp.equals(tuple.camp);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, camp);
        }
    }







}