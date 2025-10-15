#!/bin/bash
echo "=== 🚀 EXÉCUTION DES TESTS AVEC RÉSEAU ==="

# Télécharger toutes les dépendances d'abord
echo "📥 Téléchargement des dépendances..."
mvn dependency:resolve

# Exécuter les tests avec profil test
echo "🧪 Exécution des tests..."
mvn clean test -Dspring.profiles.active=test -DfailIfNoTests=false

# Générer les rapports
echo "📊 Génération des rapports..."
mvn jacoco:report

echo "✅ TESTS TERMINÉS !"