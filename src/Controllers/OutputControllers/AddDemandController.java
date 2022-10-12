package Controllers.OutputControllers;

import BddPackage.ConnectBD;
import Models.Article;
import Models.ComponentOutput;
import Models.StoreCard;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class AddDemandController implements Initializable {

    @FXML
    TextField tfQteDem,tfQteServ,tfTotal;
    @FXML
    private Button btnAdd;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;

    private List<StoreCard> store = new ArrayList<>();
    private List<ComponentOutput> componentOutputs = new ArrayList<>();
    private Article selectedArticle;
    private double price;
    private double cost;
    private int qte;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        conn = connectBD.connect();

        tfQteServ.textProperty().addListener((observable, oldValue, newValue) -> {
            if (newValue.isEmpty()) tfQteServ.setText(String.valueOf(qte));
            else Count();
        });

    }

    public void Init(Article article, List<ComponentOutput> componentOutputs, List<StoreCard> store){

        this.store = store;
        this.componentOutputs = componentOutputs;
        this.selectedArticle = article;

//        tfQteServ.setText(String.valueOf(selectedArticle.getQte()));
    }

    private void Count(){
        String stQte = tfQteServ.getText().trim();

        if (!stQte.isEmpty() && !stQte.equals("0")) {
            qte = Integer.parseInt(stQte);
            if (qte <= this.selectedArticle.getQte()) {
                try {
                    if (conn.isClosed()) conn = connectBD.connect();
                    this.store.clear();
                    this.componentOutputs.clear();
                    price = 0.0;
                    cost = 0.0;

                    try {

                        String query = "SELECT * FROM STORE_CARD WHERE STORE_CARD.ID_ARTICLE = ? AND (STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) != 0 ORDER BY DATE_STORE ASC;";
                        PreparedStatement preparedStmt = conn.prepareStatement(query);
                        preparedStmt.setInt(1, this.selectedArticle.getId());
                        ResultSet resultSet = preparedStmt.executeQuery();

                        while (resultSet.next()) {

                            int id = resultSet.getInt("ID");
                            int idInput = resultSet.getInt("ID_INPUT");
                            int idArt = resultSet.getInt("ID_ARTICLE");
                            int qteArt = resultSet.getInt("QTE_STORED") - resultSet.getInt("QTE_CONSUMED");
                            double priceArt = resultSet.getDouble("PRICE");

                            StoreCard storeCard = new StoreCard();
                            storeCard.setId(id);
                            storeCard.setIdInput(idInput);
                            storeCard.setIdArticle(idArt);
                            storeCard.setPrice(priceArt);

                            ComponentOutput output = new ComponentOutput();
                            output.setIdArt(idArt);
                            output.setIdStore(id);

                            if (qteArt >= qte) {
                                price += (priceArt * qte);

                                storeCard.setQteConsumed(qte);
                                output.setQteServ(qte);
                                this.store.add(storeCard);
                                this.componentOutputs.add(output);
                                break;
                            } else {
                                price += (priceArt * qteArt);
                                qte -= qteArt;

                                storeCard.setQteConsumed(qteArt);
                                output.setQteServ(qteArt);
                                this.store.add(storeCard);
                                this.componentOutputs.add(output);
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    qte = Integer.parseInt(stQte);
                    tfTotal.setText(String.format(Locale.FRANCE, "%,.2f", price));

                    conn.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }


            } else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("Quantité ");
                alertWarning.setContentText("Quantité non disponible");
                alertWarning.initOwner(this.btnAdd.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        }
    }

    @FXML
    private void ActionInsert(){

        String stQteDem = tfQteDem.getText().trim();
        String stQteServ = tfQteServ.getText().trim();

        if (!stQteDem.isEmpty() && !stQteServ.equals("0") && store.size() != 0 && componentOutputs.size() != 0) {

            for (ComponentOutput componentOutput : componentOutputs){
                componentOutput.setQteDem(Integer.parseInt(stQteDem));
            }

            ActionAnnulledAdd();
        }
    }

    @FXML
    private void ActionAnnulledAdd(){
        closeDialog(btnAdd);
    }

    private void closeDialog(Button btn) {
        ((Stage)btn.getScene().getWindow()).close();
    }
}
