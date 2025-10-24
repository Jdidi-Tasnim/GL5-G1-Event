package tn.esprit.eventsproject.dto;

public class LogisticsDTO {
    private String description;
    private boolean reserve;
    private float prixUnit;
    private int quantite;

    // Getters and Setters
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public boolean isReserve() { return reserve; }
    public void setReserve(boolean reserve) { this.reserve = reserve; }

    public float getPrixUnit() { return prixUnit; }
    public void setPrixUnit(float prixUnit) { this.prixUnit = prixUnit; }

    public int getQuantite() { return quantite; }
    public void setQuantite(int quantite) { this.quantite = quantite; }
}