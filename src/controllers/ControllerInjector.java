package controllers;

import javafx.scene.control.ProgressBar;
import javafx.stage.Stage;
import models.GUIHandler;

public class ControllerInjector {

    private static AdvancedPageController advancedPage;
    private static BasicPageController basicPage;
    private static CampaignManagerController campaignManager;
    private static ComparePageController comparePage;
    private static RootController rootController;
    private static SettingsPageController settingsPage;
    private static GUIHandler handler;

    //The campaign progress bar is out of 3
    private static ProgressBar campaignProgressBar;

    public static void associateRoot(RootController ref){

        System.out.println("associated root");
        rootController = ref;

    }

    public static RootController getRootController(){

        return rootController;

    }

    public static void associateCampaignManager(CampaignManagerController ref){

        System.out.println("associated campaign manager");
        campaignManager = ref;

    }

    public static CampaignManagerController getCampaignManager(){

        return campaignManager;

    }

    public static GUIHandler getHandler(){

        return handler;

    }

    public static void associateHandler(GUIHandler ref){

        System.out.println("assoicated gui handler");
        handler = ref;

    }

    public static void incrementCreateNewCampaignProgress(){

        RootController.doGUITask(() ->
                campaignProgressBar.setProgress(campaignProgressBar.getProgress() + (1/3)));

    }

    public static void incrementLoadPreviousCampaignProgress(){

        //RootController.doGUITask(() -> campaignProgressBar);

    }

}