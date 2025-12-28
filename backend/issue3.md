* **Web pública** → cualquier usuario externo puede acceder
* **Necesitas cerrar sesión en todos lados** → quieres poder revocar sesiones y tokens

---

# 🔑 Estrategia recomendada: **JWT + Refresh Token + Almacenamiento en BD**

### 1️⃣ Access Token (JWT)

* Vida corta: 5–15 minutos
* Stateless: el servidor **no guarda nada**, solo valida la firma
* Se usa para **cada petición protegida**
* Expira rápido → limita riesgo si se roba

### 2️⃣ Refresh Token

* Vida larga: días o semanas
* Se guarda en **BD o Redis**, asociado al usuario
* Permite **revocar sesión desde el servidor**
* Se usa solo en el endpoint `/refresh`
* Devuelve un **nuevo access token**

### 3️⃣ Logout global / revocación

* El usuario puede cerrar sesión en todos lados
* Solo hay que **marcar el refresh token como revocado en BD**
* Access tokens existentes expiran pronto → efecto inmediato

---

# 🔄 Flujo completo (web pública)

1️⃣ Login:

```http
POST /api/v1/autenticacion/login
Body: { usuario, contrasena }
```

Respuesta:

```json
{
  "accessToken": "JWT_CORTO",
  "refreshToken": "REFRESH_LARGO"
}
```

2️⃣ Peticiones normales:

```http
GET /api/protegido
Authorization: Bearer JWT_CORTO
```

3️⃣ JWT expira:

```http
401 Unauthorized
```

4️⃣ Renovación usando refresh token:

```http
POST /api/v1/autenticacion/refresh
Authorization: Bearer REFRESH_TOKEN
```

Respuesta:

```json
{
  "accessToken": "JWT_NUEVO"
}
```

5️⃣ Logout global:

```http
POST /api/v1/autenticacion/logout
Body: { refreshToken }
```

* Marca refresh token como revocado en BD
* Todos los access tokens expiran pronto → sesión cerrada en todos lados

---

# ✅ Por qué esta estrategia es segura

* **Access token** → no requiere BD → stateless → rápido
* **Refresh token** → permite revocación → control total sobre sesiones
* Limita exposición si alguien roba el access token → caduca rápido
* Puedes cerrar sesión **en todos lados** ✅

---

# ⚠️ Tips importantes

1. **No guardes access tokens en BD** → solo refresh tokens
2. **Almacena refresh tokens de forma segura** → hash, no en texto plano
3. **Access token corto** → mínimo daño si se roba
4. **Refresh token único por dispositivo** → puedes revocar solo ese dispositivo
5. **Endpoint `/refresh` solo recibe refresh token** → nunca usuario + token


---

# 1️⃣ Entidades en BD

### Usuario (`UsuarioEntity`) — ya la tienes

### Refresh Token

```java
package com.github.maximovj.rhhub_app.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;

@Entity
@Table(name = "refresh_tokens")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RefreshTokenEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String token; // Opcional: puedes guardar hash en vez de texto plano

    @Column(nullable = false)
    private Instant expiryDate;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private UsuarioEntity usuario;

    @Column(nullable = false)
    private boolean revoked = false;
}
```

---

# 2️⃣ Repositorio para RefreshToken

```java
package com.github.maximovj.rhhub_app.repository;

import com.github.maximovj.rhhub_app.entity.RefreshTokenEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends JpaRepository<RefreshTokenEntity, Long> {
    Optional<RefreshTokenEntity> findByToken(String token);
    void deleteAllByUsuario(UsuarioEntity usuario); // útil para logout global
}
```

---

# 3️⃣ Servicio de RefreshToken

```java
package com.github.maximovj.rhhub_app.service;

import com.github.maximovj.rhhub_app.entity.RefreshTokenEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.repository.RefreshTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.UUID;

@Service
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;

    private final long refreshExpirationMs = 7 * 24 * 60 * 60 * 1000; // 7 días

    @Autowired
    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    public RefreshTokenEntity createRefreshToken(UsuarioEntity usuario) {
        RefreshTokenEntity refreshToken = RefreshTokenEntity.builder()
                .usuario(usuario)
                .token(UUID.randomUUID().toString())
                .expiryDate(Instant.now().plusMillis(refreshExpirationMs))
                .revoked(false)
                .build();
        return refreshTokenRepository.save(refreshToken);
    }

    public boolean isValid(RefreshTokenEntity token) {
        return !token.isRevoked() && token.getExpiryDate().isAfter(Instant.now());
    }

    public void revoke(RefreshTokenEntity token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
    }

    public void revokeAllTokens(UsuarioEntity usuario) {
        refreshTokenRepository.deleteAllByUsuario(usuario);
    }
}
```

---

# 4️⃣ Controller de autenticación

