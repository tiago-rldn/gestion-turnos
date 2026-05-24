package com.tiago.gestion_turnos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiago.gestion_turnos.model.ConfiguracionJornada;

public interface ConfiguracionJornadaRepository extends JpaRepository<ConfiguracionJornada, Long> {
    
}
