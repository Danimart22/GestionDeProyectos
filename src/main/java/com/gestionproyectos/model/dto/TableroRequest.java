package com.gestionproyectos.model.dto;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO de entrada para crear un tablero - NO pedimos el proyectoId aquí,
// porque lo vamos a tomar de la URL (/api/proyectos/{proyectoId}/tableros), no del body -
// es más RESTful: la jerarquía de recursos vive en la ruta, no duplicada en el payload
public record TableroRequest(
        @NotBlank(message = "El nombre del tablero es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar los 100 caracteres")
        String nombre
) {

}
