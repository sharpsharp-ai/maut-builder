package de.sharpsharp.maut.adapter.in.web;

import de.sharpsharp.maut.core.domain.Korrespondenz;
import de.sharpsharp.maut.core.domain.Kunde;
import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.domain.Rechnungslauf;

import java.math.BigDecimal;

/** JSON-Ausgaben der Web-Oberflächen. Die Domäne selbst wird nie direkt serialisiert. */
final class Antworten {

    private Antworten() {
    }

    record KundeAntwort(String kundennummer, String name, String kennzeichen, String obuStatus) {
        static KundeAntwort von(Kunde kunde) {
            var kennzeichen = kunde.hatObu() ? kunde.obu().kennzeichen() : null;
            var obuStatus = kunde.hatObu() ? kunde.obu().status().name() : null;
            return new KundeAntwort(kunde.kundennummer(), kunde.name(), kennzeichen, obuStatus);
        }
    }

    record RechnungAntwort(String nummer, String art, String status, BigDecimal betrag, String bezugsnummer) {
        static RechnungAntwort von(Rechnung rechnung) {
            return new RechnungAntwort(rechnung.nummer(), rechnung.art().name(), rechnung.status().name(),
                    rechnung.summe().wert(), rechnung.bezugsnummer());
        }
    }

    record LaufAntwort(String id, String status, int anzahlRechnungen) {
        static LaufAntwort von(Rechnungslauf lauf) {
            return new LaufAntwort(lauf.id(), lauf.status().name(), lauf.rechnungsnummern().size());
        }
    }

    record KorrespondenzAntwort(String id, String kundennummer, String rechnungsnummer,
                                String status, String ergebnis) {
        static KorrespondenzAntwort von(Korrespondenz k) {
            var ergebnis = k.ergebnis() == null ? null : k.ergebnis().name();
            return new KorrespondenzAntwort(k.id(), k.kundennummer(), k.rechnungsnummer(), k.status().name(), ergebnis);
        }
    }

    record Fehler(String meldung) { }
}
