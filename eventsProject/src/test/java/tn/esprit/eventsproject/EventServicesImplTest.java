package tn.esprit.eventsproject.services;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.repositories.EventRepository;

import java.time.LocalDate;
import java.util.Optional;

@SpringBootTest(classes = tn.esprit.eventsproject.EventsProjectApplication.class)
@ActiveProfiles("test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class EventServicesImplTest {

    @Autowired
    IEventServices eventService;

    @Autowired
    EventRepository eventRepository;

    static int savedEventId;

    @Order(1)
    @Test
    void testAddEvent() {
        Event newEvent = new Event();
        newEvent.setDescription("Test_CRUD");
        newEvent.setDateDebut(LocalDate.now().plusDays(1));
        newEvent.setDateFin(LocalDate.now().plusDays(2));
        newEvent.setCout(100.0f);

        Event saved = eventService.addEvent(newEvent);
        Assertions.assertNotNull(saved.getIdEvent());
        savedEventId = saved.getIdEvent();
    }

    @Order(2)
    @Test
    void testRetrieveEvent() {
        Optional<Event> opt = eventRepository.findById(savedEventId);
        Assertions.assertTrue(opt.isPresent());
        Assertions.assertEquals("Test_CRUD", opt.get().getDescription());
    }

    @Order(3)
    @Test
    void testUpdateEvent() {
        Event e = eventRepository.findById(savedEventId).orElseThrow();
        e.setDescription("Updated_CRUD");
        e.setCout(200.0f);

        Event updated = eventService.updateEvent(e);
        Assertions.assertEquals("Updated_CRUD", updated.getDescription());
        Assertions.assertEquals(200.0f, updated.getCout());
    }

    @Order(4)
    @Test
    void testDeleteEvent() {
        eventService.deleteEvent(savedEventId);
        Assertions.assertFalse(eventRepository.findById(savedEventId).isPresent());
    }
}

