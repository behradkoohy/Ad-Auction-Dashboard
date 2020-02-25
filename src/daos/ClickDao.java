package daos;

import models.Click;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


public class ClickDao {

    public void save(Click click) {
        Transaction transaction = null;
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(click);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Click> getClicks() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Click", Click.class).list();
        }
    }

}
