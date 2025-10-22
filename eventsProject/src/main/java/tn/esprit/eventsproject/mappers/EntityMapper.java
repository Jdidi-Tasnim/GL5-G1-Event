package tn.esprit.eventsproject.mappers;

import org.springframework.stereotype.Component;
import tn.esprit.eventsproject.dto.EventDTO;
import tn.esprit.eventsproject.dto.LogisticsDTO;
import tn.esprit.eventsproject.dto.ParticipantDTO;
import tn.esprit.eventsproject.dto.UserDTO;
import tn.esprit.eventsproject.entities.Event;
import tn.esprit.eventsproject.entities.Logistics;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.User;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class EntityMapper {

    public ParticipantDTO toParticipantDTO(Participant participant) {
        if (participant == null) return null;
        return new ParticipantDTO(
                participant.getIdPart(),
                participant.getNom(),
                participant.getPrenom(),
                participant.getTache()
        );
    }

    public Participant toParticipantEntity(ParticipantDTO dto) {
        if (dto == null) return null;
        Participant participant = new Participant();
        participant.setIdPart(dto.getIdPart());
        participant.setNom(dto.getNom());
        participant.setPrenom(dto.getPrenom());
        participant.setTache(dto.getTache());
        return participant;
    }

    public EventDTO toEventDTO(Event event) {
        if (event == null) return null;
        return new EventDTO(
                event.getIdEvent(),
                event.getDescription(),
                event.getDateDebut(),
                event.getDateFin(),
                event.getCout()
        );
    }

    public Event toEventEntity(EventDTO dto) {
        if (dto == null) return null;
        Event event = new Event();
        event.setIdEvent(dto.getIdEvent());
        event.setDescription(dto.getDescription());
        event.setDateDebut(dto.getDateDebut());
        event.setDateFin(dto.getDateFin());
        event.setCout(dto.getCout());
        return event;
    }

    public LogisticsDTO toLogisticsDTO(Logistics logistics) {
        if (logistics == null) return null;
        return new LogisticsDTO(
                logistics.getIdLog(),
                logistics.getDescription(),
                logistics.isReserve(),
                logistics.getPrixUnit(),
                logistics.getQuantite()
        );
    }

    public Logistics toLogisticsEntity(LogisticsDTO dto) {
        if (dto == null) return null;
        Logistics logistics = new Logistics();
        logistics.setIdLog(dto.getIdLog());
        logistics.setDescription(dto.getDescription());
        logistics.setReserve(dto.getReserve());
        logistics.setPrixUnit(dto.getPrixUnit());
        logistics.setQuantite(dto.getQuantite());
        return logistics;
    }

    public UserDTO toUserDTO(User user) {
        if (user == null) return null;
        return new UserDTO(
                user.getId(),
                user.getUsername(),
                user.getEmail()
                // Note: Password is intentionally not mapped to DTO
        );
    }

    public User toUserEntity(UserDTO dto, String password) {
        if (dto == null) return null;
        User user = new User();
        user.setId(dto.getId());
        user.setUsername(dto.getUsername());
        user.setEmail(dto.getEmail());
        user.setPassword(password); // Password is handled separately
        return user;
    }
    
    // Helper method to convert list of entities to DTOs
    public List<LogisticsDTO> toLogisticsDTOList(List<Logistics> logisticsList) {
        if (logisticsList == null) return null;
        return logisticsList.stream()
                .map(this::toLogisticsDTO)
                .collect(Collectors.toList());
    }
    
    public List<UserDTO> toUserDTOList(List<User> users) {
        if (users == null) return null;
        return users.stream()
                .map(this::toUserDTO)
                .collect(Collectors.toList());
    }
}