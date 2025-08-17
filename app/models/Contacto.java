package models;

import io.ebean.Model;
import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "contactos")
public class Contacto extends Model {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false, length = 100)
    private String nombre;
    
    @Column(nullable = false, length = 150, unique = true)
    private String email;
    
    // Relación con Categoria
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "categoria_id")
    private Categoria categoria;
    
    // Relación muchos a muchos con Evento (como participante)
    @ManyToMany(mappedBy = "participantes", fetch = FetchType.LAZY)
    private List<Evento> eventosComoParticipante;

    public Contacto() {
    }

    public Contacto(Long id, String nombre, String email) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
    }

    // Constructor completo con categoría
    public Contacto(Long id, String nombre, String email, Categoria categoria) {
        this.id = id;
        this.nombre = nombre;
        this.email = email;
        this.categoria = categoria;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    // Nuevos getters y setters para relación
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
}
