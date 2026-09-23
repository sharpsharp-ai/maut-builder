package de.sharpsharp.maut.adapter.out.speicher;

import de.sharpsharp.maut.core.domain.Entgelt;
import de.sharpsharp.maut.core.port.out.EntgeltRepository;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

public class InMemoryEntgeltRepository implements EntgeltRepository {

    private final List<Entgelt> entgelte = new CopyOnWriteArrayList<>();

    @Override
    public void speichere(Entgelt entgelt) {
        entgelte.add(entgelt);
    }

    @Override
    public List<Entgelt> nichtAbgerechnete() {
        return entgelte.stream().filter(entgelt -> !entgelt.istAbgerechnet()).toList();
    }
}
