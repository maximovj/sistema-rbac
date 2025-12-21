package com.github.maximovj.rhhub_app.config.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.entity.UsuarioPermisoEstadoEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisoEstadoRepository;

@Profile("seeder")
@Component
@Order(2)
public class UsuarioPermisoEstadoSeeder implements ApplicationRunner {

    @Autowired
    UsuarioPermisoEstadoRepository repository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(!repository.existsByEstado("ACTIVO")) {

            UsuarioPermisoEstadoEntity entidad = UsuarioPermisoEstadoEntity
                .builder()
                .estado("ACTIVO")
                .descripcion("PERMISO ACTIVO")
                .build();

            if(entidad != null) {
                repository.save(entidad);
                System.out.println("usuario permiso estado ACTIVO fue creado correctamente.");
            }
                
        }

        if(!repository.existsByEstado("DESACTIVADO")) {
            UsuarioPermisoEstadoEntity entidad = UsuarioPermisoEstadoEntity
                .builder()
                .estado("DESACTIVADO")
                .descripcion("PERMISO DESACTIVADO")
                .build();
            
            if(entidad != null) {
                repository.save(entidad);
                System.out.println("usuario permiso estado DESACTIVADO fue creado correctamente.");
            }
        }

    }
    
}
