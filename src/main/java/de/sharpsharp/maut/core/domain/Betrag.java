package de.sharpsharp.maut.core.domain;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

/** Geldbetrag in Euro, immer auf zwei Nachkommastellen normiert. */
public record Betrag(BigDecimal wert) {

    public static final Betrag NULL = euro("0.00");

    public Betrag {
        Objects.requireNonNull(wert, "wert");
        wert = wert.setScale(2, RoundingMode.HALF_UP);
    }

    public static Betrag euro(String wert) {
        return new Betrag(new BigDecimal(wert));
    }

    public static Betrag euro(BigDecimal wert) {
        return new Betrag(wert);
    }

    public Betrag plus(Betrag anderer) {
        return new Betrag(wert.add(anderer.wert));
    }

    public Betrag negiert() {
        return new Betrag(wert.negate());
    }

    public boolean istPositiv() {
        return wert.signum() > 0;
    }

    @Override
    public String toString() {
        return wert.toPlainString() + " €";
    }
}
