package de.sharpsharp.maut.core;

import de.sharpsharp.maut.core.domain.Betrag;
import de.sharpsharp.maut.core.domain.Position;
import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.domain.Rechnungsart;
import de.sharpsharp.maut.core.domain.Rechnungsstatus;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.is;

public class RechnungTest {

    private final Rechnung rechnung = Rechnung.entwurf("R-1", "K-1", List.of(
            new Position("Ersatz-OBU", Betrag.euro("95.00")),
            new Position("Einbau", Betrag.euro("25.00"))));

    @Test
    public void summeIstDieSummeAllerPositionen() {
        assertThat(rechnung.summe(), equalTo(Betrag.euro("120.00")));
    }

    @Test
    public void gebuchteRechnungIstOffen() {
        rechnung.buchen();

        assertThat(rechnung.status(), is(Rechnungsstatus.OFFEN));
    }

    @Test(expected = IllegalStateException.class)
    public void entwurfKannNichtStorniertWerden() {
        rechnung.stornieren();
    }

    @Test
    public void sofortrechnungSchreibtDieSummeAlsGuthabenGut() {
        rechnung.buchen();

        var sofortrechnung = rechnung.sofortrechnungMitGuthaben("R-2");

        assertThat(sofortrechnung.art(), is(Rechnungsart.SOFORTRECHNUNG));
        assertThat(sofortrechnung.status(), is(Rechnungsstatus.OFFEN));
        assertThat(sofortrechnung.summe(), equalTo(Betrag.euro("-120.00")));
        assertThat(sofortrechnung.bezugsnummer(), is("R-1"));
    }
}
