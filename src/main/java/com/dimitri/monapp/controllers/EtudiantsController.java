package com.dimitri.monapp.controllers;

import com.dimitri.monapp.dao.EtudiantDAO;
import com.dimitri.monapp.models.Etudiant;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import javafx.util.converter.IntegerStringConverter;

import java.io.IOException;

public class EtudiantsController {

    @FXML
    private TableView<Etudiant> tableEtudiants;
    @FXML
    private TableColumn<Etudiant, Long> colId;
    @FXML
    private TableColumn<Etudiant, String> colNom;
    @FXML
    private TableColumn<Etudiant, String> colPrenom;
    @FXML
    private TableColumn<Etudiant, Integer> colScoreSnake;

    private final EtudiantDAO etudiantDAO = new EtudiantDAO();

    @FXML
    public void initialize() {
        // Liaison des colonnes
        colId.setCellValueFactory(new PropertyValueFactory<>("id"));
        colNom.setCellValueFactory(new PropertyValueFactory<>("nom"));
        colPrenom.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        colScoreSnake.setCellValueFactory(new PropertyValueFactory<>("scoreSnake"));

        // Cellules centrées et éditables
        colId.setCellFactory(col -> centeredCellFactory());
        colNom.setCellFactory(col -> centeredEditableCellFactory(new StringConverter<String>() {
            @Override public String toString(String s) { return s; }
            @Override public String fromString(String s) { return s; }
        }));
        colPrenom.setCellFactory(col -> centeredEditableCellFactory(new StringConverter<String>() {
            @Override public String toString(String s) { return s; }
            @Override public String fromString(String s) { return s; }
        }));
        colScoreSnake.setCellFactory(col -> centeredEditableCellFactory(new IntegerStringConverter()));

        // Commit pour édition du nom, prenom et score
        colNom.setOnEditCommit(event -> {
            Etudiant etudiant = event.getRowValue();
            etudiant.setNom(event.getNewValue());
            etudiantDAO.update(etudiant);
            tableEtudiants.refresh();
        });

        colPrenom.setOnEditCommit(event -> {
            Etudiant etudiant = event.getRowValue();
            etudiant.setPrenom(event.getNewValue());
            etudiantDAO.update(etudiant);
            tableEtudiants.refresh();
        });
        colScoreSnake.setOnEditCommit(event -> {
            Etudiant etudiant = event.getRowValue();
            etudiant.setScoreSnake(event.getNewValue());
            etudiantDAO.update(etudiant);
            tableEtudiants.refresh();
        });

        // Double-clic sur ligne vide = nouvel étudiant
        tableEtudiants.setRowFactory(tv -> {
            TableRow<Etudiant> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 2 && row.isEmpty()) {
                    Etudiant etudiant = new Etudiant("", "");
                    etudiantDAO.save(etudiant);
                    tableEtudiants.getItems().add(etudiant);
                    tableEtudiants.scrollTo(etudiant);
                    tableEtudiants.getSelectionModel().select(etudiant);
                    tableEtudiants.edit(tableEtudiants.getItems().indexOf(etudiant), colNom);
                }
            });
            return row;
        });

        // Raccourcis clavier pour retour menu
        tableEtudiants.sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                ajouterEcouteursRetour(newScene);
            }
        });
        tableEtudiants.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                try { handleRetourMenu(event); } catch (IOException e) { e.printStackTrace(); }
            }
        });

        // Chargement des données
        ObservableList<Etudiant> etudiants = FXCollections.observableArrayList(etudiantDAO.findAll());
        tableEtudiants.setEditable(true);
        tableEtudiants.setItems(etudiants);
    }

    // Cellule centrée non éditable (pour l'ID)
    private <T> TableCell<Etudiant, T> centeredCellFactory() {
        return new TableCell<Etudiant, T>() {
            @Override
            protected void updateItem(T item, boolean empty) {
                super.updateItem(item, empty);
                setText(empty || item == null ? null : String.valueOf(item));
                setAlignment(Pos.CENTER);
            }
        };
    }

    // Cellule centrée éditable (pour String ou Integer)
    private <T> TextFieldTableCell<Etudiant, T> centeredEditableCellFactory(StringConverter<T> converter) {
        return new TextFieldTableCell<Etudiant, T>(converter) {
            @Override
            public void startEdit() {
                if (!isEmpty()) {
                    super.startEdit();
                } else {
                    setText("");
                    super.startEdit();
                }
            }
            {
                setAlignment(Pos.CENTER);
            }
        };
    }

    public void save(javafx.event.ActionEvent event) {
        Etudiant etudiant = new Etudiant("", "");
        etudiantDAO.save(etudiant);
        tableEtudiants.getItems().add(etudiant);
    }

    public void delete(javafx.event.ActionEvent event) {
        Etudiant etudiant = tableEtudiants.getSelectionModel().getSelectedItem();
        if (etudiant != null) {
            etudiantDAO.delete(etudiant);
            tableEtudiants.getItems().remove(etudiant);
        }
    }

    private void ajouterEcouteursRetour(Scene scene) {
        scene.setOnKeyPressed(event -> {
            if (event.getCode() == javafx.scene.input.KeyCode.ESCAPE) {
                try { handleRetourMenu(event); } catch (IOException e) { e.printStackTrace(); }
            }
        });
        scene.setOnMousePressed(event -> {
            if (event.getButton() == javafx.scene.input.MouseButton.BACK) {
                try { handleRetourMenu(event); } catch (IOException e) { e.printStackTrace(); }
            }
        });
    }

    @FXML
    public void handleRetourMenu(javafx.event.Event event) throws IOException {
        Stage stage = (Stage) tableEtudiants.getScene().getWindow();
        com.dimitri.monapp.utils.SceneUtils.retourMenu(stage);
    }
}