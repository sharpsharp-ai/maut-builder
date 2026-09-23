package de.sharpsharp.maut.adapter.in.web;

import de.sharpsharp.maut.core.domain.Reklamationsergebnis;

import java.math.BigDecimal;

/** JSON-Eingaben der Web-Oberflächen. */
final class Anfragen {

    private Anfragen() {
    }

    record NeuerKunde(String name) { }

    record ObuInstallation(String kennzeichen) { }

    record Schaden(String material, BigDecimal preis) { }

    record NeueKorrespondenz(String kundennummer, String rechnungsnummer) { }

    record Reklamation(Reklamationsergebnis ergebnis) { }
}
