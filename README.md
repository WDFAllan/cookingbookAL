# CookingBook — Backend

API REST pour une application de gestion et partage de recettes culinaires.  
Architecture hexagonale (ports & adapters) · Spring Boot 3 · JWT stateless

> Frontend : [cookingbookfront](../cookingbookfront)

---

## Stack

| | |
|---|---|
| **Langage** | Java 21 |
| **Framework** | Spring Boot 3.4, Spring Security |
| **Persistance** | PostgreSQL, Spring Data JPA, Flyway |
| **Build** | Maven multi-module |
| **Tests** | JUnit 5, Mockito, Instancio, H2 (intégration) |
| **Outils** | MapStruct, Lombok |

---

## Architecture hexagonale

4 modules Maven avec des dépendances strictement orientées vers le domaine :

```
cookingbookAL/
├── domain/         → DTOs, ports (interfaces), services métier
├── api/            → Controllers REST, GlobalExceptionHandler
├── infrastructure/ → Adapters JPA, repositories, mappers MapStruct, entités DB
└── app/            → Point d'entrée Spring Boot, SecurityConfig
```

```
Controller (api)
    ↓
Service (domain)  ←→  Port (interface)
                            ↓
                      Adapter (infrastructure) → Repository JPA → PostgreSQL
```

**Règle** : `api` et `infrastructure` dépendent de `domain`. `domain` ne dépend de rien. Les entités JPA ne remontent jamais jusqu'aux controllers — seuls les DTOs transitent.

---

## Lancer le projet

### Prérequis
- Java 21+, Maven 3.9+, Docker

```bash
# 1. Démarrer PostgreSQL
docker-compose up -d

# 2. Build complet
mvn clean install

# 3. Lancer (port 8080)
cd app && mvn spring-boot:run
```

**Base de données :** `cookingbookDB` · user `postgres` · password `root` · port `5432`

---

## API

Base URL : `http://localhost:8080/api/v1`

| Méthode | Endpoint | Auth | Description |
|---------|----------|:----:|-------------|
| `POST` | `/auth/register` | | Inscription |
| `POST` | `/auth/login` | | Connexion → JWT |
| `GET` | `/recette?page=0&size=9&name=&sort=date` | | Liste paginée |
| `GET` | `/recette/{id}` | | Détail |
| `POST` | `/recette` | ✓ | Créer |
| `PUT` | `/recette/{id}` | ✓ owner | Modifier |
| `DELETE` | `/recette/{id}` | ✓ owner | Supprimer |
| `GET` | `/recette/getByTags?tags=dessert,apero` | | Filtrer par tags (AND) |
| `GET` | `/recette/me` | ✓ | Mes recettes |
| `GET` | `/recette/{id}/rating` | | Note moyenne + note utilisateur |
| `POST` | `/recette/{id}/rating` | ✓ | Noter |
| `POST` | `/upload` | ✓ | Upload image → URL |

Les endpoints protégés requièrent `Authorization: Bearer <token>`.  
Les erreurs retournent un `ProblemDetail` JSON (RFC 9457) avec `status`, `title`, `detail`.

---

## Fonctionnalités notables

- **Pagination + recherche** — `GET /recette?page=0&size=9&name=pizza&sort=rate`
- **Tri** par date de publication ou par note moyenne
- **Validation** — DTOs annotés (`@NotBlank`, `@NotEmpty`, `@Valid`), erreurs 400 structurées
- **Sécurité** — JWT stateless, vérification d'ownership sur PUT/DELETE, `@Profile("!test")` sur SecurityConfig
- **Upload fichier** — endpoint `/upload`, images servies via `/uploads/**`

---

## Tests

```bash
mvn test
```

| Module | Type | Couverture |
|--------|------|-----------|
| `domain` | Unitaires (Mockito + Instancio) | RecetteService, UserService, RatingService |
| `api` | Slice `@WebMvcTest` | RecetteController (pagination, 400, 404), RatingController |
| `app` | `@SpringBootTest` + H2 | CRUD complet, validation, 404 end-to-end |

---

## Base de données

Migrations versionnées dans `app/src/main/resources/db/migration/` :

| Fichier | Description |
|---------|-------------|
| `V1__init.sql` | Tables `recette`, `ingredient`, `user`, `rating` |
