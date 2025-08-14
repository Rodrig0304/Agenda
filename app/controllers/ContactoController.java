package controllers;

import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import models.Contacto;
import models.Categoria;
import services.AgendaService;

import java.util.*;

public class ContactoController extends Controller {

    public Result listarContactos() {
        List<Contacto> contactos = AgendaService.obtenerTodosLosContactos();
        return ok(Json.toJson(contactos));
    }

    // Crea un contacto desde JSON
    public Result crearContacto(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null || json.get("nombre") == null || json.get("email") == null) {
            return badRequest("Faltan datos");
        }

        String nombre = json.get("nombre").asText();
        String email = json.get("email").asText();
        Long categoriaId = json.get("categoriaId") != null ? json.get("categoriaId").asLong() : null;

        Contacto nuevo = AgendaService.crearContacto(nombre, email, categoriaId);
        return created(Json.toJson(nuevo));
    }

    public Result vistaContactos() {
        List<Contacto> contactos = AgendaService.obtenerTodosLosContactos();
        List<Categoria> categorias = AgendaService.obtenerTodasLasCategorias();
        return ok(views.html.contactos.render(contactos, categorias));
    }

    public Result crearDesdeFormulario(Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null) {
            String nombre = formData.get("nombre")[0];
            String email = formData.get("email")[0];
            Long categoriaId = null;
            
            if (formData.get("categoriaId") != null && !formData.get("categoriaId")[0].isEmpty()) {
                try {
                    categoriaId = Long.parseLong(formData.get("categoriaId")[0]);
                } catch (NumberFormatException e) {
                    // Si no se puede parsear, se deja como null
                }
            }
            
            AgendaService.crearContacto(nombre, email, categoriaId);
        }
        return redirect("/vista/contactos");
    }

    // Manejar peticiones POST con ID (para borrar y actualizar)
    public Result manejarPeticionConId(Long id, Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("_method") != null) {
            String method = formData.get("_method")[0];
            if (method.equals("DELETE")) {
                // Es una petición de borrado
                AgendaService.eliminarContacto(id);
            } else if (method.equals("PUT")) {
                // Es una petición de actualización
                if (formData.get("nombre") != null && formData.get("email") != null) {
                    String nombre = formData.get("nombre")[0];
                    String email = formData.get("email")[0];
                    Long categoriaId = null;
                    
                    if (formData.get("categoriaId") != null && !formData.get("categoriaId")[0].isEmpty()) {
                        try {
                            categoriaId = Long.parseLong(formData.get("categoriaId")[0]);
                        } catch (NumberFormatException e) {
                            // Si no se puede parsear, se deja como null
                        }
                    }
                    
                    if (nombre != null && !nombre.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
                        AgendaService.actualizarContacto(id, nombre, email, categoriaId);
                    }
                }
            }
        }
        return redirect("/vista/contactos");
    }

    // Borrar contacto por ID
    public Result borrarContacto(Long id) {
        AgendaService.eliminarContacto(id);
        return redirect("/vista/contactos");
    }

    // Actualizar contacto por ID
    public Result actualizarContacto(Long id, Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("nombre") != null && formData.get("email") != null) {
            String nombre = formData.get("nombre")[0];
            String email = formData.get("email")[0];
            Long categoriaId = null;
            
            if (formData.get("categoriaId") != null && !formData.get("categoriaId")[0].isEmpty()) {
                try {
                    categoriaId = Long.parseLong(formData.get("categoriaId")[0]);
                } catch (NumberFormatException e) {
                    // Si no se puede parsear, se deja como null
                }
            }
            
            if (nombre != null && !nombre.trim().isEmpty() && email != null && !email.trim().isEmpty()) {
                AgendaService.actualizarContacto(id, nombre, email, categoriaId);
            }
        }
        return redirect("/vista/contactos");
    }

    // Nuevos métodos para relaciones
    public Result obtenerContactosPorCategoria(Long categoriaId) {
        List<Contacto> contactos = AgendaService.obtenerContactosPorCategoria(categoriaId);
        return ok(Json.toJson(contactos));
    }

    public Result obtenerContactosSinCategoria() {
        List<Contacto> contactos = AgendaService.obtenerContactosSinCategoria();
        return ok(Json.toJson(contactos));
    }
}
