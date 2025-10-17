Thema der Projektarbeit:

# Konzeption und prototypische Umsetzung einer webbasierten Getränke- und Kassenverwaltung

# (WueKaStL) für die Mitarbeiter-Selbstbedienung

# am Rechenzentrum der Universität


Herr Jonas Maier Identnummer 0001117152
E-Mail: maier.j03@gmx.de
Ausbildungsberuf: Fachinformatiker Anwendungsentwicklung
Ausbildungsbetrieb: Rechenzentrum der Julius-Maximilians-Universität Würzburg
Projektbetreuer: Michael Erlenbach
Geplanter Durchführungszeitraum: 17.11.2025 – 17.12.2025
80 Stunden

Projektbeschreibung:
 
 Im Projekt wird die Grundlage für eine interne, webbasierte Getränke‑ und Kassenverwaltung geschaffen.
 Ziel ist es, einen abgestimmten fachlichen und technischen Rahmen zu erarbeiten und einen
 funktionsfähigen Prototypen der Kernprozesse zu erstellen. Mitarbeitende sollen Artikel auswählen und
 Buchungen bestätigen können; die Administration umfasst grundsätzliche Benutzer‑ und Artikelpflege sowie
 Auf‑/Abbuchen. Die Technologie ist festgelegt: serverseitige Java‑Webanwendung mit Servlets und JSP auf
 (Treiber: mariadb-java-client-2.7.12.jar). Das Frontend wird über JSP/CSS/JS (u. a. jQuery 3.7.1) bereitgestellt
 und soll auch perspektivisch in dieser Form weiterverwendet werden.
 
 Der Fokus liegt auf Anforderungsaufnahme, Abgrenzung, Grobkonzept (Use Cases, Daten‑ und
 Architekturentwurf), einem klickbaren bzw. lauffähigen Prototyp der Kernprozesse, grundlegenden Tests
 und einer nachvollziehbaren Projektdokumentation. Eine produktive Einführung (Deployment auf der produktiven
 Tomcat‑Instanz inkl. Datenbank‑Anbindung) ist Bestandteil des Projekts; zusätzlich werden Empfehlungen
 für den anschließenden Betrieb und die Weiterentwicklung ausgearbeitet.
 
 Ergänzende Eckpunkte:
 - Rollen: Mitarbeiter (Buchungen), Administrator (Benutzer/Artikel, Auf‑/Abbuchen).
 - Kernprozesse: Artikelwahl, Warenkorb, Buchungsbestätigung; einfache Buchungsbearbeitung.
 - Sicherheit: PIN‑Authentifizierung, Session‑Management, rollenbasierte Berechtigungen.
 - Datenmodell: Entitäten Benutzer, Artikel, Buchung; Persistenz in MariaDB via JNDI `jdbc/AzubiProjekt_JM-1`.
 - Einführung/Betrieb: Deployment auf Tomcat 9.0.95, DB‑Konfiguration, Basis‑Logging/Backups, Kurzanleitung.

Projektziel:
 
 Bereitstellung eines abgestimmten Grobkonzepts und eines funktionsfähigen Prototyps der Kernprozesse
 mit folgenden Ergebnissen:
 - Abgestimmte Anforderungen, Zielbild und Projektabgrenzung
 - Grobentwurf für Architektur, Datenmodell und Bedienkonzept
 - Prototyp der Kernfunktionen (Artikel wählen, Buchung bestätigen, grundlegende Administration)
 - Testprotokolle, Erkenntnisse und Handlungsempfehlungen
 - Projektdokumentation für die Weiterentwicklung und spätere Einführung
 - Produktive Einführung (Deployment auf Tomcat inkl. DB‑Anbindung, Basis‑Logging/Backups, Go‑Live‑Checkliste)

Projektumfeld:
 
 Die Arbeiten (Planung, Konzeption, Prototyp, Tests, Dokumentation) erfolgen am Arbeitsplatz im
Rechenzentrum der Julius‑Maximilians‑Universität Würzburg in Abstimmung mit dem Projektbetreuer.
 Es wird eine Entwicklungs‑ und Testumgebung eingesetzt; Kollaborations‑ und Dokumentationswerkzeuge
 unterstützen die Durchführung und Nachverfolgung. Die Betriebsumgebung ist festgelegt: Apache Tomcat
 9.0.95 mit konfigurierter JNDI‑DataSource zur MariaDB; Bereitstellung der Oberfläche über JSP/CSS/JS.
 Präsentation, Abnahme und produktive Einführung erfolgen vor Ort.

Zeitplanung:

Planung und Abstimmung 8 Stunden
Ist‑Soll‑Analyse 12 Stunden
Prototypische Umsetzung der Kernfunktionen 28 Stunden
Test und Qualitätssicherung 8 Stunden
Pflegende Einführung, Übergabe und Einweisung 4 Stunden
Dokumentation 8 Stunden

Gesamt: 80 Stunden

Hilfsmittel für Präsentation:

Laptop, Beamer, Tablet.


