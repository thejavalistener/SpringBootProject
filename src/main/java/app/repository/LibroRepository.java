package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Libro;

public interface LibroRepository extends JpaRepository<Libro, Long> {
}

