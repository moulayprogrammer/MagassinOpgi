package Controllers.ConfigurationControllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;

import java.net.URL;
import java.util.*;

public class MainController implements Initializable {

    @FXML
    TabPane tabPane;
    @FXML
    Tab tabEmployee,tabProviders,tabCategory,tabUnite,tabGasoline;

    @Override
    public void initialize(URL location, ResourceBundle resources) {

        ActionShowProviderScreen();

        tabPane.getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {
            oldTab.setContent(null);
            switch (newTab.getId()){
                case "tabEmployee":
                    ActionShowEmployeeScreen();
                    break;
                case "tabProviders":
                    ActionShowProviderScreen();
                    break;
                case "tabCategory":
                    ActionShowCategoryScreen();
                    break;
                case "tabUnite":
                    ActionShowUnitScreen();
                    break;
                case "tabGasoline":
                    ActionShowCarteGasolineScreen();
                    break;
            }
        });
    }

    private void ActionShowProviderScreen(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/ProviderViews/MainView.fxml"));
            BorderPane temp = loader.load();
            tabProviders.setContent(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ActionShowEmployeeScreen(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/EmployeeViews/MainView.fxml"));
            BorderPane temp = loader.load();
            tabEmployee.setContent(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ActionShowCategoryScreen(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/CategoryViews/MainView.fxml"));
            BorderPane temp = loader.load();
            tabCategory.setContent(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ActionShowUnitScreen(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/UniteViews/MainView.fxml"));
            BorderPane temp = loader.load();
            tabUnite.setContent(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ActionShowCarteGasolineScreen(){
        try {

            FXMLLoader loader = new FXMLLoader(getClass().getResource("/Views/ConfigurationViews/CarteGasolineViews/MainView.fxml"));
            BorderPane temp = loader.load();
            tabGasoline.setContent(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
