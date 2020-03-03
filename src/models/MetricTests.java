package models;


import daos.ClickDao;
import daos.ImpressionDao;
import daos.ServerEntryDao;
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




        metrics = new Metrics(clickDao, impressionsDao, serverDao);

    }

    @Test
    public void totalImpressionsTest() {

    }


}