package Controllers.ConfigurationControllers.ProviderControllers;

import BddPackage.ProviderOperation;
import Models.Provider;
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
    TextField tfName,tfAddress,tfActivity;
    @FXML
    Label lbAlert;
    @FXML
    Button btnInsert;



    private final ProviderOperation operation = new ProviderOperation();
    private Provider provider;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    public void Init(Provider provider){
        this.provider = provider;

        tfName.setText(provider.getName());
        tfAddress.setText(provider.getAddress());
        tfActivity.setText(provider.getActivity());
    }

    @FXML
    private void ActionAnnulledUpd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        String name = tfName.getText().trim();
        String address = tfAddress.getText().trim();
        String activity = tfActivity.getText().trim();


        if (!name.isEmpty()){

            Provider provider = new Provider();
            provider.setName(name);
            provider.setAddress(address);
            provider.setActivity(activity);

            boolean upd = update(provider);
            if (upd) closeDialog(btnInsert);
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

    private boolean update(Provider provider) {
        boolean upd = false;
        try {
            upd = operation.update(provider,this.provider);
            return upd;
        }catch (Exception e){
            e.printStackTrace();
            return upd;
        }
    }


    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
