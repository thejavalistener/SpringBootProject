package app.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "libro")
public class Libro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titulo;
    private String autor;

    @Column(unique = true)
    private String isbn;

    private Integer copiasDisponibles;

    public Libro() {}

    public Libro(String titulo, String autor, String isbn, Integer copiasDisponibles) {
        this.titulo = titulo;
        this.autor = autor;
        this.isbn = isbn;
        this.copiasDisponibles = copiasDisponibles;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getAutor() { return autor; }
    public void setAutor(String autor) { this.autor = autor; }
    public String getIsbn() { return isbn; }
    public void setIsbn(String isbn) { this.isbn = isbn; }
    public Integer getCopiasDisponibles() { return copiasDisponibles; }
    public void setCopiasDisponibles(Integer copiasDisponibles) { this.copiasDisponibles = copiasDisponibles; }
}

