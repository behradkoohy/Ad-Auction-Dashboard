package controllers;

import com.jfoenix.controls.*;
import daos.ClickDao;
import daos.DaoInjector;
import daos.ImpressionDao;
import daos.ServerEntryDao;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.effect.GaussianBlur;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;
import javafx.util.Duration;
import models.Filter;
import popups.PrintPopup;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Collections;

import static java.time.temporal.ChronoUnit.DAYS;
import static java.time.temporal.ChronoUnit.HOURS;

/**
 * As the filter panel controls the whole app it makes
 * sense for the root controller to also be the controller for the filter panel,
 * so this is now the controller for the filter panel
 */
public class RootController {

    //OTHER CONTROLLERS
    @FXML private BasicPageController basicStatsPageController;
    @FXML private AdvancedPageController advancedStatsPageController;
    @FXML private ComparePageController comparePageController;
    @FXML private CampaignManagerController campaignManagerPageController;
    @FXML private AccessibilityPageController accessibilityPageController;
    @FXML private DetailedPieChartsController detailedPieChartsPageController;

    @FXML private StackPane rootPane;
    @FXML private JFXTabPane tabPane;

    //FILTER PANEL
    @FXML private Circle circle;
    @FXML private AnchorPane sideAnchor;
    @FXML private Polygon arrow;
    @FXML private JFXComboBox filterTargetComboBox;
    @FXML private JFXDatePicker dateFromPicker;
    @FXML private JFXTimePicker timeFromPicker;
    @FXML private JFXDatePicker dateToPicker;
    @FXML private JFXTimePicker timeToPicker;
    @FXML private Spinner granularitySpinner;
    @FXML private JFXComboBox granularityComboBox;
    @FXML private JFXSlider bouncePageSlider;
    @FXML private JFXSlider bounceDurationSlider;
    @FXML private Label bouncePageLabel;
    @FXML private Label bounceDurationLabel;

    //BOTTOM BAR
    @FXML private JFXButton campaignButton;
    @FXML private JFXButton printButton;
    @FXML private Label campaignLabel;

    //Loading indicator
    @FXML private JFXSpinner spinner;
    @FXML private BorderPane mainApp;

    private ClickDao clickDao = DaoInjector.newClickDao();
    private ImpressionDao impressionDao = DaoInjector.newImpressionDao();
    private ServerEntryDao serverEntryDao = DaoInjector.newServerEntryDao();

    //Which campaign's data is currently being shown
    private String currentCampaign;


    private LocalDate dateFrom;
    private LocalTime timeFrom;
    private LocalDate dateTo;
    private LocalTime timeTo;

    private boolean circleIsRight;
    private boolean circleIsClickable;

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

    //Context
    private boolean news;
    private boolean shopping;
    private boolean socialMedia;
    private boolean blog;
    private boolean hobbies;
    private boolean travel;

    @FXML
    public void initialize(){

        disableOtherTabs();

        granTimeUnit = ChronoUnit.DAYS;
        granDigit = 1;
        ObservableList<String> timeUnits = FXCollections.observableArrayList("Hours", "Days", "Weeks");
        granularityComboBox.setItems(timeUnits);
        granularitySpinner.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(1,100) {
        });
        //Set default to days
        granularityComboBox.getSelectionModel().select(1);

        circleIsRight = true;
        circleIsClickable = true;

        this.initFilterTab();

        campaignManagerPageController.init(this);
        basicStatsPageController.init(this);
        advancedStatsPageController.init(this);
        comparePageController.init(this);
        accessibilityPageController.init(this);
        detailedPieChartsPageController.init(this);

