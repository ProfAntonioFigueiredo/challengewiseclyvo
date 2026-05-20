#!/usr/bin/env bash
set -euo pipefail

# Ajuste estes valores antes da demonstracao.
RESOURCE_GROUP="${RESOURCE_GROUP:-rg-clyvo-vet-challenge}"
LOCATION="${LOCATION:-brazilsouth}"
VM_NAME="${VM_NAME:-vm-clyvo-vet}"
ADMIN_USER="${ADMIN_USER:-azureuser}"
APP_USER="${APP_USER:-appuser}"
IMAGE="${IMAGE:-Ubuntu2204}"
SIZE="${SIZE:-Standard_B2s}"
REPO_URL="${REPO_URL:-https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git}"
ADMIN_IP_CIDR="${ADMIN_IP_CIDR:-0.0.0.0/0}"

az group create \
  --name "$RESOURCE_GROUP" \
  --location "$LOCATION"

az vm create \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --image "$IMAGE" \
  --size "$SIZE" \
  --admin-username "$ADMIN_USER" \
  --generate-ssh-keys \
  --public-ip-sku Standard

az vm open-port \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --port 8080 \
  --priority 1010

az vm open-port \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --port 22 \
  --priority 1020

az network nsg rule create \
  --resource-group "$RESOURCE_GROUP" \
  --nsg-name "${VM_NAME}NSG" \
  --name AllowOracleAdmin1521 \
  --priority 1030 \
  --access Allow \
  --direction Inbound \
  --protocol Tcp \
  --source-address-prefixes "$ADMIN_IP_CIDR" \
  --destination-port-ranges 1521

az vm run-command invoke \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --command-id RunShellScript \
  --scripts "apt-get update && apt-get install -y ca-certificates curl gnupg git nano && install -m 0755 -d /etc/apt/keyrings && curl -fsSL https://download.docker.com/linux/ubuntu/gpg -o /etc/apt/keyrings/docker.asc && chmod a+r /etc/apt/keyrings/docker.asc && echo 'deb [arch=\$(dpkg --print-architecture) signed-by=/etc/apt/keyrings/docker.asc] https://download.docker.com/linux/ubuntu \$(. /etc/os-release && echo \$VERSION_CODENAME) stable' > /etc/apt/sources.list.d/docker.list && apt-get update && apt-get install -y docker-ce docker-ce-cli containerd.io docker-buildx-plugin docker-compose-plugin && id -u $APP_USER >/dev/null 2>&1 || useradd -m -s /bin/bash $APP_USER && usermod -aG docker $APP_USER && systemctl enable --now docker"

az vm run-command invoke \
  --resource-group "$RESOURCE_GROUP" \
  --name "$VM_NAME" \
  --command-id RunShellScript \
  --scripts "sudo -u $APP_USER bash -lc 'cd /home/$APP_USER && if [ ! -d app ]; then git clone $REPO_URL app; fi && cd app && docker compose up --build -d'"

PUBLIC_IP="$(az vm show --resource-group "$RESOURCE_GROUP" --name "$VM_NAME" --show-details --query publicIps -o tsv)"
echo "Aplicacao publicada em: http://$PUBLIC_IP:8080"
echo "Swagger: http://$PUBLIC_IP:8080/swagger-ui/index.html"
