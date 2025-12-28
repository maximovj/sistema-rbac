package com.github.maximovj.rhhub_app.config.security;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;

import lombok.AllArgsConstructor;


@Service
@AllArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UsuarioRepository usuarioRepository;

    @Override
    public UserDetails loadUserByUsername(String nombreUsuario) {
        UsuarioEntity usuario = this.usuarioRepository.findByUsuario(nombreUsuario)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encotrado"));

        return User.builder()
            .username(usuario.getUsuario())
            .password(usuario.getContrasena())
            .roles(usuario.getGrupo().getNombre())
            .disabled(!usuario.getEsActivo())
            .build();
    }
    
}
