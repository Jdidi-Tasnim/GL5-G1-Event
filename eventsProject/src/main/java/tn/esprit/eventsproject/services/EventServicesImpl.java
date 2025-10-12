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
import tn.esprit.eventsproject.repositories.LogisticsRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@RequiredArgsConstructor
@Service
public class EventServicesImpl implements IEventServices {

    private final EventRepository eventRepository;
    private final ParticipantRepository participantRepository;
    private final LogisticsRepository logisticsRepository;

    // ====================== PARTICIPANT ====================== //
    @Override
    public Participant addParticipant(Participant participant) {
        log.info("🔹 Ajout d’un nouveau participant : {} {}", participant.getNom(), participant.getPrenom());
        Participant savedParticipant = participantRepository.save(participant);
        log.info("✅ Participant ajouté avec succès avec ID : {}", savedParticipant.getIdPart());
        return savedParticipant;
    }

    @Override
    public Event addAffectEvenParticipant(Event event, int idParticipant) {
        log.info("🔹 Association de l’événement [{}] au participant ID={}", event.getDescription(), idParticipant);

        Participant participant = participantRepository.findById(idParticipant).orElse(null);
        if (participant == null) {
            log.warn("⚠️ Aucun participant trouvé avec l’ID {}", idParticipant);
            return null;
        }

        if (participant.getEvents() == null) {
            participant.setEvents(new HashSet<>());
        }
        participant.getEvents().add(event);

        Event savedEvent = eventRepository.save(event);
        log.info("✅ L’événement [{}] a été associé au participant [{} {}]", event.getDescription(), participant.getNom(), participant.getPrenom());
        return savedEvent;
    }

    @Override
    public Event addAffectEvenParticipant(Event event) {
        log.info("🔹 Association d’un événement [{}] à plusieurs participants", event.getDescription());

        Set<Participant> participants = event.getParticipants();
        for (Participant aParticipant : participants) {
            Participant participant = participantRepository.findById(aParticipant.getIdPart()).orElse(null);
            if (participant != null) {
                if (participant.getEvents() == null) {
                    participant.setEvents(new HashSet<>());
                }
                participant.getEvents().add(event);
                log.info("➡️ Participant [{} {}] associé à l’événement [{}]", participant.getNom(), participant.getPrenom(), event.getDescription());
            } else {
                log.warn("⚠️ Participant avec ID={} introuvable !", aParticipant.getIdPart());
            }
        }

        Event savedEvent = eventRepository.save(event);
        log.info("✅ Tous les participants valides ont été associés à l’événement [{}]", event.getDescription());
        return savedEvent;
    }

    // ====================== LOGISTICS ====================== //
    @Override
    public Logistics addAffectLog(Logistics logistics, String descriptionEvent) {
        log.info("🔹 Ajout et affectation de la logistique [{}] à l’événement [{}]", logistics.getDescription(), descriptionEvent);

        Event event = eventRepository.findByDescription(descriptionEvent);
        if (event == null) {
            log.warn("⚠️ Aucun événement trouvé avec la description [{}]", descriptionEvent);
            return null;
        }

        if (event.getLogistics() == null) {
            event.setLogistics(new HashSet<>());
        }
        event.getLogistics().add(logistics);

        logisticsRepository.save(logistics);
        eventRepository.save(event);
        log.info("✅ Logistique [{}] affectée avec succès à l’événement [{}]", logistics.getDescription(), event.getDescription());
        return logistics;
    }

    // ====================== FILTRAGE PAR DATES ====================== //
    @Override
    public List<Logistics> getLogisticsDates(LocalDate date_debut, LocalDate date_fin) {
        log.info("🔹 Récupération des logistiques entre {} et {}", date_debut, date_fin);

        List<Event> events = eventRepository.findByDateDebutBetween(date_debut, date_fin);
        List<Logistics> logisticsList = new ArrayList<>();

        for (Event event : events) {
            if (event.getLogistics() == null || event.getLogistics().isEmpty()) {
                log.info("⚠️ L’événement [{}] ne contient aucune logistique", event.getDescription());
                continue;
            }

            for (Logistics logistics : event.getLogistics()) {
                if (logistics.isReserve()) {
                    logisticsList.add(logistics);
                    log.info("✅ Logistique réservée trouvée pour l’événement [{}] : {}", event.getDescription(), logistics.getDescription());
                }
            }
        }

        log.info("📦 Nombre total de logistiques réservées trouvées : {}", logisticsList.size());
        return logisticsList;
    }

    // ====================== TÂCHE PLANIFIÉE ====================== //
    @Scheduled(cron = "*/60 * * * * *")
    @Override
    public void calculCout() {
        log.info("🧮 Début du calcul du coût des événements (tâche planifiée)");

        List<Event> events = eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR);

        for (Event event : events) {
            float somme = 0f;
            log.info("🔹 Calcul du coût pour l’événement [{}]", event.getDescription());

            for (Logistics logistics : event.getLogistics()) {
                if (logistics.isReserve()) {
                    float cout = logistics.getPrixUnit() * logistics.getQuantite();
                    somme += cout;
                    log.info("   ➕ [{}] : {} x {} = {}", logistics.getDescription(), logistics.getPrixUnit(), logistics.getQuantite(), cout);
                }
            }

            event.setCout(somme);
            eventRepository.save(event);
            log.info("✅ Coût total de l’événement [{}] : {} DT", event.getDescription(), somme);
        }

        log.info("🟩 Fin du calcul des coûts des événements");
    }
}
