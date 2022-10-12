package Controllers.ConfigurationControllers.CategoryControllers;

import BddPackage.CategoryOperation;
import Models.Category;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    TextField tfName;
    @FXML
    Label lbAlert;
    @FXML
    Button btnUpdate;

    private final CategoryOperation operation = new CategoryOperation();
    private Category category;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void Init(Category category){
        this.category = category;
        tfName.setText(category.getName());
    }

    @FXML
    private void ActionAnnulled(){
        closeDialog(btnUpdate);
    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        String name = tfName.getText().trim();
        if (!name.isEmpty() ){

            Category category = new Category(name);

            boolean upd = update(category);
            if (upd ) {

                closeDialog(btnUpdate);
            }
            else {
                labelAlert("ERREUR INCONNUE");
            }
        }else {
            labelAlert("Merci de remplir tous les champs");
        }

    }

    private void labelAlert(String st){

        try {

            lbAlert.setText(st);
            FadeTransition ft = new FadeTransition(Duration.millis(3000), lbAlert);
            ft.setFromValue(0.0);
            ft.setToValue(1.0);
            ft.setCycleCount(2);
            ft.setAutoReverse(true);
            ft.play();

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean update(Category category) {
        boolean insert = false;
        try {
            insert = operation.update(category,this.category);
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
