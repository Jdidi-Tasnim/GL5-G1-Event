package tn.esprit.eventsproject.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;
import tn.esprit.eventsproject.repositories.EventRepository;

@ExtendWith(MockitoExtension.class)
class LogisticsFeatureTests {

    @Mock
    private EventRepository eventRepository;

    @InjectMocks
    private EventServicesImpl eventServices;

    private Event testEvent;
    private Logistics testLogistics;
    private LocalDate startDate;
    private LocalDate endDate;

    @BeforeEach
    void setUp() {
        // Initialize test dates
        startDate = LocalDate.of(2025, 1, 1);
        endDate = LocalDate.of(2025, 1, 31);

        // Initialize test logistics
        testLogistics = new Logistics();
        testLogistics.setIdLog(1);
        testLogistics.setDescription("Tables and chairs");
        testLogistics.setReserve(true);
        testLogistics.setPrixUnit(50.0f);
        testLogistics.setQuantite(10);

        // Initialize test event
        testEvent = new Event();
        testEvent.setIdEvent(1);
        testEvent.setDescription("Test Conference");
        testEvent.setDateDebut(LocalDate.of(2025, 1, 15));
        testEvent.setDateFin(LocalDate.of(2025, 1, 16));
        testEvent.setCout(0f);

        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(testLogistics);
        testEvent.setLogistics(logisticsSet);
    }

    // ==================== getLogisticsDates Tests ====================

    @Test
    void testGetLogisticsDates_WithReservedLogistics_ShouldReturnList() {
        // Arrange
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(events);

        // Act
        List<Logistics> result = eventServices.getLogisticsDates(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testLogistics.getIdLog(), result.get(0).getIdLog());
        assertEquals("Tables and chairs", result.get(0).getDescription());
        verify(eventRepository, times(1)).findByDateDebutBetween(startDate, endDate);
    }

