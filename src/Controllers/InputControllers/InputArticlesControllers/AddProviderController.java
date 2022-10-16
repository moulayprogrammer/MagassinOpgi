package Controllers.InputControllers.InputArticlesControllers;

import BddPackage.ProviderOperation;
import Models.Provider;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.ResourceBundle;

public class AddProviderController implements Initializable {

    @FXML
    TextField tfName,tfAddress,tfActivity;
    @FXML
    Label lbAlert;
    @FXML
    Button btnInsert;



    private final ProviderOperation operation = new ProviderOperation();

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
        String address = tfAddress.getText().trim();
        String activity = tfAddress.getText().trim();


        if (!name.isEmpty()){

            Provider provider = new Provider();
            provider.setName(name);
            provider.setAddress(address);
            provider.setActivity(activity);

            boolean ins = insert(provider);
            if (ins) closeDialog(btnInsert);
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

    private boolean insert(Provider provider) {
        boolean insert = false;
        try {
            insert = operation.insert(provider);
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
