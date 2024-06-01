package it.epicode.progetto.DTO;


import it.epicode.progetto.model.User;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.apache.catalina.realm.UserDatabaseRealm;

import java.time.LocalDate;

@Data
public class EventoDto {


    @NotNull(message = "Titolo obbligatorio")
    private String titolo;

    private String descrizione;

    @NotNull(message = "Data obbligatoria")
    private LocalDate data;

    @NotNull(message = "Luogo obbligatorio")
    private String luogo;

    @NotNull(message = "Posti disponibili obbligatori")
    private int postiDisponibili;
}
