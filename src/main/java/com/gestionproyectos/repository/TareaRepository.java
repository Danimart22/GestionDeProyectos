package com.gestionproyectos.repository;

import com.gestionproyectos.model.entity.Tarea;
import com.gestionproyectos.model.enums.EstadoTarea;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
//Esto es una interfaz, al ser heredada define que metodos (Nombre, tipo y que recibe) debe tener la clase que lo hereda
public interface TareaRepository extends JpaRepository<Tarea, Long> {

    List<Tarea> findByTableroId (Long tableroId);

    List<Tarea> findByAsignadoIdAndEstado (Long asignadoI, EstadoTarea estado);

}
