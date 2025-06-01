package com.dimitri.monapp.utils;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class SceneUtils {
    public static void retourMenu(Stage stage) throws IOException {
        double width = stage.getWidth();
        double height = stage.getHeight();
        FXMLLoader loader = new FXMLLoader(SceneUtils.class.getResource("/menu.fxml"));
        Scene scene = new Scene(loader.load(), width, height);
        stage.setScene(scene);
        stage.setWidth(width);
        stage.setHeight(height);
        stage.show();
    }
}