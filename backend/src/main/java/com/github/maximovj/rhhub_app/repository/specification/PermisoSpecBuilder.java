package com.github.maximovj.rhhub_app.repository.specification;

import java.time.LocalDateTime;

import org.springframework.data.jpa.domain.Specification;

import com.github.maximovj.rhhub_app.dto.request.FechaCreacionRequest;
import com.github.maximovj.rhhub_app.entity.PermisoEntity;

public class PermisoSpecBuilder extends BaseSpecification<PermisoEntity> {

    public PermisoSpecBuilder permisoId(Long permisoId) {
        if(permisoId != null) {
            super.spec = super.spec.and(equalsSpec("permisoId", permisoId));
        }
        return this;
    }

    public PermisoSpecBuilder modulo(String modulo) {
        if(modulo != null && !modulo.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("modulo", modulo.trim()));
        }
        return this;
    }

    public PermisoSpecBuilder accion(String accion) { 
        if(accion != null && !accion.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("accion", accion.trim()));
        }
        return this;
    }

    public PermisoSpecBuilder esActivo(Boolean esActivo) {
        if(esActivo != null) {
            super.spec = super.spec.and(equalsSpec("esActivo", esActivo));
        }
        return this;
    }

    public PermisoSpecBuilder fechaCreacion(FechaCreacionRequest fechaCreacion) {
          if (fechaCreacion != null) {

            LocalDateTime desde = fechaCreacion.getCreado_desde() != null
                    ? fechaCreacion.getCreado_desde().toLocalDateTime()
                    : null;

            LocalDateTime hasta = fechaCreacion.getCreado_hasta() != null
                    ? fechaCreacion.getCreado_hasta().toLocalDateTime()
                    : null;

            spec = spec.and(dateBetween("creadoEn", desde, hasta));
        }

        return this;
    }

    public Specification<PermisoEntity> build() {
        return super.spec;
    }
    
}
