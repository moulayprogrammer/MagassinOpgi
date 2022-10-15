package Controllers.ArticlesControllers.CarburantControllers;

import BddPackage.ConnectBD;
import BddPackage.GasolineCardOperation;
import Models.GasolineCard;
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
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class ArchiveController implements Initializable {

    @FXML
    TextField tfRecherche;
    @FXML
    TableView<List<StringProperty>> tableGasoline;
    @FXML
    TableColumn<List<StringProperty>,String> clIdCard,clNumberCard,clRechargeCard,clConsumedCard;
    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final GasolineCardOperation operation = new GasolineCardOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conn = connectBD.connect();

        clIdCard.setCellValueFactory(data -> data.getValue().get(0));
        clNumberCard.setCellValueFactory(data -> data.getValue().get(1));
        clRechargeCard.setCellValueFactory(data -> data.getValue().get(2));
        clConsumedCard.setCellValueFactory(data -> data.getValue().get(3));

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
        List<StringProperty> data = tableGasoline.getSelectionModel().getSelectedItem();

        if (data != null){

            try {
                GasolineCard gasolineCard = operation.get(Integer.parseInt(data.get(0).getValue()));

                operation.DeleteFromArchive(gasolineCard);
                ActionAnnuler();

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("ATTENTION ");
            alertWarning.setContentText("Veuillez sélectionner un article à désarchiver");
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

            String query = "SELECT * FROM GASOLINE_CARD WHERE ARCHIVE = 1;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                data.add( new SimpleStringProperty(resultSet.getDate("LAST_RECHARGE_DATE").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getDate("LAST_CONSUMPTION_DATE").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                dataTable.add(data);
            }

            conn.close();

            tableGasoline.setItems(dataTable);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<List<StringProperty>> items = tableGasoline.getItems();
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
        sortedList.comparatorProperty().bind(tableGasoline.comparatorProperty());
        tableGasoline.setItems(sortedList);

    }
}
