package com.github.maximovj.rhhub_app.entity;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@Table(name = "RENOVAR_TOKENS")
public class RenovarTokensEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "REVOCAR_TOKENS_ID")
    private Long renovarTokensId;

    @Column(name = "TOKEN", nullable = false, unique = true)
    private String token; // Opcional: puedes guardar hash en vez de texto plano

    @Column(name = "FECHA_DE_EXPIRACION", nullable = false)
    private Instant fechaDeExpiracion;

    @ManyToOne
    @JoinColumn(name = "USUARIO_ID", nullable = false)
    private UsuarioEntity usuario;

    @Column(name = "RECUERDAME", nullable = false)
    @Builder.Default
    private boolean recuerdame = false;

    public boolean esRecuerdame(){
        return this.suspendido;
    }
    
    public void recuerdame(boolean esSuspendido) {
        this.suspendido = esSuspendido;
    }

    @Column(name = "SUSPENDIDO", nullable = false)
    @Builder.Default
    private boolean suspendido = false;

    public boolean estaSuspendido(){
        return this.suspendido;
    }
    
    public void suspendido(boolean esSuspendido) {
        this.suspendido = esSuspendido;
    }

    public void suspender() {
        this.suspendido = true;
    }
    
}
