package com.example.projetomusicafinal.controllers;

import com.example.projetomusicafinal.repositories.UserRepository;
import com.example.projetomusicafinal.infra.security.TokenService;
import com.example.projetomusicafinal.user.AuthenticationDTO;
import com.example.projetomusicafinal.user.LoginResponseDTO;
import com.example.projetomusicafinal.user.RegisterDTO;
import com.example.projetomusicafinal.user.Usuario;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("usuarios")

public class AuthenticationController {

    @Autowired //chamando do securityConfiguration
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenService tokenService;

    @PostMapping("/login")
    public ResponseEntity login(@RequestBody @Valid AuthenticationDTO data) {
        try {
            var usernamePassword = new UsernamePasswordAuthenticationToken(data.username(), data.password());
            var auth = this.authenticationManager.authenticate(usernamePassword);

            var token = tokenService.generateToken((Usuario) auth.getPrincipal());

            return ResponseEntity.ok(new LoginResponseDTO(token));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuário ou senha incorretos.");
        }
    }
    
    @PostMapping("/novo-registro")
    public ResponseEntity register(@RequestBody @Valid RegisterDTO data) {
        if (this.userRepository.findByUsername(data.username()) != null) return ResponseEntity.badRequest().body("Usuário já existe.");

        String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
        Usuario newUser = new Usuario(data.username(), encryptedPassword);

        this.userRepository.save(newUser);

        return ResponseEntity.ok().body("Usuário criado com sucesso.");
    }
}