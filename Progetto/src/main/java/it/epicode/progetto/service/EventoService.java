package it.epicode.progetto.service;

import it.epicode.progetto.DTO.EventoDto;


import it.epicode.progetto.enums.Role;
import it.epicode.progetto.exception.UserNotFoundException;
import it.epicode.progetto.model.Evento;
import it.epicode.progetto.model.User;
import it.epicode.progetto.repository.EventoRepository;
import it.epicode.progetto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventoService {

    @Autowired
    private EventoRepository eventoRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public String saveEvento(EventoDto eventoDto, int idUtente) {

        Optional<User> userOptional = userRepository.findById(idUtente);
        if (userOptional.isPresent()) {
            Evento evento = new Evento();
            evento.setData(eventoDto.getData());
            evento.setDescrizione(eventoDto.getDescrizione());
            evento.setTitolo(eventoDto.getTitolo());
            evento.setLuogo(eventoDto.getLuogo());
            evento.setPostiDisponibili(eventoDto.getPostiDisponibili());
            evento.setUser(userOptional.get());
            eventoRepository.save(evento);
            return "Evento con id=" + evento.getId() + " correttamente salvato";
        } else {
            throw new UserNotFoundException("Utente non trovato");
        }
    }

    public List<Evento> getEventi() {
        return eventoRepository.findAll();
    }

    public Optional<Evento> getEventoById(int id) {
        return eventoRepository.findById(id);
    }

    public Evento updateEvento(int id, EventoDto eventoDto) {
        Optional<Evento> eventoOptional = getEventoById(id);
        if (eventoOptional.isPresent()) {
            Evento evento = eventoOptional.get();
            evento.setData(eventoDto.getData());
            evento.setDescrizione(eventoDto.getDescrizione());
            evento.setTitolo(eventoDto.getTitolo());
            evento.setLuogo(eventoDto.getLuogo());
            evento.setPostiDisponibili(eventoDto.getPostiDisponibili());

            return eventoRepository.save(evento);
        } else {
            throw new UserNotFoundException("User with id=" + id + " not found");
        }
    }

    public String deleteEvento(int id) {
        Optional<Evento> eventoOptional = getEventoById(id);

        if (eventoOptional.isPresent()) {
            eventoRepository.deleteById(id);
            return "Evento con id=" + id + " correttamente cancellato";
        } else {
            throw new UserNotFoundException("Evento con id=" + id + " non trovato");
        }
    }
}
