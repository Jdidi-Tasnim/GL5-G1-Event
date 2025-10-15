package tn.esprit.eventsproject.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;

import java.time.LocalDate;
import java.util.HashSet;

import static org.junit.Assert.*;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class EventServicesImplTest {

    @Autowired
    IEventServices eventServices;

    @Test
    public void testAddParticipant() {
        log.info("🧪 Début du test d'ajout de participant");

        Participant participant = new Participant();
        participant.setNom("Tounsi");
        participant.setPrenom("Ahmed");
        participant.setTache(Tache.ORGANISATEUR);

        Participant savedParticipant = eventServices.addParticipant(participant);

        log.info("✅ Participant ajouté: {} {}", savedParticipant.getNom(), savedParticipant.getPrenom());

        assertTrue(savedParticipant.getIdPart() > 0);
        assertEquals("Tounsi", savedParticipant.getNom());
        assertEquals("Ahmed", savedParticipant.getPrenom());
        assertEquals(Tache.ORGANISATEUR, savedParticipant.getTache());

        log.info("🧪 Fin du test d'ajout de participant - ID: {}", savedParticipant.getIdPart());
    }

    @Test
    public void testAddEventWithParticipant() {
        log.info("🧪 Début du test d'ajout d'événement avec participant");

        // D'abord créer un participant
        Participant participant = new Participant();
        participant.setNom("Ben Salah");
        participant.setPrenom("Mohamed");
        participant.setTache(Tache.INVITE);
        Participant savedParticipant = eventServices.addParticipant(participant);

        // Créer un événement
        Event event = new Event();
        event.setDescription("Conférence Spring Boot");
        event.setDateDebut(LocalDate.now());
        event.setDateFin(LocalDate.now().plusDays(2));
        event.setCout(0);

        Event savedEvent = eventServices.addAffectEvenParticipant(event, savedParticipant.getIdPart());

        log.info("✅ Événement ajouté: {}", savedEvent.getDescription());

        assertTrue(savedEvent.getIdEvent() > 0);
        assertEquals("Conférence Spring Boot", savedEvent.getDescription());
        assertNotNull(savedEvent.getDateDebut());
        assertNotNull(savedEvent.getDateFin());

        log.info("🧪 Fin du test d'ajout d'événement - ID: {}", savedEvent.getIdEvent());
    }

    @Test
    public void testAddMultipleParticipantsToEvent() {
        log.info("🧪 Début du test d'ajout de multiples participants");

        // Créer deux participants
        Participant participant1 = new Participant();
        participant1.setNom("Ben Ali");
        participant1.setPrenom("Sofia");
        participant1.setTache(Tache.INVITE);
        Participant savedParticipant1 = eventServices.addParticipant(participant1);

        Participant participant2 = new Participant();
        participant2.setNom("Ben Youssef");
        participant2.setPrenom("Karim");
        participant2.setTache(Tache.ANIMATEUR);
        Participant savedParticipant2 = eventServices.addParticipant(participant2);

        // Créer un événement avec plusieurs participants
        Event event = new Event();
        event.setDescription("Workshop Jenkins");
        event.setDateDebut(LocalDate.now().plusDays(1));
        event.setDateFin(LocalDate.now().plusDays(3));
        event.setParticipants(new HashSet<>());

        // Ajouter les participants à l'événement
        Participant p1 = new Participant();
        p1.setIdPart(savedParticipant1.getIdPart());

        Participant p2 = new Participant();
        p2.setIdPart(savedParticipant2.getIdPart());

        event.getParticipants().add(p1);
        event.getParticipants().add(p2);

        Event savedEvent = eventServices.addAffectEvenParticipant(event);

        log.info("✅ Événement avec multiples participants ajouté: {}", savedEvent.getDescription());

        assertEquals("Workshop Jenkins", savedEvent.getDescription());
        assertNotNull(savedEvent.getParticipants());

        log.info("🧪 Fin du test d'ajout de multiples participants");
    }

    @Test
    public void testGetLogisticsDates() {
        log.info("🧪 Début du test de récupération des logistiques par dates");

        LocalDate startDate = LocalDate.now().minusDays(1);
        LocalDate endDate = LocalDate.now().plusDays(5);

        java.util.List<tn.esprit.eventsproject.entities.Logistics> logistics = eventServices.getLogisticsDates(startDate, endDate);

        log.info("✅ Logistiques récupérées: {} éléments", logistics.size());

        assertNotNull(logistics);
        // Peut être vide si pas de logistiques dans cette période

        log.info("🧪 Fin du test de récupération des logistiques");
    }
}