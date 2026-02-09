package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.RolEntity;

public interface RolRepository extends JpaRepository<RolEntity, Long> { 

    boolean existsByNombre(String Nombre);

    Optional<RolEntity> findByNombre(String Nombre);

    @EntityGraph(attributePaths = {"grupos"})
    @Query("SELECT r FROM RolEntity r WHERE (:rolId IS NOT NULL AND r.rolId = :rolId)")
    Optional<RolEntity> qBuscarPorIdCargaRelaciones(
        @Param("rolId") Long rolId
    );

}
