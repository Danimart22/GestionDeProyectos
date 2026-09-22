package com.gestionproyectos.model.dto;

import java.time.LocalDateTime;

public record TableroResponse(
        Long id,
        String nombre,
        Long proyectoId, // el id del proyecto al que pertenece, útil para el frontend
        LocalDateTime fechaCreacion
) {
}
