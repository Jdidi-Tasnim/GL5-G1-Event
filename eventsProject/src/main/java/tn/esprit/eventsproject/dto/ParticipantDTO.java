package tn.esprit.eventsproject.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import tn.esprit.eventsproject.entities.Tache;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ParticipantDTO {
    private Integer idPart;
    private String nom;
    private String prenom;
    private Tache tache;
}