package de.sharpsharp.maut.akzeptanz.gemeinsam;

import de.sharpsharp.maut.core.domain.Betrag;
import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.domain.Rechnungsart;
import de.sharpsharp.maut.core.domain.Rechnungsstatus;
import io.cucumber.java.de.Dann;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/** Dann-Schritte: identisch, egal mit welchem Builder die Ausgangslage entstand. */
public class ErgebnisSteps {

    private final Welt welt;

    public ErgebnisSteps(Welt welt) {
        this.welt = welt;
    }

    @Dann("ist die ursprüngliche Rechnung storniert")
    public void istDieUrspruenglicheRechnungStorniert() {
        var rechnung = welt.anwendung().abrechnung().rechnung(welt.ausgangslage().rechnungsnummer());
        assertThat(rechnung.status(), is(Rechnungsstatus.STORNIERT));
    }

    @Dann("der Kunde erhält eine offene Sofortrechnung mit {bigdecimal} € Guthaben")
    public void erhaeltEineOffeneSofortrechnungMitGuthaben(BigDecimal guthaben) {
        var sofortrechnungen = sofortrechnungen();
        assertThat(sofortrechnungen, hasSize(1));
        var sofortrechnung = sofortrechnungen.getFirst();
        assertThat(sofortrechnung.status(), is(Rechnungsstatus.OFFEN));
        assertThat(sofortrechnung.summe(), equalTo(Betrag.euro(guthaben).negiert()));
    }

    private List<Rechnung> sofortrechnungen() {
        return welt.anwendung().abrechnung().rechnungenVon(welt.ausgangslage().kundennummer()).stream()
                .filter(rechnung -> rechnung.art() == Rechnungsart.SOFORTRECHNUNG)
                .toList();
    }
}
