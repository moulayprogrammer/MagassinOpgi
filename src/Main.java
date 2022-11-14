import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.util.Objects;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{

        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/Views/LoginView.fxml")));
        primaryStage.setTitle("Gestion de l'inventaire OPGI Tamanrasset");
        primaryStage.setScene(new Scene(root));
        primaryStage.getIcons().add(new Image("Images/LogoOpgi.png"));

        primaryStage.setResizable(false);
        primaryStage.initStyle(StageStyle.UTILITY);
        primaryStage.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
