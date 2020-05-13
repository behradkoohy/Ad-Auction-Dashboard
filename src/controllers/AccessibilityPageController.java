package controllers;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class AccessibilityPageController {

    //Make sure to get a reference to this
    private RootController controller;

    private static Stage stage;

    private boolean largeFont;
    private boolean highContrast;

    //The normal non large font size
    private static final int NORMAL = 13;

    //The accessible large font size
    private static final int LARGE = 18;

    public void init(RootController controller){

        this.controller = controller;

    }

    @FXML
    public void initialize(){

        largeFont = false;
        highContrast = false;

        System.out.println("accessibility initialize method was called");

    }

    @FXML
    public void toggleLargeFont(){

        largeFont = !largeFont;

        if(largeFont){

            changeFontSize(controller.getRoot(), LARGE);

        } else {

            changeFontSize(controller.getRoot(), NORMAL);

        }

    }

    @FXML
    public void toggleHighContrast(){

        Scene s = stage.getScene();

        highContrast = !highContrast;

        if(highContrast){

            s.getStylesheets().add("views/stylesheets/highContrast.css");

        } else {

            s.getStylesheets().clear();

        }

    }

    /**
     * Takes the root node and a boolean whether or not you
     * are turning high contrast on or off
     *
     * @param root
     * @param highContrast
     */
    public void setToHighContrast(Node root, boolean highContrast){

        if(root instanceof Pane){



        } else {

            root.setStyle("-fx-font-size: " + LARGE);

        }

    }

    public void changeFontSize(Node root, int FONTSIZE){

        if(root instanceof Pane){

            Pane p = (Pane) root;

            if(p.getChildren() != null && p.getChildren().size() > 0){

                for(Node n: p.getChildren()){

                    changeFontSize(n, FONTSIZE);

                }

            }

        } else {

            String currentStyle = root.getStyle();
            currentStyle = root.getStyle().split(";-fx-font-size:")[0];
            root.setStyle(currentStyle + ";-fx-font-size: " + FONTSIZE);

        }

    }

    public static void setStage(Stage s){

        stage = s;

    }

}