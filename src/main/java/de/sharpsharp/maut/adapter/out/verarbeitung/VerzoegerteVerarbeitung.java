package de.sharpsharp.maut.adapter.out.verarbeitung;

import de.sharpsharp.maut.core.port.out.Hintergrundverarbeitung;

import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Simuliert die echte, langsame Batchverarbeitung: Jede Aufgabe läuft
 * nacheinander in einem eigenen Thread, erst nach einer Wartezeit.
 */
public class VerzoegerteVerarbeitung implements Hintergrundverarbeitung {

    private final Duration latenz;
    private final ExecutorService ausfuehrer = Executors.newSingleThreadExecutor();

    public VerzoegerteVerarbeitung(Duration latenz) {
        this.latenz = latenz;
    }

    @Override
    public void ausfuehren(Runnable aufgabe) {
        ausfuehrer.submit(() -> verzoegertAusfuehren(aufgabe));
    }

    private void verzoegertAusfuehren(Runnable aufgabe) {
        try {
            Thread.sleep(latenz);
            aufgabe.run();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } catch (RuntimeException e) {
            System.err.println("Hintergrundaufgabe fehlgeschlagen: " + e.getMessage());
        }
    }
}
