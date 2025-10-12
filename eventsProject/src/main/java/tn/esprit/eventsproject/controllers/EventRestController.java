package tn.esprit.eventsproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.eventsproject.dto.EventDTO;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("event")
@RestController
public class EventRestController {

    private final IEventServices eventServices;

    // ------------------- Participant -------------------
    @PostMapping("/addPart")
    public Participant addParticipant(@RequestBody Participant participant) {
        return eventServices.addParticipant(participant);
    }

    // ------------------- Event avec affectation participant -------------------
    @PostMapping("/addEvent/{id}")
    public Event addEventPart(@RequestBody EventDTO eventDTO, @PathVariable("id") int idPart) {
        Event event = mapDtoToEvent(eventDTO);
        return eventServices.addAffectEvenParticipant(event, idPart);
    }

    // ------------------- Event simple -------------------
    @PostMapping("/addEvent")
    public Event addEvent(@RequestBody EventDTO eventDTO) {
        Event event = mapDtoToEvent(eventDTO);
        return eventServices.addAffectEvenParticipant(event);
    }

    // ------------------- Logistics -------------------
    @PutMapping("/addAffectLog/{description}")
    public Logistics addAffectLog(@RequestBody Logistics logistics, @PathVariable("description") String descriptionEvent) {
        return eventServices.addAffectLog(logistics, descriptionEvent);
    }

    @GetMapping("/getLogs/{d1}/{d2}")
    public List<Logistics> getLogistiquesDates(@PathVariable("d1") LocalDate date_debut,
                                               @PathVariable("d2") LocalDate date_fin) {
        return eventServices.getLogisticsDates(date_debut, date_fin);
    }

    // ------------------- Helper Method -------------------
    private Event mapDtoToEvent(EventDTO dto) {
        Event event = new Event();
        event.setDescription(dto.getDescription());
        event.setDateDebut(dto.getDateDebut());
        event.setDateFin(dto.getDateFin());
        event.setCout(dto.getCout());
        return event;
    }
}

