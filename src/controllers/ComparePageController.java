package controllers;

import com.jfoenix.controls.JFXComboBox;
import javafx.fxml.FXML;
import javafx.scene.chart.LineChart;

public class ComparePageController {

    //COMPARE PAGE
    @FXML private JFXComboBox firstCampaignComboBox;
    @FXML private JFXComboBox secondCampaignComboBox;
    @FXML private LineChart firstChart;
    @FXML private LineChart secondChart;
    //END COMPARE PAGE

    /**
     * Does nothing for now until compare page is implemented
     */
    public void updateData(String campaignName){

    }

}