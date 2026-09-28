package app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import app.mappings.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class AppService
{
    @Autowired
    private EntityManager em;

    public Producto productoObtener(int idProd)
    {
//      return em.find(Producto.class,idProd);
        String hql = "FROM Producto p WHERE p.idProducto =:id ";
        Query q = em.createQuery(hql);
        q.setParameter("id", idProd);
        return (Producto)q.getSingleResult();
    }

    public List<Producto> productosObtener()
    {
        String hql = "FROM Producto";
        Query q = em.createQuery(hql);
        return q.getResultList();
    }

    public List<Producto> productosObtener(int idCat)
    {
        String hql = "FROM Producto p WHERE p.categoria.idCategoria =:id ";
        Query q = em.createQuery(hql);
        q.setParameter("id", idCat);
        return q.getResultList();
    }
}
