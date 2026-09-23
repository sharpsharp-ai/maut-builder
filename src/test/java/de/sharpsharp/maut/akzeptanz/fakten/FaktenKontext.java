package de.sharpsharp.maut.akzeptanz.fakten;

import de.sharpsharp.maut.akzeptanz.gemeinsam.Welt;
import de.sharpsharp.maut.testdaten.Ausgangslage;
import de.sharpsharp.maut.testdaten.fakten.KundenszenarioBuilder;

/** Given-Schritte konfigurieren den Builder, gebaut wird beim ersten Zugriff (Lazy Build). */
public class FaktenKontext {

    private final Welt welt;
    private final KundenszenarioBuilder builder = KundenszenarioBuilder.einGanzNormalerKunde();
    private boolean gebaut;

    public FaktenKontext(Welt welt) {
        this.welt = welt;
    }

    public KundenszenarioBuilder builder() {
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
