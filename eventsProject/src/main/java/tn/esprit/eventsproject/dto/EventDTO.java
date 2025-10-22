package tn.esprit.eventsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class EventDTO {
    private Integer idEvent;
    private String description;
    private LocalDate dateDebut;
    private LocalDate dateFin;
    private Float cout;
}