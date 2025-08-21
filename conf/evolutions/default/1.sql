# --- !Ups

-- Crear base de datos si no existe
-- CREATE DATABASE IF NOT EXISTS agenda_db;
-- USE agenda_db;

-- Tabla de categorías
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500)
);

-- Tabla de contactos
CREATE TABLE IF NOT EXISTS contactos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    categoria_id BIGINT,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL
);

-- Tabla de eventos
CREATE TABLE IF NOT EXISTS eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descripcion VARCHAR(1000),
    fecha VARCHAR(10) NOT NULL,
    hora VARCHAR(5),
    categoria_id BIGINT,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL
);

-- Tabla de relación muchos a muchos entre eventos y participantes
CREATE TABLE IF NOT EXISTS evento_participantes (
    evento_id BIGINT NOT NULL,
    contacto_id BIGINT NOT NULL,
    PRIMARY KEY (evento_id, contacto_id),
    FOREIGN KEY (evento_id) REFERENCES eventos(id) ON DELETE CASCADE,
    FOREIGN KEY (contacto_id) REFERENCES contactos(id) ON DELETE CASCADE
);

-- Insertar datos de ejemplo
INSERT INTO categorias (nombre, descripcion) VALUES 
('Trabajo', 'Eventos y contactos relacionados con el trabajo'),
('Personal', 'Eventos y contactos personales'),
('Familia', 'Eventos y contactos familiares'),
('Amigos', 'Eventos y contactos con amigos')
ON DUPLICATE KEY UPDATE nombre = nombre;

INSERT INTO contactos (nombre, email, categoria_id) VALUES 
('Juan Pérez', 'juan.perez@email.com', 1),
('María García', 'maria.garcia@email.com', 2),
('Carlos López', 'carlos.lopez@email.com', 3),
('Ana Martínez', 'ana.martinez@email.com', 4)
ON DUPLICATE KEY UPDATE nombre = nombre;

INSERT INTO eventos (titulo, descripcion, fecha, hora, categoria_id) VALUES 
('Reunión de trabajo', 'Reunión semanal del equipo', '2024-01-15', '09:00', 1),
('Cumpleaños de María', 'Celebración del cumpleaños', '2024-01-20', '19:00', 2),
('Cena familiar', 'Cena dominical con la familia', '2024-01-21', '20:00', 3),
('Salida con amigos', 'Salida al cine con amigos', '2024-01-25', '18:00', 4)
ON DUPLICATE KEY UPDATE titulo = titulo;

INSERT INTO evento_participantes (evento_id, contacto_id) VALUES 
(1, 1), -- Juan Pérez participa en la reunión de trabajo
(2, 2), -- María García participa en su cumpleaños
(3, 3), -- Carlos López participa en la cena familiar
(4, 4)  -- Ana Martínez participa en la salida con amigos
ON DUPLICATE KEY UPDATE evento_id = evento_id;

-- Crear índices para mejorar rendimiento
-- Los índices se pueden crear manualmente después si es necesario
-- CREATE INDEX idx_contactos_categoria ON contactos(categoria_id);
-- CREATE INDEX idx_eventos_categoria ON eventos(categoria_id);
-- CREATE INDEX idx_eventos_fecha ON eventos(fecha);
-- CREATE INDEX idx_contactos_email ON contactos(email);

# --- !Downs

-- Eliminar índices
DROP INDEX IF EXISTS idx_contactos_email ON contactos;
DROP INDEX IF EXISTS idx_eventos_fecha ON eventos;
DROP INDEX IF EXISTS idx_eventos_categoria ON eventos;
DROP INDEX IF EXISTS idx_contactos_categoria ON contactos;

-- Eliminar datos de ejemplo
DELETE FROM evento_participantes;
DELETE FROM eventos;
DELETE FROM contactos;
DELETE FROM categorias;

-- Eliminar tablas
DROP TABLE IF EXISTS evento_participantes;
DROP TABLE IF EXISTS eventos;
DROP TABLE IF EXISTS contactos;
DROP TABLE IF EXISTS categorias;
