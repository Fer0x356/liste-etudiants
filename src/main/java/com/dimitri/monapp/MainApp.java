package com.dimitri.monapp;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.application.Platform;

public class MainApp extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/menu.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 800, 600); // largeur 800, hauteur 600
        primaryStage.setTitle("Liste etudiants");
        primaryStage.setScene(scene);
        primaryStage.show();
        Platform.runLater(primaryStage::centerOnScreen);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
