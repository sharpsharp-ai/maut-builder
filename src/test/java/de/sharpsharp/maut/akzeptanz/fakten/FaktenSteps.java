package de.sharpsharp.maut.akzeptanz.fakten;

import de.sharpsharp.maut.akzeptanz.gemeinsam.Welt;
import de.sharpsharp.maut.core.domain.Betrag;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;

import java.math.BigDecimal;

/** Variante 1: Ausgangslage über Fakten-Builder, Rechnung aus dem echten Rechnungslauf. */
public class FaktenSteps {

    private final Welt welt;
    private final FaktenKontext kontext;

    public FaktenSteps(Welt welt, FaktenKontext kontext) {
        this.welt = welt;
        this.kontext = kontext;
    }

    @Angenommen("ein Kunde mit einer defekten OBU")
    public void einKundeMitEinerDefektenObu() {
        kontext.builder().mitDefekterObu();
    }

    @Und("dem Kunden wurden {bigdecimal} € für die defekte OBU in Rechnung gestellt")
    public void demKundenWurdeDerSchadenInRechnungGestellt(BigDecimal betrag) {
        kontext.builder().mitAbgerechnetemSchaden(Betrag.euro(betrag));
    }

    @Wenn("der Kunde die Rechnung vollständig reklamiert")
    public void derKundeReklamiertDieRechnungVollstaendig() {
        kontext.ausgangslage();
        welt.vollstaendigReklamieren();
    }
}
