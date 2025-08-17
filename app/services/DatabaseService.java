package services;

import models.Categoria;
import models.Contacto;
import models.Evento;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DatabaseService {

    private static DatabaseService instance;
    private static final String URL = "jdbc:mysql://localhost:3306/agenda_db?useSSL=false&serverTimezone=UTC&allowPublicKeyRetrieval=true";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private DatabaseService() {
        // Constructor privado para singleton
    }

    public static synchronized DatabaseService getInstance() {
        if (instance == null) {
            instance = new DatabaseService();
        }
        return instance;
    }

    private Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }

    // ========== OPERACIONES DE CATEGORÍA ==========
    
    public static List<Categoria> obtenerTodasLasCategorias() {
        List<Categoria> categorias = new ArrayList<>();
        String sql = "SELECT id, nombre, descripcion FROM categorias";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getLong("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                categorias.add(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return categorias;
    }
    
    public static Optional<Categoria> obtenerCategoriaPorId(Long id) {
        String sql = "SELECT id, nombre, descripcion FROM categorias WHERE id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getLong("id"));
                categoria.setNombre(rs.getString("nombre"));
                categoria.setDescripcion(rs.getString("descripcion"));
                return Optional.of(categoria);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    public static Categoria crearCategoria(String nombre, String descripcion) {
        String sql = "INSERT INTO categorias (nombre, descripcion) VALUES (?, ?)";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Categoria categoria = new Categoria();
                categoria.setId(rs.getLong(1));
                categoria.setNombre(nombre);
                categoria.setDescripcion(descripcion);
                return categoria;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean actualizarCategoria(Long id, String nombre, String descripcion) {
        String sql = "UPDATE categorias SET nombre = ?, descripcion = ? WHERE id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, descripcion);
            stmt.setLong(3, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean eliminarCategoria(Long id) {
        String sql = "DELETE FROM categorias WHERE id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    // ========== OPERACIONES DE CONTACTO ==========
    
    public static List<Contacto> obtenerTodosLosContactos() {
        List<Contacto> contactos = new ArrayList<>();
        String sql = "SELECT c.id, c.nombre, c.email, c.categoria_id, cat.nombre as categoria_nombre " +
                     "FROM contactos c LEFT JOIN categorias cat ON c.categoria_id = cat.id";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Contacto contacto = new Contacto();
                contacto.setId(rs.getLong("id"));
                contacto.setNombre(rs.getString("nombre"));
                contacto.setEmail(rs.getString("email"));
                
                Long categoriaId = rs.getLong("categoria_id");
                if (!rs.wasNull()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(categoriaId);
                    categoria.setNombre(rs.getString("categoria_nombre"));
                    contacto.setCategoria(categoria);
                }
                
                contactos.add(contacto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactos;
    }
    
    public static Optional<Contacto> obtenerContactoPorId(Long id) {
        String sql = "SELECT c.id, c.nombre, c.email, c.categoria_id, cat.nombre as categoria_nombre " +
                     "FROM contactos c LEFT JOIN categorias cat ON c.categoria_id = cat.id WHERE c.id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Contacto contacto = new Contacto();
                contacto.setId(rs.getLong("id"));
                contacto.setNombre(rs.getString("nombre"));
                contacto.setEmail(rs.getString("email"));
                
                Long categoriaId = rs.getLong("categoria_id");
                if (!rs.wasNull()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(categoriaId);
                    categoria.setNombre(rs.getString("categoria_nombre"));
                    contacto.setCategoria(categoria);
                }
                
                return Optional.of(contacto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    public static Contacto crearContacto(String nombre, String email, Long categoriaId) {
        String sql = "INSERT INTO contactos (nombre, email, categoria_id) VALUES (?, ?, ?)";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            if (categoriaId != null) {
                stmt.setLong(3, categoriaId);
            } else {
                stmt.setNull(3, Types.BIGINT);
            }
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Contacto contacto = new Contacto();
                contacto.setId(rs.getLong(1));
                contacto.setNombre(nombre);
                contacto.setEmail(email);
                
                if (categoriaId != null) {
                    Optional<Categoria> categoriaOpt = obtenerCategoriaPorId(categoriaId);
                    categoriaOpt.ifPresent(contacto::setCategoria);
                }
                
                return contacto;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean actualizarContacto(Long id, String nombre, String email, Long categoriaId) {
        String sql = "UPDATE contactos SET nombre = ?, email = ?, categoria_id = ? WHERE id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, nombre);
            stmt.setString(2, email);
            if (categoriaId != null) {
                stmt.setLong(3, categoriaId);
            } else {
                stmt.setNull(3, Types.BIGINT);
            }
            stmt.setLong(4, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean eliminarContacto(Long id) {
        String sql = "DELETE FROM contactos WHERE id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Contacto> obtenerContactosPorCategoria(Long categoriaId) {
        List<Contacto> contactos = new ArrayList<>();
        String sql = "SELECT id, nombre, email FROM contactos WHERE categoria_id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, categoriaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Contacto contacto = new Contacto();
                contacto.setId(rs.getLong("id"));
                contacto.setNombre(rs.getString("nombre"));
                contacto.setEmail(rs.getString("email"));
                contactos.add(contacto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactos;
    }
    
    public static List<Contacto> obtenerContactosSinCategoria() {
        List<Contacto> contactos = new ArrayList<>();
        String sql = "SELECT id, nombre, email FROM contactos WHERE categoria_id IS NULL";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Contacto contacto = new Contacto();
                contacto.setId(rs.getLong("id"));
                contacto.setNombre(rs.getString("nombre"));
                contacto.setEmail(rs.getString("email"));
                contactos.add(contacto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactos;
    }
    
    // ========== OPERACIONES DE EVENTO ==========
    
    public static List<Evento> obtenerTodosLosEventos() {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT e.id, e.titulo, e.descripcion, e.fecha, e.hora, e.categoria_id, cat.nombre as categoria_nombre " +
                     "FROM eventos e LEFT JOIN categorias cat ON e.categoria_id = cat.id";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getLong("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescripcion(rs.getString("descripcion"));
                evento.setFecha(rs.getString("fecha"));
                evento.setHora(rs.getString("hora"));
                
                Long categoriaId = rs.getLong("categoria_id");
                if (!rs.wasNull()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(categoriaId);
                    categoria.setNombre(rs.getString("categoria_nombre"));
                    evento.setCategoria(categoria);
                }
                
                eventos.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }
    
    public static Optional<Evento> obtenerEventoPorId(Long id) {
        String sql = "SELECT e.id, e.titulo, e.descripcion, e.fecha, e.hora, e.categoria_id, cat.nombre as categoria_nombre " +
                     "FROM eventos e LEFT JOIN categorias cat ON e.categoria_id = cat.id WHERE e.id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            ResultSet rs = stmt.executeQuery();
            
            if (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getLong("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescripcion(rs.getString("descripcion"));
                evento.setFecha(rs.getString("fecha"));
                evento.setHora(rs.getString("hora"));
                
                Long categoriaId = rs.getLong("categoria_id");
                if (!rs.wasNull()) {
                    Categoria categoria = new Categoria();
                    categoria.setId(categoriaId);
                    categoria.setNombre(rs.getString("categoria_nombre"));
                    evento.setCategoria(categoria);
                }
                
                return Optional.of(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return Optional.empty();
    }
    
    public static Evento crearEvento(String titulo, String descripcion, String fecha, String hora, Long categoriaId) {
        String sql = "INSERT INTO eventos (titulo, descripcion, fecha, hora, categoria_id) VALUES (?, ?, ?, ?, ?)";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            
            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setString(3, fecha);
            stmt.setString(4, hora);
            if (categoriaId != null) {
                stmt.setLong(5, categoriaId);
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            stmt.executeUpdate();
            
            ResultSet rs = stmt.getGeneratedKeys();
            if (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getLong(1));
                evento.setTitulo(titulo);
                evento.setDescripcion(descripcion);
                evento.setFecha(fecha);
                evento.setHora(hora);
                
                if (categoriaId != null) {
                    Optional<Categoria> categoriaOpt = obtenerCategoriaPorId(categoriaId);
                    categoriaOpt.ifPresent(evento::setCategoria);
                }
                
                return evento;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
    
    public static boolean actualizarEvento(Long id, String titulo, String descripcion, String fecha, String hora, Long categoriaId) {
        String sql = "UPDATE eventos SET titulo = ?, descripcion = ?, fecha = ?, hora = ?, categoria_id = ? WHERE id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, titulo);
            stmt.setString(2, descripcion);
            stmt.setString(3, fecha);
            stmt.setString(4, hora);
            if (categoriaId != null) {
                stmt.setLong(5, categoriaId);
            } else {
                stmt.setNull(5, Types.BIGINT);
            }
            stmt.setLong(6, id);
            
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean eliminarEvento(Long id) {
        String sql = "DELETE FROM eventos WHERE id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, id);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Evento> obtenerEventosPorCategoria(Long categoriaId) {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT id, titulo, descripcion, fecha, hora FROM eventos WHERE categoria_id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, categoriaId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getLong("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescripcion(rs.getString("descripcion"));
                evento.setFecha(rs.getString("fecha"));
                evento.setHora(rs.getString("hora"));
                eventos.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }
    
    // ========== OPERACIONES DE PARTICIPANTES ==========
    
    public static boolean agregarParticipanteAEvento(Long eventoId, Long contactoId) {
        String sql = "INSERT INTO evento_participantes (evento_id, contacto_id) VALUES (?, ?)";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, eventoId);
            stmt.setLong(2, contactoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static boolean removerParticipanteDeEvento(Long eventoId, Long contactoId) {
        String sql = "DELETE FROM evento_participantes WHERE evento_id = ? AND contacto_id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, eventoId);
            stmt.setLong(2, contactoId);
            return stmt.executeUpdate() > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    
    public static List<Contacto> obtenerParticipantesDelEvento(Long eventoId) {
        List<Contacto> participantes = new ArrayList<>();
        String sql = "SELECT c.id, c.nombre, c.email FROM contactos c " +
                     "INNER JOIN evento_participantes ep ON c.id = ep.contacto_id " +
                     "WHERE ep.evento_id = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setLong(1, eventoId);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Contacto contacto = new Contacto();
                contacto.setId(rs.getLong("id"));
                contacto.setNombre(rs.getString("nombre"));
                contacto.setEmail(rs.getString("email"));
                participantes.add(contacto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return participantes;
    }
    
    // ========== CONSULTAS AVANZADAS ==========
    
    public static List<Categoria> obtenerCategoriasConEstadisticas() {
        return obtenerTodasLasCategorias(); // Simplificado para JDBC
    }
    
    public static List<Evento> obtenerEventosConParticipantes() {
        return obtenerTodosLosEventos(); // Simplificado para JDBC
    }
    
    public static List<Evento> obtenerEventosPorFecha(String fecha) {
        List<Evento> eventos = new ArrayList<>();
        String sql = "SELECT id, titulo, descripcion, fecha, hora FROM eventos WHERE fecha = ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, fecha);
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Evento evento = new Evento();
                evento.setId(rs.getLong("id"));
                evento.setTitulo(rs.getString("titulo"));
                evento.setDescripcion(rs.getString("descripcion"));
                evento.setFecha(rs.getString("fecha"));
                evento.setHora(rs.getString("hora"));
                eventos.add(evento);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return eventos;
    }
    
    public static List<Contacto> buscarContactosPorNombre(String nombre) {
        List<Contacto> contactos = new ArrayList<>();
        String sql = "SELECT id, nombre, email FROM contactos WHERE nombre LIKE ?";
        
        try (Connection conn = getInstance().getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setString(1, "%" + nombre + "%");
            ResultSet rs = stmt.executeQuery();
            
            while (rs.next()) {
                Contacto contacto = new Contacto();
                contacto.setId(rs.getLong("id"));
                contacto.setNombre(rs.getString("nombre"));
                contacto.setEmail(rs.getString("email"));
                contactos.add(contacto);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return contactos;
    }
}
