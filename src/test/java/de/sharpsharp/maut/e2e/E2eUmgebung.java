package de.sharpsharp.maut.e2e;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.adapter.in.web.WebServer;
import de.sharpsharp.maut.adapter.out.verarbeitung.VerzoegerteVerarbeitung;
import io.cucumber.java.AfterAll;
import io.cucumber.java.BeforeAll;
import io.javalin.Javalin;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;

import java.time.Duration;

/**
 * Startet einmal pro Testlauf die echte Anwendung (mit langsamer Hintergrundverarbeitung)
 * und einen Chrome. Mit -De2e.sichtbar=true sieht man dem Browser beim Klicken zu.
 */
public class E2eUmgebung {

    private static Javalin server;
    private static WebDriver browser;

    @BeforeAll
    public static void starten() {
        var latenz = Duration.ofMillis(Long.getLong("maut.batch.latenz", 2000));
        server = WebServer.starte(new MautAnwendung(new VerzoegerteVerarbeitung(latenz)), 0);
        browser = new ChromeDriver(chromeOptionen());
    }

    @AfterAll
    public static void beenden() {
        browser.quit();
        server.stop();
    }

    static WebDriver browser() {
        return browser;
    }

    static String basisUrl() {
        return "http://localhost:" + server.port();
    }

    private static ChromeOptions chromeOptionen() {
        var optionen = new ChromeOptions();
        if (!Boolean.getBoolean("e2e.sichtbar")) {
            optionen.addArguments("--headless=new", "--no-sandbox", "--disable-dev-shm-usage");
        }
        optionen.addArguments("--window-size=1280,1000");
        return optionen;
    }
}
