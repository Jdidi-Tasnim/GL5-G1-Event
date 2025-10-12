package tn.esprit.eventsproject.services;

import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import java.util.Optional;

import java.time.LocalDate;
import java.util.List;

public interface IEventServices {

    // ------------------- CRUD Event -------------------
    Event addEvent(Event event);
    Event updateEvent(Event event);
   Optional<Event> retrieveEvent(int id);
    void deleteEvent(int id);


    // ------------------- Participant -------------------
    Participant addParticipant(Participant participant);

    // ------------------- Affect Participant -------------------
    Event addAffectEvenParticipant(Event event, int idParticipant);
    Event addAffectEvenParticipant(Event event);

    // ------------------- Logistics -------------------
    Logistics addAffectLog(Logistics logistics, String descriptionEvent);
    List<Logistics> getLogisticsDates(LocalDate dateDebut, LocalDate dateFin);

    // ------------------- Cost Calculation -------------------
    void calculCout();
}

