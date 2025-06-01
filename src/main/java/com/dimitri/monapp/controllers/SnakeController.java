package com.dimitri.monapp.controllers;

import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import java.util.List;
import java.util.ArrayList;
import javafx.animation.AnimationTimer;
import javafx.scene.control.Label;

import com.dimitri.monapp.dao.EtudiantDAO;
import com.dimitri.monapp.models.Etudiant;
import javafx.scene.control.TextInputDialog;
import java.util.Optional;

public class SnakeController {
    private int score;
    private int snakeDirection; // 0: haut, 1: droite, 2: bas, 3: gauche
    private int snakeLength; // Longueur initiale du serpent
    private final int gridSize = 30; // Taille de la grille
    private List<Point2D> snakePosition = new ArrayList<>();
    private List<Point2D> foodPosition = new ArrayList<>();
    private AnimationTimer timer;
    private int frameCount;
    private final int framesPerMove = 30;

    private final EtudiantDAO etudiantDAO = new EtudiantDAO();

    @FXML
    private javafx.scene.canvas.Canvas canvas;

    @FXML
    private Label scoreLabel;

    @FXML
    public void initialize() {
        score = 0;
        snakeLength = 1;
        frameCount = 0;
        snakeDirection = 1;

        snakePosition.clear();
        foodPosition.clear();

        for (int i = 0; i < snakeLength; i++) {
            snakePosition.add(new Point2D(5, 5 + i));
        }

        placeRandomFood(foodPosition, snakePosition);

        if (timer != null) timer.stop();
        timer = new AnimationTimer() {
            @Override
            public void handle(long now) {
                frameCount++;
                if (frameCount >= framesPerMove) {
                    updateGame();
                    frameCount = 0;
                }
                draw();
            }
        };
        timer.start();
    }

    public void updateGame() {
        moveSnake(snakePosition, snakeDirection);
        checkFoodCollision(snakePosition, foodPosition);
        checkWallCollision(snakePosition);
    }

    public void setupKeyHandler(javafx.scene.Scene scene) {
        scene.setOnKeyPressed(event -> {
            switch (event.getCode()) {
                case UP:
                    if (snakeDirection != 2) snakeDirection = 0;
                    break;
                case RIGHT:
                    if (snakeDirection != 3) snakeDirection = 1;
                    break;
                case DOWN:
                    if (snakeDirection != 0) snakeDirection = 2;
                    break;
                case LEFT:
                    if (snakeDirection != 1) snakeDirection = 3;
                    break;
                default:
                    break;
            }
        });
    }

    public void placeRandomFood(List<Point2D> foodPosition, List<Point2D> snakePosition) {
        int x, y;
        do {
            x = (int) (Math.random() * gridSize);
            y = (int) (Math.random() * gridSize);
        } while (isSnakePosition(snakePosition, x, y));
        foodPosition.clear();
        foodPosition.add(new Point2D(x, y));
    }

    public boolean isSnakePosition(List<Point2D> snakePosition, int x, int y) {
        for (Point2D point : snakePosition) {
            if (point.getX() == x && point.getY() == y) {
                return true;
            }
        }
        return false;
    }

    public void checkFoodCollision(List<Point2D> snakePosition, List<Point2D> foodPosition) {
        Point2D head = snakePosition.get(0);
        Point2D food = foodPosition.get(0);

        if (head.equals(food)) {
            score++;
            snakeLength++;
            // Ajouter un segment à la fin (sera mis à jour au prochain déplacement)
            snakePosition.add(snakePosition.get(snakePosition.size() - 1));
            placeRandomFood(foodPosition, snakePosition);
        }
    }

