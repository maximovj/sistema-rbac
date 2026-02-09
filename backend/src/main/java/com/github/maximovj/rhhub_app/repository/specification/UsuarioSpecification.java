package com.github.maximovj.rhhub_app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

import jakarta.persistence.criteria.Join;

public class UsuarioSpecification extends BaseSpecification<UsuarioEntity> {

    public Specification<UsuarioEntity> filtro(UsuarioEntity e) {
        return (root, query, cb) -> {
            Specification<UsuarioEntity> spec = Specification.where(null);

            if (e.getUsuario() != null && !e.getUsuario().isBlank()) {
                spec = spec.and(likeIgnoreCase("nombre", e.getUsuario()));
            }

            if (e.getEsActivo() != null) {
                spec = spec.and(equalsSpec("activo", e.getEsActivo()));
            }

            if (e.getGrupo() != null) {
                spec = spec.and((r, q, c) -> {
                    Join<UsuarioEntity, GrupoEntity> grupo = r.join("grupo");
                    return c.equal(grupo.get("grupoId"), e.getGrupo().getGrupoId());
                });
            }

            return spec.toPredicate(root, query, cb);
        };
    }

    public Specification<UsuarioEntity> filtro(
            String nombre,
            Boolean activo,
            Long grupoId
    ) {
        return (root, query, cb) -> {
            Specification<UsuarioEntity> spec = Specification.where(null);

            if (nombre != null && !nombre.isBlank()) {
                spec = spec.and(likeIgnoreCase("nombre", nombre));
            }

            if (activo != null) {
                spec = spec.and(equalsSpec("activo", activo));
            }

            if (grupoId != null) {
                spec = spec.and((r, q, c) -> {
                    Join<UsuarioEntity, GrupoEntity> grupo = r.join("grupo");
                    return c.equal(grupo.get("grupoId"), grupoId);
                });
            }

            return spec.toPredicate(root, query, cb);
        };
    }
    
}
