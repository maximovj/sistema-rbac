package com.github.maximovj.rhhub_app.entity;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.SQLRestriction;
import org.hibernate.annotations.DialectOverride.Where;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import lombok.AllArgsConstructor;
import lombok.Builder;
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
@EntityListeners(AuditingEntityListener.class)
@Table(
    name = "TBL_PERMISOS",
    uniqueConstraints = {
        @UniqueConstraint(
            columnNames = {"ACCION", "MODULO"}
        )
    }
)
@SQLDelete(sql = "UPDATE TBL_PERMISOS SET ELIMINADO_EN = NOW() WHERE PERMISO_ID = ?")
@SQLRestriction("ELIMINADO_EN IS NULL")
public class PermisoEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "PERMISO_ID", nullable = false, unique = true)
    @JsonProperty("permiso_id")
    private Long permisoId;

    @Column(name = "ACCION", nullable = false, unique = true)
    @JsonProperty("accion")
    private String accion;

    @Column(name = "MODULO", nullable = false, unique = false)
    @JsonProperty("modulo")
    private String modulo;

    @Column(name = "ES_ACTIVO", nullable = false)
    @JsonProperty("es_activo")
    @Builder.Default
    private Boolean esActivo = true;

    @CreatedDate
    @Column(name = "CREADO_EN", nullable = false)
    @JsonProperty("creado_en")
    private LocalDateTime creadoEn;
    
    @LastModifiedDate
    @Column(name = "ACTUALIZADO_EN", nullable = true)
    @JsonProperty("actualizado_en")
    private LocalDateTime actualizadoEn;

    @Column(name = "ELIMINADO_EN", nullable = true)
    @JsonProperty("eliminado_en")
    private LocalDateTime eliminadoEn;

    // !! RELACIONES CORREGIDAS

    // Un permiso puede estar en muchos grupos
    @ManyToMany(mappedBy = "permisos", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonIgnore
    private Set<GrupoEntity> grupos = new HashSet<>();
    
}