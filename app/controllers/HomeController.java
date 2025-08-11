package controllers;

import play.mvc.*;

public class HomeController extends Controller {
    public Result index() {
        return ok("Bienvenido a la Agenda de Contactos");
    }
}
