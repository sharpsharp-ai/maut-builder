package de.sharpsharp.maut.core.domain;

import java.util.Objects;

/** Eine Zeile auf einer Rechnung. */
public record Position(String bezeichnung, Betrag betrag) {

    public Position {
        Objects.requireNonNull(bezeichnung, "bezeichnung");
        Objects.requireNonNull(betrag, "betrag");
    }
}
