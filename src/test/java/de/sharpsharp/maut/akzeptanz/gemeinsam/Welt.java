package de.sharpsharp.maut.akzeptanz.gemeinsam;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.core.domain.Reklamationsergebnis;
import de.sharpsharp.maut.testdaten.Ausgangslage;

/**
 * Zustand eines Szenarios, den sich alle Glue-Klassen teilen.
 * PicoContainer erzeugt pro Szenario eine neue Welt – mit eigener, schneller Anwendung.
 */
public class Welt {

    private final MautAnwendung anwendung = MautAnwendung.mitSofortigerVerarbeitung();
    private Ausgangslage ausgangslage;

    public MautAnwendung anwendung() {
        return anwendung;
    }

    public void merke(Ausgangslage neueAusgangslage) {
        ausgangslage = neueAusgangslage;
    }

    public Ausgangslage ausgangslage() {
        if (ausgangslage == null) {
            throw new IllegalStateException("Es wurde noch keine Ausgangslage gebaut");
        }
        return ausgangslage;
    }

    /** Fachlich: Korrespondenz geht ein, Sachbearbeiter:in entscheidet auf Vollstorno. */
    public void vollstaendigReklamieren() {
        var abrechnung = anwendung.abrechnung();
        var korrespondenz = abrechnung.korrespondenzErfassen(
                ausgangslage().kundennummer(), ausgangslage().rechnungsnummer());
        abrechnung.reklamationEntscheiden(korrespondenz.id(), Reklamationsergebnis.VOLLSTORNO);
    }
}
