import controllers.AccessibilityPageController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.application.Application;

import javax.swing.*;
import java.net.URL;


//JavaFX application has to extend Application
public class Main extends Application {

    //JavaFX requires to be in the same class as the main method
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = null;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("views/layout.fxml"));
        primaryStage.setScene(new Scene(root));
        try {
            URL iconURL = Main.class.getResource("/icon.png");
            java.awt.Image image = new ImageIcon(iconURL).getImage();
            com.apple.eawt.Application.getApplication().setDockIconImage(image);
            primaryStage.getIcons().add(new Image("/icon.png"));
        } catch (Exception e) {
            primaryStage.getIcons().add(new Image("/icon.png"));
        }
        //Accessibility controller needs access to the stage for high contrast
        AccessibilityPageController.setStage(primaryStage);
        primaryStage.setTitle("Ad Auction Dashboard");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}