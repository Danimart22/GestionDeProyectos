package com.gestionproyectos.model.dto;

import com.gestionproyectos.model.enums.EstadoTarea;
import jakarta.validation.constraints.NotNull;

//DTO para el endpoint que cambia el estado de las tareas.
//Se usa el enum definido anteriormente, si el cliente manda un valor que no existe, jackson falla al convertir en JSON
//asi un estado invalido jamas llega al servicio
public record CambioEstadoRequest(
        @NotNull(message = "El estado es obligatorio")
        EstadoTarea estado
) {
}
