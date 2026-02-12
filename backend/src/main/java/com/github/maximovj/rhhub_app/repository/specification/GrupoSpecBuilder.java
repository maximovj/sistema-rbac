package com.github.maximovj.rhhub_app.repository.specification;

import org.springframework.data.jpa.domain.Specification;

import com.github.maximovj.rhhub_app.entity.GrupoEntity;

public class GrupoSpecBuilder extends BaseSpecification<GrupoEntity> {

    public GrupoSpecBuilder grupoId(Long grupoId){
        if(grupoId != null) {
            super.spec = super.spec.and(equalsSpec("grupoId", grupoId));
        }
        return this;
    }

    public GrupoSpecBuilder nombre(String nombre){
        if(nombre != null && !nombre.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("nombre", nombre));
        }
        return this;
    }

    public GrupoSpecBuilder descripcion(String descripcion){
        if(descripcion != null && !descripcion.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("descripcion", descripcion));
        }
        return this;
    }

    public GrupoSpecBuilder esActivo(Boolean esActivo){
        if(esActivo != null) {
            super.spec = super.spec.and(equalsSpec("esActivo", esActivo));
        }
        return this;
    }
    
}
