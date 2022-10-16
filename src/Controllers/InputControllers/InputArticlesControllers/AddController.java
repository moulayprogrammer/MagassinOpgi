package Controllers.InputControllers.InputArticlesControllers;


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
    DatePicker dpBRDate,dpFactDate,dpBCDate;
    @FXML
    Label lbSumWeight,lbSumTotal;
    @FXML
    ComboBox<String> cbProvider;
    @FXML
    TextField tfRechercheArticle,tfRecherche,tfNumBR,tfNumFact,tfNumBC;
    @FXML
    TableView<List<StringProperty>> tableArticle, tablePorches;

    @FXML
    TableColumn<List<StringProperty>,String> tcIdArticle, tcNameArticle, tcCategoryArticle;
    @FXML
    TableColumn<List<StringProperty>,String> tcId,tcName,tcUnit,tcPriceU,tcQte,tcPriceTotal;
    @FXML
    Button btnInsert;

    private final InputOperation operation = new InputOperation();
    private final ComponentInputOperation componentInputOperation = new ComponentInputOperation();
    private final ProviderOperation providerOperation = new ProviderOperation();
    private final ArticlesOperation articlesOperation = new ArticlesOperation();
    private final StoreCardTempOperation storeCardTempOperation = new StoreCardTempOperation();

    private final ArrayList<ComponentInput> componentInputs = new ArrayList<>();
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final List<Double> priceList = new ArrayList<>();
    private final ObservableList<String> comboProviderData = FXCollections.observableArrayList();
    private ArrayList<Provider> providers = new ArrayList<>();
    private Provider providerSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        tcIdArticle.setCellValueFactory(data -> data.getValue().get(0));
        tcNameArticle.setCellValueFactory(data -> data.getValue().get(1));
        tcCategoryArticle.setCellValueFactory(data -> data.getValue().get(2));

        tcId.setCellValueFactory(data -> data.getValue().get(0));
        tcName.setCellValueFactory(data -> data.getValue().get(1));
        tcUnit.setCellValueFactory(data -> data.getValue().get(2));
        tcQte.setCellValueFactory(data -> data.getValue().get(3));
        tcPriceU.setCellValueFactory(data -> data.getValue().get(4));
        tcPriceTotal.setCellValueFactory(data -> data.getValue().get(5));

        refreshArticles();
        refreshComboProviders();

        // set Date
        dpBRDate.setValue(LocalDate.now());
        dpFactDate.setValue(LocalDate.now());
        dpBCDate.setValue(LocalDate.now());

        tfRechercheArticle.textProperty().addListener((observable, oldValue, newValue) -> {

            if (!newValue.isEmpty()) {
                refreshArticles();

                ObservableList<List<StringProperty>> items = tableArticle.getItems();
                FilteredList<List<StringProperty>> filteredData = new FilteredList<>(items, e -> true);

                filteredData.setPredicate((Predicate<? super List<StringProperty>>) stringProperties -> {

                    if (stringProperties.get(1).toString().contains(newValue)) {
                        return true;
                    } else  return stringProperties.get(2).toString().contains(newValue);
                });

                SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
                sortedList.comparatorProperty().bind(tableArticle.comparatorProperty());
                tableArticle.setItems(sortedList);
            }else {
                refreshArticles();
            }
        });

    }

    @FXML
    private void ActionComboProvider(){
        int index = cbProvider.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            providerSelected = providers.get(index);
        }
    }


    private void refreshComboProviders() {
        clearCombo();
        try {
            providers =  providerOperation.getAll();
            providers.forEach(provider -> {
                comboProviderData.add(provider.getName());
            });
            cbProvider.setItems(comboProviderData);
            if (providers.size() != 0) {
                providerSelected = providers.get(0);
                cbProvider.getSelectionModel().select(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbProvider.getSelectionModel().clearSelection();
        comboProviderData.clear();
        providers.clear();
    }

    private void refreshArticles(){
        ObservableList<List<StringProperty>> componentDataTable = FXCollections.observableArrayList();

        try {
            ArrayList<Article> articles = articlesOperation.getAllWithCat();

            articles.forEach(article -> {
                List<StringProperty> data = new ArrayList<>();
                data.add( new SimpleStringProperty(String.valueOf(article.getId())));
                data.add( new SimpleStringProperty(article.getName()));
                data.add(new SimpleStringProperty(article.getCat()));

                componentDataTable.add(data);
            });

        }catch (Exception e){
            e.printStackTrace();
        }

        tableArticle.setItems(componentDataTable);
    }

    @FXML
    private void ActionAddProvider(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputArticlesViews/AddProviderView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshComboProviders();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAddArticle(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshArticles();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionSearchProvider(){
        try {

            Provider provider = new Provider();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputArticlesViews/SelectProviderView.fxml"));
            DialogPane temp = loader.load();
            SelectProviderController controller = loader.getController();
            controller.Init(provider);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            if (provider.getName() != null){
                cbProvider.getSelectionModel().select(provider.getName());
                int index = cbProvider.getSelectionModel().getSelectedIndex();
                this.providerSelected = providers.get(index);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tableProductClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionAddPorches();
        }
    }
    @FXML
    private void ActionAddPorches(){
        List<StringProperty> dataSelected = tableArticle.getSelectionModel().getSelectedItem();

        if (dataSelected != null) {
            int ex = exist(dataSelected);
            if ( ex == -1 ){
                try {
                    Article article = articlesOperation.getWithCatUnit(Integer.parseInt(tableArticle.getSelectionModel().getSelectedItem().get(0).getValue()));
                    ComponentInput componentInput = new ComponentInput();

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputArticlesViews/AddPorchesView.fxml"));
                    DialogPane temp = loader.load();
                    AddPorchesController controller = loader.getController();
                    controller.Init(article,componentInput);
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(temp);
                    dialog.resizableProperty().setValue(false);
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                    closeButton.setVisible(false);
                    dialog.showAndWait();


                    if (componentInput.getPrice() != 0){

                        List<StringProperty> data = new ArrayList<>();
                        data.add(0, new SimpleStringProperty(String.valueOf(article.getId())));
                        data.add(1, new SimpleStringProperty(article.getName()));
                        data.add(2, new SimpleStringProperty(article.getUnit()));
                        data.add(3, new SimpleStringProperty(String.valueOf(componentInput.getQte())));
                        data.add(4, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", componentInput.getPrice())));
                        data.add(5, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (componentInput.getPrice() * componentInput.getQte()))));

                        priceList.add(componentInput.getPrice());
                        componentInputs.add(componentInput);
                        dataTable.add(data);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            tablePorches.setItems(dataTable);
            sumTotalTablePorches();
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
    private void tableSalesClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionUpdateSales();
        }
    }

    @FXML
    private void ActionUpdateSales(){
        int compoSelectedIndex = tablePorches.getSelectionModel().getSelectedIndex();
        if (compoSelectedIndex != -1){
            try {

                Article article = articlesOperation.getWithCatUnit(Integer.parseInt(tablePorches.getSelectionModel().getSelectedItem().get(0).getValue()));
                ComponentInput componentInput = componentInputs.get(compoSelectedIndex);

                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/InputArticlesViews/UpdatePorchesView.fxml"));
                DialogPane temp = loader.load();
                UpdatePorchesController controller = loader.getController();
                controller.Init(article,componentInput);
                Dialog<ButtonType> dialog = new Dialog<>();
                dialog.setDialogPane(temp);
                dialog.resizableProperty().setValue(false);
                dialog.initOwner(this.tfRecherche.getScene().getWindow());
                dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                closeButton.setVisible(false);
                dialog.showAndWait();

                if (componentInput.getPrice() != 0){

                    List<StringProperty> data = new ArrayList<>();
                    data.add(0, new SimpleStringProperty(String.valueOf(article.getId())));
                    data.add(1, new SimpleStringProperty(article.getName()));
                    data.add(2, new SimpleStringProperty(article.getUnit()));
                    data.add(3, new SimpleStringProperty(String.valueOf(componentInput.getQte())));
                    data.add(4, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", componentInput.getPrice())));
                    data.add(5, new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", (componentInput.getPrice() * componentInput.getQte()))));

                    priceList.set(compoSelectedIndex,componentInput.getPrice());
                    componentInputs.set(compoSelectedIndex,componentInput);
                    dataTable.set(compoSelectedIndex,data);
                }


            }catch (Exception e){
                e.printStackTrace();
            }
            tablePorches.setItems(dataTable);
            sumTotalTablePorches();
        }
    }

    @FXML
    private void ActionDeleteSales(){
        int compoSelectedIndex = tablePorches.getSelectionModel().getSelectedIndex();
        if (compoSelectedIndex != -1){

            dataTable.remove(compoSelectedIndex);
            componentInputs.remove(compoSelectedIndex);
            priceList.remove(compoSelectedIndex);
            tablePorches.setItems(dataTable);

            sumTotalTablePorches();
        }
    }
    private void sumTotalTablePorches(){

        double totalPrice = 0.0;
        int totalling = 0;

        for (int i = 0; i < dataTable.size() ; i++) {
            int qte = Integer.parseInt(dataTable.get(i).get(3).getValue());
            totalling += qte;
            double price = priceList.get(i);
            totalPrice += (price * qte);
        }
        double totalFacture = totalPrice;
        lbSumTotal.setText(String.format(Locale.FRANCE, "%,.2f", totalPrice));
//        lbSumWeight.setText(String.valueOf(totalling));
    }

    @FXML
    private void ActionAnnulledAdd(){

        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
        alertConfirmation.setHeaderText("CONFIRMER L'ANNULATION");
        alertConfirmation.setContentText("Êtes-vous sûr d'annuler le bon de réception?");
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
            String numBR = tfNumBR.getText().trim();
            String numFact = tfNumFact.getText().trim();
            String numBC = tfNumBC.getText().trim();

            LocalDate dateBR = dpBRDate.getValue();
            LocalDate dateFact = dpFactDate.getValue();
            LocalDate dateBC = dpBCDate.getValue();

            int indexProvider = cbProvider.getSelectionModel().getSelectedIndex();

            if (!numBR.isEmpty() && !numFact.isEmpty() && !numBC.isEmpty() && dateBR != null && dateFact != null && dateBC != null && providerSelected.getId() != -1){

                Input input = new Input();
                input.setNumber(numBR);
                input.setDate(dateBR);
                input.setNumberFact(numFact);
                input.setDateFact(dateFact);
                input.setNumberBC(numBC);
                input.setDateBC(dateBC);
                input.setIdProvider(providerSelected.getId());

                int ins = insert(input);
                if (ins != -1) {
                    insertComponent(ins);
                    closeDialog(this.btnInsert);

                } else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("ATTENTION");
                    alertWarning.setContentText("ERREUR INCONNUE");
                    alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertWarning.showAndWait();
                }
            } else {
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

    private void insertComponent(int idInput ) {

        for (ComponentInput componentInput : componentInputs){

            componentInput.setIdInput(idInput);
            insertComponentInput(componentInput);

            StoreCard storeCard = new StoreCard();
            storeCard.setIdInput(idInput);
            storeCard.setIdArticle(componentInput.getIdArticle());
            storeCard.setDateStore(dpBRDate.getValue());
            storeCard.setQteStored(componentInput.getQte());
            storeCard.setQteConsumed(0);
            storeCard.setPrice(componentInput.getPrice());

            insertStoreCardTemp(storeCard);
        }
    }

    private int insert(Input input) {
        int insert = 0;
        try {
            insert = operation.insertId(input);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertComponentInput(ComponentInput componentInput){
        boolean insert = false;
        try {
            insert = componentInputOperation.insert(componentInput);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean insertStoreCardTemp(StoreCard storeCard){
        boolean insert = false;
        try {
            insert = storeCardTempOperation.insert(storeCard);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }



    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        tablePorches.setItems(dataTable);
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<List<StringProperty>> items = tablePorches.getItems();
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
        sortedList.comparatorProperty().bind(tablePorches.comparatorProperty());
        tablePorches.setItems(sortedList);
    }


}
