package tn.esprit.eventsproject.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class EventDTO {

    int idEvent;
    String description;
    LocalDate dateDebut;
    LocalDate dateFin;
    float cout;

    // 🔹 Collections participants/logistics ne sont pas exposées pour éviter les problèmes de sécurité et de maintenabilité.
}

