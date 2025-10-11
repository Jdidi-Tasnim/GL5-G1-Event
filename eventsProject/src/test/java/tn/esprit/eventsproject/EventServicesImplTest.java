package tn.esprit.eventsproject.services;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.repositories.EventRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

// Utilisé pour assurer l'ordre d'exécution (CRUD)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest
@Slf4j
public class EventServicesImplTest {

    @Autowired
    IEventServices eventService; // Injection du service à tester
    
    @Autowired
    EventRepository eventRepository; // Utilisé pour les vérifications directes et le nettoyage
    
    // ID statique pour partager l'ID de l'événement créé entre les tests
    static int savedEventId;

    // ============== C (Create) : Test d'ajout ==================
    @org.junit.jupiter.api.Order(1)
    @Test
    public void testAddEvent() {
        log.info("--- Début testAddEvent ---");
        
        // 1. Préparation des données
        Event newEvent = new Event(
                0, // ID à générer
                "Evenement_Test_CRUD",
                LocalDate.now().plusDays(10),
                LocalDate.now().plusDays(12),
                500.0f,
                null, // Participants
                null  // Logistics
        );
        
        // 2. Exécution du service
        Event savedEvent = eventService.addEvent(newEvent); // Supposons que IEventServices ait une méthode addEvent(Event)
        
        // 3. Assertions et vérification
        Assertions.assertNotNull(savedEvent.getIdEvent(), "L'ID de l'événement ne doit pas être nul après sauvegarde.");
        Assertions.assertEquals("Evenement_Test_CRUD", savedEvent.getDescription());
        
        // Sauvegarde de l'ID pour les autres tests
        savedEventId = savedEvent.getIdEvent();
        log.info("Événement créé avec l'ID: " + savedEventId);
    }
    
    // ============== R (Read) : Test de récupération ==================
    @org.junit.jupiter.api.Order(2)
    @Test
    public void testRetrieveEvent() {
        log.info("--- Début testRetrieveEvent ---");
        
        // Utilisation de l'ID créé dans le test précédent
        Optional<Event> retrievedEventOpt = eventRepository.findById(savedEventId); 
        
        // 2. Assertions et vérification
        Assertions.assertTrue(retrievedEventOpt.isPresent(), "L'événement créé doit exister.");
        Assertions.assertEquals("Evenement_Test_CRUD", retrievedEventOpt.get().getDescription());
    }
    
    // ============== U (Update) : Test de mise à jour ==================
    @org.junit.jupiter.api.Order(3)
    @Test
    public void testUpdateEvent() {
        log.info("--- Début testUpdateEvent ---");
        
        // 1. Récupération de l'événement
        Event eventToUpdate = eventRepository.findById(savedEventId).orElse(null);
        Assertions.assertNotNull(eventToUpdate, "L'événement à mettre à jour ne doit pas être nul.");
        
        // 2. Modification des données
        final String newDescription = "Evenement_Test_UPDATED";
        eventToUpdate.setDescription(newDescription);
        eventToUpdate.setCout(750.0f);
        
        // 3. Exécution du service
        Event updatedEvent = eventService.updateEvent(eventToUpdate); // Supposons que IEventServices ait une méthode updateEvent(Event)
        
        // 4. Assertions et vérification
        Assertions.assertEquals(newDescription, updatedEvent.getDescription(), "La description doit avoir été mise à jour.");
        Assertions.assertEquals(750.0f, updatedEvent.getCout(), 0.01, "Le coût doit avoir été mis à jour.");
    }
    
    // ============== D (Delete) : Test de suppression et Nettoyage ==================
    @org.junit.jupiter.api.Order(4)
    @Test
    public void testDeleteEvent() {
        log.info("--- Début testDeleteEvent ---");
        
        // 1. Exécution du service
        eventRepository.deleteById(savedEventId); // Supposons qu'on utilise la méthode du Repository pour la suppression simple
        
        // 2. Vérification
        Optional<Event> deletedEventCheck = eventRepository.findById(savedEventId);
        
        // 3. Assertion
        Assertions.assertFalse(deletedEventCheck.isPresent(), "L'événement doit avoir été supprimé.");
    }

    // Note: Pour que ces tests fonctionnent, vous devez ajouter les méthodes simples 
    // 'addEvent(Event)' et 'updateEvent(Event)' à IEventServices et EventServicesImpl.
}
