package de.sharpsharp.maut.akzeptanz.endzustand;

import de.sharpsharp.maut.akzeptanz.gemeinsam.Welt;
import de.sharpsharp.maut.core.domain.Betrag;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;

import java.math.BigDecimal;

/** Variante 3: Ausgangslage als direkt abgelegter Endzustand (offene Rechnung). */
public class EndzustandSteps {

    private final Welt welt;
    private final EndzustandKontext kontext;

    public EndzustandSteps(Welt welt, EndzustandKontext kontext) {
        this.welt = welt;
        this.kontext = kontext;
    }

    @Angenommen("ein Kunde mit einer defekten OBU")
    public void einKundeMitEinerDefektenObu() {
        kontext.builder().fuerEineDefekteObu();
    }

    @Und("dem Kunden wurden {bigdecimal} € für die defekte OBU in Rechnung gestellt")
    public void demKundenWurdeDerSchadenInRechnungGestellt(BigDecimal betrag) {
        kontext.builder().mitSchaden(Betrag.euro(betrag));
    }

    @Wenn("der Kunde die Rechnung vollständig reklamiert")
    public void derKundeReklamiertDieRechnungVollstaendig() {
        kontext.ausgangslage();
        welt.vollstaendigReklamieren();
    }
}
