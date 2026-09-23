package de.sharpsharp.maut.akzeptanz;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.core.domain.Betrag;
import de.sharpsharp.maut.core.domain.Kunde;
import de.sharpsharp.maut.core.domain.Material;

/**
 * Baut einen Kunden im gewünschten Zustand – ohne GUI und ohne Warten.
 * Die mit-Methoden sagen nur, WAS gelten soll. build() erledigt das WIE,
 * über dieselben Use Cases, die auch die Oberfläche benutzt.
 */
public class KundeBuilder {

    private String name = "Spedition Muster GmbH";
    private String kennzeichen = "M-SH 1234";
    private boolean obuDefekt = false;
    private Betrag rechnungsbetrag = null;

    public static KundeBuilder einGanzNormalerKunde() {
        return new KundeBuilder();
    }

    public KundeBuilder mitDefekterObu() {
        obuDefekt = true;
        return this;
    }

    /** Der Schaden an der defekten OBU wurde bereits abgerechnet. */
    public KundeBuilder mitRechnungUeber(Betrag betrag) {
        rechnungsbetrag = betrag;
        return this;
    }

    public Kunde build(MautAnwendung anwendung) {
        var kunden = anwendung.kundenVerwaltung();
        var kundennummer = kunden.kundeAnlegen(name);
        kunden.obuInstallieren(kundennummer, kennzeichen);
        if (obuDefekt) {
            kunden.obuDefektMelden(kundennummer);
        }
        if (rechnungsbetrag != null) {
            kunden.schadenErfassen(kundennummer, new Material("Ersatz-OBU", rechnungsbetrag));
            rechnungslaufDurchfuehren(anwendung);
        }
        return kunden.kunde(kundennummer);
    }

    private void rechnungslaufDurchfuehren(MautAnwendung anwendung) {
        var laeufe = anwendung.rechnungslaeufe();
        var lauf = laeufe.neuerLauf();
        laeufe.starten(lauf.id());
        laeufe.freigeben(lauf.id());
    }
}
