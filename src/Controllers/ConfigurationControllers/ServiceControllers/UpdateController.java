package Controllers.ConfigurationControllers.ServiceControllers;

import BddPackage.DepartmentOperation;
import BddPackage.ServiceOperation;
import Models.Department;
import Models.Service;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    TextField tfName;
    @FXML
    ComboBox<String> cbDepartment;
    @FXML
    Label lbAlert;
    @FXML
    Button btnUpdate;

    private final ServiceOperation operation = new ServiceOperation();
    private final DepartmentOperation departmentOperation = new DepartmentOperation();

    private final ObservableList<String> comboDepData = FXCollections.observableArrayList();
    private ArrayList<Department> departments = new ArrayList<>();
    private Department selectedDepartment;
    private Service selectedService;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        refreshComboEmployee();
    }

    private void refreshComboEmployee() {
        comboDepData.clear();
        try {
            departments = departmentOperation.getAll();

            for (Department department : departments){
                comboDepData.add(department.getName());
            }

            cbDepartment.setItems(comboDepData);
            cbDepartment.getSelectionModel().select(0);
            ActionComboDepartment();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void Init(Service service){
        this.selectedService = service;
        tfName.setText(service.getName());
        selectedDepartment = departmentOperation.get(service.getIdDep());
        cbDepartment.getSelectionModel().select(selectedDepartment.getName());
    }

    @FXML
    private void ActionComboDepartment(){
        int index = cbDepartment.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            selectedDepartment = departments.get(index);
            System.out.println("id dep = " + selectedDepartment.getId() + " name = " + selectedDepartment.getName());
        }
    }

    @FXML
    private void ActionAnnulled(){
        closeDialog(btnUpdate);
    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        String name = tfName.getText().trim();
        if (!name.isEmpty() && selectedDepartment.getName() != null ){

            Service service = new Service(selectedDepartment.getId(),name);

            boolean ins = update(service);
            if (ins ) {

                closeDialog(btnUpdate);
            }
            else {
                labelAlert("ERREUR INCONNUE");
            }
        }else {
            labelAlert("Merci de remplir tous les champs");
        }

    }

    private void labelAlert(String st){

        try {

            lbAlert.setText(st);
            FadeTransition ft = new FadeTransition(Duration.millis(3000), lbAlert);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean update(Service service) {
        boolean insert = false;
        try {
            insert = operation.update(service,this.selectedService);
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
