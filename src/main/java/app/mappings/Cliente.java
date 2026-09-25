package app.mappings;

import jakarta.persistence.*;

@Entity
@Table(name = "cliente")
public class Cliente {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY) @Column(name = "id_cliente") private Integer idCliente;
    @ManyToOne @JoinColumn(name = "id_usuario") private Usuario usuario;
    private String nombre;
    private String direccion;
    @ManyToOne @JoinColumn(name = "id_tipo_cliente") private TipoCliente tipoCliente;
    public Integer getIdCliente() { return idCliente; } public void setIdCliente(Integer v) { idCliente = v; }
    public Usuario getUsuario() { return usuario; } public void setUsuario(Usuario v) { usuario = v; }
    public String getNombre() { return nombre; } public void setNombre(String v) { nombre = v; }
    public String getDireccion() { return direccion; } public void setDireccion(String v) { direccion = v; }
    public TipoCliente getTipoCliente() { return tipoCliente; } public void setTipoCliente(TipoCliente v) { tipoCliente = v; }
}
