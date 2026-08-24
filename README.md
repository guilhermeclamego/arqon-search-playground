# arqon-search-playground

An interactive sandbox and integration test environment for the **ArqonSearch** library.

This project provides a local environment to experiment with ArqonSearch against real database engines, making it easier to validate queries, search behavior, pagination, and provider-specific query translations.

## Features

- **Hands-On Sandbox:** Run full-text search queries using the unified `arqon-search-core` DSL.
- **Local Multi-Engine Stack:** Pre-configured `docker-compose.yml` with MongoDB and Elasticsearch.
- **MongoDB Replica Set:** MongoDB runs as a single-node Replica Set (`rs0`) to provide an environment closer to MongoDB Atlas Search requirements.
- **Elasticsearch + Kibana:** Elasticsearch is available for search integration tests, with Kibana included for inspecting and experimenting with indexes and queries.
- **Test Dataset:** Includes a predefined `Person` dataset (JSON) with fictional Star Wars-inspired characters and cities from Santa Catarina, Brazil.
- **Real-World Verification:** Test query translations, pagination, fuzzy search, autocomplete, range queries, and multi-field matching against live databases.

## Local Environment

The playground uses Docker to provide the required infrastructure:

```text
┌──────────────────────────────┐
│       ArqonSearch Playground │
│         Spring Boot          │
└──────────────┬───────────────┘
               │
       ┌───────┴────────┐
       │                │
       ▼                ▼
┌─────────────┐  ┌───────────────┐
│   MongoDB   │  │ Elasticsearch │
│   Replica   │  │               │
│    Set rs0  │  │    :9200      │
│   :27017    │  └───────┬───────┘
└─────────────┘          │
                         ▼
                  ┌─────────────┐
                  │   Kibana    │
                  │    :5601    │
                  └─────────────┘
```

## Getting Started

### Prerequisites

You only need:
- **Docker**
- **Docker Compose**
- **Java 25+** (to run the application)

> **Note:** The host machine does not need a local MongoDB or Elasticsearch installation. Everything runs inside Docker.

### 1. Start the Local Infrastructure

Navigate to the directory containing your `docker-compose.yml` and start the containers:

```bash
docker-compose up -d
```

### 2. Run the Application

Start the Spring Boot application (`ArqonSearchPlaygroundApplication`) using your IDE or Maven.

### 3. Populate the Databases

Instead of using scripts, the playground exposes REST endpoints to easily populate the databases. Use your preferred HTTP client (like cURL, Postman, or Insomnia) to send the `person-data.json` file as the payload.

**Populate Elasticsearch:**
```http
POST /persons/elasticsearch
Content-Type: application/json
```

**Populate MongoDB:**
```http
POST /persons/mongo
Content-Type: application/json
```

*The raw JSON data can be found at: `src/main/resources/local/person-data.json`*

### MongoDB Services

MongoDB is exposed on:  
`mongodb://localhost:27017`

- **Database:** `arqon-search`
- **Replica Set:** `rs0`

### Elasticsearch Services

Elasticsearch is available at:  
`http://localhost:9200`

Kibana is available at:  
`http://localhost:5601`

Kibana can be used to inspect indexes and manually experiment with Elasticsearch queries while developing and testing ArqonSearch.

---

## Project Structure

```text
arqon-search-playground/
├── docker-compose.yml
└── src/
    └── main/
        └── resources/
            ├── application.yml
            └── local/
                └── person-data.json
```

### `docker-compose.yml`
Defines the local infrastructure required by the playground:
- MongoDB (configured as a single-node Replica Set `rs0`)
- Elasticsearch
- Kibana

### `person-data.json`
Contains the predefined test dataset used to exercise ArqonSearch queries. This JSON payload is meant to be sent via the API to populate the respective databases.

---

## Test Data

The dataset includes fields such as:
- `id`
- `name`
- `email`
- `age`
- `city`
- `country`

The dataset is intentionally small and deterministic, making it easier to reproduce and debug search behavior.

---

## Purpose

This project is not intended to be a production application.  
Its purpose is to provide a simple environment for experimenting with and validating ArqonSearch.

Typical scenarios include:
- Full-text search
- Fuzzy search
- Autocomplete
- Term queries
- Range queries
- Boolean queries
- Pagination
- Sorting
- Multi-field search
- MongoDB/Atlas Search query rendering
- Elasticsearch query rendering

The same logical search can be executed against different providers, allowing ArqonSearch's provider-agnostic DSL to be tested against real engines.