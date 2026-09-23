package de.sharpsharp.maut.core.port.out;

import de.sharpsharp.maut.core.domain.Rechnungslauf;

import java.util.Optional;

public interface RechnungslaufRepository {

    String naechsteId();

    void speichere(Rechnungslauf lauf);

    Optional<Rechnungslauf> finde(String id);
}
