package com.gestionproyectos.model.entity;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "proyectos")
public class Proyecto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String nombre;

    @Column(length = 500)
    private String descripcion;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "creador_id", nullable = false)
    private Usuario creador;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    @Column(nullable = false)
    private boolean archivado = false;

    protected Proyecto(){

    }

    public Proyecto(String nombre, String descripcion, Usuario creador, LocalDateTime fechaCreacion, boolean archivado) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.creador = creador;
        this.fechaCreacion = LocalDateTime.now();
        this.archivado = false;
    }

    public Long getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public Usuario getCreador() {
        return creador;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public boolean isArchivado() {
        return archivado;
    }
    public void renombrar(String nuevoNombre){
        this.nombre = nuevoNombre;
    }
    public void archivar(){
        this.archivado = true;
    }
}
