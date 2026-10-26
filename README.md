# BC Phoenix API

API REST en Java (Spring Boot) pour gérer des **utilisateurs** et leurs **rôles**, avec une base MySQL.

## Technologies

- Java 21
- Spring Boot 4 (Web MVC, Data JPA, Validation)
- MySQL
- Springdoc OpenAPI (Swagger UI)
- Maven (via le wrapper `mvnw`, pas besoin d'installer Maven)

## Prérequis

- JDK 21
- Un serveur MySQL qui tourne en local (port 3306 par défaut)

## Installation

1. Cloner le projet :

   ```bash
   git clone https://github.com/<ton-compte>/bcphoenix.git
   cd bcphoenix
   ```

2. Créer la base et les tables avec le script [doc/schema.sql](doc/schema.sql) :

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

## Lancer l'application

```bash
./mvnw spring-boot:run
```

Sous Windows : `mvnw.cmd spring-boot:run`

L'API démarre sur http://localhost:8080.

## Documentation de l'API

Une fois l'application lancée, la documentation interactive (Swagger UI) est disponible ici :
http://localhost:8080/swagger-ui.html

### Endpoints

| Méthode | URL                | Description                 |
|---------|--------------------|-----------------------------|
| GET     | `/api/roles`       | Liste des rôles             |
| GET     | `/api/roles/{id}`  | Détail d'un rôle            |
| POST    | `/api/roles`       | Créer un rôle               |
| PUT     | `/api/roles/{id}`  | Modifier un rôle            |
| DELETE  | `/api/roles/{id}`  | Supprimer un rôle           |
| GET     | `/api/users`       | Liste des utilisateurs      |
| GET     | `/api/users/{id}`  | Détail d'un utilisateur     |
| POST    | `/api/users`       | Créer un utilisateur        |
| PUT     | `/api/users/{id}`  | Modifier un utilisateur     |
| DELETE  | `/api/users/{id}`  | Supprimer un utilisateur    |

## Tests

```bash
./mvnw test
```

> Le test actuel démarre toute l'application : il a besoin que MySQL soit lancé.

## Structure du projet

```
src/main/java/com/bcphoenix/
├── controller/   # Points d'entrée HTTP (les URLs de l'API)
├── service/      # Logique métier
├── repository/   # Accès à la base de données (Spring Data JPA)
├── model/        # Entités = tables de la base
├── dto/          # Objets reçus / renvoyés par l'API
└── exception/    # Gestion centralisée des erreurs

doc/
├── schema.sql                          # Création de la base MySQL
└── Guide API Spring Boot + MySQL.pdf   # Guide du projet
```
