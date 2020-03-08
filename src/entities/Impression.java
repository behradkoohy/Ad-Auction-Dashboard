package entities;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Table(name = "Impression")
public class Impression implements Serializable {

    //DON'T CHANGE ORDER OF ENUMS
    public enum Income {
        LOW, //0
        MEDIUM, //1
        HIGH, //2
    }

    public enum Age  {
        LESS25, //0
        FROM25TO34, //1
        FROM35TO44, //2
        FROM45TO54, //3
        OVER54 //4
    }

    public enum Context {
        BLOG, //0
        NEWS, //1
        SOCIALMEDIA, //2
        SHOPPING //3
    }

    public enum Gender {
        MALE, //0
        FEMALE //1
    }

    public Impression() {

    }

    public Impression(int identifier, String campaign, LocalDateTime date, long id, Gender gender, Age age,
                      Income income, Context context, double impressionCost) {
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
    private Gender gender;

    @Column
    private Age age;

    @Column
    private Income income;

    @Column
    private Context context;

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

    public Gender getGender() { return gender; }

    public Age getAge() { return age; }

    public Income getIncome() { return income; }

    public Context getContext() { return  context; }

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
