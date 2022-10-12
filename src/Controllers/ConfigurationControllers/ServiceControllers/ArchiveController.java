package Controllers.ConfigurationControllers.ServiceControllers;

import BddPackage.ServiceOperation;
import Models.Service;
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
    TableView<Service> table;
    @FXML
    TableColumn<Service,String> clId,clName,clDep;


    private final ServiceOperation serviceOperation = new ServiceOperation();
    private final ObservableList<Service> serviceDataTable = FXCollections.observableArrayList();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clDep.setCellValueFactory(new PropertyValueFactory<>("nameDep"));

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
        Service service = table.getSelectionModel().getSelectedItem();

        if (service != null){

            serviceOperation.DeleteFromArchive(service);
            ActionAnnuler();

        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("ATTENTION ");
            alertWarning.setContentText("Veuillez sélectionner un department à désarchiver");
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
        ArrayList<Service> services = serviceOperation.getAllArchive();
        serviceDataTable.setAll(services);
        table.setItems(serviceDataTable);
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<Service> services = table.getItems();
        FilteredList<Service> filteredData = new FilteredList<>(services, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super Service>) service -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (service.getName().contains(txtRecherche)) {
                return true;
            } else if (service.getNameDep().contains(txtRecherche)) {
                return true;
            }else return  (String.valueOf(service.getId()).contains(txtRecherche)) ;
        });

        SortedList<Service> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);

    }
}
