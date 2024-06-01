package it.epicode.progetto.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;

import java.time.LocalDate;
@Data
@Entity
public class Prenotazione {

    @Id
    @GeneratedValue
    private int id;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name ="evento_id")
    private Evento evento;

    private LocalDate dataPrenotazione;
}
