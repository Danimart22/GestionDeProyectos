package com.gestionproyectos.repository;

import com.gestionproyectos.model.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
//Esto es una interfaz, al ser heredada define que metodos (Nombre, tipo y que recibe) debe tener la clase que lo hereda
public interface UsuarioRepository extends JpaRepository<Usuario, Long> {

    Optional<Usuario> findByEmail(String email);

    boolean existsByEmail(String email);
}
