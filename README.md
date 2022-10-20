# Java-Swing-Projekt
Java Projekt für AP 3FA091(Gruppe G).

# AbleseBogen

Das Programm ermöglicht eine Einfache und Digitale Eingabe von Daten in den Ablesebogen.
Die Vorteile einer Digitalen Variante des Ablesebogen`s füllt eine ganze Liste:

-    Reduktion des Arbeitsaufwands bei Ablesern und Buchhaltung
-    automatische Plausibilitätsprüfung der Eingaben
-    Überblick über Energieverbrauch auch online
-    Kostenvorteile für Kunden
-    Gewinnsteigerung des Unternehmens
-    später komplette Automatisierung der Ablesung mit Echtzeitdaten möglich
-    vereinfachte Energieberatung, da Daten zentral gesammelt
-    Energieeinsparung durch automatische Steuerung
-    Vernetzung mit Smart Home-Geräten

# Overview

- [Ablesebogen]
- [Repository]
- [Inside the software]
- [License]

# Repository

Zu erst haben wir ein GitHub-Repository erstellt mit der nötigen Lizenz und die jeweiligen "Collaborator" hinzugefügt.
Zum "Committen" bzw. "Pushen" verwenden wir die Software "Smart-Git" und "GitHub-Desktop".
Wir haben uns gegen ein Git-Workflow entschieden und arbeiten somit nur an einem Branch und zwar am "main".
Für die jeweilige Commits haben wir die User Stories mit extra Nummern versehen das man auf einen Blick sieht okay es handel sich um diese User Story.

# Developed with

Für das "Frontend" benutzen wir die "Libary" Java-Swing als einfach benutzbares Java Gui-Toolkit.
Das "Backend" besteht ebenso aus Java.


# Inside the software

Zum laden des normalen Ablesebogen`s haben wir eine Klasse "Ablesebogen", welche zu gleich die "Main Methode" enthält.
Der Ablesebogen ist "Panel" als "BorderLayout" was innerhalb ein "GridLayout", "Menubar" und ein "Panel" mit buttons besitzt. Sobald er eine valide Ablesebogen.JSON/CSV/XML findet versucht er diese durch die load() Methode zu laden.

Zum erstellen eines Ablesebogen`s Element haben wir die Klasse "AbleseEntry" und "AbleseList". AbleseEntry erstellt ein Objekt aus den werten des Ablesebogen. Die AbleseList benutzt als Datenstruktur eine "LinkedList" umso schnell und einfach Elemente zu speichern / löschen.

Wenn man sich nun die erstellten Elemente tabellarisch und übersichtlicht anschauen möchte kann man ganz einfach über den Button "Liste anzeigen", sich eine Tabelle von den Daten öffnen lassen. Für das erstellen dieser übersicht haben wir die Klasse "AbleseOutPanel" und "AbleseTableModel".
AbleseOutPanel generiert ein neuen "JFrame" welcher innerhalb ein "JScrollPane" lädt und darin den "JTable" verwendet. Für die Struktur der Tabelle  wird die Klasse "AbleseTableModel" verwendet, welche von "AbstractTableModel" erbt. 

## License

<div align="center">
  <img src="https://opensource.org/files/osi_keyhole_300X300_90ppi_0.png" width="150" height="150"/>
</div>

Copyright (c) 2022 Benedikt Scheid, Bernhard Hempfer, Florian Kerscher

Permission is hereby granted, free of charge, to any person obtaining a copy of this software and associated documentation files (the "Software"), 
to deal in the Software without restriction, including without limitation the rights to use, copy, modify, merge, publish, distribute, 
sublicense, and/or sell copies of the Software, and to permit persons to whom the Software is furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, 
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, 
DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE 
OR OTHER DEALINGS IN THE SOFTWARE.

