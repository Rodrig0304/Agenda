package models;

import java.util.ArrayList;
import java.util.List;

public class Evento {
    private Long id;
    private String titulo;
    private String descripcion;
    private String fecha;
    private String hora;
    
    // Nuevas relaciones
    private Categoria categoria;
    private List<Contacto> participantes;

    public Evento() {
        this.participantes = new ArrayList<>();
    }

    public Evento(Long id, String titulo, String descripcion, String fecha, String hora) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.participantes = new ArrayList<>();
    }

    // Constructor completo con relaciones
    public Evento(Long id, String titulo, String descripcion, String fecha, String hora, Categoria categoria) {
        this.id = id;
        this.titulo = titulo;
        this.descripcion = descripcion;
        this.fecha = fecha;
        this.hora = hora;
        this.categoria = categoria;
        this.participantes = new ArrayList<>();
    }

    // Getters y setters existentes
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

    // Nuevos getters y setters para relaciones
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
        this.participantes.add(contacto);
    }

    public void removerParticipante(Contacto contacto) {
        if (this.participantes != null) {
            this.participantes.remove(contacto);
        }
    }

    public boolean tieneParticipante(Contacto contacto) {
        return this.participantes != null && this.participantes.contains(contacto);
    }
}
