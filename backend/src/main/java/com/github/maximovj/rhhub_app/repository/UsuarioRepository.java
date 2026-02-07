package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;

public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long> {

    @Query("""
        SELECT u FROM UsuarioEntity u
        WHERE (:usuario IS NULL OR u.usuario LIKE %:usuario%)
        AND (:correo IS NULL OR u.correo LIKE %:correo%)   
    """)
    Page<UsuarioProjection> buscarUsuarios(
        @Param("usuario") String usuario,
        @Param("correo") String correo,
        Pageable pageable
    );

    boolean existsByUsuario(String usuario);

    Optional<UsuarioEntity> findByUsuario(String usuario);

    Optional<UsuarioEntity> findByCorreo(String correo);
    
    Optional<UsuarioEntity> findByToken(String token);

    @EntityGraph(attributePaths = {"grupo.rol", "grupo.permisos"})
    @Query("SELECT u FROM UsuarioEntity u WHERE u.usuario = :usuario")
    Optional<UsuarioEntity> findByUsuarioWithDetails(@Param("usuario") String usuario);

    @EntityGraph(attributePaths = {"estado", "grupo", "grupo.rol", "grupo.permisos"})
    @Query("""
        SELECT u FROM UsuarioEntity u
        WHERE (:usuario_id IS NOT NULL AND u.usuarioId = :usuario_id)
    """)
    Optional<UsuarioEntity> findByUsuarioIdWithRelations(
        @Param("usuario_id") Long usuario_id
    );
    
}