package com.security.service;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.security.dto.RegisterRequest;
import com.security.dto.UserDTO;
import com.security.model.Role;
import com.security.model.User;
import com.security.repository.UserRepository;
import com.security.reqeuest.AuthenticationRequest;
import com.security.response.AuthenticationResponse;

 

@Service
public class AuthenticationService {

	private final UserRepository repository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(AuthenticationService.class);
    
    public AuthenticationService(UserRepository repository, 
            PasswordEncoder passwordEncoder, 
            JwtService jwtService, 
            AuthenticationManager authenticationManager) {
					this.repository = repository;
					this.passwordEncoder = passwordEncoder;
					this.jwtService = jwtService;
					this.authenticationManager = authenticationManager;
			}
    
    

    public AuthenticationResponse register(RegisterRequest request) {
      /*  var user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .role(Role.USER)
                .isActive(true)
                .isNotLocked(true)
                .isChangePassword(false)
                .build();

        repository.save(user);*/
        
        User user = User.builder()
        	    .fullName(request.getFullName())
        	    .email(request.getEmail())
        	    .password(passwordEncoder.encode(request.getPassword()))
        	    .role(Role.USER)
        	    .joinDate(new Date())
        	    .isActive(request.isActive())
        	    .isNotLocked(request.isNotLocked())
        	    .isChangePassword(request.isChangePassword())
        	    .build();
        repository.save(user); 


        // Geração dos tokens JWT
        var jwtToken = jwtService.generateToken(user);
        var refreshToken = jwtService.generateRefreshToken(user);

        // Criando o DTO do usuário
        UserDTO userDTO = UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .lastLoginDate(user.getLastLoginDate())
                .lastLoginDateDisplay(user.getLastLoginDateDisplay())
                .joinDate(user.getJoinDate())
                .role(user.getRole().name())
                //.authorities(user.getAuthorities()) // Como já é um String[], não precisa de conversão
                .isActive(user.isActive())
                .isNotLocked(user.isNotLocked())
                .isChangePassword(user.isChangePassword())
                .build();

        return AuthenticationResponse.builder()
                .accessToken(jwtToken)
                .refreshToken(refreshToken)
                .user(userDTO) // Inclui o usuário na resposta
                .build();
    }


    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        try {
            authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                    request.getEmail(),
                    request.getPassword()
                )
            );

            var user = repository.findByEmail(request.getEmail())
                    .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));

            var jwtToken = jwtService.generateToken(user);
            var refreshToken = jwtService.generateRefreshToken(user);

            UserDTO userDTO = UserDTO.builder()
                    .id(user.getId())
                    .fullName(user.getFullName())
                    .email(user.getEmail())
                    .profileImageUrl(user.getProfileImageUrl())
                    .lastLoginDate(user.getLastLoginDate())
                    .lastLoginDateDisplay(user.getLastLoginDateDisplay())
                    .joinDate(user.getJoinDate())
                    .role(user.getRole().name())
                    //.authorities(user.getAuthorities())
                    .isActive(user.isActive())
                    .isNotLocked(user.isNotLocked())
                    .isChangePassword(user.isChangePassword())
                    .build();

            return AuthenticationResponse.builder()
                    .accessToken(jwtToken)
                    .refreshToken(refreshToken)
                    .user(userDTO)
                    .build();
        } catch (Exception e) {
        	 e.printStackTrace();
            throw new RuntimeException("Authentication failed", e);
        }
    }
    
    
    public UserDTO createDefaultUserIfNotExists(String email, String rawPassword, String fullName) {
        return repository.findByEmail(email)
                .map(user -> {
                    LOGGER.info("Default user already exists: {}", email);
                    return toDTO(user);
                })
                .orElseGet(() -> {
                    User user = User.builder()
                            .fullName(fullName)
                            .email(email)
                            .password(passwordEncoder.encode(rawPassword))
                            .role(Role.USER)
                            .isActive(true)
                            .isNotLocked(true)
                            .isChangePassword(false)
                            .joinDate(new Date())
                            .lastLoginDate(new Date())
                            .lastLoginDateDisplay(new Date())
                            .build();

                    repository.save(user);
                    LOGGER.info("Default user created  Succesfully: {}", email);
                    return toDTO(user);
                });
    }
    
    private UserDTO toDTO(User user) {
        return UserDTO.builder()
                .id(user.getId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .profileImageUrl(user.getProfileImageUrl())
                .lastLoginDate(user.getLastLoginDate())
                .lastLoginDateDisplay(user.getLastLoginDateDisplay())
                .joinDate(user.getJoinDate())
                .role(user.getRole().name())
                .isActive(user.isActive())
                .isNotLocked(user.isNotLocked())
                .isChangePassword(user.isChangePassword())
                .build();
    }


}