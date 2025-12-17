package com.github.maximovj.rhhub_app.entity;

import org.hibernate.engine.profile.Fetch;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "TBL_USUARIO_PERMISOS")
public class UsuarioPermisosEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIO_PERMISOS_ID", nullable = false, unique = true)
    @JsonProperty("usuario_permisos_id")
    private Long usuarioPermisosId;

    @Column(name = "PERMISO_ACCION", nullable = false, unique = true)
    @JsonProperty("permiso_accion")
    private String permisoAccion;

    @Column(name = "ES_PERMITIDO", nullable = false, unique = false)
    @JsonProperty(value = "es_permitido", defaultValue = "false")
    @Builder.Default
    private Boolean esPermitido = false;

    // !! RELACIONES

    // Un permiso tiene un estado
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_permiso_estado_id")
    // --
    @JsonProperty("estado")
    private UsuarioPermisoEstadoEntity estado;

    // Muchos permisos pueden pertenecer a un grupo
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_grupo_id")
    // --
    @JsonProperty("usuario_grupos_id")   
    private UsuarioGruposEntity grupo;
}