    public void checkWallCollision(List<Point2D> snakePosition) {
        Point2D head = snakePosition.get(0);
        if (head.getX() < 0 || head.getY() < 0 || head.getX() >= gridSize || head.getY() >= gridSize) {
            if (timer != null) timer.stop();
            javafx.application.Platform.runLater(() -> {
                demanderNomEtEnregistrerScore();
                javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.CONFIRMATION);
                alert.setTitle("Fin de partie");
                alert.setHeaderText("Game Over ! Voulez-vous rejouer ?");
                alert.getButtonTypes().setAll(
                        javafx.scene.control.ButtonType.YES, // Rejouer
                        javafx.scene.control.ButtonType.NO   // Menu
                );
                java.util.Optional<javafx.scene.control.ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == javafx.scene.control.ButtonType.YES) {
                    initialize();
                } else {
                    // Retour au menu principal proprement
                    javafx.stage.Stage stage = (javafx.stage.Stage) canvas.getScene().getWindow();
                    try {
                        com.dimitri.monapp.utils.SceneUtils.retourMenu(stage);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    private void demanderNomEtEnregistrerScore() {
        TextInputDialog dialogNom = new TextInputDialog();
        dialogNom.setTitle("Fin de partie");
        dialogNom.setHeaderText("Entrez votre nom:");
        Optional<String> nomResult = dialogNom.showAndWait();
        if (!nomResult.isPresent()) return;

        TextInputDialog dialogPrenom = new TextInputDialog();
        dialogPrenom.setTitle("Fin de partie");
        dialogPrenom.setHeaderText("Entrez votre prénom:");
        Optional<String> prenomResult = dialogPrenom.showAndWait();
        if (!prenomResult.isPresent()) return;

        String nom = nomResult.get().trim();
        String prenom = prenomResult.get().trim();

        Etudiant etudiant = etudiantDAO.findByNomPrenom(nom, prenom);
        if (etudiant == null) {
            etudiant = new Etudiant(nom, prenom);
        }
        if (etudiant.getScoreSnake() < score) {
            etudiant.setScoreSnake(score);
        } else {
            // Si le score n'est pas meilleur, on ne met pas à jour
            return;
        }
        if (etudiant.getId() == null) {
            etudiantDAO.save(etudiant);
        } else {
            etudiantDAO.update(etudiant);
        }
    }

    public void moveSnake(List<Point2D> snakePosition, int direction) {
        Point2D head = snakePosition.get(0);
        Point2D newHead;
        switch (direction) {
            case 0: newHead = new Point2D(head.getX(), head.getY() - 1); break; // Haut
            case 1: newHead = new Point2D(head.getX() + 1, head.getY()); break; // Droite
            case 2: newHead = new Point2D(head.getX(), head.getY() + 1); break; // Bas
            case 3: newHead = new Point2D(head.getX() - 1, head.getY()); break; // Gauche
            default: return;
        }
        snakePosition.add(0, newHead);
        while (snakePosition.size() > snakeLength) {
            snakePosition.remove(snakePosition.size() - 1);
        }
    }

    private void draw() {
        var gc = canvas.getGraphicsContext2D();
        double cellSize = canvas.getWidth() / gridSize;

        // Efface le canvas
        gc.clearRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // --- Dessine la grille ---
        gc.setStroke(javafx.scene.paint.Color.web("#e0e0e0")); // gris doux
        gc.setLineWidth(0.5);
        for (int i = 0; i <= gridSize; i++) {
            // Lignes verticales
            gc.strokeLine(i * cellSize, 0, i * cellSize, canvas.getHeight());
            // Lignes horizontales
            gc.strokeLine(0, i * cellSize, canvas.getWidth(), i * cellSize);
        }

        // --- Dessine la bordure ---
        gc.setStroke(javafx.scene.paint.Color.web("#888")); // gris foncé
        gc.setLineWidth(2);
        gc.strokeRect(0, 0, canvas.getWidth(), canvas.getHeight());

        // --- Dessine la nourriture ---
        for (Point2D food : foodPosition) {
            gc.setFill(javafx.scene.paint.Color.RED);
            gc.fillRect(food.getX() * cellSize, food.getY() * cellSize, cellSize, cellSize);
        }

        // --- Dessine le serpent ---
        gc.setFill(javafx.scene.paint.Color.GREEN);
        for (Point2D p : snakePosition) {
            gc.fillRect(p.getX() * cellSize, p.getY() * cellSize, cellSize, cellSize);
        }

        if (scoreLabel != null) {
            scoreLabel.setText("Score : " + score);
        }
    }
}

