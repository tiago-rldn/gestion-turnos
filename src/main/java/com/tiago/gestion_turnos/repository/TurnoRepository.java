package com.tiago.gestion_turnos.repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiago.gestion_turnos.model.EstadoTurno;
import com.tiago.gestion_turnos.model.Turno;

public interface TurnoRepository extends JpaRepository<Turno, UUID> {
    // Busca turnos existentes en un rango de tiempo
    List<Turno> findByFechaHoraInicioBetween(LocalDateTime inicio, LocalDateTime fin);
    // Busca turnos para un cliente específico, ordenados por fecha y hora de inicio en orden descendente
    List<Turno> findByClienteIdOrderByFechaHoraInicioDesc(UUID clienteId);
    // Verifica si existe un turno con la misma fecha y hora de inicio que no esté en estados excluyentes
    boolean existsByFechaHoraInicioAndEstadoIn(LocalDateTime inicio, List<EstadoTurno> estadosExcluyentes);
}
