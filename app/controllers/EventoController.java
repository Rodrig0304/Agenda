package controllers;

import play.mvc.*;
import play.libs.Json;
import models.Evento;
import models.Categoria;
import models.Contacto;
import services.DatabaseService;


import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class EventoController extends Controller {



    public Result listarEventos() {
        List<Evento> eventos = DatabaseService.obtenerTodosLosEventos();
        return ok(Json.toJson(eventos));
    }

    public Result crearEvento(Http.Request request) {
        Evento evento = Json.fromJson(request.body().asJson(), Evento.class);
        Long categoriaId = null;
        
        // Extraer categoriaId del JSON si existe
        if (request.body().asJson().has("categoriaId") && !request.body().asJson().get("categoriaId").isNull()) {
            categoriaId = request.body().asJson().get("categoriaId").asLong();
        }
        
        Evento nuevoEvento = DatabaseService.crearEvento(evento.getTitulo(), evento.getDescripcion(), 
                                                     evento.getFecha(), evento.getHora(), categoriaId);
        return created(Json.toJson(nuevoEvento));
    }

    public Result vistaEventos() {
        List<Evento> eventos = DatabaseService.obtenerTodosLosEventos();
        List<Categoria> categorias = DatabaseService.obtenerTodasLasCategorias();
        List<Contacto> contactos = DatabaseService.obtenerTodosLosContactos();
        return ok(views.html.eventos.render(eventos, categorias, contactos));
    }

    public Result crearDesdeFormulario(Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("titulo") != null && formData.get("descripcion") != null && 
            formData.get("fecha") != null && formData.get("hora") != null) {
            String titulo = formData.get("titulo")[0];
            String descripcion = formData.get("descripcion")[0];
            String fecha = formData.get("fecha")[0];
            String hora = formData.get("hora")[0];
            Long categoriaId = null;
            
            if (formData.get("categoriaId") != null && !formData.get("categoriaId")[0].isEmpty()) {
                try {
                    categoriaId = Long.parseLong(formData.get("categoriaId")[0]);
                } catch (NumberFormatException e) {
                    // Si no se puede parsear, se deja como null
                }
            }
            
            if (titulo != null && !titulo.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty() &&
                fecha != null && !fecha.trim().isEmpty() && hora != null && !hora.trim().isEmpty()) {
                DatabaseService.crearEvento(titulo, descripcion, fecha, hora, categoriaId);
            }
        }
        return redirect("/vista/eventos");
    }

    // Manejar peticiones POST con ID (para borrar y actualizar)
    public Result manejarPeticionConId(Long id, Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("_method") != null) {
            String method = formData.get("_method")[0];
            if (method.equals("DELETE")) {
                // Es una petición de borrado
                DatabaseService.eliminarEvento(id);
            } else if (method.equals("PUT")) {
                // Es una petición de actualización
                if (formData.get("titulo") != null && formData.get("descripcion") != null && 
                    formData.get("fecha") != null && formData.get("hora") != null) {
                    String titulo = formData.get("titulo")[0];
                    String descripcion = formData.get("descripcion")[0];
                    String fecha = formData.get("fecha")[0];
                    String hora = formData.get("hora")[0];
                    Long categoriaId = null;
                    
                    if (formData.get("categoriaId") != null && !formData.get("categoriaId")[0].isEmpty()) {
                        try {
                            categoriaId = Long.parseLong(formData.get("categoriaId")[0]);
                        } catch (NumberFormatException e) {
                            // Si no se puede parsear, se deja como null
                        }
                    }
                    
                    if (titulo != null && !titulo.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty() &&
                        fecha != null && !fecha.trim().isEmpty() && hora != null && !hora.trim().isEmpty()) {
                        DatabaseService.actualizarEvento(id, titulo, descripcion, fecha, hora, categoriaId);
                    }
                }
            }
        }
        return redirect("/vista/eventos");
    }

    // Borrar evento por ID
    public Result borrarEvento(Long id) {
        DatabaseService.eliminarEvento(id);
        return redirect("/vista/eventos");
    }

    // Actualizar evento por ID
    public Result actualizarEvento(Long id, Http.Request request) {
        Map<String, String[]> formData = request.body().asFormUrlEncoded();
        if (formData != null && formData.get("titulo") != null && formData.get("descripcion") != null && 
            formData.get("fecha") != null && formData.get("hora") != null) {
            String titulo = formData.get("titulo")[0];
            String descripcion = formData.get("descripcion")[0];
            String fecha = formData.get("fecha")[0];
            String hora = formData.get("hora")[0];
            Long categoriaId = null;
            
            if (formData.get("categoriaId") != null && !formData.get("categoriaId")[0].isEmpty()) {
                try {
                    categoriaId = Long.parseLong(formData.get("categoriaId")[0]);
                } catch (NumberFormatException e) {
                    // Si no se puede parsear, se deja como null
                }
            }
            
            if (titulo != null && !titulo.trim().isEmpty() && descripcion != null && !descripcion.trim().isEmpty() &&
                fecha != null && !fecha.trim().isEmpty() && hora != null && !hora.trim().isEmpty()) {
                DatabaseService.actualizarEvento(id, titulo, descripcion, fecha, hora, categoriaId);
            }
        }
        return redirect("/vista/eventos");
    }

    // Nuevos métodos para relaciones
    public Result obtenerEventosPorCategoria(Long categoriaId) {
        List<Evento> eventos = DatabaseService.obtenerEventosPorCategoria(categoriaId);
        return ok(Json.toJson(eventos));
    }

    public Result obtenerEventosConParticipantes() {
        List<Evento> eventos = DatabaseService.obtenerEventosConParticipantes();
        return ok(Json.toJson(eventos));
    }

    public Result agregarParticipante(Long eventoId, Long contactoId) {
        boolean resultado = DatabaseService.agregarParticipanteAEvento(eventoId, contactoId);
        if (resultado) {
            return ok(Json.toJson("Participante agregado exitosamente"));
        } else {
            return badRequest(Json.toJson("Error al agregar participante"));
        }
    }

    public Result removerParticipante(Long eventoId, Long contactoId) {
        boolean resultado = DatabaseService.removerParticipanteDeEvento(eventoId, contactoId);
        if (resultado) {
            return ok(Json.toJson("Participante removido exitosamente"));
        } else {
            return badRequest(Json.toJson("Error al remover participante"));
        }
    }

    public Result obtenerParticipantesDelEvento(Long eventoId) {
        List<Contacto> participantes = DatabaseService.obtenerParticipantesDelEvento(eventoId);
        
        // Crear una lista de objetos simples para evitar referencias circulares
        List<Object> participantesSimples = new ArrayList<>();
        for (Contacto contacto : participantes) {
            participantesSimples.add(new Object() {
                public Long id = contacto.getId();
                public String nombre = contacto.getNombre();
                public String email = contacto.getEmail();
            });
        }
        
        return ok(Json.toJson(participantesSimples));
    }

    // Método de debug para probar participantes
    public Result debugParticipantes(Long eventoId) {
        try {
            Evento evento = DatabaseService.obtenerEventoPorId(eventoId).orElse(null);
            if (evento == null) {
                return badRequest(Json.toJson("Evento no encontrado"));
            }
            
            // Crear un objeto simple para evitar problemas de serialización
            return ok(Json.toJson(new Object() {
                public Long eventoId = evento.getId();
                public String titulo = evento.getTitulo();
                public int cantidadParticipantes = evento.getParticipantes() != null ? evento.getParticipantes().size() : 0;
                public String mensaje = "Debug exitoso";
            }));
            
        } catch (Exception e) {
            return internalServerError(Json.toJson("Error interno: " + e.getMessage()));
        }
    }

    // Método de prueba simple
    public Result testSimple() {
        return ok(Json.toJson("Test simple funcionando"));
    }

    // Clase interna para debug
    public static class DebugInfo {
        public Long eventoId;
        public String titulo;
        public int cantidadParticipantes;
        public List<Contacto> participantes;
    }
}
