package Controllers;

import BddPackage.UsersOperation;
import Models.Users;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    String errorMessage = String.format("-fx-text-fill: RED;");
    String errorStyle = String.format("-fx-border-color: RED; -fx-border-width: 2; -fx-border-radius: 5;");

    // Import the application's controls
    @FXML
    private Label invalidLoginCredentials;
    @FXML
    private Button cancelButton;
    @FXML
    private TextField loginUsernameTextField;
    @FXML
    private TextField loginPasswordPasswordField;

    private final UsersOperation operation = new UsersOperation();

    @Override
    public void initialize(URL location, ResourceBundle resources) {

    }

    // Creation of methods which are activated on events in the forms
    @FXML
    protected void onCancelButtonClick() {
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    protected void onLoginButtonClick() {
        if (loginUsernameTextField.getText().isEmpty() || loginPasswordPasswordField.getText().isEmpty()) {
            invalidLoginCredentials.setText("Merci de remplir tous les champs");
            invalidLoginCredentials.setStyle(errorMessage);

            if (loginUsernameTextField.getText().isEmpty()) {
                loginUsernameTextField.setStyle(errorStyle);
            } else if (loginPasswordPasswordField.getText().isEmpty()) {
                loginPasswordPasswordField.setStyle(errorStyle);
            }
        } else if (operation.isExist(new Users(loginUsernameTextField.getText().trim(),loginPasswordPasswordField.getText()))){

            onCancelButtonClick();
            try {

                Stage primaryStage = new Stage();
                Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/MainView.fxml")));
                primaryStage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
                primaryStage.setScene(new Scene(root));
                primaryStage.getIcons().add(new Image("Images/Picture1.png"));

                primaryStage.setMaximized(true);
                primaryStage.show();
            }catch (Exception e){
                e.printStackTrace();
            }
        }else {
            invalidLoginCredentials.setText("Veuillez vous assurer que votre nom d'utilisateur et votre mot de passe");
            invalidLoginCredentials.setStyle(errorMessage);
        }
    }

}
