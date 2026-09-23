package de.sharpsharp.maut.akzeptanz.endzustand;

import de.sharpsharp.maut.akzeptanz.gemeinsam.Welt;
import de.sharpsharp.maut.testdaten.Ausgangslage;
import de.sharpsharp.maut.testdaten.endzustand.OffeneRechnungBuilder;

/** Given-Schritte konfigurieren den Builder, gebaut wird beim ersten Zugriff (Lazy Build). */
public class EndzustandKontext {

    private final Welt welt;
    private final OffeneRechnungBuilder builder = OffeneRechnungBuilder.eineOffeneRechnung();
    private boolean gebaut;

    public EndzustandKontext(Welt welt) {
        this.welt = welt;
    }

    public OffeneRechnungBuilder builder() {
        if (gebaut) {
            throw new IllegalStateException("Ausgangslage ist schon gebaut – Angenommen nach Wenn?");
        }
        return builder;
    }

    public Ausgangslage ausgangslage() {
        if (!gebaut) {
            welt.merke(builder.build(welt.anwendung()));
            gebaut = true;
        }
        return welt.ausgangslage();
    }
}
