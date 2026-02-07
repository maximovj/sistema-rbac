package com.github.maximovj.rhhub_app.config.seeder;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.config.properties.SeederProperties;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioPermisosEntity;
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

        if(!gruposRepository.existsByNombre("SUPER_ADMINISTRADOR")) {

            // Obtener todos los permisos existentes para el ADMIN
            Set<UsuarioPermisosEntity> todosPermisos = new HashSet<>(
                permisosRepository.findAll()
            );

            // Crear grupo para administradores
            UsuarioGruposEntity grupo = UsuarioGruposEntity.builder()
            .nombre("SUPER_ADMINISTRADOR")
            .descripcion("GRUPO CON TODOS LOS PERMISOS")
            .esActivo(true)
            .rol(null)
            .permisos(todosPermisos)
            .build();

            if(grupo != null){
                gruposRepository.save(grupo); 
                System.out.println("grupo SUPER_ADMINISTRADOR fue creado correctamente.");
            }
        }
    }

}
