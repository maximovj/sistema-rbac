package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.service.integration.JpaBaseRepository;

public interface GrupoRepository extends JpaBaseRepository<GrupoEntity, Long> {

    boolean existsByNombre(String nombre);

    Optional<GrupoEntity> findByNombre(String nombre);

    @EntityGraph(attributePaths = {"rol", "permisos"})
    @Query("SELECT g FROM GrupoEntity g WHERE (:grupoId IS NOT NULL AND g.grupoId = :grupoId)")
    Optional<GrupoEntity> qBuscarPorIdRelaciones(
        @Param("grupoId") Long grupoId
    );

    @Query("""
        SELECT g FROM GrupoEntity g WHERE
        (:grupoId IS NULL OR g.grupoId = :grupoId)
        AND (:nombre IS NULL OR LOWER(g.nombre) LIKE LOWER(CONCAT('%', :nombre, '%')))
        AND (:descripcion IS NULL OR LOWER(g.descripcion) LIKE LOWER(CONCAT('%', :descripcion, '%')))
        AND (:esActivo IS NULL OR g.esActivo = :esActivo)
    """)
    Page<GrupoEntity> filtrarGrupos(
        @Param("grupoId") Long grupoId,
        @Param("nombre") String nombre,
        @Param("descripcion") String descripcion,
        @Param("esActivo") Boolean esActivo,
        Pageable pageable
    );

}
