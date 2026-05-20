# Modelagem relacional

## Modelo descritivo

A solucao Clyvo Vet registra responsaveis por pets, os pets vinculados a cada tutor e os planos de cuidado que representam a continuidade da jornada clinica. O modelo esta em 3FN: dados de tutor, pet e planos ficam em tabelas distintas, sem repeticao de grupos, com dependencias funcionais diretas das chaves primarias.

## Entidades e relacionamentos

```mermaid
erDiagram
    T_TUTOR ||--o{ T_PET : possui
    T_PET ||--o{ T_PLANO_CUIDADO : recebe

    T_TUTOR {
        NUMBER id_tutor PK
        VARCHAR2 nm_tutor
        VARCHAR2 ds_email UK
        VARCHAR2 nr_telefone
    }

    T_PET {
        NUMBER id_pet PK
        VARCHAR2 nm_pet
        VARCHAR2 ds_especie
        VARCHAR2 ds_raca
        DATE dt_nascimento
        NUMBER vl_peso_kg
        NUMBER id_tutor FK
    }

    T_PLANO_CUIDADO {
        NUMBER id_plano_cuidado PK
        NUMBER id_pet FK
        VARCHAR2 ds_titulo
        VARCHAR2 tp_categoria
        VARCHAR2 st_cuidado
        DATE dt_prevista
        VARCHAR2 ds_observacao
    }
```

## Constraints principais

- `T_TUTOR.ds_email` e unico para evitar cadastro duplicado do responsavel.
- `T_PET.id_tutor` e chave estrangeira obrigatoria para `T_TUTOR`.
- `T_PLANO_CUIDADO.id_pet` e chave estrangeira obrigatoria para `T_PET`.
- `T_PLANO_CUIDADO.tp_categoria` aceita apenas categorias da API Java.
- `T_PLANO_CUIDADO.st_cuidado` aceita apenas status da API Java.
- `T_LOG_ERRO` guarda procedure, usuario, data, codigo e mensagem de erro para as cargas.

## Arquivos SQL

- `sql/01_schema_oracle.sql`: DDL do modelo fisico Oracle.
- `sql/02_procedures_seed.sql`: procedures parametrizadas e carga inicial.
- `sql/03_reports_oracle.sql`: blocos anonimos, joins, LAG/LEAD e relatorios com cursor explicito.
