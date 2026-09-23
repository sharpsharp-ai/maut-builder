package de.sharpsharp.maut.adapter.in.web;

import de.sharpsharp.maut.core.domain.Betrag;
import de.sharpsharp.maut.core.domain.Material;
import de.sharpsharp.maut.core.port.in.KundenVerwaltung;
import io.javalin.Javalin;
import io.javalin.http.Context;

/** REST-Schnittstelle des Kundenportals. */
class KundenApi {

    private final KundenVerwaltung kunden;

    KundenApi(KundenVerwaltung kunden) {
        this.kunden = kunden;
    }

    void registriere(Javalin app) {
        app.post("/api/kunden", this::kundeAnlegen);
        app.get("/api/kunden/{nr}", this::kundeAnzeigen);
        app.post("/api/kunden/{nr}/obu", this::obuInstallieren);
        app.post("/api/kunden/{nr}/obu/defekt", this::obuDefektMelden);
        app.post("/api/kunden/{nr}/schaeden", this::schadenErfassen);
    }

    private void kundeAnlegen(Context ctx) {
        var anfrage = ctx.bodyAsClass(Anfragen.NeuerKunde.class);
        var kundennummer = kunden.kundeAnlegen(anfrage.name());
        ctx.status(201).json(Antworten.KundeAntwort.von(kunden.kunde(kundennummer)));
    }

    private void kundeAnzeigen(Context ctx) {
        ctx.json(Antworten.KundeAntwort.von(kunden.kunde(ctx.pathParam("nr"))));
    }

    private void obuInstallieren(Context ctx) {
        var anfrage = ctx.bodyAsClass(Anfragen.ObuInstallation.class);
        kunden.obuInstallieren(ctx.pathParam("nr"), anfrage.kennzeichen());
        kundeAnzeigen(ctx);
    }

    private void obuDefektMelden(Context ctx) {
        kunden.obuDefektMelden(ctx.pathParam("nr"));
        kundeAnzeigen(ctx);
    }

    private void schadenErfassen(Context ctx) {
        var anfrage = ctx.bodyAsClass(Anfragen.Schaden.class);
        kunden.schadenErfassen(ctx.pathParam("nr"), new Material(anfrage.material(), Betrag.euro(anfrage.preis())));
        ctx.status(201);
    }
}
