package tests;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import models.Metrics;
import models.ReaderCSV;
import org.junit.Before;
import org.junit.Test;
import org.junit.Assert.*;

import static org.junit.Assert.assertTrue;

public class MetricTests {

    private ClickDao clickDao;
    private ImpressionDao impressionsDao;
    private ServerEntryDao serverDao;
    private Metrics metrics;

    @Before
    public void setupThis(){
        clickDao = new ClickDao();
        impressionsDao = new ImpressionDao();
        serverDao = new ServerEntryDao();

        ReaderCSV.readCSV("src/tests/testFiles/testClick.csv");
        ReaderCSV.readCSV("src/tests/testFiles/testImpression.csv");
        ReaderCSV.readCSV("src/tests/testFiles/testServer.csv");


        metrics = new Metrics(clickDao, impressionsDao, serverDao);

    }

    @Test
    public void totalImpressionsTest() {
        System.out.println(metrics.getNumImpressions("test"));
        assertTrue(metrics.getNumImpressions("test") == 99);
    }

    @Test
    public void totalClicksTest() {
        assertTrue(metrics.getNumClicks("test") == 99);
    }

    @Test
    public void uniquesTest() {
        assertTrue(metrics.getNumUniqs("test") == 98);
    }

    @Test
    public void conversionsTest() {
        assertTrue(metrics.getConversions("test") == 6);
    }

    @Test
    public void cTRTest() {
        assertTrue(metrics.getCTR("test") == 1);
    }


    //total test click cost = 464.83541
    //total test impression cost = 0.105674
    //total total cost = 464.941084
    @Test
    public void cPATest() {
        assertTrue(metrics.getCPA("test") == (464.941084/6));
    }




}