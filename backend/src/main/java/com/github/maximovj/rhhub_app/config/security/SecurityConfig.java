package com.github.maximovj.rhhub_app.config.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import lombok.AllArgsConstructor;

import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.github.maximovj.rhhub_app.config.properties.CorsProperties;

@AllArgsConstructor
@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final AutenticacionJwtFilter filtroAutenticacionJwt;

    @Bean
    public SecurityFilterChain filtroSeguridad(
        HttpSecurity http, 
        AuthenticationProvider authenticationProvider,
        CorsProperties corsProperties
    ) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(cors -> cors.configurationSource(configuracionCors(corsProperties)))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/autenticacion/**").permitAll()
                        .anyRequest().authenticated()
                )
                .sessionManagement(session -> session
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(filtroAutenticacionJwt, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public PasswordEncoder codificadorPassword() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationProvider proveedorAutenticacion(UserDetailsService userDetailsService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(codificadorPassword());
        return provider;
    }

    @Bean
    public CorsConfigurationSource configuracionCors(CorsProperties corsProperties) {
        CorsConfiguration configuration = new CorsConfiguration();

        corsProperties.getAllowedOrigins().forEach(configuration::addAllowedOriginPattern);
        corsProperties.getAllowedMethods().forEach(configuration::addAllowedMethod);
        corsProperties.getAllowedHeaders().forEach(configuration::addAllowedHeader);
        configuration.setAllowCredentials(corsProperties.getAllowCredentials());

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration(corsProperties.getPathPattern(), configuration);

        return source;
    }

    @Bean
    public AuthenticationManager gestorAutenticacion(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /* 
    @Bean
    public UserDetailsService servicioDetallesUsuario() {
        UserDetails usuario = User.builder()
                .username("usuario")
                .password(codificadorPassword().encode("password"))
                .roles("USUARIO")
                .build();
        
        UserDetails admin = User.builder()
                .username("admin")
                .password(codificadorPassword().encode("admin"))
                .roles("ADMIN")
                .build();
        
        return new InMemoryUserDetailsManager(usuario, admin);
    }
    */
}