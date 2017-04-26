# NeoVentus #

### Beschreibung ###

In diesem Softwareprojekt soll ein Konzept und ein Prototyp für eine Mobile App erstellt werden, die für Gastronomiebetriebe eine POS-Funktionalität bietet.
Bars und Restaurants nutzen neben fest installierten elektronischen POS-Systemen mobile Endgeräte, um Bestellungen zuverlässig und effizient entgegenzunehmen. Nach der Bestellaufnahme erfolgt die Zubereitung des entsprechenden Essens oder Getränks, welche dann serviert werden können. Die aufgenommenen Bestelldaten können zentral gedruckt werden und mit dem Abkassieren endet der Geschäftsprozess des Gastronomiebetriebes. Üblicherweise werden spezielle mobile POS-Geräte verwendet. Smartphones (Geräte mit Betriebssystemen wie Android, iOS, Mozilla ...) oder Musikabspielgeräten (z. B. iPod) sind heutzutage weit verbreitet. Diese bieten alle nötigen Funktionen, um per Touchsreen Bestellungen aufzunehmen und per drahtloser Schnittstelle Daten an  einen Server (zentrale POS-Komponente) zu übermitteln, wenn eine entsprechende App verfügbar ist.  Für Gastronomiemanager ergeben sich zwei interessante Alternativen: Sie können die Kosten der mobilen POS-Geräte spraren, weil Mitarbeiter ihre eigenen Smartphones nutzen oder sie können Mitarbeiter motivieren, indem Gastronomiemanager ihren Mitarbeitern Smartphones oder Musikabspielgeräte als Perks auch für deren Freizeit überlassen. Die App-Store Konzepte der Betriebssystemhersteller ermöglichen den einfachen Vertrieb und das einfache Deployment der Apps.

Ziel diese Projektes ist es, in einem anerkannten Softwareengineering-Prozess, ein umfassendes Konzept und einen Prototypen für eine soche App zu erstellen. Das Konzept sollte wirtschaftliche Aspekte berücksichtigen. Eine Integration in ein Gastronomie-Gesamtsystem (POSServer oder ERP) sollte dabei am Rande, aber nicht vordergründig betrachtet werden (ggf. über Schnittstellendefinitionen).

### Links ###
[Trello](https://trello.com/dvprojekt)

[Drive](https://drive.google.com/drive/folders/0B2XRjr3mZqCbMFB5MzM5VFhmOGs)

[Teamkalender](https://calendar.google.com/calendar/embed?src=olaemoefk157tamjupikrnrkt4%40group.calendar.google.com&ctz=Europe/Berlin)

### Einrichtung Entwicklungsumgebung ###

* Installation [IntelliJ Ultimate 2017](https://www.jetbrains.com/idea/download/) ([JetBrains Student](https://www.jetbrains.com/student/) Account nötig)
* Installation [Git](https://git-scm.com/downloads).
* IntelliJ starten, *Check out from Version Control*, *Git*
* Git Repository URL: *https://name@bitbucket.org/timperator/neoventus* Clone
* Eventuell muss zunächst die Java SDK 1.8 hinzugefügt werden
* Zum Anzeigen des Projektexplorers Doppelklick auf den Projektnamen links oben
* View *Maven Projects* öffnen (Doppel Shift, nach Maven Projects suchen)
* Verzeichnis *neoventus/backend/src/main/java* als *Sources Root* markieren
* Verzeichnis *neoventus/backend/src/test* als *Test Sources Root*
* Verzeichnis *neoventus/backend/src/main/resources* als *Resources Root* markieren
* Maven Einstellungen: File -> Settings -> Build, Execution, Deployment -> Build Tools -> Maven
* Maven: [x] *Always update snapshots*
* Maven/Importing: [x] *Import Maven projects automatically*
* In View *Maven Projects* -> *Reimport All Maven Projects*. Nun sollten automatisch alle in der *pom.xml* deklarierten Dependencies heruntergeladen werden und im Ordner *External Libraries* auftauchen (Unter Windows entspricht das dem Ordner *C:\Users\User\.m2\repository*).
* Falls die Dependencies bereits heruntergeladen wurden und unter External Libraries angezeigt werden, in den Sourcen allerdings nicht gefunden werden, hilft: File -> Invalidate Caches
* Frontend:
    * [node.js](https://nodejs.org/en/) herunterladen
    * ```npm install -g ionic cordova``` im Terminal ausführen
    * ``` npm install``` im Terminal ausführen
    * Zum Starten der App aus dem Terminal in /frontend wechseln und ```ionic serve``` eingeben


### Coding Guide Lines ###
* Settings -> Editor -> Code Style -> [x] Enable EditorConfig support
* Settings -> Editor -> Code Style -> Java -> [x] Use tab character
* Settings -> Editor -> File and Code Templates -> Includes -> File Header:
```
/**
 * Class description
 *
 * @author ${USER}
 * @version 0.0.1
 */
```

* Ständige Entwicklung der Unittests
* einheitlicher Klassenaufbau
* Vor dem Commit **alle UnitTest** durchlaufen lassen
* Changelog kurz und knapp im Javadoc der Klasse führen
* Standard Klassennamen (UpperCamelCase) und Variablennamen (lowerCamelCase)
* Vor dem Commit Code Reformat zulassen und Imports optimieren
* Sämtliche Namen, Kommentare und Javadoc auf Englisch
