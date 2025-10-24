package tn.esprit.eventsproject.dto;

import tn.esprit.eventsproject.entities.Tache;

public class ParticipantDTO {
    private String nom;
    private String prenom;
    private Tache tache;

    // Getters and Setters
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public String getPrenom() { return prenom; }
    public void setPrenom(String prenom) { this.prenom = prenom; }

    public Tache getTache() { return tache; }
    public void setTache(Tache tache) { this.tache = tache; }
}