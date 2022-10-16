package Controllers.InputControllers.InputGasolineControllers;

import BddPackage.RechargeGasolineOperation;
import Models.RechargeGasoline;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    DatePicker dpFactDate,dpBCDate;
    @FXML
    TextField tfPrice,tfNumFact,tfNumBC;
    @FXML
    Button btnUpdate;

    private final RechargeGasolineOperation operation = new RechargeGasolineOperation();
    private RechargeGasoline gasoline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpBCDate.setValue(LocalDate.now());
        dpFactDate.setValue(LocalDate.now());
    }

    public void Init(RechargeGasoline gasoline){
        this.gasoline = gasoline;
    }

    @FXML
    private void ActionAnnul(){

        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmation.setHeaderText("CONFIRMER L'ANNULATION");
        alertConfirmation.setContentText("Êtes-vous sûr d'annuler le bon de recharge?");
        alertConfirmation.initOwner(this.tfPrice.getScene().getWindow());
        Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("D'ACCORD");

        Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.setText("Annuler");

        alertConfirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.CANCEL) {

            } else if (response == ButtonType.OK) {
                closeDialog(btnUpdate);
            }
        });

    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        try {
            String price = tfPrice.getText().trim();
            String numFact = tfNumFact.getText().trim();
            String numBC = tfNumBC.getText().trim();

            LocalDate dateFact = dpFactDate.getValue();
            LocalDate dateBC = dpBCDate.getValue();

            if (!price.isEmpty() && !numFact.isEmpty() && !numBC.isEmpty() && dateFact != null && dateBC != null ){

                RechargeGasoline gasoline = new RechargeGasoline();
                gasoline.setDateBC(dateBC);
                gasoline.setDateFact(dateFact);
                gasoline.setNumberBC(numBC);
                gasoline.setNumberFact(numFact);
                gasoline.setPrice(Double.parseDouble(price));

                boolean ins = update(gasoline);
                if (ins) {
                    closeDialog(this.btnUpdate);
                } else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("ATTENTION");
                    alertWarning.setContentText("ERREUR INCONNUE");
                    alertWarning.initOwner(this.tfPrice.getScene().getWindow());
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertWarning.showAndWait();
                }
            } else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION ");
                alertWarning.setContentText("Merci de remplir tous les champs");
                alertWarning.initOwner(this.tfPrice.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean update(RechargeGasoline gasoline) {
        boolean insert = false;
        try {
            insert = operation.update(gasoline,this.gasoline);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
