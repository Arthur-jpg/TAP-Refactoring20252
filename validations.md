# Validation Rules (Modelo AP2)

As regras abaixo refletem o modelo enxuto composto por Aluno, Curso, Disciplina, Turma, Professor e Inscrição.

## Aluno
- Matrícula inteira positiva (`@Min(1)` no DTO e validação no serviço).
- Nome obrigatório, máx. 80 caracteres.

## Curso
- Código inteiro positivo.
- Nome obrigatório, máx. 80 caracteres.

## Professor
- Nome obrigatório, máx. 80 caracteres.

## Disciplina
- Código inteiro positivo.
- Nome obrigatório, máx. 80 caracteres.
- Deve existir curso (`curso_codigo`) e professor (`professor_id`) válidos.

## Turma
- Código inteiro positivo.
- Ano entre 1900 e 2100.
- Semestre 1 ou 2.
- Necessita de disciplina existente.
- Identificada por (código, ano, semestre) => chave composta.

## Inscrição
- Exige matrícula de aluno e chave da turma (código, ano, semestre).
- Ano entre 1900 e 2100, semestre 1 ou 2.
- Uma inscrição por combinação aluno/turma (checada via `existsByAluno...`).

## Observações
- Todas as relações "1" do diagrama são obrigatórias (`optional = false`, `nullable = false`).
- Campos removidos (telefones, estado civil, avaliações, etc.) não fazem mais parte do domínio.
- Para evoluções futuras, criar migrations (ex.: Flyway) ao invés de depender de `ddl-auto`.
