package com.github.maximovj.rhhub_app.config.seeder;

import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.ApplicationArguments;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.config.properties.SeederProperties;
import com.github.maximovj.rhhub_app.entity.UsuarioEstadoEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioEstadoRepository;

import lombok.AllArgsConstructor;

@Profile("seeder")
@AllArgsConstructor
@Component
@Order(1)
public class UsuarioEstadoSeeder implements ApplicationRunner {

    private static final Logger log = LoggerFactory.getLogger(UsuarioEstadoSeeder.class);

    private final UsuarioEstadoRepository repository;

    private final SeederProperties seederProperties;

	@Override
	public void run(ApplicationArguments args) throws Exception {

        if(seederProperties.isEnabled() == false) return;

        log.info("Ejecutando seeder de: UsuarioEstadoSeeder");

        if(this.repository.existsByEstado("CREADA") == false) {

            UsuarioEstadoEntity estado = UsuarioEstadoEntity
                .builder()
                .estado("CREADA")
                .descripcion("CUENTA CREADA")
                .build();

            if(estado != null) {
                this.repository.save(estado);
                System.out.println("estado de usuario CREADA fue creado correctamente.");
            }
        }

        if(this.repository.existsByEstado("ELIMINADA") == false) {
            UsuarioEstadoEntity estado = UsuarioEstadoEntity
                .builder()
                .estado("ELIMINADA")
                .descripcion("CUENTA ELIMINADA")
                .build();

            if(estado != null) {
                this.repository.save(estado);
                System.out.println("estado de usuario ELIMINADA fue creado correctamente.");
            }
        }

        if(this.repository.existsByEstado("VERIFICADA") == false) {
            UsuarioEstadoEntity estado = UsuarioEstadoEntity
                .builder()
                .estado("VERIFICADA")
                .descripcion("CUENTA VERIFICADA")
                .build();

            if(estado != null) {
                this.repository.save(estado);
                System.out.println("estado de usuario VERIFICADA fue creado correctamente.");
            }
        }

	}
}
