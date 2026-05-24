package com.tiago.gestion_turnos.service;

import org.springframework.stereotype.Service;

import com.tiago.gestion_turnos.model.ConfiguracionJornada;
import com.tiago.gestion_turnos.repository.ConfiguracionJornadaRepository;

@Service
public class ConfiguracionJornadaService {
    private final ConfiguracionJornadaRepository configuracionRepository;

    public ConfiguracionJornadaService(ConfiguracionJornadaRepository configuracionRepository) {
        this.configuracionRepository = configuracionRepository;
    }

    // Guarda o actualiza la jornada global. Al usar validate, asumimos que operamos sobre el ID 1
    public ConfiguracionJornada guardarConfiguracion(ConfiguracionJornada configuracion) {
        return configuracionRepository.save(configuracion);
    }

    public ConfiguracionJornada obtenerConfiguracion() {
        return configuracionRepository.findById(1L)
                .orElseThrow(() -> new IllegalStateException("La jornada laboral aún no ha sido configurada por el administrador."));
    }
}
