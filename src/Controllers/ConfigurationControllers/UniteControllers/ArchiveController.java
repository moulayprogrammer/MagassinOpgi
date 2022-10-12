package Controllers.ConfigurationControllers.UniteControllers;

import BddPackage.CategoryOperation;
import BddPackage.UnitOperation;
import Models.Category;
import Models.Unit;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ArchiveController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<Unit> table;
    @FXML
    TableColumn<Unit,String> clId,clName;

    private final UnitOperation operation = new UnitOperation();
    private final ObservableList<Unit> dataTable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));

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
        Unit unit = table.getSelectionModel().getSelectedItem();

        if (unit != null){

            operation.DeleteFromArchive(unit);
            ActionAnnuler();

        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("ATTENTION ");
            alertWarning.setContentText("Veuillez sélectionner un Category à désarchiver");
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
        ArrayList<Unit> units = operation.getAllArchive();
        dataTable.setAll(units);
        table.setItems(dataTable);
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<Unit> units = table.getItems();
        FilteredList<Unit> filteredData = new FilteredList<>(units, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super Unit>) unit -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (unit.getName().contains(txtRecherche)) {
                return true;
            } else return  (String.valueOf(unit.getId()).contains(txtRecherche)) ;
        });

        SortedList<Unit> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }
}
