package models;

import io.ebean.Model;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "eventos")
public class Evento extends Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 200)
    private String titulo;
    
    @Column(length = 1000)
    private String descripcion;
    
    @Column(nullable = false, length = 10)
    private String fecha;
    
    @Column(length = 5)
    private String hora;
    
    // Relación con Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    
    // Relación muchos a muchos con Contacto (participantes)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "evento_participantes",
        joinColumns = @JoinColumn(name = "evento_id"),
        inverseJoinColumns = @JoinColumn(name = "contacto_id")
    )
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
        if (!this.participantes.contains(contacto)) {
            this.participantes.add(contacto);
        }
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
