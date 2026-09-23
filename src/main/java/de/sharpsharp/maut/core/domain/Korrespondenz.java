package de.sharpsharp.maut.core.domain;

import java.util.Objects;

/** Schreiben eines Kunden zu einer Rechnung; landet in der Tasklist ABRECHNUNGEN. */
public class Korrespondenz {

    public static final String TASKLIST_ABRECHNUNGEN = "ABRECHNUNGEN";

    private final String id;
    private final String kundennummer;
    private final String rechnungsnummer;
    private volatile KorrespondenzStatus status = KorrespondenzStatus.EINGEGANGEN;
    private volatile Reklamationsergebnis ergebnis;

    public Korrespondenz(String id, String kundennummer, String rechnungsnummer) {
        this.id = Objects.requireNonNull(id, "id");
        this.kundennummer = Objects.requireNonNull(kundennummer, "kundennummer");
        this.rechnungsnummer = Objects.requireNonNull(rechnungsnummer, "rechnungsnummer");
    }

    public void reklamationEntscheiden(Reklamationsergebnis entscheidung) {
        if (status != KorrespondenzStatus.EINGEGANGEN) {
            throw new IllegalStateException("Über Korrespondenz " + id + " wurde bereits entschieden");
        }
        ergebnis = Objects.requireNonNull(entscheidung, "entscheidung");
        status = KorrespondenzStatus.IN_BEARBEITUNG;
    }

    public void erledigt() {
        status = KorrespondenzStatus.ERLEDIGT;
    }

    public String id() { return id; }
    public String kundennummer() { return kundennummer; }
    public String rechnungsnummer() { return rechnungsnummer; }
    public KorrespondenzStatus status() { return status; }
    public Reklamationsergebnis ergebnis() { return ergebnis; }
}
