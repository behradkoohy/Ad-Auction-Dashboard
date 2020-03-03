package views;

import com.jfoenix.controls.*;
import javafx.fxml.FXML;
import javafx.scene.chart.*;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.Tab;
import javafx.scene.layout.BorderPane;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Random;
import java.util.List;

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
    private boolean totalCostB;
    private boolean CTRB;
    private boolean CPAB;
    private boolean CPCB;
    private boolean CPMB;
    private boolean bounceRateB;

    //Accessibility settings
    private boolean highContrastMode;
    private boolean largeFontMode;

    //The number of "units" that will be displayed along the x axis
    private int unitsDifference;

    private CampaignHandler campaignHandler;

    /*
    Corresponding UI components that can be found in scene builder with these identifiers,
    this is what the "@FXML" annotation means

    When the application starts the FXML loader will inject the actual
    UI component references in scene builder with these variables
     */

    @FXML
    private JFXTabPane LHS;

    @FXML
    private JFXComboBox campaignDropDown;

    @FXML
    private JFXButton loadPrevious;

    @FXML
    private JFXTextField campaignName;

    @FXML
    private Label clickLogLabel;

    @FXML
    private Label impressionLogLabel;

    @FXML
    private Label serverLogLabel;

    @FXML
    //This can be changed each time the user switches to a new/different campaign
    private Label campaignNameLabel;

    @FXML
    private JFXDatePicker dFromPicker;

    @FXML
    private JFXDatePicker dToPicker;

    @FXML
    private JFXTimePicker timeFromPicker;

    @FXML
    private JFXTimePicker timeToPicker;

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

        //Initial state of checkboxes below the chart
        impressions = true;
        conversions = true;
        clicks = false;
        uniqueUsers = false;
        bounces = false;

        highContrastMode = false;
        largeFontMode = false;

        unitsDifference = 0;

        campaignHandler = new CampaignHandler(this, clickLogLabel,
                impressionLogLabel, serverLogLabel);

    }

    @FXML
    /**
     * This method is called when the FXML loader has finished injecting
     * references, so whenever you need to call a method on a UI component
     * when the program opens do it from here so you know for
     * certain it won't be null
     */
    public void initialize(){

        //TODO dont think this is needed as I can toggle in scene builder, keep here for now
        //lineChart.setAnimated(false);

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

        //Setting up the look of the pie charts
        genderPie.setTitle("Gender");
        genderPie.setLegendVisible(false);
        genderPie.setStyle("-fx-font-size: " + 10 + "px;");
        agePie.setTitle("Age");
        agePie.setLegendVisible(false);
        agePie.setStyle("-fx-font-size: " + 10 + "px;");
        incomePie.setTitle("Income");
        incomePie.setLegendVisible(false);
        incomePie.setStyle("-fx-font-size: " + 10 + "px;");

        //TODO remove
        test();

    }

    /**
     * Updates the pie chart with the latest data with all of the values
     * @param men
     * @param women
     * @param lt25
     * @param btwn2534
     * @param btwn3544
     * @param btwn4554
     * @param gt55
     * @param lowIncome
     * @param medIncome
     * @param highIncome
     */
    public void updatePieChartData(int men, int women, int lt25,
                                   int btwn2534, int btwn3544,
                                   int btwn4554, int gt55, int lowIncome,
                                   int medIncome, int highIncome){

        genderPie.getData().clear();
        agePie.getData().clear();
        incomePie.getData().clear();

        PieChart.Data gender1 = new PieChart.Data("Men", men);
        PieChart.Data gender2 = new PieChart.Data("Women", women);
        genderPie.getData().addAll(gender1, gender2);

        PieChart.Data age1 = new PieChart.Data("<25", lt25);
        PieChart.Data age2 = new PieChart.Data("25-34", btwn2534);
        PieChart.Data age3 = new PieChart.Data("35-44", btwn3544);
        PieChart.Data age4 = new PieChart.Data("45-54", btwn4554);
        PieChart.Data age5 = new PieChart.Data(">54", gt55);
        agePie.getData().addAll(age1, age2, age3, age4, age5);

        PieChart.Data income1 = new PieChart.Data("Low", lowIncome);
        PieChart.Data income2 = new PieChart.Data("Medium", medIncome);
        PieChart.Data income3 = new PieChart.Data("High", highIncome);
        incomePie.getData().addAll(income1, income2, income3);

    }

    //TODO refreshes pie charts with random data, only for testing so remove
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
     * Used by the campaign manager to go to next page after loading/creating a campaign
     */
    public void goToMainPage(){

        SingleSelectionModel<Tab> model = LHS.getSelectionModel();
        model.select(1);

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
    private void toggleTotal(){

        totalCostB = !totalCostB;
        updateChart();

    }

    @FXML
    private void toggleCTR(){

        CTRB = !CTRB;
        updateChart();

    }

    @FXML
    private void toggleCPA(){

        CPAB = !CPAB;
        updateChart();

    }

    @FXML
    private void toggleCPC(){

        CPCB = !CPCB;
        updateChart();

    }

    @FXML
    private void toggleCPM(){

        CPMB = !CPMB;
        updateChart();

    }

    @FXML
    private void toggleBounceRate(){

        bounceRateB = !bounceRateB;
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

        List data = new ArrayList(10);

        ChartHandler handler = new ChartHandler(lineChart, lineChartXAxis,
                lineChartYAxis, calcMetric(), data, unitsDifference, impressions,
                conversions, clicks, uniqueUsers, bounces, totalCostB, CTRB, CPAB,
                CPCB, CPMB, bounceRateB);

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
     * Called by the choose file for click log button
     */
    public void chooseClickLog(){

        campaignHandler.chooseClick();

    }

    @FXML
    /**
     * Called by the choose file for impression log button
     */
    public void chooseImpressionLog(){

        campaignHandler.chooseImpression();

    }

    @FXML
    /**
     * Called by the choose file for server log button
     */
    public void chooseServerLog(){

        campaignHandler.chooseServer();

    }

    @FXML
    /**
     * Called by the load campaign button, loads a previous campaign from the combo box
     */
    public void loadCampaign(){



    }

    @FXML
    /**
     * Called by the create campaign button
     */
    public void createCampaign(){

        campaignHandler.createCampaign();

    }

    /*
    public void loadNewCampaign(){

        CampaignPopup popUp = new CampaignPopup(this);

        //System.out.println("method called");

    }*/

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

    /**
     * Displays and shows an error dialog window with the given message
     * @param message
     */
    public void error(String message){

        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();

    }

}