package tn.esprit.eventsproject.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Entity
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    int idEvent;

    String description;
    LocalDate dateDebut;
    LocalDate dateFin;
    float cout;

    // FIX: Field is implicitly PRIVATE via @FieldDefaults, 
    // satisfying the core requirement.
    @ManyToMany(mappedBy = "events")
    Set<Participant> participants; 

    // FIX: Field is implicitly PRIVATE via @FieldDefaults, 
    // satisfying the core requirement.
    @OneToMany(fetch = FetchType.EAGER)
    Set<Logistics> logistics; 
}
