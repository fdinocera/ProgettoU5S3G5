package it.epicode.progetto.controller;

import it.epicode.progetto.DTO.PrenotazioneDto;
import it.epicode.progetto.exception.BadRequestException;
import it.epicode.progetto.exception.NotFoundException;
import it.epicode.progetto.model.Prenotazione;
import it.epicode.progetto.model.User;
import it.epicode.progetto.repository.PrenotazioneRepository;
import it.epicode.progetto.service.PrenotazioneService;
import it.epicode.progetto.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.parameters.P;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@RestController
public class PrenotazioneController {

    @Autowired
    private PrenotazioneService prenotazioneService;

    @GetMapping("api/prenotazioni")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public List<Prenotazione> getPrenotazioni() {
        return prenotazioneService.getPrenotazioni();
    }

    @GetMapping("/api/prenotazioni/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Prenotazione getPrenotazioneById(@PathVariable int id) {
        Optional<Prenotazione> prenotazioneOptional = prenotazioneService.getPrenotazioneById(id);

        if (prenotazioneOptional.isPresent()) {
            return prenotazioneOptional.get();
        } else {
            throw new NotFoundException("La prenotazione con id=" + id + " non trovata");
        }
    }

    @PostMapping("/api/prenotazioni/{idUtente}/{idEvento}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public String savePrenotazione(@RequestBody PrenotazioneDto prenotazioneDto, @PathVariable int idUtente, @PathVariable int idEvento, BindingResult bindingResult) {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(error -> error.getDefaultMessage()).reduce("", (s, s2) -> s + s2));
        }
        return prenotazioneService.savePrenotazione(prenotazioneDto, idUtente, idEvento);
    }

    @PutMapping("/api/prenotazioni/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Prenotazione updatePrenotazione(@RequestBody PrenotazioneDto prenotazioneDto, @PathVariable int id, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(error -> error.getDefaultMessage()).reduce("", (s, s2) -> s + s2));
        }
        return prenotazioneService.updatePrenotazione(prenotazioneDto, id);
    }

    @DeleteMapping("/api/prenotazioni/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deletePrenotazione(@PathVariable int id) {
        return prenotazioneService.deletePrenotazione(id);
    }

    @GetMapping("/api/eventipersona/{id}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public List<Prenotazione> getPrenotazioneByUser(@PathVariable int id) {
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioni();

        return prenotazioni.stream()
                .filter(prenotazione -> prenotazione.getUser().getId() == id)
                .collect(Collectors.toList());
    }

    @DeleteMapping("/api/eventipersona/{idUtente}/{idEvento}")
    @PreAuthorize("hasAnyAuthority('USER')")
    public String deletePrenotazioneByUser(@PathVariable int idUtente, @PathVariable int idEvento) {
        List<Prenotazione> prenotazioni = prenotazioneService.getPrenotazioni();

        List<Prenotazione> result = prenotazioni.stream()
                .filter(prenotazione -> prenotazione.getUser().getId() == idUtente)
                .filter(prenotazione -> prenotazione.getEvento().getId() == idEvento)
                .collect(Collectors.toList());

        if (result.size() > 0) {
            prenotazioneService.deletePrenotazione(result.get(0).getId());
            return "Prenotazione correttamente cancellata id prenotazione=" + result.get(0).getId();
        }
        return "Prenotazione non trovata";
    }
}
