package it.epicode.progetto.service;

import it.epicode.progetto.DTO.PrenotazioneDto;
import it.epicode.progetto.exception.NotFoundException;
import it.epicode.progetto.model.Evento;
import it.epicode.progetto.model.Prenotazione;
import it.epicode.progetto.model.User;
import it.epicode.progetto.repository.EventoRepository;
import it.epicode.progetto.repository.PrenotazioneRepository;
import it.epicode.progetto.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PutMapping;

import java.util.List;
import java.util.Optional;

@Service
public class PrenotazioneService {

    @Autowired
    private PrenotazioneRepository prenotazioneRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private EventoRepository eventoRepository;

    public List<Prenotazione> getPrenotazioni() {
        return prenotazioneRepository.findAll();
    }

    public Optional<Prenotazione> getPrenotazioneById(int id) {

        return prenotazioneRepository.findById(id);
    }

    public List<Prenotazione> getPrenotazioniByUser(int idUser) {
        return prenotazioneRepository.findByUserId(idUser);
    }


    public String savePrenotazione(PrenotazioneDto prenotazioneDto, int idUtente, int idEvento) {

        Prenotazione prenotazione = new Prenotazione();

        Optional<User> optionalUser = userRepository.findById(idUtente);
        if (optionalUser.isPresent()) {
            prenotazione.setDataPrenotazione(prenotazioneDto.getDataPrenotazione());
            prenotazione.setUser(optionalUser.get());

            Optional<Evento> optionalEvento = eventoRepository.findById(idEvento);
            if (optionalEvento.isPresent()) {

                //controllo disponibilita posti
                int postiDisponibili = optionalEvento.get().getPostiDisponibili();
                if (postiDisponibili > 0) {
                    postiDisponibili--;
                    eventoRepository.findById(idEvento).get().setPostiDisponibili(postiDisponibili);
                } else {
                    return "Prenotazione non registrata per indisponibilità posti id prenotazione=" + prenotazione.getId();
                }
                prenotazione.setEvento(optionalEvento.get());
            } else {
                throw new NotFoundException("Evento non trovato id=" + idEvento);
            }
            prenotazioneRepository.save(prenotazione);
        } else {
            throw new NotFoundException("Utente non trovato id=" + idUtente);
        }

        return "Prenotazione registrata correttamente id=" + prenotazione.getId();
    }

    public Prenotazione updatePrenotazione(PrenotazioneDto prenotazioneDto, int id) {
        Optional<Prenotazione> optionalPrenotazione = prenotazioneRepository.findById(id);

        if (optionalPrenotazione.isPresent()) {
            Prenotazione prenotazione = optionalPrenotazione.get();
            prenotazione.setDataPrenotazione(prenotazioneDto.getDataPrenotazione());

            return prenotazioneRepository.save(prenotazione);
        } else {
            throw new NotFoundException("Prenotazione non trovata id prenotazione=" + id);
        }
    }

    public String deletePrenotazione(int id) {
        Optional<Prenotazione> optionalPrenotazione = getPrenotazioneById(id);

        if (optionalPrenotazione.isPresent()) {
            prenotazioneRepository.deleteById(id);
            return "Prenotazione correttamente cancellata id=" + id;
        } else {
            throw new NotFoundException("Prenotazione non trovata id=" + id);
        }
    }
}
