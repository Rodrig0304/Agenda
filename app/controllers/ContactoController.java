package controllers;

import play.mvc.*;
import com.fasterxml.jackson.databind.JsonNode;
import play.libs.Json;
import models.Contacto;

import java.util.*;

public class ContactoController extends Controller {

    private static List<Contacto> contactos = new ArrayList<>();
    private static Long contadorId = 1L;

    // Devuelve los contactos en formato JSON
    public Result listarContactos() {
        return ok(Json.toJson(contactos));
    }

    // Crea un contacto desde JSON
    public Result crearContacto(Http.Request request) {
        JsonNode json = request.body().asJson();
        if (json == null || json.get("nombre") == null || json.get("email") == null) {
            return badRequest("Faltan datos");
        }

        Contacto nuevo = new Contacto(contadorId++, json.get("nombre").asText(), json.get("email").asText());
        contactos.add(nuevo);
        return created(Json.toJson(nuevo));
    }

    // Muestra la vista HTML con la lista de contactos y el formulario
    public Result vistaContactos() {
        return ok(views.html.contactos.render(contactos));
    }

    // Crea un contacto desde el formulario HTML
    public Result crearDesdeFormulario(Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null) {
            String nombre = formData.get("nombre")[0];
            String email = formData.get("email")[0];
            contactos.add(new Contacto(contadorId++, nombre, email));
        }
        return redirect("/vista/contactos");
    }
}
