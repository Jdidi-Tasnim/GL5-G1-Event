package tn.esprit.eventsproject.services;

import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;

import java.util.List;

public interface ITacheServices {
    List<Participant> getParticipantsByTache(Tache tache);
    Participant assignTacheToParticipant(int idParticipant, Tache tache);
    long countParticipantsByTache(Tache tache);
}

