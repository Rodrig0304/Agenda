package services;

import models.Categoria;
import models.Contacto;
import models.Evento;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AgendaService {
    
    // Listas centralizadas para todas las entidades
    private static List<Categoria> categorias = new ArrayList<>();
    private static List<Contacto> contactos = new ArrayList<>();
    private static List<Evento> eventos = new ArrayList<>();
    
    // Contadores para IDs
    private static Long contadorCategoria = 1L;
    private static Long contadorContacto = 1L;
    private static Long contadorEvento = 1L;

    // ========== MÉTODOS PARA CATEGORÍAS ==========
    
    public static List<Categoria> obtenerTodasLasCategorias() {
        return new ArrayList<>(categorias);
    }
    
    public static Categoria crearCategoria(String nombre, String descripcion) {
        Categoria categoria = new Categoria(contadorCategoria++, nombre, descripcion);
        categorias.add(categoria);
        return categoria;
    }
    
    public static Categoria obtenerCategoriaPorId(Long id) {
        return categorias.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public static boolean actualizarCategoria(Long id, String nombre, String descripcion) {
        Categoria categoria = obtenerCategoriaPorId(id);
        if (categoria != null) {
            categoria.setNombre(nombre);
            categoria.setDescripcion(descripcion);
            return true;
        }
        return false;
    }
    
    public static boolean eliminarCategoria(Long id) {
        Categoria categoria = obtenerCategoriaPorId(id);
        if (categoria != null) {
            // Desvincular todos los contactos de esta categoría
            for (Contacto contacto : categoria.getContactos()) {
                contacto.setCategoria(null);
            }
            // Desvincular todos los eventos de esta categoría
            for (Evento evento : categoria.getEventos()) {
                evento.setCategoria(null);
            }
            return categorias.remove(categoria);
        }
        return false;
    }

    // ========== MÉTODOS PARA CONTACTOS ==========
    
    public static List<Contacto> obtenerTodosLosContactos() {
        return new ArrayList<>(contactos);
    }
    
    public static Contacto crearContacto(String nombre, String email, Long categoriaId) {
        Categoria categoria = obtenerCategoriaPorId(categoriaId);
        Contacto contacto = new Contacto(contadorContacto++, nombre, email, categoria);
        contactos.add(contacto);
        
        // Actualizar la relación bidireccional
        if (categoria != null) {
            categoria.agregarContacto(contacto);
        }
        
        return contacto;
    }
    
    public static Contacto obtenerContactoPorId(Long id) {
        return contactos.stream()
                .filter(c -> c.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public static List<Contacto> obtenerContactosPorCategoria(Long categoriaId) {
        return contactos.stream()
                .filter(c -> c.getCategoria() != null && c.getCategoria().getId().equals(categoriaId))
                .collect(Collectors.toList());
    }
    
    public static boolean actualizarContacto(Long id, String nombre, String email, Long categoriaId) {
        Contacto contacto = obtenerContactoPorId(id);
        if (contacto != null) {
            // Remover de la categoría anterior
            Categoria categoriaAnterior = contacto.getCategoria();
            if (categoriaAnterior != null) {
                categoriaAnterior.removerContacto(contacto);
            }
            
            // Actualizar datos
            contacto.setNombre(nombre);
            contacto.setEmail(email);
            
            // Asignar nueva categoría
            Categoria nuevaCategoria = obtenerCategoriaPorId(categoriaId);
            contacto.setCategoria(nuevaCategoria);
            
            // Actualizar relación bidireccional
            if (nuevaCategoria != null) {
                nuevaCategoria.agregarContacto(contacto);
            }
            
            return true;
        }
        return false;
    }
    
    public static boolean eliminarContacto(Long id) {
        Contacto contacto = obtenerContactoPorId(id);
        if (contacto != null) {
            // Remover de la categoría
            if (contacto.getCategoria() != null) {
                contacto.getCategoria().removerContacto(contacto);
            }
            
            // Remover de todos los eventos donde participe
            for (Evento evento : eventos) {
                evento.removerParticipante(contacto);
            }
            
            return contactos.remove(contacto);
        }
        return false;
    }

    // ========== MÉTODOS PARA EVENTOS ==========
    
    public static List<Evento> obtenerTodosLosEventos() {
        return new ArrayList<>(eventos);
    }
    
    public static Evento crearEvento(String titulo, String descripcion, String fecha, String hora, Long categoriaId) {
        Categoria categoria = obtenerCategoriaPorId(categoriaId);
        Evento evento = new Evento(contadorEvento++, titulo, descripcion, fecha, hora, categoria);
        eventos.add(evento);
        
        // Actualizar la relación bidireccional
        if (categoria != null) {
            categoria.agregarEvento(evento);
        }
        
        return evento;
    }
    
    public static Evento obtenerEventoPorId(Long id) {
        return eventos.stream()
                .filter(e -> e.getId().equals(id))
                .findFirst()
                .orElse(null);
    }
    
    public static List<Evento> obtenerEventosPorCategoria(Long categoriaId) {
        return eventos.stream()
                .filter(e -> e.getCategoria() != null && e.getCategoria().getId().equals(categoriaId))
                .collect(Collectors.toList());
    }
    
    public static boolean actualizarEvento(Long id, String titulo, String descripcion, String fecha, String hora, Long categoriaId) {
        Evento evento = obtenerEventoPorId(id);
        if (evento != null) {
            // Remover de la categoría anterior
            Categoria categoriaAnterior = evento.getCategoria();
            if (categoriaAnterior != null) {
                categoriaAnterior.removerEvento(evento);
            }
            
            // Actualizar datos
            evento.setTitulo(titulo);
            evento.setDescripcion(descripcion);
            evento.setFecha(fecha);
            evento.setHora(hora);
            
            // Asignar nueva categoría
            Categoria nuevaCategoria = obtenerCategoriaPorId(categoriaId);
            evento.setCategoria(nuevaCategoria);
            
            // Actualizar relación bidireccional
            if (nuevaCategoria != null) {
                nuevaCategoria.agregarEvento(evento);
            }
            
            return true;
        }
        return false;
    }
    
    public static boolean eliminarEvento(Long id) {
        Evento evento = obtenerEventoPorId(id);
        if (evento != null) {
            // Remover de la categoría
            if (evento.getCategoria() != null) {
                evento.getCategoria().removerEvento(evento);
            }
            
            return eventos.remove(evento);
        }
        return false;
    }

    // ========== MÉTODOS PARA RELACIONES ==========
    
    public static boolean agregarParticipanteAEvento(Long eventoId, Long contactoId) {
        Evento evento = obtenerEventoPorId(eventoId);
        Contacto contacto = obtenerContactoPorId(contactoId);
        
        if (evento != null && contacto != null) {
            evento.agregarParticipante(contacto);
            return true;
        }
        return false;
    }
    
    public static boolean removerParticipanteDeEvento(Long eventoId, Long contactoId) {
        Evento evento = obtenerEventoPorId(eventoId);
        Contacto contacto = obtenerContactoPorId(contactoId);
        
        if (evento != null && contacto != null) {
            evento.removerParticipante(contacto);
            return true;
        }
        return false;
    }
    
    public static List<Contacto> obtenerParticipantesDelEvento(Long eventoId) {
        Evento evento = obtenerEventoPorId(eventoId);
        if (evento != null) {
            return new ArrayList<>(evento.getParticipantes());
        }
        return new ArrayList<>();
    }

    // ========== MÉTODOS DE CONSULTA AVANZADA ==========
    
    public static List<Categoria> obtenerCategoriasConEstadisticas() {
        return categorias.stream()
                .filter(c -> c.tieneContactos() || c.tieneEventos())
                .collect(Collectors.toList());
    }
    
    public static List<Evento> obtenerEventosConParticipantes() {
        return eventos.stream()
                .filter(e -> !e.getParticipantes().isEmpty())
                .collect(Collectors.toList());
    }
    
    public static List<Contacto> obtenerContactosSinCategoria() {
        return contactos.stream()
                .filter(c -> c.getCategoria() == null)
                .collect(Collectors.toList());
    }
}
