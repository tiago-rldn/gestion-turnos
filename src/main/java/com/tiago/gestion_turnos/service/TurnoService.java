package com.tiago.gestion_turnos.service;

import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tiago.gestion_turnos.model.ConfiguracionJornada;
import com.tiago.gestion_turnos.model.EstadoTurno;
import com.tiago.gestion_turnos.model.Turno;
import com.tiago.gestion_turnos.repository.TurnoRepository;

import jakarta.transaction.Transactional;

@Service
public class TurnoService {
    private final TurnoRepository turnoRepository;
    private final ConfiguracionJornadaService configuracionJornadaService;

    public TurnoService(TurnoRepository turnoRepository, ConfiguracionJornadaService configuracionJornadaService) {
        this.turnoRepository = turnoRepository;
        this.configuracionJornadaService = configuracionJornadaService;
    }

    public List<Turno> getListaTurnos() {
        return turnoRepository.findAll();
    }
    
    public Turno reservaTurno(Turno turno) throws Exception {
        ConfiguracionJornada jornada = configuracionJornadaService.obtenerConfiguracion();
        
        if (!jornada.getDiasLaborales().contains(turno.getFechaHoraInicio().getDayOfWeek())) {
            throw new IllegalArgumentException("El profesional no trabaja en el día seleccionado.");
        }

        if (turno.getFechaHoraInicio().toLocalTime().isBefore(jornada.getHoraApertura()) || 
            turno.getFechaHoraFin().toLocalTime().isAfter(jornada.getHoraCierre())) {
            throw new IllegalArgumentException("El horario seleccionado está fuera de la jornada laboral.");
        }

        List<EstadoTurno> estadosConflictivos = Arrays.asList(EstadoTurno.RESERVADO, EstadoTurno.BLOQUEADO);
        boolean horarioOcupado = turnoRepository.existsByFechaHoraInicioAndEstadoIn(
                turno.getFechaHoraInicio(), estadosConflictivos);
        
        if (horarioOcupado) {
            throw new IllegalStateException("El horario ya no se encuentra disponible.");
        }

        turno.setEstado(EstadoTurno.RESERVADO);
        return turnoRepository.save(turno);
    }

    public Turno findTurnoByIdYCliente(UUID userId, UUID turnoId) throws Exception {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new Exception("Turno no encontrado."));
        if (!turno.getCliente().getId().equals(userId)) {
            throw new Exception("El turno no pertenece a este usuario.");
        }
        return turno;
    }

    @Transactional
    public Turno cancelarTurnoPorCliente(UUID userId, UUID turnoId) throws Exception {
        Turno turno = findTurnoByIdYCliente(userId, turnoId);

        if (turno.getEstado() == EstadoTurno.CANCELADO_POR_CLIENTE || turno.getEstado() == EstadoTurno.CANCELADO_POR_ADMINISTRADOR) {
            throw new IllegalStateException("El turno ya se encuentra cancelado.");
        }
        if (turno.getEstado() == EstadoTurno.COMPLETADO) {
            throw new IllegalStateException("No se puede cancelar un turno completado.");
        }
        if (LocalDateTime.now().plusHours(24).isAfter(turno.getFechaHoraInicio())) {
            throw new IllegalStateException("Los turnos solo pueden cancelarse con un mínimo de 24 horas de anticipación.");
        }

        turno.setEstado(EstadoTurno.CANCELADO_POR_CLIENTE);
        return turnoRepository.save(turno);
    }

    @Transactional
    public Turno cancelarTurnoPorAdmin(UUID turnoId) throws Exception {
        Turno turno = turnoRepository.findById(turnoId)
                .orElseThrow(() -> new Exception("Turno no encontrado."));
                
        if (turno.getEstado() == EstadoTurno.CANCELADO_POR_CLIENTE || turno.getEstado() == EstadoTurno.CANCELADO_POR_ADMINISTRADOR) {
            throw new IllegalStateException("El turno ya se encuentra cancelado.");
        }
        
        turno.setEstado(EstadoTurno.CANCELADO_POR_ADMINISTRADOR);
        return turnoRepository.save(turno);
    }

    public List<Turno> obtenerHistorialCliente(UUID clienteId) {
        return turnoRepository.findByClienteIdOrderByFechaHoraInicioDesc(clienteId);
    }
}