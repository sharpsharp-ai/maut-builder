package de.sharpsharp.maut.adapter.out.speicher;

import de.sharpsharp.maut.core.domain.Kunde;
import de.sharpsharp.maut.core.port.out.KundenRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryKundenRepository implements KundenRepository {

    private final Map<String, Kunde> kunden = new ConcurrentHashMap<>();
    private final AtomicInteger zaehler = new AtomicInteger(1000);

    @Override
    public String naechsteKundennummer() {
        return "K-" + zaehler.incrementAndGet();
    }

    @Override
    public void speichere(Kunde kunde) {
        kunden.put(kunde.kundennummer(), kunde);
    }

    @Override
    public Optional<Kunde> finde(String kundennummer) {
        return Optional.ofNullable(kunden.get(kundennummer));
    }
}
