# NimbusNote

NimbusNote ist eine vielseitige Notiz-App, die entwickelt wurde, um deine Notizen zu optimieren und die Produktivität zu steigern.

## Funktionen

- **Notizverwaltung**: Erstelle, bearbeite und organisiere deine Notizen mühelos.
- **Benutzerauthentifizierung**: Registriere dich sicher, melde dich an und verwalte Benutzerkonten.
- **Echtzeit-Wetteraktualisierungen**: Bleibe mit Echtzeit-Wetterdaten für deine gespeicherten Städte auf dem Laufenden.
- **Chat-Funktionalität**: Kommuniziere nahtlos mit anderen Benutzern über den In-App-Chat.
- **Standortbezogene Funktionen**: Greife auf standortbezogene Dienste wie das Abrufen von Wetterdaten basierend auf deinem aktuellen Standort zu.

## Installation

Um NimbusNote zu installieren, befolge diese Schritte:

1. Klone dieses Repository auf deine lokale Maschine.
2. Öffne das Projekt in Android Studio.
3. Baue und führe die Anwendung auf einem Android-Gerät oder -Emulator aus.

## Verwendung

1. **Registrierung/Anmeldung**: Erstelle ein Konto oder melde dich mit deinen vorhandenen Anmeldeinformationen an.
2. **Notizen erstellen**: Erstelle und verwalte deine Notizen mühelos.
3. **Wetteraktualisierungen**: Füge deine Lieblingsstädte hinzu, um Echtzeit-Wetteraktualisierungen zu erhalten.
4. **Chat**: Beteilige dich an Unterhaltungen mit anderen Benutzern über die In-App-Chat-Funktion.

## Verwendete Technologien

- **Android SDK**: Entwicklungsplattform zur Erstellung von Android-Apps.
- **Firebase**: Backend-Services für Authentifizierung, Datenbankverwaltung und Cloud-Speicherung.
- **Google Play-Dienste**: Bietet standortbezogene Dienste und APIs für Android-Apps.

## Mitwirkende

- [Marfcel](https://github.com/MarcelFN88) - Entwickler

## Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert - siehe die [LICENSE](LICENSE)-Datei für Details.

## Danksagungen

Ein herzliches Dankeschön an das Syntax-Team, das mir die Entwicklung mit Android beigebracht hat und mich bei meinem Lernprozess unterstützt hat.

---

## API-Service

Der API-Service bietet Zugriff auf Wetterdaten über die OpenWeatherMap-API.

### Klasse `ApiService`

- Die Klasse stellt Dienste für den Zugriff auf die Wetter-API bereit.
- Basis-URL der OpenWeatherMap-API: `https://api.openweathermap.org/`


### Verwendete Technologien

- Retrofit: Für die Kommunikation mit dem Backend.
- Moshi: JSON-Parser für die Verarbeitung von API-Antworten.

### Schnittstelle `WeatherApiService`

- Definiert die Methoden zur Abfrage von Wetterdaten.

### Repository-Klasse

- Klasse zur Abfrage von Wetterdaten.
- Ruft die Wetterdaten für eine bestimmte Stadt ab.

---

## Fragmente

### WeatherFragment

Das Fragment zeigt und verwaltet Wetterdaten.

- Zeigt eine Liste von Wetterdaten an.
- Ermöglicht das Hinzufügen und Löschen von Städten.
- Erlaubt dem Benutzer, mit anderen Benutzern über den In-App-Chat zu kommunizieren.

### ProfileFragment

Das Fragment zeigt das Benutzerprofil und ermöglicht dem Benutzer, es zu bearbeiten.

- Zeigt Benutzerinformationen an.
- Ermöglicht das Hochladen eines Profilbildes.
- Erlaubt dem Benutzer, seine Daten zu aktualisieren und sich abzumelden.

### NoteFragment

Das Fragment dient zur Anzeige und zum Hinzufügen von Notizen.

- Zeigt eine Liste von Notizen an.
- Ermöglicht das Hinzufügen neuer Notizen.

### LoginFragment

Das Fragment bietet die Anmeldungs- und Registrierungsfunktionalität für Benutzer.

- Erlaubt Benutzern, sich anzumelden oder zu registrieren.
- Unterstützt die Passwort-Wiederherstellungsfunktion.

### ForgotFragment

Das Fragment ermöglicht es Benutzern, ihr Passwort zurückzusetzen.

- Bietet eine Funktion zum Zurücksetzen des Passworts.

### ChatFragment

Das Fragment dient zur Kommunikation zwischen Benutzern über den In-App-Chat.

- Zeigt eine Liste von Chat-Nachrichten an.
- Ermöglicht das Senden neuer Chat-Nachrichten.
