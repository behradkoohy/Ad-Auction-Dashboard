package daos;

import entities.ServerEntry;
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


public class ServerEntryDao {

    private HashMap<String, List<ServerEntry>> campaignCache = new HashMap<>();
    //private HashMap<String, List<ServerEntry>> campaignDateCache = new HashMap<>();

    public void save(List<ServerEntry> serverEntries) {
        //TODO currently hacky cache when new load campaign - can't technically be sure all entities are from same campaign
        //Fine for now as we only save a list of entities from a single campaign
        //Get campaign name from first entity then cache
        //campaignCache.put(serverEntries.get(0).getCampaign(), serverEntries);
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


    public List<ServerEntry> getFromCampaign(String campaign) {
        if(campaignCache.containsKey(campaign)) {
            return campaignCache.get(campaign);
        } else {
            try (Session session = SessionHandler.getSessionFactory().openSession()) {
                List<ServerEntry> serverEntries = session.createQuery("from ServerEntry where campaign=:campaign"
                        , ServerEntry.class).setParameter("campaign", campaign).list();

                List<User> users = DaoInjector.newImpressionDao().getUsersFromCampaign(campaign);
                Map<Long, User> usersById = users.stream().collect(Collectors.toMap(User::getId, k -> k));
                List<ServerEntry> results = serverEntries.stream().map(serverEntry -> new ServerEntry(serverEntry, usersById.get(serverEntry.getId()))).collect(Collectors.toList());

                campaignCache.put(campaign, results);
                return results;
            }
        }
    }

    public List<ServerEntry> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate) {
        List<ServerEntry> serverEntries = this.getFromCampaign(campaign);

        List<ServerEntry> serverEntriesByDate = serverEntries
                .stream()
                .filter(c -> c.getEntryDate().isBefore(endDate) && c.getEntryDate().isAfter(startDate.minusSeconds(1)))
                .collect(Collectors.toList());

        return serverEntries;
    }


    public LocalDateTime getMaxDateFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            LocalDateTime maxDate = session.createQuery("select max(s.entryDate) from ServerEntry s where campaign=:campaign"
                    , LocalDateTime.class).setParameter("campaign", campaign).uniqueResult();
            return maxDate;
        }
    }

    public LocalDateTime getMinDateFromCampaign(String campaign) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            LocalDateTime maxDate = session.createQuery("select min(s.entryDate) from ServerEntry s where campaign=:campaign"
                    , LocalDateTime.class).setParameter("campaign", campaign).uniqueResult();
            return maxDate;
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

    @Transactional
    public void deleteCampaign(String campaignName) {
        try (Session session = SessionHandler.getSessionFactory().openSession()) {
            Transaction transaction = session.beginTransaction();
            session.createQuery("delete from ServerEntry where campaign=:campaign")
                    .setParameter("campaign", campaignName)
                    .executeUpdate();
            transaction.commit();
        }
    }

    //DEPRECATED - use stream filter now
    /*
    public List<ServerEntry> getByDateAndCampaign(String campaign, LocalDateTime startDate, LocalDateTime endDate) {
        String key = campaign + startDate.toString() + endDate.toString();
        if(campaignDateCache.containsKey(key)) {
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
     */

}
