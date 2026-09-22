package com.gestionproyectos.model.dto;


// Anotaciones de validación de Bean Validation
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

// DTO de entrada para crear un proyecto - el cliente solo manda nombre y descripción,
// NUNCA el creador (eso lo determinamos nosotros desde el usuario autenticado, nunca confiando
// en lo que el cliente diga que es - por seguridad, un usuario no puede crear un proyecto "a nombre de otro")
public record ProyectoRequest(
        @NotBlank(message = "El nombre del proyecto es obligatorio")
        @Size(max = 120, message = "El nombre no puede superar los 120 caracteres")
        String nombre,

        @Size(max = 500, message = "La descripción no puede superar los 500 caracteres")
        String descripcion //Es opcional la descripción
){

}