package com.github.maximovj.rhhub_app.dao;

import java.util.Objects;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.github.maximovj.rhhub_app.dto.request.UsuarioRequest;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;
import com.github.maximovj.rhhub_app.repository.UsuarioEstadoRepository;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@AllArgsConstructor
@Slf4j
public class UsuarioDao {
    
    private final PasswordEncoder passwordEncoder;

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

    @Transactional
    public UsuarioEntity deleteUsuarioPorUsuarioId(Long usuario_id) {
        Objects.requireNonNull(usuario_id);
        UsuarioEntity entidad = this.usuarioRepository.findById(usuario_id).orElse(null);
        if(entidad == null) {
            return null;
        }

        this.usuarioRepository.deleteById(usuario_id);
        return entidad;
    }

    @Transactional
    public UsuarioEntity putUsuarioPorUsuarioId(Long usuario_id, UsuarioRequest req) {
        Objects.requireNonNull(usuario_id);
        UsuarioEntity entidad = this.usuarioRepository.findById(usuario_id).orElse(null);
        if(entidad == null) {
            return null;
        }

        if(req.getUsuario() != null && !req.getUsuario().isEmpty()) { 
            entidad.setUsuario(req.getUsuario());
        }

        if(req.getCorreo() != null && !req.getCorreo().isEmpty()) { 
            entidad.setCorreo(req.getCorreo());
        }

        if(
            req.getConfirmar_contrasena() != null && 
            req.getContrasena() != null && 
            !req.getContrasena().isEmpty() &&
            !req.getConfirmar_contrasena().isEmpty() &&
            req.getContrasena().equals(req.getConfirmar_contrasena())) {
            entidad.setContrasena(passwordEncoder.encode(req.getContrasena()));
        }

        this.usuarioRepository.save(entidad);
        return entidad;
    }

}
