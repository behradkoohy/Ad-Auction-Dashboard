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

public class CampaignManagerUITest extends ApplicationTest{
    @Override
    public void start (Stage stage) throws Exception {
        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResource("../views/layout.fxml"));

        stage.setTitle("Ad Auction Analytics");

        Scene scene = new Scene(root);

        stage.setScene(scene);

        //Not needed later
        //primaryStage.setResizable(false);

        stage.show();
    }

    @Test
    public void loadNoName(){
        clickOn("#loadNewCampaign");
        alert_dialog_has_header_and_content(null, "Please specify a campaign name, or make sure the campaign name is not empty");
        press(KeyCode.ENTER);
    }


    @Test
    public void testNoFilesLoaded() throws InterruptedException {
        Thread.sleep(500);
        clickOn("#newCampaignField");
        Thread.sleep(500);
        new FxRobot().press(KeyCode.T);
        Thread.sleep(500);
        new FxRobot().press(KeyCode.E);
        Thread.sleep(500);
        new FxRobot().press(KeyCode.S);
        Thread.sleep(500);
        new FxRobot().press(KeyCode.T);
        Thread.sleep(500);
        new FxRobot().press(KeyCode.F);
        Thread.sleep(500);
        new FxRobot().press(KeyCode.X);
        Thread.sleep(500);
        clickOn("#loadNewCampaign");
        alert_dialog_has_header_and_content(null, "Please make sure you have selected the 3 unique csv log files!");
        press(KeyCode.ENTER);
    }

    @Test
    public void testStatisticsTab(){
        clickOn("#basicStatisticsTabBtn");
    }

    @Test
    public void testImpressionsStatisticsTab() throws InterruptedException {
        clickOn("#basicStatisticsTabBtn");
        Thread.sleep(500);
        clickOn("#impressionsTgl");
//        Thread.sleep(5000);
    }

    @Test
    public void testConversionsStatisticsTab() throws InterruptedException {
        clickOn("#basicStatisticsTabBtn");
        Thread.sleep(500);
        clickOn("#conversionsTgl");
        Thread.sleep(1000);
    }

    @Test
    public void testUniqueStatisticsTab() throws InterruptedException {
        clickOn("#basicStatisticsTabBtn");
        Thread.sleep(500);
        clickOn("#uniqueTgl");
        Thread.sleep(1500);
    }

    @Test
    public void testBouncesStatisticsTab() throws InterruptedException {
        clickOn("#basicStatisticsTabBtn");
        Thread.sleep(500);
        clickOn("#bouncesTgl");
        Thread.sleep(2000);
    }

    @Test
    public void testclicksStatisticsTab() throws InterruptedException {
        clickOn("#basicStatisticsTabBtn");
        Thread.sleep(500);
        clickOn("#clicksTgl");
        Thread.sleep(3000);
    }

    @Test
    public void testadvTab(){
        clickOn("#advStatsTabBtn");
    }

    @Test
    public void testCtrAdvTab() throws InterruptedException {
        clickOn("#advStatsTabBtn");
        Thread.sleep(500);
        clickOn("#ctrBtn");
//        Thread.sleep(5000);
    }

    @Test
    public void testcpaAdvTab() throws InterruptedException {
        clickOn("#advStatsTabBtn");
        Thread.sleep(500);
        clickOn("#cpaBtn");
        Thread.sleep(750);
    }

    @Test
    public void testcpcAdvTab() throws InterruptedException {
        clickOn("#advStatsTabBtn");
        Thread.sleep(500);
        clickOn("#cpcBtn");
        Thread.sleep(750);
    }
    @Test
    public void testcpmAdvTab() throws InterruptedException {
        clickOn("#advStatsTabBtn");
        Thread.sleep(500);
        clickOn("#cpmBtn");
        Thread.sleep(1000);
    }
    @Test
    public void testbounceRateAdvTab() throws InterruptedException {
        clickOn("#advStatsTabBtn");
        Thread.sleep(500);
        clickOn("#bounceRateBtn");
        Thread.sleep(1200);
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
