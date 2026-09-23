package de.sharpsharp.maut.core.service;

import de.sharpsharp.maut.core.domain.Kunde;
import de.sharpsharp.maut.core.domain.Material;
import de.sharpsharp.maut.core.port.in.KundenVerwaltung;
import de.sharpsharp.maut.core.port.out.EntgeltRepository;
import de.sharpsharp.maut.core.port.out.KundenRepository;

import java.util.NoSuchElementException;

public class KundenService implements KundenVerwaltung {

    private final KundenRepository kunden;
    private final EntgeltRepository entgelte;

    public KundenService(KundenRepository kunden, EntgeltRepository entgelte) {
        this.kunden = kunden;
        this.entgelte = entgelte;
    }

    @Override
    public String kundeAnlegen(String name) {
        var kunde = Kunde.neu(kunden.naechsteKundennummer(), name);
        kunden.speichere(kunde);
        return kunde.kundennummer();
    }

    @Override
    public void obuInstallieren(String kundennummer, String kennzeichen) {
        kunde(kundennummer).obuInstallieren(kennzeichen);
    }

    @Override
    public void obuDefektMelden(String kundennummer) {
        kunde(kundennummer).obuDefektMelden();
    }

    @Override
    public void schadenErfassen(String kundennummer, Material material) {
        entgelte.speichere(kunde(kundennummer).schadenBerechnen(material));
    }

    @Override
    public Kunde kunde(String kundennummer) {
        return kunden.finde(kundennummer)
                .orElseThrow(() -> new NoSuchElementException("Unbekannter Kunde " + kundennummer));
    }
}
