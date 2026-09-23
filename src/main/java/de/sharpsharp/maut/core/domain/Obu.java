package de.sharpsharp.maut.core.domain;

import java.util.Objects;

/** On-Board-Unit, die in einem Fahrzeug installiert ist. */
public class Obu {

    private final String kennzeichen;
    private volatile ObuStatus status;

    public Obu(String kennzeichen, ObuStatus status) {
        this.kennzeichen = Objects.requireNonNull(kennzeichen, "kennzeichen");
        this.status = Objects.requireNonNull(status, "status");
    }

    public static Obu installiertIn(String kennzeichen) {
        return new Obu(kennzeichen, ObuStatus.INSTALLIERT);
    }

    public void alsDefektMelden() {
        if (status != ObuStatus.INSTALLIERT) {
            throw new IllegalStateException("Nur eine installierte OBU kann als defekt gemeldet werden");
        }
        status = ObuStatus.DEFEKT;
    }

    public boolean istDefekt() {
        return status == ObuStatus.DEFEKT;
    }

    public String kennzeichen() {
        return kennzeichen;
    }

    public ObuStatus status() {
        return status;
    }
}
