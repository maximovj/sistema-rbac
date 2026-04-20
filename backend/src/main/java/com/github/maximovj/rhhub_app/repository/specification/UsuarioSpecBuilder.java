package com.github.maximovj.rhhub_app.repository.specification;

import java.time.LocalDateTime;

import com.github.maximovj.rhhub_app.dto.request.FechaCreacionRequest;
import com.github.maximovj.rhhub_app.entity.UsuarioEntity;

public class UsuarioSpecBuilder extends BaseSpecification<UsuarioEntity> {

    public UsuarioSpecBuilder usuarioId(Long id) {
        if (id != null && id > 0) {
            super.spec = super.spec.and(equalsSpec("usuarioId", id));
        }
        return this;
    }

    public UsuarioSpecBuilder usuario(String usuario) {
        if (usuario != null && !usuario.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("usuario", usuario));
        }
        return this;
    }

    public UsuarioSpecBuilder correo(String correo) {
        if (correo != null && !correo.isBlank()) {
            super.spec = super.spec.and(likeIgnoreCase("correo", correo));
        }
        return this;
    }

    public UsuarioSpecBuilder esActivo(Boolean esActivo) {
        if(esActivo != null) {
            super.spec = super.spec.and(equalsSpec("esActivo", esActivo));
        }
        return this;
    }

    public UsuarioSpecBuilder fechaCreacion(FechaCreacionRequest fechaCreacion) {
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

}
