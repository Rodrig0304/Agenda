package models;

import java.util.ArrayList;
import java.util.List;

public class Categoria {
    private Long id;
    private String nombre;
    private String descripcion;
    
    // Relaciones inversas
    private List<Contacto> contactos;
    private List<Evento> eventos;

    public Categoria() {
        this.contactos = new ArrayList<>();
        this.eventos = new ArrayList<>();
    }

    public Categoria(Long id, String nombre) {
        this.id = id;
        this.nombre = nombre;
        this.contactos = new ArrayList<>();
        this.eventos = new ArrayList<>();
    }

    public Categoria(Long id, String nombre, String descripcion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.contactos = new ArrayList<>();
        this.eventos = new ArrayList<>();
    }

    // Getters y setters existentes
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    // Nuevos getters y setters para relaciones inversas
    public List<Contacto> getContactos() {
        return contactos;
    }

    public void setContactos(List<Contacto> contactos) {
        this.contactos = contactos;
    }

    public List<Evento> getEventos() {
        return eventos;
    }

    public void setEventos(List<Evento> eventos) {
        this.eventos = eventos;
    }

    // Métodos de utilidad para manejar relaciones
    public void agregarContacto(Contacto contacto) {
        if (this.contactos == null) {
            this.contactos = new ArrayList<>();
        }
        this.contactos.add(contacto);
    }

    public void removerContacto(Contacto contacto) {
        if (this.contactos != null) {
            this.contactos.remove(contacto);
        }
    }

    public void agregarEvento(Evento evento) {
        if (this.eventos == null) {
            this.eventos = new ArrayList<>();
        }
        this.eventos.add(evento);
    }

    public void removerEvento(Evento evento) {
        if (this.eventos != null) {
            this.eventos.remove(evento);
        }
    }

    // Métodos para obtener información de la categoría
    public int getCantidadContactos() {
        return this.contactos != null ? this.contactos.size() : 0;
    }

    public int getCantidadEventos() {
        return this.eventos != null ? this.eventos.size() : 0;
    }

    public boolean tieneContactos() {
        return this.contactos != null && !this.contactos.isEmpty();
    }

    public boolean tieneEventos() {
        return this.eventos != null && !this.eventos.isEmpty();
    }
}
