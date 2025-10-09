# Camadas da aplicação e comparação com MVC

Este documento explica como as camadas do projeto se relacionam e exemplifica, de ponta a ponta, o fluxo de uma requisição para criar um Curso. Ao final, compara essa arquitetura com o padrão MVC clássico.


## Camadas e responsabilidades (o que fala com o quê)

- Controller (Web/API)
  - Onde: `src/main/java/br/edu/ibmec/controller/*Controller.java`
  - Papel: expõe endpoints HTTP (REST), recebe/retorna JSON, traduz exceções para códigos HTTP. Não contém regra de negócio.
  - Fala com: Service.

- Service (Regras de negócio e orquestração)
  - Onde: `src/main/java/br/edu/ibmec/service/*RepositoryService.java`
  - Papel: valida regras, controla transações (`@Transactional`), converte DTO ⇄ Entidade, coordena múltiplos repositórios.
  - Fala com: Repository (e às vezes com outros Services). É a “cola” entre Controller e Repository.

- Repository (Acesso a dados com Spring Data JPA)
  - Onde: `src/main/java/br/edu/ibmec/repository/*Repository.java`
  - Papel: persistência (consultas/saves) com JPA/Hibernate. Implementação gerada pelo Spring a partir das interfaces.
  - Fala com: Service e o Banco de Dados.

- Entity (Modelo de domínio/JPA)
  - Onde: `src/main/java/br/edu/ibmec/entity/*.java`
  - Papel: representa tabelas e relações do banco, com anotações JPA.
  - Fala com: Repository e Service (quando convertido para DTO ou manipulado em regras).

- DTO (Data Transfer Object)
  - Onde: `src/main/java/br/edu/ibmec/dto/*.java`
  - Papel: contrato de entrada/saída da API (payloads JSON). Evita expor entidades diretamente e aplica Bean Validation.
  - Fala com: Controller (entrada/saída) e Service (conversão para entidade).

- Exception (Exceções de negócio e de dados)
  - Onde: `src/main/java/br/edu/ibmec/exception/*.java`
  - Papel: `ServiceException` (regra/validação) e `DaoException` (dados/acesso). Controllers mapeiam para HTTP 400/404/500.

- Config (Infra e cross-cutting)
  - Onde: `src/main/java/br/edu/ibmec/config/*.java`
  - Papel: transações, JPA e Swagger/OpenAPI.

Diagrama textual de relacionamento:

Cliente HTTP → Controller → Service (@Transactional) → Repository (Spring Data) → Banco
                                   ↑ DTO⇄Entidade, validações, regras
                                   ↓ Exceptions mapeadas para HTTP no Controller


## Fluxo: criar um Curso (POST /api/curso)

- Objetivo: cadastrar um curso novo com código e nome.
- Arquivos envolvidos:
  - Controller: `CursoController`
  - Service: `CursoRepositoryService`
  - Repository: `CursoRepository`
  - Entidade: `Curso`
  - DTO: `CursoDTO`

Passo a passo:
1) Cliente envia POST /api/curso com JSON (ex.):
   - `{ "codigo": 10, "nome": "Engenharia de Software" }`

2) Controller recebe e delega
   - Método: `CursoController.cadastrarCurso(CursoDTO)`
   - Ação: chama `cursoService.cadastrarCurso(dto)` e captura exceções para responder com o status HTTP correto.

3) Service valida e orquestra
   - Método: `CursoRepositoryService.cadastrarCurso(CursoDTO)`
   - Validações: código > 0; nome obrigatório e ≤ 100 chars; código não duplicado (`existsByCodigo`).
   - Conversão: DTO → Entidade (`new Curso(dto.getCodigo(), dto.getNome().trim())`).
   - Persistência: `cursoRepository.save(curso)`.

4) Repository persiste via JPA/Hibernate
   - Interface: `CursoRepository extends JpaRepository<Curso, Integer>`
   - O Spring implementa `save` e os métodos derivados por convenção.

5) Resposta
   - Se tudo OK: Controller retorna `201 Created` e mensagem “Curso cadastrado com sucesso”.
   - Se inválido: `ServiceException` → Controller retorna `400 Bad Request` com a mensagem apropriada.
   - Se problema de dados/banco: `DaoException` → Controller retorna `400/500` conforme o caso.

Resumo em uma linha:
POST /api/curso → CursoController → CursoRepositoryService (valida + converte) → CursoRepository.save → Banco → 201 Created


## Comparação com MVC clássico

- MVC tradicional (camadas “puras”):
  - Model: regras de negócio + persistência + estado do domínio.
  - View: interface de apresentação (HTML, telas).
  - Controller: recebe inputs da View e chama o Model.

- No projeto (API REST com Spring):
  - Controller ≈ Controller do MVC: recebe HTTP/JSON, retorna HTTP/JSON, sem regra de negócio.
  - Model é “particionado” em várias peças para reduzir acoplamento:
    - Entidades JPA (estado do domínio, mapeamento do banco)
    - Repositórios (persistência)
    - Services (regras/orquestração/transações)
    - DTOs (contrato externo da API)
  - View: não há view server-side (ex.: Thymeleaf). A “View” é o JSON devolvido; quem renderiza é o cliente (front-end, mobile, etc.).

Por que essa variação do MVC?
- Claridade de responsabilidades: cada peça faz uma coisa específica (Single Responsibility).
- Testabilidade: Services podem ser testados sem HTTP; Repositories podem ser mockados.
- Evolução segura: mudar payloads (DTOs) sem alterar Entidades; trocar consultas sem tocar Controller.


## Tabela mental rápida

- Controller: HTTP in/out, sem regra.
- Service: regras + transações + conversões.
- Repository: CRUD e queries.
- Entity: como os dados vivem no domínio/banco.
- DTO: como os dados trafegam na API.


## Códigos HTTP (como decidimos a resposta)

- 201 Created: criado com sucesso (ex.: POST /api/curso).
- 400 Bad Request: validação/regra violada (`ServiceException`).
- 404 Not Found: recurso não existe (ex.: buscar curso inexistente).
- 500 Internal Server Error: erro inesperado de infraestrutura.


## Onde ver no código

- Criar curso (POST): `CursoController.cadastrarCurso` → `CursoRepositoryService.cadastrarCurso` → `CursoRepository.save` → `Curso`/`CursoDTO`.
- Demais domínios seguem o mesmo padrão (Aluno, Disciplina, Turma, Inscrição).
