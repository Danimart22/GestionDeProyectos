package com.gestionproyectos.model.entity;

import com.gestionproyectos.model.enums.EstadoTarea;
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
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;

@Entity
@Table(name = "tareas")
public class Tarea {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 150)
    private String titulo;

    @Column(length = 1000)
    private String descripcion;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private EstadoTarea estado;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tablero_id", nullable = false)
    private Tablero tablero;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "asignado_id")
    private Usuario asignado;

    @Column(nullable = false)
    private LocalDateTime fechaCreacion;

    private LocalDateTime fechaLimite;

    public Tarea() {
    }

    public Tarea(String titulo, String descripcion, EstadoTarea estado, Usuario asignado, LocalDateTime fechaCreacion, LocalDateTime fechaLimite) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }

    public Long getId() {
        return id;
    }

    public String getTitulo() {
        return titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public EstadoTarea getEstado() {
        return estado;
    }

    public Tablero getTablero() {
        return tablero;
    }

    public Usuario getAsignado() {
        return asignado;
    }

    public LocalDateTime getFechaLimite() {
        return fechaLimite;
    }

    public void asignarA(Usuario usuario){
        this.asignado = usuario;
    }
    public void cambiarEstado(EstadoTarea nuevoEstado){
        this.estado = nuevoEstado;
    }
    public void establecerFechaLimite(LocalDateTime fechaLimite){
        this.fechaLimite = fechaLimite;
    }
    public void actualizarDescripcion(String nuevaDescripcion){
        this.descripcion = nuevaDescripcion;
    }
}
