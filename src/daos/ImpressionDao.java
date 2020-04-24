package daos;

import entities.Impression;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


public class ImpressionDao {

    private HashMap<String, List<Impression>> campaignCache = new HashMap<>();
    private HashMap<String, List<Impression>> campaignDateCache = new HashMap<>();

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
        //TODO currently hacky cache when new load campaign - can't technically be sure all entities are from same campaign
        //Fine for now as we only save a list of entities from a single campaign
        //Get campaign name from first entity then cache
        campaignCache.put(impressions.get(0).getCampaign(), impressions);
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
        if (campaignCache.containsKey(campaign)) {
            System.out.println("ImpressionDao - hit normal cache");
            return campaignCache.get(campaign);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<Impression> impressions = session.createQuery("from Impression where campaign=:campaign"
                        , Impression.class).setParameter("campaign", campaign).list();
                campaignCache.put(campaign, impressions);
                return impressions;
            }
        }
    }

    public List<Impression> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate) {
        String key = campaign + startDate.toString() + endDate.toString();
        if(campaignDateCache.containsKey(key)) {
            System.out.println("ImpressionDao - hit date cache");
            return campaignDateCache.get(key);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<Impression> impressions = session.createQuery("from Impression where campaign=:campaign and date between :startDate and :endDate"
                        , Impression.class)
                        .setParameter("campaign", campaign)
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .list();
                campaignDateCache.put(key, impressions);
                return impressions;
            }
        }
    }

    public LocalDateTime getMaxDateFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            LocalDateTime maxDate = session.createQuery("select max(i.date) from Impression i where campaign=:campaign"
                    , LocalDateTime.class).setParameter("campaign", campaign).uniqueResult();
            return maxDate;
        }
    }

    public LocalDateTime getMinDateFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            LocalDateTime maxDate = session.createQuery("select min(i.date) from Impression i where campaign=:campaign"
                    , LocalDateTime.class).setParameter("campaign", campaign).uniqueResult();
            return maxDate;
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
