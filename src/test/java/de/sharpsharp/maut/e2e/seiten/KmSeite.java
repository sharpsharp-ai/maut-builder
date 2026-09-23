package de.sharpsharp.maut.e2e.seiten;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import java.util.List;

/** Kundenmanagement: Korrespondenz, Tasklist Abrechnungen, Reklamation, Rechnungen des Kunden. */
public class KmSeite extends Seite {

    public record Rechnungszeile(String nummer, String art, String status, String betrag) { }

    public KmSeite(WebDriver browser, String basisUrl) {
        super(browser, basisUrl);
    }

    public void oeffnen() {
        oeffne("/km.html");
    }

    public void korrespondenzAnTasklistSenden(String kundennummer, String rechnungsnummer) {
        eingeben("korr-kundennummer", kundennummer);
        eingeben("korr-rechnungsnummer", rechnungsnummer);
        klicke("korrespondenz-senden");
    }

    public void vorgangZurRechnungOeffnen(String rechnungsnummer) {
        warteBis(b -> {
            b.findElement(By.xpath("//tbody[@id='tasklist']/tr[td[3]='" + rechnungsnummer + "']//button")).click();
            return true;
        });
    }

    public void reklamationEntscheiden(String ergebnis) {
        new Select(browser.findElement(By.id("ergebnis"))).selectByValue(ergebnis);
        klicke("reklamation-anlegen");
        warteAufText("vorgang-status", "ERLEDIGT");
    }

    public List<Rechnungszeile> rechnungenDesKunden() {
        return browser.findElements(By.cssSelector("#kunden-rechnungen tr")).stream()
                .map(KmSeite::alsRechnungszeile)
                .toList();
    }

    private static Rechnungszeile alsRechnungszeile(WebElement zeile) {
        return new Rechnungszeile(
                zeile.findElement(By.className("nummer")).getText(),
                zeile.findElement(By.className("art")).getText(),
                zeile.findElement(By.className("status")).getText(),
                zeile.findElement(By.className("betrag")).getText());
    }
}