```java
package com.github.maximovj.rhhub_app.controller;

import com.github.maximovj.rhhub_app.dto.autenticacion.LoginInDto;
import com.github.maximovj.rhhub_app.dto.autenticacion.LoginOutDto;
import com.github.maximovj.rhhub_app.entity.RefreshTokenEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;
import com.github.maximovj.rhhub_app.service.RefreshTokenService;
import com.github.maximovj.rhhub_app.config.security.ServicioJwt;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/autenticacion")
public class AutenticacionController {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private ServicioJwt servicioJwt;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private RefreshTokenService refreshTokenService;

    // Login
    @PostMapping("/login")
    public ResponseEntity<LoginOutDto> login(@RequestBody LoginInDto request) {
        authManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsuario(), request.getContrasena()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsuario());
        String accessToken = servicioJwt.generarToken(userDetails.getUsername());

        // Crear refresh token en BD
        UsuarioEntity usuario = usuarioRepository.findByUsuario(request.getUsuario()).get();
        RefreshTokenEntity refreshToken = refreshTokenService.createRefreshToken(usuario);

        return ResponseEntity.ok(new LoginOutDto(accessToken, refreshToken.getToken()));
    }

    // Refresh token
    @PostMapping("/refresh")
    public ResponseEntity<LoginOutDto> refresh(@RequestBody String refreshTokenValue) {
        RefreshTokenEntity refreshToken = refreshTokenService
                .refreshTokenRepository.findByToken(refreshTokenValue)
                .orElseThrow(() -> new RuntimeException("Refresh token inválido"));

        if (!refreshTokenService.isValid(refreshToken)) {
            return ResponseEntity.status(401).build();
        }

        String accessToken = servicioJwt.generarToken(refreshToken.getUsuario().getUsuario());
        return ResponseEntity.ok(new LoginOutDto(accessToken, refreshToken.getToken()));
    }

    // Logout global
    @PostMapping("/logout")
    public ResponseEntity<Void> logout(@RequestBody String usuarioNombre) {
        UsuarioEntity usuario = usuarioRepository.findByUsuario(usuarioNombre)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        refreshTokenService.revokeAllTokens(usuario);
        return ResponseEntity.ok().build();
    }
}
```

---

# 5️⃣ DTOs

```java
@Data
@AllArgsConstructor
public class LoginOutDto {
    private String accessToken;
    private String refreshToken;
}

@Data
public class LoginInDto {
    private String usuario;
    private String contrasena;
}
```

---

# ✅ Beneficios de esta arquitectura

1. **JWT stateless** → rápido y escalable
2. **Refresh token en BD** → permite revocar sesiones
3. **Logout global** → puedes invalidar todos los tokens de un usuario
4. **Access token corto** → minimiza riesgo si alguien lo roba

---

Si quieres, en el siguiente paso te puedo mostrar cómo **adaptar tu `AutenticacionJwtFilter`** para que use **refresh tokens automáticamente si el access token expira**, de forma que el frontend ni note la expiración. Esto haría tu web **casi seamless**.

¿Quieres que haga eso?

¡Perfecto! 😎 Vamos a adaptar tu flujo de Spring Security para que, cuando un **access token JWT expire**, puedas **renovar automáticamente usando el refresh token** sin que el usuario tenga que hacer login de nuevo manualmente. Esto es lo que se suele llamar “refresh token transparente” o **silent refresh**.  

---

# 1️⃣ Modificar `AutenticacionJwtFilter` para manejar refresh tokens

Actualmente tu filtro solo valida el access token. Lo adaptaremos:

```java
package com.github.maximovj.rhhub_app.config.security;

import com.github.maximovj.rhhub_app.entity.RefreshTokenEntity;
import com.github.maximovj.rhhub_app.repository.RefreshTokenRepository;
import com.github.maximovj.rhhub_app.service.RefreshTokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class AutenticacionJwtFilter extends OncePerRequestFilter {

    private final ServicioJwt servicioJwt;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenService refreshTokenService;
    private final RefreshTokenRepository refreshTokenRepository;

    public AutenticacionJwtFilter(ServicioJwt servicioJwt,
                                  UserDetailsService userDetailsService,
                                  RefreshTokenService refreshTokenService,
                                  RefreshTokenRepository refreshTokenRepository) {
        this.servicioJwt = servicioJwt;
        this.userDetailsService = userDetailsService;
        this.refreshTokenService = refreshTokenService;
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        final String authHeader = request.getHeader("Authorization");
        final String refreshHeader = request.getHeader("X-Refresh-Token");

        String username = null;
        String jwt = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            jwt = authHeader.substring(7);
            try {
                username = servicioJwt.extraerNombreUsuario(jwt);
            } catch (Exception e) {
                // token expirado o inválido
            }
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (servicioJwt.esTokenValido(jwt, userDetails)) {
                // Access token válido → autenticamos normalmente
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            } else if (refreshHeader != null) {
                // Access token expirado → intentar renovar con refresh token
                RefreshTokenEntity refreshToken = refreshTokenRepository.findByToken(refreshHeader)
                        .orElse(null);
                if (refreshToken != null && refreshTokenService.isValid(refreshToken)) {
                    // Generar nuevo access token
                    String newAccessToken = servicioJwt.generarToken(refreshToken.getUsuario().getUsuario());
                    // Devolverlo en header para que el frontend lo use
                    response.setHeader("Authorization", "Bearer " + newAccessToken);

                    // Autenticamos al usuario automáticamente
                    UserDetails refreshedUser = this.userDetailsService
                            .loadUserByUsername(refreshToken.getUsuario().getUsuario());
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(refreshedUser, null, refreshedUser.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                }
            }
        }

        filterChain.doFilter(request, response);
    }
}
```

