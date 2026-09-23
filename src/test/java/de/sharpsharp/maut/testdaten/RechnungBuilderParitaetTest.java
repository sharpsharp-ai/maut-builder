package de.sharpsharp.maut.testdaten;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.core.domain.Betrag;
import de.sharpsharp.maut.testdaten.endzustand.OffeneRechnungBuilder;
import de.sharpsharp.maut.testdaten.fakten.KundenszenarioBuilder;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;

/**
 * Paritätstest: Liefert der Endzustand-Builder (Variante 3) fachlich dasselbe
 * wie der echte Workflow? Ein Test pro Builder-Preset. Wird er rot, ist der
 * Builder weggedriftet – nicht die Anwendung kaputt.
 */
public class RechnungBuilderParitaetTest {

    private static final Betrag SCHADEN = Betrag.euro("120.00");

    @Test
    public void offeneRechnungFuerDefekteObuEntsprichtDemEchtenRechnungslauf() {
        var echteAnwendung = MautAnwendung.mitSofortigerVerarbeitung();
        var ausWorkflow = KundenszenarioBuilder.einGanzNormalerKunde()
                .mitAbgerechnetemSchaden(SCHADEN)
                .build(echteAnwendung);

        var gebauteAnwendung = MautAnwendung.mitSofortigerVerarbeitung();
        var ausBuilder = OffeneRechnungBuilder.eineOffeneRechnung()
                .fuerEineDefekteObu()
                .mitSchaden(SCHADEN)
                .build(gebauteAnwendung);

        assertThat(FachlicheSicht.von(gebauteAnwendung, ausBuilder),
                equalTo(FachlicheSicht.von(echteAnwendung, ausWorkflow)));
    }
}
