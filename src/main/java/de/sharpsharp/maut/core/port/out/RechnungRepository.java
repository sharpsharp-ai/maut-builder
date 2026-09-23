package de.sharpsharp.maut.core.port.out;

import de.sharpsharp.maut.core.domain.Rechnung;

import java.util.List;
import java.util.Optional;

public interface RechnungRepository {

    String naechsteRechnungsnummer();

    void speichere(Rechnung rechnung);

    Optional<Rechnung> finde(String rechnungsnummer);

    List<Rechnung> vonKunde(String kundennummer);
}
