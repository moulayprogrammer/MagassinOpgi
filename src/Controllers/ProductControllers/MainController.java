package Controllers.ProductControllers;/*
package com.opgi.inventorymanagement.Controllers.ProductControllers;

import BddPackage.ProductOperation;
import Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.NodeOrientation;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class MainController implements Initializable {

    @FXML
    Label lbName;
    @FXML
    TextField tfRecherche;
    @FXML
    TableView<Product> table;
    @FXML
    TableColumn<Product,String> clName,clReference;
    @FXML
    TableColumn<Product,Integer> clId, clQte, clLimitQte;

    private final ObservableList<Product> dataTable = FXCollections.observableArrayList();
    private final ProductOperation operation = new ProductOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {
//        lbName.setText("قائمة المنتجات");

        clId.setCellValueFactory(new PropertyValueFactory<>("id"));
        clName.setCellValueFactory(new PropertyValueFactory<>("name"));
        clReference.setCellValueFactory(new PropertyValueFactory<>("reference"));
        clQte.setCellValueFactory(new PropertyValueFactory<>("qte"));
        clLimitQte.setCellValueFactory(new PropertyValueFactory<>("limitQte"));


        refresh();
    }

    @FXML
    private void ActionAdd(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProductViews/AddView.fxml"));
            BorderPane temp = loader.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(temp));
            stage.setMaximized(true);
            stage.getIcons().add(new Image("Images/logo.png"));
            stage.setTitle("مزرعة الجنوب");
            stage.initOwner(this.tfRecherche.getScene().getWindow());
            stage.showAndWait();

            refresh();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void tableClick(MouseEvent mouseEvent) {
        if ( mouseEvent.getClickCount() == 2 && mouseEvent.getButton().equals(MouseButton.PRIMARY) ){

            ActionUpdate();
        }
    }
    @FXML
    private void ActionUpdate(){

        Product product = table.getSelectionModel().getSelectedItem();
        if (product != null){
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProductViews/UpdateView.fxml"));
                BorderPane temp = loader.load();
                UpdateController controller = loader.getController();
                controller.Init(product);
                Stage stage = new Stage();
                stage.setScene(new Scene(temp));
                stage.setMaximized(true);
                stage.getIcons().add(new Image("Images/logo.png"));
                stage.setTitle("مزرعة الجنوب");
                stage.initOwner(this.tfRecherche.getScene().getWindow());
                stage.showAndWait();

                refresh();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير");
            alertWarning.setContentText("الرجاء اختيار منتج من اجل التعديل");
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionAddToArchive(){
        Product product = table.getSelectionModel().getSelectedItem();

        if (product != null){
            if (product.getQte() == 0) {
                try {

                    Alert alertConfirmation = new Alert(Alert.AlertType.CONFIRMATION);
                    alertConfirmation.setHeaderText("تاكيد الارشفة");
                    alertConfirmation.setContentText("هل انت متاكد من ارشفة المنتج");
                    alertConfirmation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                    alertConfirmation.initOwner(this.tfRecherche.getScene().getWindow());
                    Button okButton = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("موافق");

                    Button cancel = (Button) alertConfirmation.getDialogPane().lookupButton(ButtonType.CANCEL);
                    cancel.setText("الغاء");

                    alertConfirmation.showAndWait().ifPresent(response -> {
                        if (response == ButtonType.CANCEL) {
                            alertConfirmation.close();
                        } else if (response == ButtonType.OK) {
                            operation.AddToArchive(product);
                            refresh();
                        }
                    });

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }else {
                Alert alertInformation = new Alert(Alert.AlertType.INFORMATION);
                alertInformation.setHeaderText("لا تستطيع الارشفة ");
                alertInformation.setContentText("لا تستطيع ارشفة المنتج الحالي لانه متبقي في المخزن");
                alertInformation.initOwner(this.tfRecherche.getScene().getWindow());
                alertInformation.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
                Button okButton = (Button) alertInformation.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("موافق");
                alertInformation.showAndWait();
            }
        }else {
            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
            alertWarning.setHeaderText("تحذير ");
            alertWarning.setContentText("الرجاء اختيار منتج لارشفته");
            alertWarning.getDialogPane().setNodeOrientation(NodeOrientation.RIGHT_TO_LEFT);
            alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
            okButton.setText("موافق");
            alertWarning.showAndWait();
        }
    }

    @FXML
    private void ActionDeleteFromArchive(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ProductViews/ArchiveView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refresh();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void refresh(){
        ArrayList<Product> products = operation.getAll();
        dataTable.setAll(products);
        table.setItems(dataTable);
    }

    @FXML
    private void ActionRefresh(){
        clearRecherche();
        refresh();
    }

    private void clearRecherche(){
        tfRecherche.clear();
    }

    @FXML
    void ActionSearch() {
        // filtrer les données
        ObservableList<Product> dataProduct = table.getItems();
        FilteredList<Product> filteredData = new FilteredList<>(dataProduct, e -> true);
        String txtRecherche = tfRecherche.getText().trim();

        filteredData.setPredicate((Predicate<? super Product>) product -> {
            if (txtRecherche.isEmpty()) {
                //loadDataInTable();
                return true;
            } else if (product.getName().contains(txtRecherche)) {
                return true;
            } else if (product.getReference().contains(txtRecherche)) {
                return true;
            } else return String.valueOf(product.getLimitQte()).contains(txtRecherche);
        });

        SortedList<Product> sortedList = new SortedList<>(filteredData);
        sortedList.comparatorProperty().bind(table.comparatorProperty());
        table.setItems(sortedList);
    }
}
*/
