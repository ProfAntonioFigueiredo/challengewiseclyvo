#!/usr/bin/env bash
set -euo pipefail

RESOURCE_GROUP="${RESOURCE_GROUP:-rg-clyvo-vet-challenge}"

az group delete \
  --name "$RESOURCE_GROUP" \
  --yes \
  --no-wait

echo "Remocao solicitada para o resource group: $RESOURCE_GROUP"
echo "Use o portal Azure ou 'az group show --name $RESOURCE_GROUP' para evidenciar a remocao."
