package models;

import java.util.List;

public class Contacto {
    
    private Long id;
    private String nombre;
    private String email;
    
    // Relación con Categoria
    private Categoria categoria;
    
    // Relación muchos a muchos con Evento (como participante)
    private List<Evento> eventosComoParticipante;

    public Contacto() {
    }

    public Contacto(String nombre, String email) {
        this.nombre = nombre;
        this.email = email;
    }

    public Contacto(String nombre, String email, Categoria categoria) {
        this.nombre = nombre;
        this.email = email;
        this.categoria = categoria;
    }

    public Contacto(Long id, String nombre, String email, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.categoria = categoria;
    }

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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Evento> getEventosComoParticipante() {
        return eventosComoParticipante;
    }

    public void setEventosComoParticipante(List<Evento> eventosComoParticipante) {
        this.eventosComoParticipante = eventosComoParticipante;
    }

    public String getNombreCategoria() {
        return categoria != null ? categoria.getNombre() : "Sin categoría";
    }

    public Long getCategoriaId() {
        return categoria != null ? categoria.getId() : null;
    }

    @Override
    public String toString() {
        return "Contacto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                ", categoria=" + (categoria != null ? categoria.getNombre() : "Sin categoría") +
                '}';
    }
}