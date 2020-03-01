package views;

import com.jfoenix.controls.JFXDatePicker;
import com.jfoenix.controls.JFXTimePicker;
import common.Data;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

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

    //Whether or not these metrics should be displayed on the graph
    private boolean impressions;
    private boolean conversions;
    private boolean clicks;
    private boolean uniqueUsers;
    private boolean bounces;

    //Accessibility settings
    private boolean highContrastMode;
    private boolean largeFontMode;

    //The number of "units" that will be displayed along the x axis
    private int unitsDifference;

    /*
    Corresponding UI components that can be found in scene builder with these identifiers,
    this is what the "@FXML" annotation means

    When the application starts the FXML loader will inject the actual
    UI component references in scene builder with these variables
     */

    @FXML
    private BorderPane borderPane;

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
    private PieChart genderPie;

    @FXML
    private PieChart agePie;

    @FXML
    private PieChart incomePie;

    @FXML
    private LineChart<?,?> lineChart;

    @FXML
    private CategoryAxis lineChartXAxis;

    @FXML
    private NumberAxis lineChartYAxis;

    @FXML
    private BarChart barChart;

    @FXML
    private CategoryAxis barChartXAxis;

    @FXML
    private NumberAxis barChartYAxis;

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

        impressions = true;
        conversions = true;
        clicks = true;
        uniqueUsers = true;
        bounces = true;

        highContrastMode = false;
        largeFontMode = false;

        //For testing purposes
        dFrom = LocalDate.now();
        tFrom = LocalTime.now();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        dTo = LocalDate.now();
        tTo = LocalTime.now();

        unitsDifference = 0;

    }

    @FXML
    /**
     * This method is called when the FXML loader has finished injecting
     * references, so whenever you need to call a method on a UI component
     * when the program opens do it from here so you know for
     * certain it won't be null
     */
    public void initialize(){

        lineChart.setAnimated(false);

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

        //TODO remove
        test();

    }

    private void pieChartTest(){

        genderPie.getData().clear();
        agePie.getData().clear();
        incomePie.getData().clear();

        Random r = new Random();

        PieChart.Data gender1 = new PieChart.Data("Men", r.nextInt(50));
        PieChart.Data gender2 = new PieChart.Data("Women", r.nextInt(50));
        genderPie.getData().addAll(gender1, gender2);
        genderPie.setTitle("Gender");
        genderPie.setLegendVisible(false);
        genderPie.setStyle("-fx-font-size: " + 10 + "px;");

        PieChart.Data age1 = new PieChart.Data("<25", r.nextInt(30));
        PieChart.Data age2 = new PieChart.Data("25-34", r.nextInt(30));
        PieChart.Data age3 = new PieChart.Data("35-44", r.nextInt(30));
        PieChart.Data age4 = new PieChart.Data("45-54", r.nextInt(30));
        PieChart.Data age5 = new PieChart.Data(">54", r.nextInt(30));
        agePie.getData().addAll(age1, age2, age3, age4, age5);
        agePie.setTitle("Age");
        agePie.setLegendVisible(false);
        agePie.setStyle("-fx-font-size: " + 10 + "px;");

        PieChart.Data income1 = new PieChart.Data("Low", r.nextInt(30));
        PieChart.Data income2 = new PieChart.Data("Medium", r.nextInt(30));
        PieChart.Data income3 = new PieChart.Data("High", r.nextInt(30));
        incomePie.getData().addAll(income1, income2, income3);
        incomePie.setTitle("Income");
        incomePie.setLegendVisible(false);
        incomePie.setStyle("-fx-font-size: " + 10 + "px;");

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
    Methods corresponding to the checkboxes for the chart
    Each time one checkbox value is changed the chart will
    automatically update
     */
    @FXML
    private void toggleImpressions(){

        impressions = !impressions;
        updateChart();

    }

    @FXML
    private void toggleConversions(){

        conversions = !conversions;
        updateChart();

    }

    @FXML
    private void toggleClicks(){

        clicks = !clicks;
        updateChart();

    }

    @FXML
    private void toggleUnique(){

        uniqueUsers = !uniqueUsers;
        updateChart();

    }

    @FXML
    private void toggleBounces(){

        bounces = !bounces;
        updateChart();

    }

    @FXML
    private void toggleHighContrast(){

        highContrastMode = !highContrastMode;

    }

    @FXML
    private void toggleLargeFont(){

        largeFontMode = !largeFontMode;

    }

    public void updateChart(){

        ChartHandler handler = new ChartHandler(lineChart, lineChartXAxis,
                lineChartYAxis, calcMetric(), unitsDifference, impressions,
                conversions, clicks, uniqueUsers, bounces);

    }

    public void updateHistogram(){

        HistogramHandler handler = new HistogramHandler(barChart, barChartXAxis,
                barChartYAxis, calcMetric());

    }

    /**
     * Calculates the time and date difference specified by the user,
     * and returns the string of the unit that should be used as a metric
     *
     * @return metric whether the metric is minutes, hours, days or weeks
     */
    public String calcMetric(){

        LocalDateTime before = LocalDateTime.of(dFrom, tFrom);
        LocalDateTime after = LocalDateTime.of(dTo, tTo);

        long days = before.until(after, ChronoUnit.DAYS);
        long hours = before.until(after, ChronoUnit.HOURS);

        if(days >= 30){

            unitsDifference = Math.round(before.until(after, ChronoUnit.WEEKS));
            return "Weeks";

        } else if(days >= 1){

            unitsDifference = Math.round(days);
            return "Days";

        } else if(hours >= 1){

            unitsDifference = Math.round(hours);
            return "Hours";

        } else {

            unitsDifference = Math.round(before.until(after, ChronoUnit.MINUTES));
            return "Minutes";

        }

    }

    @FXML
    /**
     * Called when the user presses the "load new campaign" button
     */
    public void loadNewCampaign(){

        CampaignPopup popUp = new CampaignPopup(this);

        //System.out.println("method called");

    }

    //TODO Generates a random string to populate UI for testing
    private String random(){

        Random r = new Random();
        return String.valueOf(r.nextInt(1000));

    }

    //TODO Just for testing
    @FXML
    public void test(){

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

        updateChart();
        updateHistogram();
        pieChartTest();

    }

}