package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.RolEntity;
import com.github.maximovj.rhhub_app.projection.RolProjection;
import com.github.maximovj.rhhub_app.service.integration.JpaBaseRepository;

public interface RolRepository extends JpaBaseRepository<RolEntity, Long> { 

    default Page<RolProjection> qBuscarRol(Specification<RolEntity> spec, Pageable pageable) {
        return this.findAll(spec, pageable).map(RolProjection::fromEntity);
    };

    @Query("SELECT r FROM RolEntity r ")
    Page<RolProjection> qMostrarRoles(Pageable pageable);

    boolean existsByNombre(String Nombre);

    Optional<RolEntity> findByNombre(String Nombre);

    @EntityGraph(attributePaths = {"grupos"})
    @Query("SELECT r FROM RolEntity r WHERE (:rolId IS NOT NULL AND r.rolId = :rolId)")
    Optional<RolEntity> qBuscarPorIdCargaRelaciones(
        @Param("rolId") Long rolId
    );

}
