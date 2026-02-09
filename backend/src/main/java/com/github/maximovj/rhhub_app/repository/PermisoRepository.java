package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.PermisoEntity;

import java.util.Collection;
import java.util.List;


public interface PermisoRepository extends JpaRepository<PermisoEntity, Long> {

    boolean existsByPermisoAccion(String permisoAccion);

    @EntityGraph(attributePaths = {"grupos"})
    @Query("SELECT p FROM PermisoEntity p WHERE (:permisoId IS NOT NULL AND p.permisoId = :permisoId)")
    Optional<PermisoEntity> qBuscaPorIdCargaRelaciones(
        @Param("permisoId") Long permisoId
    );

    Optional<PermisoEntity> findByPermisoAccion(String permisoAccion);

    List<PermisoEntity> findByPermisoModulo(String permisoModulo);

    List<PermisoEntity> findByPermisoAccionIn(Collection<String> permisoAccion);

    List<PermisoEntity> findByPermisoModuloIn(Collection<String> permisoModulo);

}
