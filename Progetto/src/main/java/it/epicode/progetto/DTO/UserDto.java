package it.epicode.progetto.DTO;

import it.epicode.progetto.model.User;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class UserDto {

    private String name;
    private String surname;

    @Email
    @NotBlank (message = "l'email non puo essere vuota o mancante o composta da soli spazi")
    private String email;

    @NotBlank(message = "La password non puo essere vuota o mancante o composta da soli spazi")
    private String password;
}
