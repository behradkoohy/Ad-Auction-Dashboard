package popups;

import com.jfoenix.controls.JFXButton;
import com.jfoenix.controls.JFXComboBox;
import controllers.RootController;
import javafx.collections.ObservableSet;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.print.Printer;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Label;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

import java.awt.*;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;
import java.util.stream.Collectors;

import javafx.scene.control.ScrollPane;
import javafx.print.Paper;

public class PrintPopup {

    private RootController controller;

    private Stage popup;
    private JFXComboBox pageSelection;
    private JFXComboBox printSelection;

    private Printer defaultprinter = Printer.getDefaultPrinter();
    private ObservableSet<Printer> printers = Printer.getAllPrinters();

    private String page_name = "";
    private Printer selectedPrinter;

    private HashMap<String, Node> pages = new HashMap<String, Node>(){{
        put("Basic statistics", null);
        put("Advanced statistics", null);
        put("Detailed pie charts", null);
        put("Comparisons", null);
    }};

    private Node getPage( String pageId ){
        Scene scene = this.controller.getTabPane().getScene();
        Node chart= (Node) scene.lookup( pageId );
        WritableImage snapshot = chart.snapshot(null, null);
        ImageView printImage = new ImageView(snapshot);
        printImage.setPreserveRatio(true);
        printImage.setFitWidth(500);
        Node printNode = (Node) printImage;
        return printNode;
    }

    public PrintPopup(RootController controller){

        System.out.println("print popup made");

        this.controller = controller;
        controller.doGUITask(this::init);

        this.pages.put("Basic statistics", this.getPage("#basicStatsContent"));
        this.pages.put("Advanced statistics", this.getPage("#advancedStatsContent"));
        this.pages.put("Detailed pie charts", this.getPage("#detailedPieChartsPage"));
        this.pages.put("Comparisons",this.getPage("#compareContent"));
    }

    private void init(){

        popup = new Stage();
        VBox vBox = new VBox();
        vBox.setAlignment(Pos.CENTER);
        vBox.setPadding(new Insets(0,10,0,10));
        popup.setScene(new Scene(vBox));
        vBox.setMaxHeight(700);
        vBox.setMaxWidth(200);
        Label l = new Label("Please choose the page you would like to print");
        l.setPadding(new Insets(10,0,0,0));
        l.setWrapText(true);
        vBox.getChildren().add(l);
        pageSelection = new JFXComboBox();
        pageSelection.setPrefWidth(175);
        pageSelection.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(pageSelection);
        Label l2 = new Label("Please choose the print device you wish to use");
        l2.setPadding(new Insets(10,0,0,0));
        l2.setWrapText(true);
        vBox.getChildren().add(l2);
        printSelection = new JFXComboBox();
        printSelection.setPrefWidth(175);
        printSelection.setPadding(new Insets(10,0,0,0));
        vBox.getChildren().add(printSelection);
        JFXButton button = new JFXButton("PRINT");
        button.setOnAction(e -> printSelection());
        vBox.setMargin(button, new Insets(10,0,20,0));
        button.setStyle("-fx-text-fill: white;" + " -fx-background-color:  #9575cd");
        vBox.getChildren().add(button);

        // save to pdf
        JFXButton buttonSavePdf = new JFXButton("SAVE TO PDF");
        buttonSavePdf.setOnAction(e -> saveToPdf());
        vBox.setMargin(buttonSavePdf, new Insets(10,0,20,0));
        buttonSavePdf.setStyle("-fx-text-fill: white;" + " -fx-background-color:  #9575cd");
        vBox.getChildren().add(buttonSavePdf);

        popup.show();

        this.addPagesToSelect();
        this.getPrinters();
        printSelection.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    this.selectedPrinter = this.findPrinter(newValue.toString());
                }
        );
        pageSelection.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    this.page_name = newValue.toString();
                }
        );

    }

    private void addPagesToSelect(){
        Set<String> optionPages = this.pages.keySet();
        for( String page : optionPages ){
            pageSelection.getItems().add(page);
        }
    }

    /**
     * Called by the button
     */
    public void printSelection(){

        //Get the selected page and selected print device

        PrinterJob printerJob = PrinterJob.createPrinterJob(selectedPrinter);
        Window window = printSelection.getScene().getWindow();


        if (printerJob != null) {
            // show status page
            boolean proceed = printerJob.showPageSetupDialog(window);
            if(proceed) {

                Node node = this.pages.get(this.page_name);
                boolean success = printerJob.printPage(node);
                if (success) {
                    printerJob.endJob();
                    this.controller.success("Printing succeeded");
                }else{
                    this.controller.error("Printing failed");
                }
            }
        }

        //At the end of the method close the print popup
        popup.close();

    }

    public void saveToPdf(){

        //Get the selected page and selected print device

        PrinterJob printerJob = PrinterJob.createPrinterJob();
        Window window = printSelection.getScene().getWindow();

        if (printerJob != null) {
            boolean proceed = printerJob.showPrintDialog(window);
            if(proceed) {
                Node node = this.pages.get(this.page_name);
                boolean success = printerJob.printPage(node);
                if (success) {
                    printerJob.endJob();
                    this.controller.success("Printing succeeded");
                }else{
                    this.controller.error("Saving failed");
                }
            }
        }

        //At the end of the method close the print popup
        popup.close();

    }

    public void getPrinters(){
        if( defaultprinter != null ) {
            for (Printer printer : printers) {
                printSelection.getItems().add(printer.getName());
            }
        }else{
            printSelection.getItems().addAll("No printers installed.");
        }
    }

    private Printer findPrinter(String printerName){
        return this.printers.stream().filter( p -> p.getName().equals(printerName) ).collect(Collectors.toList()).get(0);
    }


}