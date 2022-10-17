package Controllers.OutputControllers.DechargeControllers;


import BddPackage.*;
import Models.*;
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
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Predicate;

public class UpdateController implements Initializable {

    @FXML
    ImageView ivSelectEmployee,ivSelectEmployeeDech;
    @FXML
    DatePicker dpDate;
    @FXML
    ComboBox<String> cbEmployee,cbEmployeeDech;
    @FXML
    TextField tfRechercheArticle,tfRecherche;
    @FXML
    TableView<List<StringProperty>>  tableDemands,tableArticle;

    @FXML
    TableColumn<List<StringProperty>,String> tcIdArticle, tcNameArticle, tcQteArticle, tcCategoryArticle;
    @FXML
    TableColumn<List<StringProperty>,String> tcId,tcName,tcQte;
    @FXML
    Button btnInsert;

    private final DechargeOperation operation = new DechargeOperation();
    private final ArticlesOperation articlesOperation = new ArticlesOperation();
    private final EmployeeOperation employeeOperation = new EmployeeOperation();
    private final ComponentDechargeOperation componentDechargeOperation = new ComponentDechargeOperation();
    private final StoreCardOperation storeCardOperation = new StoreCardOperation();

    private final ArrayList<StoreCard> stores = new ArrayList<>();
    private ArrayList<StoreCard> storesInit = new ArrayList<>();
    private ArrayList<ComponentDecharge> componentDecharges = new ArrayList<>();
    private ArrayList<ComponentDecharge> componentDechargesInit = new ArrayList<>();
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ObservableList<String> comboEmployeeData = FXCollections.observableArrayList();
    private ArrayList<Employee> employees = new ArrayList<>();
    private Employee selectedEmployee,selectedEmployeeDech;
    private Decharge selectDecharge;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tcIdArticle.setCellValueFactory(data -> data.getValue().get(0));
        tcNameArticle.setCellValueFactory(data -> data.getValue().get(1));
        tcQteArticle.setCellValueFactory(data -> data.getValue().get(2));
        tcCategoryArticle.setCellValueFactory(data -> data.getValue().get(3));

        tcId.setCellValueFactory(data -> data.getValue().get(0));
        tcName.setCellValueFactory(data -> data.getValue().get(1));
        tcQte.setCellValueFactory(data -> data.getValue().get(2));

        refreshProduct();
        refreshComboEmployee();

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

    public void Init(Decharge decharge){
        this.selectDecharge = decharge;

        Employee employee = employeeOperation.get(decharge.getIdEmp());
        cbEmployee.getSelectionModel().select(employee.getFirstName() + " " + employee.getLastName());
        int index = cbEmployee.getSelectionModel().getSelectedIndex();
        this.selectedEmployee = employees.get(index);

        Employee employeeDech = employeeOperation.get(decharge.getIdEmpDech());
        cbEmployeeDech.getSelectionModel().select(employeeDech.getFirstName() + " " + employeeDech.getLastName());
        int indexDech = cbEmployeeDech.getSelectionModel().getSelectedIndex();
        this.selectedEmployeeDech = employees.get(indexDech);

        dpDate.setValue(decharge.getDate());

        componentDecharges = componentDechargeOperation.getAllByDecharge(decharge.getId());
        refreshDemandes();
    }

