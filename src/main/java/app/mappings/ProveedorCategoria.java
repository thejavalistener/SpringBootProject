package app.mappings;

import jakarta.persistence.*;

@Entity
@Table(name = "proveedor_categoria")
public class ProveedorCategoria
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_proveedor_categoria")
    private Integer idProveedorCategoria;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    public Integer getIdProveedorCategoria()
    {
        return idProveedorCategoria;
    }

    public void setIdProveedorCategoria(Integer idProveedorCategoria)
    {
        this.idProveedorCategoria = idProveedorCategoria;
    }

    public Proveedor getProveedor()
    {
        return proveedor;
    }

    public void setProveedor(Proveedor proveedor)
    {
        this.proveedor = proveedor;
    }

    public Categoria getCategoria()
    {
        return categoria;
    }

    public void setCategoria(Categoria categoria)
    {
        this.categoria = categoria;
    }
}
