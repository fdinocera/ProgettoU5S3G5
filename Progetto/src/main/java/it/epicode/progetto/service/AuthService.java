package it.epicode.progetto.service;

import it.epicode.progetto.DTO.UserLoginDto;
import it.epicode.progetto.exception.UnauthorizedException;
import it.epicode.progetto.model.User;
import it.epicode.progetto.security.JwtTool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtTool jwtTool;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public String authenticateUserAndCreateToken(UserLoginDto userLoginDto) {

        User user = userService.getUserByEmail(userLoginDto.getEmail());

        if (passwordEncoder.matches(userLoginDto.getPassword(), user.getPassword())){
            return jwtTool.createToken(user);
        }else {
            throw new UnauthorizedException("Error in authorization, relogin!");
        }
    }
}
