package de.sharpsharp.maut.e2e;

import de.sharpsharp.maut.e2e.seiten.BackendSeite;
import de.sharpsharp.maut.e2e.seiten.KmSeite;
import de.sharpsharp.maut.e2e.seiten.PortalSeite;

/** Zustand eines E2E-Szenarios: die drei Oberflächen und was wir unterwegs abgelesen haben. */
public class E2eKontext {

    final PortalSeite portal = new PortalSeite(E2eUmgebung.browser(), E2eUmgebung.basisUrl());
    final BackendSeite backend = new BackendSeite(E2eUmgebung.browser(), E2eUmgebung.basisUrl());
    final KmSeite km = new KmSeite(E2eUmgebung.browser(), E2eUmgebung.basisUrl());

    String kundennummer;
    String rechnungsnummer;
}
