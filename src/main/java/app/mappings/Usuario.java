package app.mappings;

import jakarta.persistence.*;

@Entity
@Table(name = "usuario")
public class Usuario
{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_usuario")
    private Integer idUsuario;
    private String username;
    private String password;

    public Integer getIdUsuario()
    {
        return idUsuario;
    }

    public void setIdUsuario(Integer v)
    {
        idUsuario = v;
    }

    public String getUsername()
    {
        return username;
    }

    public void setUsername(String v)
    {
        username = v;
    }

    public String getPassword()
    {
        return password;
    }

    public void setPassword(String v)
    {
        password = v;
    }
}
