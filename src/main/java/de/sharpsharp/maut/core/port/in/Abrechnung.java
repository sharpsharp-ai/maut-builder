package de.sharpsharp.maut.core.port.in;

import de.sharpsharp.maut.core.domain.Korrespondenz;
import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.domain.Reklamationsergebnis;

import java.util.List;

/** Kundenmanagement: Korrespondenz bearbeiten, Reklamationen entscheiden, Rechnungen einsehen. */
public interface Abrechnung {

    Korrespondenz korrespondenzErfassen(String kundennummer, String rechnungsnummer);

    List<Korrespondenz> tasklistAbrechnungen();

    Korrespondenz korrespondenz(String korrespondenzId);

    /** Die Umsetzung (z. B. Storno und Sofortrechnung) läuft im Hintergrund. */
    void reklamationEntscheiden(String korrespondenzId, Reklamationsergebnis ergebnis);

    List<Rechnung> rechnungenVon(String kundennummer);

    Rechnung rechnung(String rechnungsnummer);
}
