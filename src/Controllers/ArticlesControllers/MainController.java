package Controllers.ArticlesControllers;

import BddPackage.ArticlesOperation;
import BddPackage.CategoryOperation;
import BddPackage.ConnectBD;
import BddPackage.GasolineCardOperation;
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
    TextField tfRecherche;
    @FXML
    ComboBox<String> cbCategory,cbFilter;
    @FXML
    Label lbCategory;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clName,clCategory,clUnit,clQte,clPrice,clQteAlert;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private final ArticlesOperation articlesOperation = new ArticlesOperation();
    private final CategoryOperation categoryOperation = new CategoryOperation();

    private final ObservableList<List<StringProperty>> dataTableArticles = FXCollections.observableArrayList();
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

        refreshArticles();
        refreshComboCategory();

        comboFilterData.addAll("Tout","Catégorie","Article en Qte Alert","Article fini");
        cbFilter.setItems(comboFilterData);
        cbFilter.getSelectionModel().select(0);

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
    private void tableClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            List<StringProperty> data = table.getSelectionModel().getSelectedItem();

            if (data != null){
                try {
                    Article article = articlesOperation.get(Integer.parseInt(data.get(0).getValue()));

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/MagasinView.fxml"));
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
            List<StringProperty> data = table.getSelectionModel().getSelectedItem();

            if (data != null) {
                try {
                    Article article = articlesOperation.get(Integer.parseInt(data.get(0).getValue()));

                    FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/UpdateView.fxml"));
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
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    @FXML
    private void ActionAddToArchive(){
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

    @FXML
    private void ActionDeleteFromArchive(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/ArchiveView.fxml"));
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


    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refreshArticles();
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    @FXML
    void ActionSearch() {
        SearchArticles();

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
}
