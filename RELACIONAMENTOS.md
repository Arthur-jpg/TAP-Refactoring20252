# Relacionamentos do Modelo Universidade

Este documento descreve, para cada relacionamento do diagrama (Aluno, Curso, Disciplina, Turma, Professor e Inscrição), como a camada de entidades, DTO/Controller, Services e o banco de dados colaboram para garantir as cardinalidades. Sempre que existir lacuna, ela é sinalizada explicitamente.

## 1. Curso ⇄ Disciplina (1:N obrigatório)
**Entidades**  
- `Curso` mantém `@OneToMany(mappedBy = "curso", cascade = ALL, orphanRemoval = true)` (`src/main/java/br/edu/ibmec/entity/Curso.java`).  
- `Disciplina` possui `@ManyToOne(optional = false)` com `@JoinColumn(name = "curso_codigo", nullable = false)` (`src/main/java/br/edu/ibmec/entity/Disciplina.java`). Isto exige um objeto `Curso` válido ao construir a disciplina.

**DTO / Controller**  
- `DisciplinaDTO` traz o código do curso (`curso`) como campo obrigatório.  
- `DisciplinaController` simplesmente delega para o service; a validação acontece no nível de serviço.

**Service / Validações**  
- `DisciplinaRepositoryService.cadastrarDisciplina` carrega o curso via `CursoRepository` antes de instanciar a entidade. Sem um curso existente, lança `DaoException`.  
- Ao alterar, o mesmo fluxo é repetido, garantindo que uma disciplina nunca fique sem curso associado.

**Banco de Dados**  
- O Hibernate cria a coluna `disciplinas.curso_codigo` com FK obrigatória para `cursos.codigo` (por causa de `nullable = false`).  
- `cascade = ALL` + `orphanRemoval = true` em `Curso` faz com que a remoção de um curso delete (ou exija remoção prévia) das disciplinas dependentes, alinhando-se à composição indicada pelo diamante no diagrama.

## 2. Turma ⇄ Professor (N:1 obrigatório)
**Entidades**  
- `Turma` agora possui `@ManyToOne(fetch = LAZY, optional = false)` para `Professor` e persiste `professor_id` (`src/main/java/br/edu/ibmec/entity/Turma.java:54`).  
- `Professor` expõe `@OneToMany(mappedBy = "professor")` para acompanhar as turmas que leciona (`src/main/java/br/edu/ibmec/entity/Professor.java:36`).

**DTO / Controller**  
- `TurmaDTO` inclui `professorId` obrigatório e os controllers simplesmente repassam o DTO para o service.

**Service / Validações**  
- `TurmaRepositoryService` valida o `professorId`, chama `ProfessorRepository` para garantir existência e associa o objeto antes de salvar/alterar (`src/main/java/br/edu/ibmec/service/TurmaRepositoryService.java:55`).  
- Alterações em turmas também atualizam o professor responsável.

**Banco de Dados**  
- A migration `V1__align_relationships.sql` cria `turmas.professor_id`, copia dados antigos de `disciplinas.professor_id`, aplica `NOT NULL`, adiciona a FK `fk_turmas_professor` e remove o campo da tabela `disciplinas`.  
- Agora o banco garante via constraint que cada turma sempre referencie um professor válido.

## 3. Disciplina ⇄ Turma (1:N obrigatório)
**Entidades**  
- `Turma` inclui `@ManyToOne(optional = false)` para `Disciplina` e armazena a chave composta própria com `@IdClass(TurmaId)`.  
- `Disciplina` mantém `@OneToMany(mappedBy = "disciplina", cascade = ALL, orphanRemoval = true)` para controlar o ciclo de vida das turmas.

**DTO / Controller**  
- `TurmaDTO` obriga informar `disciplina` (`@NotNull`).  
- `TurmaController` expõe endpoints que recebem código/ano/semestre e delega ao service.

**Service / Validações**  
- `TurmaRepositoryService` valida intervalo de código/ano/semestre e chama `obterDisciplina(int codigo)` antes de salvar. Se a disciplina não existir, é lançado `DaoException`.  
- A chave composta impede turmas duplicadas no mesmo período; o service reforça isso conferindo existência antes de salvar.

