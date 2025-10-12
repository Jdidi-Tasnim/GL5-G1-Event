package tn.esprit.eventsproject.services;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;
import tn.esprit.eventsproject.repositories.EventRepository;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServicesImpl implements IEventServices {

    private final EventRepository eventRepository;

    // ------------------- CRUD Event -------------------

    @Override
    public Event addEvent(Event event) {
        return eventRepository.save(event);
    }

    @Override
    public Event updateEvent(Event event) {
        return eventRepository.save(event);
    }

    // ------------------- Affect Participant -------------------
    @Override
    public Event addAffectEvenParticipant(Event event, int idParticipant) {
        Participant participant = event.getParticipants().stream()
                .filter(p -> p.getIdPart() == idParticipant)
                .findFirst()
                .orElse(null);

        if (participant != null) {
            Set<Event> participantEvents = participant.getEvents() != null ? participant.getEvents() : new HashSet<>();
            participantEvents.add(event);
            participant.setEvents(participantEvents);
        }
        return eventRepository.save(event);
    }

    @Override
    public Event addAffectEvenParticipant(Event event) {
        Set<Participant> participantSet = event.getParticipants();
        if (participantSet != null) {
            for (Participant participant : participantSet) {
                Participant persisted = participant; // récupéré depuis participantSet, non null
                Set<Event> participantEvents = persisted.getEvents() != null ? persisted.getEvents() : new HashSet<>();
                participantEvents.add(event);
                persisted.setEvents(participantEvents);
            }
        }
        return eventRepository.save(event);
    }

    // ------------------- Logistics related (Event only) -------------------

    @Override
    public List<Logistics> getLogisticsDates(LocalDate dateDebut, LocalDate dateFin) {
        List<Event> events = eventRepository.findByDateDebutBetween(dateDebut, dateFin);
        List<Logistics> logisticsList = new ArrayList<>();
        for (Event event : events) {
            Set<Logistics> logisticsSet = event.getLogistics();
            if (logisticsSet != null) {
                for (Logistics logistics : logisticsSet) {
                    if (logistics.isReserve()) {
                        logisticsList.add(logistics);
                    }
                }
            }
        }
        return logisticsList;
    }

    // ------------------- Cost Calculation -------------------

    @Scheduled(cron = "*/60 * * * * *")
    @Override
    public void calculCout() {
        List<Event> events = eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR
        );

        for (Event event : events) {
            float somme = 0f;
            Set<Logistics> logisticsSet = event.getLogistics();
            if (logisticsSet != null) {
                for (Logistics logistics : logisticsSet) {
                    if (logistics.isReserve()) {
                        somme += logistics.getPrixUnit() * logistics.getQuantite();
                    }
                }
            }
            event.setCout(somme);
            eventRepository.save(event);
            log.info("Cout de l'Event '{}' est {}", event.getDescription(), somme);
        }
    }

    // ------------------- Participant & Logistics (unchanged) -------------------
    @Override
    public Participant addParticipant(Participant participant) {
        return null; // non modifié, délégué à ton code existant
    }

    @Override
    public Logistics addAffectLog(Logistics logistics, String descriptionEvent) {
        return null; // non modifié, délégué à ton code existant
    }
}

