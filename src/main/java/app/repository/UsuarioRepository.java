package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Usuario;

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
}

