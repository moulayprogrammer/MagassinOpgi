package Controllers.ConfigurationControllers.EmployeeControllers;

import BddPackage.ConnectBD;
import BddPackage.EmployeeOperation;
import Models.Employee;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ArchiveController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clName,clDept,clServ,clFunction;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final EmployeeOperation operation = new EmployeeOperation();

    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clName.setCellValueFactory(data -> data.getValue().get(1));
        clDept.setCellValueFactory(data -> data.getValue().get(2));
        clServ.setCellValueFactory(data -> data.getValue().get(3));
        clFunction.setCellValueFactory(data -> data.getValue().get(4));

        refresh();
    }

    @FXML
    private void tableClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionDeleteFromArchive();
        }
    }

    @FXML
    private void ActionDeleteFromArchive(){
        List<StringProperty> data = table.getSelectionModel().getSelectedItem();

        if (data != null){

            try {
                Employee employee = operation.getArchive(Integer.parseInt(data.get(0).getValue()));

                operation.DeleteFromArchive(employee);
                ActionAnnuler();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("ATTENTION ");
            alertWarning.setContentText("Veuillez sélectionner un employee à désarchiver");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("D'ACCORD");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionAnnuler(){
        ((Stage)tfRecherche.getScene().getWindow()).close();
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refresh();
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    private void refresh(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();

            String query = "SELECT EMPLOYEE.ID, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME, DEP.NAME AS DEP_NAME, SERVICE.NAME AS SERV_NAME, EMPLOYEE.FUNCTION \n" +
                    "FROM EMPLOYEE,SERVICE,DEP WHERE EMPLOYEE.ARCHIVE = 1 AND EMPLOYEE.ID_SERVICE = SERVICE.ID AND SERVICE.ID_DEP = DEP.ID;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME") + "  "+ resultSet.getString("LAST_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("DEP_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("SERV_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("FUNCTION")));

                dataTable.add(data);
            }

            conn.close();

            table.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
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
            }  else return stringProperties.get(3).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }
}
