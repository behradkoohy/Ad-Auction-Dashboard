package daos;

import entities.Click;
import entities.Impression;
import entities.ServerEntry;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.cfg.Environment;
import org.hibernate.service.ServiceRegistry;

import java.util.Properties;
import java.util.logging.Level;

//Uses singleton pattern to create single instance of session factory
public class SessionHandler {
    private static SessionFactory sessionFactory;

    public static SessionFactory getSessionFactory() {
        if (sessionFactory == null) {
            try {
                java.util.logging.Logger.getLogger("org.hibernate").setLevel(Level.SEVERE);
                Configuration configuration = new Configuration();

                //Should create hibernate.cfg.xml but am lazy
                Properties settings = new Properties();
                settings.put(Environment.DRIVER, "com.mysql.cj.jdbc.Driver");
                settings.put(Environment.URL, "jdbc:mysql://localhost:3309/sys?rewriteBatchedStatements=true&serverTimezone=GMT");
                settings.put(Environment.USER, "application");
                settings.put(Environment.PASS, "app_password1");
                settings.put(Environment.DIALECT, "org.hibernate.dialect.MySQL5Dialect");
                settings.put(Environment.SHOW_SQL, "true");
                settings.put(Environment.CURRENT_SESSION_CONTEXT_CLASS, "thread");
                settings.put(Environment.HBM2DDL_AUTO, "update");
                settings.put("hibernate.jdbc.batch_size", "50");
                settings.put("hibernate.show_sql", "false");
                settings.put("hibernate.order_inserts", "true");

                configuration.setProperties(settings);

                configuration.addAnnotatedClass(Click.class);
                configuration.addAnnotatedClass(Impression.class);
                configuration.addAnnotatedClass(ServerEntry.class);

                ServiceRegistry serviceRegistry = new StandardServiceRegistryBuilder()
                        .applySettings(configuration.getProperties()).build();

                sessionFactory = configuration.buildSessionFactory(serviceRegistry);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return sessionFactory;
    }
}