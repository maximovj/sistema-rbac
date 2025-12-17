package com.github.maximovj.rhhub_app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "TBL_USUARIOS")
public class UsuarioEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "USUARIO_ID", nullable = false, unique = true)
    @JsonProperty("usuario_id")
    private Long usuarioId;

    @Column(name = "USUARIO", nullable = false, unique = true)
    @JsonProperty("usuario")
    private String usuario;

    @Column(name = "EMAIL", nullable = false, unique = true)
    @JsonProperty("email")
    private String email;

    @Column(name = "PASSWORD", nullable = false, unique = false)
    @JsonProperty("password")
    private String password;

    @Column(name = "NOMBRE", nullable = false, unique = false)
    @JsonProperty("nombre")
    private String nombre;

    @Column(name = "APELLIDO", nullable = false, unique = false)
    @JsonProperty("apellido")
    private String apellido;

    @Column(name = "ES_ACTIVO", nullable = false, unique = false)
    @JsonProperty("es_activo")
    private Boolean esActivo;

    @Column(name = "TOKEN", nullable = false, unique = false)
    @JsonProperty("token")
    private String token;

    // !! RELACIONES

    // Un usuario tiene un rol
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "ROL_ID", referencedColumnName = "ROL_ID")
    // --
    @JsonProperty("rol")
    private UsuarioRolEntity rol;

    // Un usuario tiene un estado
    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "USUARIO_ESTADO_ID", referencedColumnName = "USUARIO_ESTADO_ID")
    // --
    @JsonProperty("estado")
    private UsuarioEstadoEntity estado;

}
