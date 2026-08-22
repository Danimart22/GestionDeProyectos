package com.gestionproyectos;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
//Esta anotación son en realidad tres en una:
//Configuration que marca esta clase como fuente de definiciones beans
//EnableAutoConfiguration esta le dice a springboot que configure automaticamente todo lo que pueda
//basado en las dependencias que ves en el classpath
//ComponentScan que le dice a spring que escanee este paquete y todos sus subpaquetes buscando clases anotadas
//para registrarlas automaticamente
public class GestionProyectosApplication {
    public static void main (String [] args){
        //Esto arranca todo lo del contexto spring, lee la configuración, inyecta dependencias y levanta el servidor
        //embebido y deja la aplicación escuchando peticiones
        SpringApplication.run(GestionProyectosApplication.class, args);
    }
}
