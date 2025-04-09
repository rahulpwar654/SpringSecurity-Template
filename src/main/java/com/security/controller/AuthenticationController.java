package com.security.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.security.dto.RegisterRequest;
import com.security.dto.UserDTO;
import com.security.reqeuest.AuthenticationRequest;
import com.security.response.AuthenticationResponse;
import com.security.service.AuthenticationService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
//@CrossOrigin(origins = "http://localhost:4200", allowCredentials = "true")
@Tag(name = "Authentication", description = "Endpoints for user authentication and registration")
public class AuthenticationController {
    
    private final AuthenticationService authenticationService;
    private final KafkaTemplate<String, RegisterRequest> kafkaTemplate;
    

    @PostMapping("/register")
    @Operation(
        summary = "Register new user",
        description = "Creates a new user in the system and returns a valid JWT token"
    )
    public ResponseEntity<UserDTO> register(@RequestBody RegisterRequest request) {
        AuthenticationResponse response = authenticationService.register(request);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + response.getAccessToken())
                .header("Refresh-Token", response.getRefreshToken())
                .body(response.getUser()); 
    }
    
    @PostMapping("/registerKafka")
    @Operation(
        summary = "Register new user via Kafka",
        description = "Envia os dados do usuário para um tópico Kafka para ser processado"
    )
    public ResponseEntity<String> registerKafka(@RequestBody RegisterRequest request) {
        kafkaTemplate.send("user-register-topic", request); 
        return ResponseEntity.accepted().body("Usuário enviado para o tópico Kafka com sucesso");
    }


   
    @PostMapping("/authenticate")
    @Operation(
        summary = "Authenticate user",
        description = "Authenticates the user with email and password and returns a valid JWT token"
    )
    public ResponseEntity<UserDTO> authenticate(@RequestBody AuthenticationRequest request) {
        AuthenticationResponse response = authenticationService.authenticate(request);

        return ResponseEntity.ok()
                .header("Authorization", "Bearer " + response.getAccessToken())
                .header("Refresh-Token", response.getRefreshToken())
                .body(response.getUser());
    }
    
}