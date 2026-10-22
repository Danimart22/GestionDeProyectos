package com.gestionproyectos.model.enums;


// Roles que un usuario puede tener DENTRO de un proyecto concreto.
// No son roles globales del sistema: la misma persona puede ser OWNER en un proyecto y VIEWER en otro
public enum RolProyecto {
    OWNER,   // dueño: puede todo, incluido gestionar miembros y archivar el proyecto
    MEMBER,  // miembro: puede crear y modificar tableros y tareas
    VIEWER   // observador: solo puede consultar
}
