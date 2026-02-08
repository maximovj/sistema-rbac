package com.github.maximovj.rhhub_app.entity;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
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
@ToString(exclude = {"rol", "permisos"})
@EqualsAndHashCode(exclude = {"rol", "permisos"})
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

    // !! RELACIONES CORREGIDAS

    //@OneToMany(mappedBy = "grupo")
    //@JsonIgnore // Importante para evitar recursividad
    //private List<UsuarioEntity> usuarios;

    // Muchos grupos pertenecen a un rol
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ROL_ID", nullable = true)
    @JsonIgnore
    //@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"}) // <-- CORREGIDO: quitado "rol"
    private UsuarioRolEntity rol;
    
    // Un Grupo puede tener muchos Permisos
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "TBL_USUARIO_GRUPO_PERMISO",
        joinColumns = @JoinColumn(name = "USUARIO_GRUPO_ID"),
        inverseJoinColumns = @JoinColumn(name = "PERMISO_ID"),
        uniqueConstraints = @UniqueConstraint(
            columnNames = {"USUARIO_GRUPO_ID", "PERMISO_ID"}
        )
    )
    @Builder.Default
    @JsonIgnore
    //@JsonIgnoreProperties({"grupos", "hibernateLazyInitializer", "handler"}) // <-- CORREGIDO: "grupos" en lugar de "permisos"
    private Set<PermisosEntity> permisos = new HashSet<>();

    // Métodos helper corregidos
    public void addPermiso(PermisosEntity permiso) {
        this.permisos.add(permiso);
        permiso.getGrupos().add(this); // <-- IMPORTANTE: mantener consistencia bidireccional
    }
    
    public void removePermiso(PermisosEntity permiso) {
        this.permisos.remove(permiso);
        permiso.getGrupos().remove(this); // <-- IMPORTANTE: mantener consistencia bidireccional
    }

    // Método mejorado para setRol
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