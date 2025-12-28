package com.github.maximovj.rhhub_app.config.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.config.properties.JwtProperties;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Service
public class ServicioJwt {

    @Autowired
    JwtProperties jwtProperties;

    private SecretKey obtenerClaveSecreta() {
        byte[] claveBytes = Decoders.BASE64.decode(jwtProperties.getSecretKey());
        return Keys.hmacShaKeyFor(claveBytes);
    }

    public String generarToken(String nombreUsuario) {
        return generarToken(new HashMap<>(), nombreUsuario);
    }

    public String generarToken(Map<String, Object> reclamacionesExtras, String nombreUsuario) {
        return Jwts.builder()
                .claims(reclamacionesExtras)
                .subject(nombreUsuario)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + jwtProperties.getExpirationTime()))
                .signWith(obtenerClaveSecreta())
                .compact();
    }

    public boolean esTokenValido(String token, UserDetails detallesUsuario) {
        final String nombreUsuario = extraerNombreUsuario(token);
        return (nombreUsuario.equals(detallesUsuario.getUsername())) && !esTokenExpirado(token);
    }

    private boolean esTokenExpirado(String token) {
        return extraerExpiracion(token).before(new Date());
    }

    private Date extraerExpiracion(String token) {
        return extraerReclamacion(token, Claims::getExpiration);
    }

    public String extraerNombreUsuario(String token) {
        return extraerReclamacion(token, Claims::getSubject);
    }

    public <T> T extraerReclamacion(String token, Function<Claims, T> reclamacionesResolver) {
        final Claims reclamaciones = extraerTodasLasReclamaciones(token);
        return reclamacionesResolver.apply(reclamaciones);
    }

    private Claims extraerTodasLasReclamaciones(String token) {
        return Jwts.parser()
                .verifyWith(obtenerClaveSecreta())
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }
}