package tests;

import controllers.HistogramTabController;
import org.junit.BeforeClass;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class HistogramTabControllerTest {

        private static HistogramTabController c;

        // change when running your tests
        private static String campaignName = "test";

        @BeforeClass
        public static void init(){
            c = new HistogramTabController();
        }

        // test that it loads a campaign data
        @Test
        public void testLoadCampaign(){

            c.loadDataNoUI(campaignName);

            assertNotNull(c.getHistogramModel());
            assertNotNull(c.getBarChartData());
        }

        // test values are between 2 valid ranges
        @Test
        public void testCampaignRangesAll(){
            c.loadDataNoUI(campaignName);
            c.getDataNoUI(0, null, 1);

            double expected_first_range = 0.0;
            double expected_last_range = 16.0;
            double expected_band_length = 1.0;

            List<Double> keys = new ArrayList<Double>(c.getBarChartData().keySet());
            Collections.sort(keys);

            double result_first_range = keys.get(0);
            double result_last_range = keys.get(keys.size() - 1);
            double result_band_length = keys.get(1) - keys.get(0);

            assertTrue(expected_first_range == result_first_range);
            assertTrue(expected_last_range == result_last_range);
            assertTrue(expected_band_length == result_band_length);
        }

        @Test
        public void testCampaignRangesCustomRange(){
            c.loadDataNoUI(campaignName);
            c.getDataNoUI(9, (double) 16, 0.3);

            double expected_first_range = 9.0;
            double expected_last_range = 15.9;
            double expected_band_length = 0.3;

            List<Double> keys = new ArrayList<Double>(c.getBarChartData().keySet());
            Collections.sort(keys);

            double result_first_range = keys.get(0);
            double result_last_range = keys.get(keys.size() - 1);
            double result_band_length = Math.round((keys.get(1) - keys.get(0)) * 1000.0)/1000.0;

            assertTrue(expected_first_range == result_first_range);
            assertTrue(expected_last_range == result_last_range);
            assertTrue(expected_band_length == result_band_length);
        }


}
