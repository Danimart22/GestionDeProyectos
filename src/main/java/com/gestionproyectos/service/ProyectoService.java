package com.gestionproyectos.service;

import com.gestionproyectos.model.dto.ProyectoRequest;
import com.gestionproyectos.model.dto.ProyectoResponse;
import com.gestionproyectos.model.entity.Proyecto;
import com.gestionproyectos.model.entity.Usuario;
import com.gestionproyectos.repository.ProyectoRepository;
import com.gestionproyectos.repository.UsuarioRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;

// @Service marca esta clase como un componente de lógica de negocio gestionado por Spring
@Service
public class ProyectoService {
    private final ProyectoRepository proyectoRepository;
    private final UsuarioRepository usuarioRepository;

    public ProyectoService(ProyectoRepository proyectoRepository, UsuarioRepository usuarioRepository) {
        this.proyectoRepository = proyectoRepository;
        this.usuarioRepository = usuarioRepository;
    }
    // @Transactional: envuelve todo el método en una transacción de base de datos -
    // si algo falla a mitad de camino, TODO se revierte (no queda un estado a medias).
    // Aquí no es estrictamente necesario porque solo hacemos una escritura, pero es buena
    // práctica declarar la intención explícitamente en los métodos que escriben datos
    @Transactional
    public ProyectoResponse crear(ProyectoRequest request, String emailCreador){
        // Buscamos al usuario autenticado real en la base de datos usando su email
        // (que viene del token JWT, nunca confiamos en un "creadorId" que mande el cliente)
        Usuario creador = usuarioRepository.findByEmail(emailCreador).orElseThrow(() -> new IllegalStateException("Usuario autenticado no encontrado"));
        // Creamos la entidad usando el constructor de negocio
        Proyecto proyecto = new Proyecto(request.nombre(), request.descripcion(), creador);
        //Guardamos en la base de datos
        Proyecto guardado = proyectoRepository.save(proyecto);
        // Convertimos la entidad guardada a DTO de respuesta antes de devolverla
        return mapearAResponse(guardado);
    }
    // @Transactional(readOnly = true): optimización para operaciones de solo lectura -
    // le dice a Hibernate que no necesita hacer seguimiento de cambios (dirty checking)
    // sobre las entidades cargadas, lo cual mejora el rendimiento en consultas
    @Transactional(readOnly = true)
    public List<ProyectoResponse> listarActivos(){
        // Usamos el método derivado que ya creamos (findByArchivadoFalse)
        return proyectoRepository.findByArchivadoFalse().stream().map(this::mapearAResponse).toList();
    }
    @Transactional
    public void archivar(Long id){
        Proyecto proyecto = proyectoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con id "+id));
        // Usamos el método de intención de negocio que definimos en la entidad,
        // NO un setter genérico - la entidad controla cómo cambia su propio estado
        proyecto.archivar();
        // NOTA IMPORTANTE: no llamamos a proyectoRepository.save(proyecto) aquí.
        // Gracias a @Transactional, Hibernate detecta automáticamente que el objeto 'proyecto'
        // cambió (esto se llama "dirty checking") y genera el UPDATE al finalizar el método,
        // sin que tengamos que guardarlo explícitamente. Esto SOLO funciona dentro de una
        // transacción activa y con una entidad que Hibernate está "vigilando" (managed entity)
    }
    // Método privado reutilizado por todos los métodos públicos de arriba,
    // para no repetir la lógica de conversión Entidad -> DTO en cada uno
    private ProyectoResponse mapearAResponse(Proyecto proyecto){
        return new ProyectoResponse(
                proyecto.getId(),
                proyecto.getNombre(),
                proyecto.getDescripcion(),
                proyecto.getCreador().getNombre(), // aquí SI se dispara la consulta LAZY si no se cargó antes
                proyecto.getFechaCreacion(),
                proyecto.isArchivado()
        );
    }
    @Transactional(readOnly = true)
    public ProyectoResponse obtenerPorId(Long id) {
        Proyecto proyecto = proyectoRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Proyecto no encontrado con id: " + id));
        return mapearAResponse(proyecto);
    }
}
