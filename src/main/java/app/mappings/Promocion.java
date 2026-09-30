package app.mappings;

import jakarta.persistence.*;

@Entity
@Table(name = "promocion")
public class Promocion
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion")
    private Integer idPromocion;

    private String descripcion;

    public String toJPQLConsoleString()
    {
        return descripcion;
    }


    public Integer getIdPromocion()
    {
        return idPromocion;
    }

    public void setIdPromocion(Integer v)
    {
        idPromocion = v;
    }

    public String getDescripcion()
    {
        return descripcion;
    }

    public void setDescripcion(String v)
    {
        descripcion = v;
    }
}
