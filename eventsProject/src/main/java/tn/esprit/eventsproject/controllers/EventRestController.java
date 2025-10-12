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
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RequestMapping("event")
@RestController
public class EventRestController {

    private final IEventServices eventServices;

    // 🔹 Ajout de participant
    @PostMapping("/addPart")
    public Participant addParticipant(@RequestBody Participant participant){
        return eventServices.addParticipant(participant);
    }

    // 🔹 Ajout Event avec Participant
    @PostMapping("/addEvent/{id}")
    public EventDTO addEventPart(@RequestBody Event event, @PathVariable("id") int idPart){
        Event savedEvent = eventServices.addAffectEvenParticipant(event, idPart);
        return convertToDTO(savedEvent);
    }

    // 🔹 Ajout Event simple
    @PostMapping("/addEvent")
    public EventDTO addEvent(@RequestBody Event event){
        Event savedEvent = eventServices.addAffectEvenParticipant(event);
        return convertToDTO(savedEvent);
    }

    // 🔹 Affecter Logistique à un Event
    @PutMapping("/addAffectLog/{description}")
    public Logistics addAffectLog(@RequestBody Logistics logistics, @PathVariable("description") String descriptionEvent){
        return eventServices.addAffectLog(logistics, descriptionEvent);
    }

    // 🔹 Obtenir les logistiques filtrées par date
    @GetMapping("/getLogs/{d1}/{d2}")
    public List<Logistics> getLogistiquesDates (@PathVariable("d1") LocalDate date_debut, @PathVariable("d2") LocalDate date_fin){
        return eventServices.getLogisticsDates(date_debut, date_fin);
    }

    // 🔹 Conversion Event -> EventDTO
    private EventDTO convertToDTO(Event event){
        EventDTO dto = new EventDTO();
        dto.setIdEvent(event.getIdEvent());
        dto.setDescription(event.getDescription());
        dto.setDateDebut(event.getDateDebut());
        dto.setDateFin(event.getDateFin());
        dto.setCout(event.getCout());
        return dto;
    }
}

