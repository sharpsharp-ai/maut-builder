# language: de
Funktionalität: Reklamation einer Rechnung
  Als Sachbearbeiter:in in der Abrechnung
  möchte ich eine berechtigte Reklamation vollständig stornieren,
  damit Kund:innen nichts für eine defekte OBU bezahlen.

  Szenario: Vollreklamation wegen defekter OBU storniert die Rechnung und erzeugt eine Sofortrechnung mit Guthaben
    Angenommen ein Kunde mit einer defekten OBU
    Und dem Kunden wurden 120,00 € für die defekte OBU in Rechnung gestellt
    Wenn der Kunde die Rechnung vollständig reklamiert
    Dann ist die ursprüngliche Rechnung storniert
    Und der Kunde erhält eine offene Sofortrechnung mit 120,00 € Guthaben
