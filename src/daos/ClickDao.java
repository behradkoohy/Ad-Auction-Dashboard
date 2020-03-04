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

    public void save(List<Click> clicks) {
        Transaction transaction = null;
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            for(int i = 0; i < clicks.size(); i++) {
                try {
                    session.persist(clicks.get(i));
                } catch(Exception e) {
                    e.printStackTrace();
                }
                if(i % 100 == 0) {
                    session.flush();
                    session.clear();
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

    public List<Click> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate){
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Click where campaign=:campaign and date between(startDate, endDate)", Click.class)
                    .setParameter("campaign", campaign)
                    .setParameter("startDate", startDate)
                    .setParameter("endDate", endDate)
                    .list();
        }
    }

    public int getMaxIdentifier() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            List max = session.createQuery("select MAX(identifier) from Click ").list();
            if(max.get(0) == null) {
                return 0;
            } else {
                return (Integer) max.get(0);
            }
        }
    }


    public List<String> getCampaigns() throws Exception {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("select distinct campaign from Click ", String.class).list();
        }
    }

}
