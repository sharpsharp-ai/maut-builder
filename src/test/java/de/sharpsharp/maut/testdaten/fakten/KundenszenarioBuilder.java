package de.sharpsharp.maut.testdaten.fakten;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.core.domain.Betrag;
import de.sharpsharp.maut.core.domain.Material;
import de.sharpsharp.maut.core.domain.RechnungslaufStatus;
import de.sharpsharp.maut.testdaten.Ausgangslage;

import java.util.ArrayList;
import java.util.List;

/**
 * Variante 1: Der Builder beschreibt nur Fakten (Kunde, OBU, Schäden).
 * Die Rechnung berechnet der echte Produktionscode über die Use Cases –
 * nur ohne GUI und ohne Warten. Deshalb kann sie nicht wegdriften.
 */
public class KundenszenarioBuilder {

    private final String name = "Spedition Muster GmbH";
    private final String kennzeichen = "M-SH 1234";
    private final List<Material> abgerechneteSchaeden = new ArrayList<>();
    private boolean obuDefekt;

    public static KundenszenarioBuilder einGanzNormalerKunde() {
        return new KundenszenarioBuilder();
    }

    public KundenszenarioBuilder mitDefekterObu() {
        obuDefekt = true;
        return this;
    }

    public KundenszenarioBuilder mitAbgerechnetemSchaden(Betrag betrag) {
        obuDefekt = true;
        abgerechneteSchaeden.add(new Material("Ersatz-OBU", betrag));
        return this;
    }

    /** Erwartet eine Anwendung mit sofortiger Hintergrundverarbeitung. */
    public Ausgangslage build(MautAnwendung anwendung) {
        var kunden = anwendung.kundenVerwaltung();
        var kundennummer = kunden.kundeAnlegen(name);
        kunden.obuInstallieren(kundennummer, kennzeichen);
        if (obuDefekt) {
            kunden.obuDefektMelden(kundennummer);
        }
        abgerechneteSchaeden.forEach(material -> kunden.schadenErfassen(kundennummer, material));
        var rechnungsnummer = abgerechneteSchaeden.isEmpty() ? null : abrechnen(anwendung, kundennummer);
        return new Ausgangslage(kundennummer, rechnungsnummer);
    }

    private String abrechnen(MautAnwendung anwendung, String kundennummer) {
        var laeufe = anwendung.rechnungslaeufe();
        var lauf = laeufe.neuerLauf();
        laeufe.starten(lauf.id());
        laeufe.freigeben(lauf.id());
        if (lauf.status() != RechnungslaufStatus.GEBUCHT) {
            throw new IllegalStateException("Builder braucht MautAnwendung.mitSofortigerVerarbeitung()");
        }
        return anwendung.abrechnung().rechnungenVon(kundennummer).get(0).nummer();
    }
}
