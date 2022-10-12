package Controllers.InputControllers;

import BddPackage.*;
import Models.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UpdateController implements Initializable {

    @FXML
    DatePicker dpBRDate,dpFactDate,dpBCDate;
    @FXML
    ComboBox<String> cbProvider;
    @FXML
    TextField tfRecherche,tfNumBR,tfNumFact,tfNumBC;
    @FXML
    Button btnUpdate;

    private final InputOperation operation = new InputOperation();
    private final StoreCardTempOperation storeCardTempOperation = new StoreCardTempOperation();
    private final ProviderOperation providerOperation = new ProviderOperation();
    private final ObservableList<String> comboProviderData = FXCollections.observableArrayList();
    private ArrayList<Provider> providers = new ArrayList<>();
    private Provider providerSelected;
    private Input inputSelected;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        refreshComboProviders();
    }

    public void Init(Input input){

        this.inputSelected =  input;
        this.providerSelected = providerOperation.get(input.getIdProvider());


        tfNumBR.setText(input.getNumber());
        tfNumFact.setText(input.getNumberFact());
        tfNumBC.setText(input.getNumberBC());

        dpBRDate.setValue(input.getDate());
        dpFactDate.setValue(input.getDateFact());
        dpBCDate.setValue(input.getDateBC());

        cbProvider.getSelectionModel().select(providerSelected.getName());
    }

    @FXML
    private void ActionComboProvider(){
        int index = cbProvider.getSelectionModel().getSelectedIndex();
        if (index >= 0 ) {
            providerSelected = providers.get(index);
        }
    }


    private void refreshComboProviders() {
        clearCombo();
        try {
            providers =  providerOperation.getAll();
            providers.forEach(provider -> {
                comboProviderData.add(provider.getName());
            });
            cbProvider.setItems(comboProviderData);
            if (providers.size() != 0) {
                providerSelected = providers.get(0);
                cbProvider.getSelectionModel().select(0);
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private void clearCombo(){
        cbProvider.getSelectionModel().clearSelection();
        comboProviderData.clear();
        providers.clear();
    }

    @FXML
    private void ActionAddProvider(){
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/ProviderViews/AddView.fxml"));
            DialogPane temp = loader.load();
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfRecherche.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            refreshComboProviders();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionSearchProvider(){
        try {

            Provider provider = new Provider();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/SelectProviderView.fxml"));
            DialogPane temp = loader.load();
            SelectProviderController controller = loader.getController();
            controller.Init(provider);
            Dialog<ButtonType> dialog = new Dialog<>();
            dialog.setDialogPane(temp);
            dialog.resizableProperty().setValue(false);
            dialog.initOwner(this.tfNumFact.getScene().getWindow());
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.CANCEL);
            Node closeButton = dialog.getDialogPane().lookupButton(ButtonType.CANCEL);
            closeButton.setVisible(false);
            dialog.showAndWait();

            if (provider.getName() != null){
                cbProvider.getSelectionModel().select(provider.getName());
                int index = cbProvider.getSelectionModel().getSelectedIndex();
                this.providerSelected = providers.get(index);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ActionAnnulledUpdate(){

        try {
            closeDialog(btnUpdate);
        }catch (Exception e){
            e.printStackTrace();
        }

    }

    @FXML
    void ActionUpdate(ActionEvent event) {

        try {
            String numBR = tfNumBR.getText().trim();
            String numFact = tfNumFact.getText().trim();
            String numBC = tfNumBC.getText().trim();

            LocalDate dateBR = dpBRDate.getValue();
            LocalDate dateFact = dpFactDate.getValue();
            LocalDate dateBC = dpBCDate.getValue();

            if (!numBR.isEmpty() && !numFact.isEmpty() && !numBC.isEmpty() && dateBR != null && dateFact != null && dateBC != null && providerSelected.getId() != -1){

                Input input = new Input();

                input.setNumber(numBR);
                input.setDate(dateBR);
                input.setNumberFact(numFact);
                input.setDateFact(dateFact);
                input.setNumberBC(numBC);
                input.setDateBC(dateBC);
                input.setIdProvider(this.providerSelected.getId());

                boolean ins = update(input);
                if (ins) {
                    updateStoreCardTemp(dateBR);
                    closeDialog(this.btnUpdate);

                } else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("ATTENTION");
                    alertWarning.setContentText("ERREUR INCONNUE");
                    alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertWarning.showAndWait();
                }

            } else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION ");
                alertWarning.setContentText("Merci de remplir tous les champs");
                alertWarning.initOwner(this.tfRecherche.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private boolean update(Input input) {
        boolean insert = false;
        try {
            insert = operation.update(input,this.inputSelected);
            return insert;
        }catch (Exception e){
            e.printStackTrace();
            return insert;
        }
    }

    private boolean updateStoreCardTemp(LocalDate date){
        boolean insert = false;
        try {
            insert = storeCardTempOperation.updateDateStoreByInput(date,this.inputSelected.getId());
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