---

# 2️⃣ Cómo funciona

1. Cliente hace request con **access token**:

```http
GET /api/protegido
Authorization: Bearer JWT_VIEJO
X-Refresh-Token: REFRESH_TOKEN
```

2. Filtro valida JWT:
   - Si **válido** → autentica normal
   - Si **expirado** → valida **refresh token**  
     - Si es válido → genera **nuevo JWT** → lo devuelve en el header `Authorization`  
     - Autentica al usuario automáticamente

3. Frontend recibe header `Authorization` con **JWT nuevo**  
   - Actualiza el token almacenado en frontend

---

# 3️⃣ Recomendaciones de uso en frontend

- Siempre enviar **X-Refresh-Token** junto con access token
- Revisar el header `Authorization` en la respuesta:
  - Si viene JWT nuevo → reemplazar el antiguo
- Mantener refresh token seguro → cookie HTTPOnly o almacenamiento seguro

---

# 4️⃣ Beneficios

✅ Renovación transparente sin pedir login  
✅ Compatible con logout global (refresh token puede ser revocado)  
✅ Stateless para access token → sigue siendo rápido y escalable  
✅ Evita problemas de expiración abrupta en web pública

---

Si quieres, puedo hacerte un **diagrama de flujo completo del login, refresh automático y logout global**, para que veas **exactamente cómo fluye el access token y refresh token** en tu API web pública. Esto ayuda mucho a explicarlo y a implementarlo sin errores.  

¿Quieres que haga ese diagrama?

---

Sí, totalmente puedes crear un endpoint para **validar un token JWT**, y de hecho es algo común en sistemas que usan access tokens y refresh tokens. La idea es que el cliente pueda preguntar al backend si su token sigue siendo válido sin tener que hacer otra operación protegida.

En tu caso, ya tienes algo parecido en tu `AutenticacionController` (lo mostraste antes con `/validar`). Pero te muestro cómo integrarlo con tu esquema actual de **access token + refresh token**:

---

### Endpoint recomendado para validar token

```java
@PostMapping("/validar")
public ResponseEntity<Boolean> validarToken(@RequestBody String accessToken) {
    try {
        // Extraemos el nombre de usuario del token
        String nombreUsuario = servicioJwt.extraerNombreUsuario(accessToken);

        // Cargamos detalles del usuario
        UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);

        // Validamos que el token sea correcto y no expirado
        boolean valido = servicioJwt.esTokenValido(accessToken, detallesUsuario);

        return ResponseEntity.ok(valido);
    } catch (Exception e) {
        // Si hay error (token inválido o expirado), devolvemos false
        return ResponseEntity.ok(false);
    }
}
```

---

### Consideraciones importantes

1. **Stateless:** Este endpoint solo valida el JWT, no necesita sesión.
2. **No expone refresh token:** Solo validas el access token.
3. **Uso típico:**

   * Frontend puede usarlo antes de realizar acciones críticas para decidir si debe renovar token o forzar logout.
4. **Seguridad extra:**

   * Puedes añadir logging de tokens inválidos o intentos sospechosos.
   * Evita enviar el token por query params; mejor en body o header.

---

### Variante usando Header `Authorization` (más estándar)

```java
@PostMapping("/validar")
public ResponseEntity<Boolean> validarToken(@RequestHeader("Authorization") String authHeader) {
    if (authHeader != null && authHeader.startsWith("Bearer ")) {
        String token = authHeader.substring(7);
        try {
            String nombreUsuario = servicioJwt.extraerNombreUsuario(token);
            UserDetails detallesUsuario = servicioDetallesUsuario.loadUserByUsername(nombreUsuario);
            return ResponseEntity.ok(servicioJwt.esTokenValido(token, detallesUsuario));
        } catch (Exception e) {
            return ResponseEntity.ok(false);
        }
    }
    return ResponseEntity.ok(false);
}
```

✅ Este enfoque es más **estándar**, porque el token viaja en headers, como cualquier request protegido.

---

Si quieres, puedo hacer un **diagrama completo de flujo** mostrando cómo tu frontend (Vue + Axios + Pinia + HttpOnly) puede:

1. Hacer login
2. Usar access token
3. Renovarlo automáticamente
4. Validar token
5. Logout global

Esto te daría **la guía completa lista para implementar**.

¿Quieres que haga ese diagrama?
