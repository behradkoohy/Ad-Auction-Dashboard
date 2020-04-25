package popups;

import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.layout.FlowPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;

public class ImportPopup {

    private ProgressBar clickProgress;
    private ProgressBar serverProgress;
    private ProgressBar impressionProgress;

    private Stage popUp;

    public ImportPopup(){

        this.init();

    }

    private void init(){

        popUp = new Stage();
        popUp.setResizable(false);
        popUp.setTitle("Importing CSV files...");

        GridPane g = new GridPane();
        popUp.setScene(new Scene(g));

        g.add(new Label("Click file:"), 0, 0);
        clickProgress = new ProgressBar();
        g.add(clickProgress, 1, 0);

        g.add(new Label("Server file:"), 0, 1);
        serverProgress = new ProgressBar();
        g.add(serverProgress, 1, 1);

        g.add(new Label("Impression file:"), 0, 2);
        impressionProgress = new ProgressBar();
        g.add(impressionProgress, 1, 2);

        popUp.show();

    }

    public ProgressBar getClickProgress(){

        return clickProgress;

    }

    public ProgressBar getServerProgress(){

        return serverProgress;

    }

    public ProgressBar getImpressionProgress(){

        return impressionProgress;

    }

    public void close(){

        popUp.close();

    }

}