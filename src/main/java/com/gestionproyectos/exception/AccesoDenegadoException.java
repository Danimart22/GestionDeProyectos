package com.gestionproyectos.exception;


// Excepción propia para "perteneces al proyecto, pero tu rol no permite esta acción".
// Extiende RuntimeException (no marcada) para no obligar a declarar "throws" en cada método
public class AccesoDenegadoException extends RuntimeException {
    // Recibe el mensaje que verá el cliente
    public AccesoDenegadoException(String mensaje){
        super(mensaje);
    }
}
