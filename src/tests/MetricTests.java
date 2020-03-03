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
        assertTrue(metrics.getNumImpressions("test") == 99);
    }


}