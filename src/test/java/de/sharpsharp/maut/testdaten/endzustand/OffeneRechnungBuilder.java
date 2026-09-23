package de.sharpsharp.maut.testdaten.endzustand;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.core.domain.Betrag;
import de.sharpsharp.maut.core.domain.Kunde;
import de.sharpsharp.maut.core.domain.Obu;
import de.sharpsharp.maut.core.domain.ObuStatus;
import de.sharpsharp.maut.core.domain.Position;
import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.domain.Rechnungsart;
import de.sharpsharp.maut.core.domain.Rechnungsstatus;
import de.sharpsharp.maut.testdaten.Ausgangslage;

import java.util.ArrayList;
import java.util.List;

/**
 * Variante 3: Der Builder legt den Endzustand direkt im Speicher ab –
 * eine gebuchte, offene Rechnung, ohne dass je ein Rechnungslauf lief.
 * Schnell und unabhängig vom Workflow, aber er kann wegdriften, wenn sich
 * der echte Rechnungslauf ändert. Abgesichert durch RechnungBuilderParitaetTest.
 */
public class OffeneRechnungBuilder {

    private final String name = "Spedition Muster GmbH";
    private final String kennzeichen = "M-SH 1234";
    private final List<Position> positionen = new ArrayList<>();
    private ObuStatus obuStatus = ObuStatus.INSTALLIERT;

    public static OffeneRechnungBuilder eineOffeneRechnung() {
        return new OffeneRechnungBuilder();
    }

    public OffeneRechnungBuilder fuerEineDefekteObu() {
        obuStatus = ObuStatus.DEFEKT;
        return this;
    }

    /** Muss nachbilden, wie der echte Rechnungslauf eine Schadensposition benennt. */
    public OffeneRechnungBuilder mitSchaden(Betrag betrag) {
        positionen.add(new Position("Ersatz-OBU", betrag));
        return this;
    }

    public Ausgangslage build(MautAnwendung anwendung) {
        var kunde = kundeAblegen(anwendung);
        var rechnungen = anwendung.rechnungRepository();
        var rechnung = new Rechnung(rechnungen.naechsteRechnungsnummer(), kunde.kundennummer(),
                Rechnungsart.RECHNUNG, positionen, Rechnungsstatus.OFFEN, null);
        rechnungen.speichere(rechnung);
        return new Ausgangslage(kunde.kundennummer(), rechnung.nummer());
    }

    private Kunde kundeAblegen(MautAnwendung anwendung) {
        var kunden = anwendung.kundenRepository();
        var kunde = new Kunde(kunden.naechsteKundennummer(), name, new Obu(kennzeichen, obuStatus));
        kunden.speichere(kunde);
        return kunde;
    }
}
