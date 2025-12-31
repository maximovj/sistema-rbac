package com.github.maximovj.rhhub_app.service;

import java.time.Instant;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.config.properties.CookieRefreshTokenProperties;
import com.github.maximovj.rhhub_app.config.properties.JwtProperties;
import com.github.maximovj.rhhub_app.entity.RenovarTokensEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.repository.RenovarTokensRepository;

import jakarta.servlet.http.Cookie;
import jakarta.transaction.Transactional;
import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor
public class RenovarTokensService {

    public static Logger logger = LoggerFactory.getLogger(RenovarTokensService.class);

    RenovarTokensRepository refreshTokenRepository;
    
    CookieRefreshTokenProperties cookieRefreshTokenProperties;

    JwtProperties jwtProperties;

    public RenovarTokensEntity createRefreshToken(UsuarioEntity usuario, boolean recuerdame) {
        RenovarTokensEntity refreshToken = RenovarTokensEntity.builder()
                .usuario(usuario)
                .token(UUID.randomUUID().toString())
                .fechaDeExpiracion(Instant.now().plusMillis(jwtProperties.getRefreshExpiration()))
                .suspendido(false)
                .recuerdame(recuerdame)
                .build();
        
        if(refreshToken == null) return null;
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isValid(RenovarTokensEntity token) {
        logger.info("Refresh Token Recibido : {}", token.estaSuspendido());
        logger.info("Refresh Token Recibido : {}", token.getFechaDeExpiracion());
        return !token.estaSuspendido() && token.getFechaDeExpiracion().isAfter(Instant.now());
    }

    public Cookie getCookie(RenovarTokensEntity token) {
        // Configurar nueva cookie
        Cookie refreshTokenCookie = new Cookie(cookieRefreshTokenProperties.getName(), token.getToken());
        refreshTokenCookie.setHttpOnly(cookieRefreshTokenProperties.isHttpOnly());
        refreshTokenCookie.setSecure(cookieRefreshTokenProperties.isSecure());
        refreshTokenCookie.setPath(cookieRefreshTokenProperties.getPath());
        refreshTokenCookie.setMaxAge(token.isRecuerdame() ? cookieRefreshTokenProperties.getMaxAge() : 0);
        refreshTokenCookie.setAttribute("SameSite", cookieRefreshTokenProperties.getSameSite());
        return refreshTokenCookie;
    }

    public Cookie removeCookie() {
        // Configurar nueva cookie
        Cookie refreshTokenCookie = new Cookie(cookieRefreshTokenProperties.getName(), null);
        refreshTokenCookie.setHttpOnly(cookieRefreshTokenProperties.isHttpOnly());
        refreshTokenCookie.setSecure(cookieRefreshTokenProperties.isSecure());
        refreshTokenCookie.setPath(cookieRefreshTokenProperties.getPath());
        refreshTokenCookie.setMaxAge(0);
        refreshTokenCookie.setAttribute("SameSite", cookieRefreshTokenProperties.getSameSite());
        return refreshTokenCookie;
    }

    @Transactional
    public void revoke(RenovarTokensEntity token) {
        token.suspendido(true);
        refreshTokenRepository.save(token);
    }

    @Transactional
    public void revokeAllTokens(UsuarioEntity usuario) {
        refreshTokenRepository.deleteAllByUsuario(usuario);
    }
    
}
