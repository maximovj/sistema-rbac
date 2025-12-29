package com.github.maximovj.rhhub_app.entity;

import java.util.HashSet;
import java.util.Set;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(
    name = "TBL_USUARIO_PERMISOS",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"PERMISO_ACCION", "USUARIO_GRUPO_ID"}
        )
    }
)
public class UsuarioPermisosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIO_PERMISOS_ID", nullable = false, unique = true)
    @JsonProperty("usuario_permisos_id")
    private Long usuarioPermisosId;

    @Column(name = "PERMISO_ACCION", nullable = false, unique = true)
    @JsonProperty("permiso_accion")
    private String permisoAccion;

    @Column(name = "PERMISO_MODULO", nullable = false, unique = false)
    @JsonProperty("permiso_modulo")
    private String permisoModulo;

    @Column(name = "ES_PERMITIDO", nullable = false, unique = false)
    @JsonProperty(value = "es_permitido", defaultValue = "false")
    @Builder.Default
    private Boolean esPermitido = false;

    // !! RELACIONES

    // Un permiso puede estar en muchos grupos
    @ManyToMany(mappedBy = "permisos")
    @Builder.Default
    private Set<UsuarioGruposEntity> grupos = new HashSet<>();
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "USUARIO_PERMISO_ESTADO_ID",
        referencedColumnName = "USUARIO_PERMISO_ESTADO_ID"
    )
    @JsonIgnore
    private UsuarioPermisoEstadoEntity estado;

    // Añade estos métodos para equals/hashCode seguros
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof UsuarioPermisosEntity)) return false;
        UsuarioPermisosEntity that = (UsuarioPermisosEntity) o;
        return usuarioPermisosId != null && 
               usuarioPermisosId.equals(that.usuarioPermisosId);
    }
    
    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
    
}
