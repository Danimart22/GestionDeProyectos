package com.gestionproyectos.repository;

import com.gestionproyectos.model.entity.MiembroProyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface MiembroProyectoRepository extends JpaRepository<MiembroProyecto, Long> {
    Optional<MiembroProyecto> findByProyectoIdAndUsuarioId(Long proyectoId, Long usuarioId);
    List<MiembroProyecto> findByUsuarioId(Long usuarioId);
}
