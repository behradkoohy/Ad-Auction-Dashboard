package controllers;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import javafx.fxml.FXML;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;

public class FilterTabController {

    @FXML private JFXDatePicker dFromPicker;
    @FXML private JFXDatePicker dToPicker;
    @FXML private JFXTimePicker timeFromPicker;
    @FXML private JFXTimePicker timeToPicker;

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

    //Date and time range
    public LocalDate dFrom;
    public LocalDate dTo;
    public LocalTime tFrom;
    public LocalTime tTo;

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

    /*
    @FXML
    
     * Called by the reload data button

    public void reloadData(){

        //FILL IN THE RELOAD DATA STUFF HERE

    }


     * Return the values of all the checkboxes in the order
     * they are shown in the UI from left to right, top to bottom
     * @return

    public boolean[] getCheckBoxVals(){

        return new boolean[]{male, female, lt25, btwn2534,
        btwn3544, btwn4554, gt55, lowIncome, medIncome, highIncome};

    }


     * Returns an array containing the before date time,
     * followed by the after date time
     * @return

    public LocalDateTime[] getDateTime(){

        LocalDateTime[] arr = new LocalDateTime[2];

        arr[0] = LocalDateTime.of(dFrom, tFrom);
        arr[1] = LocalDateTime.of(dTo, tTo);

        return arr;

    }*/

}
