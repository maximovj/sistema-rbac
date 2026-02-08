package com.github.maximovj.rhhub_app.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dao.UsuarioDao;
import com.github.maximovj.rhhub_app.dto.request.UsuarioRequest;
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

    public ResponseEntity<?> eliminarUsuarioEntity(Long usuario_id) {
        UsuarioEntity usuario = usuarioDao.getUsuarioPorUsuarioId(usuario_id);

        if(usuario == null) {
            String messageError = "Entidad no encontrada con usuario_id (%d)".formatted(usuario_id);
            return ApiResponse.notFound(messageError, null);
        }

        if(usuario.getGrupo().getUsuarioGrupoId() == 1L || usuario.getGrupo().getNombre() == "SUPER_ADMINISTRADOR" ) {
            String messageError = "Entidad no se puede eliminar con usuario_id (%d)".formatted(usuario_id);
            return ApiResponse.notFound(messageError, null);
        }
        
        this.usuarioDao.deleteUsuarioPorUsuarioId(usuario_id);
        return ApiResponse.ok("Entidad eliminada correctamente", UsuarioMapper.toDTO(usuario));
    }

    public ResponseEntity<?> actualizarUsuarioEntity(Long usuario_id, UsuarioRequest req) {
        UsuarioEntity usuario = usuarioDao.getUsuarioPorUsuarioId(usuario_id);

        if(usuario == null) {
            String messageError = "Entidad no encontrada con usuario_id (%d)".formatted(usuario_id);
            return ApiResponse.notFound(messageError, null);
        }

        UsuarioEntity entidadActualizado = this.usuarioDao.putUsuarioPorUsuarioId(usuario_id, req);
        return ApiResponse.ok("Entidad actualizada correctamente", UsuarioMapper.toDTOBasic(entidadActualizado));
    }
    
}
