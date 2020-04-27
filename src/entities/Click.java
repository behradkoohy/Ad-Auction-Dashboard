package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Click")
public class Click extends EntityAbstract implements Serializable {

    public Click() {

    }

    public Click(int identifier, String campaign, long id, LocalDateTime date, double clickCost) {
        this.identifier = identifier;
        this.campaign = campaign;
        this.id = id;
        this.date = date;
        this.clickCost = clickCost;
    }

    public Click(Click click, User user) {
        this.identifier = click.getIdentifier();
        this.campaign = click.getCampaign();
        this.id = click.getId();
        this.date = click.getDate();
        this.clickCost = click.getClickCost();
        this.user = user;
    }

    @Id
    @Column
    private int identifier;

    @Column
    private String campaign;

    @Column
    private LocalDateTime date;

    @Column
    private long id;

    @Column
    private double clickCost;

    @Transient
    private User user;

    public int getIdentifier() {
        return identifier;
    }

    public String getCampaign() { return campaign; }

    public LocalDateTime getDate() { return date; }

    public long getId() {
        return id;
    }

    public double getClickCost() {
        return clickCost;
    }

    public User getUser() { return user; }


    //For testing
    public void print() {
        System.out.println("Identifier: " + identifier);
        System.out.println("Campaign: " + campaign );
        System.out.println("ID: " + id);
        System.out.println("Date: " + date);
        System.out.println("Click cost: " + clickCost);
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
}
