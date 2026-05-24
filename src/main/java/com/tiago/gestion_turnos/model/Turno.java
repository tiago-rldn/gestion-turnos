package com.tiago.gestion_turnos.model;

import java.time.LocalDateTime;
import java.util.UUID;

import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "turnos")
public class Turno {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private UUID id;

    @Column(name = "fecha_hora_inicio", nullable = false)
    private LocalDateTime fechaHoraInicio;
    
    @Column(name = "fecha_hora_fin", nullable = false)
    private LocalDateTime fechaHoraFin;

    @ManyToOne
    @JoinColumn(name = "cliente_id", referencedColumnName = "id")
    @JdbcTypeCode(SqlTypes.VARCHAR)
    private Usuario cliente;

    @Enumerated(EnumType.STRING)
    private EstadoTurno estado;


    public UUID getId() {
        return id;
    }
    public void setId(UUID id) {
        this.id = id;
    }
    public LocalDateTime getFechaHoraInicio() {
        return fechaHoraInicio;
    }
    public void setFechaHoraInicio(LocalDateTime fechaHoraInicio) {
        this.fechaHoraInicio = fechaHoraInicio;
    }
    public LocalDateTime getFechaHoraFin() {
        return fechaHoraFin;
    }
    public void setFechaHoraFin(LocalDateTime fechaHoraFin) {
        this.fechaHoraFin = fechaHoraFin;
    }
    public Usuario getCliente() {
        return cliente;
    }
    public void setCliente(Usuario cliente) {
        this.cliente = cliente;
    }
    public EstadoTurno getEstado() {
        return estado;
    }
    public void setEstado(EstadoTurno estado) {
        this.estado = estado;
    }

    public void cancelarPorCliente() {
        if (this.getEstado() == EstadoTurno.RESERVADO) {
            this.setEstado(EstadoTurno.CANCELADO_POR_CLIENTE);
        }
    }

    public void cancelarPorAdmin() {
        this.setEstado(EstadoTurno.CANCELADO_POR_ADMINISTRADOR);
    }

    public void completar() {
        if (this.getEstado() == EstadoTurno.RESERVADO) {
            this.setEstado(EstadoTurno.COMPLETADO);
        }
    }
}
