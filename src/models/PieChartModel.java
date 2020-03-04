package models;

import daos.ImpressionDao;
import entities.Impression.Age;
import entities.Impression.Gender;
import entities.Impression.Income;

import java.util.HashMap;


public class PieChartModel {
    private String campaign;
    private Integer nrImpressions;
    private ImpressionDao impressionDao;

    private Age[] AGES= {Age.LESS25, Age.FROM25TO34, Age.FROM35TO44, Age.FROM45TO54, Age.OVER54};
    private Gender[] GENDERS = {Gender.MALE, Gender.FEMALE};
    private Income[] INCOMES = {Income.LOW, Income.MEDIUM, Income.HIGH};

    public PieChartModel(String campaign, int nrImpressions){
        this.campaign = campaign;
        this.nrImpressions = nrImpressions;
        this.impressionDao = new ImpressionDao();
    }

    public HashMap<String, Integer> getAgeDistributions(){
        HashMap<String, Integer> ages = new HashMap<String, Integer>();
        ages.put("lt25", (int)(Double.valueOf( this.impressionDao.getByAge(this.campaign, AGES[0]).size()) / Double.valueOf(this.nrImpressions) * 100));
        ages.put("btwn2534", (int)(Double.valueOf( this.impressionDao.getByAge(this.campaign, AGES[1]).size()) / Double.valueOf(this.nrImpressions) * 100));
        ages.put("btwn3544", (int)(Double.valueOf( this.impressionDao.getByAge(this.campaign, AGES[2]).size()) / Double.valueOf(this.nrImpressions) * 100));
        ages.put("btwn4554", (int)(Double.valueOf( this.impressionDao.getByAge(this.campaign, AGES[3]).size()) / Double.valueOf(this.nrImpressions) * 100));
        ages.put("gt55", (int)(Double.valueOf( this.impressionDao.getByAge(this.campaign, AGES[4]).size()) / Double.valueOf(this.nrImpressions) * 100));

        return ages;
    }

    public HashMap<String, Integer> getGenderDistributions(){
        HashMap<String, Integer> genders = new HashMap<String, Integer>();
        genders.put("men", (int)(Double.valueOf( this.impressionDao.getByGender(this.campaign, GENDERS[0]).size()) / Double.valueOf( this.nrImpressions ) * 100));
        genders.put("women", (int)(Double.valueOf( this.impressionDao.getByGender(this.campaign, GENDERS[1]).size()) / Double.valueOf( this.nrImpressions ) * 100));

        return genders;
    }

    public HashMap<String, Integer> getIncomeDistributions(){
        HashMap<String, Integer> incomes = new HashMap<String, Integer>();
        incomes.put("low", (int)(Double.valueOf(this.impressionDao.getByIncome(this.campaign, INCOMES[0]).size()) / Double.valueOf(this.nrImpressions) * 100));
        incomes.put("medium", (int)(Double.valueOf(this.impressionDao.getByIncome(this.campaign, INCOMES[1]).size()) / Double.valueOf(this.nrImpressions) * 100));
        incomes.put("high", (int)(Double.valueOf(this.impressionDao.getByIncome(this.campaign, INCOMES[2]).size()) / Double.valueOf(this.nrImpressions) * 100));

        return incomes;
    }
}
