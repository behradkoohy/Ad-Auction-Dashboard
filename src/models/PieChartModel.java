package models;

import daos.DaoInjector;
import daos.ImpressionDao;
import entities.Impression;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


public class PieChartModel {
    private String campaign = null;
    private LocalDateTime start;
    private LocalDateTime end;
    private ImpressionDao impressionDao = DaoInjector.newImpressionDao();

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }
    public void setStart(LocalDateTime start) { this.start = start; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public HashMap<String, Integer> getDistributions() {
        System.out.println(start);
        System.out.println(end);
        List<Impression> impressions = this.impressionDao.getByDateAndCampaign(campaign, start, end);
        int nrImpressions = impressions.size();
        HashMap<String, Integer> distr = new HashMap<String, Integer>();

        int[] ages = {0, 0, 0, 0, 0};
        int[] genders = {0, 0};
        int[] incomes = {0, 0, 0};

        for(Impression impression : impressions){
            switch(impression.getAge()) {
                case LESS25:
                    ages[0]++;
                    break;
                case FROM25TO34:
                    ages[1]++;
                    break;
                case FROM35TO44:
                    ages[2]++;
                    break;
                case FROM45TO54:
                    ages[3]++;
                    break;
                case OVER54:
                    ages[4]++;
                    break;
            }

            switch (impression.getGender()){
                case MALE:
                    genders[0]++;
                    break;
                case FEMALE:
                    genders[1]++;
                    break;
            }

            switch (impression.getIncome()){
                case LOW:
                    incomes[0]++;
                    break;
                case MEDIUM:
                    incomes[1]++;
                    break;
                case HIGH:
                    incomes[2]++;
                    break;
            }
        }

        distr.put("lt25", (int) ((double) ages[0] / (double) nrImpressions * 100));
        distr.put("btwn2534", (int) ((double) ages[1] / (double) nrImpressions * 100));
        distr.put("btwn3544", (int) ((double) ages[2] / (double) nrImpressions * 100));
        distr.put("btwn4554", (int) ((double) ages[3] / (double) nrImpressions * 100));
        distr.put("gt55", (int) ((double) ages[4] / (double) nrImpressions * 100));
        distr.put("men", (int) ((double) genders[0] / (double) nrImpressions * 100));
        distr.put("women", (int) ((double) genders[1] / (double) nrImpressions * 100));
        distr.put("low", (int) ((double) incomes[0] / (double) nrImpressions * 100));
        distr.put("medium", (int) ((double) incomes[1] / (double) nrImpressions * 100));
        distr.put("high", (int) ((double) incomes[2] / (double) nrImpressions * 100));

        return distr;
    }


}
