package tn.esprit.eventsproject.services;

import org.junit.jupiter.api.Test;  // ✅ Changé vers JUnit 5
import org.junit.jupiter.api.extension.ExtendWith;  // ✅ Nouvel import
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;  // ✅ Changé vers JUnit 5
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;
import tn.esprit.eventsproject.repositories.EventRepository;
import tn.esprit.eventsproject.repositories.ParticipantRepository;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;  // ✅ Changé vers JUnit 5
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)  // ✅ Changé vers JUnit 5
public class EventServicesImplMockitoTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private ParticipantRepository participantRepository;

    @InjectMocks
    private EventServicesImpl eventServices;

    @Test
    public void testAddParticipant() {
        // Given
        Participant participant = new Participant();
        participant.setNom("Doe");
        participant.setPrenom("John");
        participant.setTache(Tache.INVITE);

        when(participantRepository.save(any(Participant.class))).thenReturn(participant);

        // When
        Participant savedParticipant = eventServices.addParticipant(participant);

        // Then
        assertNotNull(savedParticipant);
        assertEquals("Doe", savedParticipant.getNom());
        assertEquals("John", savedParticipant.getPrenom());
        assertEquals(Tache.INVITE, savedParticipant.getTache());
        verify(participantRepository, times(1)).save(participant);
    }

    @Test
    public void testAddAffectEvenParticipantWithId() {
        // Given
        Event event = new Event();
        event.setDescription("Test Event");
        event.setDateDebut(LocalDate.now());
        event.setDateFin(LocalDate.now().plusDays(1));

        Participant participant = new Participant();
        participant.setIdPart(1);
        participant.setNom("Doe");
        participant.setPrenom("John");

        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        Event savedEvent = eventServices.addAffectEvenParticipant(event, 1);

        // Then
        assertNotNull(savedEvent);
        assertEquals("Test Event", savedEvent.getDescription());
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(event);
    }

    @Test
    public void testAddAffectEvenParticipantWithId_ParticipantNotFound() {
        // Given
        Event event = new Event();
        event.setDescription("Test Event");

        when(participantRepository.findById(999)).thenReturn(Optional.empty());

        // When
        Event result = eventServices.addAffectEvenParticipant(event, 999);

        // Then
        assertNull(result);
        verify(participantRepository, times(1)).findById(999);
        verify(eventRepository, never()).save(any(Event.class));
    }

    @Test
    public void testAddAffectEvenParticipant() {
        // Given
        Event event = new Event();
        event.setDescription("Test Event");
        event.setParticipants(new HashSet<>());

        Participant participant = new Participant();
        participant.setIdPart(1);
        participant.setNom("Doe");
        participant.setPrenom("John");

        event.getParticipants().add(participant);

        when(participantRepository.findById(1)).thenReturn(Optional.of(participant));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        // When
        Event savedEvent = eventServices.addAffectEvenParticipant(event);

        // Then
        assertNotNull(savedEvent);
        assertEquals("Test Event", savedEvent.getDescription());
        verify(participantRepository, times(1)).findById(1);
        verify(eventRepository, times(1)).save(event);
    }
}