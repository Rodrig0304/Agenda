-- Script para crear la base de datos y tablas de la Agenda
-- Ejecutar este script en MySQL Workbench o en la línea de comandos

-- Crear la base de datos
CREATE DATABASE IF NOT EXISTS agenda_db
CHARACTER SET utf8mb4
COLLATE utf8mb4_unicode_ci;

-- Usar la base de datos
USE agenda_db;

-- Crear tabla de categorías
CREATE TABLE IF NOT EXISTS categorias (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    descripcion VARCHAR(500)
);

-- Crear tabla de contactos
CREATE TABLE IF NOT EXISTS contactos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(100) NOT NULL,
    email VARCHAR(150) NOT NULL UNIQUE,
    categoria_id BIGINT,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL
);

-- Crear tabla de eventos
CREATE TABLE IF NOT EXISTS eventos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    titulo VARCHAR(200) NOT NULL,
    descripcion VARCHAR(1000),
    fecha VARCHAR(10) NOT NULL,
    hora VARCHAR(5),
    categoria_id BIGINT,
    FOREIGN KEY (categoria_id) REFERENCES categorias(id) ON DELETE SET NULL
);

-- Crear tabla de relación muchos a muchos entre eventos y participantes
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
('Amigos', 'Eventos y contactos con amigos');

-- Insertar contactos de ejemplo
INSERT INTO contactos (nombre, email, categoria_id) VALUES
('Juan Pérez', 'juan.perez@email.com', 1),
('María García', 'maria.garcia@email.com', 2),
('Carlos López', 'carlos.lopez@email.com', 3),
('Ana Martínez', 'ana.martinez@email.com', 4);

-- Insertar eventos de ejemplo
INSERT INTO eventos (titulo, descripcion, fecha, hora, categoria_id) VALUES
('Reunión de trabajo', 'Reunión semanal del equipo', '2024-01-15', '09:00', 1),
('Cumpleaños de María', 'Celebración del cumpleaños', '2024-01-20', '19:00', 2),
('Cena familiar', 'Cena dominical con la familia', '2024-01-21', '20:00', 3),
('Salida con amigos', 'Salida al cine con amigos', '2024-01-25', '18:00', 4);

-- Insertar participantes en eventos
INSERT INTO evento_participantes (evento_id, contacto_id) VALUES
(1, 1), -- Juan Pérez participa en la reunión de trabajo
(2, 2), -- María García participa en su cumpleaños
(3, 3), -- Carlos López participa en la cena familiar
(4, 4); -- Ana Martínez participa en la salida con amigos

-- Crear índices para mejorar el rendimiento
CREATE INDEX idx_contactos_categoria ON contactos(categoria_id);
CREATE INDEX idx_eventos_categoria ON eventos(categoria_id);
CREATE INDEX idx_eventos_fecha ON eventos(fecha);
CREATE INDEX idx_contactos_email ON contactos(email);

-- Mostrar las tablas creadas
SHOW TABLES;

-- Mostrar algunos datos de ejemplo
SELECT 'Categorías:' as info;
SELECT * FROM categorias;

SELECT 'Contactos:' as info;
SELECT c.nombre, c.email, cat.nombre as categoria 
FROM contactos c 
LEFT JOIN categorias cat ON c.categoria_id = cat.id;

SELECT 'Eventos:' as info;
SELECT e.titulo, e.fecha, e.hora, cat.nombre as categoria 
FROM eventos e 
LEFT JOIN categorias cat ON e.categoria_id = cat.id;

SELECT 'Participantes en eventos:' as info;
SELECT e.titulo, c.nombre as participante 
FROM eventos e 
JOIN evento_participantes ep ON e.id = ep.evento_id 
JOIN contactos c ON ep.contacto_id = c.id;
