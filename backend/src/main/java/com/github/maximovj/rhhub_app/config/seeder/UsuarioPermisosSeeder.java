package com.github.maximovj.rhhub_app.config.seeder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.entity.UsuarioPermisoEstadoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioPermisosEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisoEstadoRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisosRepository;

@Profile("seeder")
@Component
@Order(3)
public class UsuarioPermisosSeeder implements ApplicationRunner {

    @Autowired
    UsuarioPermisosRepository repository;

    @Autowired
    UsuarioPermisoEstadoRepository usuarioPermisoEstadoRepository;

	@Override
	public void run(ApplicationArguments args) throws Exception {
		
        if(!repository.existsByPermisoAccion("CRUD_USUARIOS")) {

            Optional<UsuarioPermisoEstadoEntity> estado = usuarioPermisoEstadoRepository.findByEstado("ACTIVO");
            if(estado.isPresent()) {
                UsuarioPermisoEstadoEntity estado_activo = estado.get();
                
                // Crear el objeto de manera más explícita
                UsuarioPermisosEntity permiso = UsuarioPermisosEntity.builder()
                .permisoAccion("CRUD_USUARIOS")  // Esto es un literal, no es null
                .permisoModulo("MODULO_USUARIOS")
                .esPermitido(true)
                .estado(estado_activo)
                .build();

                if(permiso != null) {
                    repository.save(permiso);
                    System.out.println("permiso CRUD_USUARIOS fue creado correctamente.");
                }
            }
        }

	}

}
