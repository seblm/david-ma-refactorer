# David m'a refactorer

## Étape 0 : démarrer le serveur d'ascenseur

Ouvrir nouveau terminal.

    $ java8 && mvn clean compile -f ~/src/david-ma-refactorer && java -classpath ~/src/david-ma-refactorer/target/classes fr.xebia.codeelevator.server.ElevatorServer

Ouvrir une nouvelle tabulation, placer le terminal à droite.

    $ java8 && cd src/code-elevator-dgageot && git checkout master && clear

Ouvrir un navigateur et le mettre à gauche sur [serveur](http://localhost:8080).

## Étape 1 : le contexte

Aller au commit "1.0 version" et démarrer le serveur.

    $ git checkout 370fb4d
    $ mvn clean install && mvn -f elevator-server jetty:run

Sur le navigateur, enregistrer le client et faire une démo de l'application. Expliquer le plan. Arrêter le serveur.

    $ clear

## step 2 : building size is not fixed

    $ git checkout e73bc508be

 - `BuildingTest` : add a test that goes UP 3 times and inject `BuildingDimensions(0, 2)`
 - repair test by addind `BuildingDimensions` to `Building` constructor
 - cover `Building.floorStates()` with `BuildingDimensions(-1, 4)`
 - verify that `Building` and `BuildingTest` doesn't uses `ElevatorEngine.LOWER_FLOOR` nor `ElevatorEngine.HIGHER_FLOOR`
 - How to launch a test with modified `BuildingDimensions` ? with system properties : write a test
 - When to initialize BuildingDimensions for all ? `ElevatorServer`. Gives this instance to `ElevatorGame` and `RandomUser`
 - Launch server and see that my client is not working

    $ mvn install && mvn -f code-elevator jetty:run -DLOWER_FLOOR=-3 -DHIGHER_FLOOR=2

 - Modify `HTTPElevatorTest` to add `lowerFloor` and `higherFloor` parameters to `reset`
 - Relaunch server and assert that behavior is correct

## step 3 : no more multithread

    $ git checkout da846f724a

 - `ElevatorServer` create a new `Thread` from scratch and begin with a call to `playerSynchronizer.run()`
 - `Clock` doesn't use `executorService` anymore (test ?)
 - `HTTPElevator` doesn't use `executorService` anymore and dot the real job at `httpGet()`

    $ mvn clean install && mvn -f elevator-server jetty:run -DPLAYERS_URL="file:elevator-server/src/test/resources/users.json" -DHIGHER_FLOOR=20
