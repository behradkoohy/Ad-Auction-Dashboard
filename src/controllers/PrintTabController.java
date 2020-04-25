package controllers;
import javafx.fxml.FXML;
import javafx.print.Printer;
import com.jfoenix.controls.JFXComboBox;
import javafx.collections.ObservableSet;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.stage.Window;

import java.util.stream.Collectors;

public class PrintTabController {

    private Controller controller;

    @FXML private JFXComboBox printersList;

    //Get all Printers
    private Printer defaultprinter = Printer.getDefaultPrinter();
    private ObservableSet<Printer> printers = Printer.getAllPrinters();

    public void init(Controller controller){
        this.controller = controller;
    }

    @FXML
    public void initialize() {
        getPrinters();

        printersList.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> {
                    this.selectPrinter(newValue.toString());
                }
        );
    }



    public void getPrinters(){
        if( defaultprinter != null ) {
            for (Printer printer : printers) {
                printersList.getItems().add(printer.getName());
            }
        }else{
            printersList.getItems().addAll("No printers installed.");
        }
    }

    private void selectPrinter(String printerName){
        Printer selectedPrinter = this.findPrinter(printerName);
        this.print(selectedPrinter);
    }

    private Printer findPrinter(String printerName){
        return this.printers.stream().filter( p -> p.getName().equals(printerName) ).collect(Collectors.toList()).get(0);
    }

    private void print(Printer selectedPrinter){
        PrinterJob printerJob = PrinterJob.createPrinterJob(selectedPrinter);
        Window window = printersList.getScene().getWindow();


        if (printerJob != null) {
            // show status page
            boolean proceed = printerJob.showPageSetupDialog(window);
            if(proceed) {
                Node node = this.controller.getLHS().getSelectionModel().getSelectedItem().getContent();
                boolean success = printerJob.printPage(node);
                if (success) {
                    printerJob.endJob();
                    this.controller.success("Printing succeeded");
                }else{
                    this.controller.error("Printing failed");
                }
            }
        }
    }
}
