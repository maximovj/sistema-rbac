package com.github.maximovj.rhhub_app.entity;

import java.util.List;

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
import jakarta.persistence.OneToMany;
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
@Table(name = "TBL_USUARIO_GRUPOS")
public class UsuarioGruposEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USUARIO_GRUPO_ID", nullable = false, unique = true)
    @JsonProperty("usuario_grupo_id")
    private Long usuarioGrupoId;

    @Column(name="NOMBRE", nullable = false, unique = true)
    @JsonProperty("nombre")
    private String nombre;

    @Column(name="DESCRIPCION", nullable = false, unique = true)
    @JsonProperty("descripcion")
    private String descripcion;

    @Column(name="ACTIVO", nullable = false, unique = true)
    @JsonProperty(value = "activo", defaultValue = "true")
    @Builder.Default
    private Boolean activo = false;

    // !! RELACIONES

    // Muchos grupos pertenecen a un rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "rol_id")
    // --
    @JsonProperty("rol_id")
    private UsuarioRolEntity roles;

    // Un grupo puede tener muchos permisos
    @OneToMany(mappedBy = "grupo", cascade = CascadeType.ALL)
    // --
    @JsonProperty("usuario_permisos_id")
    private List<UsuarioPermisosEntity> permisos;

}
