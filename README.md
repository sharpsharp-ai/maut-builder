# maut-builder – schnelle Akzeptanztests mit Testdaten-Buildern

Übungsrepo für das CSD-Training. Es zeigt, wie man eine langsame, workflow-getriebene
Akzeptanztestsuite schnell macht, ohne das Vertrauen in die echten Abläufe zu verlieren.

Fachlicher Kontext (stark vereinfacht): Ein Kunde hat eine defekte On-Board-Unit (OBU).
Der Schaden wird als Entgelt erfasst, ein Rechnungslauf erzeugt daraus eine Rechnung.
Der Kunde reklamiert, die Abrechnung entscheidet auf Vollstorno: Die Rechnung wird storniert,
und der Kunde erhält eine Sofortrechnung mit Guthaben.

## Voraussetzungen

Java 21 und Maven. Für die E2E-Tests zusätzlich Google Chrome. Fehlt Chrome, lädt der
Selenium Manager beim ersten Lauf automatisch eine passende Version herunter; in Netzen mit
Proxy oder ohne Internet Chrome vorher installieren.

## Befehle

| Befehl | Was passiert | Dauer (ca.) |
|---|---|---|
| `mvn test` | Unit-, Paritäts- und Akzeptanztests | Sekunden |
| `mvn verify` | zusätzlich E2E-Tests über die GUI | + 15–20 s |
| `mvn verify -De2e.sichtbar=true` | E2E mit sichtbarem Browser | |
| `mvn verify -Dmaut.batch.latenz=5000` | E2E mit langsamerem Batch | |
| `mvn compile exec:java` | Anwendung starten, dann http://localhost:7070/portal.html | |

## Eine Spezifikation, drei Ausführungswege

Es gibt genau **eine** Feature-Datei: `src/test/resources/features/reklamation.feature`.
Drei Runner führen sie mit unterschiedlichem Glue-Code aus:

| Runner | Glue | Ausgangslage entsteht durch |
|---|---|---|
| `AkzeptanzMitFaktenBuilderTest` | `akzeptanz.gemeinsam` + `akzeptanz.fakten` | **Variante 1:** Builder erzeugt Fakten, der echte Rechnungslauf rechnet (in-memory, ohne Warten) |
| `AkzeptanzMitEndzustandBuilderTest` | `akzeptanz.gemeinsam` + `akzeptanz.endzustand` | **Variante 3:** Builder legt die fertige offene Rechnung direkt im Speicher ab |
| `E2EIT` | `e2e` | Selenium klickt Kundenportal, Backend und Kundenmanagement durch, mit echter Wartezeit |

Das funktioniert nur, weil die Schritte fachlich formuliert sind. Ein Schritt wie
„I click the BBY sub menu item RECHNUNGSLAEUFE“ ließe sich ohne GUI gar nicht umsetzen.

## Warum zwei Builder-Varianten?

- **Variante 1 (`testdaten.fakten.KundenszenarioBuilder`)** baut nur Eingangsfakten und ruft
  die echten Use Cases auf. Die Rechnung berechnet der Produktionscode. Sie *kann* nicht
  wegdriften. Der Trick: Die Hintergrundverarbeitung ist ein Port
  (`core.port.out.Hintergrundverarbeitung`); im Test läuft sie sofort und synchron.
- **Variante 3 (`testdaten.endzustand.OffeneRechnungBuilder`)** legt den Endzustand direkt
  ab. Das ist nötig, wenn der echte Weg im Test nicht erreichbar ist (z. B. Rechnungen aus
  einem Fremdsystem). Er kann wegdriften und wird deshalb vom
  `RechnungBuilderParitaetTest` abgesichert: Echter Workflow und Builder müssen fachlich
  dasselbe ergeben (`testdaten.FachlicheSicht` legt fest, was verglichen wird).

## Übung: Drift erleben

Der Rechnungslauf soll künftig jeder Rechnung eine Bearbeitungsgebühr von 2,50 € hinzufügen.
Baue das in `RechnungslaufService.entwurfErstellen` ein und führe `mvn test` aus.

1. Welche Tests werden rot, welche bleiben grün?
2. Warum ist der grüne Akzeptanztest gefährlich?
3. Was muss jetzt angepasst werden: die Spezifikation, der Builder oder beides?

## Aufbau

```
core/domain               Kunde, Obu, Entgelt, Rechnung, Rechnungslauf, Korrespondenz
core/port/in              Use Cases: KundenVerwaltung, Rechnungslaeufe, Abrechnung
core/port/out             Repositories, Hintergrundverarbeitung
core/service              Umsetzung der Use Cases
adapter/in/web            Javalin: REST-API und drei statische Oberflächen
adapter/out/speicher      In-Memory-Repositories
adapter/out/verarbeitung  Sofortige (Test) und verzögerte (echt) Hintergrundverarbeitung
```

Bewusste Vereinfachungen gegenüber dem echten System: kein Login, ein Fahrzeug pro Kunde,
keine Stornorechnung als eigener Beleg, keine SAP-Anbindung, Speicherung nur im Arbeitsspeicher.
