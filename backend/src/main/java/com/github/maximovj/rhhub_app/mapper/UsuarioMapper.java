package com.github.maximovj.rhhub_app.mapper;

import java.util.Optional;

import org.springframework.security.crypto.password.PasswordEncoder;

import com.github.maximovj.rhhub_app.dto.records.UsuarioDTO;
import com.github.maximovj.rhhub_app.dto.request.UsuarioRequest;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public class UsuarioMapper {

    public static UsuarioDTO toDTO(UsuarioEntity e) 
    { 
        return new UsuarioDTO(e);
    }

    public static UsuarioDTO toDTOBasic(UsuarioEntity e) {
        return new UsuarioDTO(e.getUsuarioId(), 
            e.getUsuario(), 
            e.getCorreo(), 
            e.getToken(), 
            null, 
            null);

    }

    // Método para actualizar una nueva entidad desde un request
    public static void updateFromRequest(
            UsuarioEntity usuario, 
            UsuarioRequest req, 
            PasswordEncoder passwordEncoder
    ) {
        if (req.getUsuario() != null && !req.getUsuario().isBlank()) {
            usuario.setUsuario(req.getUsuario());
        }

        if (req.getCorreo() != null && !req.getCorreo().isBlank()) {
            usuario.setCorreo(req.getCorreo());
        }

        // Actualización de contraseña si es válida
        if (req.getContrasena() != null &&
            req.getConfirmar_contrasena() != null &&
            !req.getContrasena().trim().isBlank() &&
            !req.getConfirmar_contrasena().trim().isBlank() &&
            req.getContrasena().trim().equals(req.getConfirmar_contrasena().trim()) &&
            req.getContrasena().trim().length() >= 8 &&
            req.getContrasena().trim().length() <= 30
        ) {
            usuario.setContrasena(passwordEncoder.encode(req.getConfirmar_contrasena().trim()));
        }
    }

    // Método para crear una nueva entidad desde un request
    public static Optional<UsuarioEntity> fromRequest(UsuarioRequest req, PasswordEncoder passwordEncoder) {
        try {
            UsuarioEntity usuario = new UsuarioEntity();

            if (req.getUsuario() != null && !req.getUsuario().isBlank()) {
                usuario.setUsuario(req.getUsuario());
            }

            if (req.getCorreo() != null && !req.getCorreo().isBlank()) {
                usuario.setCorreo(req.getCorreo());
            }

            // Validación y encriptación de contraseña
            if (req.getContrasena() != null &&
                req.getConfirmar_contrasena() != null &&
                !req.getContrasena().trim().isBlank() &&
                req.getContrasena().trim().equals(req.getConfirmar_contrasena().trim()) &&
                req.getContrasena().trim().length() >= 8 &&
                req.getContrasena().trim().length() <= 30
            ) {
                usuario.setContrasena(passwordEncoder.encode(req.getConfirmar_contrasena().trim()));
            }

            usuario.setEsActivo(req.getEs_activo());
            return Optional.of(usuario);
        } catch (Exception e) {
            System.out.println("Hubo un error: %s".formatted(e.getMessage()));
            return Optional.empty();
        }

    }
    
}
