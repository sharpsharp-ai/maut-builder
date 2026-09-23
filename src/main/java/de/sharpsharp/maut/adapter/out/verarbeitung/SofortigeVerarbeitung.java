package de.sharpsharp.maut.adapter.out.verarbeitung;

import de.sharpsharp.maut.core.port.out.Hintergrundverarbeitung;

/** Führt jede Aufgabe sofort und synchron aus. Macht Akzeptanztests schnell und deterministisch. */
public class SofortigeVerarbeitung implements Hintergrundverarbeitung {

    @Override
    public void ausfuehren(Runnable aufgabe) {
        aufgabe.run();
    }
}
