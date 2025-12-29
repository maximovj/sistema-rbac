package com.github.maximovj.rhhub_app.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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

    @Column(name = "ROL_DESCRIPCION", nullable = false, unique = true)
    @JsonProperty("rol_descripcion")
    private String rolDescripcion;

    @Column(name = "ROL_ES_ADMINISTRADOR", nullable = false, unique = false)
    @JsonProperty(value = "rol_es_administrador", defaultValue = "false")
    @Builder.Default
    private Boolean rolEsAdministrador = false;

    @Column(name = "ES_ACTIVO", nullable = false, unique = false)
    @JsonProperty(value = "es_activo", defaultValue = "false")
    @Builder.Default
    private Boolean esActivo = false;

    // !! RELACIONES

    // Un rol puede tener muchos grupos
    // Relación con grupos (OneToMany si un rol puede tener varios grupos)
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<UsuarioGruposEntity> grupos = new ArrayList<>();
    
    // Método helper para añadir grupo
    public void addGrupo(UsuarioGruposEntity grupo) {
        grupos.add(grupo);
        grupo.setRol(this);
    }

    public void removeGrupo(UsuarioGruposEntity grupo) {
        grupos.remove(grupo);
        grupo.setRol(null);  // Rompe la relación inversa
    }

    // Método para añadir múltiples grupos
    public void addAllGrupos(List<UsuarioGruposEntity> grupos) {
        for (UsuarioGruposEntity grupo : grupos) {
            addGrupo(grupo);
        }
    }

}
