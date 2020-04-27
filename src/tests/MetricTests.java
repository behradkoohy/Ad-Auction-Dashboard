package tests;


import models.Metrics;
import models.ReaderCSV;
import org.junit.BeforeClass;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

public class MetricTests {

    private static Metrics metrics;

    @BeforeClass
    public static void setupThis(){

        ReaderCSV.readCSV("src/tests/testFiles/testClick.csv", "test");
        ReaderCSV.readCSV("src/tests/testFiles/testImpression.csv", "test");
        ReaderCSV.readCSV("src/tests/testFiles/testServer.csv", "test");

        System.out.println("Setup");

        metrics = new Metrics();
        metrics.setCampaign("test");

    }

    @Test
    public void totalImpressionsTest() {
        System.out.println(metrics.getNumImpressions());
        assertTrue(metrics.getNumImpressions() == 99);
    }

    @Test
    public void totalClicksTest() {
        assertTrue(metrics.getNumClicks() == 99);
    }

    @Test
    public void uniquesTest() {
        assertTrue(metrics.getNumUniqs() == 98);
    }

    @Test
    public void conversionsTest() {
        assertTrue(metrics.getConversions() == 6);
    }

    @Test
    public void cTRTest() {
        assertTrue(metrics.getCTR() == 1);
    }


    //total test click cost = 464.83541
    //total test impression cost = 0.105674
    //total total cost = 464.941084
    @Test
    public void cPATest() {
        assertTrue(metrics.getCPA() == (464.9410839999999/6));
    }

    @Test
    public void cPCTest() {
        assertTrue(metrics.getCPC() == (464.83540999999985/99));
    }

    @Test
    public void cPMTest() {
        assertTrue(metrics.getCPM() == (4696.374585858584));
    }

}