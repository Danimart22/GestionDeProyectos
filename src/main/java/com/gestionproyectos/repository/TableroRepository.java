package com.gestionproyectos.repository;

import com.gestionproyectos.model.entity.Tablero;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

//Esto es una interfaz, al ser heredada define que metodos (Nombre, tipo y que recibe) debe tener la clase que lo hereda
public interface TableroRepository extends JpaRepository<Tablero, Long> {

    List<Tablero> findByProyectoId(Long proyectoId);

}