**Banco de Dados**  
- A tabela `turmas` contém `disciplina_codigo` como FK obrigatória para `disciplinas.codigo`.  
- Como `Disciplina` usa `cascade = ALL`, deletar disciplina remove (ou exige remoção) das turmas associadas.

## 4. Turma ⇄ Aluno (N:M por Inscrição)
**Entidades**  
- `Aluno` tem `@OneToMany(mappedBy = "aluno", cascade = ALL, orphanRemoval = true)` para `Inscricao`.  
- `Inscricao` guarda dois `@ManyToOne(optional = false)`: um para `Aluno` (`aluno_matricula`) e outro para `Turma` (três `@JoinColumn` compondo a FK para `turmas`).  
- `Turma` também mantém `@OneToMany(mappedBy = "turma")` com `Inscricao`.

**DTO / Controller**  
- `InscricaoDTO` exige matrícula do aluno, código da turma, ano e semestre.  
- `InscricaoController` usa o DTO para expor endpoints de cadastro/listagem/remoção; a lógica de integridade fica no service.

**Service / Validações**  
- `InscricaoRepositoryService.cadastrarInscricao` verifica duplicidade (`existsByAlunoMatriculaAndTurma...`) antes de salvar.  
- Ele recupera o `Aluno` e a `Turma` usando os respectivos repositórios; se qualquer um não existir, lança `DaoException`.  
- Ao remover, o service busca pela combinação completa para garantir que a exclusão respeite a chave de negócio.

**Banco de Dados**  
- `inscricoes` possui FK obrigatória para `alunos.matricula` e outra para `turmas (codigo, ano, semestre)`.  
- A constraint `uk_inscricao_aluno_turma` (definida via JPA + Flyway) impede duplicidade da combinação aluno/turma diretamente no banco.

## 5. Aluno ⇄ Curso (1:N obrigatório)
**Entidades**  
- `Aluno` ganhou `@ManyToOne(optional = false)` para `Curso` (`src/main/java/br/edu/ibmec/entity/Aluno.java:42`) e validação defensiva em `setCurso`.  
- `Curso` mantém coleções tanto de disciplinas quanto de alunos (`src/main/java/br/edu/ibmec/entity/Curso.java:41`), permitindo navegação inversa.

**DTO / Controller**  
- `AlunoDTO` agora contém `cursoCodigo` obrigatório (e `cursoNome` para leitura) (`src/main/java/br/edu/ibmec/dto/AlunoDTO.java:24`).  
- Controllers apenas repassam o DTO, enquanto o service valida.

**Service / Validações**  
- `AlunoRepositoryService` verifica o código do curso, consulta `CursoRepository` e injeta o objeto antes de salvar/alterar (`src/main/java/br/edu/ibmec/service/AlunoRepositoryService.java:54`).  
- Qualquer tentativa de cadastrar/atualizar aluno com curso inexistente gera `ServiceException`.

**Banco de Dados**  
- A migration adiciona `curso_codigo` à tabela `alunos`, preenche registros nulos com o menor curso existente, marca como `NOT NULL` e cria a FK `fk_alunos_curso`.  
- A constraint garante que todo aluno esteja vinculado a um curso válido mesmo fora da aplicação.

## 6. Observações gerais de integridade
- O `application.properties` aponta para o schema `universidade_v2`, enquanto `consulta_db.sh` consulta `universidade_db`; alinhe os ambientes antes de auditar dados reais.  
- Como o projeto depende de `spring.jpa.hibernate.ddl-auto=update`, é recomendável migrar para migrations versionadas ao alterar relacionamentos (especialmente para adicionar o vínculo Aluno→Curso e o índice único em Inscrição).  
- Quando uma regra só é garantida no serviço (por exemplo, unicidade de inscrição), considere reforçar com constraints de banco para evitar corrupção em cenários fora da aplicação.

Com os ajustes acima, todo o modelo físico/logico ficará aderente ao diagrama fornecido.
