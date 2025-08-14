package models;

public class Contacto {
    private Long id;
    private String nombre;
    private String email;
    
    // Nueva relación
    private Categoria categoria;

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
}
