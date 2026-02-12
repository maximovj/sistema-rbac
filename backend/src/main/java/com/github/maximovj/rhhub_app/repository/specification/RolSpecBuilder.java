package com.github.maximovj.rhhub_app.repository.specification;

import com.github.maximovj.rhhub_app.entity.RolEntity;

public class RolSpecBuilder  extends BaseSpecification<RolEntity> {

    public RolSpecBuilder nombre(String nombre) {
        if(nombre != null && !nombre.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("nombre", nombre));
        }
        return this;
    }

    public RolSpecBuilder descripcion(String descripcion) {
        if(descripcion != null && !descripcion.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("descripcion", descripcion));
        }
        return this;
    }
    
}
