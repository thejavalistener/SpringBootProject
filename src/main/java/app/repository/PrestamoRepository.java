package app.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import app.model.Prestamo;

public interface PrestamoRepository extends JpaRepository<Prestamo, Long> {
}

