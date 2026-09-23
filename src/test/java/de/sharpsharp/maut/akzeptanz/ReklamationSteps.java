package de.sharpsharp.maut.akzeptanz;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.core.domain.Betrag;
import de.sharpsharp.maut.core.domain.Kunde;
import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.domain.Rechnungsart;
import de.sharpsharp.maut.core.domain.Reklamationsergebnis;
import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;

import java.math.BigDecimal;

import static de.sharpsharp.maut.core.domain.Rechnungsstatus.OFFEN;
import static de.sharpsharp.maut.core.domain.Rechnungsstatus.STORNIERT;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class ReklamationSteps {

    // Sofortige Verarbeitung: Rechnungsläufe laufen synchron, niemand muss warten.
    private final MautAnwendung anwendung = MautAnwendung.mitSofortigerVerarbeitung();
    private final KundeBuilder kundeBuilder = KundeBuilder.einGanzNormalerKunde();
    private Kunde kunde;

    @Angenommen("ein Kunde mit einer defekten OBU")
    public void einKundeMitEinerDefektenObu() {
        kundeBuilder.mitDefekterObu();
    }

    @Und("dem Kunden wurden {bigdecimal} € für die defekte OBU in Rechnung gestellt")
    public void demKundenWurdeDerSchadenInRechnungGestellt(BigDecimal betrag) {
        kundeBuilder.mitRechnungUeber(Betrag.euro(betrag));
    }

    @Wenn("der Kunde die Rechnung vollständig reklamiert")
    public void derKundeReklamiertDieRechnungVollstaendig() {
        kunde = kundeBuilder.build(anwendung);
        var urspruenglicheRechnung = rechnung(Rechnungsart.RECHNUNG);
        var abrechnung = anwendung.abrechnung();
        var korrespondenz = abrechnung.korrespondenzErfassen(kunde.kundennummer(), urspruenglicheRechnung.nummer());
        abrechnung.reklamationEntscheiden(korrespondenz.id(), Reklamationsergebnis.VOLLSTORNO);
    }

    @Dann("ist die ursprüngliche Rechnung storniert")
    public void istDieUrspruenglicheRechnungStorniert() {
        assertThat(rechnung(Rechnungsart.RECHNUNG).status(), is(STORNIERT));
    }

    @Und("der Kunde erhält eine offene Sofortrechnung mit {bigdecimal} € Guthaben")
    public void erhaeltEineOffeneSofortrechnungMitGuthaben(BigDecimal guthaben) {
        var sofortrechnung = rechnung(Rechnungsart.SOFORTRECHNUNG);
        assertThat(sofortrechnung.status(), is(OFFEN));
        assertThat(sofortrechnung.summe(), equalTo(Betrag.euro(guthaben).negiert()));
    }

    private Rechnung rechnung(Rechnungsart art) {
        return anwendung.abrechnung().rechnungenVon(kunde.kundennummer()).stream()
                .filter(rechnung -> rechnung.art() == art)
                .findFirst()
                .orElseThrow(() -> new AssertionError("Keine " + art + " für " + kunde.kundennummer()));
    }
}
