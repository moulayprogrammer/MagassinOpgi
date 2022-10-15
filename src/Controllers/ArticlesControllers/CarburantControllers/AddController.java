package Controllers.ArticlesControllers.CarburantControllers;

import BddPackage.ArticlesOperation;
import BddPackage.CategoryOperation;
import BddPackage.GasolineCardOperation;
import BddPackage.UnitOperation;
import Models.Article;
import Models.Category;
import Models.GasolineCard;
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
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class AddController implements Initializable {

    @FXML
    TextField tfNumber, tfBalance;
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
        String balance = tfBalance.getText().trim();


        if ( !number.isEmpty() && !balance.isEmpty() ){

            GasolineCard gasolineCard = new GasolineCard();
            gasolineCard.setNumber(number);
            gasolineCard.setBalance(Double.parseDouble(balance));
            gasolineCard.setLastRechargeDate(LocalDate.now());
            gasolineCard.setLastConsumptionDate(LocalDate.now());

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
