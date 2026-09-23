package de.sharpsharp.maut.core.port.in;

import de.sharpsharp.maut.core.domain.Rechnungslauf;

/** Backend: Rechnungsläufe anlegen, starten und freigeben. Starten und Freigeben laufen im Hintergrund. */
public interface Rechnungslaeufe {

    Rechnungslauf neuerLauf();

    void starten(String laufId);

    void freigeben(String laufId);

    Rechnungslauf lauf(String laufId);
}
