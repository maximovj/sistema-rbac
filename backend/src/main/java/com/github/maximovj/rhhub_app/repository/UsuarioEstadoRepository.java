package com.github.maximovj.rhhub_app.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.github.maximovj.rhhub_app.entity.UsuarioEstadoEntity;

@Repository
public interface UsuarioEstadoRepository extends JpaRepository<UsuarioEstadoEntity, Long> {
    
    boolean existsByEstado(String estado);

}
