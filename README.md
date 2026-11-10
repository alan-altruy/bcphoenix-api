# BC Phoenix API

API REST en Java (Spring Boot) pour un club de bowling : affiliés, lieux, événements et scores, avec un accès public en lecture et un espace réservé aux administrateurs.

> **État du projet :** V1 en cours de développement, containerisée via Docker.

## Technologies

- Java 21
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- Spring Security (session par cookie, mots de passe hachés avec BCrypt)
- MySQL
- Springdoc OpenAPI (Swagger UI)
- Docker & Docker Compose

## Prérequis

- [Docker](https://www.docker.com/) et Docker Compose installés sur votre machine.
- Aucun besoin d'installer Java, Maven ou MySQL en local.

## Installation et Lancement avec Docker

1. Cloner le projet :
   git clone https://github.com/JasonFouret/bcphoenix.git
   cd bcphoenix

2. Créer un fichier `.env` à la racine du projet en y renseignant vos variables :
   DB_NAME=bcphoenix
   DB_USERNAME=root
   DB_PASSWORD=votre_mot_de_passe_securise
   API_PORT=8080
   ADMIN_EMAIL=votre_email_admin
   ADMIN_PASSWORD=votre_mot_de_passe_securise

3. Lancer l'application complète (Base de données MySQL + API) :
   docker compose up --build

L'API démarre sur http://localhost:8080 et initialise automatiquement la base de données ainsi que le compte administrateur.

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

Pour lancer les tests unitaires et d'intégration dans l'environnement conteneurisé :

./run-tests.sh

## Structure du projet

src/main/java/com/bcphoenix/
├── config/         # Configuration générale & Initialiseur d'admin)
├── controller/     # Points d'entrée HTTP (les URLs de l'API)
│   ├── account/    # Routes de la personne connectée
│   ├── admin/      # Routes réservées aux administrateurs
│   └── visitor/    # Routes publiques en lecture seule
├── dto/            # Objets reçus / renvoyés par l'API
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
