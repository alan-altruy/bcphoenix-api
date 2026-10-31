# BC Phoenix API

API REST en Java (Spring Boot) pour un club de bowling : affiliés, lieux, événements et scores, avec un accès public en lecture et un espace réservé aux administrateurs.

> **État du projet :** V1 en cours de développement, pas encore mise en ligne.

## Technologies

- Java 21
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- Spring Security (session par cookie, mots de passe hachés avec BCrypt)
- MySQL
- Springdoc OpenAPI (Swagger UI)
- Maven (via le wrapper `mvnw`, pas besoin d'installer Maven)

## Prérequis

- JDK 21
- Un serveur MySQL qui tourne en local (port 3306 par défaut)

## Installation

1. Cloner le projet :

   ```bash
   git clone https://github.com/JasonFouret/bcphoenix.git
   cd bcphoenix
   ```

2. Créer la base et toutes les tables avec le script [doc/schema.sql](doc/schema.sql) :

   ```bash
   mysql -u root -p < doc/schema.sql
   ```

3. Donner les identifiants MySQL via des variables d'environnement
   (ils ne sont **pas** écrits dans le code, pour ne pas les publier sur GitHub) :

   | Variable      | Rôle                    | Valeur par défaut                          |
   |---------------|-------------------------|--------------------------------------------|
   | `DB_URL`      | URL JDBC de la base     | `jdbc:mysql://localhost:3306/bcphoenix`    |
   | `DB_USERNAME` | Utilisateur MySQL       | `root`                                     |
   | `DB_PASSWORD` | Mot de passe MySQL      | *(vide)*                                   |

   Sous Windows (PowerShell) :

   ```powershell
   $env:DB_PASSWORD = "ton_mot_de_passe"
   ```

   Dans IntelliJ : *Run > Edit Configurations > Environment variables*.

4. Créer le premier administrateur. `doc/schema.sql` ne crée aucun compte ni aucun rôle :
   - ajouter un rôle nommé exactement `ADMIN` dans la table `roles` ;
   - ajouter un utilisateur lié à ce rôle dans `users`, avec son email en minuscules et un hash BCrypt dans `password_hash`.

   Ne jamais mettre de mot de passe ou de hash dans le dépôt.

## Lancer l'application

```bash
./mvnw spring-boot:run
```

Sous Windows : `mvnw.cmd spring-boot:run`

L'API démarre sur http://localhost:8080.

## Connexion

L'API utilise une **session par cookie**. L'identifiant de connexion est l'email.

| Méthode | URL                | Description                                                  |
|---------|--------------------|--------------------------------------------------------------|
| POST    | `/api/auth/login`  | Se connecter, avec les champs de formulaire `email` et `password` |
| POST    | `/api/auth/logout` | Se déconnecter                                               |

Ces deux routes répondent `204` en cas de succès, et `/api/auth/login` répond `401` si les identifiants sont incorrects.

Les requêtes `POST`, `PUT` et `DELETE` sont protégées contre le CSRF : le client doit lire le cookie `XSRF-TOKEN` et renvoyer sa valeur dans l'en-tête `X-XSRF-TOKEN`. Le jeton change après la connexion, il faut donc le relire avant chaque requête.

| Code | Signification                                |
|------|----------------------------------------------|
| 401  | Pas connecté, ou identifiants incorrects     |
| 403  | Accès refusé : rôle insuffisant ou jeton CSRF absent |

## Documentation de l'API

Une fois l'application lancée, la documentation interactive (Swagger UI) est disponible ici :
http://localhost:8080/swagger-ui.html

Swagger envoie automatiquement le jeton CSRF. Il reste ouvert pour le développement et devra être désactivé en production.

### Routes publiques

Accessibles sans connexion, en lecture seule.

| Méthode | URL                     | Description                 |
|---------|-------------------------|-----------------------------|
| GET     | `/api/affiliates`       | Liste des affiliés          |
| GET     | `/api/affiliates/{id}`  | Détail d'un affilié         |
| GET     | `/api/events`           | Liste des événements        |
| GET     | `/api/events/{id}`      | Détail d'un événement       |
| GET     | `/api/games`            | Liste des parties           |
| GET     | `/api/games/{id}`       | Détail d'une partie         |
| GET     | `/api/locations`        | Liste des lieux             |
| GET     | `/api/locations/{id}`   | Détail d'un lieu            |

### Routes du compte connecté

Accessibles à toute personne connectée.

| Méthode | URL                     | Description                      |
|---------|-------------------------|----------------------------------|
| GET     | `/api/account/me`       | Informations du compte connecté  |
| PUT     | `/api/account/password` | Changer son mot de passe         |

### Routes d'administration

Réservées au rôle `ADMIN`.

| Méthode | URL                           | Description                                 |
|---------|-------------------------------|---------------------------------------------|
| GET     | `/api/admin/affiliates`       | Liste des affiliés avec toutes leurs données |
| GET     | `/api/admin/affiliates/{id}`  | Détail complet d'un affilié                 |
| POST    | `/api/admin/affiliates`       | Créer un affilié                            |
| PUT     | `/api/admin/affiliates/{id}`  | Modifier un affilié                         |
| DELETE  | `/api/admin/affiliates/{id}`  | Supprimer un affilié                        |
| POST    | `/api/admin/events`           | Créer un événement                          |
| PUT     | `/api/admin/events/{id}`      | Modifier un événement                       |
| DELETE  | `/api/admin/events/{id}`      | Supprimer un événement                      |
| GET     | `/api/admin/games`            | Liste des parties avec les données complètes |
| GET     | `/api/admin/games/{id}`       | Détail complet d'une partie                 |
| POST    | `/api/admin/games`            | Créer une partie                            |
| PUT     | `/api/admin/games/{id}`       | Modifier une partie                         |
| DELETE  | `/api/admin/games/{id}`       | Supprimer une partie                        |
| POST    | `/api/admin/locations`        | Créer un lieu                               |
| PUT     | `/api/admin/locations/{id}`   | Modifier un lieu                            |
| DELETE  | `/api/admin/locations/{id}`   | Supprimer un lieu                           |
| GET     | `/api/admin/roles`            | Liste des rôles                             |
| GET     | `/api/admin/roles/{id}`       | Détail d'un rôle                            |
| POST    | `/api/admin/roles`            | Créer un rôle                               |
| PUT     | `/api/admin/roles/{id}`       | Modifier un rôle                            |
| DELETE  | `/api/admin/roles/{id}`       | Supprimer un rôle                           |
| GET     | `/api/admin/users`            | Liste des utilisateurs                      |
| GET     | `/api/admin/users/{id}`       | Détail d'un utilisateur                     |
| POST    | `/api/admin/users`            | Créer un utilisateur                        |
| PUT     | `/api/admin/users/{id}`       | Modifier un utilisateur (sans son mot de passe) |
| DELETE  | `/api/admin/users/{id}`       | Supprimer un utilisateur                    |

## Tests

```bash
./mvnw test
```

> Le test actuel démarre toute l'application : il a besoin que MySQL soit lancé.

## Structure du projet

```
src/main/java/com/bcphoenix/
├── config/         # Configuration générale (hachage des mots de passe)
├── controller/     # Points d'entrée HTTP (les URLs de l'API)
│   ├── account/    # Routes de la personne connectée
│   ├── admin/      # Routes réservées aux administrateurs
│   └── visitor/    # Routes publiques en lecture seule
├── dto/            # Objets reçus / renvoyés par l'API
│   ├── request/    # Données reçues
│   └── response/   # Données renvoyées (admin/, visitor/ ou communes)
├── exception/      # Gestion centralisée des erreurs
├── model/          # Entités = tables de la base
├── repository/     # Accès à la base de données (Spring Data JPA)
├── security/       # Règles d'accès et chargement des comptes
└── service/        # Logique métier

doc/
├── schema.sql                          # Création de la base MySQL
└── Guide API Spring Boot + MySQL.pdf   # Guide du projet
```

## Limites connues

- Pas de récupération de mot de passe oublié.
- Pas de limitation des tentatives de connexion.
- Changer son mot de passe ne ferme pas les autres sessions ouvertes.
- Avant la mise en ligne, il faudra aussi activer HTTPS, sécuriser les cookies et désactiver Swagger.