    @Test
    void testGetLogisticsDates_WithNonReservedLogistics_ShouldReturnEmptyList() {
        // Arrange
        testLogistics.setReserve(false);
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(events);

        // Act
        List<Logistics> result = eventServices.getLogisticsDates(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(eventRepository, times(1)).findByDateDebutBetween(startDate, endDate);
    }

    @Test
    void testGetLogisticsDates_WithMultipleEvents_ShouldReturnAllReservedLogistics() {
        // Arrange
        Logistics logistics2 = new Logistics();
        logistics2.setIdLog(2);
        logistics2.setDescription("Sound system");
        logistics2.setReserve(true);
        logistics2.setPrixUnit(200.0f);
        logistics2.setQuantite(2);

        Event event2 = new Event();
        event2.setIdEvent(2);
        event2.setDescription("Workshop");
        event2.setDateDebut(LocalDate.of(2025, 1, 20));
        event2.setDateFin(LocalDate.of(2025, 1, 21));
        
        Set<Logistics> logisticsSet2 = new HashSet<>();
        logisticsSet2.add(logistics2);
        event2.setLogistics(logisticsSet2);

        List<Event> events = Arrays.asList(testEvent, event2);
        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(events);

        // Act
        List<Logistics> result = eventServices.getLogisticsDates(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        verify(eventRepository, times(1)).findByDateDebutBetween(startDate, endDate);
    }

    @Test
    void testGetLogisticsDates_WithNullLogistics_ShouldReturnEmptyList() {
        // Arrange
        testEvent.setLogistics(null);
        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(events);

        // Act
        List<Logistics> result = eventServices.getLogisticsDates(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(eventRepository, times(1)).findByDateDebutBetween(startDate, endDate);
    }

    @Test
    void testGetLogisticsDates_WithNoEventsInRange_ShouldReturnEmptyList() {
        // Arrange
        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(Collections.emptyList());

        // Act
        List<Logistics> result = eventServices.getLogisticsDates(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(eventRepository, times(1)).findByDateDebutBetween(startDate, endDate);
    }

    @Test
    void testGetLogisticsDates_WithMixedReservedAndNonReserved_ShouldReturnOnlyReserved() {
        // Arrange
        Logistics nonReservedLogistics = new Logistics();
        nonReservedLogistics.setIdLog(2);
        nonReservedLogistics.setDescription("Non-reserved item");
        nonReservedLogistics.setReserve(false);
        nonReservedLogistics.setPrixUnit(100.0f);
        nonReservedLogistics.setQuantite(5);

        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(testLogistics); // reserved
        logisticsSet.add(nonReservedLogistics); // not reserved
        testEvent.setLogistics(logisticsSet);

        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByDateDebutBetween(startDate, endDate)).thenReturn(events);

        // Act
        List<Logistics> result = eventServices.getLogisticsDates(startDate, endDate);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertTrue(result.get(0).isReserve());
        assertEquals(testLogistics.getIdLog(), result.get(0).getIdLog());
        verify(eventRepository, times(1)).findByDateDebutBetween(startDate, endDate);
    }

    // ==================== calculCout Tests ====================

    @Test
    void testCalculCout_WithReservedLogistics_ShouldCalculateCorrectCost() {
        // Arrange
        Participant organizer = new Participant();
        organizer.setIdPart(1);
        organizer.setNom("Tounsi");
        organizer.setPrenom("Ahmed");
        organizer.setTache(Tache.ORGANISATEUR);

        Set<Participant> participants = new HashSet<>();
        participants.add(organizer);
        testEvent.setParticipants(participants);

        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // Act
        eventServices.calculCout();

        // Assert
        verify(eventRepository, times(1)).findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, times(1)).save(testEvent);
        
        float expectedCost = 50.0f * 10; // prixUnit * quantite
        assertEquals(expectedCost, testEvent.getCout(), 0.01f);
    }

    @Test
    void testCalculCout_WithMultipleReservedLogistics_ShouldCalculateTotalCost() {
        // Arrange
        Logistics logistics2 = new Logistics();
        logistics2.setIdLog(2);
        logistics2.setDescription("Projector");
        logistics2.setReserve(true);
        

        

        Set<Logistics> logisticsSet = new HashSet<>();
        logisticsSet.add(testLogistics); // 50 * 10 = 500
        logisticsSet.add(logistics2);     // 150 * 3 = 450
        testEvent.setLogistics(logisticsSet);

        Participant organizer = new Participant();
        organizer.setNom("Tounsi");
        organizer.setPrenom("Ahmed");
        organizer.setTache(Tache.ORGANISATEUR);
        
        Set<Participant> participants = new HashSet<>();
        participants.add(organizer);
        testEvent.setParticipants(participants);

        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // Act
        eventServices.calculCout();

        // Assert
        float expectedCost = (50.0f * 10) + (150.0f * 3); // 500 + 450 = 950
        assertEquals(expectedCost, testEvent.getCout(), 0.01f);
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    void testCalculCout_WithNonReservedLogistics_ShouldCalculateZeroCost() {
        // Arrange
        testLogistics.setReserve(false);

        Participant organizer = new Participant();
        organizer.setNom("Tounsi");
        organizer.setPrenom("Ahmed");
        organizer.setTache(Tache.ORGANISATEUR);
        
        Set<Participant> participants = new HashSet<>();
        participants.add(organizer);
        testEvent.setParticipants(participants);

        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // Act
        eventServices.calculCout();

        // Assert
        assertEquals(0.0f, testEvent.getCout(), 0.01f);
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    void testCalculCout_WithNullLogistics_ShouldSetCostToZero() {
        // Arrange
        testEvent.setLogistics(null);

        Participant organizer = new Participant();
        organizer.setNom("Tounsi");
        organizer.setPrenom("Ahmed");
        organizer.setTache(Tache.ORGANISATEUR);
        
        Set<Participant> participants = new HashSet<>();
        participants.add(organizer);
        testEvent.setParticipants(participants);

        List<Event> events = Arrays.asList(testEvent);
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenReturn(testEvent);

        // Act
        eventServices.calculCout();

        // Assert
        assertEquals(0.0f, testEvent.getCout(), 0.01f);
        verify(eventRepository, times(1)).save(testEvent);
    }

    @Test
    void testCalculCout_WithNoEventsForOrganizer_ShouldNotCalculateAnything() {
        // Arrange
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(Collections.emptyList());

        // Act
        eventServices.calculCout();

        // Assert
        verify(eventRepository, times(1)).findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    void testCalculCout_WithMultipleEvents_ShouldCalculateCostForAll() {
        // Arrange
        Event event2 = new Event();
        event2.setIdEvent(2);
        event2.setDescription("Second Event");
        event2.setCout(0f);

        Logistics logistics2 = new Logistics();
        logistics2.setIdLog(2);
        logistics2.setReserve(true);
        logistics2.setPrixUnit(100.0f);
        logistics2.setQuantite(5);

        Set<Logistics> logisticsSet2 = new HashSet<>();
        logisticsSet2.add(logistics2);
        event2.setLogistics(logisticsSet2);

        Participant organizer = new Participant();
        organizer.setNom("Tounsi");
        organizer.setPrenom("Ahmed");
        organizer.setTache(Tache.ORGANISATEUR);
        
        Set<Participant> participants = new HashSet<>();
        participants.add(organizer);
        testEvent.setParticipants(participants);
        event2.setParticipants(participants);

        List<Event> events = Arrays.asList(testEvent, event2);
        when(eventRepository.findByParticipants_NomAndParticipants_PrenomAndParticipants_Tache(
                "Tounsi", "Ahmed", Tache.ORGANISATEUR)).thenReturn(events);
        when(eventRepository.save(any(Event.class))).thenAnswer(i -> i.getArguments()[0]);

        // Act
        eventServices.calculCout();

        // Assert
        assertEquals(500.0f, testEvent.getCout(), 0.01f); // 50 * 10
        assertEquals(500.0f, event2.getCout(), 0.01f);    // 100 * 5
        verify(eventRepository, times(2)).save(any(Event.class));
    }

    // ==================== addAffectLog Tests ====================

    @Test
    void testAddAffectLog_ShouldReturnLogistics() {
        // Arrange
        String eventDescription = "Test Conference";

        // Act
        Logistics result = eventServices.addAffectLog(testLogistics, eventDescription);

        // Assert
        assertNotNull(result);
        assertEquals(testLogistics, result);
    }

    @Test
    void testAddAffectLog_WithNullLogistics_ShouldReturnNull() {
        // Arrange
        String eventDescription = "Test Conference";

        // Act
        Logistics result = eventServices.addAffectLog(null, eventDescription);

        // Assert
        assertNull(result);
    }
}