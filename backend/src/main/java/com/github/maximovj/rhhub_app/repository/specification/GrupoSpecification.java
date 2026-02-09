package com.github.maximovj.rhhub_app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;

public class GrupoSpecification extends BaseSpecification<GrupoEntity> {

    public Specification<GrupoEntity> filtro(GrupoEntity e) {
        return Specification
                .where(equalsSpec("grupoId", e.getGrupoId()))
                .and(likeIgnoreCase("nombre", e.getNombre()))
                .and(likeIgnoreCase("descripcion", e.getDescripcion()))
                .and(equalsSpec("esActivo", e.getEsActivo()));
    }

    public Specification<GrupoEntity> filtro(
            Long id,
            String nombre,
            String descripcion,
            Boolean activo
    ) {
        return Specification
                .where(equalsSpec("grupoId", id))
                .and(likeIgnoreCase("nombre", nombre))
                .and(likeIgnoreCase("descripcion", descripcion))
                .and(equalsSpec("esActivo", activo));
    }
    
}
