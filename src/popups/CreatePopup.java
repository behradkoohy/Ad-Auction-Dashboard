package popups;

import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple loading popup with a progress bar for each csv file thats
 * being imported, violates MVC so will look to replace with an fxml
 * version later but keep like this for now
 */
public class CreatePopup {

    private Stage popUp;
    private ProgressBar clickBar;
    private ProgressBar impressionBar;
    private ProgressBar serverBar;

    public CreatePopup(){

        if(Platform.isFxApplicationThread()){

            this.init();

        } else {

            Platform.runLater(() -> this.init());

        }

    }

    private void init(){

        popUp = new Stage();
        popUp.setTitle("Importing Campaign Data");
        GridPane g = new GridPane();
        popUp.setScene(new Scene(g));

        clickBar = new ProgressBar();
        clickBar.setPrefHeight(30);
        clickBar.setPrefWidth(250);
        g.add(new Label("Click CSV file progress: "), 0, 0);
        g.add(clickBar, 1, 0);

        impressionBar = new ProgressBar();
        impressionBar.setPrefHeight(30);
        impressionBar.setPrefWidth(250);
        g.add(new Label("Impression CSV file progress: "), 0, 1);
        g.add(impressionBar, 1, 1);

        serverBar = new ProgressBar();
        serverBar.setPrefHeight(30);
        serverBar.setPrefWidth(250);
        g.add(new Label("Server CSV file progress: "), 0, 2);
        g.add(serverBar, 1, 2);

        popUp.setResizable(false);
        popUp.show();

    }

    /**
     * Tell the popup to close from an external thread
     */
    public void close(){

        Platform.runLater(() -> popUp.close());

    }

    public void bindClick(ObservableValue v){

        clickBar.progressProperty().bind(v);

    }

    public void bindImpression(ObservableValue v){

        impressionBar.progressProperty().bind(v);

    }

    public void bindServer(ObservableValue v){

        serverBar.progressProperty().bind(v);

    }

}