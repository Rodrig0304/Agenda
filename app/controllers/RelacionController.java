package controllers;

import play.mvc.*;
import play.libs.Json;
import models.Categoria;
import models.Contacto;
import models.Evento;
import services.AgendaService;

import java.util.List;

public class RelacionController extends Controller {

    // ========== MÉTODOS PARA RELACIONES DE CATEGORÍAS ==========
    
    public Result obtenerEstadisticasCategoria(Long categoriaId) {
        Categoria categoria = AgendaService.obtenerCategoriaPorId(categoriaId);
        if (categoria != null) {
            return ok(Json.toJson(categoria));
        }
        return notFound(Json.toJson("Categoría no encontrada"));
    }

    public Result obtenerContactosDeCategoria(Long categoriaId) {
        List<Contacto> contactos = AgendaService.obtenerContactosPorCategoria(categoriaId);
        return ok(Json.toJson(contactos));
    }

    public Result obtenerEventosDeCategoria(Long categoriaId) {
        List<Evento> eventos = AgendaService.obtenerEventosPorCategoria(categoriaId);
        return ok(Json.toJson(eventos));
    }

    // ========== MÉTODOS PARA RELACIONES DE EVENTOS ==========
    
    public Result obtenerParticipantesDeEvento(Long eventoId) {
        List<Contacto> participantes = AgendaService.obtenerParticipantesDelEvento(eventoId);
        return ok(Json.toJson(participantes));
    }

    public Result agregarParticipanteAEvento(Long eventoId, Long contactoId) {
        boolean resultado = AgendaService.agregarParticipanteAEvento(eventoId, contactoId);
        if (resultado) {
            return ok(Json.toJson("Participante agregado exitosamente"));
        } else {
            return badRequest(Json.toJson("Error al agregar participante"));
        }
    }

    public Result removerParticipanteDeEvento(Long eventoId, Long contactoId) {
        boolean resultado = AgendaService.removerParticipanteDeEvento(eventoId, contactoId);
        if (resultado) {
            return ok(Json.toJson("Participante removido exitosamente"));
        } else {
            return badRequest(Json.toJson("Error al remover participante"));
        }
    }

    // ========== MÉTODOS DE CONSULTA GENERAL ==========
    
    public Result obtenerResumenAgenda() {
        List<Categoria> categorias = AgendaService.obtenerCategoriasConEstadisticas();
        List<Evento> eventosConParticipantes = AgendaService.obtenerEventosConParticipantes();
        List<Contacto> contactosSinCategoria = AgendaService.obtenerContactosSinCategoria();
        
        ResumenAgenda resumen = new ResumenAgenda();
        resumen.categorias = categorias;
        resumen.eventosConParticipantes = eventosConParticipantes;
        resumen.contactosSinCategoria = contactosSinCategoria;
        
        return ok(Json.toJson(resumen));
    }

    public Result obtenerEventosPorFecha(String fecha) {
        List<Evento> todosLosEventos = AgendaService.obtenerTodosLosEventos();
        List<Evento> eventosDeFecha = todosLosEventos.stream()
                .filter(e -> fecha.equals(e.getFecha()))
                .collect(java.util.stream.Collectors.toList());
        return ok(Json.toJson(eventosDeFecha));
    }

    public Result buscarContactosPorNombre(String nombre) {
        List<Contacto> todosLosContactos = AgendaService.obtenerTodosLosContactos();
        List<Contacto> contactosEncontrados = todosLosContactos.stream()
                .filter(c -> c.getNombre().toLowerCase().contains(nombre.toLowerCase()))
                .collect(java.util.stream.Collectors.toList());
        return ok(Json.toJson(contactosEncontrados));
    }

    // Clase interna para el resumen de agenda
    public static class ResumenAgenda {
        public List<Categoria> categorias;
        public List<Evento> eventosConParticipantes;
        public List<Contacto> contactosSinCategoria;
    }
}
