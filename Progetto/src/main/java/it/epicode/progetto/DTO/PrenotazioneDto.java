package it.epicode.progetto.DTO;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PrenotazioneDto {

    @NotNull(message = "Data obbligatoria")
    private LocalDate dataPrenotazione;
}
