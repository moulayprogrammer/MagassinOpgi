package Controllers.ArticlesControllers;

import BddPackage.ArticlesOperation;
import BddPackage.ConnectBD;
import Models.Article;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

public class MagasinController implements Initializable {

    @FXML
    Label lbName,lbTotalQte,lbTotMontant;
    @FXML
    TableView<List<StringProperty>> table;
    @FXML
    TableColumn<List<StringProperty>,String> clId,clDate,clQteRest,clPrice,clMontant;

    private final ConnectBD connectBD = new ConnectBD();
    private Connection conn;
    private final ObservableList<List<StringProperty>> dataTable = FXCollections.observableArrayList();
    private final ArticlesOperation operation = new ArticlesOperation();

    private Article article;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        conn = connectBD.connect();

        clId.setCellValueFactory(data -> data.getValue().get(0));
        clDate.setCellValueFactory(data -> data.getValue().get(1));
        clQteRest.setCellValueFactory(data -> data.getValue().get(2));
        clPrice.setCellValueFactory(data -> data.getValue().get(3));
        clMontant.setCellValueFactory(data -> data.getValue().get(4));

    }

    public void Init(Article article){

        this.article = article;
        lbName.setText(article.getName());

        refresh();
    }

    @FXML
    private void ActionAnnuler(){
        ((Stage)lbName.getScene().getWindow()).close();
    }


    private void refresh(){
        try {
            if (conn.isClosed()) conn = connectBD.connect();
            dataTable.clear();
            int totQte = 0 ;
            double totMontant = 0.0;

            String query = "SELECT STORE_CARD.ID, STORE_CARD.QTE_STORED, STORE_CARD.QTE_CONSUMED, STORE_CARD.DATE_STORE, STORE_CARD.PRICE FROM STORE_CARD \n" +
                    "WHERE STORE_CARD.ID_ARTICLE = ? AND (STORE_CARD.QTE_STORED - STORE_CARD.QTE_CONSUMED) > 0 ORDER BY STORE_CARD.DATE_STORE ASC;";
            PreparedStatement preparedStmt = conn.prepareStatement(query);
            preparedStmt.setInt(1,this.article.getId());
            ResultSet resultSet = preparedStmt.executeQuery();
            while (resultSet.next()){

                List<StringProperty> data = new ArrayList<>();

                int qte = resultSet.getInt("QTE_STORED") - resultSet.getInt("QTE_CONSUMED");
                double montant = resultSet.getDouble("PRICE") * qte;

                totQte += qte;
                totMontant += montant;

                data.add( new SimpleStringProperty(String.valueOf(resultSet.getInt("ID"))));
                data.add( new SimpleStringProperty(resultSet.getDate("DATE_STORE").toLocalDate().format(DateTimeFormatter.ofPattern("dd-MM-yyyy"))));
                data.add( new SimpleStringProperty(String.valueOf(qte)));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", resultSet.getDouble("PRICE"))));
                data.add( new SimpleStringProperty(String.format(Locale.FRANCE, "%,.2f", montant)));

                dataTable.add(data);
            }

            conn.close();

            table.setItems(dataTable);
            lbTotalQte.setText(String.valueOf(totQte));
            lbTotMontant.setText(String.format(Locale.FRANCE, "%,.2f", totMontant));
        }catch (Exception e){
            e.printStackTrace();
        }
    }
}