        //Initially the date spinners will be from week ago until now
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime weekAgo = now.minus(1, ChronoUnit.WEEKS);
        dateToPicker.setValue(now.toLocalDate());
        updateDTo();
        timeToPicker.setValue(now.toLocalTime());
        updateTTo();
        dateFromPicker.setValue(weekAgo.toLocalDate());
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
        news = true;
        shopping = true;
        socialMedia = true;
        blog = true;
        hobbies = true;
        travel = true;

    }

    public void disableOtherTabs(){

        doGUITask(() -> {

            //Disable all other tabs except the campaign manager tab or accessibility
            for(int i = 1; i < 5; i++){

                tabPane.getTabs().get(i).setDisable(true);

            }

        });

    }

    public void enableOtherTabs(){

        doGUITask(() -> {

            for(int i = 1; i < 5; i++){

                tabPane.getTabs().get(i).setDisable(false);

            }

        });

    }

    public void goToBasicsPage(){

        SingleSelectionModel<Tab> model = tabPane.getSelectionModel();
        doGUITask(() -> model.select(1));

    }

    /**
     * Call this method whenever a new / different campaign
     * has been selected by the user to populate all parts
     * of the UI with the data of the newly selected campaign
     *
     * @param campaign
     */
    public void loadCampaignData(String campaign){

        new Thread(() -> {

            startLoadingIndicator();
            currentCampaign = campaign;
            doGUITask(() -> campaignLabel.setText(currentCampaign));
            comparePageController.setFirstChartLabel(currentCampaign);
            comparePageController.loadOtherCampaignsCombo();
            LocalDateTime from = getFromDateForCampaign(currentCampaign);
            LocalDateTime to = getToDateForCampaign(currentCampaign);
            setDateTimeFrom(from);
            setDateTimeTo(to);
            this.loadData();
            endLoadingIndicator();
            enableOtherTabs();
            goToBasicsPage();

        }).start();

    }


    /**
     * Causes all parts of the app to update their data, ie each controller
     * to update all the ui components they are responsible for
     */
    private void loadData() {
        basicStatsPageController.updateData();
        advancedStatsPageController.updateData();
        comparePageController.updateData();
        detailedPieChartsPageController.updateData();
    }

    @FXML
    public void reloadDataButtonMethod() {
        new Thread(() -> {
            //this.handleCircleClick();
            startLoadingIndicator();
            this.loadData();
            endLoadingIndicator();
        }).start();
    }

    private LocalDateTime getFromDateForCampaign(String campaignName) {
        ArrayList<LocalDateTime> mins = new ArrayList<>();
        mins.add(clickDao.getMinDateFromCampaign(campaignName));
        mins.add(impressionDao.getMinDateFromCampaign(campaignName));
        mins.add(serverEntryDao.getMinDateFromCampaign(campaignName));
        Collections.sort(mins);
        return mins.get(0);
    }

    private LocalDateTime getToDateForCampaign(String campaignName) {

        ArrayList<LocalDateTime> maxs = new ArrayList<>();
        maxs.add(clickDao.getMaxDateFromCampaign(campaignName));
        maxs.add(impressionDao.getMaxDateFromCampaign(campaignName));
        maxs.add(serverEntryDao.getMaxDateFromCampaign(campaignName));
        Collections.sort(maxs);
        return maxs.get(maxs.size() - 1);

    }


    private void initFilterTab(){

        double val = circle.getRadius();

        Translate circleTrans = new Translate();
        circleTrans.setX(val + 20);

        Translate arrowTrans = new Translate();
        arrowTrans.setY(val + (val / 3) + 40);

        circle.getTransforms().add(circleTrans);
        arrow.getTransforms().add(arrowTrans);

        Translate anchorTrans = new Translate();
        anchorTrans.setX(sideAnchor.getMaxWidth() + 10);
        sideAnchor.getTransforms().add(anchorTrans);

        FadeTransition startCircle = new FadeTransition(Duration.seconds(8), circle);
        startCircle.setFromValue(circle.getOpacity());
        startCircle.setToValue(0);

        FadeTransition startArrow = new FadeTransition(Duration.seconds(8), arrow);
        startArrow.setFromValue(arrow.getOpacity());
        startArrow.setToValue(0);

        ParallelTransition parallelTransition = new ParallelTransition();
        parallelTransition.getChildren().add(startCircle);
        parallelTransition.getChildren().add(startArrow);
        parallelTransition.play();

        initFilterTabListeners();

    }

    private void initFilterTabListeners(){

        circle.setOnMouseExited(e -> handleCircleExit());
        arrow.setOnMouseEntered(e -> handleCircleEnter());
        circle.setOnMouseEntered(e -> handleCircleEnter());
        circle.setOnMouseClicked(e -> handleCircleClick());
        arrow.setOnMouseClicked(e -> handleCircleClick());

    }

    private void handleCircleExit(){

        if(circleIsRight && circleIsClickable){

            FadeTransition arrowTrans = new FadeTransition(Duration.seconds(0.5), arrow);
            arrowTrans.setFromValue(arrow.getOpacity());
            arrowTrans.setToValue(0);

            FadeTransition transition = new FadeTransition(Duration.seconds(0.5), circle);
            transition.setFromValue(circle.getOpacity());
            transition.setToValue(0);

            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(arrowTrans);
            parallelTransition.getChildren().add(transition);

            parallelTransition.play();

        }

    }

    private void handleCircleEnter(){

        if(circleIsRight && circleIsClickable){

            FadeTransition arrowTrans = new FadeTransition(Duration.seconds(0.5), arrow);
            arrowTrans.setFromValue(arrow.getOpacity());
            arrowTrans.setToValue(1);

            FadeTransition transition = new FadeTransition(Duration.seconds(0.5), circle);
            transition.setFromValue(circle.getOpacity());
            transition.setToValue(1);

            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(arrowTrans);
            parallelTransition.getChildren().add(transition);
            parallelTransition.play();

        }

    }

    private void handleCircleClick(){

        if(circleIsClickable){

            circleIsClickable = false;
            circleIsRight = !circleIsRight;

            FadeTransition arrowFade = new FadeTransition(Duration.seconds(0.1), arrow);
            arrowFade.setFromValue(arrow.getOpacity());
            arrowFade.setToValue(1);

            FadeTransition fade = new FadeTransition(Duration.seconds(0.1), circle);
            fade.setFromValue(circle.getOpacity());
            fade.setToValue(1);

            TranslateTransition translateCircle = new TranslateTransition(Duration.seconds(0.5), circle);
            TranslateTransition translateArrow = new TranslateTransition(Duration.seconds(0.5), arrow);
            TranslateTransition translateAnchor = new TranslateTransition(Duration.seconds(0.5), sideAnchor);

            double amt = sideAnchor.getMaxWidth();

            if(!circleIsRight){

                amt *= -1;

            }

            translateCircle.setByX(amt);
            translateAnchor.setByX(amt);

            translateArrow.setByX(amt);

            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(translateCircle);
            parallelTransition.getChildren().add(translateAnchor);
            parallelTransition.getChildren().add(translateArrow);
            parallelTransition.setOnFinished(g -> {

                circleIsClickable = true;
                handleCircleExit();

            });

            fade.setOnFinished(e -> parallelTransition.play());
            fade.play();

        }

    }


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
    public void toggleNews(){

        news = !news;

    }

    @FXML
    public void toggleShopping(){

        shopping = !shopping;

    }

    @FXML
    public void toggleSocialMedia(){

        socialMedia = !socialMedia;

    }

    @FXML
    public void toggleBlog(){

        blog = !blog;

    }

    @FXML
    public void toggleHobbies(){

        hobbies = !hobbies;

    }

    @FXML
    public void toggleTravel(){

        travel = !travel;

    }

    @FXML
    public void updateBouncePageLabel(){

        bouncePageLabel.setText(String.valueOf(Math.round(bouncePageSlider.getValue())));
        bouncePageLabel.setStyle("-fx-text-fill: deepskyblue");
        bounceDurationLabel.setStyle("-fx-text-fill: black");
        basicStatsPageController.getMetricsModel().setBouncePages((int) Math.round(bouncePageSlider.getValue()));
        advancedStatsPageController.getMetricsModel().setBouncePages((int) Math.round(bouncePageSlider.getValue()));
        comparePageController.getMetricsModelFirst().setBouncePages((int) Math.round(bouncePageSlider.getValue()));
        comparePageController.getMetricsModelSecond().setBouncePages((int) Math.round(bouncePageSlider.getValue()));

    }

    @FXML
    public void updateBounceDurationLabel(){

        bounceDurationLabel.setText(String.valueOf(Math.round(bounceDurationSlider.getValue())));
        bounceDurationLabel.setStyle("-fx-text-fill: deepskyblue");
        bouncePageLabel.setStyle("-fx-text-fill: black");
        basicStatsPageController.getMetricsModel().setBounceTime(java.time.Duration.ofSeconds((int) Math.round(bounceDurationSlider.getValue())));
        advancedStatsPageController.getMetricsModel().setBounceTime(java.time.Duration.ofSeconds((int) Math.round(bounceDurationSlider.getValue())));
        comparePageController.getMetricsModelFirst().setBounceTime(java.time.Duration.ofSeconds((int) Math.round(bounceDurationSlider.getValue())));
        comparePageController.getMetricsModelSecond().setBounceTime(java.time.Duration.ofSeconds((int) Math.round(bounceDurationSlider.getValue())));

    }

    /**
     * Shows the spinner loading indicator and blurs the app
     */
    public void startLoadingIndicator(){

        doGUITask(() -> {

            spinner.setDisable(false);
            spinner.setVisible(true);
            mainApp.setEffect(new GaussianBlur(30));

        });

    }

    /**
     * Hides the spinner loading indicator and unblurs the app
     */
    public void endLoadingIndicator(){

        doGUITask(() -> {

            spinner.setDisable(true);
            spinner.setVisible(false);
            mainApp.setEffect(null);

        });

    }

    //BOUNCE SLIDER CHANGE METHODS HERE

    public String getCurrentCampaign(){

        return currentCampaign;

    }

    public LocalDateTime getPeriodStart(){

        return LocalDateTime.of(dateFrom, timeFrom);

    }

    public LocalDateTime getPeriodEnd(){

        return LocalDateTime.of(dateTo, timeTo);

    }

    @FXML
    /**
     * Called every time the value of the from date picker changes
     */
    public void updateDFrom(){

        dateFrom = dateFromPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the to date picker changes
     */
    public void updateDTo(){

        System.out.println(dateToPicker.getValue());
        dateTo = dateToPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the from time picker changes
     */
    public void updateTFrom(){

        timeFrom = timeFromPicker.getValue();

    }

    @FXML
    /**
     * Called every time the value of the to time picker changes
     */
    public void updateTTo(){

        timeTo = timeToPicker.getValue();

    }

    @FXML
    /**
     * Called by the print button
     */
    public void print(){

        System.out.println("print method called");
        new PrintPopup(this);

    }

    public void setDateTimeFrom(LocalDateTime from) {

        //Updates tFrom and dFrom automatically
        dateFromPicker.setValue(from.toLocalDate());
        timeFromPicker.setValue(from.toLocalTime());

    }

    public void setDateTimeTo(LocalDateTime to) {

        //Updates tFrom and dFrom automatically
        dateToPicker.setValue(to.toLocalDate());
        timeToPicker.setValue(to.toLocalTime());

    }

    /**
     * WHENEVER YOU ARE CALLING ANY GUI UPDATE METHODS
     * MAKE SURE YOU CALL THEM AS A RUNNABLE INSERTED
     * INTO THIS AS AN ARGUMENT AS IT FORCES
     * ANY GUI UPDATE CODE TO TAKE PLACE ON THE
     * JAVAFX THREAD REGARDLESS OF THREAD IT IS
     * CALLED FROM
     */
    public static void doGUITask(Runnable runnable){

        if(Platform.isFxApplicationThread()){

            runnable.run();

        } else {

            //This makes the runnable instead be added to the javafx queue and run on javafx thread
            Platform.runLater(runnable);

        }

    }

    public void error(String message){

        doGUITask(() -> {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();

        });

    }

    public void success(String message){

        doGUITask(() -> {

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();

        });

    }

    /**
     * Calculates the time and date difference specified by the user,
     * and returns the string of the unit that should be used as a metric
     *
     * @return metric whether the metric is minutes, hours, days or weeks
     */
    public String calcMetric(){

        LocalDateTime start = LocalDateTime.of(dateFrom, timeFrom);
        LocalDateTime end = LocalDateTime.of(dateTo, timeTo);

        long days = start.until(end, ChronoUnit.DAYS);
        long hours = start.until(end, ChronoUnit.HOURS);

        int unitsDifference = 0;

        if(days >= 30){

            unitsDifference = Math.round(start.until(end, ChronoUnit.WEEKS));
            return "Weeks";

        } else if(days >= 1){

            unitsDifference = Math.round(days);
            return "Days";

        } else if(hours >= 1){

            unitsDifference = Math.round(hours);
            return "Hours";

        } else {

            unitsDifference = Math.round(start.until(end, ChronoUnit.MINUTES));
            return "Minutes";

        }

    }

    public java.time.Duration calcDuration() {

        int digits = getGranDigits();
        ChronoUnit unit = getGranUnit();
        java.time.Duration dur;

        switch (unit) {
            case HOURS:
                dur = java.time.Duration.of(digits, HOURS);
                break;

            case DAYS:
                dur = java.time.Duration.of(digits, DAYS);
                break;

            case WEEKS:
                dur = java.time.Duration.of(digits, DAYS).multipliedBy(7);
                break;

            default:
                dur = java.time.Duration.ofDays(1);
        }

        return dur;

    }

    public ChronoUnit getGranUnit(){

        ChronoUnit granTimeUnit = null;

        switch (String.valueOf(granularityComboBox.getValue())) {
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

        return granTimeUnit;

    }

    public int getGranDigits(){

        System.out.println("value of spinner: " + granularitySpinner);
        return granDigit;

    }


    private int granDigit = 1;
    private ChronoUnit granTimeUnit;

    @FXML
    /**
     * called when spinner changes
     */
    public void updateGranSpin(){
        granDigit = (int) granularitySpinner.getValue();
    }

    @FXML
    /**
     * called when combobox changes
     */
    public void updateGranCombo(){
        System.out.println("COMBO");
        switch (String.valueOf(granularityComboBox.getValue())) {
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

    /**
     * Returns the double rounded to 2 decimal places, used
     * for presenting data in the stats tab
     * @return
     */
    public static double to2DP(double x){

        x = (double) Math.round(x * 100);
        x /= 100;
        return x;

    }

    public Filter getFilter() {
        return new Filter(male, female, lt25, btwn2534, btwn3544, btwn4554, gt55, lowIncome, medIncome, highIncome,
                news, shopping, socialMedia, blog, hobbies, travel);
    }

    public Node getRoot(){

        return rootPane;

    }

}