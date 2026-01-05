package com.github.maximovj.rhhub_app.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.github.maximovj.rhhub_app.config.security.ServicioJwt;
import com.github.maximovj.rhhub_app.dto.autenticacion.LoginInDto;
import com.github.maximovj.rhhub_app.dto.autenticacion.LoginOutDto;
import com.github.maximovj.rhhub_app.dto.autenticacion.InfoUsuarioOutDto;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.RenovarTokensEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.repository.RenovarTokensRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;
import com.github.maximovj.rhhub_app.service.RenovarTokensService;

import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/v1/autenticacion")
public class AutenticacionController {

    public static Logger logger = LoggerFactory.getLogger(AutenticacionController.class);

    @Autowired    
    private AuthenticationManager gestorAutenticacion;

    @Autowired
    private ServicioJwt servicioJwt;

    @Autowired
    private UserDetailsService servicioDetallesUsuario;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired 
    private RenovarTokensRepository renovarTokensRepository;

    @Autowired
    private RenovarTokensService renovarTokensService;

    // ---------------- LOGIN ----------------
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginInDto request, HttpServletResponse response) {
        boolean recuerdame = Boolean.valueOf(request.isRecuerdame());

        try {
            // Autenticar usuario
            gestorAutenticacion.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getContrasena())
            );
        } catch (Exception e) {
            return ApiResponse.unauthorized("Usuario y/o contraseña incorrectas", null);
        }

        // Buscar usuario
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByUsuarioWithDetails(request.getUsuario());
        if (usuarioOpt.isEmpty()) {
            return ApiResponse.notFound("Usuario no encontrado", null);
        }
        UsuarioEntity usuario = usuarioOpt.get();

        // Revocar todos los refresh tokens antiguos
        this.renovarTokensService.revokeAllTokens(usuario);

        // Generar Access Token
        String accessToken = servicioJwt.generarToken(usuario.getUsuario());

        // Crear nuevo Refresh Token
        RenovarTokensEntity refreshToken = this.renovarTokensService.createRefreshToken(usuario, recuerdame);

        // Guardar cookie HttpOnly
        ResponseCookie cookie = this.renovarTokensService.getCookie(refreshToken);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
        
        InfoUsuarioOutDto usuarioDto = InfoUsuarioOutDto.builder()
        .grupo(usuario.getGrupo().getNombre())
        .permisos(usuario.getGrupo().getPermisos())
        .rol(usuario.getGrupo().getRol().getRolNombre())
        .usuario(usuario.getUsuario())
        .usuarioId(usuario.getUsuarioId())
        .build();

        return ApiResponse.ok("Acceso exitosa", new LoginOutDto(accessToken, usuarioDto));
    }


    // ---------------- REFRESH TOKEN ----------------
    @PostMapping("/refresh")
    public ResponseEntity<?> refresh(@CookieValue(value = "renovar_token", required = false) String refreshTokenValue,
                                    HttpServletResponse response) {
        if (refreshTokenValue == null) {
            return ApiResponse.unauthorized("No se encontró cookie de renovar token", null);
        }

        Optional<RenovarTokensEntity> refreshTokenOpt = renovarTokensRepository.findByToken(refreshTokenValue);
        if (refreshTokenOpt.isEmpty()) {
            return ApiResponse.unauthorized("renovar token inválido", null);
        }

        RenovarTokensEntity refreshToken = refreshTokenOpt.get();

        if (!this.renovarTokensService.isValid(refreshToken)) {
            return ApiResponse.unauthorized("renovar token expirado o revocado", null);
        }

        // Buscar usuario
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByUsuarioWithDetails(refreshToken.getUsuario().getUsuario());
        if (usuarioOpt.isEmpty()) {
            return ApiResponse.notFound("Usuario no encontrado", null);
        }
        UsuarioEntity usuario = usuarioOpt.get();

        // Revocar antiguo token
        this.renovarTokensService.revoke(refreshToken);

        // Generar access token
        String accessToken = servicioJwt.generarToken(usuario.getUsuario());

        // Crear refresh token nuevo y cookie
        RenovarTokensEntity refreshTokenNuevo = this.renovarTokensService.createRefreshToken(usuario, refreshToken.isRecuerdame());
        ResponseCookie cookie = this.renovarTokensService.getCookie(refreshTokenNuevo);
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        InfoUsuarioOutDto usuarioDto = InfoUsuarioOutDto.builder()
        .grupo(usuario.getGrupo().getNombre())
        .permisos(usuario.getGrupo().getPermisos())
        .rol(usuario.getGrupo().getRol().getRolNombre())
        .usuario(usuario.getUsuario())
        .usuarioId(usuario.getUsuarioId())
        .build();

        return ApiResponse.ok("renovar token generada correctamente", new LoginOutDto(accessToken, usuarioDto));
    }


    // ---------------- VALIDAR JWT ----------------
    @PostMapping("/validate")
    public ResponseEntity<?> validarToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ApiResponse.badRequest("No se encontro acceso token", null);
        }

        String token = authHeader.substring(7);

        try {
            String nombreUsuario = servicioJwt.extraerNombreUsuario(token);
            UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);

            boolean esValido = servicioJwt.esTokenValido(token, detallesUsuario);

            return esValido ?   ApiResponse.ok("acceso token válido", null) : 
                                ApiResponse.unauthorized("acceso inválido o expirado", null);

        } catch (UsernameNotFoundException e) {
            return ApiResponse.notFound("Usuario no encontrado", null);
        } catch (Exception e) {
            return ApiResponse.unauthorized("acceso token inválido o expirado", null);
        }
    }

    // ---------------- LOGOUT GLOBAL ----------------
    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestBody String usuario, HttpServletResponse response) {
        logger.info("usuario: {}", usuario);
        // Buscar usuario
        Optional<UsuarioEntity> usuarioOpt = usuarioRepository.findByUsuarioWithDetails(usuario);
        if (usuarioOpt.isEmpty()) {
            return ApiResponse.notFound("Usuario no encontrado", null);
        }
        UsuarioEntity usuarioEntidad = usuarioOpt.get();

        // Revocar todos los refresh tokens
        this.renovarTokensService.revokeAllTokens(usuarioEntidad);

        // Eliminar cookie del cliente
        ResponseCookie cookie = this.renovarTokensService.removeCookie();
        response.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());

        return ApiResponse.ok("Sesión cerrada correctamente", null);
    }

}