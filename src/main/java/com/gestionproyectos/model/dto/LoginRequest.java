package com.gestionproyectos.model.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;


// Record simple para el login: solo necesitamos email y password,
// no repetimos las validaciones de registro (como el mínimo de caracteres)
// porque aquí solo estamos verificando credenciales existentes, no creando una cuenta nueva
public record LoginRequest(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email tiene que tener un formato valido")
        String email,

        @NotBlank(message = "La contraseña es obligatoria")
        String password
) {
}
