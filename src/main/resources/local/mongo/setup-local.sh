#!/usr/bin/env bash

set -e

MONGO_CONTAINER="arqon-mongodb"
MONGO_URI="mongodb://localhost:27017"
DATABASE="arqon-search"

SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"

echo "========================================="
echo " Arqon Search - Local MongoDB Setup"
echo "========================================="
echo
echo "MongoDB: $MONGO_URI"
echo "Database: $DATABASE"
echo

echo "Starting local services..."

docker compose up -d mongodb elasticsearch kibana

echo
echo "Waiting for MongoDB..."

until docker exec "$MONGO_CONTAINER" \
    mongosh --quiet \
    --eval "db.adminCommand({ ping: 1 }).ok" 2>/dev/null | grep -q "1"
do
    echo "MongoDB is not ready yet..."
    sleep 2
done

echo "MongoDB is available."
echo

echo "Checking Replica Set..."

until docker exec "$MONGO_CONTAINER" \
    mongosh --quiet \
    --eval "rs.status().ok" 2>/dev/null | grep -q "1"
do
    echo "Replica Set is not ready yet..."
    sleep 2
done

echo "Replica Set is available."
echo

echo "Loading Person dataset..."

docker exec -i "$MONGO_CONTAINER" \
    mongosh "mongodb://localhost:27017/$DATABASE" \
    < "$SCRIPT_DIR/mongo/person-data.js"

echo
echo "========================================="
echo " Setup completed!"
echo "========================================="