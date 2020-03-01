package tests;

import daos.ClickDao;
import daos.ImpressionDao;
import entities.Impression;

import java.time.LocalDateTime;


public class Main {
    public static void main(String[] args) {
        ClickDao clickDao = new ClickDao();
        ImpressionDao impressionDao = new ImpressionDao();
        //Testing
        /*
        Click newClick = new Click("Second Campaign", 1234562922096180000L, LocalDateTime.now(), 1.245378 );
        clickDao.save(newClick);
        clickDao.getFromCampaign("First Campaign").forEach(Click::print);
        */


        Impression newImpression = new Impression("Another Campaign", LocalDateTime.now(), 1234562922096180000L,
                Impression.Gender.MALE, Impression.Age.FROM35TO44, Impression.Income.MEDIUM, Impression.Context.NEWS,
                1.245378);
        Impression anotherImpression = new Impression("Another Campaign", LocalDateTime.now(), 1234564922096183300L,
                Impression.Gender.MALE, Impression.Age.OVER54, Impression.Income.LOW, Impression.Context.NEWS,
                0.448778);

        impressionDao.save(newImpression);
        impressionDao.save(anotherImpression);

        impressionDao.getByAge(Impression.Age.FROM35TO44).forEach(Impression::print);;
    }
}
