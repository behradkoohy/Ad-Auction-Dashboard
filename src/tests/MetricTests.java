//package tests;
//
//
//import models.Metrics;
//import models.ReaderCSV;
//import org.junit.BeforeClass;
//import org.junit.Test;
//
//import java.time.LocalDateTime;
//
//import static org.junit.Assert.assertTrue;
//
//public class MetricTests {
//
//    private static Metrics metrics;
//    private static LocalDateTime start = LocalDateTime.of(2015, 1, 1, 12, 0);
//    private static LocalDateTime end = LocalDateTime.of(2015, 1, 2, 12, 0);
//
//    @BeforeClass
//    public static void setupThis(){
//
//        ReaderCSV.readCSV("src/tests/testFiles/testClick.csv", "test");
//        ReaderCSV.readCSV("src/tests/testFiles/testImpression.csv", "test");
//        ReaderCSV.readCSV("src/tests/testFiles/testServer.csv", "test");
//
//        System.out.println("Setup");
//
//        metrics = new Metrics();
//        metrics.setCampaign("test");
//
//    }
//
//    @Test
//    public void totalImpressionsTest() {
//        assertTrue(metrics.getNumImpressions(start, end) == 99);
//    }
//
//    @Test
//    public void totalClicksTest() {
//        assertTrue(metrics.getNumClicks(start, end) == 99);
//    }
//
//    @Test
//    public void uniquesTest() {
//        assertTrue(metrics.getNumUniqs(start, end) == 98);
//    }
//
//    @Test
//    public void conversionsTest() {
//        assertTrue(metrics.getConversions(start, end) == 6);
//    }
//
//    @Test
//    public void cTRTest() {
//        assertTrue(metrics.getCTR(start, end) == 1);
//    }
//
//
//    //total test click cost = 464.83541
//    //total test impression cost = 0.105674
//    //total total cost = 464.941084
//    @Test
//    public void cPATest() {
//        assertTrue(metrics.getCPA(start, end) == (464.9410839999999/6));
//    }
//
//    @Test
//    public void cPCTest() {
//        assertTrue(metrics.getCPC(start, end) == (464.83540999999985/99));
//    }
//
//    @Test
//    public void cPMTest() {
//        assertTrue(metrics.getCPM(start, end) == (4696.374585858584));
//    }
//
//}