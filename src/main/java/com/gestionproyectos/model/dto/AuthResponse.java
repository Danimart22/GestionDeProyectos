package com.gestionproyectos.model.dto;

// DTO de salida: lo que el servidor devuelve al cliente después de un login/registro exitoso
public record AuthResponse(
        String token,  // el JWT que el cliente debe guardar y enviar en futuras peticiones
        String email, // email del usuario autenticado, útil para que el frontend lo muestre sin decodificar el token
        String nombre // nombre del usuario, mismo propósito
) {

}
