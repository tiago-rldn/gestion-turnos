package com.tiago.gestion_turnos.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import com.tiago.gestion_turnos.model.Usuario;
import com.tiago.gestion_turnos.repository.UsuarioRepository;
import com.tiago.gestion_turnos.util.JwtTokenUtil;
import com.tiago.gestion_turnos.util.PasswordEncoder;

@Service
public class UsuarioService {
    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenUtil jwtTokenUtil;

    public UsuarioService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtTokenUtil jwtTokenUtil) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenUtil = jwtTokenUtil;
    }

    public Usuario registrarUsuario(Usuario usuario) throws Exception {
        // validar email único
        if (usuarioRepository.findByEmail(usuario.getEmail()).isPresent()) {
            throw new Exception("El email ya está en uso");
        }

        // encriptar password
        usuario.setPassword(passwordEncoder.encode(usuario.getPassword()));

        return usuarioRepository.save(usuario);
    }

    public String autenticarUsuario(Usuario usuario) throws Exception {

        Usuario db = usuarioRepository.findByEmail(usuario.getEmail())
                .orElseThrow(() -> new Exception("Usuario no encontrado"));

        if (!passwordEncoder.verify(usuario.getPassword(), db.getPassword())) {
            throw new Exception("Password incorrecto");
        }

        return jwtTokenUtil.generateToken(db.getEmail());
    }

    public Usuario autorizarUsuario(String token) throws Exception {

        if (!jwtTokenUtil.verify(token)) {
            throw new Exception("Token inválido");
        }

        String email = jwtTokenUtil.getSubject(token);

        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new Exception("Usuario no encontrado"));
    }

    public List<Usuario> listaUsuarios() {
        return usuarioRepository.findAll();
    }

    public void eliminarUsuario(UUID id) throws Exception {
        if (!usuarioRepository.existsById(id)) {
            throw new Exception("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }

    public Usuario findByEmail(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
    }
}