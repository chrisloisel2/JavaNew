# API REST de Gestion de Bibliothèque

Une API REST complète pour gérer une bibliothèque avec gestion des livres, auteurs, utilisateurs et emprunts.

## Technologies utilisées

- **Java 25** avec toutes les fonctionnalités modernes
- **Spring Boot 3.5.7**
- **Spring Data JPA** - Accès aux données
- **H2 Database** - Base de données en mémoire
- **Spring Security** - Sécurité
- **Maven** - Gestion des dépendances

## Fonctionnalités

- CRUD complet pour les livres, auteurs, utilisateurs et emprunts
- Gestion des emprunts avec suivi des dates et des retours
- Validation des données avec Bean Validation
- Gestion globale des exceptions avec pattern matching
- Base de données H2 avec console web
- Données de test préchargées
- **Virtual Threads** pour une concurrence massive
- **Records Java** pour des DTOs immuables
- **Sealed Classes** pour des hiérarchies contrôlées

## 🚀 Optimisations Java 25

Ce projet utilise pleinement les fonctionnalités modernes de Java 25:

- ✅ **Records** - DTOs immuables et concis (réduction de 50% du code)
- ✅ **Sealed Classes** - Hiérarchies de types contrôlées
- ✅ **Pattern Matching** - Code plus expressif et sûr
- ✅ **Switch Expressions** - Expressions switch modernes
- ✅ **Virtual Threads** - Millions de threads concurrents
- ✅ **Stream API moderne** - `toList()`, `formatted()`, etc.
- ✅ **Optimisations Spring Boot 3.x** - Configuration optimale

📖 **[Voir la documentation complète des optimisations](JAVA25_OPTIMIZATIONS.md)**

## Démarrage rapide

### Prérequis
- JDK 25 installé
- Maven installé (ou utiliser le wrapper Maven inclus)

### Lancer l'application

```bash
./mvnw spring-boot:run
```

L'application démarre sur `http://localhost:8080`

### Accéder à la console H2

URL: `http://localhost:8080/h2-console`

Paramètres de connexion:
- JDBC URL: `jdbc:h2:mem:librarydb`
- Username: `sa`
- Password: (laisser vide)

## Endpoints de l'API

### Auteurs

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/authors` | Liste tous les auteurs |
| GET | `/api/authors/{id}` | Récupère un auteur par ID |
| POST | `/api/authors` | Crée un nouvel auteur |
| PUT | `/api/authors/{id}` | Met à jour un auteur |
| DELETE | `/api/authors/{id}` | Supprime un auteur |
| GET | `/api/authors/search?lastName={nom}` | Recherche des auteurs par nom |

### Livres

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/books` | Liste tous les livres |
| GET | `/api/books/{id}` | Récupère un livre par ID |
| POST | `/api/books` | Crée un nouveau livre |
| PUT | `/api/books/{id}` | Met à jour un livre |
| DELETE | `/api/books/{id}` | Supprime un livre |
| GET | `/api/books/search?title={titre}` | Recherche des livres par titre |
| GET | `/api/books/available` | Liste les livres disponibles |
| GET | `/api/books/author/{authorId}` | Liste les livres d'un auteur |

### Utilisateurs

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/users` | Liste tous les utilisateurs |
| GET | `/api/users/{id}` | Récupère un utilisateur par ID |
| GET | `/api/users/username/{username}` | Récupère un utilisateur par nom d'utilisateur |
| POST | `/api/users/register` | Enregistre un nouvel utilisateur |
| PUT | `/api/users/{id}` | Met à jour un utilisateur |
| DELETE | `/api/users/{id}` | Supprime un utilisateur |
| PATCH | `/api/users/{id}/deactivate` | Désactive un utilisateur |

### Emprunts

| Méthode | Endpoint | Description |
|---------|----------|-------------|
| GET | `/api/loans` | Liste tous les emprunts |
| GET | `/api/loans/{id}` | Récupère un emprunt par ID |
| POST | `/api/loans` | Crée un nouvel emprunt |
| PATCH | `/api/loans/{id}/return` | Enregistre le retour d'un livre |
| GET | `/api/loans/user/{userId}` | Liste les emprunts d'un utilisateur |
| GET | `/api/loans/user/{userId}/active` | Liste les emprunts actifs d'un utilisateur |
| GET | `/api/loans/overdue` | Liste les emprunts en retard |

## Exemples de requêtes

### Créer un auteur

```bash
curl -X POST http://localhost:8080/api/authors \
  -H "Content-Type: application/json" \
  -d '{
    "firstName": "Jules",
    "lastName": "Verne",
    "birthDate": "1828-02-08",
    "nationality": "Française",
    "biography": "Écrivain français dont l œuvre est, pour la plus grande partie, constituée de romans d aventures"
  }'
