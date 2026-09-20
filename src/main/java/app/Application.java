package app;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// Compilar:  .\gradlew.bat build    (Borra la carpeta build y recompila todo nuevamente)
// Ejecutar:  .\gradlew.bat bootRun  (Compila y ejecuta la aplicacion localmente con un servidor embebido)
// Limpiar:   .\gradlew.bat clean    (Compila, pasa los tests unitarios y genera el Fat JAR ejecutable)

@SpringBootApplication
public class Application
{
    public static void main(String[] args)
    {
        SpringApplication.run(Application.class, args);
    }
}
