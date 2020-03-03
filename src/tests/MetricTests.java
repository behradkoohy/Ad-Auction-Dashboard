package tests;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import models.Metrics;
import models.ReaderCSV;
import org.junit.Before;
import org.junit.Test;

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

        ReaderCSV.readCSV("/testFiles/testClick.csv");
        ReaderCSV.readCSV("/testFiles/testImpression.csv");
        ReaderCSV.readCSV("/testFiles/testServer.csv");


        metrics = new Metrics(clickDao, impressionsDao, serverDao);

    }

    @Test
    public void totalImpressionsTest() {

    }


}