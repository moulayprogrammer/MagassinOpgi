package Controllers.OutputControllers.DechargeControllers;

import Models.Decharge;
import com.sun.org.apache.xml.internal.security.Init;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import javax.print.PrintService;
import javax.print.PrintServiceLookup;
import java.net.URL;
import java.util.ResourceBundle;

public class PrintController implements Initializable {

    @FXML
    private ComboBox<String> cbPrinters;
    @FXML
    private TextField tfNbr;
    @FXML
    private Button btnPrint;

    private final ObservableList<String> comboPrintersData = FXCollections.observableArrayList();
    private final PrintService[] printServices = PrintServiceLookup.lookupPrintServices(null, null);
    private Decharge decharge;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshPrinters();
    }

    public void Init(Decharge decharge){
        this.decharge = decharge;
    }

    private void refreshPrinters() {
        comboPrintersData.clear();
        try {
            if (printServices.length != 0){
                for (PrintService printer : printServices) comboPrintersData.add(printer.getName());
                cbPrinters.setItems(comboPrintersData);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionPrint(ActionEvent event) {
        try {
            int indexP = cbPrinters.getSelectionModel().getSelectedIndex();

            if (indexP != -1) {
                PrintService printService = printServices[indexP];
                Print print = new Print(this.decharge);

                print.CreatePdfFacture();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    private void ActionCancel(ActionEvent event) {
        try {
            ((Stage)btnPrint.getScene().getWindow()).close();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
