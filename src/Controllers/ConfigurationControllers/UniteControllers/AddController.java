package Controllers.ConfigurationControllers.UniteControllers;

import BddPackage.UnitOperation;
import Models.Unit;
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

public class AddController implements Initializable {

    @FXML
    TextField tfName;
    @FXML
    Label lbAlert;
    @FXML
    Button btnInsert;

    private final UnitOperation operation = new UnitOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        String name = tfName.getText().trim();
        if (!name.isEmpty() ){

            Unit unit = new Unit(name);

            boolean ins = insert(unit);
            if (ins ) {

                closeDialog(btnInsert);
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

    private boolean insert(Unit unit) {
        boolean insert = false;
        try {
            insert = operation.insert(unit);
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
