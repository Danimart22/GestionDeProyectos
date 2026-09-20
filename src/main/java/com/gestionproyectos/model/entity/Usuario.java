package com.gestionproyectos.model.entity;
import  jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.persistence.Id;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Column;
import java.time.LocalDateTime;


//Mapea la clase como una tabla llamada "usuarios"
@Entity
@Table(name = "usuarios")
public class Usuario {
    //Esto le dice a postgreSQL que para la columna ID use una columna con un valor generado
    //de tipo Identity
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //Especifica que la columna no pueda ser nulo, que tiene que ser unico y de maximo 100 caracteres
    @Column(nullable = false, unique = true, length = 100)
    private String email;

    @Column(nullable = false)
    private String passwordHash;
    //Maximo 80 caracteres y tampoco puede ser nulo
    @Column(nullable = false, length = 80)
    private String nombre;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private boolean activo = true;

    protected Usuario() {
    }

    public Usuario( String email, String passwordHash, String nombre) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.nombre = nombre;
        this.fechaCreacion = LocalDateTime.now();
        this.activo = true;
    }

    public Long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    public String getNombre() {
        return nombre;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean isActivo() {
        return activo;
    }

    public void desactivar(){
        this.activo = false;
    }
    public void cambiarPassword(String nuevoPasswordHash){
        this.passwordHash = nuevoPasswordHash;
    }
}
