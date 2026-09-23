package de.sharpsharp.maut.e2e.seiten;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.math.BigDecimal;

/** Kundenportal: Kunde, Fahrzeug, OBU und Schäden. */
public class PortalSeite extends Seite {

    public PortalSeite(WebDriver browser, String basisUrl) {
        super(browser, basisUrl);
    }

    public void oeffnen() {
        oeffne("/portal.html");
    }

    public String kundeAnlegen(String name) {
        eingeben("name", name);
        klicke("kunde-anlegen");
        warteBis(b -> text("kundennummer").startsWith("K-"));
        return text("kundennummer");
    }

    public void obuInstallieren(String kennzeichen) {
        eingeben("kennzeichen", kennzeichen);
        klicke("obu-installieren");
        warteAufText("obu-status", "INSTALLIERT");
    }

    public void obuAlsDefektMelden() {
        klicke("obu-defekt-melden");
        warteAufText("obu-status", "DEFEKT");
    }

    public void schadenErfassen(String material, BigDecimal preis) {
        eingeben("material", material);
        eingeben("preis", alsEingabe(preis));
        klicke("schaden-erfassen");
        warteBis(b -> !text("schaden-meldung").isEmpty());
    }

    public String ersteRechnungsnummer() {
        return warteBis(b -> b.findElement(By.cssSelector("#rechnungen tr")).getDomAttribute("data-nummer"));
    }
}
