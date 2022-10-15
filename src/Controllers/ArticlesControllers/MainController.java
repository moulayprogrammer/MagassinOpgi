package Controllers.ArticlesControllers;

import BddPackage.ArticlesOperation;
import BddPackage.CategoryOperation;
import BddPackage.ConnectBD;
import BddPackage.GasolineCardOperation;
import Controllers.ArticlesControllers.ConsommablesControllers.MagasinController;
import Controllers.ArticlesControllers.ConsommablesControllers.UpdateController;
import Models.Article;
import Models.Category;
import Models.GasolineCard;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    TabPane tabPane;
    @FXML
    Tab tabConsumed,tabGasoline;
    @FXML
    TextField tfRecherche;
    @FXML
    ComboBox<String> cbCategory,cbFilter;
    @FXML
    Label lbCategory;
    @FXML
    TableView<List<StringProperty>> table,tableGasoline;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clName,clCategory,clUnit,clQte,clPrice,clQteAlert;
    @FXML
    TableColumn<List<StringProperty>,String> clIdCard,clNumberCard,clBalanceCard,clRechargeCard,clConsumedCard;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final ArticlesOperation articlesOperation = new ArticlesOperation();
    private final GasolineCardOperation gasolineCardOperation = new GasolineCardOperation();
    private final CategoryOperation categoryOperation = new CategoryOperation();

    private final ObservableList<List<StringProperty>> dataTableArticles = FXCollections.observableArrayList();
    private final ObservableList<List<StringProperty>> dataTableGasoline = FXCollections.observableArrayList();
    private final ObservableList<String> comboCategoryData = FXCollections.observableArrayList();
    private final ObservableList<String> comboFilterData = FXCollections.observableArrayList();

    ArrayList<Category> categories;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clName.setCellValueFactory(data -> data.getValue().get(1));
        clCategory.setCellValueFactory(data -> data.getValue().get(2));
        clUnit.setCellValueFactory(data -> data.getValue().get(3));
        clQte.setCellValueFactory(data -> data.getValue().get(4));
        clPrice.setCellValueFactory(data -> data.getValue().get(5));
        clQteAlert.setCellValueFactory(data -> data.getValue().get(6));

        clIdCard.setCellValueFactory(data -> data.getValue().get(0));
        clNumberCard.setCellValueFactory(data -> data.getValue().get(1));
        clBalanceCard.setCellValueFactory(data -> data.getValue().get(2));
        clRechargeCard.setCellValueFactory(data -> data.getValue().get(3));
        clConsumedCard.setCellValueFactory(data -> data.getValue().get(4));

        refreshArticles();
        refreshComboCategory();

        comboFilterData.addAll("Tout","Catégorie","Article en Qte Alert","Article fini");
        cbFilter.setItems(comboFilterData);
        cbFilter.getSelectionModel().select(0);

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            switch (newTab.getId()){
                case "tabConsumed":
                    refreshArticles();
                    break;
                case "tabGasoline":
                    refreshGasoline();
                    break;
            }
        });
    }

    private void refreshComboCategory() {
        comboCategoryData.clear();
        try {

            this.categories = categoryOperation.getAll();
            for (Category category: categories){
                comboCategoryData.add(category.getName());
            }

            cbCategory.setItems(comboCategoryData);
            cbCategory.getSelectionModel().select(0);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAdd(){
        String idTab = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (idTab) {
            case "tabConsumed":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/ConsommablesViews/AddView.fxml"));
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
                break;
            case "tabGasoline":
                try {
                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/CarburantViews/AddView.fxml"));
                    DialogPane temp = loader.load();
                    Dialog<ButtonType> dialog = new Dialog<>();
                    dialog.setDialogPane(temp);
                    dialog.resizableProperty().setValue(false);
                    dialog.initOwner(this.tfRecherche.getScene().getWindow());
                    dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
                    Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
                    closeButton.setVisible(false);
                    dialog.showAndWait();

                    refreshGasoline();

                } catch (IOException e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    @FXML
    private void tableClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            List<StringProperty> data = table.getSelectionModel().getSelectedItem();

            if (data != null){
                try {
                    Article article = articlesOperation.get(Integer.parseInt(data.get(0).getValue()));

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/ConsommablesViews/MagasinView.fxml"));
                    DialogPane temp = loader.load();
                    MagasinController controller = loader.getController();
                    controller.Init(article);
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
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION");
                alertWarning.setContentText("Veuillez sélectionner un élément à modifier");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        }
    }

    @FXML
    private void ActionUpdate() {

        try {
            String idTab = tabPane.getSelectionModel().getSelectedItem().getId();
            switch (idTab) {
                case "tabConsumed":
                    List<StringProperty> data = table.getSelectionModel().getSelectedItem();

                    if (data != null) {
                        try {
                            Article article = articlesOperation.get(Integer.parseInt(data.get(0).getValue()));

                            updateArticle(article);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un élément à modifier");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                    break;
                case "tabGasoline":
                    List<StringProperty> dataG = tableGasoline.getSelectionModel().getSelectedItem();

                    if (dataG != null) {
                        try {
                            GasolineCard gasolineCard = gasolineCardOperation.get(Integer.parseInt(dataG.get(0).getValue()));

                            updateGasoline(gasolineCard);

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION");
                        alertWarning.setContentText("Veuillez sélectionner un élément à modifier");
                        alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    private void updateArticle(Article article){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/ConsommablesViews/UpdateView.fxml"));
            DialogPane temp = loader.load();
            UpdateController controller = loader.getController();
            controller.InitUpdate(article);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshArticles();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void tableGasClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            List<StringProperty> dataG = tableGasoline.getSelectionModel().getSelectedItem();

            if (dataG != null) {
                try {
                    GasolineCard gasolineCard = gasolineCardOperation.get(Integer.parseInt(dataG.get(0).getValue()));

                    updateGasoline(gasolineCard);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION");
                alertWarning.setContentText("Veuillez sélectionner un élément à modifier");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        }
    }
    private void updateGasoline(GasolineCard gasolineCard){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/CarburantViews/UpdateView.fxml"));
            DialogPane temp = loader.load();
            Controllers.ArticlesControllers.CarburantControllers.UpdateController controller = loader.getController();
            controller.Init(gasolineCard);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshGasoline();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAddToArchive(){

        String idTab = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (idTab) {
            case "tabConsumed":
                AddArticleToArchive();
            case "tabGasoline":
                AddGasolineToArchive();
                break;
        }

    }

    private void AddArticleToArchive(){
        try {
            List<StringProperty> data = table.getSelectionModel().getSelectedItem();

            if (data != null){

                if (data.get(4).getValue().equals("0")) {

                    Article article = articlesOperation.get(Integer.parseInt(data.get(0).getValue()));

                    try {

                        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                        alertConfirmation.setHeaderText("CONFIRMATION D'ARCHIVAGE");
                        alertConfirmation.setContentText("Êtes-vous sûr d'avoir archivé l'article ?");
                        alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");

                        Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                        cancel.setText("ANNULATION");

                        alertConfirmation.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.CANCEL) {
                                alertConfirmation.close();
                            } else if (response == ButtonType.OK) {
                                articlesOperation.AddToArchive(article);
                                refreshArticles();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
                    alertInformation.setHeaderText("IMPOSSIBLE D'ARCHIVER ");
                    alertInformation.setContentText("Vous ne pouvez pas archiver l'article actuel car il est laissé dans le stock");
                    alertInformation.initOwner(this.tfRecherche.getScene().getWindow());
                    Button okButton = (Button) alertInformation.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertInformation.showAndWait();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION ");
                alertWarning.setContentText("Veuillez sélectionner un article à archiver");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void AddGasolineToArchive(){
        try {
            List<StringProperty> data = tableGasoline.getSelectionModel().getSelectedItem();

            if (data != null){

                if (data.get(2).getValue().equals("0,00")) {

                    GasolineCard gasolineCard = gasolineCardOperation.get(Integer.parseInt(data.get(0).getValue()));

                    try {

                        Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                        alertConfirmation.setHeaderText("CONFIRMATION D'ARCHIVAGE");
                        alertConfirmation.setContentText("Êtes-vous sûr d'avoir archivé la carte ?");
                        alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                        Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");

                        Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                        cancel.setText("ANNULATION");

                        alertConfirmation.showAndWait().ifPresent(response -> {
                            if (response == ButtonType.CANCEL) {
                                alertConfirmation.close();
                            } else if (response == ButtonType.OK) {
                                gasolineCardOperation.AddToArchive(gasolineCard);
                                refreshGasoline();
                            }
                        });

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else {
                    Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
                    alertInformation.setHeaderText("IMPOSSIBLE D'ARCHIVER ");
                    alertInformation.setContentText("Vous ne pouvez pas archiver la carte actuelle car il reste un solde dessus");
                    alertInformation.initOwner(this.tfRecherche.getScene().getWindow());
                    Button okButton = (Button) alertInformation.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertInformation.showAndWait();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION ");
                alertWarning.setContentText("Veuillez sélectionner un carte à archiver");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionDeleteFromArchive(){
        String idTab = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (idTab) {
            case "tabConsumed":
                deleteArticleArchive();
            case "tabGasoline":
                deleteGasolineArchive();
                break;
        }
    }

    private void deleteArticleArchive(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/ConsommablesViews/ArchiveView.fxml"));
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

    private void deleteGasolineArchive(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/CarburantViews/ArchiveView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshGasoline();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void comboFilterAction() {
        try {
            int select  = cbFilter.getSelectionModel().getSelectedIndex();
            if (select != -1 ) {
                if (conn.isClosed()) conn = connectBD.connect();
                dataTableArticles.clear();

                cbCategory.setVisible(false);
                lbCategory.setVisible(false);

                String query = "";
                switch (select) {
                    case 0:
                        refreshArticles();

                        break;
                    case 1:
                        cbCategory.setVisible(true);
                        lbCategory.setVisible(true);
                        refreshComboCategory();

                        break;
                    case 2:
                        query = "SELECT ARTICLE.ID, ARTICLE.NAME, CATEGORY.NAME AS NAME_CAT, UNIT.NAME AS NAME_UNIT, ARTICLE.QTE_ALERT,\n" +
                                "(SELECT SUM(STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS QTE,\n" +
                                "(SELECT SUM((STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) * STORE_CARD.PRICE) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS PRICE\n" +
                                "FROM ARTICLE, CATEGORY, UNIT WHERE ARTICLE.QTE_ALERT >=  QTE AND ARTICLE.ID_CAT = CATEGORY.ID AND ARTICLE.ID_UNIT = UNIT.ID AND ARTICLE.ARCHIVE = 0 ;";

                        break;
                    case 3:
                        query = "SELECT ARTICLE.ID, ARTICLE.NAME, CATEGORY.NAME AS NAME_CAT, UNIT.NAME AS NAME_UNIT, ARTICLE.QTE_ALERT,\n" +
                                "(SELECT SUM(STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS QTE,\n" +
                                "(SELECT SUM((STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) * STORE_CARD.PRICE) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS PRICE\n" +
                                "FROM ARTICLE, CATEGORY, UNIT WHERE QTE IS NULL AND ARTICLE.ID_CAT = CATEGORY.ID AND ARTICLE.ID_UNIT = UNIT.ID AND ARTICLE.ARCHIVE = 0 ;";

                        break;
                }

                if (select > 1 ) {

                    PreparedStatement preparedStmt = conn.prepareStatement(query);
                    ResultSet resultSet = preparedStmt.executeQuery();
                    while (resultSet.next()) {

                        List<StringProperty> data = new ArrayList<>();

                        data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                        data.add(new SimpleStringProperty(resultSet.getString("NAME")));
                        data.add(new SimpleStringProperty(resultSet.getString("NAME_CAT")));
                        data.add(new SimpleStringProperty(resultSet.getString("NAME_UNIT")));
                        data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("QTE"))));
                        data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));
                        data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("QTE_ALERT"))));

                        dataTableArticles.add(data);
                    }

                    conn.close();

                    table.setItems(dataTableArticles);
                }
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void comboFilterCategoryAction() {
        try {
            int select  = cbCategory.getSelectionModel().getSelectedIndex();

            if (select != -1 ) {

                if (conn.isClosed()) conn = connectBD.connect();
                dataTableArticles.clear();

                String query = "SELECT ARTICLE.ID, ARTICLE.NAME, CATEGORY.NAME AS NAME_CAT, UNIT.NAME AS NAME_UNIT, ARTICLE.QTE_ALERT,\n" +
                        "(SELECT SUM(STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS QTE,\n" +
                        "(SELECT SUM((STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) * STORE_CARD.PRICE) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS PRICE\n" +
                        "FROM ARTICLE, CATEGORY, UNIT WHERE ARTICLE.ID_CAT = CATEGORY.ID AND ARTICLE.ID_UNIT = UNIT.ID AND ARTICLE.ARCHIVE = 0 AND CATEGORY.ID = ? ;";
                PreparedStatement preparedStmt = conn.prepareStatement(query);
                preparedStmt.setInt(1,categories.get(select).getId());
                ResultSet resultSet = preparedStmt.executeQuery();

                while (resultSet.next()) {

                    List<StringProperty> data = new ArrayList<>();

                    data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                    data.add(new SimpleStringProperty(resultSet.getString("NAME")));
                    data.add(new SimpleStringProperty(resultSet.getString("NAME_CAT")));
                    data.add(new SimpleStringProperty(resultSet.getString("NAME_UNIT")));
                    data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("QTE"))));
                    data.add(new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));
                    data.add(new SimpleStringProperty(String.valueOf(resultSet.getInt("QTE_ALERT"))));

                    dataTableArticles.add(data);
                }

                conn.close();

                table.setItems(dataTableArticles);

            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshArticles(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTableArticles.clear();

            String query = "SELECT ARTICLE.ID, ARTICLE.NAME, CATEGORY.NAME AS NAME_CAT, UNIT.NAME AS NAME_UNIT, ARTICLE.QTE_ALERT,\n" +
                    "(SELECT SUM(STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS QTE,\n" +
                    "(SELECT SUM((STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) * STORE_CARD.PRICE) FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ARTICLE.ID) AS PRICE\n" +
                    "FROM ARTICLE, CATEGORY, UNIT WHERE ARTICLE.ID_CAT = CATEGORY.ID AND ARTICLE.ID_UNIT = UNIT.ID AND ARTICLE.ARCHIVE = 0;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getString("NAME")));
                data.add( new SimpleStringProperty(resultSet.getString("NAME_CAT")));
                data.add( new SimpleStringProperty(resultSet.getString("NAME_UNIT")));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("QTE"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE") )));
                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("QTE_ALERT"))));

                dataTableArticles.add(data);
            }

            conn.close();

            table.setItems(dataTableArticles);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshGasoline(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTableGasoline.clear();

            String query = "SELECT * FROM GASOLINE_CARD WHERE ARCHIVE = 0;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getString("NUMBER")));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("BALANCE") )));
                data.add( new SimpleStringProperty(resultSet.getDate("LAST_RECHARGE_DATE").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                data.add( new SimpleStringProperty(resultSet.getDate("LAST_CONSUMPTION_DATE").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                dataTableGasoline.add(data);
            }

            conn.close();

            tableGasoline.setItems(dataTableGasoline);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        String idTab = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (idTab) {
            case "tabConsumed":
                refreshArticles();
            case "tabGasoline":
                refreshGasoline();
                break;
        }
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    @FXML
    void ActionSearch() {

        String idTab = tabPane.getSelectionModel().getSelectedItem().getId();
        switch (idTab) {
            case "tabConsumed":
                SearchArticles();
            case "tabGasoline":
                SearchGasoline();
                break;
        }
        // filtrer les données

    }

    private void SearchArticles(){
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

    private void SearchGasoline(){
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
            }else if (stringProperties.get(3).toString().contains(txtRecherche)) {
                return true;
            }  else return stringProperties.get(4).toString().contains(txtRecherche);
        });

        SortedList<List<StringProperty>> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(tableGasoline.comparatorProperty());
        tableGasoline.setItems(sortedList);
    }
}
