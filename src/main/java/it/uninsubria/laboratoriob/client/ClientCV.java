package it.uninsubria.laboratoriob.client;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class ClientCV extends Application {
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(ClientCV.class.getResource("fxml/Avvio.fxml"));
        Scene scene = new Scene(fxmlLoader.load());
        stage.setTitle("Centri Vaccinali");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}