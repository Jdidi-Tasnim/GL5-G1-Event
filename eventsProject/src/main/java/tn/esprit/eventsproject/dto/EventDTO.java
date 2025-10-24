package tn.esprit.eventsproject.dto;

import java.time.LocalDate;

public class EventDTO {
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private float cout;

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public LocalDate getDateDebut() { return dateDebut; }
    public void setDateDebut(LocalDate dateDebut) { this.dateDebut = dateDebut; }

    public LocalDate getDateFin() { return dateFin; }
    public void setDateFin(LocalDate dateFin) { this.dateFin = dateFin; }

    public float getCout() { return cout; }
    public void setCout(float cout) { this.cout = cout; }
}