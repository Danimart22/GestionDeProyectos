package com.gestionproyectos.repository;

import com.gestionproyectos.model.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByArchivadoFalse();

}
