package de.sharpsharp.maut.core.port.out;

import de.sharpsharp.maut.core.domain.Korrespondenz;

import java.util.List;
import java.util.Optional;

public interface KorrespondenzRepository {

    String naechsteId();

    void speichere(Korrespondenz korrespondenz);

    Optional<Korrespondenz> finde(String id);

    List<Korrespondenz> alle();
}
