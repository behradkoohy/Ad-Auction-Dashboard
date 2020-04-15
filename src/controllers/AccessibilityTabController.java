package controllers;

import javafx.fxml.FXML;

public class AccessibilityTabController {
    private Controller controller;

    //Accessibility settings
    private boolean highContrastMode;
    private boolean largeFontMode;

    public void init(Controller controller){
        this.controller = controller;
        this.highContrastMode = false;
        this.largeFontMode = false;
    }

    @FXML
    private void toggleHighContrast(){

        highContrastMode = !highContrastMode;

    }

    @FXML
    private void toggleLargeFont(){

        largeFontMode = !largeFontMode;

    }
}
