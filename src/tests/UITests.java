package tests;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DialogPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.MouseButton;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.testfx.api.FxAssert;
import org.testfx.api.FxRobot;
import org.testfx.api.FxToolkit;
import org.testfx.framework.junit.ApplicationTest;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;


import static org.junit.Assert.*;

public class UITests extends ApplicationTest{

    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResource("../views/sample.fxml"));

        stage.setTitle("Ad Auction Analytics");

        Scene scene = new Scene(root);

        stage.setScene(scene);

        //Not needed later
        //primaryStage.setResizable(false);

        stage.show();
    }

    @Before
    public void setUp () throws Exception {
    }

    @After
    public void tearDown () throws Exception {
        FxToolkit.hideStage();
        release(new KeyCode[]{});
        release(new MouseButton[]{});
    }

    @Test
    public void testNoFilesLoaded() {
        clickOn("#createCampaignBtn");
        alert_dialog_has_header_and_content(null, "Please make sure you have selected the 3 unique csv log files!");
        press(KeyCode.ENTER);
        alert_dialog_has_header_and_content(null, "Please make sure all 3 CSV files are unique!");
        press(KeyCode.ENTER);
    }


    @Test
    public void testStatisticsTab() {
        // Get to Graph tab
        press(KeyCode.RIGHT);
        // TODO: when the stats tab finally works
    }


    @Test
    public void testGraphTab() {
        // Get to Graph tab
        ArrayList<String> tags = new ArrayList<>(Arrays.asList(
                "#bounceRateToggle",
                "#cpmToggle",
                "#cpcToggle",
                "#cpaToggle",
                "#ctrToggle",
                "#bounceToggle",
                "#uniqueToggle",
                "#clicksToggle",
                "#conversationsToggle",
                "#impressionsToggle",
                "#conversationsToggle",
                "#impressionsToggle"
        ));
        new FxRobot().press(KeyCode.RIGHT);
        new FxRobot().press(KeyCode.RIGHT);
        try {
            Thread.sleep(1000);
        } catch ( InterruptedException e){
            System.out.println(e.getMessage());
        }
        Collections.shuffle(tags);
        for (String s : tags){
            clickOn(s);
        }
        try {
            Thread.sleep(1000);
        } catch ( InterruptedException e){
            System.out.println(e.getMessage());
        }
    }

    public void alert_dialog_has_header_and_content(final String expectedHeader, final String expectedContent) {
        final javafx.stage.Stage actualAlertDialog = getTopModalStage();
        assertNotNull(actualAlertDialog);

        final DialogPane dialogPane = (DialogPane) actualAlertDialog.getScene().getRoot();
        assertEquals(expectedHeader, dialogPane.getHeaderText());
        assertEquals(expectedContent, dialogPane.getContentText());
    }

    private javafx.stage.Stage getTopModalStage() {
        // Get a list of windows but ordered from top[0] to bottom[n] ones.
        // It is needed to get the first found modal window.
        final List<Window> allWindows = new ArrayList<>(robotContext().getWindowFinder().listWindows());
        Collections.reverse(allWindows);

        return (javafx.stage.Stage) allWindows
                .stream()
                .filter(window -> window instanceof javafx.stage.Stage)
                .filter(window -> ((javafx.stage.Stage) window).getModality() == Modality.APPLICATION_MODAL)
                .findFirst()
                .orElse(null);
    }

}
