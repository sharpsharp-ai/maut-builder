package de.sharpsharp.maut.core.port.out;

import de.sharpsharp.maut.core.domain.Kunde;

import java.util.Optional;

public interface KundenRepository {

    String naechsteKundennummer();

    void speichere(Kunde kunde);

    Optional<Kunde> finde(String kundennummer);
}
