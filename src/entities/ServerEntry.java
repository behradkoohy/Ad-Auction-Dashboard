package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "ServerEntry")
public class ServerEntry extends EntityAbstract implements Serializable {

    public ServerEntry() {

    }

    public ServerEntry(int identifier, String campaign, LocalDateTime entryDate, long id, LocalDateTime exitDate,
                       int pageViews, boolean conversion) {
        this.identifier = identifier;
        this.campaign = campaign;
        this.entryDate = entryDate;
        this.id = id;
        this.exitDate = exitDate;
        this.pageViews = pageViews;
        this.conversion = conversion;
    }
    public ServerEntry(ServerEntry serverEntry, User user) {
        this.identifier = serverEntry.getIdentifier();
        this.campaign = serverEntry.getCampaign();
        this.entryDate = serverEntry.getEntryDate();
        this.id = serverEntry.getId();
        this.exitDate = serverEntry.getExitDate();
        this.pageViews = serverEntry.getPageViews();
        this.conversion = serverEntry.getConversion();
        this.user = user;
    }


    @Id
    @Column
    private int identifier;

    @Column
    private String campaign;

    @Column
    private LocalDateTime entryDate;

    @Column
    private long id;

    @Column
    private LocalDateTime exitDate;

    @Column
    private int pageViews;

    @Column
    private boolean conversion;

    @Transient
    private User user;

    public int getIdentifier() {
        return identifier;
    }

    public String getCampaign() { return campaign; }

    public LocalDateTime getEntryDate() { return entryDate; }

    public long getId() {
        return id;
    }

    public LocalDateTime getExitDate() { return exitDate; }

    public int getPageViews() { return pageViews; }

    public boolean getConversion() { return conversion; }

    public User getUser() { return user; }

    //For testing
    public void print() {
        System.out.println("Identifier: " + identifier);
        System.out.println("Campaign: " + campaign );
        System.out.println("ID: " + id);
        System.out.println("Entry Date: " + entryDate);
        System.out.println("Exit Date: " + exitDate);
        System.out.println("Page Views: " + pageViews);
        System.out.println("Conversion: " + conversion);
        user.print();
    }

    @Override
    public User.Gender getGender() {
        return getUser().getGender();
    }

    @Override
    public User.Age getAge() {
        return getUser().getAge();
    }

    @Override
    public User.Income getIncome() {
        return getUser().getIncome();
    }

    @Override
    public User.Context getContext() {
        return getUser().getContext();
    }

    @Override
    public LocalDateTime getDate() {
        return getEntryDate();
    }

}
