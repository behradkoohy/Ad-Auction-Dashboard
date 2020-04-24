package daos;

import entities.Click;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;


public class ClickDao {
    private HashMap<String, List<Click>> campaignCache = new HashMap<>();
    private HashMap<String, List<Click>> campaignDateCache = new HashMap<>();

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
        //TODO currently hacky cache when new load campaign - can't technically be sure all entities are from same campaign
        //Fine for now as we only save a list of entities from a single campaign
        //Get campaign name from first entity then cache
        campaignCache.put(clicks.get(0).getCampaign(), clicks);
        Transaction transaction = null;
        try (StatelessSession session = SessionHandler.getSessionFactory().openStatelessSession()) {
            transaction = session.beginTransaction();
            for(int i = 0; i < clicks.size(); i++) {
                try {
                    session.insert(clicks.get(i));
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

    public List<Click> getAll() {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            return session.createQuery("from Click", Click.class).list();
        }
    }

    public List<Click> getFromCampaign(String campaign) {
        if(campaignCache.containsKey(campaign)) {
            System.out.println("ClickDao - hit normal cache");
            return campaignCache.get(campaign);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<Click> clicks = session.createQuery("from Click where campaign=:campaign"
                        , Click.class).setParameter("campaign", campaign).list();
                campaignCache.put(campaign, clicks);
                return clicks;
            }
        }
    }

    public List<Click> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate){
        String key = campaign + startDate.toString() + endDate.toString();
        if(campaignDateCache.containsKey(key)) {
            System.out.println("ClickDao - hit date cache");
            return campaignDateCache.get(key);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<Click> clicks = session.createQuery("from Click where campaign=:campaign and date between :startDate and :endDate"
                        , Click.class)
                        .setParameter("campaign", campaign)
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .list();
                campaignDateCache.put(key, clicks);
                return clicks;
            }
        }
    }

    public LocalDateTime getMaxDateFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            LocalDateTime maxDate = session.createQuery("select max(c.date) from Click c where campaign=:campaign"
                    , LocalDateTime.class).setParameter("campaign", campaign).uniqueResult();
            return maxDate;
        }
    }

    public LocalDateTime getMinDateFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            LocalDateTime maxDate = session.createQuery("select min(c.date) from Click c where campaign=:campaign"
                    , LocalDateTime.class).setParameter("campaign", campaign).uniqueResult();
            return maxDate;
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
