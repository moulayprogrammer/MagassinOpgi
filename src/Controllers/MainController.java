package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private Label welcomeText;
    @FXML
    private Button btn;
    @FXML
    BorderPane mainPane;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        ShowArticlesScreen();
    }

    @FXML
    protected void onHelloButtonClick() {
        welcomeText.setText("Welcome to JavaFX Application!");
    }

    @FXML
    private void ShowArticlesScreen(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ArticlesViews/MainView.fxml"));
            BorderPane temp = loader.load();
            Controllers.ArticlesControllers.MainController controller = loader.getController();
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowInputScreen(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/MainView.fxml"));
            BorderPane temp = loader.load();
            Controllers.InputControllers.MainController controller = loader.getController();
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowOutputScreen(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/MainView.fxml"));
            BorderPane temp = loader.load();
//            Controllers.InputControllers.MainController controller = loader.getController();
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowConfigurationScreen(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/MainView.fxml"));
            BorderPane temp = loader.load();
//            Controllers.InputControllers.MainController controller = loader.getController();
            mainPane.setCenter(temp);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}