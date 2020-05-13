import controllers.AccessibilityPageController;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.application.Application;


//JavaFX application has to extend Application
public class Main extends Application {

    //JavaFX requires to be in the same class as the main method
    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = null;
        root = FXMLLoader.load(getClass().getClassLoader().getResource("views/layout.fxml"));
        primaryStage.setScene(new Scene(root));

        //Accessibility controller needs access to the stage for high contrast
        AccessibilityPageController.setStage(primaryStage);
        primaryStage.setTitle("Ad Auction Dashboard");
        primaryStage.show();

    }

    public static void main(String[] args) {
        launch(args);
    }


}