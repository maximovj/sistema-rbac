package com.github.maximovj.rhhub_app.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import com.github.maximovj.rhhub_app.dao.UsuarioDao;
import com.github.maximovj.rhhub_app.dto.request.UsuarioRequest;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;
import com.github.maximovj.rhhub_app.exception.BusinessException;
import com.github.maximovj.rhhub_app.exception.ResourceNotFoundException;
import com.github.maximovj.rhhub_app.projection.UsuarioProjection;
import com.github.maximovj.rhhub_app.repository.UsuarioRepository;
import com.github.maximovj.rhhub_app.repository.specification.UsuarioSpecification;
import com.github.maximovj.rhhub_app.service.integration.BaseServiceImpl;
import com.github.maximovj.rhhub_app.service.integration.JpaBaseRepository;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class UsuarioService extends BaseServiceImpl<UsuarioEntity, Long, UsuarioRepository> {
    
    protected UsuarioService(UsuarioRepository jpaBaseRepository) {
        super(jpaBaseRepository);
    }

    @Override
    public void delete(Long id) {
        UsuarioEntity usuario = this.jpaBaseRepository
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
        return jpaBaseRepository
            .findByUsuario(usuario).orElse(null);
            //.orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));
    }

    public UsuarioEntity findByUsuarioOrCorreo(String usuario, String correo) {
        return jpaBaseRepository
            .findByUsuarioOrCorreo(usuario, correo).orElse(null);
            //.orElseThrow(() -> new ResourceNotFoundException("Entidad no encontrado"));
    }

    public boolean existsByUsuarioOrCorreo(String usuario, String correo) {
        return jpaBaseRepository.existsByUsuarioOrCorreo(usuario, correo);
    }

    public Page<UsuarioProjection> listarUsuarios(Pageable pageable) {
        return this.jpaBaseRepository.qMostrarUsuarios(pageable);
    }

    public Page<UsuarioProjection> buscarUsuarios(Specification<UsuarioEntity> spec, Pageable pageable) {
        return this.jpaBaseRepository.qBuscarUsuarios(spec, pageable);
    }

}
