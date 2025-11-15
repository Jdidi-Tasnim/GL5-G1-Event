package tn.esprit.eventsproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.eventsproject.dto.EventDTO;
import tn.esprit.eventsproject.dto.ParticipantDTO;
import tn.esprit.eventsproject.dto.LogisticsDTO;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api")  // 🔧 CORRECTION : Ajout du prefix /api
@RequiredArgsConstructor
@CrossOrigin(origins = "*")  // 🔧 CORRECTION : Autoriser CORS
public class EventRestController {
    private final IEventServices eventServices;

    @PostMapping("/participants")
    public Participant addParticipant(@RequestBody ParticipantDTO participantDTO){
        Participant participant = new Participant();
        participant.setNom(participantDTO.getNom());
        participant.setPrenom(participantDTO.getPrenom());
        participant.setTache(participantDTO.getTache());
        return eventServices.addParticipant(participant);
    }

    @PostMapping("/events/participant/{id}")
    public Event addEventPart(@RequestBody EventDTO eventDTO, @PathVariable("id") int idPart){
        Event event = new Event();
        event.setDescription(eventDTO.getDescription());
        event.setDateDebut(eventDTO.getDateDebut());
        event.setDateFin(eventDTO.getDateFin());
        event.setCout(eventDTO.getCout());
        return eventServices.addAffectEvenParticipant(event, idPart);
    }

    @PostMapping("/events")
    public Event addEvent(@RequestBody EventDTO eventDTO){
        Event event = new Event();
        event.setDescription(eventDTO.getDescription());
        event.setDateDebut(eventDTO.getDateDebut());
        event.setDateFin(eventDTO.getDateFin());
        event.setCout(eventDTO.getCout());
        return eventServices.addAffectEvenParticipant(event);
    }

    @PutMapping("/logistics/event/{description}")
    public Logistics addAffectLog(@RequestBody LogisticsDTO logisticsDTO, @PathVariable("description") String descriptionEvent){
        Logistics logistics = new Logistics();
        logistics.setDescription(logisticsDTO.getDescription());
        logistics.setReserve(logisticsDTO.isReserve());
        logistics.setPrixUnit(logisticsDTO.getPrixUnit());
        logistics.setQuantite(logisticsDTO.getQuantite());
        return eventServices.addAffectLog(logistics, descriptionEvent);
    }

    @GetMapping("/logistics/date-range")
    public List<Logistics> getLogistiquesDates(
            @RequestParam("startDate") LocalDate date_debut,
            @RequestParam("endDate") LocalDate date_fin){
        return eventServices.getLogisticsDates(date_debut, date_fin);
    }

    // 🔧 CORRECTION : Ajout d'un endpoint de test
    @GetMapping("/test")
    public String test() {
        return "Events API is working!!";
    }

}