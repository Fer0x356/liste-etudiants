package com.dimitri.monapp.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.scene.Node;
import java.io.IOException;

public class AproposController {
    @FXML
    public void handleRetourMenu(javafx.event.Event event) throws IOException {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        com.dimitri.monapp.utils.SceneUtils.retourMenu(stage);
    }
}