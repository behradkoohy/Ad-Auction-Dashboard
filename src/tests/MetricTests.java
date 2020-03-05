package tests;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import models.Metrics;
import models.ReaderCSV;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MetricTests {

    private static ClickDao clickDao = new ClickDao();
    private static ImpressionDao impressionDao = new ImpressionDao();
    private static ServerEntryDao serverEntryDao = new ServerEntryDao();
    private static Metrics metrics;

    @BeforeClass
    public static void setupThis(){

        ReaderCSV.readCSV("src/tests/testFiles/testClick.csv", "test",
                clickDao, impressionDao, serverEntryDao);
        ReaderCSV.readCSV("src/tests/testFiles/testImpression.csv", "test",
                clickDao, impressionDao, serverEntryDao);
        ReaderCSV.readCSV("src/tests/testFiles/testServer.csv", "test",
                clickDao, impressionDao, serverEntryDao);

        System.out.println("Setup");

        metrics = new Metrics(clickDao, impressionDao, serverEntryDao);

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
        assertTrue(metrics.getCPA("test") == (464.9410839999999/6));
    }

    @Test
    public void cPCTest() {
        assertTrue(metrics.getCPC("test") == (464.83540999999985/99));
    }

    @Test
    public void cPMTest() {
        assertTrue(metrics.getCPM("test") == (4696.374585858584));
    }



}