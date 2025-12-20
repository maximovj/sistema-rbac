package com.github.maximovj.rhhub_app.config.seeder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.entity.UsuarioPermisoEstadoEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisoEstadoRepository;

@Component
@Order(2)
public class UsuarioPermisoEstadoSeeder implements ApplicationRunner {

    @Autowired
    UsuarioPermisoEstadoRepository repository;

    @Override
    public void run(ApplicationArguments args) throws Exception {

        if(!repository.existsByEstado("ACTIVO")) {
            repository.save(
                UsuarioPermisoEstadoEntity
                .builder()
                .estado("ACTIVO")
                .build());
        }

        if(!repository.existsByEstado("DESACTIVADO")) {
            repository.save(
                UsuarioPermisoEstadoEntity
                .builder()
                .estado("DESACTIVADO")
                .build());
        }

    }
    
}
