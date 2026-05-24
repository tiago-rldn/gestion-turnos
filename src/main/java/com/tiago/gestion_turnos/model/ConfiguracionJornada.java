package com.tiago.gestion_turnos.model;

import java.time.DayOfWeek;
import java.time.LocalTime;
import java.util.List;

import com.tiago.gestion_turnos.converter.DaysOfWeekListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "configuracion_jornada")
public class ConfiguracionJornada {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "hora_apertura", nullable = false)
    private LocalTime horaApertura;

    @Column(name = "hora_cierre", nullable = false)
    private LocalTime horaCierre;

    @Column(name = "duracion_turno_minutos", nullable = false)
    private Integer duracionTurnoMinutos;

    @Column(name = "dias_laborables", nullable = false)
    @Convert(converter = DaysOfWeekListConverter.class)
    private List<DayOfWeek> diasLaborales;

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public LocalTime getHoraApertura() {
        return horaApertura;
    }
    public void setHoraApertura(LocalTime horaApertura) {
        this.horaApertura = horaApertura;
    }
    public LocalTime getHoraCierre() {
        return horaCierre;
    }
    public void setHoraCierre(LocalTime horaCierre) {
        this.horaCierre = horaCierre;
    }
    public int getDuracionTurnoMinutos() {
        return duracionTurnoMinutos;
    }
    public void setDuracionTurnoMinutos(int duracionTurnoMinutos) {
        this.duracionTurnoMinutos = duracionTurnoMinutos;
    }
    public List<DayOfWeek> getDiasLaborales() {
        return diasLaborales;
    }
    public void setDiasLaborales(List<DayOfWeek> diasLaborales) {
        this.diasLaborales = diasLaborales;
    }
}
