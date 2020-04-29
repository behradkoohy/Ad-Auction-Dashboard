package popups;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

/**
 * Simple loading popup with a progress bar, violates MVC so will
 * look to replace with an fxml version later but keep like this for now
 */
public class LoadPreviousPopup {

    private Stage popUp;
    private ProgressBar bar;

    public LoadPreviousPopup(String message){

        if(Platform.isFxApplicationThread()){

            this.init(message);

        } else {

            Platform.runLater(() -> this.init(message));

        }

    }

    private void init(String message){

        popUp = new Stage();
        popUp.setTitle("Loading");
        VBox v = new VBox();
        popUp.setScene(new Scene(v));

        Label l = new Label(message);

        bar = new ProgressBar();
        bar.setPrefWidth(250);
        bar.setPrefHeight(30);

        v.setAlignment(Pos.CENTER);
        v.getChildren().add(bar);
        v.getChildren().add(l);

        popUp.setResizable(false);
        popUp.show();

    }

    /**
     * Tell the popup to close from an external thread
     */
    public void close(){

        Platform.runLater(() -> popUp.close());

    }

    public void bind(ObservableValue v){

        bar.progressProperty().bind(v);

    }

}