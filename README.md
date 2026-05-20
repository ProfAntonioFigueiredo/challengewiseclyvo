# PortalWeb Clyvo Vet

Aplicacao Java com Spring Boot para o Challenge Clyvo Vet. A solucao demonstra CRUD, API REST, persistencia em Oracle conteinerizado, modelagem relacional e infraestrutura Docker/Cloud.

## Problema de negocio

A jornada de cuidado do pet costuma ser fragmentada: tutores esquecem retornos, exames e cuidados preventivos, enquanto clinicas perdem recorrencia e visibilidade sobre continuidade de tratamento. A API Clyvo Vet organiza tutores, pets e planos de cuidado para apoiar acompanhamento preventivo, retorno clinico e alertas de risco.

## Beneficios para o negocio

- Melhora a continuidade do cuidado do pet por meio de planos e proximos passos.
- Ajuda clinicas a aumentar recorrencia, fidelizacao e previsibilidade.
- Centraliza dados de tutor, pet e cuidados em modelo relacional normalizado.
- Permite evoluir para dashboards, recomendacoes e alertas preditivos.

## Arquitetura macro

Fluxo principal:

1. Usuario acessa o front-end em `http://localhost:8080`.
2. Spring Boot entrega os arquivos estaticos e expoe APIs REST.
3. A aplicacao usa JDBC Oracle Thin para acessar o Oracle Free no servico `FREEPDB1`.
4. O banco persiste dados no volume nomeado `oracle_data`.
5. A API pode ser testada via Swagger e Postman.
6. A infraestrutura em nuvem pode ser criada por Azure CLI e executada em VM Linux com Docker.

## Rotas principais

### CRUD legado de alunos

- `GET /alunos?busca=&page=0&size=10&sort=nmAluno,asc`
- `GET /alunos/{rmAluno}`
- `POST /alunos`
- `PUT /alunos/{rmAluno}`
- `DELETE /alunos/{rmAluno}`

### API Clyvo Vet

- `GET /api/v1/tutores`
- `POST /api/v1/tutores`
- `PUT /api/v1/tutores/{id}`
- `DELETE /api/v1/tutores/{id}`
- `GET /api/v1/pets`
- `POST /api/v1/pets`
- `PUT /api/v1/pets/{id}`
- `DELETE /api/v1/pets/{id}`
- `GET /api/v1/planos-cuidado?busca=&status=AGENDADO&page=0&size=10&sort=dataPrevista,asc`
- `POST /api/v1/planos-cuidado`
- `PUT /api/v1/planos-cuidado/{id}`
- `DELETE /api/v1/planos-cuidado/{id}`

### Documentacao e entrega

- `GET /swagger-ui/index.html`
- `POST /entregas`

## Requisitos Java Advanced atendidos

- Entidades relacionadas e mapeadas com JPA: `Tutor`, `Pet`, `PlanoCuidado`.
- DTOs para entrada e saida da API.
- Bean Validation nos requests.
- Paginacao, ordenacao e busca com parametros.
- Cache com Spring Cache.
- Tratamento global de erros/excecoes.
- Swagger/OpenAPI com Springdoc.
- Colecao Postman em `documentos/PortalWeb-ClyvoVet.postman_collection.json`.

## Requisitos Banco Relacional atendidos

- Modelo em 3FN para tutores, pets e planos de cuidado.
- DDL Oracle em `sql/01_schema_oracle.sql`.
- Procedures parametrizadas e carga inicial em `sql/02_procedures_seed.sql`.
- Logs de erro em `t_log_erro`.
- Joins, agrupamentos, ordenacao, LAG/LEAD e cursores explicitos em `sql/03_reports_oracle.sql`.
- Modelo descritivo em `documentos/modelagem-relacional.md`.

## Execucao local com Docker Compose

```bash
docker compose up --build -d
```

Aplicacao:

```text
http://localhost:8080
```

Swagger:

```text
http://localhost:8080/swagger-ui/index.html
```

Oracle:

```text
Host: localhost
Porta: 1521
Service: FREEPDB1
Usuario: APP_USER
Senha: AppPassword123
```

## Docker Hub

Imagem configurada no Compose:

```text
profantoniofigueiredo/portalweb-oracle:v1
```

Build e push sugeridos:

```bash
docker build -t profantoniofigueiredo/portalweb-oracle:v1 .
docker push profantoniofigueiredo/portalweb-oracle:v1
```

## Azure CLI

Scripts:

- `cloud/azure-create-vm-docker.sh`: cria Resource Group, VM Linux, abre portas, instala Docker/Git/nano e executa a aplicacao com Docker Compose.
- `cloud/azure-delete-resources.sh`: remove o Resource Group ao final da entrega.

Antes da execucao, ajuste:

```bash
export REPO_URL="https://github.com/SEU_USUARIO/SEU_REPOSITORIO.git"
export ADMIN_IP_CIDR="SEU_IP/32"
```

Criar a infraestrutura:

```bash
./cloud/azure-create-vm-docker.sh
```

Remover a infraestrutura:

```bash
./cloud/azure-delete-resources.sh
```

## Links da entrega

- GitHub: preencher link do repositorio publico.
- YouTube: preencher link do video demonstrando Azure CLI, Docker, aplicacao e persistencia.
- Evidencia de remocao da VM: anexar print no PDF final.
