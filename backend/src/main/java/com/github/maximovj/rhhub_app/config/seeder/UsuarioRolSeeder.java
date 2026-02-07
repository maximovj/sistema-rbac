package com.github.maximovj.rhhub_app.config.seeder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.config.properties.SeederProperties;
import com.github.maximovj.rhhub_app.entity.UsuarioGruposEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioRolEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioGruposRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisosRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioRolRepository;

import jakarta.transaction.Transactional;

@Profile("seeder")
@Component
@Order(5)
public class UsuarioRolSeeder implements ApplicationRunner {

    @Autowired
    UsuarioRolRepository rolRepository;

    @Autowired
    UsuarioPermisosRepository permisosRepository;

    @Autowired
    UsuarioGruposRepository gruposRepository;

    @Autowired
    SeederProperties seederProperties;

    @Override
    @Transactional
    public void run(ApplicationArguments args) throws Exception {
        
        if(this.seederProperties.isEnabled() == false) return;

        if(!rolRepository.existsByRolNombre("ROOT")) {
            crearRolUsuarioAdministrador();
        }

    }

    private void crearRolUsuarioAdministrador() {
        Optional<UsuarioGruposEntity> grupo = gruposRepository.findByNombre("SUPER_ADMINISTRADOR");

        if(!grupo.isPresent())  return;
        UsuarioGruposEntity grupoAdmin = grupo.get();
        
        // Crear el rol ADMIN
        UsuarioRolEntity rolAdmin = UsuarioRolEntity.builder()
            .rolNombre("ROOT")
            .rolDescripcion("Rol de administrador del sistema")
            .rolEsAdministrador(true)
            .esActivo(true)
            .build();

        if(rolAdmin != null) {

            // Asociar grupo al rol (depende de tu modelo de datos)
            // Si Rol tiene relación con Grupos:
            rolAdmin.addGrupo(grupoAdmin);

            // O si Grupo tiene relación con Rol:
            //grupoAdmin.setRol(rolAdmin);
            
            // Guardar (el orden depende de tus cascades)
            //gruposRepository.save(grupoAdmin);
            rolRepository.save(rolAdmin);

            int numPermisos = grupoAdmin.getPermisos().size();
            System.out.println("Rol ADMIN y grupo ADMINISTRADORES creados con " + numPermisos + " permisos");
        }
    }
    
}