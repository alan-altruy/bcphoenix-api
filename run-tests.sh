#!/bin/bash
set -e

echo "=========================================="
echo "  Lancement de l'environnement de test"
echo "=========================================="

chmod +x mvnw

# Lance les tests et s'arrête avec le test-runner
docker compose -f docker-compose-unit-test.yaml up --build test-runner ./mvnw clean verify

echo "=========================================="
echo "  Nettoyage des conteneurs de test..."
echo "=========================================="
# Supprime proprement les conteneurs arrêtés après les tests (laisse le cache Maven intact)
docker compose -f docker-compose-unit-test.yaml down

echo "  Terminé !"
