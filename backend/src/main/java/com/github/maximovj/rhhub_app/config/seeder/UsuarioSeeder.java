package com.github.maximovj.rhhub_app.config.seeder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.config.properties.SeederProperties;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEstadoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioEstadoRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioGruposRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;

import jakarta.transaction.Transactional;

@Profile("seeder")
@Component
@Order(6)
public class UsuarioSeeder implements ApplicationRunner {

    @Autowired
    UsuarioRepository usuarioRepository;

    @Autowired
    UsuarioEstadoRepository usuarioEstadoRepository;

    @Autowired
    UsuarioGruposRepository gruposRepository;

    @Autowired
    SeederProperties seederProperties;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        
        if(this.seederProperties.isEnabled() == false) return;

        if(!usuarioRepository.existsByUsuario("admin")) {
            this.crearUsuario(
                "admin",
                "admin@admin.com",
                "admin@admin.com",
                "CREADA",
                "SUPER_ADMINISTRADOR"
            );
        }

        if(!usuarioRepository.existsByUsuario("usuarios")) {
            this.crearUsuario(
                "usuarios",
                "usuarios@demo.com",
                "1",
                "CREADA",
                "MOD_USUARIOS"
            );
        }

        if(!usuarioRepository.existsByUsuario("demo")) {
            this.crearUsuario(
                "demo",
                "demo@demo.com",
                "1",
                "CREADA",
                "INVITADO"
            );
        }

    }

    @Transactional
    private void crearUsuario(
            String username,
            String correo,
            String passwordPlano,
            String estadoNombre,
            String grupoNombre
    ) {
        if (usuarioRepository.existsByUsuario(username)) {
            return;
        }

        Optional<UsuarioEstadoEntity> estadoOpt =
                usuarioEstadoRepository.findByEstado(estadoNombre);

        Optional<UsuarioGruposEntity> grupoOpt =
                gruposRepository.findByNombre(grupoNombre);

        if (estadoOpt.isEmpty() || grupoOpt.isEmpty()) {
            System.out.println(
                "No se pudo crear usuario " + username +
                ". Estado o grupo no encontrado."
            );
            return;
        }

        UsuarioEntity usuario = UsuarioEntity.builder()
                .usuario(username)
                .correo(correo)
                .contrasena(passwordEncoder.encode(passwordPlano))
                .esActivo(true)
                .token(null)
                .estado(estadoOpt.get())
                .grupo(grupoOpt.get())
                .build();

        usuarioRepository.save(usuario);

        System.out.println("Usuario " + username + " creado correctamente.");
    }

}
