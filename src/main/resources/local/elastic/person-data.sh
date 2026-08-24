#!/usr/bin/env bash

set -e

ES_URL="${ES_URL:-http://elasticsearch:9200}"
INDEX="person"

echo "========================================="
echo " Arqon Search - Elasticsearch Seed"
echo "========================================="
echo
echo "Elasticsearch: $ES_URL"
echo "Index: $INDEX"
echo

echo "Waiting for Elasticsearch..."

until curl -s "$ES_URL/_cluster/health" > /dev/null; do
    sleep 2
done

echo "Elasticsearch is available."
echo

echo "Removing existing index..."

curl -s -X DELETE "$ES_URL/$INDEX" > /dev/null || true

echo
echo "Creating index..."

curl -s -X PUT "$ES_URL/$INDEX" \
    -H "Content-Type: application/json" \
    -d '{
        "mappings": {
            "properties": {
                "id": {
                    "type": "keyword"
                },
                "name": {
                    "type": "text"
                },
                "email": {
                    "type": "text"
                },
                "age": {
                    "type": "integer"
                },
                "city": {
                    "type": "text"
                },
                "country": {
                    "type": "text"
                }
            }
        }
    }'

echo
echo
echo "Loading Person dataset..."

curl -s -X POST "$ES_URL/$INDEX/_bulk" \
    -H "Content-Type: application/x-ndjson" \
    --data-binary '
{"index":{"_id":"1"}}
{"id":"1","name":"Luke Skywalker","email":"luke.skywalker@example.com","age":32,"city":"Criciúma","country":"Brazil"}
{"index":{"_id":"2"}}
{"id":"2","name":"Leia Organa","email":"leia.organa@example.com","age":28,"city":"Balneário Camboriú","country":"Brazil"}
{"index":{"_id":"3"}}
{"id":"3","name":"Han Solo","email":"han.solo@example.com","age":41,"city":"Florianópolis","country":"Brazil"}
{"index":{"_id":"4"}}
{"id":"4","name":"Padmé Amidala","email":"padme.amidala@example.com","age":35,"city":"Joinville","country":"Brazil"}
{"index":{"_id":"5"}}
{"id":"5","name":"Obi-Wan Kenobi","email":"obi.wan@example.com","age":50,"city":"Blumenau","country":"Brazil"}
{"index":{"_id":"6"}}
{"id":"6","name":"Anakin Skywalker","email":"anakin.skywalker@example.com","age":29,"city":"Itajaí","country":"Brazil"}
{"index":{"_id":"7"}}
{"id":"7","name":"Yoda","email":"yoda@example.com","age":900,"city":"São José","country":"Brazil"}
{"index":{"_id":"8"}}
{"id":"8","name":"Mace Windu","email":"mace.windu@example.com","age":53,"city":"Chapecó","country":"Brazil"}
{"index":{"_id":"9"}}
{"id":"9","name":"Chewbacca","email":"chewbacca@example.com","age":200,"city":"Lages","country":"Brazil"}
{"index":{"_id":"10"}}
{"id":"10","name":"R2-D2","email":"r2d2@example.com","age":33,"city":"Tubarão","country":"Brazil"}
'

echo
echo "Refreshing index..."

curl -s -X POST "$ES_URL/$INDEX/_refresh" > /dev/null

echo
echo "========================================="
echo " Elasticsearch seed completed!"
echo "========================================="