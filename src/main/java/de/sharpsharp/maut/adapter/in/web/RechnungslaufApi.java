package de.sharpsharp.maut.adapter.in.web;

import de.sharpsharp.maut.core.port.in.Rechnungslaeufe;
import io.javalin.Javalin;
import io.javalin.http.Context;

/** REST-Schnittstelle der Backend-GUI für Rechnungsläufe. */
class RechnungslaufApi {

    private final Rechnungslaeufe laeufe;

    RechnungslaufApi(Rechnungslaeufe laeufe) {
        this.laeufe = laeufe;
    }

    void registriere(Javalin app) {
        app.post("/api/rechnungslaeufe", this::neuerLauf);
        app.get("/api/rechnungslaeufe/{id}", this::laufAnzeigen);
        app.post("/api/rechnungslaeufe/{id}/start", this::starten);
        app.post("/api/rechnungslaeufe/{id}/freigabe", this::freigeben);
    }

    private void neuerLauf(Context ctx) {
        ctx.status(201).json(Antworten.LaufAntwort.von(laeufe.neuerLauf()));
    }

    private void laufAnzeigen(Context ctx) {
        ctx.json(Antworten.LaufAntwort.von(laeufe.lauf(ctx.pathParam("id"))));
    }

    private void starten(Context ctx) {
        laeufe.starten(ctx.pathParam("id"));
        laufAnzeigen(ctx);
    }

    private void freigeben(Context ctx) {
        laeufe.freigeben(ctx.pathParam("id"));
        laufAnzeigen(ctx);
    }
}
