package Controllers.OutputControllers.RechargeCarteGasolineControllers;


import BddPackage.EmployeeOperation;
import BddPackage.GasolineCardOperation;
import BddPackage.RechargeGasolineCardOperation;
import Models.Employee;
import Models.GasolineCard;
import Models.RechargeGasolineCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    DatePicker dpDateBC;
    @FXML
    TextField tfPrice, tfNumBN,tfNumBC;
    @FXML
    ComboBox<String> cbCarteNaftal,cbEmployee;
    @FXML
    Button btnInsert;

    private final RechargeGasolineCardOperation operation = new RechargeGasolineCardOperation();
    private final EmployeeOperation employeeOperation = new EmployeeOperation();
    private final GasolineCardOperation gasolineCardOperation = new GasolineCardOperation();

    private final ObservableList<String> comboEmployeeData = FXCollections.observableArrayList();
    private final ObservableList<String> comboGasolineCardData = FXCollections.observableArrayList();
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<GasolineCard> gasolineCards = new ArrayList<>();
    private Employee selectedEmployee;
    private GasolineCard selectedGasolineCard;
    private RechargeGasolineCard selectedRechargeGasolineCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        dpDateBC.setValue(LocalDate.now());

        refreshComboEmployee();
        refreshComboGasolineCard();
    }

    public void Init(RechargeGasolineCard gasolineCard){
        this.selectedRechargeGasolineCard = gasolineCard;

        tfNumBC.setText(gasolineCard.getNumber());
        tfNumBN.setText(gasolineCard.getNumberNaftal());
        dpDateBC.setValue(gasolineCard.getDate());
        tfPrice.setText(String.valueOf(gasolineCard.getPrice()));

        selectedEmployee = employeeOperation.get(gasolineCard.getIdEmp());
        selectedGasolineCard = gasolineCardOperation.get(gasolineCard.getIdGasolineCard());

        cbEmployee.getSelectionModel().select(selectedEmployee.getFirstName() + " " + selectedEmployee.getLastName());
        cbCarteNaftal.getSelectionModel().select(selectedGasolineCard.getNumber());
    }

    private void refreshComboEmployee() {
        clearCombo();
        try {
            employees = employeeOperation.getAll();

            for (Employee employee : employees){
                comboEmployeeData.add(employee.getFirstName() + " " + employee.getLastName());
            }

            cbEmployee.setItems(comboEmployeeData);
            cbEmployee.getSelectionModel().select(0);
            ActionComboEmployee();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbEmployee.getSelectionModel().clearSelection();
        comboEmployeeData.clear();
    }

    @FXML
    private void ActionComboEmployee(){
        int index = cbEmployee.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            selectedEmployee = employees.get(index);
        }
    }

    private void refreshComboGasolineCard() {
        clearComboGasoline();
        try {
            gasolineCards = gasolineCardOperation.getAll();

            for (GasolineCard gasolineCard : gasolineCards){
                comboGasolineCardData.add(gasolineCard.getNumber());
            }

            cbCarteNaftal.setItems(comboGasolineCardData);
            cbCarteNaftal.getSelectionModel().select(0);
            ActionComboGasolineCard();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearComboGasoline(){
        cbCarteNaftal.getSelectionModel().clearSelection();
        comboGasolineCardData.clear();
        gasolineCards.clear();
        selectedGasolineCard = null;
    }

    @FXML
    private void ActionComboGasolineCard(){
        int index = cbCarteNaftal.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            selectedGasolineCard = gasolineCards.get(index);
        }
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
                closeDialog(btnInsert);
            }
        });

    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        try {
            String price = tfPrice.getText().trim();
            String numBN = tfNumBN.getText().trim();
            String numBC = tfNumBC.getText().trim();

            LocalDate dateBC = dpDateBC.getValue();

            int indexEmp = cbEmployee.getSelectionModel().getSelectedIndex();
            int indexCard = cbCarteNaftal.getSelectionModel().getSelectedIndex();

            if (!price.isEmpty() && !numBN.isEmpty() && !numBC.isEmpty()  && dateBC != null && indexCard != -1 && indexEmp != -1){


                RechargeGasolineCard gasoline = new RechargeGasolineCard();
                gasoline.setNumber(numBC);
                gasoline.setDate(dateBC);
                gasoline.setIdEmp(this.selectedEmployee.getId());
                gasoline.setIdGasolineCard(this.selectedGasolineCard.getId());
                gasoline.setNumberNaftal(numBN);
                gasoline.setPrice(Double.parseDouble(price));

                boolean ins = update(gasoline);
                if (ins) {
                    closeDialog(this.btnInsert);

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

    private boolean update(RechargeGasolineCard gasoline) {
        boolean update = false;
        try {
            update = operation.update(gasoline,this.selectedRechargeGasolineCard);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
