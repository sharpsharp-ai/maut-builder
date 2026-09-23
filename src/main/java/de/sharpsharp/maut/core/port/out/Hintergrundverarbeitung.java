package de.sharpsharp.maut.core.port.out;

/**
 * Führt länger laufende Arbeit aus (Rechnungsläufe, Buchungen).
 * In Produktion asynchron und langsam, in Akzeptanztests sofort und synchron.
 */
public interface Hintergrundverarbeitung {

    void ausfuehren(Runnable aufgabe);
}
