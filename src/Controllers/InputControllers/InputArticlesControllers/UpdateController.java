package Controllers.InputControllers.InputArticlesControllers;

import BddPackage.*;
import Models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    DatePicker dpBRDate,dpFactDate,dpBCDate;
    @FXML
    Label lbSumTotal;
    @FXML
    ComboBox<String> cbProvider;
    @FXML
    TextField tfRecherche,tfNumBR,tfNumFact,tfNumBC;
    @FXML
    Button btnUpdate;
    @FXML
    TableView<List<StringProperty>>  tablePorches;
    @FXML
    TableColumn<List<StringProperty>,String> tcId,tcName,tcUnit,tcPriceU,tcQte,tcPriceTotal;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final InputOperation operation = new InputOperation();
    private final StoreCardTempOperation storeCardTempOperation = new StoreCardTempOperation();
    private final ProviderOperation providerOperation = new ProviderOperation();
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ObservableList<String> comboProviderData = FXCollections.observableArrayList();
    private ArrayList<Provider> providers = new ArrayList<>();
    private final List<Double> priceList = new ArrayList<>();
    private Provider providerSelected;
    private Input inputSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        tcId.setCellValueFactory(data -> data.getValue().get(0));
        tcName.setCellValueFactory(data -> data.getValue().get(1));
        tcUnit.setCellValueFactory(data -> data.getValue().get(2));
        tcQte.setCellValueFactory(data -> data.getValue().get(3));
        tcPriceU.setCellValueFactory(data -> data.getValue().get(4));
        tcPriceTotal.setCellValueFactory(data -> data.getValue().get(5));

        refreshComboProviders();
    }

    public void Init(Input input){

        this.inputSelected =  input;
        this.providerSelected = providerOperation.get(input.getIdProvider());


        tfNumBR.setText(input.getNumber());
        tfNumFact.setText(input.getNumberFact());
        tfNumBC.setText(input.getNumberBC());

        dpBRDate.setValue(input.getDate());
        dpFactDate.setValue(input.getDateFact());
        dpBCDate.setValue(input.getDateBC());

        cbProvider.getSelectionModel().select(providerSelected.getName());

        refreshComponent();
    }

    @FXML
    private void ActionComboProvider(){
        int index = cbProvider.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            providerSelected = providers.get(index);
        }
    }

    private void refreshComponent(){
        try {
            if (this.conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();

            String query = "SELECT COMPONENT_INPUT.ID_ARTICLE, ARTICLE.NAME, UNIT.NAME AS NAME_UNIT, COMPONENT_INPUT.PRICE, COMPONENT_INPUT.QTE\n" +
                    "FROM COMPONENT_INPUT,ARTICLE,UNIT WHERE COMPONENT_INPUT.ID_INPUT = ? AND COMPONENT_INPUT.ID_ARTICLE = ARTICLE.ID \n" +
                    "AND ARTICLE.ID_UNIT = UNIT.ID;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,this.inputSelected.getId());
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();
                data.add(0, new SimpleStringProperty(String.valueOf(resultSet.getInt("ID_ARTICLE"))));
                data.add(1, new SimpleStringProperty(resultSet.getString("NAME")));
                data.add(2, new SimpleStringProperty(resultSet.getString("NAME_UNIT")));
                data.add(3, new SimpleStringProperty(String.valueOf(resultSet.getInt("QTE"))));
                data.add(4, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));
                data.add(5, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (resultSet.getDouble("PRICE") * resultSet.getInt("QTE")))));

                priceList.add(resultSet.getDouble("PRICE"));
                dataTable.add(data);
            }
            tablePorches.setItems(dataTable);
            sumTotalTablePorches();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void sumTotalTablePorches(){

        double totalPrice = 0.0;
        int totalling = 0;

        for (int i = 0; i < dataTable.size() ; i++) {
            int qte = Integer.parseInt(dataTable.get(i).get(3).getValue());
            totalling += qte;
            double price = priceList.get(i);
            totalPrice += (price * qte);
        }
        double totalFacture = totalPrice;
        lbSumTotal.setText(String.format(Locale.FRANCE, "%,.2f", totalPrice));
//        lbSumWeight.setText(String.valueOf(totalling));
    }

    private void refreshComboProviders() {
        clearCombo();
        try {
            providers =  providerOperation.getAll();
            providers.forEach(provider -> {
                comboProviderData.add(provider.getName());
            });
            cbProvider.setItems(comboProviderData);
            if (providers.size() != 0) {
                providerSelected = providers.get(0);
                cbProvider.getSelectionModel().select(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbProvider.getSelectionModel().clearSelection();
        comboProviderData.clear();
        providers.clear();
    }

    @FXML
    private void ActionAddProvider(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/ProviderViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshComboProviders();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionSearchProvider(){
        try {

            Provider provider = new Provider();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputArticlesViews/SelectProviderView.fxml"));
            DialogPane temp = loader.load();
            SelectProviderController controller = loader.getController();
            controller.Init(provider);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfNumFact.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            if (provider.getName() != null){
                cbProvider.getSelectionModel().select(provider.getName());
                int index = cbProvider.getSelectionModel().getSelectedIndex();
                this.providerSelected = providers.get(index);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAnnulledUpdate(){

        try {
            closeDialog(btnUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        try {
            String numBR = tfNumBR.getText().trim();
            String numFact = tfNumFact.getText().trim();
            String numBC = tfNumBC.getText().trim();

            LocalDate dateBR = dpBRDate.getValue();
            LocalDate dateFact = dpFactDate.getValue();
            LocalDate dateBC = dpBCDate.getValue();

            if (!numBR.isEmpty() && !numFact.isEmpty() && !numBC.isEmpty() && dateBR != null && dateFact != null && dateBC != null && providerSelected.getId() != -1){

                Input input = new Input();

                input.setNumber(numBR);
                input.setDate(dateBR);
                input.setNumberFact(numFact);
                input.setDateFact(dateFact);
                input.setNumberBC(numBC);
                input.setDateBC(dateBC);
                input.setIdProvider(this.providerSelected.getId());

                boolean ins = update(input);
                if (ins) {
                    updateStoreCardTemp(dateBR);
                    closeDialog(this.btnUpdate);

                } else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("ATTENTION");
                    alertWarning.setContentText("ERREUR INCONNUE");
                    alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertWarning.showAndWait();
                }

            } else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION ");
                alertWarning.setContentText("Merci de remplir tous les champs");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean update(Input input) {
        boolean insert = false;
        try {
            insert = operation.update(input,this.inputSelected);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean updateStoreCardTemp(LocalDate date){
        boolean insert = false;
        try {
            insert = storeCardTempOperation.updateDateStoreByInput(date,this.inputSelected.getId());
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
