package de.sharpsharp.maut.testdaten;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.core.domain.ObuStatus;
import de.sharpsharp.maut.core.domain.Position;
import de.sharpsharp.maut.core.domain.Rechnungsart;
import de.sharpsharp.maut.core.domain.Rechnungsstatus;

import java.util.List;

/**
 * Was an einer Ausgangslage fachlich zählt. Bewusst NICHT enthalten und damit
 * vom Vergleich ausgenommen: Kunden- und Rechnungsnummern (werden fortlaufend vergeben).
 */
public record FachlicheSicht(ObuStatus obuStatus, String kennzeichen, Rechnungsart art,
                             Rechnungsstatus status, List<Position> positionen) {

    public static FachlicheSicht von(MautAnwendung anwendung, Ausgangslage ausgangslage) {
        var obu = anwendung.kundenVerwaltung().kunde(ausgangslage.kundennummer()).obu();
        var rechnung = anwendung.abrechnung().rechnung(ausgangslage.rechnungsnummer());
        return new FachlicheSicht(obu.status(), obu.kennzeichen(), rechnung.art(),
                rechnung.status(), rechnung.positionen());
    }
}
