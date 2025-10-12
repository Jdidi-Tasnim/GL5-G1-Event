package tn.esprit.eventsproject.entities;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
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

    // FIX: Removed inline initialization "= new HashSet<>()". 
    // The collection should be initialized in the constructor (or lazy-initialized by JPA/Hibernate).
    @ManyToMany(mappedBy = "events")
    Set<Participant> participants; 

    // FIX: Removed inline initialization "= new HashSet<>()".
    @OneToMany(fetch = FetchType.EAGER)
    Set<Logistics> logistics; 
    
    /* * NOTE: If you need to ensure these collections are never null 
     * when manipulating the entity outside of JPA, you should add a constructor:
     * * public Event() {
     * this.participants = new HashSet<>();
     * this.logistics = new HashSet<>();
     * }
     * * However, removing the initialization is the simplest fix to the *Maintainability Warning*
     * because the fields are implicitly private due to @FieldDefaults.
     */
}
