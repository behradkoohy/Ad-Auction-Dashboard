package models;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.Click;
import entities.Impression;
import entities.ServerEntry;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.HashSet;

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
                Duration duration = Duration.between(server.getEntryDate(), server.getExitDate());
                if (duration.compareTo(bounceTime) <= 0) {
                    num ++;
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

    public double getCPA(String campaign) {
        return (this.getTotalCost(campaign) / this.getConversions(campaign));
    }


    private double getTotalCost(String campaign) {

        return (this.getTotalClickCost(campaign) + this.getTotalImpressionsCost(campaign));

    }

    private double getTotalClickCost(String campaign) {
        double cost = 0;
        for (Click click : clickDao.getFromCampaign(campaign)) {
            cost += click.getClickCost();
        }
        return cost;
    }

    private double getTotalImpressionsCost(String campaign) {
        double cost = 0;
        for (Impression impression : impressionsDao.getFromCampaign(campaign)) {
            cost += impression.getImpressionCost();
        }

        return cost;
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



}