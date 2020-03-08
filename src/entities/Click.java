package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Click")
public class Click implements Serializable {

    public Click() {

    }

    public Click(int identifier, String campaign, long id, LocalDateTime date, double clickCost) {
        this.identifier = identifier;
        this.campaign = campaign;
        this.id = id;
        this.date = date;
        this.clickCost = clickCost;
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

    //For testing
    public void print() {
        System.out.println("Identifier: " + identifier);
        System.out.println("Campaign: " + campaign );
        System.out.println("ID: " + id);
        System.out.println("Date: " + date);
        System.out.println("Click cost: " + clickCost);
    }
}
