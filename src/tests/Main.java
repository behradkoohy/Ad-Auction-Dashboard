package tests;

import daos.ClickDao;
import entities.Click;
import models.ReaderCSV;

import java.time.LocalDateTime;

public class Main {
    public static void main(String[] args) {
        //Testing
        ClickDao clickDao = new ClickDao();
        Click newClick = new Click();
        newClick.setId(1234562922096180000L);
        newClick.setDate(LocalDateTime.now());
        newClick.setClickCost(1.245378);
        clickDao.save(newClick);
        clickDao.getClicks().forEach(Click::printClick);
        
        ReaderCSV.readCSV("click_log.csv", "click");
    	
    }
}
