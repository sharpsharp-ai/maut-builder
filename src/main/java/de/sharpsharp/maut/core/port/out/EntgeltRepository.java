package de.sharpsharp.maut.core.port.out;

import de.sharpsharp.maut.core.domain.Entgelt;

import java.util.List;

public interface EntgeltRepository {

    void speichere(Entgelt entgelt);

    List<Entgelt> nichtAbgerechnete();
}
