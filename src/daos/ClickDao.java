package daos;

import entities.Click;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
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

    public List<Click> getAll() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Click", Click.class).list();
        }
    }

    public List<Click> getFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Click where campaign=:campaign", Click.class).setParameter("campaign", campaign).list();
        }
    }

    public List<Click> getByDateAndCampaign(String campaign, LocalDateTime current, LocalDateTime nextTime) {
        return null;
    }
}
