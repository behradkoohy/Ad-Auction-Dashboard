package models;

import daos.ClickDao;
import daos.DaoInjector;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import entities.EntityAbstract;
import entities.Impression;
import entities.User;
import javafx.scene.chart.PieChart;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PieChartModel {
    private String campaign = null;
    private LocalDateTime start;
    private LocalDateTime end;
    private ImpressionDao impressionDao = DaoInjector.newImpressionDao();
    private ClickDao clickDao = DaoInjector.newClickDao();
    private ServerEntryDao serverEntryDao = DaoInjector.newServerEntryDao();

    public void setCampaign(String campaign) {
        this.campaign = campaign;
    }
    public void setStart(LocalDateTime start) { this.start = start; }
    public void setEnd(LocalDateTime end) { this.end = end; }

    public HashMap<String, Integer> getDistributions(String type) {

        List<? extends EntityAbstract> listData;

        switch (type) {
            case "server":
                listData = this.serverEntryDao.getByDateAndCampaign(campaign, start, end);
                break;

            case "click":
                listData = this.clickDao.getByDateAndCampaign(campaign, start, end);
                break;

            default:
                listData = this.impressionDao.getByDateAndCampaign(campaign, start, end);
        }

        int number = listData.size();
        HashMap<String, Integer> distr = new HashMap<String, Integer>();

        int[] ages = {0, 0, 0, 0, 0};
        int[] genders = {0, 0};
        int[] incomes = {0, 0, 0};
        int[] contexts = {0, 0, 0, 0, 0, 0};

        ages[0] = (int) listData.stream().filter(n -> n.getAge() == User.Age.LESS25).count();
        ages[1] = (int) listData.stream().filter(n -> n.getAge() == User.Age.FROM25TO34).count();
        ages[2] = (int) listData.stream().filter(n -> n.getAge() == User.Age.FROM35TO44).count();
        ages[3] = (int) listData.stream().filter(n -> n.getAge() == User.Age.FROM45TO54).count();
        ages[4] = (int) listData.stream().filter(n -> n.getAge() == User.Age.OVER54).count();

        genders[0] = (int) listData.stream().filter(n -> n.getGender() == User.Gender.MALE).count();
        genders[1] = (int) listData.stream().filter(n -> n.getGender() == User.Gender.FEMALE).count();

        incomes[0] = (int) listData.stream().filter(n -> n.getIncome() == User.Income.LOW).count();
        incomes[1] = (int) listData.stream().filter(n -> n.getIncome() == User.Income.MEDIUM).count();
        incomes[2] = (int) listData.stream().filter(n -> n.getIncome() == User.Income.HIGH).count();

        contexts[0] = (int) listData.stream().filter(n -> n.getContext() == User.Context.BLOG).count();
        contexts[1] = (int) listData.stream().filter(n -> n.getContext() == User.Context.NEWS).count();
        contexts[2] = (int) listData.stream().filter(n -> n.getContext() == User.Context.SOCIALMEDIA).count();
        contexts[3] = (int) listData.stream().filter(n -> n.getContext() == User.Context.SHOPPING).count();
        contexts[4] = (int) listData.stream().filter(n -> n.getContext() == User.Context.HOBBIES).count();
        contexts[5] = (int) listData.stream().filter(n -> n.getContext() == User.Context.TRAVEL).count();

//        for(Impression impression : impressions){
//            switch(impression.getAge()) {
//                case LESS25:
//                    ages[0]++;
//                    break;
//                case FROM25TO34:
//                    ages[1]++;
//                    break;
//                case FROM35TO44:
//                    ages[2]++;
//                    break;
//                case FROM45TO54:
//                    ages[3]++;
//                    break;
//                case OVER54:
//                    ages[4]++;
//                    break;
//            }
//
//            switch (impression.getGender()){
//                case MALE:
//                    genders[0]++;
//                    break;
//                case FEMALE:
//                    genders[1]++;
//                    break;
//            }
//
//            switch (impression.getIncome()){
//                case LOW:
//                    incomes[0]++;
//                    break;
//                case MEDIUM:
//                    incomes[1]++;
//                    break;
//                case HIGH:
//                    incomes[2]++;
//                    break;
//            }
//        }

        distr.put("lt25", (int) ((double) ages[0] / (double) number * 100));
        distr.put("btwn2534", (int) ((double) ages[1] / (double) number * 100));
        distr.put("btwn3544", (int) ((double) ages[2] / (double) number * 100));
        distr.put("btwn4554", (int) ((double) ages[3] / (double) number * 100));
        distr.put("gt55", (int) ((double) ages[4] / (double) number * 100));
        distr.put("men", (int) ((double) genders[0] / (double) number * 100));
        distr.put("women", (int) ((double) genders[1] / (double) number * 100));
        distr.put("low", (int) ((double) incomes[0] / (double) number * 100));
        distr.put("medium", (int) ((double) incomes[1] / (double) number * 100));
        distr.put("high", (int) ((double) incomes[2] / (double) number * 100));
        distr.put("blog", (int) ((double) contexts[0] / (double) number * 100));
        distr.put("news", (int) ((double) contexts[1] / (double) number * 100));
        distr.put("socialmedia", (int) ((double) contexts[2] / (double) number * 100));
        distr.put("shopping", (int) ((double) contexts[3] / (double) number * 100));
        distr.put("hobbies", (int) ((double) contexts[4] / (double) number * 100));
        distr.put("travel", (int) ((double) contexts[5] / (double) number * 100));

        return distr;
    }

    public HashMap<String, Integer> getContextDistributions() {
        List<Impression> impressions = this.impressionDao.getByDateAndCampaign(campaign, start, end);
        int nrImpressions = impressions.size();
        HashMap<String, Integer> distr = new HashMap<String, Integer>();

        int[] contexts = {0, 0, 0, 0, 0, 0};

        for(Impression impression : impressions){
            switch(impression.getContext()) {
                case BLOG:
                    contexts[0]++;
                    break;
                case NEWS:
                    contexts[1]++;
                    break;
                case SOCIALMEDIA:
                    contexts[2]++;
                    break;
                case SHOPPING:
                    contexts[3]++;
                    break;
                case HOBBIES:
                    contexts[4]++;
                    break;
                case TRAVEL:
                    contexts[5]++;
                    break;
            }
        }
        distr.put("blog", (int) ((double) contexts[0] / (double) nrImpressions * 100));
        distr.put("news", (int) ((double) contexts[1] / (double) nrImpressions * 100));
        distr.put("socialmedia", (int) ((double) contexts[2] / (double) nrImpressions * 100));
        distr.put("shopping", (int) ((double) contexts[3] / (double) nrImpressions * 100));
        distr.put("hobbies", (int) ((double) contexts[4] / (double) nrImpressions * 100));
        distr.put("travel", (int) ((double) contexts[5] / (double) nrImpressions * 100));

        return distr;
    }

    public List<PieChart.Data> getGenderPieData(int men, int women){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        out.add(new PieChart.Data("Men", men));
        out.add(new PieChart.Data("Women", women));
        return out;

    }

    public List<PieChart.Data> getAgePieData(int lt25, int btwn2534, int btwn3544,
                                              int btwn4554, int gt55){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        String[] tags = new String[]{"<25","25-34","35-44","45-54",">55"};
        int[] vals = new int[]{lt25, btwn2534,btwn3544,btwn4554,gt55};

        for(int i = 0; i < tags.length; i++){
            out.add(new PieChart.Data(tags[i], vals[i]));
        }

        return out;
    }

    public List<PieChart.Data> getIncomePieData(int high, int medium, int low){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        out.add(new PieChart.Data("High", high));
        out.add(new PieChart.Data("Medium", medium));
        out.add(new PieChart.Data("Low", low));
        return out;

    }

    /**
     * Generate the context pie chart data that will populate the context pie chart
     * update. Make sure you add the arguments in the order they are listed
     * @param blog
     * @param news
     * @param socialmedia
     * @param shopping
     * @param hobbies
     * @param travel
     */
    public List<PieChart.Data> getContextPieData(int blog, int news, int socialmedia, int shopping,
                                                 int hobbies, int travel){

        List<PieChart.Data> out = new ArrayList<PieChart.Data>();
        String[] tags = new String[]{"News","Shopping","Social Media", "Blogs", "Hobbies", "Travel"};
        int[] vals = new int[]{news, shopping, socialmedia, blog, hobbies, travel};

        for(int i = 0; i < tags.length; i++){

            out.add(new PieChart.Data(tags[i],vals[i]));

        }

        return out;

    }


}
