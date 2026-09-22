package com.gestionproyectos.model.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "tableros")
public class Tablero {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "proyecto_id", nullable = false)
    private Proyecto proyecto;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    protected Tablero(){

    }

    public Tablero(String nombre, Proyecto proyecto) {
        this.nombre = nombre;
        this.proyecto = proyecto;
        this.fechaCreacion = LocalDateTime.now();
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public Proyecto getProyecto() {
        return proyecto;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public void renombrar(String nuevoNombre){
        this.nombre = nuevoNombre;
    }
}
