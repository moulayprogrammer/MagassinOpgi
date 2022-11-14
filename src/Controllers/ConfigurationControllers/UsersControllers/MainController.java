package Controllers.ConfigurationControllers.UsersControllers;

import BddPackage.UsersOperation;
import Models.Users;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class MainController implements Initializable {

    @FXML
    private PasswordField tfPassOld,tfPassNew,tfPassConfirm;
    @FXML
    private Label lbUser;

    private final UsersOperation operation = new UsersOperation();
    private Users users;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        this.users = operation.getAll().get(0);
        lbUser.setText(users.getUsername());
    }

    @FXML
    private void ActionChangePass(ActionEvent event) {
        try {
            String stOldPass =  tfPassOld.getText();
            String stNewPass =  tfPassNew.getText();
            String stConfirmPass =  tfPassConfirm.getText();

            if (!stOldPass.isEmpty() && !stNewPass.isEmpty() && !stConfirmPass.isEmpty()){
                if (operation.isExist(new Users(lbUser.getText(),stOldPass))){
                    if (stNewPass.equals(stConfirmPass)){
                        if (operation.update(new Users(lbUser.getText(),stNewPass),this.users)){

                            ((Stage)lbUser.getScene().getWindow()).close();

                            Stage primaryStage = new Stage();
                            Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/LoginView.fxml")));
                            primaryStage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
                            primaryStage.setScene(new Scene(root));
                            primaryStage.getIcons().add(new Image("Images/Picture1.png"));

                            primaryStage.setResizable(false);
                            primaryStage.initStyle(StageStyle.UTILITY);
                            primaryStage.show();

                        }else {
                            Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                            alertWarning.setHeaderText("ATTENTION");
                            alertWarning.setContentText("ERREUR INCONNUE");
                            alertWarning.initOwner(this.lbUser.getScene().getWindow());
                            Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                            okButton.setText("D'ACCORD");
                            alertWarning.showAndWait();
                        }
                    }else {
                        Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                        alertWarning.setHeaderText("ATTENTION ");
                        alertWarning.setContentText("Les mots de passe ne correspondent pas");
                        alertWarning.initOwner(this.lbUser.getScene().getWindow());
                        Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                        okButton.setText("D'ACCORD");
                        alertWarning.showAndWait();
                    }
                }else {
                    Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                    alertWarning.setHeaderText("ATTENTION ");
                    alertWarning.setContentText("Mot de passe incorrect");
                    alertWarning.initOwner(this.lbUser.getScene().getWindow());
                    Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                    okButton.setText("D'ACCORD");
                    alertWarning.showAndWait();
                }
            }else {
                Alert alertWarning = new Alert(Alert.AlertType.WARNING);
                alertWarning.setHeaderText("ATTENTION ");
                alertWarning.setContentText("Merci de remplir tous les champs");
                alertWarning.initOwner(this.lbUser.getScene().getWindow());
                Button okButton = (Button) alertWarning.getDialogPane().lookupButton(ButtonType.OK);
                okButton.setText("D'ACCORD");
                alertWarning.showAndWait();
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
