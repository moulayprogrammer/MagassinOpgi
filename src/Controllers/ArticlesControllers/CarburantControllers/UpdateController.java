package Controllers.ArticlesControllers.CarburantControllers;

import BddPackage.GasolineCardOperation;
import Models.GasolineCard;
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
import java.time.LocalDate;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    TextField tfNumber, tfBalance;
    @FXML
    Label lbAlert;
    @FXML
    Button btnUpdate;

    private final GasolineCardOperation operation = new GasolineCardOperation();

    private GasolineCard gasolineCard;
    @Override
    public void initialize(URL location, ResourceBundle resources) {
    }

    public void Init(GasolineCard gasolineCard){
        this.gasolineCard = gasolineCard;
        tfNumber.setText(gasolineCard.getNumber());
    }

    @FXML
    private void ActionAnnul(){
        closeDialog(btnUpdate);
    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        String number = tfNumber.getText().trim();

        if ( !number.isEmpty() ){

            GasolineCard gasolineCard = new GasolineCard();
            gasolineCard.setNumber(number);

            boolean ins = update(gasolineCard);
            if (ins){
                closeDialog(btnUpdate);
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

    private boolean update(GasolineCard gasolineCard) {
        boolean update = false;
        try {
            update = operation.update(gasolineCard,this.gasolineCard);
            return update;
        }catch (Exception e){
            e.printStackTrace();
            return update;
        }
    }



    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
