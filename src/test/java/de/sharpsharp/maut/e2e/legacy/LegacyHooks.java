package de.sharpsharp.maut.e2e.legacy;

import de.sharpsharp.maut.MautAnwendung;
import de.sharpsharp.maut.adapter.in.web.WebServer;
import de.sharpsharp.maut.adapter.out.verarbeitung.VerzoegerteVerarbeitung;
import io.cucumber.java.After;
import io.cucumber.java.AfterAll;
import io.cucumber.java.Before;
import io.cucumber.java.BeforeAll;
import io.javalin.Javalin;

import java.time.Duration;

/** Eine echte Anwendung pro Testlauf, ein Browser pro Szenario. */
public class LegacyHooks {

    private static Javalin server;

    private final LegacyKontext kontext;

    public LegacyHooks(LegacyKontext kontext) {
        this.kontext = kontext;
    }

    @BeforeAll
    public static void serverStarten() {
        var latenz = Duration.ofMillis(Long.getLong("maut.batch.latenz", 2000));
        server = WebServer.starte(new MautAnwendung(new VerzoegerteVerarbeitung(latenz)), 0);
    }

    @AfterAll
    public static void serverStoppen() {
        server.stop();
    }

    static String basisUrl() {
        return "http://localhost:" + server.port();
    }

    @Before
    public void browserStarten() {
        kontext.starten();
    }

    @After
    public void browserBeenden() {
        kontext.beenden();
    }
}
