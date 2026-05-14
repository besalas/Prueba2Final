package com.ecommerce.identity.services;

import com.ecommerce.identity.dto.AuthRequestDTO;
import com.ecommerce.identity.dto.UserRegisterDTO;
import com.ecommerce.identity.dto.UserResponseDTO;
import com.ecommerce.identity.entity.AppUser;
import com.ecommerce.identity.exceptions.ResourceNotFoundException;
import com.ecommerce.identity.exceptions.UnauthorizedException;
import com.ecommerce.identity.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;

    public UserResponseDTO register(UserRegisterDTO requestDTO) {
        log.info("Intentando registrar nuevo usuario con email: {}", requestDTO.getEmail());
        
        if (userRepository.existsByEmail(requestDTO.getEmail())) {
            log.warn("El correo {} ya está registrado", requestDTO.getEmail());
            throw new RuntimeException("El correo ya está en uso");
        }

        AppUser user = new AppUser();
        BeanUtils.copyProperties(requestDTO, user);

        
        AppUser savedUser = userRepository.save(user);
        log.info("Usuario registrado con éxito. ID: {}", savedUser.getId());
        
        return mapToResponse(savedUser, "Registro exitoso");
    }

    public UserResponseDTO login(AuthRequestDTO authRequest) {
        log.info("Intento de login para el email: {}", authRequest.getEmail());
        
        AppUser user = userRepository.findByEmail(authRequest.getEmail())
                .orElseThrow(() -> {
                    log.error("Usuario no encontrado con email: {}", authRequest.getEmail());
                    return new ResourceNotFoundException("Usuario no registrado");
                });

        if (!user.getPassword().equals(authRequest.getPassword())) {
            log.error("Credenciales inválidas para el email: {}", authRequest.getEmail());
            throw new UnauthorizedException("Contraseña incorrecta");
        }

        log.info("Login exitoso para el usuario: {}", user.getUsername());

        return mapToResponse(user, "mock-jwt-token-123456789");
    }

    private UserResponseDTO mapToResponse(AppUser user, String token) {
        UserResponseDTO response = new UserResponseDTO();
        BeanUtils.copyProperties(user, response);
        response.setToken(token);
        return response;
    }
}