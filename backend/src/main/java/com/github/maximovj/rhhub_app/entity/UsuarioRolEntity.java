package com.github.maximovj.rhhub_app.entity;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
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
@Table(name = "TBL_USUARIO_ROLES")
public class UsuarioRolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROL_ID", nullable = false, unique = true)
    @JsonProperty("rol_id")
    private Long rolId;

    @Column(name = "ROL_NOMBRE", nullable = false, unique = true)
    @JsonProperty("rol_nombre")
    private String rolNombre;

    @Column(name = "ROL_ES_ADMINISTRADOR", nullable = false, unique = false)
    @JsonProperty(value = "rol_es_administrador", defaultValue = "false")
    @Builder.Default
    private Boolean rolEsAdministrador = false;

    // !! RELACIONES

    // Un rol puede tener muchos grupos
    @OneToMany(mappedBy = "roles", cascade = CascadeType.ALL)
    @Column(name = "ROL_GRUPO_ID", nullable = false, unique = false)
    @JsonProperty("rol_grupo_id")
    private List<UsuarioGruposEntity> rolGrupos;

    // Un rol pertenece a un usuario
    @OneToOne(mappedBy = "rol", cascade = CascadeType.ALL)
    private UsuarioEntity usuarioRol;

}
