package daos;

import entities.Impression;
import entities.User;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class ImpressionDao {

    private HashMap<String, List<Impression>> campaignCache = new HashMap<>();
    private HashMap<String, List<Impression>> campaignDateCache = new HashMap<>();
    private HashMap<String, List<User>> campaignUserCache = new HashMap<>();

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

    public List<Impression> getFromCampaign(String campaign) {
        if (campaignCache.containsKey(campaign)) {
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
        List<Impression> impressions = this.getFromCampaign(campaign);

        List<Impression> impressionsByDate = impressions
                .stream()
                .filter(c -> c.getDate().isBefore(endDate) && c.getDate().isAfter(startDate.minusSeconds(1)))
                .collect(Collectors.toList());

        return impressionsByDate;
    }

    public List<User> getUsersFromCampaign(String campaign) {
        if(campaignUserCache.containsKey(campaign)) {
            System.out.println("Hit user cache");
            return campaignUserCache.get(campaign);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<Object[]> res = session.createQuery("select distinct id, gender, age, income, context from Impression where campaign=:campaign"
                        , Object[].class).setParameter("campaign", campaign).list();

                List<User> users = new ArrayList<>();
                for (Object[] row : res) {
                    users.add(new User((long) row[0], (User.Gender) row[1], (User.Age) row[2], (User.Income) row[3], (User.Context) row[4]));
                }

                campaignUserCache.put(campaign, users);
                return users;
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

    @Transactional
    public void deleteCampaign(String campaignName) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from Impression where campaign=:campaign")
                    .setParameter("campaign", campaignName)
                    .executeUpdate();
            transaction.commit();
        }
    }


    //DEPRECATED - use stream filter now
    /*
    public List<Impression> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate) {
        String key = campaign + startDate.toString() + endDate.toString();
        if(campaignDateCache.containsKey(key)) {
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
     */
}
