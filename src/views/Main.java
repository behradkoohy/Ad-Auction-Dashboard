package views;

/*
import daos.ClickDao;
import entities.Click;*/
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.time.LocalDateTime;

//JavaFX application has to extend Application
public class Main extends Application {

    //JavaFX requires to be in the same class as the main method
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();

        Parent root = loader.load(getClass().getResource("sample.fxml"));
        primaryStage.setTitle("Ad Auction Analytics");

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        //Not needed later
        //primaryStage.setResizable(false);

        primaryStage.show();

    }

    public static void main(String[] args) {

        /*
        //Testing
        ClickDao clickDao = new ClickDao();
        Click newClick = new Click();
        newClick.setId(1234562922096180000L);
        newClick.setDate(LocalDateTime.now());
        newClick.setClickCost(1.245378);
        clickDao.save(newClick);
        clickDao.getClicks().forEach(Click::printClick);*/

        //Launches the GUI
        launch(args);

    }

}