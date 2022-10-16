package Controllers.InputControllers.InputArticlesControllers;

import BddPackage.ConnectBD;
import Models.Article;
import Models.ComponentInput;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.util.Locale;
import java.util.ResourceBundle;

public class UpdatePorchesController implements Initializable {

    @FXML
    TextField tfQte,tfPrice,tfTotal;
    @FXML
    private Button btnPorch;
    @FXML
    private Label lbArticle,lbAlert;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private Article article;
    private ComponentInput componentInput;

    private double price;
    private double total;
    private int qte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        tfQte.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) Count();
            else {
                qte = 0;
                Count();
            }
        });

        tfPrice.textProperty().addListener((observable, oldValue, newValue) -> {
            if (!newValue.isEmpty()) Count();
            else {
                price = 0;
                Count();
            }
        });
    }

    public void Init(Article article, ComponentInput componentInput){

        this.article = article;
        this.componentInput = componentInput;

        lbArticle.setText(article.getName());

        qte = componentInput.getQte();
        price = componentInput.getPrice();
        total = componentInput.getQte() * componentInput.getPrice();

        tfQte.setText(String.valueOf(qte));
        tfPrice.setText(String.valueOf(price));

        Count();
    }

    private void Count(){
        try {
            String stQte = tfQte.getText().trim();
            String stPrice = tfPrice.getText().trim();
            if (!stQte.isEmpty() && !stPrice.isEmpty()) {

                qte = Integer.parseInt(stQte);
                price = Double.parseDouble(stPrice);

                total = qte * price;

                tfTotal.setText(String.format(Locale.FRANCE, "%,.2f",total));

            }else {
                tfTotal.setText("");
            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    private void ActionInsert(){

        String stQte = tfQte.getText().trim();
        String stPriceU = tfPrice.getText().trim();

        if (!stQte.isEmpty() && !stQte.equals("0") && !stPriceU.isEmpty() && !stPriceU.equals("0")) {
            int qte = Integer.parseInt(stQte);
            double PriceU = Double.parseDouble(stPriceU);

            this.componentInput.setIdArticle(this.article.getId());
            this.componentInput.setPrice(PriceU);
            this.componentInput.setQte(qte);

            ActionAnnulledAdd();
        }
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnPorch);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
