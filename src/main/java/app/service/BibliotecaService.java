package app.service;

import java.time.LocalDate;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import app.model.Libro;
import app.model.Prestamo;
import app.model.Usuario;
import app.repository.LibroRepository;
import app.repository.PrestamoRepository;
import app.repository.UsuarioRepository;

@Service
public class BibliotecaService {

    private final LibroRepository libroRepository;
    private final UsuarioRepository usuarioRepository;
    private final PrestamoRepository prestamoRepository;

    public BibliotecaService(LibroRepository libroRepository, UsuarioRepository usuarioRepository,
            PrestamoRepository prestamoRepository) {
        this.libroRepository = libroRepository;
        this.usuarioRepository = usuarioRepository;
        this.prestamoRepository = prestamoRepository;
    }

    public Libro registrarLibro(Libro libro) {
        return libroRepository.save(libro);
    }

    public Usuario registrarUsuario(Usuario usuario) {
        return usuarioRepository.save(usuario);
    }

    @Transactional
    public Prestamo prestarLibro(Long libroId, Long usuarioId) {
        Libro libro = libroRepository.findById(libroId)
                .orElseThrow(() -> new RuntimeException("Libro no encontrado"));
        Usuario usuario = usuarioRepository.findById(usuarioId)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        if (libro.getCopiasDisponibles() <= 0) {
            throw new RuntimeException("No hay copias disponibles de este libro");
        }

        libro.setCopiasDisponibles(libro.getCopiasDisponibles() - 1);
        libroRepository.save(libro);

        return prestamoRepository.save(new Prestamo(libro, usuario, LocalDate.now()));
    }

    @Transactional
    public Prestamo devolverLibro(Long prestamoId) {
        Prestamo prestamo = prestamoRepository.findById(prestamoId)
                .orElseThrow(() -> new RuntimeException("Préstamo no encontrado"));

        if (Boolean.TRUE.equals(prestamo.getDevuelto())) {
            throw new RuntimeException("Este préstamo ya fue devuelto");
        }

        prestamo.setDevuelto(true);
        prestamo.setFechaDevolucion(LocalDate.now());

        Libro libro = prestamo.getLibro();
        libro.setCopiasDisponibles(libro.getCopiasDisponibles() + 1);
        libroRepository.save(libro);

        return prestamoRepository.save(prestamo);
    }

    public List<Libro> obtenerLibros() {
        return libroRepository.findAll();
    }
}

