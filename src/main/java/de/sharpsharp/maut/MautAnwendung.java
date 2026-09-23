package de.sharpsharp.maut;

import de.sharpsharp.maut.adapter.out.speicher.InMemoryEntgeltRepository;
import de.sharpsharp.maut.adapter.out.speicher.InMemoryKorrespondenzRepository;
import de.sharpsharp.maut.adapter.out.speicher.InMemoryKundenRepository;
import de.sharpsharp.maut.adapter.out.speicher.InMemoryRechnungRepository;
import de.sharpsharp.maut.adapter.out.speicher.InMemoryRechnungslaufRepository;
import de.sharpsharp.maut.adapter.out.verarbeitung.SofortigeVerarbeitung;
import de.sharpsharp.maut.core.port.in.Abrechnung;
import de.sharpsharp.maut.core.port.in.KundenVerwaltung;
import de.sharpsharp.maut.core.port.in.Rechnungslaeufe;
import de.sharpsharp.maut.core.port.out.Hintergrundverarbeitung;
import de.sharpsharp.maut.core.service.AbrechnungService;
import de.sharpsharp.maut.core.service.KundenService;
import de.sharpsharp.maut.core.service.RechnungslaufService;

/** Setzt Core und Adapter zusammen. Die Hintergrundverarbeitung ist austauschbar: langsam (echt) oder sofort (Test). */
public class MautAnwendung {

    private final KundenVerwaltung kundenVerwaltung;
    private final Rechnungslaeufe rechnungslaeufe;
    private final Abrechnung abrechnung;

    public MautAnwendung(Hintergrundverarbeitung hintergrund) {
        var entgelte = new InMemoryEntgeltRepository();
        var rechnungen = new InMemoryRechnungRepository();
        kundenVerwaltung = new KundenService(new InMemoryKundenRepository(), entgelte);
        rechnungslaeufe = new RechnungslaufService(
                new InMemoryRechnungslaufRepository(), entgelte, rechnungen, hintergrund);
        abrechnung = new AbrechnungService(new InMemoryKorrespondenzRepository(), rechnungen, hintergrund);
    }

    public static MautAnwendung mitSofortigerVerarbeitung() {
        return new MautAnwendung(new SofortigeVerarbeitung());
    }

    public KundenVerwaltung kundenVerwaltung() { return kundenVerwaltung; }
    public Rechnungslaeufe rechnungslaeufe() { return rechnungslaeufe; }
    public Abrechnung abrechnung() { return abrechnung; }
}
