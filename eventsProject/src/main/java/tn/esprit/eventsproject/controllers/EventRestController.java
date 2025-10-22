package tn.esprit.eventsproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.eventsproject.dto.EventDTO;
import tn.esprit.eventsproject.dto.LogisticsDTO;
import tn.esprit.eventsproject.dto.ParticipantDTO;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.mappers.EntityMapper;
import tn.esprit.eventsproject.services.IEventServices;

import java.time.LocalDate;
import java.util.List;

@RequiredArgsConstructor
@RequestMapping("event")
@RestController
public class EventRestController {
    private final IEventServices eventServices;
    private final EntityMapper entityMapper;

    @PostMapping("/addPart")
    public ParticipantDTO addParticipant(@RequestBody ParticipantDTO participantDTO){
        Participant participant = entityMapper.toParticipantEntity(participantDTO);
        Participant savedParticipant = eventServices.addParticipant(participant);
        return entityMapper.toParticipantDTO(savedParticipant);
    }
    
    @PostMapping("/addEvent/{id}")
    public EventDTO addEventPart(@RequestBody EventDTO eventDTO, @PathVariable("id") int idPart){
        Event event = entityMapper.toEventEntity(eventDTO);
        Event savedEvent = eventServices.addAffectEvenParticipant(event, idPart);
        return entityMapper.toEventDTO(savedEvent);
    }
    
    @PostMapping("/addEvent")
    public EventDTO addEvent(@RequestBody EventDTO eventDTO){
        Event event = entityMapper.toEventEntity(eventDTO);
        Event savedEvent = eventServices.addAffectEvenParticipant(event);
        return entityMapper.toEventDTO(savedEvent);
    }
    
    @PutMapping("/addAffectLog/{description}")
    public LogisticsDTO addAffectLog(@RequestBody LogisticsDTO logisticsDTO, @PathVariable("description") String descriptionEvent){
        Logistics logistics = entityMapper.toLogisticsEntity(logisticsDTO);
        Logistics savedLogistics = eventServices.addAffectLog(logistics, descriptionEvent);
        return entityMapper.toLogisticsDTO(savedLogistics);
    }
    
    @GetMapping("/getLogs/{d1}/{d2}")
    public List<LogisticsDTO> getLogistiquesDates (@PathVariable("d1") LocalDate date_debut, @PathVariable("d2") LocalDate date_fin){
        List<Logistics> logisticsList = eventServices.getLogisticsDates(date_debut, date_fin);
        return entityMapper.toLogisticsDTOList(logisticsList);
    }
}
