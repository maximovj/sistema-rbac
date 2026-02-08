package com.github.maximovj.rhhub_app.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.github.maximovj.rhhub_app.entity.embedded_id.UsuarioPermisoEmbeddedId;

import jakarta.persistence.Column;
import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {})
@EqualsAndHashCode(exclude = {})
@Builder
@Entity
@Table(
    name = "TBL_USUARIO_PERMISO",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NOMBRE", "DESCRIPCION"})
    }
)
public class UsuarioPermisoEntity {

    @EmbeddedId
    private UsuarioPermisoEmbeddedId id;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("usuarioId")
    @JoinColumn(name = "USUARIO_ID")
    @JsonIgnore
    private UsuarioEntity usuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("permisoId")
    @JoinColumn(name = "PERMISO_ID")
    @JsonIgnore
    private PermisosEntity permiso;

    @Column(name = "ESTADO")
    private Boolean estado;
    
}
