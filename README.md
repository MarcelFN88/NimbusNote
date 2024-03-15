# NimbusNote - Android App

## Übersicht

Die Android-App "NimbusNote" ist eine umfassende Plattform, die Funktionalitäten von der Benutzerauthentifizierung, dem Chatten und Notizen machen bis zur Anzeige von Wettervorhersagen umfasst. Nachfolgend finden Sie eine Übersicht über die Kotlin-Dateien und ihre Rollen innerhalb der App.

## Beschreibung der Kotlin-Dateien

- **Adapters**:
  - `ChatAdapter.kt`: Verwaltet die Darstellung von Chat-Nachrichten in einer Liste.
  - `UserAdapter.kt`: Behandelt die Anzeige von Benutzerinformationen und erleichtert Benutzerinteraktionen.
  - `NoteAdapter.kt`: Verantwortlich für die Anzeige von Notizen im Listenformat und ermöglicht so eine einfache Verwaltung und Anzeige von Benutzernotizen.
  - `WeatherAdapter.kt`: Zeigt Wetterinformationen an und wandelt Rohwetterdaten in benutzerfreundliche Formate um.

- **Fragments**:
  - `LoginFragment.kt`: Ermöglicht die Benutzeranmeldung und integriert sich mit der Firebase-Authentifizierung.
  - `ChatFragment.kt`: Unterstützt Messaging-Funktionen und ermöglicht es den Benutzern, in Echtzeit zu kommunizieren.
  - `ForgotFragment.kt`: Bietet einen Mechanismus, damit Benutzer ihr Passwort zurücksetzen können, falls sie es vergessen haben.
  - `NoteFragment.kt`: Ein Bereich, in dem Benutzer ihre persönlichen Notizen erstellen, anzeigen und verwalten können.
  - `WeatherFragment.kt`: Zeigt Wettervorhersagen und Informationen für vom Benutzer interessante Standorte an.
  - `ProfileFragment.kt`: Ermöglicht Benutzern das Anzeigen und Bearbeiten ihres Profils, einschließlich Name, Kontaktinformationen und Profilbild.

## Funktionen

- Echtzeit-Chat-Funktionalität.
- Benutzerauthentifizierung einschließlich Anmeldung, Registrierung und Passwortrücksetzung.
- Notizverwaltungssystem zum Erstellen und Organisieren persönlicher Notizen.
- Anzeige von Wettervorhersagen mit detaillierten Wetterinformationen.
- Benutzerprofilanpassung und -verwaltung.

## Einrichtung

Um dieses Projekt in Ihrer Entwicklungsumgebung einzurichten:

1. Klonen Sie das Repository auf Ihren lokalen Computer.
2. Öffnen Sie das Projekt in Android Studio.
3. Konfigurieren Sie Firebase mit dem Projekt, indem Sie Ihre Firebase-Projektkonfigurationsdatei (`google-services.json`) im Verzeichnis `/app` hinzufügen.
4. Erstellen Sie das Projekt und führen Sie es auf einem Android-Gerät oder -Emulator aus.

## Abhängigkeiten

Dieses Projekt verwendet die folgenden wichtigen Abhängigkeiten:

- Firebase-Authentifizierung zur Verwaltung der Benutzeranmeldung und -registrierung.
- Firebase Firestore für die Speicherung und Abfrage von Echtzeitdaten.
- Firebase Storage zum Speichern und Abrufen von Benutzerprofilbildern.
- Retrofit für Netzwerkanfragen zum Abrufen von Wetterdaten.

## Lizenz

Dieses Projekt ist unter der MIT-Lizenz lizenziert - siehe die LICENSE-Datei für Details.

## Zusätzliche Kotlin-Dateien Beschreibung

...

## Core Components Description

...

