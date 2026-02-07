package com.github.maximovj.rhhub_app.dao;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Component;

import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class UsuarioDao {
    
    private UsuarioRepository usuarioRepository;

    public Page<UsuarioProjection> buscar(Integer page, Integer size) {
        Pageable pageable = PageRequest.of(
            page, 
            size, 
            Sort.by("usuario").ascending());

        Page<UsuarioProjection> lstUsuarios = this.usuarioRepository.buscarUsuarios(null, null, pageable);
        return lstUsuarios;
    }

    public UsuarioEntity getUsuarioPorUsuarioId(Long usuario_id) {
        return this.usuarioRepository.findByUsuarioIdWithRelations(usuario_id).orElse(null);
    }

}
