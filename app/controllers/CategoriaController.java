package controllers;

import play.mvc.*;
import play.libs.Json;
import models.Categoria;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class CategoriaController extends Controller {

    private static List<Categoria> categorias = new ArrayList<>();
    private static Long contadorId = 1L;

    // Devuelve las categorías en formato JSON
    public Result listarCategorias() {
        return ok(Json.toJson(categorias));
    }

    // Crea una categoría desde JSON
    public Result crearCategoria(Http.Request request) {
        Categoria categoria = Json.fromJson(request.body().asJson(), Categoria.class);
        categoria.setId(contadorId++);
        categorias.add(categoria);
        return created(Json.toJson(categoria));
    }

    // Muestra la vista HTML con la lista de categorías y el formulario
    public Result vistaCategorias() {
        return ok(views.html.categorias.render(categorias));
    }

    // Crea una categoría desde el formulario HTML
    public Result crearDesdeFormulario(Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("nombre") != null && formData.get("descripcion") != null) {
            String nombre = formData.get("nombre")[0];
            String descripcion = formData.get("descripcion")[0];
            if (nombre != null && !nombre.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty()) {
                categorias.add(new Categoria(contadorId++, nombre, descripcion));
            }
        }
        return redirect("/vista/categorias");
    }
}
