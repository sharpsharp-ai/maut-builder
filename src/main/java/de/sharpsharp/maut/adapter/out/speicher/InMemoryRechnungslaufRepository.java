package de.sharpsharp.maut.adapter.out.speicher;

import de.sharpsharp.maut.core.domain.Rechnungslauf;
import de.sharpsharp.maut.core.port.out.RechnungslaufRepository;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryRechnungslaufRepository implements RechnungslaufRepository {

    private final Map<String, Rechnungslauf> laeufe = new ConcurrentHashMap<>();
    private final AtomicInteger zaehler = new AtomicInteger(0);

    @Override
    public String naechsteId() {
        return "L-" + zaehler.incrementAndGet();
    }

    @Override
    public void speichere(Rechnungslauf lauf) {
        laeufe.put(lauf.id(), lauf);
    }

    @Override
    public Optional<Rechnungslauf> finde(String id) {
        return Optional.ofNullable(laeufe.get(id));
    }
}
