package de.sharpsharp.maut.core.domain;

import java.util.List;
import java.util.Objects;

/**
 * Sammelt alle noch nicht abgerechneten Entgelte zu Rechnungen.
 * Ablauf: ANGELEGT → LAEUFT → BUCHUNGSBEREIT → (Freigabe) → WIRD_GEBUCHT → GEBUCHT
 */
public class Rechnungslauf {

    private final String id;
    private volatile RechnungslaufStatus status = RechnungslaufStatus.ANGELEGT;
    private volatile List<String> rechnungsnummern = List.of();

    public Rechnungslauf(String id) {
        this.id = Objects.requireNonNull(id, "id");
    }

    public void starten() {
        wechsleStatus(RechnungslaufStatus.ANGELEGT, RechnungslaufStatus.LAEUFT);
    }

    public void entwuerfeErstellt(List<String> nummern) {
        wechsleStatus(RechnungslaufStatus.LAEUFT, RechnungslaufStatus.BUCHUNGSBEREIT);
        rechnungsnummern = List.copyOf(nummern);
    }

    public void freigeben() {
        wechsleStatus(RechnungslaufStatus.BUCHUNGSBEREIT, RechnungslaufStatus.WIRD_GEBUCHT);
    }

    public void gebucht() {
        wechsleStatus(RechnungslaufStatus.WIRD_GEBUCHT, RechnungslaufStatus.GEBUCHT);
    }

    private void wechsleStatus(RechnungslaufStatus von, RechnungslaufStatus nach) {
        if (status != von) {
            throw new IllegalStateException("Rechnungslauf " + id + " ist " + status + ", erwartet war " + von);
        }
        status = nach;
    }

    public String id() { return id; }
    public RechnungslaufStatus status() { return status; }
    public List<String> rechnungsnummern() { return rechnungsnummern; }
}
