package de.sharpsharp.maut.core.domain;

import java.util.Objects;

/** Mautkunde mit (vereinfacht) genau einem Fahrzeug und dessen OBU. */
public class Kunde {

    private final String kundennummer;
    private final String name;
    private volatile Obu obu;

    public Kunde(String kundennummer, String name, Obu obu) {
        this.kundennummer = Objects.requireNonNull(kundennummer, "kundennummer");
        this.name = Objects.requireNonNull(name, "name");
        this.obu = obu;
    }

    public static Kunde neu(String kundennummer, String name) {
        return new Kunde(kundennummer, name, null);
    }

    public void obuInstallieren(String kennzeichen) {
        if (obu != null) {
            throw new IllegalStateException("Kunde " + kundennummer + " hat bereits eine OBU");
        }
        obu = Obu.installiertIn(kennzeichen);
    }

    public void obuDefektMelden() {
        obu().alsDefektMelden();
    }

    /** Ein Schaden an der OBU wird dem Kunden als Entgelt berechnet. */
    public Entgelt schadenBerechnen(Material material) {
        if (!obu().istDefekt()) {
            throw new IllegalStateException("Schäden können nur für eine defekte OBU berechnet werden");
        }
        return new Entgelt(kundennummer, material.bezeichnung(), material.preis());
    }

    public Obu obu() {
        if (obu == null) {
            throw new IllegalStateException("Kunde " + kundennummer + " hat keine OBU");
        }
        return obu;
    }

    public boolean hatObu() {
        return obu != null;
    }

    public String kundennummer() {
        return kundennummer;
    }

    public String name() {
        return name;
    }
}
