package de.sharpsharp.maut.core.service;

import de.sharpsharp.maut.core.domain.Korrespondenz;
import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.domain.Reklamationsergebnis;
import de.sharpsharp.maut.core.port.in.Abrechnung;
import de.sharpsharp.maut.core.port.out.Hintergrundverarbeitung;
import de.sharpsharp.maut.core.port.out.KorrespondenzRepository;
import de.sharpsharp.maut.core.port.out.RechnungRepository;

import java.util.List;
import java.util.NoSuchElementException;

public class AbrechnungService implements Abrechnung {

    private final KorrespondenzRepository korrespondenzen;
    private final RechnungRepository rechnungen;
    private final Hintergrundverarbeitung hintergrund;

    public AbrechnungService(KorrespondenzRepository korrespondenzen, RechnungRepository rechnungen,
                             Hintergrundverarbeitung hintergrund) {
        this.korrespondenzen = korrespondenzen;
        this.rechnungen = rechnungen;
        this.hintergrund = hintergrund;
    }

    @Override
    public Korrespondenz korrespondenzErfassen(String kundennummer, String rechnungsnummer) {
        if (!rechnung(rechnungsnummer).kundennummer().equals(kundennummer)) {
            throw new IllegalArgumentException("Rechnung " + rechnungsnummer + " gehört nicht zu " + kundennummer);
        }
        var korrespondenz = new Korrespondenz(korrespondenzen.naechsteId(), kundennummer, rechnungsnummer);
        korrespondenzen.speichere(korrespondenz);
        return korrespondenz;
    }

    @Override
    public List<Korrespondenz> tasklistAbrechnungen() {
        return korrespondenzen.alle();
    }

    @Override
    public Korrespondenz korrespondenz(String korrespondenzId) {
        return korrespondenzen.finde(korrespondenzId)
                .orElseThrow(() -> new NoSuchElementException("Unbekannte Korrespondenz " + korrespondenzId));
    }

    @Override
    public void reklamationEntscheiden(String korrespondenzId, Reklamationsergebnis ergebnis) {
        var korrespondenz = korrespondenz(korrespondenzId);
        korrespondenz.reklamationEntscheiden(ergebnis);
        hintergrund.ausfuehren(() -> umsetzen(korrespondenz));
    }

    @Override
    public List<Rechnung> rechnungenVon(String kundennummer) {
        return rechnungen.vonKunde(kundennummer);
    }

    @Override
    public Rechnung rechnung(String rechnungsnummer) {
        return rechnungen.finde(rechnungsnummer)
                .orElseThrow(() -> new NoSuchElementException("Unbekannte Rechnung " + rechnungsnummer));
    }

    private void umsetzen(Korrespondenz korrespondenz) {
        if (korrespondenz.ergebnis() == Reklamationsergebnis.VOLLSTORNO) {
            vollstorno(rechnung(korrespondenz.rechnungsnummer()));
        }
        korrespondenz.erledigt();
    }

    private void vollstorno(Rechnung rechnung) {
        rechnung.stornieren();
        rechnungen.speichere(rechnung.sofortrechnungMitGuthaben(rechnungen.naechsteRechnungsnummer()));
    }
}
