package com.github.maximovj.rhhub_app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class UsuarioPermisoEstadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="USUARIO_PERMISO_ESTADO_ID", nullable = false, unique = true)
    @JsonProperty("usuario_permiso_estado_id")
    private Long usuarioPermisoEstadoId;

    @Column(name="ESTADO", nullable = false, unique = false)
    @JsonProperty("estado")
    private String estado;

    // !! RELACIONES

    // Un estado pertenece a un permiso
    @OneToOne(mappedBy = "estado")
    private UsuarioPermisosEntity permiso;
    
}
