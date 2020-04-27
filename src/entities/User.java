package entities;

public class User {

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
        SHOPPING, //3
        HOBBIES, //4
        TRAVEL //5
    }

    public enum Gender {
        MALE, //0
        FEMALE //1
    }

    private long id;

    private Gender gender;

    private Age age;

    private Income income;

    private Context context;

    public User(long id, Gender gender, Age age, Income income, Context context) {
        this.id = id;
        this.gender = gender;
        this.age = age;
        this.income = income;
        this.context = context;
    }

    public long getId() { return id; }

    public Gender getGender() { return gender; }

    public Age getAge() { return age; }

    public Income getIncome() { return income; }

    public Context getContext() { return  context; }

    public void print() {
        System.out.println("User ID: " + id);
        System.out.println("Gender: " + gender);
        System.out.println("Age: " + age);
        System.out.println("Income: " + income);
        System.out.println("Context: " + context);
        System.out.println();
    }

}
