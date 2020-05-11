package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;
import javafx.scene.control.Label;

public class ComparePageController {

    @FXML private JFXComboBox firstCampaignComboBox;
    @FXML private JFXComboBox secondCampaignComboBox;
    @FXML private LineChart firstChart;
    @FXML private LineChart secondChart;

    //Labels for each campaign in the comparison grid
    @FXML private Label firstNumImpressionsLabel;
    @FXML private Label firstNumClicksLabel;
    @FXML private Label firstNumUniquesLabel;
    @FXML private Label firstNumBouncesLabel;
    @FXML private Label firstNumConversionsLabel;
    @FXML private Label firstTotCostLabel;
    @FXML private Label firstCtrLabel;
    @FXML private Label firstCpaLabel;
    @FXML private Label firstCpcLabel;
    @FXML private Label firstCpmLabel;
    @FXML private Label firstBounceRateLabel;
    @FXML private Label secondNumImpressionsLabel;
    @FXML private Label secondNumClicksLabel;
    @FXML private Label secondNumUniquesLabel;
    @FXML private Label secondNumBouncesLabel;
    @FXML private Label secondNumConversionsLabel;
    @FXML private Label secondTotCostLabel;
    @FXML private Label secondCtrLabel;
    @FXML private Label secondCpaLabel;
    @FXML private Label secondCpcLabel;
    @FXML private Label secondCpmLabel;
    @FXML private Label secondBounceRateLabel;

    //Checkbox values for the compare page
    private boolean impressions;
    private boolean clicks;
    private boolean uniques;
    private boolean bounces;
    private boolean conversions;
    private boolean totCost;
    private boolean ctr;
    private boolean cpa;
    private boolean cpc;
    private boolean cpm;
    private boolean bounceRate;

    @FXML
    public void initialize(){

        impressions = true;
        clicks = true;
        uniques = true;
        bounces = true;
        conversions = true;
        totCost = true;
        ctr = true;
        cpa = true;
        cpc = true;
        cpm = true;
        bounceRate = true;

    }

    /**
     * Does nothing for now until compare page is implemented
     */
    public void updateData(){

    }

    @FXML
    public void toggleImpressions(){

        impressions = !impressions;

    }

    @FXML
    public void toggleClicks(){

        clicks = !clicks;

    }

    @FXML
    public void toggleUniques(){

        uniques = !uniques;

    }

    @FXML
    public void toggleBounces(){

        bounces = !bounces;

    }

    @FXML
    public void toggleConversions(){

        conversions = !conversions;

    }

    @FXML
    public void toggleCtr(){

        ctr = !ctr;

    }

    @FXML
    public void toggleCpa(){

        cpa = !cpa;

    }

    @FXML
    public void toggleCpc(){

        cpc = !cpc;

    }

    @FXML
    public void toggleCpm(){

        cpm = !cpm;

    }

    @FXML
    public void toggleBounceRate(){

        bounceRate = !bounceRate;

    }

}