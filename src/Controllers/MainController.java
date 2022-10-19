package Controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class MainController implements Initializable {
    @FXML
    private VBox vBox;
    @FXML
    private Label welcomeText;
    @FXML
    private Button btnArticles,btnInput,btnOutput,btnConfiguration;
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
            mainPane.setCenter(temp);
            setStyleDefaultAll();
            setStyleActive(btnArticles);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowInputScreen(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/InputViews/MainView.fxml"));
            BorderPane temp = loader.load();
            mainPane.setCenter(temp);
            setStyleDefaultAll();
            setStyleActive(btnInput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowOutputScreen(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/OutputViews/MainView.fxml"));
            BorderPane temp = loader.load();
            mainPane.setCenter(temp);
            setStyleDefaultAll();
            setStyleActive(btnOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void ShowConfigurationScreen(){

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/MainView.fxml"));
            BorderPane temp = loader.load();
            mainPane.setCenter(temp);
            setStyleDefaultAll();
            setStyleActive(btnConfiguration);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void setStyleDefaultAll(){
        setStyleDefault(btnArticles);
        setStyleDefault(btnInput);
        setStyleDefault(btnOutput);
        setStyleDefault(btnConfiguration);
    }

    private void setStyleActive(Button btn){
        btn.setId("side-bar-button-active");
        VBox.setMargin(btn,new Insets(0,-12,0,0));
    }

    private void setStyleDefault(Button btn){
        btn.setId("side-bar-button");
        VBox.setMargin(btn,new Insets(0,0,0,0));
    }
}