package com.gestionproyectos.model.dto;
// DTO (Data Transfer Object): solo transporta datos entre el cliente y el servidor,
// no tiene lógica de negocio ni anotaciones de JPA - no es una entidad de base de datos

import jakarta.validation.constraints.Email;       // valida que el campo tenga formato de email
import jakarta.validation.constraints.NotBlank;     // valida que el campo no sea null ni esté vacío/solo espacios
import jakarta.validation.constraints.Size;         // valida longitud mínima/máxima de un String

// record: tipo de clase de Java (desde Java 16) que genera automáticamente
// constructor, getters, equals(), hashCode() y toString() - ideal para DTOs inmutables
public record RegistroRequest(
        @NotBlank(message = "El email es obligatorio") // rechaza si viene vacío o null
        @Email(message = "El email no tiene un formato valido") //Rechaza si no tiene forma de email
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        @Size(min = 8, message = "La contraseña debe tener al menos 8 caracteres")
        String password,

        @NotBlank(message = "El nombre es obligatorio")
        String nombre
) {


}
