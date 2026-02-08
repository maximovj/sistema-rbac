package com.github.maximovj.rhhub_app.config.seeder;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.config.properties.SeederProperties;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;
import com.github.maximovj.rhhub_app.entity.PermisosEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioGruposRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisosRepository;

import jakarta.transaction.Transactional;

@Profile("seeder")
@Component
@Order(4)
public class UsuarioGruposSeeder implements ApplicationRunner {

    @Autowired
    UsuarioGruposRepository gruposRepository;

    @Autowired
    UsuarioPermisosRepository permisosRepository;

    @Autowired
    SeederProperties seederProperties;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        
        if(this.seederProperties.isEnabled() == false) return;

        if (!gruposRepository.existsByNombre("SUPER_ADMINISTRADOR")) {
            crearGrupo(
                "SUPER_ADMINISTRADOR",
                "GRUPO CON TODOS LOS PERMISOS",
                new HashSet<>(permisosRepository.findAll())
            );
        }

        if (!gruposRepository.existsByNombre("MOD_USUARIOS")) {
            Set<PermisosEntity> permisos = new HashSet<>(
                permisosRepository.findByPermisoAccionIn(
                    List.of("VIEW_USUARIOS", "UPDATE_USUARIOS")
                )
            );
            crearGrupo(
                "MOD_USUARIOS",
                "GRUPO SOLO MODIFICA USUARIOS",
                permisos
            );
        }

        if (!gruposRepository.existsByNombre("INVITADO")) {
            crearGrupo(
                "INVITADO",
                "GRUPO SIN PERMISOS",
                Collections.emptySet()
            );
        }

    }

    @Transactional
    private void crearGrupo(
            String nombre,
            String descripcion,
            Set<PermisosEntity> permisos
    ) {
        UsuarioGruposEntity grupo = UsuarioGruposEntity.builder()
                .nombre(nombre)
                .descripcion(descripcion)
                .esActivo(true)
                .rol(null)
                .permisos(permisos)
                .build();

        gruposRepository.save(grupo);

        int numPermisos = permisos != null ? permisos.size() : 0;
        System.out.println("Grupo " + nombre + " creado con " + numPermisos + " permisos");
    }


}
