package com.github.maximovj.rhhub_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dao.UsuarioDao;
import com.github.maximovj.rhhub_app.dto.response.ApiResponse;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.mapper.UsuarioMapper;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;

@Service
public class UsuarioService {

    @Autowired
    UsuarioDao usuarioDao;

    public ResponseEntity<?> busqueda(Integer page, Integer size) {
        ApiResponse<UsuarioProjection> response = new ApiResponse<>();
        Page<UsuarioProjection> pageUsuario = usuarioDao.buscar(page, size);
        return response.okPage("Lista de usuarios", pageUsuario);
    }

    public ResponseEntity<?> cargarUsuarioEntity(Long usuario_id) {
        UsuarioEntity usuario = usuarioDao.getUsuarioPorUsuarioId(usuario_id);
        if(usuario == null) {
            String messageError = "Entidad no encontrada con usuario_id (%d)".formatted(usuario_id);
            return ApiResponse.notFound(messageError, null);
        }
        return ApiResponse.ok("La entidad fue localizada", UsuarioMapper.toDTO(usuario));
    }
    
}
