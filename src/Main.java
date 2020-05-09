import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.fxml.FXMLLoader;
import javafx.stage.Stage;
import javafx.application.Application;
import models.GUIHandler;


//JavaFX application has to extend Application
public class Main {

    public static void main(String[] args) {
        new GUIHandler();
    }

}