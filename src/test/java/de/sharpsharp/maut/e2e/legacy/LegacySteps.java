package de.sharpsharp.maut.e2e.legacy;

import io.cucumber.java.de.Angenommen;
import io.cucumber.java.de.Dann;
import io.cucumber.java.de.Und;
import io.cucumber.java.de.Wenn;
import org.junit.Assert;

/**
 * Schritte der Legacy-Feature-Datei. Jeder Schritt ist eine Handlung oder Beobachtung
 * auf genau einem Bildschirm. Die Schlüsselwörter Angenommen/Und/Wenn/Dann dienen nur der Lesbarkeit.
 */
public class LegacySteps {

    private final LegacyKontext k;

    public LegacySteps(LegacyKontext kontext) {
        this.k = kontext;
    }

    // ---------- Navigation ----------

    @Angenommen("ich öffne das Kundenportal")
    @Wenn("ich das Kundenportal öffne")
    public void ichOeffneDasKundenportal() {
        k.oeffne("portal");
    }

    @Wenn("ich das Backend öffne")
    public void ichOeffneDasBackend() {
        k.oeffne("backend");
    }

    @Wenn("ich das Kundenmanagement öffne")
    public void ichOeffneDasKundenmanagement() {
        k.oeffne("km");
    }

    // ---------- Kundenportal ----------

    @Und("ich lege den Kunden {string} an")
    public void ichLegeDenKundenAn(String name) {
        k.tippe("name", name);
        k.klicke("kunde-anlegen");
        k.warteBis("Kundennummer ist vergeben", b -> k.text("kundennummer").startsWith("K-"));
    }

    @Und("ich merke mir die angezeigte Kundennummer")
    public void ichMerkeMirDieKundennummer() {
        k.kundennummer = k.text("kundennummer");
    }

    @Und("ich installiere die OBU mit dem Kennzeichen {string}")
    public void ichInstalliereDieObu(String kennzeichen) {
        k.tippe("kennzeichen", kennzeichen);
        k.klicke("obu-installieren");
        k.sieheText("obu-status", "INSTALLIERT");
    }

    @Und("ich melde die OBU als defekt")
    public void ichMeldeDieObuAlsDefekt() {
        k.klicke("obu-defekt-melden");
    }

    @Dann("sehe ich den OBU-Status {string}")
    public void sehIchDenObuStatus(String status) {
        k.sieheText("obu-status", status);
    }

    @Wenn("ich den Schaden {string} mit dem Preis {string} erfasse")
    public void ichErfassDenSchaden(String material, String preis) {
        k.tippe("material", material);
        k.tippe("preis", preis);
        k.klicke("schaden-erfassen");
    }

    @Dann("sehe ich die Meldung {string}")
    public void sehIchDieMeldung(String meldung) {
        k.sieheText("schaden-meldung", meldung);
    }

    // ---------- Backend ----------

    @Und("ich lege einen neuen Rechnungslauf an")
    public void ichLegeEinenNeuenRechnungslaufAn() {
        k.klicke("neuer-lauf");
        k.sieheText("lauf-status", "ANGELEGT");
    }

    @Und("ich starte den Rechnungslauf")
    public void ichStarteDenRechnungslauf() {
        k.klicke("lauf-starten");
    }

    @Wenn("ich den Rechnungslauf freigebe")
    public void ichGebeDenRechnungslaufFrei() {
        k.klicke("lauf-freigeben");
    }

    @Dann("sehe ich den Rechnungslauf mit dem Status {string}")
    public void sehIchDenRechnungslaufMitDemStatus(String status) {
        k.sieheText("lauf-status", status);
    }

    @Und("sehe ich, dass der Rechnungslauf {int} Rechnung enthält")
    public void sehIchDassDerRechnungslaufRechnungenEnthaelt(int anzahl) {
        k.sieheText("lauf-rechnungen", String.valueOf(anzahl));
    }

    // ---------- Rechnungen (Kundenportal und Kundenmanagement) ----------

    @Dann("sehe ich in den Rechnungen eine Rechnung der Art {string} mit dem Status {string} und dem Betrag {string}")
    public void sehIchInDenRechnungenEineRechnung(String art, String status, String betrag) {
        k.sieheRechnung(art, status, betrag);
    }

    @Und("ich merke mir die Nummer der Rechnung der Art {string}")
    public void ichMerkeMirDieNummerDerRechnung(String art) {
        k.rechnungsnummer = k.rechnungsnummerDerArt(art);
    }

    // ---------- Kundenmanagement ----------

    @Und("ich trage die Kundennummer und die Rechnungsnummer in die Korrespondenz ein")
    public void ichTrageDieNummernEin() {
        k.tippe("korr-kundennummer", k.kundennummer);
        k.tippe("korr-rechnungsnummer", k.rechnungsnummer);
    }

    @Und("ich sende die Korrespondenz an die Tasklist Abrechnungen")
    public void ichSendeDieKorrespondenz() {
        k.klicke("korrespondenz-senden");
    }

    @Dann("sehe ich in der Tasklist den Vorgang zur Rechnung mit dem Status {string}")
    public void sehIchInDerTasklist(String status) {
        k.sieheVorgangsstatusInTasklist(status);
    }

    @Wenn("ich den Vorgang zur Rechnung über den Button Öffnen öffne")
    public void ichOeffneDenVorgang() {
        k.oeffneVorgang();
    }

    @Dann("sehe ich den Bereich Reklamation")
    public void sehIchDenBereichReklamation() {
        k.warteBis("Bereich Reklamation ist sichtbar", b -> k.reklamationSichtbar());
    }

    @Wenn("ich das Ergebnis {string} wähle")
    public void ichWaehleDasErgebnis(String ergebnis) {
        k.waehle("ergebnis", ergebnis);
    }

    @Und("ich entscheide die Reklamation")
    public void ichEntscheideDieReklamation() {
        k.klicke("reklamation-anlegen");
    }

    @Dann("sehe ich den Vorgangsstatus {string}")
    public void sehIchDenVorgangsstatus(String status) {
        k.sieheText("vorgang-status", status);
    }
}
