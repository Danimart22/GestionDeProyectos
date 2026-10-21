package com.gestionproyectos.model.dto;

import com.gestionproyectos.model.enums.EstadoTarea;
import java.time.LocalDateTime;

//DTO de salida que muestra lo que el cliente ve de una tarea

public record TareaResponse(
        Long id,
        String titulo,
        String descripcion,
        EstadoTarea estado,
        Long tableroId, //Id del tablero al que pertenece
        String asignadoNombre,   // nombre del asignado o null si aún no tiene responsable
        LocalDateTime fechaCreacion,
        LocalDateTime fechaLimite  //null si no se definió
) {


}
