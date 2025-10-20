package tn.esprit.eventsproject.services;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;
import tn.esprit.eventsproject.repositories.ParticipantRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class TacheServicesImpl implements ITacheServices {

    private final ParticipantRepository participantRepository;

    @Override
    public List<Participant> getParticipantsByTache(Tache tache) {
        System.out.println("Retrieving participants with tache: " + tache);
        List<Participant> participants = participantRepository.findAll().stream()
                .filter(p -> p.getTache() == tache)
                .collect(Collectors.toList());
        System.out.println("Found " + participants.size() + " participants with tache " + tache);
        return participants;
    }

    @Override
    public Participant assignTacheToParticipant(int idParticipant, Tache tache) {
        System.out.println("Assigning tache " + tache + " to participant ID: " + idParticipant);
        Participant participant = participantRepository.findById(idParticipant)
                .orElseThrow(() -> new RuntimeException("Participant not found"));
        participant.setTache(tache);
        Participant saved = participantRepository.save(participant);
        System.out.println("Successfully assigned tache " + tache + " to participant " + saved.getNom() + " " + saved.getPrenom());
        return saved;
    }

    @Override
    public long countParticipantsByTache(Tache tache) {
        System.out.println("Counting participants with tache: " + tache);
        long count = participantRepository.findAll().stream()
                .filter(p -> p.getTache() == tache)
                .count();
        System.out.println("Total participants with tache " + tache + ": " + count);
        return count;
    }
}

