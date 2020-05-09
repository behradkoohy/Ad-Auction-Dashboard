package controllers;

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

}