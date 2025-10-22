package tn.esprit.eventsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class LogisticsDTO {
    private Integer idLog;
    private String description;
    private Boolean reserve;
    private Float prixUnit;
    private Integer quantite;
}