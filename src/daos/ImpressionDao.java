package daos;

import entities.Impression;
import entities.Impression.Age;
import entities.Impression.Income;
import entities.Impression.Gender;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
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

    public void save(List<Impression> impressions) {
        Transaction transaction = null;
        try (StatelessSession session = SessionHandler.getSessionFactory().openStatelessSession()) {
            transaction = session.beginTransaction();
            for(int i = 0; i < impressions.size(); i++) {
                try {
                    session.insert(impressions.get(i));
                } catch(Exception e) {
                    e.printStackTrace();
                }
            }
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
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

    public List<Impression> getByAge(String campaign, Age age) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Impression where campaign=:campaign and age=:age", Impression.class)
                    .setParameter("campaign", campaign)
                    .setParameter("age", age)
                    .list();
        }
    }

    public List<Impression> getByIncome(String campaign, Income income) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Impression where campaign=:campaign and income=:income", Impression.class)
                    .setParameter("campaign", campaign)
                    .setParameter("income", income)
                    .list();
        }
    }

    public List<Impression> getByGender(String campaign, Gender gender) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Impression where campaign=:campaign and gender=:gender", Impression.class)
                    .setParameter("campaign", campaign)
                    .setParameter("gender", gender)
                    .list();
        }
    }

    public List<Impression> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Impression where campaign=:campaign and date between(startDate, endDate)", Impression.class)
                    .setParameter("campaign", campaign)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .list();
        }

    }

    public int getMaxIdentifier() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            List max = session.createQuery("select MAX(identifier) from Impression ").list();
            if(max.get(0) == null) {
                return 0;
            } else {
                return (Integer) max.get(0);
            }
        }
    }
}
