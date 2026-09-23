# maut-builder – Akzeptanztests mit einem Testdaten-Builder

Übungsrepo für das CSD-Training. Es zeigt, wie ein Testdaten-Builder einen Akzeptanztest
kurz und schnell macht: Statt sich durch den ganzen Workflow zu klicken, beschreibt der
Test nur, was gelten soll, und der Builder stellt diesen Zustand her.

Fachlicher Kontext (stark vereinfacht): Ein Kunde hat eine defekte On-Board-Unit (OBU).
Der Schaden wurde ihm in Rechnung gestellt. Er reklamiert, die Abrechnung entscheidet auf
Vollstorno: Die Rechnung wird storniert, und der Kunde erhält eine Sofortrechnung mit Guthaben.

## Wo steht was?

| Datei | Rolle |
|---|---|
| `src/test/resources/features/reklamation.feature` | die Spezifikation |
| `src/test/java/.../akzeptanz/ReklamationSteps.java` | verbindet Feature und Code |
| `src/test/java/.../akzeptanz/KundeBuilder.java` | der Testdaten-Builder |
| `src/test/java/.../akzeptanz/AkzeptanzTest.java` | startet Cucumber |

Die Angenommen-Steps rufen nur `mitDefekterObu()` und `mitRechnungUeber(...)` auf.
Gebaut wird erst im Wenn-Step mit `build(...)`.

## Befehle

Voraussetzungen: Java 17 und Maven.

| Befehl | Was passiert |
|---|---|
| `mvn test` | führt den Akzeptanztest aus |
| `mvn compile exec:java` | startet die Anwendung: http://localhost:7272/portal.html |

## Stufe 2

Der Branch `stufe-2-e2e-und-drift` vertieft das Thema: dieselbe Feature-Datei zusätzlich
als E2E-Test über die GUI (Selenium), ein zweiter Builder, der den Endzustand direkt
ablegt, und eine Übung dazu, wie ein solcher Builder unbemerkt vom echten System wegdriftet.
