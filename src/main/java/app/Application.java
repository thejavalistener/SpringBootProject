package app;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import app.mappings.Producto;

// Compilar:  .\gradlew.bat build    (Borra la carpeta build y recompila todo nuevamente)
// Ejecutar:  .\gradlew.bat bootRun  (Compila y ejecuta la aplicacion localmente con un servidor embebido)
// Limpiar:   .\gradlew.bat clean    (Compila, pasa los tests unitarios y genera el Fat JAR ejecutable)

@SpringBootApplication
public class Application implements CommandLineRunner
{
    @Autowired
    private AppService s;

    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }

    @Override
    public void run(String... args) throws Exception
    {
        Producto p = s.productoObtener(40);
        System.out.println(p.getDescripcion());
    }
}
