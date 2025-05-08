package com.svalero.clashstatsfx;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;

import java.io.IOException;

/*Carga la interfaz principal desde un archivo FXML y lanza la ventana principal.*/
public class MainApp extends Application {

    /*Punto de entrada de JavaFX. Carga la vista desde FXML y configura el escenario.*/
    @Override
    public void start(Stage stage) {    //escenario principal de la aplicación
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(MainApp.class.getResource("main-view.fxml"));
            Scene scene = new Scene(fxmlLoader.load(), 800, 600);

            stage.setTitle("ClashStatsFX - Tu dosis de datos épicos");
            stage.setScene(scene);
            stage.show();

        } catch (IOException e) {
            System.err.println("No se pudo cargar la interfaz: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void main(String[] args) {
        launch();
    }
}
