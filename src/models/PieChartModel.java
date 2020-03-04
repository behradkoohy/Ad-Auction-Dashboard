package models;

import daos.ImpressionDao;
import entities.Click;
import entities.Impression;
import entities.Impression.Age;
import entities.Impression.Gender;
import entities.Impression.Income;

import java.util.HashMap;
import java.util.List;


public class PieChartModel {
    private String campaign;
    private List<Impression> impressions;
    private ImpressionDao impressionDao;
    private Integer nrImpressions;

    private Age[] AGES= {Age.LESS25, Age.FROM25TO34, Age.FROM35TO44, Age.FROM45TO54, Age.OVER54};
    private Gender[] GENDERS = {Gender.MALE, Gender.FEMALE};
    private Income[] INCOMES = {Income.LOW, Income.MEDIUM, Income.HIGH};

    public PieChartModel(String campaign, List<Impression> impressions){
        this.campaign = campaign;
        this.impressions = impressions;
        this.impressionDao = new ImpressionDao();
        this.nrImpressions = this.impressions.size();
    }

    public HashMap<String, Integer> getDistributions(){
        HashMap<String, Integer> distr = new HashMap<String, Integer>();

        int ages[] = {0, 0, 0, 0, 0};
        int genders[] = {0, 0};
        int incomes[] = {0, 0, 0};

        for( Impression impression : this.impressions){
            switch(impression.getAge()){
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

        distr.put("lt25", (int)(Double.valueOf(ages[0]) / Double.valueOf(this.nrImpressions) * 100));
        distr.put("btwn2534", (int)(Double.valueOf(ages[1]) / Double.valueOf(this.nrImpressions) * 100));
        distr.put("btwn3544", (int)(Double.valueOf(ages[2]) / Double.valueOf(this.nrImpressions) * 100));
        distr.put("btwn4554", (int)(Double.valueOf(ages[3]) / Double.valueOf(this.nrImpressions) * 100));
        distr.put("gt55", (int)(Double.valueOf(ages[4]) / Double.valueOf(this.nrImpressions) * 100));
        distr.put("men", (int)(Double.valueOf(genders[0]) / Double.valueOf( this.nrImpressions ) * 100));
        distr.put("women", (int)(Double.valueOf(genders[1]) / Double.valueOf( this.nrImpressions ) * 100));
        distr.put("low", (int)(Double.valueOf(incomes[0]) / Double.valueOf(this.nrImpressions) * 100));
        distr.put("medium", (int)(Double.valueOf(incomes[1]) / Double.valueOf(this.nrImpressions) * 100));
        distr.put("high", (int)(Double.valueOf(incomes[2]) / Double.valueOf(this.nrImpressions) * 100));

        return distr;
    }


}
