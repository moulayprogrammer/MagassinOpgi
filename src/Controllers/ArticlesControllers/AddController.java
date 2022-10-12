package Controllers.ArticlesControllers;

import BddPackage.*;
import Models.Article;
import Models.Category;
import Models.Unit;
import javafx.animation.FadeTransition;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    TextField tfName,tfQteAlert;
    @FXML
    Label lbAlert;
    @FXML
    ComboBox<String> cbCategory,cbUnit;
    @FXML
    Button btnInsert;

    private final ArticlesOperation operation = new ArticlesOperation();
    private final CategoryOperation categoryOperation = new CategoryOperation();
    private final UnitOperation unitOperation = new UnitOperation();
    private final ObservableList<String> comboCategoryData = FXCollections.observableArrayList();
    private final ObservableList<String> comboUnitData = FXCollections.observableArrayList();
    ArrayList<Category> categories;
    ArrayList<Unit> units;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        refreshComboCategory();
        refreshComboUnit();

    }

    private void refreshComboCategory() {
        comboCategoryData.clear();
        try {

            this.categories = categoryOperation.getAll();
            for (Category category: categories){
                comboCategoryData.add(category.getName());
            }

            cbCategory.setItems(comboCategoryData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void refreshComboUnit() {
        comboUnitData.clear();
        try {

            this.units = unitOperation.getAll();
            for (Unit unit: units){
                comboUnitData.add(unit.getName());
            }

            cbUnit.setItems(comboUnitData);

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAddCategory(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/AddCategoryView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.btnInsert.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshComboCategory();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAddUnit(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/AddUnitView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.btnInsert.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshComboUnit();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        String name = tfName.getText().trim();
        String qteAlert = tfQteAlert.getText().trim();

        int categoryIndex = cbCategory.getSelectionModel().getSelectedIndex();
        int unitIndex = cbUnit.getSelectionModel().getSelectedIndex();

        if ( !name.isEmpty() &&  categoryIndex != -1 &&  unitIndex != -1){

            Article article = new Article();
            article.setName(name);
            article.setIdCategory(this.categories.get(categoryIndex).getId());
            article.setIdUnit(this.units.get(unitIndex).getId());
            if (!qteAlert.isEmpty()) article.setQteAlert(Integer.parseInt(qteAlert));

            boolean ins = insert(article);
            if (ins){
                closeDialog(btnInsert);
            }else {
                labelAlert("ERREUR INCONNUE");
            }

        }else {
        labelAlert("Merci de remplir tous les champs");
        }

    }

    private void labelAlert(String st){

        try {

            lbAlert.setText(st);
            FadeTransition ft = new FadeTransition(Duration.millis(4000), lbAlert);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean insert(Article article) {
        boolean insert = false;
        try {
            insert = operation.insert(article);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }



    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
