package daos;

import entities.Click;
import entities.User;
import org.hibernate.Session;
import org.hibernate.StatelessSession;
import org.hibernate.Transaction;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class ClickDao {
    private HashMap<String, List<Click>> campaignCache = new HashMap<>();
    private HashMap<String, List<Click>> campaignDateCache = new HashMap<>();

    public void save(List<Click> clicks) {
        //TODO currently hacky cache when new load campaign - can't technically be sure all entities are from same campaign
        //Fine for now as we only save a list of entities from a single campaign
        //Get campaign name from first entity then cache
        //campaignCache.put(clicks.get(0).getCampaign(), clicks);
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

    public List<Click> getFromCampaign(String campaign) {
        if(campaignCache.containsKey(campaign)) {
            return campaignCache.get(campaign);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<Click> clicks = session.createQuery("from Click where campaign=:campaign"
                        , Click.class).setParameter("campaign", campaign).list();

                List<User> users = DaoInjector.newImpressionDao().getUsersFromCampaign(campaign);
                Map<Long, User> usersById = users.stream().collect(Collectors.toMap(User::getId, k -> k));
                List<Click> results = clicks.stream().map(click -> new Click(click, usersById.get(click.getId()))).collect(Collectors.toList());

                campaignCache.put(campaign, results);
                return results;
            }
        }
    }

    public List<Click> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate) {
        List<Click> clicks = this.getFromCampaign(campaign);

        List<Click> clicksByDate = clicks
                .stream()
                .filter(c -> c.getDate().isBefore(endDate) && c.getDate().isAfter(startDate.minusSeconds(1)))
                .collect(Collectors.toList());

        return clicksByDate;
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

    @Transactional
    public void deleteCampaign(String campaignName) {
        System.out.println("Test");
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from Click where campaign=:campaign")
                    .setParameter("campaign", campaignName)
                    .executeUpdate();
            transaction.commit();
        }
    }

    //DEPRECATED - use local stream filter now
    /*
    public List<Click> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate) {
        String key = campaign + startDate.toString() + endDate.toString();
        if(campaignDateCache.containsKey(key)) {
            System.out.println("hit cache");
            return campaignDateCache.get(key);
        } else {
            System.out.println("no hit cache");
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<Click> clicks = session.createQuery("from Click where campaign=:campaign and date between :startDate and :endDate"
                        , Click.class)
                        .setParameter("campaign", campaign)
                        .setParameter("startDate", startDate)
                        .setParameter("endDate", endDate)
                        .list();

                List<User> users = DaoInjector.newImpressionDao().getUsersFromCampaign(campaign);

                Map<Long, User> usersById = users.stream().collect(Collectors.toMap(User::getId, k -> k));
                List<Click> results = clicks.stream().map(click -> new Click(click, usersById.get(click.getId()))).collect(Collectors.toList());

                campaignDateCache.put(key, results);
                return clicks;
            }
        }
    }
     */

}
