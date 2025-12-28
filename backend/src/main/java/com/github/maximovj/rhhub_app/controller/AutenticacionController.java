package com.github.maximovj.rhhub_app.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.config.security.ServicioJwt;
import com.github.maximovj.rhhub_app.dto.autenticacion.LoginInDto;
import com.github.maximovj.rhhub_app.dto.autenticacion.LoginOutDto;

@RestController
@RequestMapping("/api/v1/autenticacion")
public class AutenticacionController {

    @Autowired    
    AuthenticationManager gestorAutenticacion;
    
    @Autowired
    ServicioJwt servicioJwt;
    
    @Autowired
    UserDetailsService servicioDetallesUsuario;

    @PostMapping("/autenticar")
    public ResponseEntity<LoginOutDto> autenticar(
            @RequestBody LoginInDto solicitud) {
        
        gestorAutenticacion.authenticate(
            new UsernamePasswordAuthenticationToken(
                solicitud.getUsuario(),
                solicitud.getContrasena()
            )
        );
        
        final UserDetails detallesUsuario = servicioDetallesUsuario
                .loadUserByUsername(solicitud.getUsuario());
        
        final String token = servicioJwt.generarToken(detallesUsuario.getUsername());
        
        return ResponseEntity.ok(new LoginOutDto(token));
    }

    @PostMapping("/validar")
    public ResponseEntity<Boolean> validarToken(@RequestHeader("Authorization") String token) {
        if (token != null && token.startsWith("Bearer ")) {
            String jwt = token.substring(7);
            try {
                String nombreUsuario = servicioJwt.extraerNombreUsuario(jwt);
                UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);
                return ResponseEntity.ok(servicioJwt.esTokenValido(jwt, detallesUsuario));
            } catch (Exception e) {
                return ResponseEntity.ok(false);
            }
        }
        return ResponseEntity.ok(false);
    }

    
    
}
