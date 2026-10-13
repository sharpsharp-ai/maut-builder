package de.sharpsharp.maut.e2e.legacy;

import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

/**
 * Zustand eines Szenarios und die Handgriffe auf den Oberflächen. Bewusst ohne Seitenobjekte:
 * Jeder Schritt im Feature ist hier genau eine Aktion oder eine Prüfung auf dem Bildschirm.
 */
public class LegacyKontext {

    private static final Duration GEDULD = Duration.ofSeconds(30);

    record Rechnungszeile(String nummer, String art, String status, String betrag) { }

    WebDriver browser;
    String kundennummer;
    String rechnungsnummer;
    private String rechnungstabelle = "rechnungen";

    void starten() {
        var optionen = new ChromeOptions();
        if (!Boolean.getBoolean("e2e.sichtbar")) {
            optionen.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        optionen.addArguments("--window-size=1280,1000");
        browser = new ChromeDriver(optionen);
    }

    void beenden() {
        if (browser != null) {
            browser.quit();
        }
    }

    // ---------- Navigation ----------

    void oeffne(String seite) {
        browser.get(LegacyHooks.basisUrl() + "/" + seite + ".html");
        rechnungstabelle = seite.equals("km") ? "kunden-rechnungen" : "rechnungen";
    }

    // ---------- Handgriffe ----------

    void tippe(String id, String wert) {
        warteBis("Feld " + id + " ist bedienbar", b -> b.findElement(By.id(id)).isEnabled());
        var feld = browser.findElement(By.id(id));
        feld.clear();
        feld.sendKeys(wert);
    }

    void klicke(String id) {
        warteBis("Schaltfläche " + id + " ist bedienbar", b -> b.findElement(By.id(id)).isEnabled());
        browser.findElement(By.id(id)).click();
    }

    void waehle(String id, String sichtbarerText) {
        warteBis("Auswahl " + id + " ist bedienbar", b -> b.findElement(By.id(id)).isEnabled());
        new Select(browser.findElement(By.id(id))).selectByVisibleText(sichtbarerText);
    }

    String text(String id) {
        return browser.findElement(By.id(id)).getText();
    }

    void sieheText(String id, String erwartet) {
        warteBis("Feld " + id + " zeigt \"" + erwartet + "\"", b -> erwartet.equals(text(id)));
    }

    // ---------- Rechnungstabellen (Kundenportal und Kundenmanagement) ----------

    List<Rechnungszeile> rechnungszeilen() {
        return browser.findElements(By.cssSelector("#" + rechnungstabelle + " tr")).stream()
                .map(zeile -> new Rechnungszeile(
                        zeile.getDomAttribute("data-nummer"),
                        zeile.findElement(By.className("art")).getText(),
                        zeile.findElement(By.className("status")).getText(),
                        zeile.findElement(By.className("betrag")).getText()))
                .toList();
    }

    void sieheRechnung(String art, String status, String betrag) {
        warteBis("Rechnung " + art + " mit Status " + status + " und Betrag " + betrag, b ->
                rechnungszeilen().stream().anyMatch(z ->
                        z.art().equals(art) && z.status().equals(status) && z.betrag().equals(betrag)));
        long anzahl = rechnungszeilen().stream().filter(z -> z.art().equals(art)).count();
        Assert.assertEquals("Anzahl Rechnungen der Art " + art, 1, anzahl);
    }

    String rechnungsnummerDerArt(String art) {
        return warteBis("Rechnung der Art " + art + " ist sichtbar", b ->
                rechnungszeilen().stream()
                        .filter(z -> z.art().equals(art))
                        .map(Rechnungszeile::nummer)
                        .findFirst().orElse(null));
    }

    // ---------- Tasklist Abrechnungen ----------

    private WebElement vorgangszeile() {
        return browser.findElement(By.xpath(
                "//tbody[@id='tasklist']/tr[td[3]='" + rechnungsnummer + "']"));
    }

    void sieheVorgangsstatusInTasklist(String status) {
        warteBis("Vorgang zur Rechnung " + rechnungsnummer + " zeigt " + status, b ->
                status.equals(vorgangszeile().findElement(By.xpath("./td[4]")).getText()));
    }

    void oeffneVorgang() {
        warteBis("Vorgang ist über Öffnen bedienbar", b -> {
            vorgangszeile().findElement(By.cssSelector("button.oeffnen")).click();
            return true;
        });
    }

    boolean reklamationSichtbar() {
        return browser.findElement(By.id("vorgang")).isDisplayed();
    }

    // ---------- Warten ----------

    <T> T warteBis(String beschreibung, ExpectedCondition<T> bedingung) {
        return new WebDriverWait(browser, GEDULD)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .withMessage(beschreibung)
                .until(bedingung);
    }
}
