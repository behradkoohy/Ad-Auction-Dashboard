package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class FilterTabController {
    @FXML private JFXDatePicker dFromPicker;
    @FXML private JFXDatePicker dToPicker;
    @FXML private JFXTimePicker timeFromPicker;
    @FXML private JFXTimePicker timeToPicker;
    @FXML private Spinner granSpinner;
    @FXML private ComboBox granCombo;

    private Controller controller;

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
    public LocalDate dFrom;
    public LocalDate dTo;
    public LocalTime tFrom;
    public LocalTime tTo;

    //Time granularity
    public int granDigit;
    public ChronoUnit granTimeUnit;


    public void init(Controller controller){

        this.controller = controller;
        //Initially the date spinners will be from week ago until now
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minus(1, ChronoUnit.WEEKS);
        dToPicker.setValue(now.toLocalDate());
        updateDTo();
        timeToPicker.setValue(now.toLocalTime());
        updateTTo();
        dFromPicker.setValue(weekAgo.toLocalDate());
        updateDFrom();
        timeFromPicker.setValue(weekAgo.toLocalTime());
        updateTFrom();
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

        granDigit = 1;
        granTimeUnit = ChronoUnit.DAYS;

        ObservableList<String> timeUnits = FXCollections.observableArrayList("Hours", "Days", "Weeks");
        granCombo.setItems(timeUnits);

        granSpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100) {
        });

    }

    //These toggle methods will be called whenever the checkboxes are ticked/unticked
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
     * Called every time the value of the from date picker changes
     */
    public void updateDFrom(){

        dFrom = dFromPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the to date picker changes
     */
    public void updateDTo(){

        dTo = dToPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the from time picker changes
     */
    public void updateTFrom(){

        tFrom = timeFromPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the to time picker changes
     */
    public void updateTTo(){

        tTo = timeToPicker.getValue();

    }

    @FXML
    /**
     * called when spinner changes
     */
    public void updateGranSpin(){
        granDigit = (int) granSpinner.getValue();
    }

    @FXML
    /**
     * called when combobox changes
     */
    public void updateGranCombo(){
        System.out.println("COMBO");
        switch (String.valueOf(granCombo.getValue())) {
            case "Hours":
                granTimeUnit = ChronoUnit.HOURS;
                break;

            case "Days":
                granTimeUnit = ChronoUnit.DAYS;
                break;

            case "Weeks":
                granTimeUnit = ChronoUnit.WEEKS;
                break;

            default:
                granTimeUnit = ChronoUnit.DAYS;

        }
    }

    public int getGranDigit() {
        return granDigit;
    }

    public ChronoUnit getGranTimeUnit() {
        return granTimeUnit;
    }
}
