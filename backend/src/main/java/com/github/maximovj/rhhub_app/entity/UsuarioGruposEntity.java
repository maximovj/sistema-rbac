package com.github.maximovj.rhhub_app.entity;

import java.util.HashSet;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
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
    name = "TBL_USUARIO_GRUPOS",
    uniqueConstraints = {
        @UniqueConstraint(columnNames = {"NOMBRE", "DESCRIPCION"})
    }
)
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

    @Column(name="ES_ACTIVO", nullable = false, unique = false)
    @JsonProperty(value = "es_activo", defaultValue = "true")
    @Builder.Default
    private Boolean esActivo = true;

    // !! RELACIONES

    // Muchos grupos pertenecen a un rol
    // Relación ManyToOne con Rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROL_ID", nullable = true)
    @JsonIgnoreProperties({"grupos", "hibernateLazyInitializer", "handler"}) // Evita recursión infinita en JSON
    private UsuarioRolEntity rol;
    
    // Un Grupo puede tener muchos Permisos
    // Relación ManyToMany con Permisos
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TBL_GRUPO_PERMISOS",
        joinColumns = @JoinColumn(name = "USUARIO_GRUPO_ID"),
        inverseJoinColumns = @JoinColumn(name = "USUARIO_PERMISOS_ID"),
        uniqueConstraints = @UniqueConstraint(
            columnNames = {"USUARIO_GRUPO_ID", "USUARIO_PERMISOS_ID"}
        )
    )
    @JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
    @Builder.Default
    private Set<UsuarioPermisosEntity> permisos = new HashSet<>();

    // Método helper para añadir permiso
    public void addPermiso(UsuarioPermisosEntity permiso) {
        permisos.add(permiso);
    }
    
    // Método helper para eliminar permiso
    public void removePermiso(UsuarioPermisosEntity permiso) {
        permisos.remove(permiso);
        // Si tienes relación inversa:
        // permiso.getGrupos().remove(this);
    }

    public void setRol(UsuarioRolEntity nuevoRol) {
        // Si ya tenía un rol, quitar este grupo de ese rol
        if (this.rol != null) {
            this.rol.getGrupos().remove(this);
        }
        
        // Asignar nuevo rol
        this.rol = nuevoRol;
        
        // Si el nuevo rol no es null, añadir este grupo al rol
        if (nuevoRol != null && !nuevoRol.getGrupos().contains(this)) {
            nuevoRol.getGrupos().add(this);
        }
    }
    
    // Método para verificar si tiene un permiso específico
    public boolean tienePermiso(String permisoAccion) {
        return permisos.stream()
            .anyMatch(permiso -> permiso.getPermisoAccion().equals(permisoAccion));
    }
    
    // Método estático para builder
    public static UsuarioGruposEntityBuilder builder() {
        return new UsuarioGruposEntityBuilder()
            .esActivo(true)
            .permisos(new HashSet<>());
    }

}
