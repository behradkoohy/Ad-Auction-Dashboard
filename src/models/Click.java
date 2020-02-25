package models;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "click")
public class Click implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column
    private int identifier;

    @Column
    private long id;

    @Column
    private LocalDateTime date;

    @Column
    private double clickCost;

    public int getIdentifier() {
        return identifier;
    }

    public long getId() {
        return id;
    }

    public LocalDateTime getDate() {
        return date;
    }

    public double getClickCost() {
        return clickCost;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setDate(LocalDateTime date) {
        this.date = date;
    }

    public void setClickCost(double clickCost) {
        this.clickCost = clickCost;
    }


    public void printClick() {
        System.out.println(identifier);
        System.out.println(id);
        System.out.println(date);
        System.out.println(clickCost);
    }
}
