package Controllers.ConfigurationControllers.CarteGasolineControllers;

import BddPackage.GasolineCardOperation;
import Models.GasolineCard;
import javafx.animation.FadeTransition;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    TextField tfNumber;
    @FXML
    Label lbAlert;
    @FXML
    Button btnInsert;

    private final GasolineCardOperation operation = new GasolineCardOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnInsert);
    }

    @FXML
    void ActionInsert(ActionEvent event) {

        String number = tfNumber.getText().trim();


        if ( !number.isEmpty() ){

            GasolineCard gasolineCard = new GasolineCard();
            gasolineCard.setNumber(number);
            gasolineCard.setLastBalance(0.0);
            gasolineCard.setLastRechargeDate(LocalDate.now());

            boolean ins = insert(gasolineCard);
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

    private boolean insert(GasolineCard gasolineCard) {
        boolean insert = false;
        try {
            insert = operation.insert(gasolineCard);
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
