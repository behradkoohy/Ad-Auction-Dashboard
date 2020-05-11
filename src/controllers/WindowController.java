package controllers;

import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Objects;

/**
 * This class manages all the different windows that will be open
 */
public class WindowController {

    private Stage managerStage;
    private Stage mainStage;

    public WindowController() {

        //RootController.setWindowController(this);
        //Platform.startup(() ->  showCampaignManager());
        Platform.startup(this::init);

    }


    private void init() {

        Parent mainParent = null;

        try {
            mainParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/layout.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainStage = new Stage();
        //Keep the main stage invisible for now
        assert mainParent != null;
        mainStage.setScene(new Scene(mainParent));

        Parent campaignParent = null;

        try {
            campaignParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/CampaignManager.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        managerStage = new Stage();
        assert campaignParent != null;
        managerStage.setScene(new Scene(campaignParent));


        //KEEP MAIN STAGE HIDDEN UNTIL CAMPAIGN IS LOADED
        //mainStage.show(); //comment this line out

        managerStage.show();

    }

    /**
     * Called when the campaign manager button is pressed to show the campaign manager
     */
    public void reloadCampaignManager(){

        Parent campaignParent = null;

        try {
            campaignParent = FXMLLoader.load(Objects.requireNonNull(getClass().getClassLoader().getResource("views/CampaignManager.fxml")));
        } catch (IOException e) {
            e.printStackTrace();
        }

        managerStage = new Stage();
        assert campaignParent != null;
        managerStage.setScene(new Scene(campaignParent));
        managerStage.show();

    }

    public void closeCampaignManager(){

        doGUITask(() -> managerStage.close());

    }


    public void showMainGUI(){

        doGUITask(() -> mainStage.show());

    }

    private void doGUITask(Runnable runnable){

        if(Platform.isFxApplicationThread()){

            runnable.run();

        } else {

            Platform.runLater(runnable);

        }

    }

}