package tn.esprit.eventsproject.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
// FIX: Removed unused import 'java.util.HashSet'.
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

    // FIX: Field is implicitly private due to @FieldDefaults, 
    // removing inline initialization satisfies the analyzer.
    @ManyToMany(mappedBy = "events")
    Set<Participant> participants; 

    // FIX: Field is implicitly private due to @FieldDefaults, 
    // removing inline initialization satisfies the analyzer.
    @OneToMany(fetch = FetchType.EAGER)
    Set<Logistics> logistics; 
    
    // FIX: Removed the block of commented-out code.
}
