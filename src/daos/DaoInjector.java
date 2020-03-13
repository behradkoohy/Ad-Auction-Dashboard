package daos;

public class DaoInjector {
    private static ClickDao clickDao = new ClickDao();
    private static ImpressionDao impressionDao = new ImpressionDao();
    private static ServerEntryDao serverEntryDao = new ServerEntryDao();

    public static ClickDao newClickDao() {
        return clickDao;
    }

    public static ImpressionDao newImpressionDao() {
        return impressionDao;
    }

    public static ServerEntryDao newServerEntryDao() {
        return serverEntryDao;
    }
}
