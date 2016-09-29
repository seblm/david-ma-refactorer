# David m'a refactorer

## Étape 0 : démarrer le serveur d'ascenseur

Ouvrir nouveau terminal.

    $ java8 && mvn clean compile -f ~/src/david-ma-refactorer && java -classpath ~/src/david-ma-refactorer/target/classes fr.xebia.codeelevator.server.ElevatorServer

Ouvrir un nouvel onglet, placer le terminal à droite.

    $ java8 && cd src/code-elevator-dgageot && gitreset && clear

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

 - `ElevatorServer` doit passer d'un mode toutes les secondes à un nouveau Thread qui poutre.

Puis se rendre sur `clock.tick()` et constater qu'il est lui aussi asynchrone : on re-synchronise tout ça.
On supprime l'`ExecutorService` : c'est `HTTPElevator` qui ne compile plus car il utilisait également
l'`executorService`. C'est le troisième niveau d'asynchronisme : les empilages/dépilages de requêtes HTTP. On vire tout
ça.

    $ mvn clean install && mvn -f elevator-server jetty:run -DPLAYERS_URL="file:elevator-server/src/test/resources/users.json" -DHIGHER_FLOOR=20

## Étape 4 : bon mais finalement si

    $ gitreset && gco step4

Un peu d'API concurrente :

Créer des `Callable<Void>` dans une liste, puis un `executor.invokeAll()` qui est un `Executors.newFixedThreadPool(20)`.

## Étape 5 : suppression de projets

    $ gitreset && gco step5

On supprime les deux modules inutiles `elevator-test` et `elevator-participant` du pom puis physiquement. On supprime l'UI, les implémentations de ElevatorEngine dans le code de production et dans le code de test, on rend HTTPElevator public, on le bouge dans core, on inline ElevatorEngine. On supprime le code web. On compte le nombre de classes java en moins :

    $ tree | grep "\.java" | wc -l

## Étape 6 : quick win

    $ gitreset && gco step6

`UsersFromStream` n'a pas besoin des getters/setters pour fonctionner.

## Étape 7 : Woot

    $ gitreset && gco step7

On passe en java8, on enlève toutes les dépendances à jaxrs, servlet, jetty, jackson. On compile. Il y a des corrections à faire dans
 - ScoresPersister
 - WebResource
 - PlayerNotFound : on vire
 - renommer ElevatorApplication en main :)

$ mvn clean install && mvn assembly:single && mkdir deploy && cp target/distribution.zip deploy && cd deploy && unzip distribution.zip && java -classpath 'classes:lib/*' -Dhttp.disable.classpath=true -DPROD_MODE=true -DPLAYERS_URL=file:../src/test/resources/users.json -DLOWER_FLOOR=-3 -DHIGHER_FLOOR=25 -DCABIN_SIZE=42 elevator.server.Main