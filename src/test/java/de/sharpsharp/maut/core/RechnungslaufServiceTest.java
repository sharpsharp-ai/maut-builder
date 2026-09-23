package de.sharpsharp.maut.core;

import de.sharpsharp.maut.adapter.out.speicher.InMemoryEntgeltRepository;
import de.sharpsharp.maut.adapter.out.speicher.InMemoryRechnungRepository;
import de.sharpsharp.maut.adapter.out.speicher.InMemoryRechnungslaufRepository;
import de.sharpsharp.maut.core.domain.RechnungslaufStatus;
import de.sharpsharp.maut.core.port.out.Hintergrundverarbeitung;
import de.sharpsharp.maut.core.service.RechnungslaufService;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class RechnungslaufServiceTest {

    private final Hintergrundverarbeitung hintergrund = mock(Hintergrundverarbeitung.class);
    private final RechnungslaufService service = new RechnungslaufService(new InMemoryRechnungslaufRepository(),
            new InMemoryEntgeltRepository(), new InMemoryRechnungRepository(), hintergrund);

    @Test
    public void gestarteterLaufArbeitetImHintergrund() {
        var lauf = service.neuerLauf();

        service.starten(lauf.id());

        assertThat(lauf.status(), is(RechnungslaufStatus.LAEUFT));
        verify(hintergrund).ausfuehren(any(Runnable.class));
    }

    @Test(expected = IllegalStateException.class)
    public void nichtGestarteterLaufKannNichtFreigegebenWerden() {
        var lauf = service.neuerLauf();

        service.freigeben(lauf.id());
    }
}
