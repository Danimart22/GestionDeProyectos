package com.gestionproyectos.controller;

// DTOs que ya construimos para las peticiones y respuestas de Proyecto
import com.gestionproyectos.model.dto.ProyectoRequest;
import com.gestionproyectos.model.dto.ProyectoResponse;
// El servicio con toda la lógica de negocio, que este controlador solo va a invocar
import com.gestionproyectos.service.ProyectoService;
// Activa las validaciones (@NotBlank, @Size) declaradas en ProyectoRequest
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
// Representa al usuario autenticado de la petición actual (igual que usamos en /api/me)
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

// Todos los endpoints de este controlador empiezan con /api/proyectos
@RestController
@RequestMapping("/api/proyectos")
public class ProyectoController {
    // Única dependencia: el servicio. El controlador NO habla directamente
    // con los repositorios - esa responsabilidad es exclusiva de la capa de servicio
    private final ProyectoService proyectoService;

    public ProyectoController(ProyectoService proyectoService) {
        this.proyectoService = proyectoService;
    }
    // POST /api/proyectos - crea un proyecto nuevo
    @PostMapping
    public ResponseEntity<ProyectoResponse> crear(
            @Valid @RequestBody ProyectoRequest request,
            // Spring inyecta automáticamente el usuario autenticado extraído del JWT
            // por nuestro JwtAuthenticationFilter (mismo mecanismo que en /api/me)
            Authentication authentication
    ){
        String emailCreador = authentication.getName();
        ProyectoResponse response = proyectoService.crear(request, emailCreador);
        // 201 Created: se creó un recurso nuevo exitosamente
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }
    // GET /api/proyectos - lista todos los proyectos activos (no archivados)
    @GetMapping
    public ResponseEntity<List<ProyectoResponse>> listar(){
        List<ProyectoResponse> proyectos = proyectoService.listarActivos();
        // 200 OK con la lista completa (puede ser una lista vacía si no hay proyectos,
        // eso NO es un error, así que sigue siendo 200, nunca 404)
        return ResponseEntity.ok(proyectos);
    }
    // GET /api/proyectos/{id} - obtiene un proyecto específico por su id
    @GetMapping("/{id}")
    public ResponseEntity<ProyectoResponse> obtenerPorId(@PathVariable Long id){
        // @PathVariable extrae el {id} de la URL y lo convierte automáticamente a Long
        ProyectoResponse response = proyectoService.obtenerPorId(id);
        return ResponseEntity.ok(response);
    }
    // PATCH /api/proyectos/{id}/archivar - archiva un proyecto existente
    // Usamos PATCH (no PUT) porque es una modificación PARCIAL del recurso
    // (solo cambiamos el estado "archivado", no reemplazamos el proyecto completo)
    @PatchMapping("/{id}/archivar")
    public ResponseEntity<Void> archivar(@PathVariable Long id){
        proyectoService.archivar(id);
        // 204 No Content: la operación fue exitosa, pero no hay nada que devolver en el cuerpo
        return ResponseEntity.noContent().build();
    }
}
