package daos;

import entities.ServerEntry;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


public class ServerEntryDao {

    private HashMap<String, List<ServerEntry>> campaignCache = new HashMap<>();
    private HashMap<String, List<ServerEntry>> campaignDateCache = new HashMap<>();

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

    public void save(List<ServerEntry> serverEntries) {
        Transaction transaction = null;
        try (StatelessSession session = SessionHandler.getSessionFactory().openStatelessSession()) {
            transaction = session.beginTransaction();
            for(int i = 0; i < serverEntries.size(); i++) {
                try {
                    session.insert(serverEntries.get(i));
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

    public List<ServerEntry> getAll() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from ServerEntry", ServerEntry.class).list();
        }
    }

    public List<ServerEntry> getFromCampaign(String campaign) {
        if(campaignCache.containsKey(campaign)) {
            System.out.println("ServerEntryDao - Woo hit normal cache");
            return campaignCache.get(campaign);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<ServerEntry> serverEntries = session.createQuery("from ServerEntry where campaign=:campaign"
                        , ServerEntry.class).setParameter("campaign", campaign).list();
                campaignCache.put(campaign, serverEntries);
                return serverEntries;
            }
        }
    }

    public List<ServerEntry> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate) {
        String key = campaign + startDate.toString() + endDate.toString();
        if(campaignDateCache.containsKey(key)) {
            System.out.println("ServerEntryDao - hit date cache");
            return campaignDateCache.get(key);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<ServerEntry> serverEntries = session.createQuery("from ServerEntry where campaign=:campaign and entryDate between :startDate and :endDate"
                        , ServerEntry.class)
                        .setParameter("campaign", campaign)
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .list();
                campaignDateCache.put(key, serverEntries);
                return serverEntries;
            }
        }
    }

    public int getMaxIdentifier() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            List max = session.createQuery("select MAX(identifier) from ServerEntry").list();
            if(max.get(0) == null) {
                return 0;
            } else {
                return (Integer) max.get(0);
            }
        }
    }

}
