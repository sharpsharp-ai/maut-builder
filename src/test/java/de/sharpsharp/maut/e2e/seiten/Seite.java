package de.sharpsharp.maut.e2e.seiten;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.StaleElementReferenceException;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;

/** Gemeinsame Handgriffe aller Seitenobjekte. Nur hier steckt Selenium-Technik. */
public abstract class Seite {

    private static final Duration GEDULD = Duration.ofSeconds(30);

    protected final WebDriver browser;
    private final String basisUrl;

    Seite(WebDriver browser, String basisUrl) {
        this.browser = browser;
        this.basisUrl = basisUrl;
    }

    protected void oeffne(String pfad) {
        browser.get(basisUrl + pfad);
    }

    protected void eingeben(String id, String wert) {
        var feld = browser.findElement(By.id(id));
        feld.clear();
        feld.sendKeys(wert);
    }

    protected void klicke(String id) {
        warteBis(b -> b.findElement(By.id(id)).isEnabled());
        browser.findElement(By.id(id)).click();
    }

    protected String text(String id) {
        return browser.findElement(By.id(id)).getText();
    }

    protected void warteAufText(String id, String erwartet) {
        warteBis(b -> erwartet.equals(b.findElement(By.id(id)).getText()));
    }

    protected <T> T warteBis(ExpectedCondition<T> bedingung) {
        return new WebDriverWait(browser, GEDULD)
                .ignoring(StaleElementReferenceException.class)
                .ignoring(NoSuchElementException.class)
                .until(bedingung);
    }

    /** So zeigt die GUI Beträge an, z. B. "-120,00 €". */
    public static String alsAnzeige(BigDecimal betrag) {
        return alsEingabe(betrag) + " €";
    }

    /** So tippt ein Mensch einen Betrag ein, z. B. "120,00". */
    public static String alsEingabe(BigDecimal betrag) {
        return betrag.setScale(2, RoundingMode.HALF_UP).toPlainString().replace('.', ',');
    }
}
