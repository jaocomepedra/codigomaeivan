package tela.main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.Parent;
import javafx.stage.Stage;

public class App extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        // 🔄 MODIFICADO: Agora carrega a tela de login primeiro
        Parent root = FXMLLoader.load(getClass().getResource("/telas/view/TelaLogin.fxml"));
        Scene scene = new Scene(root, 400, 500);
        scene.getStylesheets().add(getClass().getResource("/globalStyle/style.css").toExternalForm());
        primaryStage.setTitle("Flor & Ser - Login");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}