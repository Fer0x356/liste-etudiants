# Liste etudiants
## Qu'est-ce que c'est ?

C'est une application Java avec JavaFX et Hibernate qui permet de lister, modifier et supprimer une base de données d'étudiants, ainsi que de stocker leur score au jeu snake inclut dans le jeux.

## Setup :

Il faut mysql d'installé, vous pourrez modifier dans ```src/main/resources/hibernate.cfg.xml``` le nom d'utilisateur et le mot de passe de la base de données à utiliser.
Il faut une database "etudiants_db", et dedans une table avec ces attributs :
| Field       | Type         | Null | Key | Default | Extra          |
|-------------|--------------|------|-----|---------|----------------|
| id          | bigint(20)   | NO   | PRI | NULL    | auto_increment |
| nom         | varchar(100) | NO   |     | NULL    |                |
| prenom      | varchar(100) | NO   |     | NULL    |                |
| score_snake | int(11)      | YES  |     | 0       |                |


Il faut les paquets JavaFX d'installés : https://gluonhq.com/products/javafx/ 
et de liés à IntelliJ dans ```File > Project Structure > Librairies```.

De préférence les options de run de la JVM : ```--module-path path/to/javafx-sdk-24.0.1/lib --add-modules javafx.controls,javafx.fxml --enable-native-access=javafx.graphics --add-opens=java.base/sun.misc=ALL-UNNAMED -Dprism.marlinlog=false```.

Il faut aussi ```git lfs``` d'installé.

### Disclaimer :

C'est un repo de test et d'entraînement, je le met en public car ça m'amuse de le mettre en public (et pour pouvoir le partager à n'importe qui sans que ce soit embêtant). Donc l'application est totalement inutile, n'y portez pas attention si vous cherchez un outil pratique :).
