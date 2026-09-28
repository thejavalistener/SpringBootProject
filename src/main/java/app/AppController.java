package app;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import app.mappings.Producto;

@RestController
public class AppController
{
    @Autowired
    private AppService appService;

    @GetMapping("/producto/{idProd}")
    public Producto productoObtener(@PathVariable int idProd)
    {
        return appService.productoObtener(idProd);
    }

    @GetMapping("/productos")
    public List<Producto> productosObtener()
    {
        return appService.productosObtener();
    }

    @GetMapping("/productos/categoria/{idCat}")
    public List<Producto> productosObtener(@PathVariable int idCat)
    {
        return appService.productosObtener(idCat);
    }
}
