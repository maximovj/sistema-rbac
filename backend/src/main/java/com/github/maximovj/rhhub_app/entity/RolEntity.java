package com.github.maximovj.rhhub_app.entity;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
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
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString(exclude = {"grupos"})
@EqualsAndHashCode(exclude = {"grupos"})
@Builder
@Entity
@Table(name = "TBL_ROLES")
public class RolEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ROL_ID", nullable = false, unique = true)
    @JsonProperty("rol_id")
    private Long rolId;

    @Column(name = "NOMBRE", nullable = false, unique = true)
    @JsonProperty("nombre")
    private String nombre;

    @Column(name = "DESCRIPCION", nullable = false, unique = true)
    @JsonProperty("descripcion")
    private String descripcion;

    @Column(name = "ES_ADMINISTRADOR", nullable = false, unique = false)
    @JsonProperty(value = "es_administrador", defaultValue = "false")
    @Builder.Default
    private Boolean esAdministrador = false;

    @Column(name = "ES_ACTIVO", nullable = false, unique = false)
    @JsonProperty(value = "es_activo", defaultValue = "false")
    @Builder.Default
    private Boolean esActivo = false;

    // !! RELACIONES

    // Un rol puede tener muchos grupos
    // Relación con grupos (OneToMany si un rol puede tener varios grupos)
    @OneToMany(mappedBy = "rol", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private List<GrupoEntity> grupos = new ArrayList<>();
    
    // Método helper para añadir grupo
    public void addGrupo(GrupoEntity grupo) {
        grupos.add(grupo);
        grupo.setRol(this);
    }

    public void removeGrupo(GrupoEntity grupo) {
        grupos.remove(grupo);
        grupo.setRol(null);  // Rompe la relación inversa
    }

    // Método para añadir múltiples grupos
    public void addAllGrupos(List<GrupoEntity> grupos) {
        for (GrupoEntity grupo : grupos) {
            addGrupo(grupo);
        }
    }

}
