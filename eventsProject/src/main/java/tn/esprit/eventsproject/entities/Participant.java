package tn.esprit.eventsproject.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Participant implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idPart;
    String nom;
    String prenom;
    @Enumerated(EnumType.STRING)
    Tache tache;

    @ManyToMany
    @JoinTable(
        // Set the join table name to match your SQL script
        name = "participant_events",
        // The column for the owning side (Participant)
        joinColumns = @JoinColumn(name = "participant_id"), 
        // The column for the inverse side (Event)
        inverseJoinColumns = @JoinColumn(name = "events_id_event") 
    )
    Set<Event> events;
}