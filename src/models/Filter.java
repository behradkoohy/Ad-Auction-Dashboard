package models;

import entities.EntityAbstract;
import entities.User;

import java.util.function.Predicate;

public class Filter implements Predicate<EntityAbstract> {

    private boolean male;
    private boolean female;

    //Age range
    private boolean lt25;
    private boolean btwn2534;
    private boolean btwn3544;
    private boolean btwn4554;
    private boolean gt55;

    //Income
    private boolean lowIncome;
    private boolean medIncome;
    private boolean highIncome;

    //Context
    //TODO add contexts

    public Filter(boolean male, boolean female,
                  boolean lt25, boolean btwn2534, boolean btwn3544, boolean btwn4554, boolean gt55,
                  boolean lowIncome, boolean medIncome, boolean highIncome) {
        this.male = male;
        this.female = female;
        this.lt25 = lt25;
        this.btwn2534 = btwn2534;
        this.btwn3544 = btwn3544;
        this.btwn4554 = btwn4554;
        this.gt55 = gt55;
        this.lowIncome = lowIncome;
        this.medIncome = medIncome;
        this.highIncome = highIncome;
    }

    @Override
    public boolean test(EntityAbstract i) {
        return (((i.getGender().equals(User.Gender.MALE) && male) ||
                (i.getGender().equals(User.Gender.FEMALE) && female)) &&

                ((i.getAge().equals(User.Age.LESS25) && lt25) ||
                (i.getAge().equals(User.Age.FROM25TO34) && btwn2534) ||
                (i.getAge().equals(User.Age.FROM35TO44) && btwn3544) ||
                (i.getAge().equals(User.Age.FROM45TO54) && btwn4554) ||
                (i.getAge().equals(User.Age.OVER54) && gt55)) &&

                ((i.getIncome().equals(User.Income.LOW) && lowIncome) ||
                (i.getIncome().equals(User.Income.MEDIUM) && medIncome) ||
                (i.getIncome().equals(User.Income.HIGH) && highIncome))
        );
    }
}
