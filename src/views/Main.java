package views;

/*
import daos.ClickDao;
import entities.Click;*/

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

//JavaFX application has to extend Application
public class Main extends Application {

    //JavaFX requires to be in the same class as the main method
    @Override
    public void start(Stage primaryStage) throws Exception{

        FXMLLoader loader = new FXMLLoader();


        Parent root = loader.load(getClass().getResource("/views/sample.fxml"));

        primaryStage.setTitle("Ad Auction Analytics");

        Scene scene = new Scene(root);

        primaryStage.setScene(scene);

        //Not needed later
        //primaryStage.setResizable(false);

        primaryStage.show();
        
        

    }

    public static void main(String[] args) {
        //Testing ignore for now
        /*

        ImpressionDao impressionDao = new ImpressionDao();

        Click newClick = new Click("Second Campaign", 1234562922096180000L, LocalDateTime.now(), 1.245378 );
        clickDao.save(newClick);
        clickDao.getFromCampaign("First Campaign").forEach(Click::print);


        Impression newImpression = new Impression("Another Campaign", LocalDateTime.now(), 1234562922096180000L,
                Impression.Gender.MALE, Impression.Age.FROM35TO44, Impression.Income.MEDIUM, Impression.Context.NEWS,
                1.245378);
        Impression anotherImpression = new Impression("Another Campaign", LocalDateTime.now(), 1234564922096183300L,
                Impression.Gender.MALE, Impression.Age.OVER54, Impression.Income.LOW, Impression.Context.NEWS,
                0.448778);

        impressionDao.save(newImpression);
        impressionDao.save(anotherImpression);

        impressionDao.getByAge(Impression.Age.FROM35TO44).forEach(Impression::print);;
        */


        launch(args);

    }

}