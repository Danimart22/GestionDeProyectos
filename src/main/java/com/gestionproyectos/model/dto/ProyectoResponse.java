package com.gestionproyectos.model.dto;

import java.time.LocalDateTime;

// DTO de salida - expone solo lo que el cliente necesita ver, en el formato que nos convenga,
// desacoplado por completo de cómo está estructurada la entidad Proyecto internamente
public record ProyectoResponse(
        Long id,
        String nombre,
        String descripcion,
        String creadorNombre, // el nombre del creador, no el objeto Usuario completo
        LocalDateTime fechaCreacion,
        boolean archivado
) {

}
