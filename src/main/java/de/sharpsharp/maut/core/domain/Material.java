package de.sharpsharp.maut.core.domain;

import java.util.Objects;

/** Bei einem OBU-Schaden verbrauchtes Material, das dem Kunden berechnet wird. */
public record Material(String bezeichnung, Betrag preis) {

    public Material {
        Objects.requireNonNull(bezeichnung, "bezeichnung");
        if (!preis.istPositiv()) {
            throw new IllegalArgumentException("Materialpreis muss positiv sein: " + preis);
        }
    }
}
