package entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Impression")
public class Impression implements Serializable {

    public Impression() {

    }

    public Impression(int identifier, String campaign, LocalDateTime date, long id, User.Gender gender, User.Age age,
                      User.Income income, User.Context context, double impressionCost) {
        this.identifier = identifier;
        this.campaign = campaign;
        this.date = date;
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.income = income;
        this.context = context;
        this.impressionCost = impressionCost;
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
    private User.Gender gender;

    @Column
    private User.Age age;

    @Column
    private User.Income income;

    @Column
    private User.Context context;

    @Column
    private double impressionCost;

    public int getIdentifier() {
        return identifier;
    }

    public String getCampaign() { return  campaign; }

    public LocalDateTime getDate() {
        return date;
    }

    public long getId() {
        return id;
    }

    public User.Gender getGender() { return gender; }

    public User.Age getAge() { return age; }

    public User.Income getIncome() { return income; }

    public User.Context getContext() { return  context; }

    public double getImpressionCost() { return impressionCost; }

    //For testing
    public void print() {
        System.out.println("Identifier: " + identifier);
        System.out.println("Campaign: " + campaign );
        System.out.println("ID: " + id);
        System.out.println("Date: " + date);
        System.out.println("Gender: " + gender);
        System.out.println("Age: " + age);
        System.out.println("Income: " + income);
        System.out.println("Context: " + context);
        System.out.println("Impression cost: " + impressionCost);
    }

}
