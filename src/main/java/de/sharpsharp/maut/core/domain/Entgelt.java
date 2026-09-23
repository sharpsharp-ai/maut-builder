package de.sharpsharp.maut.core.domain;

import java.util.Objects;

/** Ein einzelner Betrag, den ein Kunde schuldet und der in einem Rechnungslauf abgerechnet wird. */
public class Entgelt {

    private final String kundennummer;
    private final String bezeichnung;
    private final Betrag betrag;
    private volatile boolean abgerechnet;

    public Entgelt(String kundennummer, String bezeichnung, Betrag betrag) {
        this.kundennummer = Objects.requireNonNull(kundennummer, "kundennummer");
        this.bezeichnung = Objects.requireNonNull(bezeichnung, "bezeichnung");
        this.betrag = Objects.requireNonNull(betrag, "betrag");
    }

    public Position alsPosition() {
        return new Position(bezeichnung, betrag);
    }

    public void abrechnen() {
        if (abgerechnet) {
            throw new IllegalStateException("Entgelt wurde bereits abgerechnet");
        }
        abgerechnet = true;
    }

    public boolean istAbgerechnet() {
        return abgerechnet;
    }

    public String kundennummer() {
        return kundennummer;
    }
}
