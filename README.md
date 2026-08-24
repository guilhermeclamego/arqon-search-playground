# arqon-search-playground

An interactive sandbox and integration test environment for the **ArqonSearch** library.

This project provides a local environment to experiment with ArqonSearch against real database engines, making it easier to validate queries, search behavior, pagination, and provider-specific query translations.

## Features

- **Hands-On Sandbox:** Run full-text search queries using the unified `arqon-search-core` DSL.
- **Local Multi-Engine Stack:** Pre-configured `docker-compose.yml` with MongoDB and Elasticsearch.
- **MongoDB Replica Set:** MongoDB runs as a single-node Replica Set (`rs0`) to provide an environment closer to MongoDB Atlas Search requirements.
- **Elasticsearch + Kibana:** Elasticsearch is available for search integration tests, with Kibana included for inspecting and experimenting with indexes and queries.
- **Test Dataset:** Includes a predefined `Person` dataset with fictional Star Wars-inspired characters and cities from Santa Catarina, Brazil.
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

> **Note:** The host machine does not need a local MongoDB, Elasticsearch, or `mongosh` installation. Everything runs inside Docker.

For Linux and macOS, the setup script can be executed directly.  
On Windows, you can run the script using Git Bash or WSL.

### Start the Local Environment

From the `src/main/resources/local/mongo` directory:

```bash
./setup-local.sh
```

The setup script is responsible for:
1. Starting the Docker containers (if `docker-compose.yml` is triggered from here).
2. Waiting for MongoDB to become available.
3. Initializing the MongoDB Replica Set (`rs0`) inside the container if necessary.
4. Creating/loading the `arqon-search` database.
5. Loading the predefined `Person` test dataset.

After the setup completes, the environment is ready for the playground application.

### MongoDB

MongoDB is exposed on:  
`mongodb://localhost:27017`

- **Database:** `arqon-search`
- **Replica Set:** `rs0`

The `Person` collection is populated automatically by the local setup.

### Elasticsearch

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
                ├── elastic/
                │   └── person-data.sh
                └── mongo/
                    ├── person-data.js
                    └── setup-local.sh
```

### `docker-compose.yml`
Defines the local infrastructure required by the playground:
- MongoDB
- Elasticsearch
- Kibana

MongoDB is configured as a single-node Replica Set (`rs0`).

### `setup-local.sh`
The setup script is the main entry point for preparing the local MongoDB environment.  
It is intentionally kept inside the project so the playground can be started consistently without manually configuring MongoDB or importing test data.

The script executes `mongosh` **directly inside the running MongoDB container** (`docker exec -i ... mongosh`), ensuring that zero local database dependencies are required on the host machine.

The script can be executed on:
- Linux
- macOS
- Windows using Git Bash or WSL

### `person-data.sh` (Elasticsearch)
Responsible for setting up the Elasticsearch index mappings and importing the initial test data for the `Person` dataset into the cluster.

---

## Test Data

The playground contains a predefined `Person` dataset used to exercise ArqonSearch queries.  
The data includes fields such as:
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