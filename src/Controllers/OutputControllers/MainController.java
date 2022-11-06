package Controllers.OutputControllers;

import BddPackage.*;
import Controllers.OutputControllers.OutputArticlesControllers.Print;
import Controllers.OutputControllers.OutputArticlesControllers.UpdateController;
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
import javafx.scene.Node;
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
    TabPane tabPane;
    @FXML
    TextField tfRecherche;
    @FXML
    Label lbDep,lbServ;
    @FXML
    ComboBox<String> cbFilter,cbDep,cbServ;
    @FXML
    DatePicker dpFrom,dpTo;
    @FXML
    TableView<List<StringProperty>> table,tableDecharge,tableCarteGasoline;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clNumBS,clDateBS,clName,clDep,clServ,clMontant;
    @FXML
    TableColumn<List<StringProperty>,String> clIdDecharge,clDateDecharge,clNameDecharge ,clNameDechargeur,clDepDecharge,clServDecharge;
    @FXML
    TableColumn<List<StringProperty>,String> clIdBR,clNumBR,clDateBR ,clEmployee,clNumCN,clNumBN,clMontantBRG;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTableOutput = FXCollections.observableArrayList();
    private final ObservableList<List<StringProperty>> dataTableDecharge = FXCollections.observableArrayList();
    private final ObservableList<List<StringProperty>> dataTableGasolineCard = FXCollections.observableArrayList();
    private final OutputOperation outputOperation = new OutputOperation();
    private final DechargeOperation dechargeOperation = new DechargeOperation();
    private final RechargeGasolineCardOperation rechargeGasolineCardOperation = new RechargeGasolineCardOperation();

    private final ComponentOutputOperation componentOutputOperation = new ComponentOutputOperation();
    private final ComponentDechargeOperation componentDechargeOperation = new ComponentDechargeOperation();
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

        clIdDecharge.setCellValueFactory(data -> data.getValue().get(0));
        clDateDecharge.setCellValueFactory(data -> data.getValue().get(1));
        clNameDecharge.setCellValueFactory(data -> data.getValue().get(2));
        clNameDechargeur.setCellValueFactory(data -> data.getValue().get(3));
        clServDecharge.setCellValueFactory(data -> data.getValue().get(4));
        clDepDecharge.setCellValueFactory(data -> data.getValue().get(5));

        clIdBR.setCellValueFactory(data -> data.getValue().get(0));
        clNumBR.setCellValueFactory(data -> data.getValue().get(1));
        clDateBR.setCellValueFactory(data -> data.getValue().get(2));
        clEmployee.setCellValueFactory(data -> data.getValue().get(3));
        clNumCN.setCellValueFactory(data -> data.getValue().get(4));
        clNumBN.setCellValueFactory(data -> data.getValue().get(5));
        clMontantBRG.setCellValueFactory(data -> data.getValue().get(6));


        refreshSortie();
        refreshComboDepartment();
        refreshComboServices();

        comboFilterData.addAll("Tout","Department","Service");
        cbFilter.setItems(comboFilterData);
        cbFilter.getSelectionModel().select(0);

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            switch (newTab.getId()){
                case "tabSortie":
                    refreshSortie();
                    break;
                case "tabDecharge":
                    refreshDecharge();
                    break;
                case "tabCarteGasoline":
                    refreshCarteGasoline();
                    break;
            }
        });
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
            String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
            switch (tabId){
                case "tabSortie":
                    try {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/ArticlesOutputViews/AddView.fxml"));
                        BorderPane temp = loader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(temp));
                        stage.setMaximized(true);
                        stage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
                        stage.getIcons().add(new Image("/Images/logo.png"));
                        stage.initOwner(this.tfRecherche.getScene().getWindow());
                        stage.showAndWait();

                        refreshSortie();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tabDecharge":
                    try {

                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/DechargeViews/AddView.fxml"));
                        BorderPane temp = loader.load();
                        Stage stage = new Stage();
                        stage.setScene(new Scene(temp));
                        stage.setMaximized(true);
                        stage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
                        stage.getIcons().add(new Image("/Images/logo.png"));
                        stage.initOwner(this.tfRecherche.getScene().getWindow());
                        stage.showAndWait();

                        refreshDecharge();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                    break;
                case "tabCarteGasoline":
                    try {
                        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/RechargeGasolineCardViews/AddView.fxml"));
                        DialogPane temp = loader.load();
                        Dialog<ButtonType> dialog = new Dialog<>();
                        dialog.setDialogPane(temp);
                        dialog.resizableProperty().setValue(false);
                        dialog.initOwner(this.tfRecherche.getScene().getWindow());
                        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                        Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                        closeButton.setVisible(false);
                        dialog.showAndWait();

                        refreshCarteGasoline();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }catch (Exception e){
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
                case "tabSortie":
                    try {
                        List<StringProperty> data = table.getSelectionModel().getSelectedItem();
                        if (data != null) {

                            try {
                                Output output = outputOperation.get(Integer.parseInt(data.get(0).getValue()));

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/ArticlesOutputViews/UpdateView.fxml"));
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

                                refreshSortie();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("ATTENTION");
                            alertWarning.setContentText("Veuillez sélectionner une bon de recéption à modifier");
                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "tabDecharge":
                    try {

                        List<StringProperty> data = tableDecharge.getSelectionModel().getSelectedItem();
                        if (data != null) {

                            try {
                                Decharge decharge = dechargeOperation.get(Integer.parseInt(data.get(0).getValue()));

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/DechargeViews/UpdateView.fxml"));
                                BorderPane temp = loader.load();
                                Controllers.OutputControllers.DechargeControllers.UpdateController controller = loader.getController();
                                controller.Init(decharge);
                                Stage stage = new Stage();
                                stage.setScene(new Scene(temp));
                                stage.setMaximized(true);
                                stage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
                                stage.getIcons().add(new Image("/Images/logo.png"));
                                stage.initOwner(this.tfRecherche.getScene().getWindow());
                                stage.showAndWait();

                                refreshDecharge();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("ATTENTION");
                            alertWarning.setContentText("Veuillez sélectionner un décharge à modifier");
                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                    break;
                case "tabCarteGasoline":
                    try {
                        List<StringProperty> data = tableCarteGasoline.getSelectionModel().getSelectedItem();
                        if (data != null) {

                            try {
                                RechargeGasolineCard gasolineCard = rechargeGasolineCardOperation.get(Integer.parseInt(data.get(0).getValue()));

                                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/RechargeGasolineCardViews/UpdateView.fxml"));
                                DialogPane temp = loader.load();
                                Controllers.OutputControllers.RechargeCarteGasolineControllers.UpdateController controller =  loader.getController();
                                controller.Init(gasolineCard);
                                Dialog<ButtonType> dialog = new Dialog<>();
                                dialog.setDialogPane(temp);
                                dialog.resizableProperty().setValue(false);
                                dialog.initOwner(this.tfRecherche.getScene().getWindow());
                                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                                Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                                closeButton.setVisible(false);
                                dialog.showAndWait();

                                refreshCarteGasoline();

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("ATTENTION");
                            alertWarning.setContentText("Veuillez sélectionner une bon de recéption à modifier");
                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    private void ActionDelete(){
        try {
            String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
            switch (tabId){
                case "tabSortie":
                    try {
                        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
                        if (data != null){
                            try {
                                Output output = outputOperation.get(Integer.parseInt(data.get(0).getValue()));

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
                                        outputOperation.delete(output);

                                        refreshSortie();
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
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
                case "tabDecharge":
                    try {

                        List<StringProperty> data = tableDecharge.getSelectionModel().getSelectedItem();
                        if (data != null) {

                            try {
                                Decharge decharge = dechargeOperation.get(Integer.parseInt(data.get(0).getValue()));

                                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                                alertConfirmation.setHeaderText("supprimer le décharge");
                                alertConfirmation.setContentText("Voulez-vous vraiment supprimer le décharge?");
                                alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                                okButton.setText("D'ACCORD");

                                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                                cancel.setText("Annulation");

                                alertConfirmation.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.CANCEL) {
                                        alertConfirmation.close();
                                    } else if (response == ButtonType.OK) {

                                        ArrayList<ComponentDecharge> componentDecharges = componentDechargeOperation.getAllByDecharge(decharge.getId());
                                        for (ComponentDecharge componentDecharge : componentDecharges){

                                            StoreCard storeCard = storeCardOperation.get(componentDecharge.getIdStore());
                                            storeCard.setQteConsumed(componentDecharge.getQte());

                                            storeCardOperation.subQteConsumed(storeCard);
                                            componentDechargeOperation.delete(componentDecharge);
                                        }
                                        dechargeOperation.delete(decharge);

                                        refreshDecharge();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("ATTENTION");
                            alertWarning.setContentText("Veuillez sélectionner un décharge à supprimer");
                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tabCarteGasoline":
                    try {

                        List<StringProperty> data = tableCarteGasoline.getSelectionModel().getSelectedItem();
                        if (data != null) {

                            try {
                                RechargeGasolineCard gasolineCard = rechargeGasolineCardOperation.get(Integer.parseInt(data.get(0).getValue()));

                                Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                                alertConfirmation.setHeaderText("supprimer le décharge");
                                alertConfirmation.setContentText("Voulez-vous vraiment supprimer le consumation ?");
                                alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                                Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                                okButton.setText("D'ACCORD");

                                Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                                cancel.setText("Annulation");

                                alertConfirmation.showAndWait().ifPresent(response -> {
                                    if (response == ButtonType.CANCEL) {
                                        alertConfirmation.close();
                                    } else if (response == ButtonType.OK) {

                                        rechargeGasolineCardOperation.delete(gasolineCard);
                                        refreshCarteGasoline();
                                    }
                                });

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("ATTENTION");
                            alertWarning.setContentText("Veuillez sélectionner un consumation à supprimer");
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
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionPrint(){

        try {
            String tabId = tabPane.getSelectionModel().getSelectedItem().getId();
            switch (tabId){
                case "tabSortie":
                    try {
                        List<StringProperty> data  = table.getSelectionModel().getSelectedItem();
                        if (data != null){
                            try {
                                Output output = outputOperation.get(Integer.parseInt(data.get(0).getValue()));

                                Print print = new Print(output);
                                print.CreatePdfFacture();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("Attention ");
                            alertWarning.setContentText("Veuillez sélectionner une bonne à imprimmer");
                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    break;
                case "tabDecharge":
                    try {
                        List<StringProperty> data  = tableDecharge.getSelectionModel().getSelectedItem();
                        if (data != null){
                            try {
                                Decharge decharge = dechargeOperation.get(Integer.parseInt(data.get(0).getValue()));

                                Controllers.OutputControllers.DechargeControllers.Print print = new Controllers.OutputControllers.DechargeControllers.Print(decharge);
                                print.CreatePdfFacture();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("Attention ");
                            alertWarning.setContentText("Veuillez sélectionner une decharge à imprimmer");
                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }                    break;
                case "tabCarteGasoline":
                    try {
                        List<StringProperty> data  = tableCarteGasoline.getSelectionModel().getSelectedItem();
                        if (data != null){
                            try {
                                RechargeGasolineCard gasolineCard = rechargeGasolineCardOperation.get(Integer.parseInt(data.get(0).getValue()));

                                Controllers.OutputControllers.RechargeCarteGasolineControllers.Print print = new Controllers.OutputControllers.RechargeCarteGasolineControllers.Print(gasolineCard);
                                print.CreatePdf();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("Attention ");
                            alertWarning.setContentText("Veuillez sélectionner une bonne à imprimmer");
                            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    break;
            }
        }catch (Exception e){
            e.printStackTrace();
        }


    }

    @FXML
    private void ConsumptionStatus(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/ConsumptionStatusView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void refreshSortie(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTableOutput.clear();

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

                dataTableOutput.add(data);
            }
            conn.close();

            table.setItems(dataTableOutput);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshDecharge(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTableDecharge.clear();

            String query = "SELECT DECHARGE.ID, DECHARGE.DATE, E.FIRST_NAME, E.LAST_NAME, SERVICE.NAME AS NAME_SERV, DEP.NAME AS NAME_DEP, ED.FIRST_NAME AS FIRST_NAME_ED, ED.LAST_NAME AS LAST_NAME_ED\n" +
                    "FROM DECHARGE,EMPLOYEE E,EMPLOYEE ED,SERVICE,DEP WHERE DECHARGE.ID_EMP = E.ID AND E.ID_SERVICE = SERVICE.ID AND SERVICE.ID_DEP = DEP.ID AND ED.ID = DECHARGE.ID_EMP_DECH;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME_ED") + "   " + resultSet.getString("LAST_NAME_ED")));
                data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME") + "   " + resultSet.getString("LAST_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("NAME_SERV")));
                data.add( new SimpleStringProperty(resultSet.getString("NAME_DEP")));

                dataTableDecharge.add(data);
            }
            conn.close();

            tableDecharge.setItems(dataTableDecharge);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshCarteGasoline(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTableGasolineCard.clear();

            String query = "SELECT RECHARGE_GASOLINE_CARD.ID, RECHARGE_GASOLINE_CARD.NUMBER, RECHARGE_GASOLINE_CARD.DATE, EMPLOYEE.FIRST_NAME, EMPLOYEE.LAST_NAME,\n" +
                    "GASOLINE_CARD.NUMBER AS CARD, RECHARGE_GASOLINE_CARD.NUMBER_NAFTAL, RECHARGE_GASOLINE_CARD.PRICE FROM RECHARGE_GASOLINE_CARD,EMPLOYEE,GASOLINE_CARD\n" +
                    "WHERE RECHARGE_GASOLINE_CARD.ID_EMP = EMPLOYEE.ID AND RECHARGE_GASOLINE_CARD.ID_CARD_GASOLINE = GASOLINE_CARD.ID;\n";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                data.add(new SimpleStringProperty(resultSet.getDate("DATE").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getString("FIRST_NAME") + "   " + resultSet.getString("LAST_NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("CARD")));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER_NAFTAL")));
                data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));

                dataTableGasolineCard.add(data);
            }
            conn.close();

            tableCarteGasoline.setItems(dataTableGasolineCard);
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
                        else refreshSortie();

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
                dataTableOutput.clear();

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

                    dataTableOutput.add(data);
                }

                conn.close();

                table.setItems(dataTableOutput);

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
                dataTableOutput.clear();

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

                    dataTableOutput.add(data);
                }

                conn.close();

                table.setItems(dataTableOutput);

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
                    dataTableOutput.clear();

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

                        dataTableOutput.add(data);
                    }

                    conn.close();

                    table.setItems(dataTableOutput);
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
        else refreshSortie();
    }
    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refreshSortie();
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
