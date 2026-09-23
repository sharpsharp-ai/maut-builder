package de.sharpsharp.maut.core.domain;

import java.util.List;
import java.util.Objects;

/** Rechnung an einen Kunden. Status-Übergänge sind nur über die fachlichen Methoden möglich. */
public class Rechnung {

    private final String nummer;
    private final String kundennummer;
    private final Rechnungsart art;
    private final List<Position> positionen;
    private final String bezugsnummer;
    private volatile Rechnungsstatus status;

    public Rechnung(String nummer, String kundennummer, Rechnungsart art,
                    List<Position> positionen, Rechnungsstatus status, String bezugsnummer) {
        this.nummer = Objects.requireNonNull(nummer, "nummer");
        this.kundennummer = Objects.requireNonNull(kundennummer, "kundennummer");
        this.art = Objects.requireNonNull(art, "art");
        this.positionen = List.copyOf(positionen);
        this.status = Objects.requireNonNull(status, "status");
        this.bezugsnummer = bezugsnummer;
        if (this.positionen.isEmpty()) {
            throw new IllegalArgumentException("Eine Rechnung braucht mindestens eine Position");
        }
    }

    public static Rechnung entwurf(String nummer, String kundennummer, List<Position> positionen) {
        return new Rechnung(nummer, kundennummer, Rechnungsart.RECHNUNG, positionen, Rechnungsstatus.ENTWURF, null);
    }

    public void buchen() {
        wechsleStatus(Rechnungsstatus.ENTWURF, Rechnungsstatus.OFFEN);
    }

    public void stornieren() {
        wechsleStatus(Rechnungsstatus.OFFEN, Rechnungsstatus.STORNIERT);
    }

    /** Erzeugt eine offene Sofortrechnung, die dem Kunden die Summe dieser Rechnung gutschreibt. */
    public Rechnung sofortrechnungMitGuthaben(String neueNummer) {
        var guthaben = new Position("Guthaben aus Reklamation " + nummer, summe().negiert());
        return new Rechnung(neueNummer, kundennummer, Rechnungsart.SOFORTRECHNUNG,
                List.of(guthaben), Rechnungsstatus.OFFEN, nummer);
    }

    public Betrag summe() {
        return positionen.stream().map(Position::betrag).reduce(Betrag.NULL, Betrag::plus);
    }

    private void wechsleStatus(Rechnungsstatus von, Rechnungsstatus nach) {
        if (status != von) {
            throw new IllegalStateException("Rechnung " + nummer + " ist " + status + ", erwartet war " + von);
        }
        status = nach;
    }

    public String nummer() { return nummer; }
    public String kundennummer() { return kundennummer; }
    public Rechnungsart art() { return art; }
    public List<Position> positionen() { return positionen; }
    public Rechnungsstatus status() { return status; }
    public String bezugsnummer() { return bezugsnummer; }
}
