package com.gestionproyectos.controller;

import com.gestionproyectos.model.dto.TableroRequest;
import com.gestionproyectos.model.dto.TableroResponse;
import com.gestionproyectos.model.dto.TareaResponse;
import com.gestionproyectos.service.TableroService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

@RestController
@RequestMapping("/api/proyectos/{proyectoId}/tableros")
public class TableroController {
    private final TableroService tableroService;

    public TableroController(TableroService tableroService){
        this.tableroService = tableroService;
    }

    @PostMapping
    public ResponseEntity<TableroResponse> crear(
        @PathVariable Long proyectoId,
        @Valid @RequestBody TableroRequest request
    ){
        return ResponseEntity.status(HttpStatus.CREATED).body(tableroService.crear(proyectoId, request));
    }

    @GetMapping
    public ResponseEntity<List<TableroResponse>> listar(@PathVariable Long proyectoId){
        return ResponseEntity.ok(tableroService.listarPorProyecto(proyectoId));
    }
}
