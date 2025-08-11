package controllers;

import play.mvc.*;
import play.libs.Json;
import models.Categoria;

import java.util.ArrayList;
import java.util.List;

public class CategoriaController extends Controller {

    private static List<Categoria> categorias = new ArrayList<>();
    private static Long contadorId = 1L;

    public Result listarCategorias() {
        return ok(Json.toJson(categorias));
    }

    public Result crearCategoria(Http.Request request) {
        Categoria categoria = Json.fromJson(request.body().asJson(), Categoria.class);
        categoria.setId(contadorId++);
        categorias.add(categoria);
        return created(Json.toJson(categoria));
    }
}
