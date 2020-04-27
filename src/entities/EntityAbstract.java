package entities;

import java.time.LocalDateTime;

public abstract class EntityAbstract {

    public abstract User.Gender getGender();

    public abstract User.Age getAge();

    public abstract User.Income getIncome();

    public abstract User.Context getContext();

    public abstract LocalDateTime getDate();

    public abstract long getId();
}
