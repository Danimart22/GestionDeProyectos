package com.gestionproyectos.service;

import com.gestionproyectos.model.dto.TableroRequest;
import com.gestionproyectos.model.dto.TableroResponse;
import com.gestionproyectos.model.entity.Proyecto;
import com.gestionproyectos.model.entity.Tablero;
import com.gestionproyectos.repository.ProyectoRepository;
import com.gestionproyectos.repository.TableroRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TableroService {
    private final TableroRepository tableroRepository;
    // Necesitamos el ProyectoRepository para verificar que el proyecto exista
    // antes de crear un tablero dentro de él - nunca confiamos en que el id de la URL
    // corresponda a un proyecto real sin comprobarlo primero
    private final ProyectoRepository proyectoRepository;

    public TableroService(TableroRepository tableroRepository, ProyectoRepository proyectoRepository) {
        this.tableroRepository = tableroRepository;
        this.proyectoRepository = proyectoRepository;
    }

    @Transactional
    public TableroResponse crear(Long proyectoId, TableroRequest request){
        // Verificamos que el proyecto exista - si no, lanzamos la misma excepción
        // que ya usamos en ProyectoService, y GlobalExceptionHandler la convierte en 404
        Proyecto proyecto = proyectoRepository.findById(proyectoId).orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con id: "+proyectoId));

        Tablero tablero = new Tablero(request.nombre(), proyecto);
        Tablero guardado = tableroRepository.save(tablero);

        return mapearAresponse(guardado);
    }
    @Transactional(readOnly = true)
    public List<TableroResponse> listarProyecto(Long proyectoId){
        // Verificamos que el proyecto exista primero - así, si alguien pide los tableros
        // de un proyecto inexistente, recibe un 404 claro en vez de una lista vacía
        // (una lista vacía sugeriría "el proyecto existe pero no tiene tableros",
        // que es una situación distinta a "este proyecto no existe")
        if(!proyectoRepository.existsById(proyectoId)){
            throw new IllegalArgumentException("Proyecto no encontrado con id: "+proyectoId);
        }
        // Usamos el método derivado que ya creamos (findByProyectoId)
        return tableroRepository.findByProyectoId(proyectoId).stream().map(this::mapearAResponse).toList();
    }
}
