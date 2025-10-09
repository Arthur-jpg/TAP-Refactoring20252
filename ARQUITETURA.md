# Arquitetura da aplicação “Universidade”

Este documento explica como o projeto está organizado (camadas/pacotes), o papel de cada classe principal e, principalmente, como uma informação “anda” pela aplicação em fluxos do tipo: API (HTTP) → Controller → Service → Repository → Banco, e volta mapeada para DTOs. Exemplos concretos foram extraídos do código atual.


## Visão geral das camadas e pacotes

- `controller` (Web/API): expõe endpoints REST (Spring MVC). Recebe requisições HTTP, valida entrada básica, traduze exceções para códigos HTTP e chama a camada de serviço.
- `service` (Regras de negócio + orquestração): concentra validações de negócio, transações (@Transactional), conversão entre DTO e Entidade e coordena repositórios. No código atual, a convenção “RepositoryService” indica serviços que usam Spring Data JPA diretamente.
- `repository` (Acesso a dados com Spring Data JPA): interfaces que estendem `JpaRepository` e expõem consultas por convenção (ex.: `findByMatricula`, `existsByCodigo`, etc.). O Spring implementa a persistência para nós.
- `entity` (Modelo de domínio): classes anotadas com JPA que representam as tabelas e relações (ex.: `Aluno`, `Curso`, `Disciplina`, `Turma`). São usadas pelo JPA/Hibernate para ler e gravar no banco.
- `dto` (Data Transfer Object): objetos “de borda” pensados para troca com a camada web (entrada/saída da API). Evitam expor entidades diretamente e permitem formatos específicos de payload.
- `exception` (Exceções checked de domínio): `ServiceException` (erros de validação/regra de negócio) e `DaoException` (erros de consulta/persistência). Os controllers convertem essas exceções em respostas HTTP adequadas.
- `config` (Configurações): habilita transações (`@EnableTransactionManagement`) e centraliza configs JPA/Swagger, etc.
- `dao` (legado): há DAOs antigos, mas, na prática, os controllers usam os “RepositoryService” junto com `repository`. Considere o pacote `dao` como legado em transição.


## Fluxo típico de uma requisição

Exemplo genérico “happy path” para um POST/GET:

1) Cliente chama a API HTTP
- Ex.: `POST /api/aluno` com JSON de `AlunoDTO`.

2) `controller` recebe e delega
- Ex.: `AlunoController.cadastrarAluno(AlunoDTO)` chama `AlunoRepositoryService.cadastrarAluno(...)`.

3) `service` valida, converte e orquestra
- Valida regras de negócio (`ServiceException` para dados inválidos).
- Busca dados relacionados via `repository` (ex.: curso do aluno).
- Converte DTO → Entidade (`convertToEntity`) e chama o `repository.save(...)`.

4) `repository` executa a operação no banco via JPA/Hibernate
- Implementação gerada pelo Spring Data JPA.

5) Retorno e mapeamento
- Para consultas: entidade → DTO (`convertToDTO`) antes de voltar ao controller.
- Controller transforma exceções em HTTP (200/201/400/404/500) e responde ao cliente.

Diagrama textual:

Cliente HTTP → Controller → Service (@Transactional) → Repository (Spring Data) → Banco de Dados
                                     ↑ conversões DTO⇄Entidade, validações, regras
                                     ↓ exceptions de negócio/DAO mapeadas para HTTP


## Mapeamento por domínio (onde cada coisa acontece)

### Aluno
- Controller: `br.edu.ibmec.controller.AlunoController`
- Service: `br.edu.ibmec.service.AlunoRepositoryService`
- Repository: `br.edu.ibmec.repository.AlunoRepository`
- DTO: `br.edu.ibmec.dto.AlunoDTO`
- Entity: `br.edu.ibmec.entity.Aluno`

Operações e fluxo:
- Buscar: `GET /api/aluno/{matricula}`
  - Controller → `alunoService.buscarAluno(matricula)`
  - Service → `alunoRepository.findByMatricula(...)` → converte Entidade→DTO → retorna
  - Controller → 200 com `AlunoDTO` ou 404 (captura `DaoException`)

- Listar nomes: `GET /api/aluno`
  - Controller → `alunoService.listarAlunos()` → retorna `Collection<Aluno>`
  - Controller mapeia para `List<String>` com `aluno.getNome()`

- Listar completos: `GET /api/aluno/completos`
  - Controller → `alunoService.listarAlunosCompletos()` → lista de `Aluno` → mapeia `convertToDTO`

