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

    // 🔹 Collections private et non-static (Sonar Maintainability)
    @ManyToMany(mappedBy = "events")
    Set<Participant> participants = new HashSet<>();

    @OneToMany(fetch = FetchType.EAGER)
    Set<Logistics> logistics = new HashSet<>();
}

