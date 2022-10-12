package Controllers.OutputControllers;

import BddPackage.*;
import Models.*;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

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
    TextField tfRecherche;
    @FXML
    Label lbDep,lbServ;
    @FXML
    ComboBox<String> cbFilter,cbDep,cbServ;
    @FXML
    DatePicker dpFrom,dpTo;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clNumBS,clDateBS,clName,clDep,clServ,clMontant;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final OutputOperation operation = new OutputOperation();
    private final ComponentOutputOperation componentOutputOperation = new ComponentOutputOperation();
    private final StoreCardOperation storeCardOperation = new StoreCardOperation();
    private final DepartmentOperation departmentOperation = new DepartmentOperation();
    private final ServiceOperation serviceOperation = new ServiceOperation();

    private final ObservableList<String> comboDepData = FXCollections.observableArrayList();
    private final ObservableList<String> comboServData = FXCollections.observableArrayList();
    private final ObservableList<String> comboFilterData = FXCollections.observableArrayList();

    private ArrayList<Department> departments;
    private ArrayList<Service> services;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clNumBS.setCellValueFactory(data -> data.getValue().get(1));
        clDateBS.setCellValueFactory(data -> data.getValue().get(2));
        clName.setCellValueFactory(data -> data.getValue().get(3));
        clDep.setCellValueFactory(data -> data.getValue().get(4));
        clServ.setCellValueFactory(data -> data.getValue().get(5));
        clMontant.setCellValueFactory(data -> data.getValue().get(6));

        refresh();
        refreshComboDepartment();
        refreshComboServices();

        comboFilterData.addAll("Tout","Department","Service");
        cbFilter.setItems(comboFilterData);
        cbFilter.getSelectionModel().select(0);
    }

    private void refreshComboDepartment() {
        comboDepData.clear();
        try {

            this.departments = departmentOperation.getAll();
            for (Department department: departments){
                comboDepData.add(department.getName());
            }

            cbDep.setItems(comboDepData);
            cbDep.getSelectionModel().select(0);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshComboServices() {
        comboServData.clear();
        try {

            this.services = serviceOperation.getAll();
            for (Service service: this.services){
                comboServData.add(service.getName());
            }

            cbServ.setItems(comboServData);
            cbServ.getSelectionModel().select(0);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAdd(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/AddView.fxml"));
            BorderPane temp = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(temp));
            stage.setMaximized(true);
            stage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
            stage.getIcons().add(new Image("/Images/logo.png"));
            stage.initOwner(this.tfRecherche.getScene().getWindow());
            stage.showAndWait();

            refresh();
        } catch (Exception e) {
            e.printStackTrace();
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

        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){

            try {
                Output output = operation.get(Integer.parseInt(data.get(0).getValue()));

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/UpdateView.fxml"));
                BorderPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.Init(output);
                Stage stage = new Stage();
                stage.setScene(new Scene(temp));
                stage.setMaximized(true);
                stage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
                stage.getIcons().add(new Image("/Images/logo.png"));
                stage.initOwner(this.tfRecherche.getScene().getWindow());
                stage.showAndWait();

                refresh();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("ATTENTION");
            alertWarning.setContentText("Veuillez sélectionner une bon de recéption à modifier");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("D'ACCORD");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionDelete(){

        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
        if (data != null){
            try {
                Output output = operation.get(Integer.parseInt(data.get(0).getValue()));

                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                alertConfirmation.setHeaderText("supprimer le bonne sortée");
                alertConfirmation.setContentText("Voulez-vous vraiment supprimer le bonne?");
                alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");

                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                cancel.setText("Annulation");

                alertConfirmation.showAndWait().ifPresent(response -> {
                    if (response == ButtonType.CANCEL) {
                        alertConfirmation.close();
                    } else if (response == ButtonType.OK) {

                        ArrayList<ComponentOutput> componentOutputs = componentOutputOperation.getAllByOutput(output.getId());
                        for (ComponentOutput componentOutput : componentOutputs){

                            StoreCard storeCard = storeCardOperation.get(componentOutput.getIdStore());
                            storeCard.setQteConsumed(componentOutput.getQteServ());

                            storeCardOperation.subQteConsumed(storeCard);
                            componentOutputOperation.delete(componentOutput);
                        }
                        operation.delete(output);

                        refresh();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("Attention ");
            alertWarning.setContentText("Veuillez sélectionner une bonne à supprimer");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("D'ACCORD");
            alertWarning.showAndWait();
        }
    }

    private void refresh(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();

            String query = "SELECT OUTPUT.ID, OUTPUT.NUMBER, OUTPUT.DATE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SERVICE.NAME AS NAME_SERV , DEP.NAME AS NAME_DEP,\n" +
                    "(SELECT SUM(COMPONENT_OUTPUT.QTE_SERV * STORE_CARD.PRICE) FROM COMPONENT_OUTPUT,STORE_CARD \n" +
                    "WHERE COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID) AS MONTANT \n" +
                    "FROM OUTPUT,EMPLOYEE,SERVICE,DEP WHERE OUTPUT.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND SERVICE.ID_DEP = DEP.ID;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME") + "   " + resultSet.getString("LAST_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("NAME_DEP")));
                data.add( new SimpleStringProperty(resultSet.getString("NAME_SERV")));
                data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("MONTANT"))));

                dataTable.add(data);
            }
            conn.close();

            table.setItems(dataTable);
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

                cbServ.setVisible(false);
                cbServ.setManaged(false);
                lbServ.setVisible(false);
                lbServ.setManaged(false);

                cbDep.setVisible(false);
                cbDep.setManaged(false);
                lbDep.setVisible(false);
                lbDep.setManaged(false);

                switch (select) {
                    case 0:
                        if (dateFrom != null && dateTo != null) ActionSearchDate();
                        else refresh();

                        break;
                    case 1:
                        cbDep.setVisible(true);
                        cbDep.setManaged(true);
                        lbDep.setVisible(true);
                        lbDep.setManaged(true);
                        refreshComboDepartment();
                        comboFilterDepartmentAction();

                        break;
                    case 2:
                        cbServ.setVisible(true);
                        cbServ.setManaged(true);
                        lbServ.setVisible(true);
                        lbServ.setManaged(true);
                        refreshComboServices();
                        comboFilterServicesAction();
                        break;
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void comboFilterDepartmentAction() {
        try {
            int select  = cbDep.getSelectionModel().getSelectedIndex();
            LocalDate dateFrom = dpFrom.getValue();
            LocalDate dateTo = dpTo.getValue();

            if (select != -1 ) {

                if (conn.isClosed()) conn = connectBD.connect();
                dataTable.clear();

                String query = "";
                PreparedStatement preparedStmt;

                if (dateFrom != null && dateTo != null) {
                    query = "SELECT OUTPUT.ID, OUTPUT.NUMBER, OUTPUT.DATE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SERVICE.NAME AS NAME_SERV , DEP.NAME AS NAME_DEP,\n" +
                            "(SELECT SUM(COMPONENT_OUTPUT.QTE_SERV * STORE_CARD.PRICE) FROM COMPONENT_OUTPUT,STORE_CARD \n" +
                            "WHERE COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID) AS MONTANT \n" +
                            "FROM OUTPUT,EMPLOYEE,SERVICE,DEP WHERE DEP.ID = ? AND OUTPUT.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = SERVICE.ID \n" +
                            "AND SERVICE.ID_DEP = DEP.ID AND OUTPUT.DATE BETWEEN ? AND ?;";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,departments.get(select).getId());
                    preparedStmt.setDate(2, Date.valueOf(dateFrom));
                    preparedStmt.setDate(3, Date.valueOf(dateTo));
                }else {
                    query = "SELECT OUTPUT.ID, OUTPUT.NUMBER, OUTPUT.DATE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SERVICE.NAME AS NAME_SERV , DEP.NAME AS NAME_DEP,\n" +
                            "(SELECT SUM(COMPONENT_OUTPUT.QTE_SERV * STORE_CARD.PRICE) FROM COMPONENT_OUTPUT,STORE_CARD \n" +
                            "WHERE COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID) AS MONTANT \n" +
                            "FROM OUTPUT,EMPLOYEE,SERVICE,DEP WHERE DEP.ID = ? AND OUTPUT.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = SERVICE.ID \n" +
                            "AND SERVICE.ID_DEP = DEP.ID;;";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,departments.get(select).getId());
                }

                ResultSet resultSet = preparedStmt.executeQuery();

                while (resultSet.next()) {

                    List<StringProperty> data = new ArrayList<>();

                    data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                    data.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                    data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                    data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME") + "   " + resultSet.getString("LAST_NAME")));
                    data.add( new SimpleStringProperty(resultSet.getString("NAME_DEP")));
                    data.add( new SimpleStringProperty(resultSet.getString("NAME_SERV")));
                    data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("MONTANT"))));

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
    private void comboFilterServicesAction() {
        try {
            int select  = cbServ.getSelectionModel().getSelectedIndex();
            LocalDate dateFrom = dpFrom.getValue();
            LocalDate dateTo = dpTo.getValue();

            if (select != -1 ) {

                if (conn.isClosed()) conn = connectBD.connect();
                dataTable.clear();

                String query = "";
                PreparedStatement preparedStmt;

                if (dateFrom != null && dateTo != null) {
                    query = "SELECT OUTPUT.ID, OUTPUT.NUMBER, OUTPUT.DATE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SERVICE.NAME AS NAME_SERV , DEP.NAME AS NAME_DEP,\n" +
                            "(SELECT SUM(COMPONENT_OUTPUT.QTE_SERV * STORE_CARD.PRICE) FROM COMPONENT_OUTPUT,STORE_CARD \n" +
                            "WHERE COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID) AS MONTANT \n" +
                            "FROM OUTPUT,EMPLOYEE,SERVICE,DEP WHERE SERVICE.ID = ? AND OUTPUT.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = SERVICE.ID \n" +
                            "AND SERVICE.ID_DEP = DEP.ID AND OUTPUT.DATE BETWEEN ? AND ? ;";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,services.get(select).getId());
                    preparedStmt.setDate(2, Date.valueOf(dateFrom));
                    preparedStmt.setDate(3, Date.valueOf(dateTo));
                }else {
                    query = "SELECT OUTPUT.ID, OUTPUT.NUMBER, OUTPUT.DATE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SERVICE.NAME AS NAME_SERV , DEP.NAME AS NAME_DEP,\n" +
                            "(SELECT SUM(COMPONENT_OUTPUT.QTE_SERV * STORE_CARD.PRICE) FROM COMPONENT_OUTPUT,STORE_CARD \n" +
                            "WHERE COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID) AS MONTANT \n" +
                            "FROM OUTPUT,EMPLOYEE,SERVICE,DEP WHERE SERVICE.ID = ? AND OUTPUT.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = SERVICE.ID \n" +
                            "AND SERVICE.ID_DEP = DEP.ID;";
                    preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setInt(1,services.get(select).getId());
                }

                ResultSet resultSet = preparedStmt.executeQuery();

                while (resultSet.next()) {

                    List<StringProperty> data = new ArrayList<>();

                    data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                    data.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                    data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                    data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME") + "   " + resultSet.getString("LAST_NAME")));
                    data.add( new SimpleStringProperty(resultSet.getString("NAME_DEP")));
                    data.add( new SimpleStringProperty(resultSet.getString("NAME_SERV")));
                    data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("MONTANT"))));

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

                    String query = "SELECT OUTPUT.ID, OUTPUT.NUMBER, OUTPUT.DATE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, SERVICE.NAME AS NAME_SERV , DEP.NAME AS NAME_DEP,\n" +
                            "(SELECT SUM(COMPONENT_OUTPUT.QTE_SERV * STORE_CARD.PRICE) FROM COMPONENT_OUTPUT,STORE_CARD \n" +
                            "WHERE COMPONENT_OUTPUT.ID_OUTPUT = OUTPUT.ID AND COMPONENT_OUTPUT.ID_STORE = STORE_CARD.ID) AS MONTANT \n" +
                            "FROM OUTPUT,EMPLOYEE,SERVICE,DEP WHERE OUTPUT.ID_EMP = EMPLOYEE.ID AND EMPLOYEE.ID_SERVICE = SERVICE.ID \n" +
                            "AND SERVICE.ID_DEP = DEP.ID AND OUTPUT.DATE BETWEEN ? AND ? ;";
                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    preparedStmt.setDate(1, Date.valueOf(dateFrom));
                    preparedStmt.setDate(2, Date.valueOf(dateTo));
                    ResultSet resultSet = preparedStmt.executeQuery();
                    while (resultSet.next()) {

                        List<StringProperty> data = new ArrayList<>();

                        data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                        data.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                        data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("MM-dd-yyyy"))));
                        data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME") + "   " + resultSet.getString("LAST_NAME")));
                        data.add( new SimpleStringProperty(resultSet.getString("NAME_DEP")));
                        data.add( new SimpleStringProperty(resultSet.getString("NAME_SERV")));
                        data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("MONTANT"))));

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
        clearRecherche();
        if (cbFilter.getSelectionModel().getSelectedIndex() != 0) comboFilterAction();
        else refresh();
    }
    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refresh();
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
                //loadDataInTable();
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
