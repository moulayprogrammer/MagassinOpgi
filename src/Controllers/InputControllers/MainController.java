package Controllers.InputControllers;

import BddPackage.*;
import Controllers.InputControllers.InputArticlesControllers.UpdateController;
import Models.Input;
import Models.Provider;
import Models.RechargeGasoline;
import Models.StoreCard;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    TabPane tabPane;
    @FXML
    Tab tabBon,tabGasoline;
    @FXML
    TextField tfRecherche;
    @FXML
    Label lbProvider;
    @FXML
    ComboBox<String> cbProvider,cbFilter;
    @FXML
    DatePicker dpFrom,dpTo;
    @FXML
    TableView<List<StringProperty>> table,tableGasoline;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clProvider,clNumBR,clDateBR,clNumFact,clDateFact,clNumBC,clDateBC,clPrice,clConfirm;
    @FXML
    TableColumn<List<StringProperty>,String> clIdGasoline, clNumFactGasoline, clDateFactGasoline, clNumBCGasoline, clDateBCGasoline, clPriceGasoline;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ObservableList<List<StringProperty>> dataTableGasoline = FXCollections.observableArrayList();
    private final InputOperation inputOperation = new InputOperation();
    private final RechargeGasolineOperation rechargeGasolineOperation = new RechargeGasolineOperation();
    private final ComponentInputOperation componentInputOperation = new ComponentInputOperation();
    private final StoreCardOperation storeCardOperation = new StoreCardOperation();
    private final StoreCardTempOperation storeCardTempOperation = new StoreCardTempOperation();
    private final ProviderOperation providerOperation = new ProviderOperation();

    private final ObservableList<String> comboProviderData = FXCollections.observableArrayList();
    private final ObservableList<String> comboFilterData = FXCollections.observableArrayList();
    ArrayList<Provider> providers;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clProvider.setCellValueFactory(data -> data.getValue().get(1));
        clNumBR.setCellValueFactory(data -> data.getValue().get(2));
        clDateBR.setCellValueFactory(data -> data.getValue().get(3));
        clNumFact.setCellValueFactory(data -> data.getValue().get(4));
        clDateFact.setCellValueFactory(data -> data.getValue().get(5));
        clNumBC.setCellValueFactory(data -> data.getValue().get(6));
        clDateBC.setCellValueFactory(data -> data.getValue().get(7));
        clPrice.setCellValueFactory(data -> data.getValue().get(8));
        clConfirm.setCellValueFactory(data -> data.getValue().get(9));

        clIdGasoline.setCellValueFactory(data -> data.getValue().get(0));
        clNumFactGasoline.setCellValueFactory(data -> data.getValue().get(1));
        clDateFactGasoline.setCellValueFactory(data -> data.getValue().get(2));
        clNumBCGasoline.setCellValueFactory(data -> data.getValue().get(3));
        clDateBCGasoline.setCellValueFactory(data -> data.getValue().get(4));
        clPriceGasoline.setCellValueFactory(data -> data.getValue().get(5));

        refreshInput();
        refreshComboProviders();

        comboFilterData.addAll("Tout","Fournisseur","confirmé","Non confirmé");
        cbFilter.setItems(comboFilterData);
        cbFilter.getSelectionModel().select(0);

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            switch (newTab.getId()){
                case "tabBon":
                    refreshInput();
                    break;
                case "tabGasoline":
                    refreshGasoline();
                    break;
            }
        });
    }

    private void refreshComboProviders() {
        comboProviderData.clear();
        try {

            this.providers = providerOperation.getAll();
            for (Provider provider: providers){
                comboProviderData.add(provider.getName());
            }

            cbProvider.setItems(comboProviderData);
            cbProvider.getSelectionModel().select(0);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAdd(){

        String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (tabId){
            case "tabBon":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputArticlesViews/AddView.fxml"));
                    BorderPane temp = loader.load();
                    Stage stage = new Stage();
                    stage.setScene(new Scene(temp));
                    stage.setMaximized(true);
                    stage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
                    stage.getIcons().add(new Image("/Images/logo.png"));
                    stage.initOwner(this.tfRecherche.getScene().getWindow());
                    stage.showAndWait();

                    refreshInput();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
            case "tabGasoline":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputGasolineViews/AddView.fxml"));
                    DialogPane temp = loader.load();
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(temp);
                    dialog.resizableProperty().setValue(false);
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                    closeButton.setVisible(false);
                    dialog.showAndWait();

                    refreshGasoline();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    @FXML
    private void tableClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionUpdate();
        }
    }

    @FXML
    private void ActionUpdate(){

        String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (tabId){
            case "tabBon":
                try {
                    List<StringProperty> data = table.getSelectionModel().getSelectedItem();

                    if (data != null){
                        try {
                            Input input = inputOperation.get(Integer.parseInt(data.get(0).getValue()));

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputArticlesViews/UpdateView.fxml"));
                            DialogPane temp = loader.load();
                            UpdateController controller = loader.getController();
                            controller.Init(input);
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setDialogPane(temp);
                            dialog.resizableProperty().setValue(false);
                            dialog.initOwner(this.tfRecherche.getScene().getWindow());
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                            closeButton.setVisible(false);
                            dialog.showAndWait();

                            refreshInput();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un Bon Réception à modifier");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "tabGasoline":
                try {
                    List<StringProperty> data = tableGasoline.getSelectionModel().getSelectedItem();

                    if (data != null){
                        try {
                            RechargeGasoline gasoline = rechargeGasolineOperation.get(Integer.parseInt(data.get(0).getValue()));

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputGasolineViews/UpdateView.fxml"));
                            DialogPane temp = loader.load();
                            Controllers.InputControllers.InputGasolineControllers.UpdateController controller = loader.getController();
                            controller.Init(gasoline);
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setDialogPane(temp);
                            dialog.resizableProperty().setValue(false);
                            dialog.initOwner(this.tfRecherche.getScene().getWindow());
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                            closeButton.setVisible(false);
                            dialog.showAndWait();

                            refreshGasoline();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un Bon de Recharge à modifier");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @FXML
    private void ActionDelete(){
        String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (tabId){
            case "tabBon":
                try {
                    List<StringProperty> data = table.getSelectionModel().getSelectedItem();

                    if (data != null) {
                        if (data.get(9).getValue().equals("Non confirmé")) {
                            try {
                                Input input = inputOperation.get(Integer.parseInt(data.get(0).getValue()));

                                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                                alertConfirmation.setHeaderText("CONFIRMER LA SUPPRESSION");
                                alertConfirmation.setContentText("Êtes-vous sûr de supprimer le Bonne ?");
                                alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                                okButton.setText("D'ACCORD");

                                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                                cancel.setText("ANNULATION");

                                alertConfirmation.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.CANCEL) {
                                        alertConfirmation.close();
                                    } else if (response == ButtonType.OK) {

                                        delete(input);
                                        deleteComponentInput(input.getId());
                                        deleteStoreCardTemp(input.getId());

                                        refreshInput();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("ATTENTION");
                            alertWarning.setContentText("Le Bonne est confirmé et ne peut pas être supprimé");

                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un Bon Réception à supprimer");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case "tabGasoline":
                try {
                    List<StringProperty> data = tableGasoline.getSelectionModel().getSelectedItem();

                    if (data != null) {
                        try {
                            RechargeGasoline gasoline = rechargeGasolineOperation.get(Integer.parseInt(data.get(0).getValue()));

                            Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                            alertConfirmation.setHeaderText("CONFIRMER LA SUPPRESSION");
                            alertConfirmation.setContentText("Êtes-vous sûr de supprimer le Bonne ?");
                            alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");

                            Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                            cancel.setText("ANNULATION");

                            alertConfirmation.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.CANCEL) {
                                    alertConfirmation.close();
                                } else if (response == ButtonType.OK) {

                                    rechargeGasolineOperation.delete(gasoline);

                                    refreshGasoline();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un Bon de Recharge à supprimer");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }

    }

    private boolean delete(Input input) {
        boolean insert = false;
        try {
            insert = inputOperation.delete(input);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean deleteComponentInput(int idInput){
        boolean insert = false;
        try {
            insert = componentInputOperation.deleteByInput(idInput);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean deleteStoreCardTemp(int idInput){
        boolean insert = false;
        try {
            insert = storeCardTempOperation.deleteByInput(idInput);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    @FXML
    private void ActionConfirm(){
        String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (tabId){
            case "tabBon":
                try {
                    List<StringProperty> data = table.getSelectionModel().getSelectedItem();

                    if (data != null){
                        if (data.get(9).getValue().equals("Non confirmé")) {
                            try {
                                Input input = inputOperation.get(Integer.parseInt(data.get(0).getValue()));

                                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                                alertConfirmation.setHeaderText("CONFIRMERMATION");
                                alertConfirmation.setContentText("Êtes-vous sûr de Confirmer le Bonne ?");
                                alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                                okButton.setText("D'ACCORD");

                                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                                cancel.setText("ANNULATION");

                                alertConfirmation.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.CANCEL) {
                                        alertConfirmation.close();
                                    } else if (response == ButtonType.OK) {

                                        insertStoreCardsFromTemp(input);

                                        refreshInput();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("ATTENTION");
                            alertWarning.setContentText("Le Bonne est déja confirmé");
                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un Bon Réception à Confirmer");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
                break;
            case "tabGasoline":
                break;
        }
    }

    private void insertStoreCardsFromTemp(Input input){
        try {

            ArrayList<StoreCard> storeCardsTemp = storeCardTempOperation.getAllByInput(input.getId());

            for (StoreCard storeCardTemp : storeCardsTemp){

                storeCardOperation.insert(storeCardTemp);
                storeCardTempOperation.delete(storeCardTemp);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void comboFilterAction() {
        try {
            int select  = cbFilter.getSelectionModel().getSelectedIndex();
            LocalDate dateFrom = dpFrom.getValue();
            LocalDate dateTo = dpTo.getValue();
            if (select != -1 ) {
                if (conn.isClosed()) conn = connectBD.connect();
                dataTable.clear();

                cbProvider.setVisible(false);
                lbProvider.setVisible(false);

                String query = "";
                PreparedStatement preparedStmt;
                ResultSet resultSet;

                switch (select) {
                    case 0:
                        if (dateFrom != null && dateTo != null) ActionSearchDate();
                        else refreshInput();

                        break;
                    case 1:
                        cbProvider.setVisible(true);
                        lbProvider.setVisible(true);
                        refreshComboProviders();
                        comboFilterProviderAction();

                        break;
                    case 2:
                        if (dateFrom != null && dateTo != null) {
                            query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                                    "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                                    "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                                    "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = PROVIDER.ID AND INPUT.DATE BETWEEN ? AND ?;";
                            preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(dateFrom));
                            preparedStmt.setDate(2, Date.valueOf(dateTo));
                        }else {
                            query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                                    "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                                    "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                                    "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = PROVIDER.ID;";
                            preparedStmt = conn.prepareStatement(query);
                        }
                        resultSet = preparedStmt.executeQuery();
                        while (resultSet.next()) {
                            if (resultSet.getInt("CONFIRM") != 0) {
                                List<StringProperty> data = new ArrayList<>();

                                data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                                data.add(new SimpleStringProperty(resultSet.getString("NAME")));
                                data.add(new SimpleStringProperty(resultSet.getString("NUMBER")));
                                data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                                data.add(new SimpleStringProperty(resultSet.getString("NUMBER_FACTUR")));
                                data.add(new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                                data.add(new SimpleStringProperty(resultSet.getString("NUMBER_BC")));
                                data.add(new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                                data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));
                                data.add(new SimpleStringProperty("Confirmé"));

                                dataTable.add(data);
                            }
                        }
                        break;
                    case 3:
                        if (dateFrom != null && dateTo != null) {
                            query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                                    "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                                    "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                                    "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = PROVIDER.ID AND INPUT.DATE BETWEEN ? AND ?;";
                            preparedStmt = conn.prepareStatement(query);
                            preparedStmt.setDate(1, Date.valueOf(dateFrom));
                            preparedStmt.setDate(2, Date.valueOf(dateTo));
                        }else {
                            query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                                    "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                                    "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                                    "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = PROVIDER.ID;";
                            preparedStmt = conn.prepareStatement(query);
                        }
                        resultSet = preparedStmt.executeQuery();
                        while (resultSet.next()) {
                            if (resultSet.getInt("CONFIRM") == 0) {
                                List<StringProperty> data = new ArrayList<>();

                                data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                                data.add(new SimpleStringProperty(resultSet.getString("NAME")));
                                data.add(new SimpleStringProperty(resultSet.getString("NUMBER")));
                                data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                                data.add(new SimpleStringProperty(resultSet.getString("NUMBER_FACTUR")));
                                data.add(new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                                data.add(new SimpleStringProperty(resultSet.getString("NUMBER_BC")));
                                data.add(new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                                data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));
                                data.add(new SimpleStringProperty("Non confirmé"));

                                dataTable.add(data);
                            }
                        }
                        break;

                }
                conn.close();
                table.setItems(dataTable);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void comboFilterProviderAction() {
        try {
            int select  = cbProvider.getSelectionModel().getSelectedIndex();
            LocalDate dateFrom = dpFrom.getValue();
            LocalDate dateTo = dpTo.getValue();

            if (select != -1 ) {

                if (conn.isClosed()) conn = connectBD.connect();
                dataTable.clear();

                String query = "";
                PreparedStatement preparedStmt;

                if (dateFrom != null && dateTo != null) {
                    query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                            "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                            "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                            "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = ? AND INPUT.ID_PROVIDER = PROVIDER.ID AND INPUT.DATE BETWEEN ? AND ? ;";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,providers.get(select).getId());
                    preparedStmt.setDate(2, Date.valueOf(dateFrom));
                    preparedStmt.setDate(3, Date.valueOf(dateTo));
                }else {
                    query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                            "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                            "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                            "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = ? AND INPUT.ID_PROVIDER = PROVIDER.ID ;";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,providers.get(select).getId());
                }

                ResultSet resultSet = preparedStmt.executeQuery();

                while (resultSet.next()) {

                    List<StringProperty> data = new ArrayList<>();

                    data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                    data.add(new SimpleStringProperty(resultSet.getString("NAME")));
                    data.add(new SimpleStringProperty(resultSet.getString("NUMBER")));
                    data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                    data.add(new SimpleStringProperty(resultSet.getString("NUMBER_FACTUR")));
                    data.add(new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                    data.add(new SimpleStringProperty(resultSet.getString("NUMBER_BC")));
                    data.add(new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                    data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));
                    if (resultSet.getInt("CONFIRM") != 0) data.add(new SimpleStringProperty("Confirmé"));
                    else data.add(new SimpleStringProperty("Non confirmé"));

                    dataTable.add(data);
                }

                conn.close();

                table.setItems(dataTable);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionSearchDate(){
        try {
            LocalDate dateFrom = dpFrom.getValue();
            LocalDate dateTo = dpTo.getValue();

            if (cbFilter.getSelectionModel().getSelectedIndex() != 0) comboFilterAction();
            else {
                if (dateFrom != null && dateTo != null) {
                    if (conn.isClosed()) conn = connectBD.connect();
                    dataTable.clear();

                    String query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                            "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                            "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                            "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = PROVIDER.ID AND INPUT.DATE BETWEEN ? AND ?;";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setDate(1, Date.valueOf(dateFrom));
                    preparedStmt.setDate(2, Date.valueOf(dateTo));
                    ResultSet resultSet = preparedStmt.executeQuery();
                    while (resultSet.next()) {

                        List<StringProperty> data = new ArrayList<>();

                        data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                        data.add(new SimpleStringProperty(resultSet.getString("NAME")));
                        data.add(new SimpleStringProperty(resultSet.getString("NUMBER")));
                        data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                        data.add(new SimpleStringProperty(resultSet.getString("NUMBER_FACTUR")));
                        data.add(new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                        data.add(new SimpleStringProperty(resultSet.getString("NUMBER_BC")));
                        data.add(new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                        data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));
                        if (resultSet.getInt("CONFIRM") != 0) data.add(new SimpleStringProperty("Confirmé"));
                        else data.add(new SimpleStringProperty("Non confirmé"));

                        dataTable.add(data);
                    }

                    conn.close();

                    table.setItems(dataTable);
                } else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("ATTENTION");
                    alertWarning.setContentText("Veuillez remplir les dates");
                    alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertWarning.showAndWait();
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionRefreshDate(){
        try {
            clearRecherche();
            refreshInput();
        }catch (Exception e){
            e.printStackTrace();

        }
    }

    private void refreshInput(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();
            LocalDate dateFrom = dpFrom.getValue();
            LocalDate dateTo = dpTo.getValue();

            String query = "";
            PreparedStatement preparedStmt;

            if (dateFrom != null && dateTo != null) {
                query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                        "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                        "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                        "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = PROVIDER.ID AND INPUT.DATE BETWEEN ? AND ?;";
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setDate(1, Date.valueOf(dateFrom));
                preparedStmt.setDate(2, Date.valueOf(dateTo));
            }else {
                query = "SELECT INPUT.ID, INPUT.NUMBER, INPUT.DATE, PROVIDER.NAME, INPUT.NUMBER_FACTUR, INPUT.DATE_FACTUR, INPUT.NUMBER_BC, INPUT.DATE_FACTUR, \n" +
                        "(SELECT SUM(COMPONENT_INPUT.QTE * COMPONENT_INPUT.PRICE) FROM COMPONENT_INPUT WHERE COMPONENT_INPUT.ID_INPUT = INPUT.ID) AS PRICE,\n" +
                        "(SELECT ID FROM STORE_CARD WHERE STORE_CARD.ID_INPUT = INPUT.ID) AS CONFIRM \n" +
                        "FROM INPUT, PROVIDER WHERE INPUT.ID_PROVIDER = PROVIDER.ID;";
                preparedStmt = conn.prepareStatement(query);
            }
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getString("NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                data.add( new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER_FACTUR")));
                data.add( new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER_BC")));
                data.add( new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE") )));
                if (resultSet.getInt("CONFIRM") != 0) data.add( new SimpleStringProperty("Confirmé"));
                else data.add( new SimpleStringProperty("Non confirmé"));

                dataTable.add(data);
            }

            conn.close();

            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshGasoline(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTableGasoline.clear();
            LocalDate dateFrom = dpFrom.getValue();
            LocalDate dateTo = dpTo.getValue();

            String query = "";
            PreparedStatement preparedStmt;

            if (dateFrom != null && dateTo != null) {
                query = "SELECT RECHARGE_GASOLINE.ID, RECHARGE_GASOLINE.DATE, RECHARGE_GASOLINE.NUMBER_BC, RECHARGE_GASOLINE.DATE_BC,\n" +
                        "RECHARGE_GASOLINE.NUMBER_FACTUR, RECHARGE_GASOLINE.DATE_FACTUR, RECHARGE_GASOLINE.PRICE FROM RECHARGE_GASOLINE\n" +
                        "WHERE RECHARGE_GASOLINE.DATE BETWEEN ? AND ?;";
                preparedStmt = conn.prepareStatement(query);
                preparedStmt.setDate(1, Date.valueOf(dateFrom));
                preparedStmt.setDate(2, Date.valueOf(dateTo));
            }else {
                query = "SELECT RECHARGE_GASOLINE.ID, RECHARGE_GASOLINE.DATE, RECHARGE_GASOLINE.NUMBER_BC, RECHARGE_GASOLINE.DATE_BC,\n" +
                        "RECHARGE_GASOLINE.NUMBER_FACTUR, RECHARGE_GASOLINE.DATE_FACTUR, RECHARGE_GASOLINE.PRICE FROM RECHARGE_GASOLINE\n";

                preparedStmt = conn.prepareStatement(query);
            }
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER_BC")));
                data.add( new SimpleStringProperty(resultSet.getDate("DATE_BC").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER_FACTUR")));
                data.add( new SimpleStringProperty(resultSet.getDate("DATE_FACTUR").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE") )));

                dataTableGasoline.add(data);
            }

            conn.close();

            tableGasoline.setItems(dataTableGasoline);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refreshInput();
    }

    private void clearRecherche(){
        tfRecherche.clear();
        dpFrom.setValue(null);
        dpTo.setValue(null);
    }

    @FXML
    void ActionSearch() {

        // filtrer les données
        ObservableList<List<StringProperty>> items = table.getItems();
        FilteredList<List<StringProperty>> filteredData = new FilteredList<>(items, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super List<StringProperty>>) stringProperties -> {
            if (txtRecherche.isEmpty()) {
                ActionRefresh();
                return true;
            } else if (stringProperties.get(1).toString().contains(txtRecherche)) {
                return true;
            } else if (stringProperties.get(2).toString().contains(txtRecherche)) {
                return true;
            }else if (stringProperties.get(3).toString().contains(txtRecherche)) {
                return true;
            } else if (stringProperties.get(4).toString().contains(txtRecherche)) {
                return true;
            }  else return stringProperties.get(5).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }


}
