package de.sharpsharp.maut.adapter.in.web;

import de.sharpsharp.maut.core.port.in.Abrechnung;
import io.javalin.Javalin;
import io.javalin.http.Context;

/** REST-Schnittstelle der KM-GUI: Tasklist, Reklamationen, Rechnungen. */
class AbrechnungApi {

    private final Abrechnung abrechnung;

    AbrechnungApi(Abrechnung abrechnung) {
        this.abrechnung = abrechnung;
    }

    void registriere(Javalin app) {
        app.post("/api/korrespondenzen", this::korrespondenzErfassen);
        app.get("/api/tasklists/abrechnungen", this::tasklistAnzeigen);
        app.get("/api/korrespondenzen/{id}", this::korrespondenzAnzeigen);
        app.post("/api/korrespondenzen/{id}/reklamation", this::reklamationEntscheiden);
        app.get("/api/kunden/{nr}/rechnungen", this::rechnungenAnzeigen);
    }

    private void korrespondenzErfassen(Context ctx) {
        var anfrage = ctx.bodyAsClass(Anfragen.NeueKorrespondenz.class);
        var korrespondenz = abrechnung.korrespondenzErfassen(anfrage.kundennummer(), anfrage.rechnungsnummer());
        ctx.status(201).json(Antworten.KorrespondenzAntwort.von(korrespondenz));
    }

    private void tasklistAnzeigen(Context ctx) {
        ctx.json(abrechnung.tasklistAbrechnungen().stream().map(Antworten.KorrespondenzAntwort::von).toList());
    }

    private void korrespondenzAnzeigen(Context ctx) {
        ctx.json(Antworten.KorrespondenzAntwort.von(abrechnung.korrespondenz(ctx.pathParam("id"))));
    }

    private void reklamationEntscheiden(Context ctx) {
        var anfrage = ctx.bodyAsClass(Anfragen.Reklamation.class);
        abrechnung.reklamationEntscheiden(ctx.pathParam("id"), anfrage.ergebnis());
        korrespondenzAnzeigen(ctx);
    }

    private void rechnungenAnzeigen(Context ctx) {
        ctx.json(abrechnung.rechnungenVon(ctx.pathParam("nr")).stream().map(Antworten.RechnungAntwort::von).toList());
    }
}
