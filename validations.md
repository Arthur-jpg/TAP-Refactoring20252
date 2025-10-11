# Validation Rules Summary

This document enumerates all hardcoded validation rules found across the project, grouped by domain. Use it to check compatibility of other versions against these constraints.

Notes:
- Service-layer validations are the authoritative input constraints (ranges/lengths).
- Entities also enforce invariants (non-null, positive, duplicates) using IllegalArgumentException/IllegalStateException.
- DTOs do not carry annotations; all checks are manual in services/entities.

## Aluno (Student)

Source:
- service/AlunoService.java
- entity/Aluno.java

Input constraints (Service):
- matricula: integer, 1..99 inclusive. Error: ServiceExceptionEnum.CURSO_CODIGO_INVALIDO
- nome: length 1..20 inclusive. Error: ServiceExceptionEnum.CURSO_NOME_INVALIDO

Entity invariants:
- constructor: matricula > 0 else IllegalArgumentException("Número da matrícula deve ser positivo")
- constructor and setters: nomeCompleto non-null and non-blank (trim().isEmpty() -> IAE)
- dataNascimento non-null
- estadoCivil non-null
- curso non-null
- telefones: stored as list; constructor copies Vector if provided else empty list
- idade: must be >= 0 (setIdade)
- numeroMatricula: must be > 0 (setNumeroMatricula)
- telefones operations:
  - adicionarTelefone: telefone non-null, non-blank; duplicates not allowed (IllegalStateException)
  - removerTelefone: must exist (IllegalStateException)
- inscricoes operations:
  - adicionarInscricao: non-null; duplicates not allowed (IllegalStateException)
  - removerInscricao: must exist (IllegalStateException)

## Curso (Course)

Source:
- service/CursoService.java
- entity/Curso.java

Input constraints (Service):
- codigo: integer, 1..99 inclusive. Error: ServiceExceptionEnum.CURSO_CODIGO_INVALIDO
- nome: length 1..20 inclusive. Error: ServiceExceptionEnum.CURSO_NOME_INVALIDO

Entity invariants:
- constructor: codigo > 0 else IllegalArgumentException("Código do curso deve ser positivo")
- nomeCurso non-null and non-blank
- codigoCurso setter: > 0
- alunosMatriculados list: non-null on setter
- disciplinasOfertadas list: non-null on setter
- business rules:
  - matricularAluno: aluno non-null; cannot add duplicate (IllegalStateException)
  - desmatricularAluno: aluno non-null; must be enrolled (IllegalStateException)
  - adicionarDisciplina: disciplina non-null; cannot add duplicate (IllegalStateException)
  - removerDisciplina: disciplina non-null; must exist (IllegalStateException)

## Disciplina (Subject)

Source:
- service/DisciplinaService.java
- entity/Disciplina.java

Input constraints (Service):
- codigo: integer, 1..99 inclusive. Error: ServiceExceptionEnum.CURSO_CODIGO_INVALIDO
- nome: length 1..20 inclusive. Error: ServiceExceptionEnum.CURSO_NOME_INVALIDO

Entity fields (no explicit validations present):
- codigo: int (no bounds in entity)
- nome: String (no null/blank check in entity)
- curso: reference (no null check in entity)
- turmas: add/remove manage list without duplicate checks

## Turma (Class Offering)

Source:
- service/TurmaService.java
- entity/Turma.java

Input constraints (Service):
- codigo: integer, 1..99 inclusive. Error: ServiceExceptionEnum.CURSO_CODIGO_INVALIDO
- ano: integer, 1900..2020 inclusive. Error mapped to CURSO_NOME_INVALIDO

Entity fields (no explicit validations present):
- codigo: int (no bounds in entity)
- ano: int (no bounds in entity)
- semestre: int (no bounds in entity)
- disciplina: reference (no null check in entity)
- inscricoes: add/remove without duplicate checks

## Inscricao (Enrollment)

Source:
- service/InscricaoService.java
- entity/Inscricao.java

Input constraints (Service):
- codigo (used as turma code when building Turma key): integer, 1..999 inclusive. Error: ServiceExceptionEnum.CURSO_CODIGO_INVALIDO
- ano: integer, 1900..2020 inclusive. Error mapped to CURSO_NOME_INVALIDO

Entity fields (no explicit validations present):
- avaliacao1, avaliacao2, media: floats (no range checks)
- numFaltas: int (no range checks)
- situacao: String (no enum binding in this layer; separate enums exist elsewhere in br/com/softwareacademy)
- aluno, turma: references (no null checks in entity)

## General/Utilities

- ServiceExceptionEnum used for validation failures: CURSO_CODIGO_INVALIDO and CURSO_NOME_INVALIDO are reused across domains.
- Many entity setters use Objects.requireNonNull with specific error messages (Aluno, Curso).
- No Bean Validation (JSR 380) annotations are used.
- application.properties is empty; no database-level constraints are visible here.

## Known Inconsistencies and Notes

- Inscricao code range differs (1..999) vs others (1..99).
- Year upper bound is 2020; consider if newer data should be allowed.
- Disciplina, Turma, Inscricao entities lack defensive validations present in Aluno/Curso.
- Service error enums reuse course-related names for non-course validations.

## Checklist for Compatibility Testing in Another Version

For each endpoint/service that creates or updates entities, verify:
- Aluno: matricula in [1,99], nome length in [1,20]
- Curso: codigo in [1,99], nome length in [1,20]
- Disciplina: codigo in [1,99], nome length in [1,20]
- Turma: codigo in [1,99], ano in [1900,2020]
- Inscricao: codigo in [1,999], ano in [1900,2020]

And entity-level invariants:
- Aluno: non-null fields (nome, dataNascimento, estadoCivil, curso), idade >= 0, matricula > 0, no duplicate telefones/inscricoes
- Curso: codigo > 0, nome non-null/non-blank, no duplicate alunos/disciplinas

If the other version implements additional constraints (e.g., max name > 20, year > 2020), flag as divergence.
