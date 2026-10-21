package com.gestionproyectos.controller;


import com.gestionproyectos.model.dto.AsignacionRequest;
import com.gestionproyectos.model.dto.CambioEstadoRequest;
import com.gestionproyectos.model.dto.TareaRequest;
import com.gestionproyectos.model.dto.TareaResponse;
import com.gestionproyectos.service.TareaService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

// Prefijo común /api. Las rutas completas van en cada método porque crear/listar cuelgan
// de un tablero (/tableros/{id}/tareas), pero cambiar estado y asignar operan sobre una tarea
// concreta que ya tiene su propio id (/tareas/{id}/...)

@RestController
@RequestMapping("/api")
public class TareaController {
    private final TareaService tareaService;

    public TareaController(TareaService tareaService){
        this.tareaService = tareaService;
    }

    //POST /api/tableros/{tableroId}/tareas -> crea una tarea para este tablero
    @PostMapping("/tableros/{tableroId}/tareas")
    public ResponseEntity<TareaResponse> crear(
            @PathVariable Long tableroId,         // id del tablero, tomado de la URL
            @Valid @RequestBody TareaRequest request     // body validado con las anotaciones del DTO
    ){
        //201 CREATED porque se creó un nuevo recurso
        return ResponseEntity.status(HttpStatus.CREATED).body(tareaService.crear(tableroId, request));
    }
    // GET /api/tableros/{tableroId}/tareas -> lista las tareas de ese tablero
    @GetMapping("/tableros/{tableroId}/tareas")
    public ResponseEntity<List<TareaResponse>> listar(@PathVariable Long tableroId){
        return ResponseEntity.ok(tareaService.listarPorTablero(tableroId));
    }
    // PATCH /api/tareas/{id}/estado -> cambia el estado (modificación parcial, por eso PATCH)
    @PatchMapping("/tareas/{id}/estado")
    public ResponseEntity<TareaResponse> cambiarEstado(
            @PathVariable Long id,
            @Valid @RequestBody CambioEstadoRequest request
    ){
        return ResponseEntity.ok(tareaService.cambiarEstado(id, request.estado()));
    }

    @PatchMapping("/tareas/{id}/asignar")
    public ResponseEntity<TareaResponse> asignar(
            @PathVariable Long id,
            @Valid @RequestBody AsignacionRequest request
    ){
        return ResponseEntity.ok(tareaService.asignar(id, request.emailAsignado()));
    }
}
