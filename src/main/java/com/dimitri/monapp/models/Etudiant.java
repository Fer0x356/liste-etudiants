package com.dimitri.monapp.models;

import jakarta.persistence.*;

@Entity
@Table(name = "etudiants")
public class Etudiant {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nom;
    private String prenom;

    @Basic
    @Column(name = "score_snake")
    private Integer scoreSnake = 0;

    public Integer getScoreSnake() { return scoreSnake; }
    public void setScoreSnake(Integer scoreSnake) { this.scoreSnake = scoreSnake; }

    public Etudiant(){
        this.scoreSnake = 0;
    }

    public Etudiant(String nom, String prenom){
        this.nom = nom;
        this.prenom = prenom;
        this.scoreSnake = 0;
    }

    // Getters et Setters
    public Long getId(){
        return id;
    }

    public String getNom(){
        return nom;
    }

    public String getPrenom(){
        return prenom;
    }

    public void setNom(String nom){
        this.nom = nom;
    }

    public void setPrenom(String prenom){
        this.prenom = prenom;
    }

    @Override
    public String toString() {
        return prenom + " " + nom;
    }
}
