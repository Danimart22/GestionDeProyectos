package com.gestionproyectos.model.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;


//DTO de entrada para una nueva tearea. No se pide TableroId (porque viene de la URL dinamica),
//Tampoco se pide estado (toda tarea nace pendiente por defecto)
//ni asignado (Luego se asigna en otro endpoint)
public record TareaRequest(
        @NotBlank(message = "El titulo es obligatorio")
        @Size(max = 150, message = "El titulo no puede superar los 150 caracteres")
        String titulo,
        @Size(max = 1000, message = "La descripción no puede superar los 1000 caracteres")
        String descripcion,

        LocalDateTime fechaLimite
) {


}
