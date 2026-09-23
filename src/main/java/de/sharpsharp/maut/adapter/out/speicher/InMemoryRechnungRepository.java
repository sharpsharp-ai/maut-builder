package de.sharpsharp.maut.adapter.out.speicher;

import de.sharpsharp.maut.core.domain.Rechnung;
import de.sharpsharp.maut.core.port.out.RechnungRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRechnungRepository implements RechnungRepository {

    private final Map<String, Rechnung> rechnungen = new ConcurrentHashMap<>();
    private final AtomicInteger zaehler = new AtomicInteger(2000);

    @Override
    public String naechsteRechnungsnummer() {
        return "R-" + zaehler.incrementAndGet();
    }

    @Override
    public void speichere(Rechnung rechnung) {
        rechnungen.put(rechnung.nummer(), rechnung);
    }

    @Override
    public Optional<Rechnung> finde(String rechnungsnummer) {
        return Optional.ofNullable(rechnungen.get(rechnungsnummer));
    }

    @Override
    public List<Rechnung> vonKunde(String kundennummer) {
        return rechnungen.values().stream()
                .filter(rechnung -> rechnung.kundennummer().equals(kundennummer))
                .sorted(Comparator.comparing(Rechnung::nummer))
                .toList();
    }
}
