package app.mappings;

import java.time.LocalDate;
import jakarta.persistence.*;

@Entity
@Table(name = "orden")
public class Orden
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_orden")
    private Integer idOrden;
    @ManyToOne
    @JoinColumn(name = "id_cliente")
    private Cliente cliente;
    @ManyToOne
    @JoinColumn(name = "id_empleado")
    private Empleado empleado;
    @Column(name = "fecha_generada")
    private LocalDate fechaGenerada;
    @Column(name = "fecha_entregada")
    private LocalDate fechaEntregada;

    public Integer getIdOrden()
    {
        return idOrden;
    }

    public void setIdOrden(Integer v)
    {
        idOrden = v;
    }

    public Cliente getCliente()
    {
        return cliente;
    }

    public void setCliente(Cliente v)
    {
        cliente = v;
    }

    public Empleado getEmpleado()
    {
        return empleado;
    }

    public void setEmpleado(Empleado v)
    {
        empleado = v;
    }

    public LocalDate getFechaGenerada()
    {
        return fechaGenerada;
    }

    public void setFechaGenerada(LocalDate v)
    {
        fechaGenerada = v;
    }

    public LocalDate getFechaEntregada()
    {
        return fechaEntregada;
    }

    public void setFechaEntregada(LocalDate v)
    {
        fechaEntregada = v;
    }
}
