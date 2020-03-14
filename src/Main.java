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
        FXMLLoader loader = new FXMLLoader();
        Parent root = loader.load(getClass().getResource("/views/layout.fxml"));

        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Ad Auction Dashboard");
        //Not needed later
        //primaryStage.setResizable(false);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}