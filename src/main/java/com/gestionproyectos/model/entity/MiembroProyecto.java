package com.gestionproyectos.model.entity;

import com.gestionproyectos.model.enums.RolProyecto;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

// Entidad "puente" entre Usuario y Proyecto: una fila por cada usuario que pertenece a un proyecto.
// Tiene datos propios (el rol), por eso es una entidad y no un simple @ManyToMany
@Entity
@Table( name = "miembros_proyecto", uniqueConstraints = @UniqueConstraint(columnNames = {"proyecto_id", "usuario_id"}))
public class MiembroProyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)                           // LAZY: no cargamos el proyecto hasta que se necesite
    @JoinColumn(name = "proyecto_id", nullable = false)        // FK obligatoria hacia proyectos
    private Proyecto proyecto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "usuario_id", nullable = false)         // FK obligatoria hacia usuarios
    private Usuario usuario;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RolProyecto rol;

    @Column(nullable = false)
    private LocalDateTime fechaUnion;   //Cuando entró al proyecto

    protected MiembroProyecto(){

    }
    public MiembroProyecto(Proyecto proyecto, Usuario usuario, RolProyecto rol){
        this.proyecto = proyecto;
        this.usuario = usuario;
        this.rol = rol;
        this.fechaUnion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public RolProyecto getRol() {
        return rol;
    }

    public LocalDateTime getFechaUnion() {
        return fechaUnion;
    }
}
