package de.sharpsharp.maut.e2e.seiten;

import org.openqa.selenium.WebDriver;

/** Backend: Rechnungsläufe. Starten und Freigeben dauern – die Seite wartet wie ein Mensch. */
public class BackendSeite extends Seite {

    public BackendSeite(WebDriver browser, String basisUrl) {
        super(browser, basisUrl);
    }

    public void oeffnen() {
        oeffne("/backend.html");
    }

    public void rechnungslaufDurchfuehren() {
        klicke("neuer-lauf");
        warteAufText("lauf-status", "ANGELEGT");
        klicke("lauf-starten");
        warteAufText("lauf-status", "BUCHUNGSBEREIT");
        klicke("lauf-freigeben");
        warteAufText("lauf-status", "GEBUCHT");
    }
}
