package tn.esprit.eventsproject.controllers;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import tn.esprit.eventsproject.entities.Participant;
import tn.esprit.eventsproject.entities.Tache;
import tn.esprit.eventsproject.services.ITacheServices;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/tache")
public class TacheRestController {

    private final ITacheServices tacheServices;

    @GetMapping("/participants/{tache}")
    public List<Participant> getParticipantsByTache(@PathVariable Tache tache) {
        return tacheServices.getParticipantsByTache(tache);
    }

    @PutMapping("/assign/{idParticipant}/{tache}")
    public Participant assignTache(@PathVariable int idParticipant, @PathVariable Tache tache) {
        return tacheServices.assignTacheToParticipant(idParticipant, tache);
    }

    @GetMapping("/count/{tache}")
    public long countByTache(@PathVariable Tache tache) {
        return tacheServices.countParticipantsByTache(tache);
    }
}

