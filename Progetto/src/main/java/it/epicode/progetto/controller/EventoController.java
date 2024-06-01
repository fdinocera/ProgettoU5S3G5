package it.epicode.progetto.controller;

import com.fasterxml.jackson.annotation.JsonIgnore;
import it.epicode.progetto.DTO.EventoDto;
import it.epicode.progetto.exception.BadRequestException;
import it.epicode.progetto.exception.NotFoundException;
import it.epicode.progetto.model.Evento;
import it.epicode.progetto.service.EventoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
public class EventoController {

    @Autowired
    private EventoService eventoService;

    @GetMapping("/api/eventi")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public List<Evento> getEventi() {
        return eventoService.getEventi();
    }

    @GetMapping("/api/eventi/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN', 'USER')")
    public Evento getEventoById(@PathVariable int id) {
        Optional<Evento> eventoOptional = eventoService.getEventoById(id);

        if (eventoOptional.isPresent()) {
            return eventoOptional.get();
        } else {
            throw new NotFoundException("Evento con id=" + id + " non trovato!");
        }
    }

    @PostMapping("/api/eventi/{idUtente}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String saveEvento(@RequestBody @Validated EventoDto eventoDto, @PathVariable int idUtente, BindingResult bindingResult) throws BadRequestException {

        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().
                    map(objectError -> objectError.getDefaultMessage()).reduce("", (s, s2) -> s + s2));
        }
        return eventoService.saveEvento(eventoDto, idUtente);
    }

    @PutMapping("/api/eventi/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public Evento updateEvento(@PathVariable int id, @RequestBody @Validated EventoDto eventoDto, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new BadRequestException(bindingResult.getAllErrors().stream().map(error -> error.getDefaultMessage()).
                    reduce("", (s, s2) -> s + s2));
        }
        return eventoService.updateEvento(id, eventoDto);
    }

    @DeleteMapping("/api/eventi/{id}")
    @PreAuthorize("hasAnyAuthority('ADMIN')")
    public String deleteEvento(@PathVariable int id) {
        return eventoService.deleteEvento(id);
    }
}
