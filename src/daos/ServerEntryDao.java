package daos;

import entities.Click;
import entities.ServerEntry;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.List;


public class ServerEntryDao {

    public void save(ServerEntry serverEntry) {
        Transaction transaction = null;
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            transaction = session.beginTransaction();
            session.save(serverEntry);
            transaction.commit();
        } catch (Exception e) {
            if (transaction != null) {
                transaction.rollback();
            }
            e.printStackTrace();
        }
    }

    public List<ServerEntry> getAll() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from ServerEntry", ServerEntry.class).list();
        }
    }

    public List<ServerEntry> getFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Click where campaign=:campaign", ServerEntry.class).setParameter("campaign", campaign).list();
        }
    }

    public List<ServerEntry> getByDateAndCampaign(String campaign, LocalDateTime current, LocalDateTime nextTime) {
        return null;
    }
}
