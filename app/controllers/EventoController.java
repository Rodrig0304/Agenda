package controllers;

import play.mvc.*;
import play.libs.Json;
import models.Evento;

import java.util.ArrayList;
import java.util.List;

public class EventoController extends Controller {

    private static List<Evento> eventos = new ArrayList<>();
    private static Long contadorId = 1L;

    public Result listarEventos() {
        return ok(Json.toJson(eventos));
    }

    public Result crearEvento(Http.Request request) {
        Evento evento = Json.fromJson(request.body().asJson(), Evento.class);
        evento.setId(contadorId++);
        eventos.add(evento);
        return created(Json.toJson(evento));
    }
    
}
