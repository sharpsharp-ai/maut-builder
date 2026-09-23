package de.sharpsharp.maut.adapter.in.web;

import de.sharpsharp.maut.MautAnwendung;
import io.javalin.Javalin;
import io.javalin.http.HttpStatus;
import io.javalin.http.staticfiles.Location;

import java.util.NoSuchElementException;

/** Startet Javalin mit den drei Oberflächen (Kundenportal, Backend, KM-GUI) und ihren REST-Schnittstellen. */
public final class WebServer {

    private WebServer() {
    }

    /** @param port 0 wählt einen freien Port (für Tests) */
    public static Javalin starte(MautAnwendung anwendung, int port) {
        var app = Javalin.create(config -> config.staticFiles.add("/public", Location.CLASSPATH));
        new KundenApi(anwendung.kundenVerwaltung()).registriere(app);
        new RechnungslaufApi(anwendung.rechnungslaeufe()).registriere(app);
        new AbrechnungApi(anwendung.abrechnung()).registriere(app);
        fehlerbehandlung(app);
        return app.start(port);
    }

    private static void fehlerbehandlung(Javalin app) {
        app.exception(NoSuchElementException.class,
                (e, ctx) -> ctx.status(HttpStatus.NOT_FOUND).json(new Antworten.Fehler(e.getMessage())));
        app.exception(IllegalArgumentException.class,
                (e, ctx) -> ctx.status(HttpStatus.BAD_REQUEST).json(new Antworten.Fehler(e.getMessage())));
        app.exception(IllegalStateException.class,
                (e, ctx) -> ctx.status(HttpStatus.CONFLICT).json(new Antworten.Fehler(e.getMessage())));
    }
}
