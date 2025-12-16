package com.github.maximovj.rhhub_app.entity;

import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "TBL_OFICINA")
public class OficinaEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "OFICINA_ID", nullable = false, unique = true)
    @JsonProperty("oficina_id")
    private Long oficinaId;

    @Column(name = "NOMBRE", nullable = false, unique = true)
    @JsonProperty("nombre")
    private String nombre;
    
    @Column(name = "DIRECCION", nullable = false, unique = false)
    @JsonProperty("direccion")
    private String direccion;

    @Column(name = "LOGO", nullable = false, unique = false)
    @JsonProperty("logo")
    private String logo;

    @Column(name = "ES_ACTIVO", nullable = false, unique = false)
    @JsonProperty("es_activo")
    private Boolean esActivo;

}
