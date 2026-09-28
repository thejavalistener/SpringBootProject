package app.mappings;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "promocion_vigencia")
public class PromocionVigencia
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_promocion_vigencia")
    private Integer idPromocionVigencia;

    @ManyToOne
    @JoinColumn(name = "id_promocion")
    private Promocion promocion;

    @Column(name = "fecha_inicio")
    private LocalDate fechaInicio;

    @Column(name = "fecha_fin")
    private LocalDate fechaFin;

    public Integer getIdPromocionVigencia()
    {
        return idPromocionVigencia;
    }

    public void setIdPromocionVigencia(Integer v)
    {
        idPromocionVigencia = v;
    }

    public Promocion getPromocion()
    {
        return promocion;
    }

    public void setPromocion(Promocion v)
    {
        promocion = v;
    }

    public LocalDate getFechaInicio()
    {
        return fechaInicio;
    }

    public void setFechaInicio(LocalDate v)
    {
        fechaInicio = v;
    }

    public LocalDate getFechaFin()
    {
        return fechaFin;
    }

    public void setFechaFin(LocalDate v)
    {
        fechaFin = v;
    }
}
