package com.gestionproyectos.security;

import com.gestionproyectos.model.entity.Usuario;
import com.gestionproyectos.repository.UsuarioRepository;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.util.Collections;

@Service
//Implemens UserDetailService es una interfaz de spring security que tiene un solo metodo loadUserByUsername
//le decimos como buscar el usuario que en este caso es por email
public class CustomUserDetailsService implements UserDetailsService {
    private final UsuarioRepository usuarioRepository;

    public CustomUserDetailsService(UsuarioRepository usuarioRepository){
        this.usuarioRepository = usuarioRepository;
    }

    @Override
    //En caso de que no se encuentre el usuario se lanza la excepción personalizada de spring security: throws UsernameNotFoundException
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException{
        Usuario usuario = usuarioRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado:  "+email));
        //este User es una clase que da spring security, empaqueta lo minimo que necesita el framework que es el identificador (email),
        //hash de la contraseña y la lista de roles
        return new User(
                usuario.getEmail(),
                usuario.getPasswordHash(),
                Collections.emptyList()
        );
    }
}
