package controllers;

import com.jfoenix.controls.*;
import javafx.animation.FadeTransition;
import javafx.animation.ParallelTransition;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.PieChart;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Spinner;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Polygon;
import javafx.scene.transform.Translate;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class RootController {

    public static void main(String[] args){

        new Tester();

    }

    static class Tester {

        public Tester(){

            //FOR TESTING
            Platform.startup(() -> {

                Parent root = null;

                try {
                    root = FXMLLoader.load(getClass().getClassLoader().getResource("views/layout.fxml"));
                } catch (IOException e) {
                    e.printStackTrace();
                }

                Stage stage = new Stage();
                stage.setScene(new Scene(root));
                stage.show();

            });

        }

    }

    @FXML
    public void initialize(){

        initFilterTab();

    }

    @FXML private BorderPane mainApp;
    @FXML private ScrollPane scrollPane;
    @FXML private ScrollPane sideScrollPane;
    @FXML private BorderPane borderPane;
    @FXML private BorderPane arrowBorder;
    @FXML private FlowPane borderBody;
    @FXML private StackPane rootPane;
    @FXML private JFXTabPane tabPane;
    @FXML private Circle circle;
    @FXML private AnchorPane sideAnchor;
    @FXML private Polygon arrow;

    private void initFilterTab(){

        //Make sure the filter panel goes to the right by the width of the filter vbox

        TranslateTransition moveRight = new TranslateTransition(Duration.millis(1), sideOverlay);
        moveRight.setByX(326);

        System.out.println("tab pane width: " + tabPane.getWidth());
        System.out.println("tab pane max width: " + tabPane.getMaxWidth());

        System.out.println("root pane width: " + rootPane.getWidth());
        System.out.println("root pane max width: " + rootPane.getMaxWidth());

        double val = circle.getRadius();

        Translate circleTrans = new Translate();
        circleTrans.setX(val);

        Translate arrowTrans = new Translate();
        arrowTrans.setY(val + (val / 3));

        //They are the same relative position to each other
        circle.getTransforms().add(circleTrans);
        arrow.getTransforms().add(arrowTrans);

        Translate anchorTrans = new Translate();
        anchorTrans.setX(sideAnchor.getMaxWidth() + 10);
        sideAnchor.getTransforms().add(anchorTrans);

        //sideOverlay.setPickOnBounds(false);
        //spinner.setPickOnBounds(false);
        //filterIndicatorStack.setPickOnBounds(false);

        //So that the scrolling doesnt get messed up, undisable it each time it slides in
        //borderPane.setDisable(true);
        //arrowBorder.setDisable(true);
        //sideOverlay.setDisable(true);
        System.out.println("starting animation...");
        moveRight.play();
        moveRight.setOnFinished(e -> System.out.println("Done animation"));

        //sideOverlay.setOnMouseClicked(e -> System.out.println("Top stack thing was clicked!"));
        //scrollPane.setOnMouseClicked(e -> System.out.println("Main app was clicked!"));
        //filterShowHideButton.setVisible(false);
        //filterArrow.setVisible(false);
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

        //start.play();

        /*
        TranslateTransition sideTest = new TranslateTransition(Duration.seconds(5), sideAnchor);
        sideTest.setByX(-sideAnchor.getMaxWidth());
        sideTest.play();*/

        initFilterTabListeners();

    }

    private void initFilterTabListeners(){

        circle.setOnMouseExited(e -> handleCircleExit());
        arrow.setOnMouseEntered(e -> handleCircleEnter());
        circle.setOnMouseEntered(e -> handleCircleEnter());
        circle.setOnMouseClicked(e -> handleCircleClick());
        arrow.setOnMouseClicked(e -> handleCircleClick());

    }

    private boolean circleIsRight = true;
    private boolean circleIsClickable = true;

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

            System.out.println("passed circle click boolean");

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

            //circleIsRight = !circleIsRight;

            ParallelTransition parallelTransition = new ParallelTransition();
            parallelTransition.getChildren().add(translateCircle);
            parallelTransition.getChildren().add(translateAnchor);
            parallelTransition.getChildren().add(translateArrow);
            parallelTransition.setOnFinished(g -> {

                circleIsClickable = true;
                //circleIsRight = !circleIsRight;
                handleCircleExit();

            });

            fade.setOnFinished(e -> parallelTransition.play());
            fade.play();

        }

    }



    //FILTER PANEL
    @FXML private JFXSpinner spinner;
    @FXML private JFXComboBox filterTargetComboBox;
    @FXML private StackPane sideOverlay;
    @FXML private VBox filterVBox;
    @FXML private StackPane filterIndicatorStack;
    @FXML private Circle filterShowHideButton;
    @FXML private Polygon filterArrow;

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
    public void toggleMale(){}
    @FXML
    public void toggleFemale(){}
    @FXML
    public void toggleLt25(){}
    @FXML
    public void toggleBtwn2534(){}
    @FXML
    public void toggleBtwn3544(){}
    @FXML
    public void toggleBtwn4554(){}
    @FXML
    public void toggleGt55(){}
    @FXML
    public void toggleLow(){}
    @FXML
    public void toggleMed(){}
    @FXML
    public void toggleHigh(){}
    @FXML
    public void toggleNews(){}
    @FXML
    public void toggleShopping(){}
    @FXML
    public void toggleSocialMedia(){}
    @FXML
    public void toggleBlog(){}
    @FXML
    public void toggleHobbies(){}
    @FXML
    public void toggleTravel(){}
    @FXML
    public void updateDFrom(){}
    @FXML
    public void updateTFrom(){}
    @FXML
    public void updateDTo(){}
    @FXML
    public void updateTTo(){}
    @FXML
    public void reloadData(){}
    @FXML
    public void updateGranSpin(){}
    @FXML
    public void updateGranCombo(){}
    @FXML
    public void updateBouncePageLabel(){}
    @FXML
    public void updateBounceDurationLabel(){}


    /*
    METHODS FOR UPDATING DATE AND TIME PICKERS HERE

    RELOAD DATA BUTTON METHOD HERE

    BOUNCE SLIDER RESPONSE METHODS HERE
     */
    @FXML private JFXDatePicker dateFromPicker;
    @FXML private JFXTimePicker timeFromPicker;
    @FXML private JFXDatePicker dateToPicker;
    @FXML private JFXTimePicker timeToPicker;
    @FXML private Spinner granularitySpinner;
    @FXML private JFXComboBox granularityComboBox;
    @FXML private JFXSlider bouncePageSlider;
    @FXML private JFXSlider bounceDurationSlider;
    //END FILTER PANEL

    //BOTTOM BAR
    @FXML private JFXButton campaignButton;
    @FXML private JFXButton printButton;
    @FXML private Label campaignLabel;
    //END BOTTOM BAR

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

            Platform.runLater(runnable);

        }

    }

    public static void error(String message){

        doGUITask(() -> {

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText(null);
            alert.setContentText(message);
            alert.showAndWait();

        });

    }

}