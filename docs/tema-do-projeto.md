# Tema do projeto

## Identificação

- Nome: `estoque2026`
- Tema: gerenciamento de estoque de bebidas
- Objetivo: cadastrar bebidas e organizá-las por categorias de bebidas

## Entidade de classificação

- Nome no singular: `CategoriaBebida`
- Nome no plural: categorias de bebidas
- Descrição: classificação utilizada para organizar as bebidas
- Exemplos: Cervejas, Vinhos, Destilados, Sem Álcool
- Status: ativo ou inativo

## Entidade principal

- Nome no singular: `Bebida`
- Nome no plural: bebidas
- Código único: código de barras
- Descrição: nome ou descrição comercial da bebida
- Medida quantitativa: saldo em estoque
- Valor monetário: valor unitário
- Valor calculado: saldo em estoque multiplicado pelo valor unitário
- Data relevante: data de cadastro
- Status: ativo ou inativo

## Relacionamento

- Uma categoria de bebida pode classificar várias bebidas.
- Cada bebida pertence a uma categoria de bebida.

## Exemplos

| Categoria | Código | Bebida | Saldo | Valor unitário |
|---|---|---|---:|---:|
| Cervejas | `7890000000001` | Cerveja Pilsen 500ml | 50 | 7,50 |
| Vinhos | `7890000000002` | Vinho Tinto Cabernet | 20 | 45,00 |
| Destilados | `7890000000003` | Whisky 12 Anos | 10 | 120,00 |

## Correspondência para o projeto

```text
CategoriaBebida -> entidade de classificação do tema
Bebida          -> entidade principal do tema
```
