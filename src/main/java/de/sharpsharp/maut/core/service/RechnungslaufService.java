package de.sharpsharp.maut.core.service;

import de.sharpsharp.maut.core.domain.Entgelt;
import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.domain.Rechnungslauf;
import de.sharpsharp.maut.core.port.in.Rechnungslaeufe;
import de.sharpsharp.maut.core.port.out.EntgeltRepository;
import de.sharpsharp.maut.core.port.out.Hintergrundverarbeitung;
import de.sharpsharp.maut.core.port.out.RechnungRepository;
import de.sharpsharp.maut.core.port.out.RechnungslaufRepository;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

public class RechnungslaufService implements Rechnungslaeufe {

    private final RechnungslaufRepository laeufe;
    private final EntgeltRepository entgelte;
    private final RechnungRepository rechnungen;
    private final Hintergrundverarbeitung hintergrund;

    public RechnungslaufService(RechnungslaufRepository laeufe, EntgeltRepository entgelte,
                                RechnungRepository rechnungen, Hintergrundverarbeitung hintergrund) {
        this.laeufe = laeufe;
        this.entgelte = entgelte;
        this.rechnungen = rechnungen;
        this.hintergrund = hintergrund;
    }

    @Override
    public Rechnungslauf neuerLauf() {
        var lauf = new Rechnungslauf(laeufe.naechsteId());
        laeufe.speichere(lauf);
        return lauf;
    }

    @Override
    public void starten(String laufId) {
        var lauf = lauf(laufId);
        lauf.starten();
        hintergrund.ausfuehren(() -> lauf.entwuerfeErstellt(entwuerfeErstellen()));
    }

    @Override
    public void freigeben(String laufId) {
        var lauf = lauf(laufId);
        lauf.freigeben();
        hintergrund.ausfuehren(() -> buchen(lauf));
    }

    @Override
    public Rechnungslauf lauf(String laufId) {
        return laeufe.finde(laufId)
                .orElseThrow(() -> new NoSuchElementException("Unbekannter Rechnungslauf " + laufId));
    }

    private List<String> entwuerfeErstellen() {
        var proKunde = entgelte.nichtAbgerechnete().stream()
                .collect(Collectors.groupingBy(Entgelt::kundennummer));
        return proKunde.entrySet().stream()
                .map(eintrag -> entwurfErstellen(eintrag.getKey(), eintrag.getValue()))
                .toList();
    }

    private String entwurfErstellen(String kundennummer, List<Entgelt> offeneEntgelte) {
        var positionen = offeneEntgelte.stream().map(Entgelt::alsPosition).toList();
        var entwurf = Rechnung.entwurf(rechnungen.naechsteRechnungsnummer(), kundennummer, positionen);
        offeneEntgelte.forEach(Entgelt::abrechnen);
        rechnungen.speichere(entwurf);
        return entwurf.nummer();
    }

    private void buchen(Rechnungslauf lauf) {
        lauf.rechnungsnummern().forEach(nummer -> rechnungen.finde(nummer).orElseThrow().buchen());
        lauf.gebucht();
    }
}