    private void refreshDemandes(){
        try {
            dataTable.clear();
            for (ComponentDecharge decharge: componentDecharges){
                StoreCard storeCard = storeCardOperation.get(decharge.getIdStore());
                Article article = articlesOperation.get(decharge.getIdArt());

                stores.add(storeCard);

                List<StringProperty> data = new ArrayList<>();
                data.add(0, new SimpleStringProperty(String.valueOf(article.getId())));
                data.add(1, new SimpleStringProperty(article.getName()));
                data.add(2, new SimpleStringProperty(String.valueOf(decharge.getQte())));

                dataTable.add(data);
            }
            tableDemands.setItems(dataTable);
            storesInit = new ArrayList<>(stores);
            componentDechargesInit = new ArrayList<>(componentDecharges);
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionComboEmployee(){
        int index = cbEmployee.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            selectedEmployee = employees.get(index);
        }
    }

    @FXML
    private void ActionComboEmployeeDech(){
        int index = cbEmployeeDech.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            selectedEmployeeDech = employees.get(index);
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
            cbEmployeeDech.setItems(comboEmployeeData);
            cbEmployee.getSelectionModel().select(0);
            cbEmployeeDech.getSelectionModel().select(0);
            ActionComboEmployee();
            ActionComboEmployeeDech();
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
            cbEmployeeDech.getSelectionModel().select(comboEmployeeData.size() - 1);
            ActionComboEmployee();
            ActionComboEmployeeDech();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionSearchEmployee(MouseEvent event){
        if (event.getSource().equals(ivSelectEmployeeDech)){
            try {

                Employee employee = new Employee();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/DechargeViews/SelectEmployeeView.fxml"));
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
        }else if (event.getSource().equals(ivSelectEmployee)){
            try {

                Employee employee = new Employee();

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/DechargeViews/SelectEmployeeView.fxml"));
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
                    cbEmployeeDech.getSelectionModel().select(employee.getFirstName() + " " + employee.getLastName());
                    int index = cbEmployeeDech.getSelectionModel().getSelectedIndex();
                    this.selectedEmployeeDech = employees.get(index);
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
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
                   TextInputDialog dialog = new TextInputDialog();

                   dialog.setTitle("QUANTITE");
                   dialog.initOwner(this.tfRecherche.getScene().getWindow());
                   dialog.setHeaderText("Entre le quantite");
                   dialog.setContentText("Qte :");

                   Optional<String> result = dialog.showAndWait();

                   result.ifPresent(Q -> {
                       int qte = Integer.parseInt(Q);
                       if (qte <= article.getQte()){
                           StoreCard storeCard = storeCardOperation.getByArticleQteNotNull(article.getId());
                           ComponentDecharge componentDecharge = new ComponentDecharge();

                           componentDecharge.setIdArt(article.getId());
                           componentDecharge.setIdStore(storeCard.getId());
                           componentDecharge.setQte(qte);
                           componentDecharges.add(componentDecharge);

                           StoreCard store = new StoreCard();
                           store.setId(storeCard.getId());
                           store.setQteConsumed(qte);

                           stores.add(store);

                           List<StringProperty> data = new ArrayList<>();
                           data.add(0, new SimpleStringProperty(String.valueOf(article.getId())));
                           data.add(1, new SimpleStringProperty(article.getName()));
                           data.add(2, new SimpleStringProperty(String.valueOf(qte)));

                           dataTable.add(data);
                       }else {
                           Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                           alertWarning.setHeaderText("ATTENTION ");
                           alertWarning.setContentText("La quantité est supérieure à la quantité disponible");
                           alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                           Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                           okButton.setText("D'ACCORD");
                           alertWarning.showAndWait();
                       }

                   });
               }catch (Exception e){
                   e.printStackTrace();
               }
            }
            tableDemands.setItems(dataTable);
        }
    }

    private int exist(List<StringProperty> dataSelected){
        AtomicInteger ex = new AtomicInteger(-1);
            for (int i = 0 ; i < dataTable.size() ; i++) {
                if (dataTable.get(i).get(0).getValue().equals(dataSelected.get(0).getValue()) ){
                    ex.set(i);
                    break;
                }
            }
        return ex.get();
    }
    @FXML
    private void tableClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionUpdateDemand();
        }
    }
    @FXML
    private void ActionUpdateDemand(){

        List<StringProperty> dataSelected = tableDemands.getSelectionModel().getSelectedItem();
        int index = tableDemands.getSelectionModel().getSelectedIndex();
        if (dataSelected != null) {
            try {
                Article article = articlesOperation.get(Integer.parseInt(dataSelected.get(0).getValue()));
                TextInputDialog dialog = new TextInputDialog(dataSelected.get(2).getValue());

                dialog.setTitle("QUANTITE");
                dialog.initOwner(this.tfRecherche.getScene().getWindow());
                dialog.setHeaderText("Entre le quantite");
                dialog.setContentText("Qte :");

                Optional<String> result = dialog.showAndWait();

                result.ifPresent(Q -> {
                    int qte = Integer.parseInt(Q);
                    int qteEx = Integer.parseInt(dataSelected.get(2).getValue());
                    if (qte <= (article.getQte() + qteEx)){
                        StoreCard storeCard = storeCardOperation.getByArticleQteNotNull(article.getId());
                        ComponentDecharge componentDecharge = new ComponentDecharge();

                        componentDecharge.setIdArt(article.getId());
                        componentDecharge.setIdStore(storeCard.getId());
                        componentDecharge.setQte(qte);
                        componentDecharges.set(index,componentDecharge);


                        StoreCard store = new StoreCard();
                        store.setId(storeCard.getId());
                        store.setQteConsumed(qte);

                        stores.set(index,store);

                        List<StringProperty> data = new ArrayList<>();
                        data.add(0, new SimpleStringProperty(String.valueOf(article.getId())));
                        data.add(1, new SimpleStringProperty(article.getName()));
                        data.add(2, new SimpleStringProperty(String.valueOf(qte)));

                        dataTable.set(index,data);
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION ");
                        alertWarning.setContentText("La quantité est supérieure à la quantité disponible");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }

                });
            }catch (Exception e){
                e.printStackTrace();
            }

            tableDemands.setItems(dataTable);
        }
    }

