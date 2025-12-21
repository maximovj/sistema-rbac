package com.github.maximovj.rhhub_app.config.seeder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEstadoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioEstadoRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioGruposRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;

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

    @Override
    public void run(ApplicationArguments args) throws Exception {
        UsuarioEntity entidad = null;

        if(!usuarioRepository.existsByUsuario("ADMIN")) {

            Optional<UsuarioEstadoEntity> estado = usuarioEstadoRepository.findByEstado("CREADA");
            Optional<UsuarioGruposEntity> grupo =  gruposRepository.findByNombre("ADMINISTRADOR");

            if(estado.isEmpty() || grupo.isEmpty()) {
                return;
            }

            entidad = UsuarioEntity.builder()
            .usuario("admin")
            .correo("admin@admin.com")
            .contrasena("admin@admin.com")
            .esActivo(true)
            .token(null)
            .estado(estado.get())
            .grupo(grupo.get())
            .build();

            if(entidad != null) {
                usuarioRepository.save(entidad);
                System.out.println("usuario ADMIN fue creado correctamente.");
            }
        }
    }

}
