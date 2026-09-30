package app.mappings;

import jakarta.persistence.*;

@Entity
@Table(name = "empleado")
public class Empleado
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_empleado")
    private Integer idEmpleado;

    private String nombre;

    @ManyToOne
    @JoinColumn(name = "id_jefe")
    private Empleado jefe;

    public String toJPQLConsoleString()
    {
        return nombre;
    }

    public Integer getIdEmpleado()
    {
        return idEmpleado;
    }

    public void setIdEmpleado(Integer v)
    {
        idEmpleado = v;
    }

    public String getNombre()
    {
        return nombre;
    }

    public void setNombre(String v)
    {
        nombre = v;
    }

    public Empleado getJefe()
    {
        return jefe;
    }

    public void setJefe(Empleado v)
    {
        jefe = v;
    }
}
