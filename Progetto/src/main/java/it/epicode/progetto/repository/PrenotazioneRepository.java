package it.epicode.progetto.repository;

import it.epicode.progetto.model.Prenotazione;
import it.epicode.progetto.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrenotazioneRepository extends JpaRepository<Prenotazione, Integer> {
    public List<Prenotazione> findByUserId(int userId);
}
