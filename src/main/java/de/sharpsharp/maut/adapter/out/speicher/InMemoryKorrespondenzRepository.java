package de.sharpsharp.maut.adapter.out.speicher;

import de.sharpsharp.maut.core.domain.Korrespondenz;
import de.sharpsharp.maut.core.port.out.KorrespondenzRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

public class InMemoryKorrespondenzRepository implements KorrespondenzRepository {

    private final Map<String, Korrespondenz> korrespondenzen = new ConcurrentHashMap<>();
    private final AtomicInteger zaehler = new AtomicInteger(0);

    @Override
    public String naechsteId() {
        return "KO-" + String.format("%03d", zaehler.incrementAndGet());
    }

    @Override
    public void speichere(Korrespondenz korrespondenz) {
        korrespondenzen.put(korrespondenz.id(), korrespondenz);
    }

    @Override
    public Optional<Korrespondenz> finde(String id) {
        return Optional.ofNullable(korrespondenzen.get(id));
    }

    @Override
    public List<Korrespondenz> alle() {
        return korrespondenzen.values().stream()
                .sorted(Comparator.comparing(Korrespondenz::id))
                .toList();
    }
}
