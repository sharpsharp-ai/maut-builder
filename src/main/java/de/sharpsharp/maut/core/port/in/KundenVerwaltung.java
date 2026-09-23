package de.sharpsharp.maut.core.port.in;

import de.sharpsharp.maut.core.domain.Kunde;
import de.sharpsharp.maut.core.domain.Material;

/** Kundenportal: Kunden, Fahrzeuge und OBUs verwalten. */
public interface KundenVerwaltung {

    String kundeAnlegen(String name);

    void obuInstallieren(String kundennummer, String kennzeichen);

    void obuDefektMelden(String kundennummer);

    void schadenErfassen(String kundennummer, Material material);

    Kunde kunde(String kundennummer);
}
