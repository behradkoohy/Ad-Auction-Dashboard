package daos;

import entities.Impression;
import entities.Impression.Age;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


public class ImpressionDao {

    public void save(Impression impression) {
        Transaction transaction = null;
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(impression);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                //transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<Impression> getAll() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Impression", Impression.class).list();
        }
    }

    public List<Impression> getFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Impression where campaign=:campaign", Impression.class).setParameter("campaign", campaign).list();
        }
    }

    public List<Impression> getByAge(Age age) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Impression where age=:age", Impression.class).setParameter("age", age).list();
        }
    }


}
