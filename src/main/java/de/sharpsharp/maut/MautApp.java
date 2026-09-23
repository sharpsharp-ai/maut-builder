package de.sharpsharp.maut;

import de.sharpsharp.maut.adapter.in.web.WebServer;
import de.sharpsharp.maut.adapter.out.verarbeitung.VerzoegerteVerarbeitung;

import java.time.Duration;

/** Startet die Anwendung mit langsamer Hintergrundverarbeitung: mvn compile exec:java */
public class MautApp {

    public static void main(String[] args) {
        var latenz = Duration.ofMillis(Long.getLong("maut.batch.latenz", 3000));
        var anwendung = new MautAnwendung(new VerzoegerteVerarbeitung(latenz));
        var server = WebServer.starte(anwendung, 7272);
        System.out.println("Kundenportal:  http://localhost:" + server.port() + "/portal.html");
        System.out.println("Backend:       http://localhost:" + server.port() + "/backend.html");
        System.out.println("KM-GUI:        http://localhost:" + server.port() + "/km.html");
    }
}
