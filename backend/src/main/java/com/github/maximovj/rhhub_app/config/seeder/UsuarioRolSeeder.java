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

        if (!rolRepository.existsByRolNombre("ROOT")) {
            crearRol(
                "ROOT",
                "Rol de administrador del sistema",
                true,
                "SUPER_ADMINISTRADOR"
            );
        }

        if (!rolRepository.existsByRolNombre("MOD_USUARIOS")) {
            crearRol(
                "MOD_USUARIOS",
                "Rol solo crear usuarios del sistema",
                false,
                "MOD_USUARIOS"
            );
        }

        if (!rolRepository.existsByRolNombre("GUEST")) {
            crearRol(
                "GUEST",
                "Rol de invitado del sistema",
                false,
                "INVITADO"
            );
        }

    }

    @Transactional
    private void crearRol(
            String nombreRol,
            String descripcion,
            boolean esAdministrador,
            String nombreGrupo
    ) {
        Optional<UsuarioGruposEntity> grupoOpt = gruposRepository.findByNombre(nombreGrupo);

        if (!grupoOpt.isPresent()) {
            System.out.println("Grupo " + nombreGrupo + " no encontrado. Rol " + nombreRol + " no creado.");
            return;
        }

        UsuarioGruposEntity grupo = grupoOpt.get();

        UsuarioRolEntity rol = UsuarioRolEntity.builder()
                .rolNombre(nombreRol)
                .rolDescripcion(descripcion)
                .rolEsAdministrador(esAdministrador)
                .esActivo(true)
                .build();

        rol.addGrupo(grupo);
        rolRepository.save(rol);

        int numPermisos = grupo.getPermisos().size();
        System.out.println("Rol " + nombreRol + " y grupo " + nombreGrupo +
                " creados con " + numPermisos + " permisos");
    }
    
}