```

### Créer un livre

```bash
curl -X POST http://localhost:8080/api/books \
  -H "Content-Type: application/json" \
  -d '{
    "title": "Vingt Mille Lieues sous les mers",
    "isbn": "978-2253006329",
    "authorId": 1,
    "publicationDate": "1870-01-01",
    "genre": "Science-fiction",
    "description": "Roman d aventures",
    "totalCopies": 3,
    "availableCopies": 3
  }'
```

### Enregistrer un utilisateur

```bash
curl -X POST http://localhost:8080/api/users/register \
  -H "Content-Type: application/json" \
  -d '{
    "username": "marie.dupont",
    "password": "password123",
    "email": "marie.dupont@email.com",
    "firstName": "Marie",
    "lastName": "Dupont",
    "phoneNumber": "0612345678"
  }'
```

### Créer un emprunt

```bash
curl -X POST http://localhost:8080/api/loans \
  -H "Content-Type: application/json" \
  -d '{
    "bookId": 1,
    "userId": 2,
    "notes": "Premier emprunt"
  }'
```

### Retourner un livre

```bash
curl -X PATCH http://localhost:8080/api/loans/1/return
```

## Données de test

L'application est préchargée avec des données de test:

### Utilisateurs

| Username | Password | Role | Email |
|----------|----------|------|-------|
| admin | admin123 | ADMIN | admin@library.com |
| librarian | librarian123 | LIBRARIAN | librarian@library.com |
| john.doe | password | USER | john.doe@email.com |
| jane.smith | password | USER | jane.smith@email.com |

### Auteurs

- Victor Hugo
- Albert Camus
- Molière Pocquelin
- George Orwell

### Livres

- Les Misérables (Victor Hugo)
- Notre-Dame de Paris (Victor Hugo)
- L'Étranger (Albert Camus)
- La Peste (Albert Camus)
- Le Tartuffe (Molière)
- 1984 (George Orwell)
- La Ferme des animaux (George Orwell)

## Modèle de données

### Entités principales

1. **Author** (Auteur)
   - id, firstName, lastName, birthDate, nationality, biography
   - Relations: OneToMany avec Book

2. **Book** (Livre)
   - id, title, isbn, publicationDate, genre, description
   - totalCopies, availableCopies
   - Relations: ManyToOne avec Author, OneToMany avec Loan

3. **User** (Utilisateur)
   - id, username, password, email, firstName, lastName
   - phoneNumber, role, active, createdAt
   - Relations: OneToMany avec Loan

4. **Loan** (Emprunt)
   - id, loanDate, dueDate, returnDate, status, notes
   - Relations: ManyToOne avec Book et User

## Règles métier

- Un utilisateur peut emprunter maximum 5 livres simultanément
- La durée d'emprunt par défaut est de 2 semaines
- Un livre doit avoir au moins une copie disponible pour être emprunté
- Les mots de passe sont chiffrés avec BCrypt
- Les emprunts en retard sont automatiquement détectés

## Structure du projet

```
src/main/java/com/example/backend/
├── config/              # Configuration (Security, Data Initializer)
├── controller/          # Contrôleurs REST
├── dto/                 # Data Transfer Objects
├── exception/           # Gestion des exceptions
├── model/               # Entités JPA
├── repository/          # Repositories Spring Data
└── service/             # Services métier
```

## Configuration

Les paramètres de configuration se trouvent dans [application.properties](src/main/resources/application.properties):

- Base de données H2 en mémoire
- Console H2 activée
- Mode DDL: create-drop (recrée la base à chaque démarrage)
- SQL logging activé pour le développement

## Prochaines étapes possibles

- Ajouter l'authentification JWT
- Implémenter la pagination et le tri
- Ajouter des statistiques (livres les plus empruntés, etc.)
- Mettre en place des notifications pour les retours en retard
- Ajouter la gestion des réservations
- Intégrer Swagger/OpenAPI pour la documentation
- Migrer vers PostgreSQL pour la production

## Licence

Ce projet est un exemple éducatif.
