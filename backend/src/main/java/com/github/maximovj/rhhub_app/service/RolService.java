package com.github.maximovj.rhhub_app.service;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import com.github.maximovj.rhhub_app.entity.RolEntity;
import com.github.maximovj.rhhub_app.projection.RolProjection;
import com.github.maximovj.rhhub_app.repository.RolRepository;
import com.github.maximovj.rhhub_app.service.integration.BaseServiceImpl;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class RolService extends BaseServiceImpl<RolEntity, Long, RolRepository> {
    
    public RolService(RolRepository jpaBaseRepository) {
        super(jpaBaseRepository);
    }

    public boolean esoExiste(String nombre) {
        return this.jpaBaseRepository.existsByNombre(nombre);
    }

    public Optional<RolEntity> buscaPorNombre(String nombre) {
        return this.jpaBaseRepository.findByNombre(nombre);
    }

    public Optional<RolEntity> cargarTodoPorId(Long rolId) {
        return this.jpaBaseRepository.qBuscarPorIdCargaRelaciones(rolId);
    }

    public Page<RolProjection> buscar(Specification<RolEntity> spec, Pageable pageable) {
        return this.jpaBaseRepository.qBuscarRol(spec, pageable);
    } 

    public Page<RolProjection> mostrarTodos(Pageable pageable) {
        return this.jpaBaseRepository.qMostrarRoles(pageable);
    }

}
