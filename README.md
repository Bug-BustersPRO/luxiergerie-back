To RUN (en local ou via une image issu de dockerHub)

mvn clean install -DskipTests (à lancer après toute modification du fichier docker-compose)

Si le fichier d'import SQL ne fonctionne pas, pensez à lancer la commande : chmod +r ../src/main/resources/import.sql

docker-compose up --build

To NGINX and dockerHub

1 - Créer un repository sur dockerHub

2 - Adapter le docker-compose.yml pour le nom du repository et le nom du user sur dockerHub
(Nginx.conf déjà configuré)

3 -  Utiliser la commande docker login pour se connecter à dockerHub (sera indiqué dans la console)

4 - Build les images qui seront push sur dockerHub avec la commande : docker buildx build -t bugbstrspro/luxiergerie:main .

5 - Push l'image avec : docker push "bugbstrspro"/luxiergerie:main
Idem pour luxiergerie:dev

6 - Lancer docker-compose up

To GITHUB ACTIONS

1 - Configurer le fichier on-push.yml pour les actions à effectuer lors d'un push

2 - Sur le repository Github, allez dans settings -> secrets and variables -> actions -> new repository secret
et définir les variables d'environnement DOCKERHUB_USERNAME, DOCKERHUB_TOKEN

3 - Push sur le repository Github

4 - En cas d'erreur "Permission denied" sur Github actions, saisissez la commande : chmod +x mvnw pour accorder les permissions d'exécution au fichier mvnw
