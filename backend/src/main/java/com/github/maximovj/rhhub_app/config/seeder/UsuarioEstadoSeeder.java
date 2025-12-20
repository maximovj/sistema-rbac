package com.github.maximovj.rhhub_app.config.seeder;

import org.springframework.boot.ApplicationRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.entity.UsuarioEstadoEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioEstadoRepository;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@Component
@Order(1)
public class UsuarioEstadoSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(UsuarioEstadoSeeder.class);

    private final UsuarioEstadoRepository repository;

	@Override
	public void run(ApplicationArguments args) throws Exception {

        log.info("Ejecutando seeder de: UsuarioEstadoSeeder");

        if(this.repository.existsByEstado("CREADA") == false) {
            this.repository.save(UsuarioEstadoEntity
                .builder()
                .estado("CREADA")
                .descripcion("CUENTA CREADA")
                .build());
        }

        if(this.repository.existsByEstado("ELIMINADA") == false) {
            this.repository.save(UsuarioEstadoEntity
                .builder()
                .estado("ELIMINADA")
                .descripcion("CUENTA ELIMINADA")
                .build());
        }

        if(this.repository.existsByEstado("VERIFICADA") == false) {
            this.repository.save(UsuarioEstadoEntity
                .builder()
                .estado("VERIFICADA")
                .descripcion("CUENTA VERIFICADA")
                .build());
        }

	}
}
