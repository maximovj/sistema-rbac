package com.github.maximovj.rhhub_app.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.PermisosEntity;

import java.util.Collection;
import java.util.List;


public interface UsuarioPermisosRepository extends JpaRepository<PermisosEntity, Long> {

    boolean existsByPermisoAccion(String permisoAccion);

    Optional<PermisosEntity> findByPermisoAccion(String permisoAccion);

    List<PermisosEntity> findByPermisoModulo(String permisoModulo);

    List<PermisosEntity> findByPermisoAccionIn(Collection<String> permisoAccion);

    List<PermisosEntity> findByPermisoModuloIn(Collection<String> permisoModulo);

}
