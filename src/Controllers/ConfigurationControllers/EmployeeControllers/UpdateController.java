package Controllers.ConfigurationControllers.EmployeeControllers;

import BddPackage.DepartmentOperation;
import BddPackage.EmployeeOperation;
import BddPackage.ServiceOperation;
import Controllers.ConfigurationControllers.ServiceControllers.AddController;
import Models.Department;
import Models.Employee;
import Models.Service;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    TextField tfFirstName,tfLastName,tfFunction;
    @FXML
    Label lbAlert;
    @FXML
    ComboBox<String> cbDep,cbServ;
    @FXML
    Button btnInsert;

    private final EmployeeOperation operation = new EmployeeOperation();
    private final DepartmentOperation departmentOperation = new DepartmentOperation();
    private final ServiceOperation serviceOperation = new ServiceOperation();
    private final ObservableList<String> comboDepartmentData = FXCollections.observableArrayList();
    private final ObservableList<String> comboServiceData = FXCollections.observableArrayList();
    ArrayList<Department> departments;
    ArrayList<Service> services;
    private Employee employeeSelected;
    private Department departmentSelected;
    private Service serviceSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        refreshComboDepartment();

    }

    public  void InitUpdate(Employee employee){
        this.employeeSelected = employee;

        this.serviceSelected = serviceOperation.get(employee.getIdService());
        this.departmentSelected = departmentOperation.get(this.serviceSelected.getIdDep());

        tfFirstName.setText(employee.getFirstName());
        tfLastName.setText(employee.getLastName());
        tfFunction.setText(employee.getFunction());

        cbDep.getSelectionModel().select(this.departmentSelected.getName());
        cbServ.getSelectionModel().select(this.serviceSelected.getName());

    }

    private void refreshComboDepartment() {
        comboDepartmentData.clear();
        try {

            this.departments = departmentOperation.getAll();
            for (Department department : departments){
                comboDepartmentData.add(department.getName());
            }

            cbDep.setItems(comboDepartmentData);
            cbDep.getSelectionModel().select(0);
            ActionComboDepartment();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionComboDepartment(){
        try {
            int index = cbDep.getSelectionModel().getSelectedIndex();
            if (index != -1){
                int id = departments.get(index).getId();
                refreshComboService(id);
                cbServ.getSelectionModel().select(0);
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshComboService(int idDep) {
        comboServiceData.clear();
        try {

            this.services = serviceOperation.getAllByDepartment(idDep);
            for (Service service : services){
                comboServiceData.add(service.getName());
            }

            cbServ.setItems(comboServiceData);
            cbServ.getSelectionModel().select(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAddDepartment(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/DepartmentViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.btnInsert.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshComboDepartment();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAddService(){
        try {
            int index = cbDep.getSelectionModel().getSelectedIndex();
            if (index != -1){

                Department department = departments.get(index);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/ServiceViews/AddView.fxml"));
                DialogPane temp = loader.load();
                AddController controller = loader.getController();
                controller.Init(department);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(temp);
                dialog.resizableProperty().setValue(false);
                dialog.initOwner(this.btnInsert.getScene().getWindow());
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                closeButton.setVisible(false);
                dialog.showAndWait();


                refreshComboService(department.getId());
                cbServ.getSelectionModel().select(0);

            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAnnul(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        String firstName = tfFirstName.getText().trim();
        String lastName = tfLastName.getText().trim();
        String function = tfFunction.getText().trim();

        int depIndex = cbDep.getSelectionModel().getSelectedIndex();
        int servIndex = cbServ.getSelectionModel().getSelectedIndex();

        if ( !firstName.isEmpty() && !lastName.isEmpty() && !function.isEmpty() &&  depIndex != -1 &&  servIndex != -1){

            Employee employee = new Employee();
            employee.setFirstName(firstName);
            employee.setLastName(lastName);
            employee.setFunction(function);
            employee.setIdService(services.get(servIndex).getId());

            boolean update = update(employee);
            if (update){
                closeDialog(btnInsert);
            }else {
                labelAlert("ERREUR INCONNUE");
            }

        }else {
        labelAlert("Merci de remplir tous les champs");
        }

    }

    private void labelAlert(String st){

        try {

            lbAlert.setText(st);
            FadeTransition ft = new FadeTransition(Duration.millis(4000), lbAlert);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean update(Employee employee) {
        boolean update = false;
        try {
            update = operation.update(employee,this.employeeSelected);
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
