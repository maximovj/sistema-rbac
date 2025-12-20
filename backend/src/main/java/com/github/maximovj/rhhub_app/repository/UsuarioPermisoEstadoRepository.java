package com.github.maximovj.rhhub_app.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.github.maximovj.rhhub_app.entity.UsuarioPermisoEstadoEntity;

public interface UsuarioPermisoEstadoRepository extends JpaRepository<UsuarioPermisoEstadoEntity, Long> {

    boolean existsByEstado(String estado);

    Optional<UsuarioPermisoEstadoEntity> findByEstado(String estado);
    
    //List<UsuarioPermisoEstadoEntity> findByEstado(String estado);
    
}
