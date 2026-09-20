package com.gestionproyectos.controller;

// Representa al usuario autenticado en el contexto de seguridad de la petición actual
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Map;
@RestController
@RequestMapping("/api")
public class UsuarioController {
    // GET /api/me - endpoint protegido de prueba: solo responde si el JwtAuthenticationFilter
    // logró autenticar correctamente la petición (si no, Spring Security rechaza antes de llegar aquí)
    @GetMapping("/me")
    public Map<String, String> obtenerUsuarioActual(Authentication authentication){
        // Spring inyecta automáticamente el objeto Authentication que nuestro
        // JwtAuthenticationFilter guardó en el SecurityContextHolder - authentication.getName()
        // devuelve el "subject" del token, que en nuestro caso es el email (ver JwtService.generarToken)
        return Map.of("email", authentication.getName());
    }
}
