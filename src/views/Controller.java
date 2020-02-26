package views;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTextField;
import com.jfoenix.controls.JFXTimePicker;
import models.Data;
import javafx.fxml.FXML;
import javafx.scene.chart.AreaChart;
import javafx.scene.control.Label;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.Random;

public class Controller {

    //Current data values, changed each time UI manipulates them:

    //Gender
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

    //Date range
    private LocalDate dFrom;
    private LocalDate dTo;
    private LocalTime tFrom;
    private LocalTime tTo;

    //Accessibility settings
    private boolean highContrastMode;
    private boolean largeFontMode;

    /*
    Corresponding UI components that can be found in scene builder with these identifiers,
    this is what the "@FXML" annotation means
     */

    @FXML
    private JFXDatePicker dFromPicker;

    @FXML
    private JFXDatePicker dToPicker;

    @FXML
    private JFXTimePicker timeFromPicker;

    @FXML
    private JFXTimePicker timeToPicker;

    @FXML
    private Label genderTitle;

    @FXML
    private Label ageTitle;

    @FXML
    private Label incomeTitle;

    @FXML
    private Label numImpressions;

    @FXML
    private Label numClicks;

    @FXML
    private Label numUnique;

    @FXML
    private Label numBounces;

    @FXML
    private Label numConversions;

    @FXML
    private Label totalCost;

    @FXML
    private Label CTR;

    @FXML
    private Label CPA;

    @FXML
    private Label CPC;

    @FXML
    private Label CPM;

    @FXML
    private Label bounceRate;

    @FXML
    private AreaChart chart;

    public Controller(){

        male = true;
        female = true;
        lt25 = true;
        btwn2534 = true;
        btwn3544 = true;
        btwn4554 = true;
        gt55 = true;
        lowIncome = true;
        medIncome = true;
        highIncome = true;

        highContrastMode = false;
        largeFontMode = false;

        dFrom = LocalDate.now();
        tFrom = LocalTime.now();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dTo = LocalDate.now();
        tTo = LocalTime.now();

        this.init();

    }

    private void init(){


    }

    /**
     * Forwards a request to the controller which in turn
     * forwards a request to the database to get a data object with
     * all the specified attributes
     *
     * @param gender String represenation of gender groups
     * @param age String representation of age groups
     * @param income String representation of income groups
     * @param dateFrom String representation of the start filter date
     * @param dateTo String representation of the end filter date
     * @param timeFrom String representation of the start filter time
     * @param timeTo String representation of the end filter time
     * @return Data object with the specified attributes
     */
    public Data getRequest(String gender, String age, String income,
                           String dateFrom, String dateTo, String timeFrom,
                           String timeTo){

        /*
        Will work something like this:

        gender could be "MF" or "M" or "F"

        age could be "1234" where 1 to 4 are the available age groups
        e.g. if you only wanted oldest and youngest just do "14"

        income could be "123" if you wanted low, medium and high, "1" if you just want low etc

        //controller will then forward this request to database then when it receives will return the data object
        Data d = controller.get(gender, age ... timeFrom, timeTo);
        return d
        */

        return null;

    }

    /**
     * Refreshes the UI according to the data in object d
     * @param d
     */
    public void refreshUI(Data d){}

    @FXML
    public void toggleMale(){

        male = !male;

    }

    @FXML
    public void toggleFemale(){

        female = !female;

    }

    @FXML
    public void toggleLt25(){

        lt25 = !lt25;

    }

    @FXML
    public void toggleBtwn2534(){

        btwn2534 = !btwn2534;

    }

    @FXML
    public void toggleBtwn3544(){

        btwn3544 = !btwn3544;

    }

    @FXML
    public void toggleBtwn4554(){

        btwn4554 = !btwn4554;

    }

    @FXML
    public void toggleGt55(){

        gt55 = !gt55;

    }

    @FXML
    public void toggleLow(){

        lowIncome = !lowIncome;

    }

    @FXML
    public void toggleMed(){

        medIncome = !medIncome;

    }

    @FXML
    public void toggleHigh(){

        highIncome = !highIncome;

    }

    @FXML
    /**
     * Called every time the value of the date from picker changes
     */
    public void updateDFrom(){

        dFrom = dFromPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the date to picker changes
     */
    public void updateDTo(){

        dTo = dToPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the time from picker changes
     */
    public void updateTFrom(){

        tFrom = timeFromPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the time to picker changes
     */
    public void updateTTo(){

        tTo = timeToPicker.getValue();

    }

    @FXML
    private void toggleHighContrast(){

        highContrastMode = !highContrastMode;

    }

    @FXML
    private void toggleLargeFont(){

        largeFontMode = !largeFontMode;

    }

    /**
     * Calculates the time and date difference specified by the user,
     * and returns a value from 1 to 4 corresponding to which metric should be
     * used
     *
     * If they have chosen a time period less than one hour then the
     * metric should be minutes
     *
     * If they have chosen a time period between one hour and 24 hours
     * then the metric should be hours
     *
     * If they have chosen a time period between 24 hours and one month then
     * the metric should be days
     *
     * If they have chosen a time period greater than a month then the metric
     * should be weeks
     *
     * @return code 1,2, 3 or 4 denotes whether the metric should be mins, hours, days or weeks
     */
    public int calcMetric(){

        LocalDateTime before = LocalDateTime.of(dFrom, tFrom);
        LocalDateTime after = LocalDateTime.of(dTo, tTo);

        long days = before.until(after, ChronoUnit.DAYS);
        long hours = before.until(after, ChronoUnit.HOURS);

        if(days >= 30){

            return 4;

        } else if(days >= 1){

            return 3;

        } else if(hours >= 1){

            return 2;

        } else {

            return 1;

        }

    }

    //Generates a random string to populate UI for testing
    private String random(){

        Random r = new Random();
        return String.valueOf(r.nextInt(100));

    }

    //Just for testing
    @FXML
    private void test(){

        numImpressions.setText(random());
        numClicks.setText(random());
        numUnique.setText(random());
        numBounces.setText(random());
        numConversions.setText(random());
        totalCost.setText(random());
        CTR.setText(random());
        CPA.setText(random());
        CPC.setText(random());
        CPM.setText(random());
        bounceRate.setText(random());

    }

}