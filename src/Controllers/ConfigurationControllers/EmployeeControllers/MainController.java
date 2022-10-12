package Controllers.ConfigurationControllers.EmployeeControllers;


import BddPackage.ConnectBD;
import BddPackage.DepartmentOperation;
import BddPackage.EmployeeOperation;
import BddPackage.ServiceOperation;
import Models.Department;
import Models.Employee;
import Models.Service;
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
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    TabPane tabPane;
    @FXML
    Tab tabEmployee,tabDep,tabServ;
    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> tableEmployee;
    @FXML
    TableView<Department> tableDep;
    @FXML
    TableView<Service> tableServ;
    @FXML
    TableColumn<Service,String> clIdServ,clNameServ,clDepIdServ,clDepNameServ;
    @FXML
    TableColumn<Department,String> clIdDep,clNameDep;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clName,clLastName,clDept,clServ,clFunction;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final EmployeeOperation employeeOperation = new EmployeeOperation();
    private final DepartmentOperation departmentOperation = new DepartmentOperation();
    private final ServiceOperation serviceOperation = new ServiceOperation();

    private final ObservableList<Service> serviceDataTable = FXCollections.observableArrayList();
    private final ObservableList<Department> dataTableDep = FXCollections.observableArrayList();
    private final ObservableList<List<StringProperty>> dataTableEmployee = FXCollections.observableArrayList();
    private final ObservableList<String> comboOrderData = FXCollections.observableArrayList();


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clName.setCellValueFactory(data -> data.getValue().get(1));
        clLastName.setCellValueFactory(data -> data.getValue().get(2));
        clDept.setCellValueFactory(data -> data.getValue().get(3));
        clServ.setCellValueFactory(data -> data.getValue().get(4));
        clFunction.setCellValueFactory(data -> data.getValue().get(5));

        clIdDep.setCellValueFactory(new PropertyValueFactory<>("id"));
        clNameDep.setCellValueFactory(new PropertyValueFactory<>("name"));

        clIdServ.setCellValueFactory(new PropertyValueFactory<>("id"));
        clDepIdServ.setCellValueFactory(new PropertyValueFactory<>("idDep"));
        clNameServ.setCellValueFactory(new PropertyValueFactory<>("name"));
        clDepNameServ.setCellValueFactory(new PropertyValueFactory<>("nameDep"));

        refreshEmployee();

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            switch (newTab.getId()){
                case "tabEmployee":
                    refreshEmployee();
                    break;
                case "tabDep":
                    refreshDep();
                    break;
                case "tabServ":
                    refreshService();
                    break;
            }
        });
    }

    @FXML
    private void ActionAdd(){
        try {
            String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
            switch (tabId){
                case "tabEmployee":
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/EmployeeViews/AddView.fxml"));
                    DialogPane temp = loader.load();
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(temp);
                    dialog.resizableProperty().setValue(false);
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                    closeButton.setVisible(false);
                    dialog.showAndWait();

                    refreshEmployee();
                    break;
                case "tabDep":
                    loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/DepartmentViews/AddView.fxml"));
                    temp = loader.load();
                    dialog = new Dialog<>();
                    dialog.setDialogPane(temp);
                    dialog.resizableProperty().setValue(false);
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                    closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                    closeButton.setVisible(false);
                    dialog.showAndWait();

                    refreshDep();
                    break;
                case "tabServ":
                    loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/ServiceViews/AddView.fxml"));
                    temp = loader.load();
                    dialog = new Dialog<>();
                    dialog.setDialogPane(temp);
                    dialog.resizableProperty().setValue(false);
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                    closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                    closeButton.setVisible(false);
                    dialog.showAndWait();

                    refreshService();
                    break;
            }
        } catch (IOException e) {
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

        try {
            String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
            switch (tabId){
                case "tabEmployee":
                    List<StringProperty> dataEmployee = tableEmployee.getSelectionModel().getSelectedItem();

                    if (dataEmployee != null){
                        try {
                            Employee employee = employeeOperation.get(Integer.parseInt(dataEmployee.get(0).getValue()));

                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/EmployeeViews/UpdateView.fxml"));
                            DialogPane temp = loader.load();
                            UpdateController controller = loader.getController();
                            controller.InitUpdate(employee);
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setDialogPane(temp);
                            dialog.resizableProperty().setValue(false);
                            dialog.initOwner(this.tfRecherche.getScene().getWindow());
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                            closeButton.setVisible(false);
                            dialog.showAndWait();

                            refreshEmployee();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un élément à modifier");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                    break;
                case "tabDep":
                    Department department = tableDep.getSelectionModel().getSelectedItem();

                    if (department != null){

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/DepartmentViews/UpdateView.fxml"));
                            DialogPane temp = loader.load();
                            Controllers.ConfigurationControllers.DepartmentControllers.UpdateController controller = loader.getController();
                            controller.Init(department);
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setDialogPane(temp);
                            dialog.resizableProperty().setValue(false);
                            dialog.initOwner(this.tfRecherche.getScene().getWindow());
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                            closeButton.setVisible(false);
                            dialog.showAndWait();

                            refreshDep();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un élément à modifier");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                    break;
                case "tabServ":
                    Service service = tableServ.getSelectionModel().getSelectedItem();

                    if (service != null){

                        try {
                            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/ServiceViews/UpdateView.fxml"));
                            DialogPane temp = loader.load();
                            Controllers.ConfigurationControllers.ServiceControllers.UpdateController controller = loader.getController();
                            controller.Init(service);
                            Dialog<ButtonType> dialog = new Dialog<>();
                            dialog.setDialogPane(temp);
                            dialog.resizableProperty().setValue(false);
                            dialog.initOwner(this.tfRecherche.getScene().getWindow());
                            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                            closeButton.setVisible(false);
                            dialog.showAndWait();

                            refreshService();

                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un élément à modifier");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAddToArchive(){

        try {
            String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
            switch (tabId){
                case "tabEmployee":
                    List<StringProperty> data = tableEmployee.getSelectionModel().getSelectedItem();

                    if (data != null){

                        Employee employee = employeeOperation.get(Integer.parseInt(data.get(0).getValue()));

                        try {

                            Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                            alertConfirmation.setHeaderText("CONFIRMATION D'ARCHIVAGE");
                            alertConfirmation.setContentText("Êtes-vous sûr d'avoir archivé l'employee ?");
                            alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");

                            Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                            cancel.setText("ANNULATION");

                            alertConfirmation.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.CANCEL) {
                                    alertConfirmation.close();
                                } else if (response == ButtonType.OK) {
                                    employeeOperation.AddToArchive(employee);
                                    refreshEmployee();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION ");
                        alertWarning.setContentText("Veuillez sélectionner un employee à archiver");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                    break;
                case "tabDep":
                    Department department = tableDep.getSelectionModel().getSelectedItem();
                    if (department != null){

                        try {
                            Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                            alertConfirmation.setHeaderText("CONFIRMATION D'ARCHIVAGE");
                            alertConfirmation.setContentText("Êtes-vous sûr d'avoir archivé le department ?");
                            alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");

                            Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                            cancel.setText("ANNULATION");

                            alertConfirmation.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.CANCEL) {
                                    alertConfirmation.close();
                                } else if (response == ButtonType.OK) {
                                    departmentOperation.AddToArchive(department);
                                    refreshDep();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION ");
                        alertWarning.setContentText("Veuillez sélectionner un department à archiver");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                    break;
                case "tabServ":
                    Service service = tableServ.getSelectionModel().getSelectedItem();
                    if (service != null){

                        try {
                            Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                            alertConfirmation.setHeaderText("CONFIRMATION D'ARCHIVAGE");
                            alertConfirmation.setContentText("Êtes-vous sûr d'avoir archivé le service ?");
                            alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");

                            Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                            cancel.setText("ANNULATION");

                            alertConfirmation.showAndWait().ifPresent(response -> {
                                if (response == ButtonType.CANCEL) {
                                    alertConfirmation.close();
                                } else if (response == ButtonType.OK) {
                                    serviceOperation.AddToArchive(service);
                                    refreshService();
                                }
                            });

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION ");
                        alertWarning.setContentText("Veuillez sélectionner un service à archiver");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionDeleteFromArchive(){

        try {
            String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
            switch (tabId){
                case "tabEmployee":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/EmployeeViews/ArchiveView.fxml"));
                        DialogPane temp = loader.load();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane(temp);
                        dialog.resizableProperty().setValue(false);
                        dialog.initOwner(this.tfRecherche.getScene().getWindow());
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                        closeButton.setVisible(false);
                        dialog.showAndWait();

                        refreshEmployee();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "tabDep":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/DepartmentViews/ArchiveView.fxml"));
                        DialogPane temp = loader.load();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane(temp);
                        dialog.resizableProperty().setValue(false);
                        dialog.initOwner(this.tfRecherche.getScene().getWindow());
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                        closeButton.setVisible(false);
                        dialog.showAndWait();

                        refreshDep();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
                case "tabServ":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/ServiceViews/ArchiveView.fxml"));
                        DialogPane temp = loader.load();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane(temp);
                        dialog.resizableProperty().setValue(false);
                        dialog.initOwner(this.tfRecherche.getScene().getWindow());
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                        closeButton.setVisible(false);
                        dialog.showAndWait();

                        refreshService();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshEmployee(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTableEmployee.clear();

            String query = "SELECT EMPLOYEE.ID, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, DEP.NAME AS DEP_NAME, SERVICE.NAME AS SERV_NAME, EMPLOYEE.FUNCTION \n" +
                    "FROM EMPLOYEE,SERVICE,DEP WHERE EMPLOYEE.ARCHIVE = 0 AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND SERVICE.ID_DEP = DEP.ID;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("LAST_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("DEP_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("SERV_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("FUNCTION")));

                dataTableEmployee.add(data);
            }

            conn.close();

            tableEmployee.setItems(dataTableEmployee);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshDep(){
        ArrayList<Department> departments = departmentOperation.getAll();
        dataTableDep.setAll(departments);
        tableDep.setItems(dataTableDep);
    }

    private void refreshService(){
        ArrayList<Service> services = serviceOperation.getAll();
        serviceDataTable.setAll(services);
        tableServ.setItems(serviceDataTable);
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refreshEmployee();
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    @FXML
    void ActionSearch() {

        // filtrer les données
        ObservableList<List<StringProperty>> items = tableEmployee.getItems();
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
        sortedList.comparatorProperty().bind(tableEmployee.comparatorProperty());
        tableEmployee.setItems(sortedList);
    }
}
