package com.gestionproyectos.repository;

import com.gestionproyectos.model.entity.Proyecto;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
//Esto es una interfaz, al ser heredada define que metodos (Nombre, tipo y que recibe) debe tener la clase que lo hereda
public interface ProyectoRepository extends JpaRepository<Proyecto, Long> {

    List<Proyecto> findByArchivadoFalse();

}
