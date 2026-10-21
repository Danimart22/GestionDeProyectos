package com.gestionproyectos.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


//DTO para asignar una tarea a un usuario. Identificamos al usuario por email
//porque es el dato que ya manejamos en todo el proyecto (Login y JWT)
public record AsignacionRequest(
        @NotBlank(message = "El email de el usuario es obligatorio")
        @Email(message = "El email no tiene un formato valido")
        String emailAsignado
) {
}