- Cadastrar: `POST /api/aluno`
  - Controller → `alunoService.cadastrarAluno(AlunoDTO)`
  - Service valida campos (`ServiceException` em caso de dados inválidos)
  - Se `curso > 0`: `cursoRepository.findByCodigo` para validar a referência
  - Converte DTO→Entidade (`convertToEntity`) incluindo `Data` (embedded) e `EstadoCivil`
  - `alunoRepository.save(aluno)`
  - Controller → 201 ou 400/500 conforme exceção

- Alterar: `PUT /api/aluno`
  - Controller → `alunoService.alterarAluno(AlunoDTO)`
  - Service valida, carrega o aluno (`alunoRepository.findById`), atualiza campos, salva
  - Controller → 200 ou 400/500

- Remover: `DELETE /api/aluno/{matricula}`
  - Controller → `alunoService.removerAluno(matricula)`
  - Service verifica existência (`existsById`), `deleteById`
  - Controller → 200 ou 404

Conversões relevantes no serviço:
- `convertToDTO(Aluno)` e `convertToEntity(AlunoDTO, Curso)`
- Mapeamentos auxiliares: `EstadoCivilDTO` ⇄ `EstadoCivil`, `Data.fromString(dto.getDtNascimento())`


### Curso
- Controller: `br.edu.ibmec.controller.CursoController`
- Service: `br.edu.ibmec.service.CursoRepositoryService`
- Repository: `br.edu.ibmec.repository.CursoRepository`
- DTO: `br.edu.ibmec.dto.CursoDTO`
- Entity: `br.edu.ibmec.entity.Curso`

Fluxos (análogos a Aluno): buscar (`findByCodigo`), listar, cadastrar (valida código/nome, verifica duplicidade com `existsByCodigo`, `save`), alterar, remover.


### Disciplina
- Controller: `br.edu.ibmec.controller.DisciplinaController`
- Service: `br.edu.ibmec.service.DisciplinaRepositoryService`
- Repository: `br.edu.ibmec.repository.DisciplinaRepository`
- DTO: `br.edu.ibmec.dto.DisciplinaDTO`
- Entity: `br.edu.ibmec.entity.Disciplina`

Fluxo de cadastro: valida dados, verifica duplicidade com `existsByCodigo`, busca `Curso` se informado (`cursoRepository.findByCodigo`), converte DTO→Entidade, `save`.


### Turma
- Controller: `br.edu.ibmec.controller.TurmaController`
- Service: `br.edu.ibmec.service.TurmaRepositoryService`
- Repository: `br.edu.ibmec.repository.TurmaRepository`
- DTO: `br.edu.ibmec.dto.TurmaDTO`
- Entity: `br.edu.ibmec.entity.Turma` (chave composta modelada via `TurmaId`)

Operações e pontos de atenção:
- Buscar: `GET /api/turma/{codigo}/{ano}/{semestre}` → `findByCodigoAndAnoAndSemestre`
- Listar por disciplina: filtra na memória por `turma.getDisciplina().getCodigo()` e converte para DTO
- Cadastrar: valida campos, carrega `Disciplina` (`disciplinaRepository.findById`), verifica duplicidade (`findByCodigoAndAnoAndSemestre`), converte DTO→Entidade e `save`
- Alterar: carrega a mesma chave, atualiza a disciplina se necessário e `save`
- Remover: carrega por chave e `delete`


## Exemplo ponta-a-ponta: “Cadastrar Aluno”

Caminho completo (dados de entrada em JSON com `AlunoDTO`):

- Cliente → `POST /api/aluno`
- `AlunoController.cadastrarAluno(AlunoDTO)`
  - try { `alunoService.cadastrarAluno(dto)`; retorna 201 }
  - catch `ServiceException` → 400 com mensagem amigável
  - catch `DaoException` → 400 (quando erro de banco) ou 500 em outros casos
- `AlunoRepositoryService.cadastrarAluno(AlunoDTO)`
  - Valida matrícula>0, nome obrigatório → lança `ServiceException` se inválido
  - Verifica duplicidade: `alunoRepository.existsByMatricula(m)` → lança `ServiceException` se já existir
  - Se `curso > 0`: `cursoRepository.findByCodigo(cod)` → lança `DaoException` se não encontrado
  - Converte DTO→Entidade: `convertToEntity(dto, curso)`
    - `Data.fromString(dto.getDtNascimento())`, mapeia `EstadoCivilDTO`→`EstadoCivil`, copia telefones
  - `alunoRepository.save(entidade)`
