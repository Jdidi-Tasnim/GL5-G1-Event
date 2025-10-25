package tn.esprit.eventsproject.entities;

import lombok.*;
// FIX: Removed lombok.experimental.FieldDefaults
import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//@FieldDefaults(level = AccessLevel.PRIVATE) // FIX: Removed this line
@Entity
public class Event implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idEvent; // FIX: Added explicit 'private'

    private String description; // FIX: Added explicit 'private'
    private LocalDate dateDebut; // FIX: Added explicit 'private'
    private LocalDate dateFin; // FIX: Added explicit 'private'
    private float cout; // FIX: Added explicit 'private'

    // FIX: Added explicit 'private'
    @ManyToMany(mappedBy = "events")
    private Set<Participant> participants; 

    // FIX: Added explicit 'private'
    @OneToMany(fetch = FetchType.EAGER)
    private Set<Logistics> logistics; 
}
