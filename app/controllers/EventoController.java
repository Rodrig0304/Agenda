package controllers;

import play.mvc.*;
import play.libs.Json;
import models.Evento;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventoController extends Controller {

    private static List<Evento> eventos = new ArrayList<>();
    private static Long contadorId = 1L;

    // Devuelve los eventos en formato JSON
    public Result listarEventos() {
        return ok(Json.toJson(eventos));
    }

    // Crea un evento desde JSON
    public Result crearEvento(Http.Request request) {
        Evento evento = Json.fromJson(request.body().asJson(), Evento.class);
        evento.setId(contadorId++);
        eventos.add(evento);
        return created(Json.toJson(evento));
    }

    // Muestra la vista HTML con la lista de eventos y el formulario
    public Result vistaEventos() {
        return ok(views.html.eventos.render(eventos));
    }

    // Crea un evento desde el formulario HTML
    public Result crearDesdeFormulario(Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("titulo") != null && formData.get("descripcion") != null && 
            formData.get("fecha") != null && formData.get("hora") != null) {
            String titulo = formData.get("titulo")[0];
            String descripcion = formData.get("descripcion")[0];
            String fecha = formData.get("fecha")[0];
            String hora = formData.get("hora")[0];
            if (titulo != null && !titulo.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty() &&
                fecha != null && !fecha.trim().isEmpty() && hora != null && !hora.trim().isEmpty()) {
                eventos.add(new Evento(contadorId++, titulo, descripcion, fecha, hora));
            }
        }
        return redirect("/vista/eventos");
    }
}
