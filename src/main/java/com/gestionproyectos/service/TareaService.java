package com.gestionproyectos.service;

import com.gestionproyectos.model.dto.TareaRequest;
import com.gestionproyectos.model.dto.TareaResponse;
import com.gestionproyectos.model.entity.Tablero;
import com.gestionproyectos.model.entity.Tarea;
import com.gestionproyectos.model.entity.Usuario;
import com.gestionproyectos.model.enums.EstadoTarea;
import com.gestionproyectos.repository.TableroRepository;
import com.gestionproyectos.repository.TareaRepository;
import com.gestionproyectos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

@Service
public class TareaService {
    private final TareaRepository tareaRepository;       // para guardar y consultar tareas
    private final TableroRepository tableroRepository;   // para verificar que el tablero exista
    private final UsuarioRepository usuarioRepository;   // para buscar al usuario que se asigna

    //Inyección por constructor, igual que el resto de servicios
    public TareaService(
            TareaRepository tareaRepository,
            TableroRepository tableroRepository,
            UsuarioRepository usuarioRepository
    ){
        this.tareaRepository = tareaRepository;
        this.tableroRepository = tableroRepository;
        this.usuarioRepository = usuarioRepository;
    }

    //Necesita transacción completa
    @Transactional
    public TareaResponse crear(Long tableroId, TareaRequest request){
        //Se verifica si el tablero existe primero, si no da error 404 por el GlobalExceptionHandler
        Tablero tablero = tableroRepository.findById(tableroId).orElseThrow(() -> new IllegalArgumentException("Tablero no encontrado con id: "+ tableroId));
        //Constructor de negocio, la entidad fija sola el estado de PENDIENTE y la fecha de creación
        Tarea tarea = new Tarea(request.titulo(), request.descripcion(), tablero);

        //La fecha limite es opcional, solo se establece si el cliente la envió
        if(request.fechaLimite() != null){
            tarea.establecerFechaLimite(request.fechaLimite());
        }

        //Guardamos y devolvemos el DTO de respuesta
        return mapearAResponse(tareaRepository.save(tarea));
    }
    //readOnly evita el dirty checking innecesario
    @Transactional(readOnly = true)
    public List<TareaResponse> listarPorTablero(Long tableroId){
        // Mismo criterio que en TableroService: tablero inexistente (404) no es lo mismo que tablero vacío (200 con lista vacía)
        if(!tableroRepository.existsById(tableroId)){
            throw new IllegalArgumentException("Tablero no encontrado con id: "+tableroId);
        }
        return tareaRepository.findByTableroId(tableroId).stream().map(this::mapearAResponse).toList();
    }

    @Transactional
    public TareaResponse cambiarEstado(Long tareaId, EstadoTarea nuevoEstado){
        Tarea tarea = buscarTarea(tareaId);
        tarea.cambiarEstado(nuevoEstado);
        return mapearAResponse(tarea);
    }

    @Transactional
    public TareaResponse asignar(Long tareaId, String emailAsignado) {
        Tarea tarea = buscarTarea(tareaId);

        // Buscamos al usuario por email; si no existe, 404
        Usuario usuario = usuarioRepository.findByEmail(emailAsignado)
                .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado con email: " + emailAsignado));

        // Método de intención de negocio de la entidad
        tarea.asignarA(usuario);

        return mapearAResponse(tarea);
    }

    @Transactional
    private Tarea buscarTarea(Long tareaId){
        return tareaRepository.findById(tareaId).orElseThrow(() -> new IllegalArgumentException("Tarea no encontrada con id: " + tareaId));
    }

    //Convierte la entidad a DTO. como el asignado es opcional, evitamos un NullPointerException
    private TareaResponse mapearAResponse(Tarea tarea){
        String asignadoNombre = tarea.getAsignado() != null ? tarea.getAsignado().getNombre() : null;
        return new TareaResponse(
                tarea.getId(),
                tarea.getTitulo(),
                tarea.getDescripcion(),
                tarea.getEstado(),
                tarea.getTablero().getId(),
                asignadoNombre,
                tarea.getFechaCreacion(),
                tarea.getFechaLimite()
        );
    }
}
