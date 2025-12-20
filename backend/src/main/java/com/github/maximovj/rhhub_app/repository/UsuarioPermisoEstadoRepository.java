package com.github.maximovj.rhhub_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.UsuarioPermisoEstadoEntity;

public interface UsuarioPermisoEstadoRepository extends JpaRepository<UsuarioPermisoEstadoEntity, Long> {

    boolean existsByEstado(String estado);
    
}