- Resposta → 201 Created

Resumo do fluxo: Cliente → Controller → Service (valida, carrega curso, converte) → Repositories (`AlunoRepository`, `CursoRepository`) → Banco → volta


## Exemplo ponta-a-ponta: “Buscar Turma”

- Cliente → `GET /api/turma/{codigo}/{ano}/{semestre}`
- `TurmaController.buscarTurma(...)` → `turmaRepositoryService.buscarTurma(codigo, ano, semestre)`
- Service: `turmaRepository.findByCodigoAndAnoAndSemestre(...)`
  - Se null → `DaoException` (Controller retorna 404)
  - Se ok → `convertToDTO(Turma)` e retorna
- Controller → 200 com `TurmaDTO`


## Tratamento de erros e códigos HTTP

- `ServiceException` (regra/validação): geralmente respondido como 400 Bad Request.
- `DaoException` (não encontrado/erro de acesso):
  - 404 Not Found quando recurso ausente (ex.: aluno não encontrado)
  - 500/400 quando erro de banco genérico (no código atual muitas vezes 400)

Os controllers mapeiam explicitamente essas exceções para os status. Exemplos:
- `AlunoController.buscarAluno` → 200 ou 404
- `AlunoController.cadastrarAluno` → 201 ou 400
- `TurmaController.removerTurma` → 200 ou 404


## Transações (@Transactional)

- Os services (`AlunoRepositoryService`, `CursoRepositoryService`, `DisciplinaRepositoryService`, `TurmaRepositoryService`) são anotados com `@Transactional`.
  - Métodos de leitura usam `@Transactional(readOnly = true)` para otimizações.
  - Métodos de escrita rodam dentro de transação e fazem `save/delete` atômicos.
- `TransactionConfig` habilita `@EnableTransactionManagement` para a aplicação.


## Conversão DTO ⇄ Entidade (por que existe?)

- Os DTOs (`dto/*DTO.java`) representam o formato “externo” da API. Permitem:
  - Isolar o modelo de domínio de mudanças de contrato HTTP.
  - Validar com Bean Validation ao entrar na API (`@NotBlank`, `@Min`, etc.).
  - Transformar enums e datas para formatos amigáveis ao cliente.
- As Entidades (`entity/*.java`) representam “como os dados vivem no domínio/banco”. Têm anotações JPA, relacionamentos, invariantes.
- A conversão acontece na camada de serviço para manter o Controller fino e a entidade limpa de preocupações de transporte.

Exemplos no código:
- `AlunoRepositoryService.convertToDTO/convertToEntity`
- Mapeamento `EstadoCivilDTO` ⇄ `EstadoCivil`
- `Data` como objeto embutido (`@Embedded`) em `Aluno`


## Por que tantas classes/camadas?

Separação de responsabilidades e acoplamento baixo:
- Controller: protocolo HTTP e mapeamento de status/códigos — fácil de testar sem banco.
- Service: regras de negócio e orquestração — concentra validações e transações, facilita reutilizar lógica em vários endpoints.
- Repository: persistência declarativa com Spring Data — reduz boilerplate e deixa consultas expressivas.
- DTO/Entity: cada um no seu papel — contratos externos vs. modelo interno.
- Exceptions dedicadas: permitem que o Controller responda adequadamente sem vazar detalhes de infra.

Benefícios: testabilidade, manutenção, evolução independente (ex.: trocar payloads sem mexer no domínio), observabilidade de regras, e suporte melhor a refatorações.


## Referências rápidas por arquivo

- Controllers: `controller/*Controller.java`
- Services: `service/*RepositoryService.java`
- Repositórios: `repository/*Repository.java`
- DTOs: `dto/*DTO.java`
- Entidades: `entity/*.java`
- Exceções: `exception/*.java`
- Config: `config/*.java`
- Legado (não usado pelos controllers atuais): `dao/*`, `resource/*.bak`


## Observações finais

- Há serviços “antigos” (`AlunoService`, `CursoService`, etc.) e DAOs no projeto, mas os controllers atuais já consomem a versão com Spring Data (`*RepositoryService`). Isso é comum em migrações; com o tempo, o legado tende a ser removido.
- A documentação de API é exposta via Springdoc/Swagger (veja `SwaggerConfig` e a dependência `springdoc-openapi-starter-webmvc-ui`).
- Se quiser visualizar os endpoints e payloads, suba o app e acesse a UI do Swagger (geralmente em `/swagger-ui.html` ou `/swagger-ui/index.html`).
