package Controllers.OutputControllers.RechargeCarteGasolineControllers;


import BddPackage.ConnectBD;
import BddPackage.EmployeeOperation;
import BddPackage.GasolineCardOperation;
import BddPackage.RechargeGasolineCardOperation;
import Models.Employee;
import Models.GasolineCard;
import Models.RechargeGasoline;
import Models.RechargeGasolineCard;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    DatePicker dpDateBC;
    @FXML
    TextField tfPrice, tfNumBN,tfNumBC;
    @FXML
    ComboBox<String> cbCarteNaftal,cbEmployee;
    @FXML
    Button btnInsert;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final RechargeGasolineCardOperation operation = new RechargeGasolineCardOperation();
    private final EmployeeOperation employeeOperation = new EmployeeOperation();
    private final GasolineCardOperation gasolineCardOperation = new GasolineCardOperation();

    private final ObservableList<String> comboEmployeeData = FXCollections.observableArrayList();
    private final ObservableList<String> comboGasolineCardData = FXCollections.observableArrayList();
    private ArrayList<Employee> employees = new ArrayList<>();
    private ArrayList<GasolineCard> gasolineCards = new ArrayList<>();
    private Employee selectedEmployee;
    private GasolineCard selectedGasolineCard;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        dpDateBC.setValue(LocalDate.now());

        refreshComboEmployee();
        refreshComboGasolineCard();
        getLastNumber();
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

    private void getLastNumber(){
        LocalDateTime dateTimeOutput = LocalDateTime.of(1990, Month.JANUARY, 1, 0, 0, 0);
        LocalDateTime dateTimeCarte = LocalDateTime.of(1990, Month.JANUARY, 1, 0, 0, 0);
        int nbrOutput = 0;
        int nbrCarte = 0;

        try {
            if (this.conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT OUTPUT.NUMBER, OUTPUT.INSERT_DATE FROM OUTPUT ORDER BY OUTPUT.INSERT_DATE DESC LIMIT 1;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){
                nbrOutput = Integer.parseInt(resultSet.getString("NUMBER"));
                dateTimeOutput = resultSet.getTimestamp("INSERT_DATE").toLocalDateTime();
            }
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        try {
            if (this.conn.isClosed()) conn = connectBD.connect();

            String query = "SELECT RECHARGE_GASOLINE_CARD.NUMBER , RECHARGE_GASOLINE_CARD.INSERT_DATE FROM RECHARGE_GASOLINE_CARD ORDER BY RECHARGE_GASOLINE_CARD.INSERT_DATE DESC LIMIT 1;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            if (resultSet.next()){
                nbrCarte = Integer.parseInt(resultSet.getString("NUMBER"));
                dateTimeCarte = resultSet.getTimestamp("INSERT_DATE").toLocalDateTime();
            }
            conn.close();
        }catch (Exception e) {
            e.printStackTrace();
        }

        if (dateTimeOutput.isAfter(dateTimeCarte)){
            nbrOutput++;
            tfNumBC.setText("000" + nbrOutput);
        }else {
            nbrCarte++;
            tfNumBC.setText("000" + nbrCarte);
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
    void ActionInsert(ActionEvent event) {

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

                boolean ins = insert(gasoline);
                if (ins) {
                    GasolineCard card = new GasolineCard();
                    card.setId(this.selectedGasolineCard.getId());
                    card.setLastBalance(Double.parseDouble(price));
                    card.setLastRechargeDate(dateBC);
                    updateGasolineCard(card);

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

    private boolean insert(RechargeGasolineCard gasoline) {
        boolean insert = false;
        try {
            insert = operation.insert(gasoline);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean updateGasolineCard(GasolineCard card) {
        boolean insert = false;
        try {
            insert = gasolineCardOperation.setLAST_BALANCE(card);
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
