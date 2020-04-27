package popups;

import com.jfoenix.controls.JFXSpinner;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.layout.FlowPane;
import javafx.stage.Stage;

public class LoadingPopup {

    private Stage popUp;

    public LoadingPopup(String message){

        this.init(message);

    }

    private void init(String message){

        popUp = new Stage();
        popUp.setTitle("Loading");
        FlowPane p = new FlowPane();
        popUp.setScene(new Scene(p));

        JFXSpinner spinner = new JFXSpinner();

        p.getChildren().add(spinner);
        p.getChildren().add(new Label(message));

        popUp.setResizable(false);
        popUp.show();

    }

    /**
     * Tell the popup to close from an external thread
     */
    public void close(){

        Platform.runLater(() -> popUp.close());

    }

}