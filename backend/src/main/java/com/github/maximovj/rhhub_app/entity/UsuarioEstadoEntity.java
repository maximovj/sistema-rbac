package com.github.maximovj.rhhub_app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
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
@Table(name = "TBL_USUARIO_ESTADOS")
public class UsuarioEstadoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIO_ESTADO_ID", nullable = false, unique = true)
    @JsonProperty("usuario_estado_id")
    private Long usuarioEstadoId;

    @Column(name = "ESTADO", nullable = false, unique = true)
    @JsonProperty("estado")
    private String estado;

    @Column(name = "DESCRIPCION", nullable = false, unique = true)
    @JsonProperty("descripcion")
    private String descripcion;

    // !! RELACIONES
    
    // Un estado pertenece a un usuario
    @OneToOne(mappedBy = "estado")
    private UsuarioEntity usuarioEstado;
    
}
