CREATE TABLE usuarios(
    id BIGSERIAL PRIMARY KEY ,
    email VARCHAR(100) NOT NULL UNIQUE,
    password_hash VARCHAR(255) NOT NULL,
    nombre VARCHAR(80) NOT NULL,
    fecha_creacion TIMESTAMP NOT NULL,
    activo BOOLEAN NOT NULL DEFAULT TRUE
);

CREATE TABLE proyectos (
    id BIGSERIAL PRIMARY KEY ,
    nombre VARCHAR(120) NOT NULL,
    descripcion VARCHAR(500),
    creador_id BIGINT NOT NULL REFERENCES usuarios(id),
    fecha_creacion TIMESTAMP NOT NULL,
    archivado BOOLEAN NOT NULL DEFAULT FALSE
);

CREATE TABLE tableros (
    id BIGSERIAL PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    proyecto_id BIGINT NOT NULL REFERENCES proyectos(id),
    fecha_creacion TIMESTAMP NOT NULL
);

CREATE TABLE tareas (
    id BIGSERIAL PRIMARY KEY ,
    titulo VARCHAR(150) NOT NULL,
    descripcion VARCHAR(1000),
    estado VARCHAR(20) NOT NULL,
    tablero_id BIGINT NOT NULL REFERENCES tableros(id),
    asignado_id BIGINT REFERENCES usuarios(id),
    fecha_creacion TIMESTAMP NOT NULL,
    fecha_limite TIMESTAMP
);

CREATE INDEX idx_proyectos_creador ON proyectos(creador_id);
CREATE INDEX idx_tableros_proyecto ON tableros(proyecto_id);
CREATE INDEX idx_tareas_tablero ON tareas(tablero_id);
CREATE INDEX id_tareas_asignado ON tareas(asignado_id);