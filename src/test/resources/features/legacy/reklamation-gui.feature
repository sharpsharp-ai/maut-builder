# language: de
@legacy
Funktionalität: Reklamation einer Rechnung – jeder Schritt über die Oberfläche
  Als Sachbearbeiter:in in der Abrechnung
  möchte ich eine berechtigte Reklamation vollständig über die drei Oberflächen stornieren,
  damit Kund:innen nichts für eine defekte OBU bezahlen.

  Szenario: Vollreklamation wegen defekter OBU storniert die Rechnung und erzeugt eine Sofortrechnung mit Guthaben
    # Kundenportal: Kunde anlegen, OBU einbauen, defekt melden, Schaden erfassen
    Angenommen ich öffne das Kundenportal
    Und ich lege den Kunden "Spedition Muster GmbH" an
    Und ich merke mir die angezeigte Kundennummer
    Und ich installiere die OBU mit dem Kennzeichen "M-SH 1234"
    Und ich melde die OBU als defekt
    Dann sehe ich den OBU-Status "DEFEKT"
    Wenn ich den Schaden "Ersatz-OBU" mit dem Preis "120,00" erfasse
    Dann sehe ich die Meldung "Schaden erfasst: Ersatz-OBU, 120,00 €"

    # Backend: Rechnungslauf anlegen, starten, freigeben
    Wenn ich das Backend öffne
    Und ich lege einen neuen Rechnungslauf an
    Und ich starte den Rechnungslauf
    Dann sehe ich den Rechnungslauf mit dem Status "BUCHUNGSBEREIT"
    Wenn ich den Rechnungslauf freigebe
    Dann sehe ich den Rechnungslauf mit dem Status "GEBUCHT"
    Und sehe ich, dass der Rechnungslauf 1 Rechnung enthält

    # Kundenportal: die entstandene Rechnung finden und ihre Nummer merken
    Wenn ich das Kundenportal öffne
    Dann sehe ich in den Rechnungen eine Rechnung der Art "RECHNUNG" mit dem Status "OFFEN" und dem Betrag "120,00 €"
    Und ich merke mir die Nummer der Rechnung der Art "RECHNUNG"

    # Kundenmanagement: Korrespondenz erfassen, Vorgang öffnen, Reklamation entscheiden
    Wenn ich das Kundenmanagement öffne
    Und ich trage die Kundennummer und die Rechnungsnummer in die Korrespondenz ein
    Und ich sende die Korrespondenz an die Tasklist Abrechnungen
    Dann sehe ich in der Tasklist den Vorgang zur Rechnung mit dem Status "EINGEGANGEN"
    Wenn ich den Vorgang zur Rechnung über den Button Öffnen öffne
    Dann sehe ich den Bereich Reklamation
    Wenn ich das Ergebnis "Vollstorno" wähle
    Und ich entscheide die Reklamation
    Dann sehe ich den Vorgangsstatus "ERLEDIGT"
    Und sehe ich in den Rechnungen eine Rechnung der Art "RECHNUNG" mit dem Status "STORNIERT" und dem Betrag "120,00 €"
    Und sehe ich in den Rechnungen eine Rechnung der Art "SOFORTRECHNUNG" mit dem Status "OFFEN" und dem Betrag "-120,00 €"

    # Kundenportal: der Kunde sieht dasselbe Ergebnis
    Wenn ich das Kundenportal öffne
    Dann sehe ich in den Rechnungen eine Rechnung der Art "RECHNUNG" mit dem Status "STORNIERT" und dem Betrag "120,00 €"
    Und sehe ich in den Rechnungen eine Rechnung der Art "SOFORTRECHNUNG" mit dem Status "OFFEN" und dem Betrag "-120,00 €"
