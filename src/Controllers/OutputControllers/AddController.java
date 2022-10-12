package Controllers.OutputControllers;


import BddPackage.*;
import Models.*;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class AddController implements Initializable {

    @FXML
    DatePicker dpDate;
    @FXML
    Label lbSumTotal;
    @FXML
    ComboBox<String> cbEmployee;
    @FXML
    TextField tfRechercheArticle,tfRecherche,tfNumBR;
    @FXML
    TableView<List<List<StringProperty>>>  tableDemands;
    @FXML
    TableView<List<StringProperty>> tableArticle;

    @FXML
    TableColumn<List<StringProperty>,String> tcIdArticle, tcNameArticle, tcQteArticle, tcCategoryArticle;
    @FXML
    TableColumn<List<List<StringProperty>>,String> tcId,tcName,tcQte,tcQtDEM,tcQteSERV,tcPriceUnit,tcPriceTotal;
    @FXML
    Button btnInsert;

    private final OutputOperation operation = new OutputOperation();
    private final ArticlesOperation articlesOperation = new ArticlesOperation();
    private final EmployeeOperation employeeOperation = new EmployeeOperation();
    private final ComponentOutputOperation componentOutputOperation = new ComponentOutputOperation();
    private final StoreCardOperation storeCardOperation = new StoreCardOperation();

    private final HashMap<Integer,List<StoreCard>> stores = new HashMap<>();
    private final HashMap<Integer,List<ComponentOutput>> outputs = new HashMap<>();
    private final ObservableList<List<List<StringProperty>>> dataTable = FXCollections.observableArrayList();
    private final List<Double> priceList = new ArrayList<>();
    private final ObservableList<String> comboEmployeeData = FXCollections.observableArrayList();
    private ArrayList<Employee> employees = new ArrayList<>();
    private Employee selectedEmployee;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tcIdArticle.setCellValueFactory(data -> data.getValue().get(0));
        tcNameArticle.setCellValueFactory(data -> data.getValue().get(1));
        tcQteArticle.setCellValueFactory(data -> data.getValue().get(2));
        tcCategoryArticle.setCellValueFactory(data -> data.getValue().get(3));


        tcId.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().get(0).get(0).getValue()));
        tcName.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().get(0).get(1).getValue()));
        tcQtDEM.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().get(1).get(0).getValue()));
        tcQteSERV.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().get(1).get(1).getValue()));
        tcPriceUnit.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().get(0).get(2).getValue()));
        tcPriceTotal.setCellValueFactory(cellData -> Bindings.createObjectBinding(() -> cellData.getValue().get(0).get(3).getValue()));

        refreshProduct();
        refreshComboEmployee();

        // set Date
        dpDate.setValue(LocalDate.now());

        tfRechercheArticle.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()) {
                ObservableList<List<StringProperty>> items = tableArticle.getItems();
                FilteredList<List<StringProperty>> filteredData = new FilteredList<>(items, e -> true);

                filteredData.setPredicate((Predicate<? super List<StringProperty>>) stringProperties -> {

                    if (stringProperties.get(1).toString().contains(newValue)) {
                        return true;
                    } else if (stringProperties.get(2).toString().contains(newValue)) {
                        return true;
                    } else return stringProperties.get(3).toString().contains(newValue);
                });

                SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
                sortedList.comparatorProperty().bind(tableArticle.comparatorProperty());
                tableArticle.setItems(sortedList);
            }else {
                refreshProduct();
            }
        });

    }

    @FXML
    private void ActionComboEmployee(){
        int index = cbEmployee.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            selectedEmployee = employees.get(index);
        }
    }

    private void refreshComboEmployee() {
        clearCombo();
        try {
            employees = employeeOperation.getAll();

            for (Employee employee : employees){
                comboEmployeeData.add(employee.getFirstName() + " " + employee.getLastName());
            }

            cbEmployee.setItems(comboEmployeeData);
            cbEmployee.getSelectionModel().select(0);
            ActionComboEmployee();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbEmployee.getSelectionModel().clearSelection();
        comboEmployeeData.clear();
    }

    private void refreshProduct(){
        ObservableList<List<StringProperty>> componentDataTable = FXCollections.observableArrayList();

        try {
            ArrayList<Article> articles = articlesOperation.getAllWithCatUnitAndQteNotNull();

            articles.forEach(article -> {
                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(article.getId())));
                data.add( new SimpleStringProperty(article.getName()));
                data.add( new SimpleStringProperty(String.valueOf(article.getQte())));
                data.add( new SimpleStringProperty(String.valueOf(article.getCat())));

                componentDataTable.add(data);
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        tableArticle.setItems(componentDataTable);

    }

    @FXML
    private void ActionAddEmployee(){
        try {
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

            refreshComboEmployee();
            cbEmployee.getSelectionModel().select(comboEmployeeData.size() - 1);
            ActionComboEmployee();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionSearchEmployee(){
        try {

            Employee employee = new Employee();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/SelectEmployeeView.fxml"));
            DialogPane temp = loader.load();
            SelectEmployeeController controller = loader.getController();
            controller.Init(employee);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            if (employee.getFirstName() != null){
                cbEmployee.getSelectionModel().select(employee.getFirstName() + " " + employee.getLastName());
                int index = cbEmployee.getSelectionModel().getSelectedIndex();
                this.selectedEmployee = employees.get(index);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tableProductClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionAddDemand();
        }
    }
    @FXML
    private void ActionAddDemand(){
        List<StringProperty> dataSelected = tableArticle.getSelectionModel().getSelectedItem();
        if (dataSelected != null) {
            int ex = exist(dataSelected);
            if ( ex == -1 ){
               try {
                   Article article = articlesOperation.get(Integer.parseInt(tableArticle.getSelectionModel().getSelectedItem().get(0).getValue()));
                   List<ComponentOutput> componentOutputs = new ArrayList<>();
                   List<StoreCard> storeCards = new ArrayList<>();


                   FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/AddDemandView.fxml"));
                   DialogPane temp = loader.load();
                   AddDemandController controller = loader.getController();
                   controller.Init(article,componentOutputs,storeCards);
                   Dialog<ButtonType> dialog = new Dialog<>();
                   dialog.setDialogPane(temp);
                   dialog.resizableProperty().setValue(false);
                   dialog.initOwner(this.tfRecherche.getScene().getWindow());
                   dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                   Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                   closeButton.setVisible(false);
                   dialog.showAndWait();

                   for (int i = 0; i < componentOutputs.size(); i++) {
                       ComponentOutput componentOutput = componentOutputs.get(i);
                       StoreCard storeCard = storeCards.get(i);

                       List<List<StringProperty>> data = new ArrayList<>();
                       List<StringProperty> dataG = new ArrayList<>();
                       List<StringProperty> dataQ = new ArrayList<>();

                       dataG.add(0, new SimpleStringProperty(String.valueOf(article.getId())));
                       dataG.add(1, new SimpleStringProperty(article.getName()));

                       dataQ.add(0, new SimpleStringProperty(String.valueOf(componentOutput.getQteDem())));
                       dataQ.add(1, new SimpleStringProperty(String.valueOf(componentOutput.getQteServ())));

                       dataG.add(2, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", storeCard.getPrice())));
                       dataG.add(3, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (storeCard.getPrice() * componentOutput.getQteServ()))));

                       data.add(dataG);
                       data.add(dataQ);

                       priceList.add((storeCard.getPrice() * componentOutput.getQteServ()));
                       dataTable.add(data);
                   }
                   if (componentOutputs.size() != 0) {
                       stores.put(article.getId(), storeCards);
                       outputs.put(article.getId(), componentOutputs);
                   }

               }catch (Exception e){
                   e.printStackTrace();
               }
            }
            tableDemands.setItems(dataTable);
            sumTotalTableSales();
        }
    }

    private int exist(List<StringProperty> dataSelected){
        AtomicInteger ex = new AtomicInteger(-1);
            for (int i = 0 ; i < dataTable.size() ; i++) {
                if (dataTable.get(i).get(0).get(0).getValue().equals(dataSelected.get(0).getValue()) ){
                    ex.set(i);
                    break;
                }
            }
        return ex.get();
    }

    @FXML
    private void ActionDeleteSales(){
        int compoSelectedIndex = tableDemands.getSelectionModel().getSelectedIndex();

        if (compoSelectedIndex != -1){
            int id = Integer.parseInt(dataTable.get(compoSelectedIndex).get(0).get(0).getValue());

            stores.remove(id);
            outputs.remove(id);

            int size = dataTable.size();

            for (int i = 0; i < size; i++) {

                if (Integer.parseInt(dataTable.get(i).get(0).get(0).getValue()) == id){
                    dataTable.remove(i);
                    priceList.remove(i);
                    size--;
                    i--;
                }
            }
            tableDemands.setItems(dataTable);
            sumTotalTableSales();
        }
    }
    private void sumTotalTableSales(){
        double totalPrice = 0.0;

        for (int i = 0; i < dataTable.size() ; i++) {
            double price = priceList.get(i);
            totalPrice += price;
        }
        lbSumTotal.setText(String.format(Locale.FRANCE, "%,.2f", totalPrice));
    }

    @FXML
    private void ActionAnnulledAdd(){
        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmation.setHeaderText("CONFIRMER L'ANNULATION");
        alertConfirmation.setContentText("Êtes-vous sûr d'annuler le bon de sortie ?");
        alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
        Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
        okButton.setText("D'ACCORD");

        Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
        cancel.setText("Annuler");

        alertConfirmation.showAndWait().ifPresent(response -> {
            if (response == ButtonType.CANCEL) {

            } else if (response == ButtonType.OK) {
                closeDialog(btnInsert);
            }
        });

    }

    @FXML
    void ActionInsert(ActionEvent event) {

        try {
            LocalDate date = dpDate.getValue();
            String number = tfNumBR.getText().trim();

            int indexEmployee = cbEmployee.getSelectionModel().getSelectedIndex();

            if (date != null && !number.isEmpty() && indexEmployee != -1 && dataTable.size() != 0 ){

                Output output = new Output();
                output.setDate(date);
                output.setNumber(number);
                output.setIdEmp(selectedEmployee.getId());

                int ins = insert(output);
                if (ins != -1) {
                    insertComponent(ins);
                    closeDialog(this.btnInsert);
                } else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("ATTENTION ");
                    alertWarning.setContentText("ERREUR INCONNUE");
                    alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertWarning.showAndWait();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION ");
                alertWarning.setContentText("Merci de remplir tous les champs");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }


    private void insertComponent(int idOutput) {

        outputs.forEach((integer, componentOutputs) -> {
            List<StoreCard> storeCards = stores.get(integer);

            for (int i = 0; i < componentOutputs.size(); i++) {
                ComponentOutput componentOutput = componentOutputs.get(i);
                StoreCard storeCard = storeCards.get(i);

                componentOutput.setIdOutput(idOutput);
                storeCard.setQteConsumed(componentOutput.getQteServ());

                insertComponentOutput(componentOutput);
                updateQteConsumedStore(storeCard);
            }
        });
    }

    private int insert(Output output) {
        int insert = 0;
        try {
            insert = operation.insertId(output);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentOutput(ComponentOutput componentOutput){
        boolean insert = false;
        try {
            insert = componentOutputOperation.insert(componentOutput);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean updateQteConsumedStore(StoreCard storeCard){
        boolean update = false;
        try {
            update = storeCardOperation.addQteConsumed(storeCard);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        tableDemands.setItems(dataTable);
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        /*ObservableList<List<StringProperty>> items = tableDemands.getItems();
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
            }  else return stringProperties.get(4).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableDemands.comparatorProperty());
        tableDemands.setItems(sortedList);*/
    }


}
