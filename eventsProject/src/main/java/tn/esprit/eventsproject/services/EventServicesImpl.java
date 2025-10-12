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
import java.util.Optional;

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

    // ------------------- Participant -------------------
    @Override
    public Participant addParticipant(Participant participant) {
        // Ici, tu peux ajouter le participant à sa table via repository (non fourni)
        return participant;
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
                Set<Event> participantEvents = participant.getEvents() != null ? participant.getEvents() : new HashSet<>();
                participantEvents.add(event);
                participant.setEvents(participantEvents);
            }
        }
        return eventRepository.save(event);
    }
@Override
public Optional<Event> retrieveEvent(int id) {
    return eventRepository.findById(id);
}


@Override
public void deleteEvent(int id) {
    eventRepository.deleteById(id);
}

    // ------------------- Logistics -------------------
    @Override
    public Logistics addAffectLog(Logistics logistics, String descriptionEvent) {
        // Ajoute le logistics à l'événement correspondant
        // Exemple: recherche de l'event par description et ajout du logistics
        return logistics;
    }

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
}

