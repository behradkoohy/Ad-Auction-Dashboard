package models;

import controllers.ControllerInjector;
import controllers.RootController;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class GUIHandler {

    private Stage managerStage;
    private Stage mainStage;

    public GUIHandler(){

        ControllerInjector.associateHandler(this);
        System.out.println("associated handler");

        //Platform.startup(() ->  showCampaignManager());
        Platform.startup(() -> init());

    }

    private void init(){

        Parent mainParent = null;

        try {
            mainParent = FXMLLoader.load(getClass().getClassLoader().getResource("views/layout.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        mainStage = new Stage();
        //Keep the main stage invisible for now
        mainStage.setScene(new Scene(mainParent));

        Parent campaignParent = null;

        try {
            campaignParent = FXMLLoader.load(getClass().getClassLoader().getResource("views/CampaignManager.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        managerStage = new Stage();
        managerStage.setScene(new Scene(campaignParent));

        //KEEP MAIN STAGE HIDDEN UNTIL CAMPAIGN IS LOADED
        mainStage.show(); //comment this line out

        managerStage.show();

    }

    /**
     * Makes the campaign manager load from its fxml file
     */
    public void showCampaignManager(){

        Parent root = null;

        try {
            root = FXMLLoader.load(getClass().getClassLoader().getResource("views/CampaignManager.fxml"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        managerStage = new Stage();
        managerStage.setScene(new Scene(root));
        managerStage.show();

    }

    public void closeCampaignManager(){

        RootController.doGUITask(() -> managerStage.close());

    }

    /**
     * Makes the main app load from its fxml file
     */
    public void showMainGUI(){

        RootController.doGUITask(() -> mainStage.show());

    }

}