package models;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    
    private Long id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    
    // Relación muchos a uno con Categoria
    private Categoria categoria;
    
    // Relación muchos a muchos con Contacto (participantes)
    private List<Contacto> participantes;

    public Evento() {
        this.participantes = new ArrayList<>();
    }

    public Evento(String titulo, String descripcion, String fecha, String hora) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.participantes = new ArrayList<>();
    }

    public Evento(String titulo, String descripcion, String fecha, String hora, Categoria categoria) {
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.categoria = categoria;
        this.participantes = new ArrayList<>();
    }

    public Evento(Long id, String titulo, String descripcion, String fecha, String hora, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.categoria = categoria;
        this.participantes = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public Categoria getCategoria() {
        return categoria;
    }

    public void setCategoria(Categoria categoria) {
        this.categoria = categoria;
    }

    public List<Contacto> getParticipantes() {
        return participantes;
    }

    public void setParticipantes(List<Contacto> participantes) {
        this.participantes = participantes;
    }

    // Métodos de utilidad para manejar participantes
    public void agregarParticipante(Contacto contacto) {
        if (this.participantes == null) {
            this.participantes = new ArrayList<>();
        }
        if (!this.participantes.contains(contacto)) {
            this.participantes.add(contacto);
        }
    }

    public void removerParticipante(Contacto contacto) {
        if (this.participantes != null) {
            this.participantes.remove(contacto);
        }
    }

    public int getCantidadParticipantes() {
        return this.participantes != null ? this.participantes.size() : 0;
    }

    public String getNombreCategoria() {
        return categoria != null ? categoria.getNombre() : "Sin categoría";
    }

    public Long getCategoriaId() {
        return categoria != null ? categoria.getId() : null;
    }

    @Override
    public String toString() {
        return "Evento{" +
                "id=" + id +
                ", titulo='" + titulo + '\'' +
                ", fecha='" + fecha + '\'' +
                ", hora='" + hora + '\'' +
                ", categoria=" + (categoria != null ? categoria.getNombre() : "Sin categoría") +
                ", participantes=" + getCantidadParticipantes() +
                '}';
    }
}