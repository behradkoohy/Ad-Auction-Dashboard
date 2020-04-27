package tests;

import controllers.StatisticsTabController;
import models.Metrics;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.List;

import static org.junit.Assert.*;

public class StatisticsTabControllerTest {

    private static StatisticsTabController c;

    @BeforeClass
    public static void init(){

        c = new StatisticsTabController();

    }

    @Test
    public void to2DPTest(){

        double expected = 2.51;
        double result = c.to2DP(2.513);
        assertTrue(expected == result);

        expected = -2.51;
        result = c.to2DP(-2.513);
        assertTrue(expected == result);

        expected = 0;
        result = c.to2DP(0);

        assertTrue(expected == result);

    }

    @Test
    public void testInitialize(){

        c.initialize(new Metrics());
        assertNotNull(c.getMetrics());

    }

    @Test
    public void testLoadData(){

        //Change the name of the campaign to be one you already have loaded in your database
        c.loadData("test");

        //Check all labels are not the empty string
        for(String s: c.getLabelVals()){

            assertFalse(s.equals(""));

        }

        //Check the pie chart data is not empty
        assertFalse(c.getPieChartModelData().isEmpty());

        //Check the data the pie charts are displaying is not empty
        for(List l: c.getPieChartData()){

            assertFalse(l.isEmpty());

        }

    }

}