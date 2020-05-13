package tests;


import models.Filter;
import models.Metrics;
import models.ReaderCSV;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.Assert.assertTrue;

public class MetricTests {

    private static Metrics metrics;
    private static LocalDateTime start = LocalDateTime.of(2015, 1, 1, 12, 0);
    private static LocalDateTime end = LocalDateTime.of(2015, 1, 2, 12, 0);

    private static Filter totalFilter = new Filter(true, true,true,true,true,true,true,true,true,true,true,true,true,true,true,true);


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
        assertTrue(metrics.getNumImpressions(start, end) == 99);
    }

    @Test
    public void totalClicksTest() {
        assertTrue(metrics.getNumClicks(start, end) == 99);
    }

    @Test
    public void uniquesTest() {
        assertTrue(metrics.getNumUniqs(start, end) == 98);
    }

    @Test
    public void conversionsTest() {
        assertTrue(metrics.getConversions(start, end) == 6);
    }

    @Test
    public void cTRTest() {
        assertTrue(metrics.getCTR(start, end) == 1);
    }


    //total test click cost = 464.83541
    //total test impression cost = 0.105674
    //total total cost = 464.941084
    @Test
    public void cPATest() {
        assertTrue(metrics.getCPA(start, end) == (464.9410839999999/6));
    }

    @Test
    public void cPCTest() {
        assertTrue(metrics.getCPC(start, end) == (464.83540999999985/99));
    }

    @Test
    public void cPMTest() {
        assertTrue(metrics.getCPM(start, end) == (4696.374585858584));
    }

    @Test
    public void maxGranularityTest() {
        Duration duration = Duration.ofDays(7);
        assertTrue(metrics.getBouncePerTimeList(start, end, duration).size() == 1);
    }

    @Test
    public void minGranularityTest() {
        Duration duration = Duration.ofHours(1);
        assertTrue(metrics.getBouncePerTimeList(start, end, duration).size() == 2);
    }

    @Test
    public void maxBounceTimeTest() {
        Duration duration = Duration.ofDays(1);
        metrics.setBounceTime(duration);
        assertTrue(metrics.getNumBounces(start, end) == 99);
    }

    @Test
    public void maxBouncePageTest() {
        metrics.setBouncePages(100);
        assertTrue(metrics.getNumBounces(start, end) == 99);
    }

    @Test
    public void minBounceTimeTest() {
        Duration duration = Duration.ofMillis(1);
        metrics.setBounceTime(duration);
        assertTrue(metrics.getNumBounces(start, end) == 0);
    }

    @Test
    public void minBouncePageTest() {
        metrics.setBouncePages(0);
        assertTrue(metrics.getNumBounces(start, end) == 0);
    }

    @Test
    public void minFilterTest() {
        Filter filter = new Filter(false, false,false,false,false,false,false,false,false,false,false,false,false,false,false,false);
        metrics.setFilter(filter);
        assertTrue(metrics.getNumImpressions(start, end) == 0);
    }

    @Test
    public void minFilterTest() {
        Filter filter = new Filter(true, true,true,true,true,true,true,true,true,true,true,true,true,true,true,true);
        metrics.setFilter(filter);
        assertTrue(metrics.getNumImpressions(start, end) == 99);
    }

    @Test
    public void genderFilterTest() {
        Filter filter = new Filter(false, true,true,true,true,true,true,true,true,true,true,true,true,true,true,true);
        metrics.setFilter(filter);
        assertTrue(metrics.getNumImpressions(start, end) == 68);
    }

    @Test
    public void ageFilterTest() {
        Filter filter = new Filter(true, true,false,false,false,false,true,true,true,true,true,true,true,true,true,true);
        metrics.setFilter(filter);
        assertTrue(metrics.getNumImpressions(start, end) == 17);
    }

    @Test
    public void incomeFilterTest() {
        Filter filter = new Filter(true, true,true,true,true,true,true,false,false,true,true,true,true,true,true,true);
        metrics.setFilter(filter);
        assertTrue(metrics.getNumImpressions(start, end) == 16);
    }

    @Test
    public void contextFilterTest() {
        Filter filter = new Filter(true, true,true,true,true,true,true,true,true,true,true,false,false,false,false,false);
        metrics.setFilter(filter);
        assertTrue(metrics.getNumImpressions(start, end) == 24);
    }


}