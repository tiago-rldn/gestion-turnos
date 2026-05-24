package com.tiago.gestion_turnos.repository;

import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.tiago.gestion_turnos.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, UUID> {
    // Busca un usuario por su email
    Optional<Usuario> findByEmail(String email);
    // Verifica si un usuario con el email dado ya existe
    boolean existsByEmail(String email);
}
