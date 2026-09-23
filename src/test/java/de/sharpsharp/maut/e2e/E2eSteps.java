package de.sharpsharp.maut.e2e;

import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;

import java.math.BigDecimal;

import static de.sharpsharp.maut.e2e.seiten.Seite.alsAnzeige;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/** Dieselbe Feature-Datei, aber jeder Schritt wird über die drei Oberflächen geklickt. */
public class E2eSteps {

    private final E2eKontext kontext;

    public E2eSteps(E2eKontext kontext) {
        this.kontext = kontext;
    }

    @Angenommen("ein Kunde mit einer defekten OBU")
    public void einKundeMitEinerDefektenObu() {
        kontext.portal.oeffnen();
        kontext.kundennummer = kontext.portal.kundeAnlegen("Spedition Muster GmbH");
        kontext.portal.obuInstallieren("M-SH 1234");
        kontext.portal.obuAlsDefektMelden();
    }

    @Und("dem Kunden wurden {bigdecimal} € für die defekte OBU in Rechnung gestellt")
    public void demKundenWurdeDerSchadenInRechnungGestellt(BigDecimal betrag) {
        kontext.portal.schadenErfassen("Ersatz-OBU", betrag);
        kontext.backend.oeffnen();
        kontext.backend.rechnungslaufDurchfuehren();
        kontext.portal.oeffnen();
        kontext.rechnungsnummer = kontext.portal.ersteRechnungsnummer();
    }

    @Wenn("der Kunde die Rechnung vollständig reklamiert")
    public void derKundeReklamiertDieRechnungVollstaendig() {
        kontext.km.oeffnen();
        kontext.km.korrespondenzAnTasklistSenden(kontext.kundennummer, kontext.rechnungsnummer);
        kontext.km.vorgangZurRechnungOeffnen(kontext.rechnungsnummer);
        kontext.km.reklamationEntscheiden("VOLLSTORNO");
    }

    @Dann("ist die ursprüngliche Rechnung storniert")
    public void istDieUrspruenglicheRechnungStorniert() {
        var urspruenglich = kontext.km.rechnungenDesKunden().stream()
                .filter(zeile -> zeile.nummer().equals(kontext.rechnungsnummer))
                .findFirst().orElseThrow();
        assertThat(urspruenglich.status(), is("STORNIERT"));
    }

    @Und("der Kunde erhält eine offene Sofortrechnung mit {bigdecimal} € Guthaben")
    public void erhaeltEineOffeneSofortrechnungMitGuthaben(BigDecimal guthaben) {
        var sofortrechnungen = kontext.km.rechnungenDesKunden().stream()
                .filter(zeile -> zeile.art().equals("SOFORTRECHNUNG"))
                .toList();
        assertThat(sofortrechnungen, hasSize(1));
        assertThat(sofortrechnungen.get(0).status(), is("OFFEN"));
        assertThat(sofortrechnungen.get(0).betrag(), is(alsAnzeige(guthaben.negate())));
    }
}
