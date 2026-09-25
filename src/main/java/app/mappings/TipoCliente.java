package app.mappings;

import jakarta.persistence.*;

@Entity
@Table(name = "tipo_cliente")
public class TipoCliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_tipo_cliente") private Integer idTipoCliente;
    private String descripcion;
    public Integer getIdTipoCliente() { return idTipoCliente; } public void setIdTipoCliente(Integer v) { idTipoCliente = v; }
    public String getDescripcion() { return descripcion; } public void setDescripcion(String v) { descripcion = v; }
}
