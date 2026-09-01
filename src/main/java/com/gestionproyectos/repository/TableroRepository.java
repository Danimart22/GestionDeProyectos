package com.gestionproyectos.repository;

import com.gestionproyectos.model.entity.Tablero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TableroRepository extends JpaRepository<Tablero, Long> {

    List<Tablero> findByProyectoId(Long proyectoId);

}
