// Hilfsfunktionen, die alle drei Oberflächen nutzen.

async function api(methode, pfad, daten) {
    const antwort = await fetch(pfad, {
        method: methode,
        headers: { "Content-Type": "application/json" },
        body: daten === undefined ? undefined : JSON.stringify(daten)
    });
    const text = await antwort.text();
    const inhalt = text ? JSON.parse(text) : null;
    if (!antwort.ok) {
        throw new Error(inhalt && inhalt.meldung ? inhalt.meldung : "Anfrage fehlgeschlagen: " + antwort.status);
    }
    return inhalt;
}

function betragText(betrag) {
    return Number(betrag).toFixed(2).replace(".", ",") + " €";
}

function zeigeFehler(fehler) {
    document.getElementById("fehler").textContent = fehler ? fehler.message : "";
}

// Ruft `aktion` auf und zeigt einen Fehler an, statt ihn zu verschlucken.
function beiKlick(id, aktion) {
    document.getElementById(id).addEventListener("click", async () => {
        zeigeFehler(null);
        try {
            await aktion();
        } catch (fehler) {
            zeigeFehler(fehler);
        }
    });
}

// Zeichnet eine Tabelle nur neu, wenn sich die Daten geändert haben (ruhige Oberfläche, stabile Tests).
function tabelleAktualisieren(tbody, eintraege, zeileBauen) {
    const neu = JSON.stringify(eintraege);
    if (tbody.dataset.stand === neu) {
        return;
    }
    tbody.dataset.stand = neu;
    tbody.replaceChildren(...eintraege.map(zeileBauen));
}

function zelle(text, klasse) {
    const td = document.createElement("td");
    td.textContent = text;
    if (klasse) {
        td.className = klasse;
    }
    return td;
}

function rechnungsZeile(rechnung) {
    const tr = document.createElement("tr");
    tr.dataset.nummer = rechnung.nummer;
    tr.append(zelle(rechnung.nummer, "nummer"), zelle(rechnung.art, "art"),
        zelle(rechnung.status, "status"), zelle(betragText(rechnung.betrag), "betrag"));
    return tr;
}

function alleSekunde(aufgabe) {
    aufgabe();
    setInterval(aufgabe, 1000);
}
