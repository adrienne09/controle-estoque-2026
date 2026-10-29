# Estoque 2026

API didática desenvolvida na disciplina de Programação da graduação em Sistemas de Informação.

## Domínio do projeto

Sistema de Gerenciamento de Estoque de Bebidas:
- Categoria de bebida: classificação e agrupamento dos produtos (ex.: Refrigerantes, Cervejas, Sucos, Águas).
- Bebida: item estocado identificado por código de barras único, com descrição, saldo em estoque, valor unitário, estoque mínimo e data de cadastro.
- Fornecedor: parceiro comercial identificado por CNPJ único e razão social, associado opcionalmente às bebidas.

## Requisitos

- Java 21
- Git
- IntelliJ IDEA com suporte a Maven e Spring Boot
- Docker Desktop, utilizado para instanciar o PostgreSQL e gerenciar containers

## Organização do curso

O sistema é construído incrementalmente. Cada aula termina em um estado executável, registrado por um commit e, após validação, por uma tag Git no formato `aula-NN-*`.

## Material de aula

| Aula | Tema | Status |
|---|---|---|
| 00 | GitHub e início do projeto | Concluído (`aula-00-inicio`) |
| 01 | Configuração do ambiente | Concluído |
| 02 | Criação do projeto e definição do tema | Concluído (`aula-02-projeto-spring-boot`) |
| 03 | Modelagem de domínio com Java puro | Concluído (`aula-03-dominio-estoque`) |
| 04 | Persistência com JPA, PostgreSQL, profiles e Liquibase | Concluído (`aula-04-jpa-postgresql-liquibase`) |
| 05 | Spring Data JPA, repositories, serviços e transações | Concluído (`aula-05-repositories-servicos-transacoes`) |
| 06 | Evolução do modelo com Fornecedor e Liquibase diff | Concluído (`aula-06-evolucao-modelo-liquibase-diff`) |
| 07 | API REST, DTOs, mapeadores e testes com Postman | Concluído (`aula-07-api-rest-dtos-mapeadores` / `aula-07-api-rest-dtos-mappers`) |
| 08 | Alteração, status, exclusão, movimentação de estoque e paginação | Concluído (`aula-08-crud-estoque-paginacao`) |
| 09 | Documentação da API com OpenAPI e Swagger UI | Concluído (`aula-09-openapi`) |
| 10 | Pessoas, clientes, colaboradores e vendas | Concluído (`aula-10-vendas`) |

## Executando o projeto

Na primeira execução, copie `.env.example` para `.env`, preencha `DB_DEV_PASSWORD` e `DB_TEST_PASSWORD` e mantenha esse arquivo fora do Git.

No Windows:

```powershell
Copy-Item .env.example .env
.\mvnw.cmd spring-boot:run "-Dspring-boot.run.profiles=dev"
```

No macOS ou Linux:

```bash
cp .env.example .env
./mvnw spring-boot:run -Dspring-boot.run.profiles=dev
```

Com a aplicação iniciada, acesse <http://localhost:8080/api/health>. A resposta esperada é `OK`.

## Modelo de domínio atual

A partir da Aula 10, o modelo representa o estoque de bebidas e as vendas:

```text
CategoriaBebida 1 ─────── N Bebida
Fornecedor      1 ─────── N Bebida (opcional)
Pessoa          1 ─────── 0..1 Cliente
Pessoa          1 ─────── 0..1 Colaborador
Cliente         1 ─────── N Venda
Colaborador     1 ─────── N Venda (vendedor)
Venda           1 ─────── N ItemVenda N ─────── 1 Bebida
```

O fornecedor de uma bebida é opcional. CPF e CNPJ são validados pelos dígitos verificadores (`DocumentoFiscal`). Cada item de venda guarda o preço da bebida no momento em que foi incluído, e a finalização da venda baixa o estoque de todos os itens na mesma transação: se uma bebida não tiver saldo, nada é baixado. As entidades pertencem ao pacote `com.curso.estoque2026.domain`, são mapeadas com JPA e preservam invariantes de negócio. O Liquibase cria e versiona o esquema; o Hibernate valida a correspondência (`ddl-auto=validate`). Os profiles `dev`, `test` e `prod` utilizam PostgreSQL.

## API atual

Após iniciar a aplicação, estão disponíveis:

- `/api/categorias-bebida`: cadastro, consulta, pesquisa paginada, alteração (`PUT`), status (`PUT /{id}/status`) e exclusão (`409` se houver bebidas);
- `/api/bebidas`: o mesmo, mais entradas e saídas de estoque (`POST /{id}/estoque/entradas` e `/saidas`) e o filtro `abaixoEstoqueMinimo`;
- `/api/fornecedores`: cadastro, consulta e listagem;
- `/api/pessoas`, `/api/clientes` e `/api/colaboradores`;
- `/api/vendas`: abrir, incluir e remover itens, finalizar e cancelar;
- `/api/health`.

As pesquisas aceitam `page`, `size` e `sort` (ex.: `?page=0&size=10&sort=descricao,asc`) e respondem com `conteudo`, `pagina`, `tamanho`, `totalElementos`, `totalPaginas`, `primeira` e `ultima`. O tamanho padrão é 20 e o máximo, 100.

A documentação interativa fica em `http://localhost:8080/swagger-ui/index.html` e o contrato OpenAPI em `http://localhost:8080/v3/api-docs`. No profile `prod` as duas ficam desligadas.

Os contratos utilizam DTOs imutáveis (`record`), validações Bean Validation (`@Valid`) e tratamento centralizado de erros (`GlobalExceptionHandler`) retornando `400 Bad Request`, `404 Not Found` e `409 Conflict`.

## Testes com Postman

A coleção completa de testes automatizados e manuais com variáveis de ambiente e asserções está disponível em:
- Arquivo de coleção: [`postman/estoque2026.postman_collection.json`](postman/estoque2026.postman_collection.json)
- Roteiro e justificativas de status HTTP: [`docs/postman-lab.md`](docs/postman-lab.md)

## Executando os testes automatizados

Os testes cobrem regras de domínio, serviços de aplicação, repositórios JPA e a camada web (MockMvc).

No Windows:

```powershell
.\mvnw.cmd test
```

No macOS ou Linux:

```bash
./mvnw test
```

## Histórico didático

Para consultar os marcos publicados do repositório:

```bash
git tag --list
```