    @FXML
    private void ActionDeleteSales(){
        int compoSelectedIndex = tableDemands.getSelectionModel().getSelectedIndex();

        if (compoSelectedIndex != -1){
            stores.remove(compoSelectedIndex);
            componentDecharges.remove(compoSelectedIndex);
            dataTable.remove(compoSelectedIndex);

            tableDemands.setItems(dataTable);
        }
    }

    @FXML
    private void ActionAnnulledAdd(){
        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmation.setHeaderText("CONFIRMER L'ANNULATION");
        alertConfirmation.setContentText("Êtes-vous sûr d'annuler le modification de décharge ?");
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
    void ActionUpdate(ActionEvent event) {

        try {
            LocalDate date = dpDate.getValue();

            int indexEmployee = cbEmployee.getSelectionModel().getSelectedIndex();
            int indexEmployeeDech = cbEmployeeDech.getSelectionModel().getSelectedIndex();

            if (date != null && indexEmployee != -1 && indexEmployeeDech != -1 && dataTable.size() != 0 ){

                Decharge decharge = new Decharge();
                decharge.setIdEmp(selectedEmployee.getId());
                decharge.setIdEmpDech(selectedEmployeeDech.getId());
                decharge.setDate(date);

                boolean ins = update(decharge);
                if (ins) {
                    deleteComponent();
                    insertComponent(this.selectDecharge.getId());
                    /*output.setId(ins);
                    Print print = new Print(output);
                    print.CreatePdfFacture();*/
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

    private void deleteComponent() {
        try {
            int size = componentDechargesInit.size();
            for (int i = 0; i < size; i++) {
                ComponentDecharge decharge = componentDechargesInit.get(i);
                StoreCard storeCard = storesInit.get(i);

                deleteComponentDecharge(decharge);
                subQteConsumedStore(storeCard);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void insertComponent(int idDecharge) {
        try {
            int size = componentDecharges.size();
            for (int i = 0; i < size; i++) {
                ComponentDecharge decharge = componentDecharges.get(i);
                StoreCard storeCard = stores.get(i);

                decharge.setIdDecharge(idDecharge);

                insertComponentDecharge(decharge);
                addQteConsumedStore(storeCard);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean update(Decharge decharge) {
        boolean insert = false;
        try {
            insert = operation.update(decharge,this.selectDecharge);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentDecharge(ComponentDecharge componentDecharge){
        boolean insert = false;
        try {
            insert = componentDechargeOperation.insert(componentDecharge);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean deleteComponentDecharge(ComponentDecharge componentDecharge){
        boolean insert = false;
        try {
            insert = componentDechargeOperation.delete(componentDecharge);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean addQteConsumedStore(StoreCard storeCard){
        boolean update = false;
        try {
            update = storeCardOperation.addQteConsumed(storeCard);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }

    private boolean subQteConsumedStore(StoreCard storeCard){
        boolean update = false;
        try {
            update = storeCardOperation.subQteConsumed(storeCard);
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
        ObservableList<List<StringProperty>> items = tableDemands.getItems();
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
            }  else return stringProperties.get(3).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableDemands.comparatorProperty());
        tableDemands.setItems(sortedList);
    }


}
