package com.github.maximovj.rhhub_app.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dao.UsuarioDao;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.exception.BusinessException;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;
import com.github.maximovj.rhhub_app.service.integration.BaseServiceImpl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@AllArgsConstructor
@Slf4j
public class UsuarioService extends BaseServiceImpl<UsuarioEntity, Long> {

    private final UsuarioRepository usuarioRepository;

    @Override
    protected JpaRepository<UsuarioEntity, Long> getRepository() {
        return this.usuarioRepository;
    }
   
    @Override
    public void delete(Long id) {
        UsuarioEntity usuario = this.usuarioRepository
            .findByUsuarioIdWithRelations(id)
            .orElseThrow(()-> new ResourceNotFoundException("Usuario no encontrada"));

        if(usuario.getGrupo() != null && usuario.getGrupo().getRol() != null) {
            if (usuario.getGrupo().getRol().getEsAdministrador()) {
                throw new BusinessException("No se puede eliminar un SUPER_ADMINISTRADOR");
            }
        }

        super.delete(id);
    }

    public UsuarioEntity findByUsuario(String usuario) {
        return usuarioRepository
            .findByUsuario(usuario).orElse(null);
            //.orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));
    }

    public UsuarioEntity findByUsuarioOrCorreo(String usuario, String correo) {
        return usuarioRepository
            .findByUsuarioOrCorreo(usuario, correo).orElse(null);
            //.orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));
    }

    public boolean existsByUsuarioOrCorreo(String usuario, String correo) {
        return usuarioRepository.existsByUsuarioOrCorreo(usuario, correo);
    }

}
