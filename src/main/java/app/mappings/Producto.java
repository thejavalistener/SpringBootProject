package app.mappings;

import java.math.BigDecimal;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "producto")
public class Producto
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_producto")
    private Integer idProducto;

    private String descripcion;

    @ManyToOne
    @JoinColumn(name = "id_proveedor")
    private Proveedor proveedor;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;

    @Column(name = "precio_unitario")
    private BigDecimal precioUnitario;

    @Column(name = "unidades_stock")
    private Integer unidadesStock;

    @Column(name = "unidades_reposicion")
    private Integer unidadesReposicion;

    @Column(name = "flg_discontinuo")
    private Integer flgDiscontinuo;

    public String toHqlConsoleString()
    {
        return descripcion;
    }

    public Integer getIdProducto()
    {
        return idProducto;
    }

    public void setIdProducto(Integer v)
    {
        idProducto = v;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String v)
    {
        descripcion = v;
    }

    public Proveedor getProveedor()
    {
        return proveedor;
    }

    public void setProveedor(Proveedor v)
    {
        proveedor = v;
    }

    public Categoria getCategoria()
    {
        return categoria;
    }

    public void setCategoria(Categoria v)
    {
        categoria = v;
    }

    public BigDecimal getPrecioUnitario()
    {
        return precioUnitario;
    }

    public void setPrecioUnitario(BigDecimal v)
    {
        precioUnitario = v;
    }

    public Integer getUnidadesStock()
    {
        return unidadesStock;
    }

    public void setUnidadesStock(Integer v)
    {
        unidadesStock = v;
    }

    public Integer getUnidadesReposicion()
    {
        return unidadesReposicion;
    }

    public void setUnidadesReposicion(Integer v)
    {
        unidadesReposicion = v;
    }

    public Integer getFlgDiscontinuo()
    {
        return flgDiscontinuo;
    }

    public void setFlgDiscontinuo(Integer v)
    {
        flgDiscontinuo = v;
    }
}
