# Laboratório de Testes com Postman — Estoque 2026

Roteiro de execução e documentação técnica da API REST para o tema **Controle de Estoque de Bebidas** (Aula 07).

---

## 1. Importação da Coleção

1. Abra o Postman.
2. Clique em **Import** e selecione o arquivo [postman/estoque2026.postman_collection.json](../postman/estoque2026.postman_collection.json).
3. A coleção define a variável de ambiente:
   - `baseUrl`: `http://localhost:8080`

---

## 2. Roteiro de Execução Recomendado

Execute as requisições na ordem abaixo para permitir que os scripts automáticos capturem os IDs gerados:

1. **`00 - Health / Health Check`**: valida se a aplicação está ativa (`200 OK`).
2. **`01 - Categorias / 1.1 Cadastrar Categoria`**: cria a categoria *Refrigerantes* (`201 Created`) e armazena `categoriaId`.
3. **`02 - Fornecedores / 2.1 Cadastrar Fornecedor`**: cria o fornecedor *Ambev Distribuidora Brasil LTDA* (`201 Created`) e armazena `fornecedorId`.
4. **`03 - Bebidas / 3.1 Cadastrar Bebida Completa`**: associa a categoria e o fornecedor criados (`201 Created`) e armazena `bebidaId`.
5. **`03 - Bebidas / 3.2 Cadastrar Bebida sem Fornecedor`**: valida a regra de fornecedor opcional (`201 Created`).
6. **Consultas (`GET`)**:
   - `1.2 Listar Categorias` / `1.3 Buscar por ID` (`200 OK`)
   - `2.2 Listar Fornecedores` / `2.3 Buscar por ID` (`200 OK`)
   - `3.3 Listar Bebidas` / `3.4 Buscar por ID` (`200 OK`)
7. **Cenários de Erro e Validação**:
   - `1.4 Erro 400`: nome de categoria em branco / inválido.
   - `1.5 Erro 409`: tentativa de duplicar categoria com mesmo nome.
   - `2.4 Erro 400`: CNPJ com máscara ou menos de 14 dígitos.
   - `2.5 Erro 409`: CNPJ de fornecedor duplicado.
   - `3.5 Erro 400`: payload vazio ou campos obrigatórios ausentes.
   - `3.6 Erro 404`: tentativa de cadastrar bebida com `categoriaId` inexistente.
   - `3.7 Erro 404`: busca por ID de bebida inexistente (`/api/bebidas/999999`).
   - `3.8 Erro 409`: tentativa de cadastrar bebida com código de barras já existente.

---

## 3. Justificativa dos Códigos de Status HTTP Adotados

| Status HTTP | Significado REST | Quando é utilizado nesta API |
|---|---|---|
| **`200 OK`** | Requisição atendida com sucesso | Consultas (`GET`) retornando lista de registros ou um recurso específico por ID. |
| **`201 Created`** | Recurso criado com sucesso | Cadastros (`POST`) bem-sucedidos. Devolve no cabeçalho `Location` a URI do novo recurso criado e no corpo a representação Response DTO. |
| **`400 Bad Request`** | Sintaxe incorreta ou violação de validação | Falha de validação Bean Validation (campos obrigatórios vazios, valores negativos para saldos/preços, CNPJ fora do padrão de 14 dígitos). |
| **`404 Not Found`** | Recurso solicitado não existe | Busca por ID inexistente ou tentativa de associar um recurso a uma categoria/fornecedor cujo ID não existe no banco. |
| **`409 Conflict`** | Conflito com o estado atual do servidor | Violação de regras de unicidade de negócio (código de barras de bebida já cadastrado, CNPJ de fornecedor já existente ou nome de categoria duplicado). |
| **`500 Internal Server Error`** | Erro inesperado no servidor | Falhas de infraestrutura não tratadas. A API trata exceções de negócio antes que alcancem erro 500. |
