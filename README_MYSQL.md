# 🗄️ Configuración de MySQL para Agenda

## 📋 Requisitos Previos

1. **MySQL Server** instalado (versión 8.0 o superior)
2. **MySQL Workbench** (opcional, para gestión visual)
3. **Java 8+** y **SBT** ya configurados

## 🚀 Pasos para Configurar MySQL

### **Paso 1: Instalar MySQL (si no lo tienes)**

#### **Windows:**
1. Descarga MySQL desde: https://dev.mysql.com/downloads/mysql/
2. Ejecuta el instalador y sigue las instrucciones
3. Anota la contraseña del usuario `root` que configures

#### **macOS:**
```bash
brew install mysql
brew services start mysql
```

#### **Linux (Ubuntu/Debian):**
```bash
sudo apt update
sudo apt install mysql-server
sudo mysql_secure_installation
```

### **Paso 2: Crear la Base de Datos**

#### **Opción A: Usando MySQL Workbench**
1. Abre MySQL Workbench
2. Conéctate a tu servidor MySQL
3. Abre el archivo `database_setup.sql`
4. Ejecuta todo el script (Ctrl+Shift+Enter)

#### **Opción B: Usando línea de comandos**
```bash
# Conectar a MySQL
mysql -u root -p

# Una vez dentro de MySQL, ejecutar:
source /ruta/completa/a/tu/proyecto/database_setup.sql;
```

#### **Opción C: Ejecutar directamente**
```bash
mysql -u root -p < database_setup.sql
```

### **Paso 3: Configurar la Aplicación**

#### **Editar `conf/application.conf`:**
```conf
# Cambiar estas líneas con tus datos:
db.default.username=root
db.default.password=TU_PASSWORD_AQUI
```

**⚠️ IMPORTANTE:** Reemplaza `TU_PASSWORD_AQUI` con la contraseña real de tu usuario MySQL.

### **Paso 4: Verificar la Conexión**

1. **Ejecutar la aplicación:**
```bash
sbt run
```

2. **Verificar en los logs:**
   - Busca mensajes como "Database connection successful"
   - No debe haber errores de conexión

3. **Probar la aplicación:**
   - Ve a http://localhost:9000
   - Crea algunas categorías, contactos y eventos
   - Verifica que se guarden en la base de datos

## 🔧 Solución de Problemas

### **Error: "Access denied for user 'root'@'localhost'"**
```sql
-- Conectar como administrador y ejecutar:
ALTER USER 'root'@'localhost' IDENTIFIED WITH mysql_native_password BY 'tu_nueva_password';
FLUSH PRIVILEGES;
```

### **Error: "Database 'agenda_db' doesn't exist"**
```sql
-- Crear la base de datos manualmente:
CREATE DATABASE agenda_db CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

### **Error: "Table 'categorias' doesn't exist"**
- Ejecuta el script `database_setup.sql` completo
- Verifica que todas las tablas se crearon correctamente

### **Error: "Connection timeout"**
- Verifica que MySQL esté ejecutándose
- Comprueba que el puerto 3306 esté disponible
- Verifica la configuración de firewall

## 📊 Estructura de la Base de Datos

### **Tablas Principales:**

1. **`categorias`** - Categorías para organizar contactos y eventos
   - `id` (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
   - `nombre` (VARCHAR(100), NOT NULL)
   - `descripcion` (VARCHAR(500))

2. **`contactos`** - Información de contactos
   - `id` (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
   - `nombre` (VARCHAR(100), NOT NULL)
   - `email` (VARCHAR(150), NOT NULL, UNIQUE)
   - `categoria_id` (BIGINT, FOREIGN KEY)

3. **`eventos`** - Información de eventos
   - `id` (BIGINT, AUTO_INCREMENT, PRIMARY KEY)
   - `titulo` (VARCHAR(200), NOT NULL)
   - `descripcion` (VARCHAR(1000))
   - `fecha` (VARCHAR(10), NOT NULL)
   - `hora` (VARCHAR(5))
   - `categoria_id` (BIGINT, FOREIGN KEY)

4. **`evento_participantes`** - Relación muchos a muchos entre eventos y participantes
   - `evento_id` (BIGINT, FOREIGN KEY)
   - `contacto_id` (BIGINT, FOREIGN KEY)
   - PRIMARY KEY (evento_id, contacto_id)

### **Relaciones:**
- **Categoría → Contactos:** Uno a Muchos
- **Categoría → Eventos:** Uno a Muchos
- **Eventos ↔ Contactos:** Muchos a Muchos (a través de evento_participantes)

## 🔍 Comandos Útiles de MySQL

### **Verificar la base de datos:**
```sql
USE agenda_db;
SHOW TABLES;
```

### **Ver datos de ejemplo:**
```sql
SELECT * FROM categorias;
SELECT * FROM contactos;
SELECT * FROM eventos;
SELECT * FROM evento_participantes;
```

### **Verificar relaciones:**
```sql
-- Contactos con sus categorías
SELECT c.nombre, c.email, cat.nombre as categoria 
FROM contactos c 
LEFT JOIN categorias cat ON c.categoria_id = cat.id;

-- Eventos con sus categorías y participantes
SELECT e.titulo, cat.nombre as categoria, 
       COUNT(ep.contacto_id) as num_participantes
FROM eventos e 
LEFT JOIN categorias cat ON e.categoria_id = cat.id
LEFT JOIN evento_participantes ep ON e.id = ep.evento_id
GROUP BY e.id;
```

## 🎯 Próximos Pasos

1. **Ejecutar la aplicación** y verificar que funciona
2. **Crear datos de prueba** usando la interfaz web
3. **Verificar que los datos se guardan** en la base de datos
4. **Probar las relaciones** entre entidades
5. **Hacer respaldos** de la base de datos regularmente

## 📝 Notas Importantes

- **Contraseñas:** Nunca subas contraseñas reales a Git
- **Respaldo:** Haz respaldos regulares de tu base de datos
- **Desarrollo:** Usa una base de datos separada para desarrollo y producción
- **Seguridad:** Configura usuarios específicos para la aplicación en producción

¡Tu aplicación ahora está conectada a MySQL! 🎉
