package com.github.maximovj.rhhub_app.config.seeder;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.config.properties.SeederProperties;
import com.github.maximovj.rhhub_app.entity.UsuarioPermisoEstadoEntity;
import com.github.maximovj.rhhub_app.entity.PermisoEntity;
import com.github.maximovj.rhhub_app.repository.UsuarioPermisoEstadoRepository;
import com.github.maximovj.rhhub_app.repository.PermisoRepository;

import jakarta.transaction.Transactional;

@Profile("seeder")
@Component
@Order(3)
public class PermisoSeeder implements ApplicationRunner {

    @Autowired
    PermisoRepository repository;

    @Autowired
    UsuarioPermisoEstadoRepository usuarioPermisoEstadoRepository;

    @Autowired
    SeederProperties seederProperties;

	@Override
    @Transactional
	public void run(ApplicationArguments args) throws Exception {
		
        if(this.seederProperties.isEnabled() == false) return;
        
        this.crearModuloConPermisosCRUD("USUARIOS");
        this.crearModuloConPermisosCRUD("ROLES");
        this.crearModuloConPermisosCRUD("GRUPOS");
        this.crearModuloConPermisosCRUD("PERMISOS");
	}

    @Transactional
    private void crearUnPermiso(String strPermiso, String strModulo) {
        try {
            if(!repository.existsByAccion(strPermiso)) {
                Optional<UsuarioPermisoEstadoEntity> estado = usuarioPermisoEstadoRepository.findByEstado("ACTIVO");
                if(estado.isPresent()) {
                    UsuarioPermisoEstadoEntity estado_activo = estado.get();

                    // Crear el objeto de manera más explícita
                    PermisoEntity permiso = PermisoEntity.builder()
                    .accion(strPermiso)
                    .modulo(strModulo)
                    .esActivo(true)
                    .build();

                    if(permiso != null) {
                        repository.save(permiso);
                        System.out.println("permiso "+strPermiso+" fue creado correctamente.");
                    }
                }
            }
        } catch (Exception e) {
            System.out.println("Hubo un error: " + e.getMessage());
        }
    }

    private void crearModuloConPermisosCRUD(String nombreModulo) {
        String modulo = "MODULO_" + nombreModulo;
        String[] permisosModuloPermisos = {
            "VIEW_" + nombreModulo, 
            "CREATE_" + nombreModulo, 
            "READ_" + nombreModulo, 
            "UPDATE_" + nombreModulo, 
            "DELETE_" + nombreModulo};

        for (String string : permisosModuloPermisos) {
            this.crearUnPermiso(string, modulo);
        }
    }

}
