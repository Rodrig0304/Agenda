package controllers;

import play.mvc.*;
import play.libs.Json;
import models.Categoria;
import services.DatabaseService;


import java.util.List;
import java.util.Map;

public class CategoriaController extends Controller {



    public Result listarCategorias() {
        List<Categoria> categorias = DatabaseService.obtenerTodasLasCategorias();
        return ok(Json.toJson(categorias));
    }

    public Result crearCategoria(Http.Request request) {
        Categoria categoria = Json.fromJson(request.body().asJson(), Categoria.class);
        Categoria nuevaCategoria = DatabaseService.crearCategoria(categoria.getNombre(), categoria.getDescripcion());
        return created(Json.toJson(nuevaCategoria));
    }

    public Result vistaCategorias() {
        List<Categoria> categorias = DatabaseService.obtenerTodasLasCategorias();
        return ok(views.html.categorias.render(categorias));
    }

    public Result crearDesdeFormulario(Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("nombre") != null && formData.get("descripcion") != null) {
            String nombre = formData.get("nombre")[0];
            String descripcion = formData.get("descripcion")[0];
            if (nombre != null && !nombre.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty()) {
                DatabaseService.crearCategoria(nombre, descripcion);
            }
        }
        return redirect("/vista/categorias");
    }

    // Manejar peticiones POST con ID (para borrar y actualizar)
    public Result manejarPeticionConId(Long id, Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("_method") != null) {
            String method = formData.get("_method")[0];
            if (method.equals("DELETE")) {
                // Es una petición de borrado
                DatabaseService.eliminarCategoria(id);
            } else if (method.equals("PUT")) {
                // Es una petición de actualización
                if (formData.get("nombre") != null && formData.get("descripcion") != null) {
                    String nombre = formData.get("nombre")[0];
                    String descripcion = formData.get("descripcion")[0];
                    if (nombre != null && !nombre.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty()) {
                        DatabaseService.actualizarCategoria(id, nombre, descripcion);
                    }
                }
            }
        }
        return redirect("/vista/categorias");
    }

    // Borrar categoría por ID
    public Result borrarCategoria(Long id) {
        DatabaseService.eliminarCategoria(id);
        return redirect("/vista/categorias");
    }

    // Actualizar categoría por ID
    public Result actualizarCategoria(Long id, Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("nombre") != null && formData.get("descripcion") != null) {
            String nombre = formData.get("nombre")[0];
            String descripcion = formData.get("descripcion")[0];
            if (nombre != null && !nombre.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty()) {
                DatabaseService.actualizarCategoria(id, nombre, descripcion);
            }
        }
        return redirect("/vista/categorias");
    }

    // Nuevos métodos para relaciones
    public Result obtenerCategoriasConEstadisticas() {
        List<Categoria> categorias = DatabaseService.obtenerCategoriasConEstadisticas();
        return ok(Json.toJson(categorias));
    }

    public Result obtenerContactosPorCategoria(Long categoriaId) {
        List<models.Contacto> contactos = DatabaseService.obtenerContactosPorCategoria(categoriaId);
        return ok(Json.toJson(contactos));
    }

    public Result obtenerEventosPorCategoria(Long categoriaId) {
        List<models.Evento> eventos = DatabaseService.obtenerEventosPorCategoria(categoriaId);
        return ok(Json.toJson(eventos));
    }
}
