package com.dimitri.monapp.controllers;

import javafx.fxml.FXML;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;

import java.io.IOException;


public class MenuController {
    @FXML
    private javafx.scene.control.Button darkModeButton;

    @FXML
    public void initialize() {
        Scene scene = null;

        if (darkModeButton != null) {
            darkModeButton.sceneProperty().addListener((obs, oldScene, newScene) -> {
                if (newScene != null && com.dimitri.monapp.utils.ConfigUtils.isDarkMode()) {
                    String darkCss = getClass().getResource("/styles/dark-theme.css").toExternalForm();
                    if (!newScene.getStylesheets().contains(darkCss)) {
                        newScene.getStylesheets().add(darkCss);
                    }
                }
            });
        }
    }

    public void loadScene(String fxmlFile, ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double width = stage.getWidth();
        double height = stage.getHeight();

        FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlFile));
        Scene scene = new Scene(loader.load(), width, height);

        // Gestion des thèmes
        String darkCss = getClass().getResource("/styles/dark-theme.css").toExternalForm();
        String whiteCss = getClass().getResource("/styles/white-theme.css").toExternalForm();
        scene.getStylesheets().clear();
        if (com.dimitri.monapp.utils.ConfigUtils.isDarkMode()) {
            scene.getStylesheets().add(darkCss);
        } else {
            scene.getStylesheets().add(whiteCss);
        }

        stage.setScene(scene);
        stage.show();
    }

    public void handleListeEtudiants(ActionEvent event) throws IOException {
        loadScene("/etudiants.fxml", event);
    }

    public void handleAPropos(ActionEvent event) throws IOException {
        loadScene("/apropos.fxml", event);
    }

    public void handleSnake(ActionEvent event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        double width = stage.getWidth();
        double height = stage.getHeight();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("/snake.fxml"));
        Scene scene = new Scene(loader.load(), width, height);

        // Gestion des thèmes
        String darkCss = getClass().getResource("/styles/dark-theme.css").toExternalForm();
        String whiteCss = getClass().getResource("/styles/white-theme.css").toExternalForm();
        scene.getStylesheets().clear();
        if (com.dimitri.monapp.utils.ConfigUtils.isDarkMode()) {
            scene.getStylesheets().add(darkCss);
        } else {
            scene.getStylesheets().add(whiteCss);
        }

        // Récupérer le contrôleur Snake et lui passer la scène pour le clavier
        com.dimitri.monapp.controllers.SnakeController controller = loader.getController();
        controller.setupKeyHandler(scene);

        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void toggleDarkMode(ActionEvent event) {
        Scene scene = ((Node) event.getSource()).getScene();
        String darkCss = getClass().getResource("/styles/dark-theme.css").toExternalForm();
        String whiteCss = getClass().getResource("/styles/white-theme.css").toExternalForm();
        boolean enabled;
        if (scene.getStylesheets().contains(darkCss)) {
            scene.getStylesheets().remove(darkCss);
            scene.getStylesheets().remove(whiteCss);
            scene.getStylesheets().add(whiteCss);
            enabled = false;
        } else {
            scene.getStylesheets().remove(whiteCss);
            scene.getStylesheets().add(darkCss);
            enabled = true;
        }
        com.dimitri.monapp.utils.ConfigUtils.setDarkMode(enabled);
    }
}

