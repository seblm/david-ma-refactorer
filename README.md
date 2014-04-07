# David m'a refactorer

## Étape 0 : démarrer le serveur d'ascenseur

Ouvrir nouveau terminal.

    $ java8 && mvn clean compile -f ~/src/david-ma-refactorer && java -classpath ~/src/david-ma-refactorer/target/classes fr.xebia.codeelevator.server.ElevatorServer

Ouvrir une nouvelle tabulation, placer le terminal à droite.

    $ java8 && cd src/code-elevator-dgageot && gco master && clear

Ouvrir un navigateur et le mettre à gauche sur [serveur](http://localhost:8080).

## Étape 1 : le contexte

Aller au commit "1.0 version" et démarrer le serveur.

    $ gco step1
    $ mvn clean install && mvn -f elevator-server jetty:run

Sur le navigateur, enregistrer le client et faire une démo de l'application. Expliquer le plan. Arrêter le serveur.

    $ Ctrl + C
    $ clear

## Étape 2 : implémenter une nouvelle règle pour gérer une taille d'immeuble non fixée

    $ gco step2

 - `BuildingTest` : ajouter un test qui va 3 fois en haut et injecter `BuildingDimensions(0, 2)`
 - Créer la classe `BuildingDimensions`
 - Ajouter `BuildingDimensions` au constructeur de `Building`
    - Dans la valeur par défaut et importer BuildingDimensions
 - Lancer les tests
 - Réparer
    - À partir des tests se rendre sur `updateBuildingState`
    - Constater qu'on utilise ElevatorEngine.LOWER_FLOOR
    - Le supprimer des imports
    - Modifier BuildingDimensions en conséquence

 - Relancer le serveur

    $ mvn clean install && mvn -f elevator-server jetty:run

 - Comment configurer la taille du building dans le cloud ? avec des propriétés système. Exemple :

    $ mvn clean install && mvn -f elevator-server jetty:run -DHIGHER_FLOOR=10

 - Écrire un test
 - Relancer le serveur

    $ mvn clean install && mvn -f elevator-server jetty:run -DHIGHER_FLOOR=10

 - On casse des tests car on ne restaure pas les propriétés système. Importer la `@Rule RestoreSystemProperties`.

 - Relancer le serveur

    $ mvn clean install && mvn -f elevator-server jetty:run -DHIGHER_FLOOR=10

## Étape 3 : plus de multithreading

    $ gitreset && gco step3
    $ mvn clean install && mvn -f elevator-server jetty:run

Rafraîchir le serveur : oups plus d'interface. Il faut rajouter une propriété système pour lire les joueurs.

    $ mvn -f elevator-server jetty:run -DPLAYERS_URL="file:elevator-server/src/test/resources/users.json"

Constater qu'on envoie une requête toutes les secondes.

 - `ElevatorServer` create a new `Thread` from scratch and begin with a call to `playerSynchronizer.run()`
 - `Clock` doesn't use `executorService` anymore (test ?)
 - `HTTPElevator` doesn't use `executorService` anymore and dot the real job at `httpGet()`

    $ mvn clean install && mvn -f elevator-server jetty:run -DPLAYERS_URL="file:elevator-server/src/test/resources/users.json" -DHIGHER_FLOOR=20
