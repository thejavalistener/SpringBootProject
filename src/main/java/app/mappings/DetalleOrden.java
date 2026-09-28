package app.mappings;

import jakarta.persistence.*;

@Entity
@Table(name = "detalle_orden")
public class DetalleOrden
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_detalle_orden")
    private Integer idDetalleOrden;

    @ManyToOne
    @JoinColumn(name = "id_orden")
    private Orden orden;

    @ManyToOne
    @JoinColumn(name = "id_producto")
    private Producto producto;

    private Integer cantidad;

    public Integer getIdDetalleOrden()
    {
        return idDetalleOrden;
    }

    public void setIdDetalleOrden(Integer v)
    {
        idDetalleOrden = v;
    }

    public Orden getOrden()
    {
        return orden;
    }

    public void setOrden(Orden v)
    {
        orden = v;
    }

    public Producto getProducto()
    {
        return producto;
    }

    public void setProducto(Producto v)
    {
        producto = v;
    }

    public Integer getCantidad()
    {
        return cantidad;
    }

    public void setCantidad(Integer v)
    {
        cantidad = v;
    }
}
