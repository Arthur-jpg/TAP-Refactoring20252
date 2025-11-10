# Universidade — Projeto Spring Boot

Este repositório contém uma aplicação Spring Boot (exemplo acadêmico) que usa Spring Data JPA para persistência e foi configurada para criar o banco de dados automaticamente ao iniciar (quando possível).

O passo a passo abaixo foi testado pelo autor: ao clonar o repositório e executar a aplicação com Maven, o banco de dados MySQL foi criado automaticamente e a API ficou funcional.

## Modelo de domínio alinhado ao diagrama AP2

O domínio foi enxugado para refletir apenas as entidades presentes no quadro branco enviado:

- `Aluno` — identificado por matrícula, relaciona-se com `Turma` via `Inscrição`.
- `Turma` — identificada por código/ano/semestre e vinculada obrigatoriamente a uma `Disciplina`.
- `Disciplina` — parte de um `Curso` e ministrada por exatamente um `Professor`.
- `Curso` — agrega disciplinas (composição) e mantém código/nome.
- `Professor` — novo agregado com id e nome, podendo lecionar várias disciplinas.
- `Inscrição` — associação entre `Aluno` e `Turma` (sem notas/campos extras, apenas a relação).

Campos que não estavam na modelagem (telefones, estado civil, avaliações, etc.) foram removidos para manter o modelo limpo.

## Sumário

- Pré-requisitos
- Clonando o repositório
- Configuração do banco de dados (MySQL)
- Rodando a aplicação
- Endpoints úteis (Swagger/OpenAPI)
- Como a criação automática do banco funciona
- Problemas comuns e soluções
- Testes
- Observações finais

## Pré-requisitos

- Java 17 (JDK 17)
- Maven 3.6+ (ou utilize o wrapper Maven incluido: `./mvnw`)
- MySQL rodando localmente (ou acesso a um servidor MySQL)
- Git

Confirme sua versão do Java:

```bash
java -version
```

Deve exibir algo como `openjdk version "17.x.x"`.

## Clonando o repositório

No terminal (pasta onde quer colocar o projeto):

```bash
git clone <URL_DO_REPOSITORIO>
cd TAP-Refactoring20252
```

Substitua `<URL_DO_REPOSITORIO>` pela URL do repositório remoto (HTTPS ou SSH).

## Configuração do banco de dados (MySQL)

A aplicação está configurada por padrão para conectar em um MySQL local com as seguintes credenciais (arquivo `src/main/resources/application.properties`):

- URL: jdbc:mysql://localhost:3306/universidade_v2?createDatabaseIfNotExist=true&useSSL=false&allowPublicKeyRetrieval=true&serverTimezone=America/Sao_Paulo
- Usuário: `root`
- Senha: `admin`

Observações:
- O parâmetro `createDatabaseIfNotExist=true` (na URL) permite que o MySQL crie o banco `universidade_v2` automaticamente se o usuário tiver permissão.
- A propriedade `spring.jpa.hibernate.ddl-auto=update` faz o Hibernate ajustar o esquema automaticamente conforme as entidades.

Se quiser usar outras credenciais ou um servidor remoto, edite `src/main/resources/application.properties` (ou use variáveis de ambiente) antes de iniciar a aplicação. Exemplo com variáveis de ambiente:

```bash
export SPRING_DATASOURCE_URL="jdbc:mysql://meu-host:3306/universidade_v2?createDatabaseIfNotExist=true"
export SPRING_DATASOURCE_USERNAME="meu_usuario"
export SPRING_DATASOURCE_PASSWORD="minha_senha"
```

No macOS com zsh, você pode exportar no terminal atual; para persistir, coloque no `~/.zshrc`.

### Criando o usuário e banco manualmente (opcional)

Se você preferir criar o banco e o usuário manualmente no MySQL:

1. Acesse o MySQL como root:

```bash
mysql -u root -p
# digite a senha do root
```

2. Execute os comandos (substitua `admin` pela senha desejada):

```sql
CREATE DATABASE IF NOT EXISTS universidade_v2 CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
CREATE USER IF NOT EXISTS 'root'@'localhost' IDENTIFIED BY 'admin';
GRANT ALL PRIVILEGES ON universidade_v2.* TO 'root'@'localhost';
FLUSH PRIVILEGES;
```

> Dica: para ambiente de desenvolvimento é comum usar um usuário com permissões amplas; em produção, crie um usuário com permissões mínimas necessárias.

## Rodando a aplicação

No diretório do projeto você pode usar o wrapper Maven incluído (recomendado) ou o Maven instalado globalmente.

Com o wrapper (Unix/macOS):

```bash
./mvnw spring-boot:run
```

Ou com Maven local:

```bash
mvn spring-boot:run
```

Ou gerar um jar e executar:

```bash
./mvnw clean package -DskipTests
java -jar target/universidade-0.0.1-SNAPSHOT.jar
```

Ao iniciar, você verá logs mostrando a conexão com o banco e o Hibernate atualizando/criando as tabelas. A aplicação estará disponível em http://localhost:8080 por padrão (configurado em `application.properties`).

Se você usou variáveis de ambiente para sobrescrever as configurações do banco, garanta que elas estejam definidas no mesmo terminal antes de iniciar.

## Endpoints úteis

- Swagger UI (documentação interativa): http://localhost:8080/swagger-ui.html
- OpenAPI JSON: http://localhost:8080/api-docs

Também existem diversos controllers no projeto (`AlunoController`, `CursoController`, `DisciplinaController`, `TurmaController`, `InscricaoController`) com endpoints REST para manipular as entidades.

## Como a criação automática do banco funciona

Duas configurações trabalham juntas:

1. Na URL do datasource há `createDatabaseIfNotExist=true`, parâmetro do conector MySQL que instrui o servidor a criar o schema se ele não existir (desde que o usuário tenha permissão).
2. `spring.jpa.hibernate.ddl-auto=update` faz com que o Hibernate compare as entidades JPA com o esquema e execute alterações necessárias (criação/alteração de tabelas/colunas) sem excluir dados.

Isto permite iniciar a aplicação sem criar manualmente o banco e ainda assim ter as tabelas geradas automaticamente.

## Problemas comuns e soluções

1. Erro de conexão (Access denied / authentication):
   - Verifique usuário/senha no `application.properties`.
   - Teste a conexão direta com o MySQL usando `mysql -u root -p -h localhost -P 3306`.

2. Porta 8080 ocupada:
   - Pare o serviço que está usando a porta ou mude a porta editando `server.port` em `application.properties`.

3. Versão do Java incompatível:
   - Projeto configurado para Java 17 no `pom.xml`. Instale JDK 17 ou a versão apropriada.

4. Permissões para criar o banco automaticamente:
   - Se `createDatabaseIfNotExist=true` falhar, crie o banco manualmente conforme a seção acima e tente novamente.

5. Dependências faltando / build falhando:
   - Execute `./mvnw clean package` para ver os erros completos. Se faltarem credenciais do Maven ou problemas de rede, corrija antes de rodar.

## Testes

O projeto contém testes básicos de Spring Boot. Execute:

```bash
./mvnw test
```

## Observações finais

- O autor do repositório testou os passos acima: ao clonar e executar a aplicação, o banco foi criado automaticamente e a aplicação ficou funcional.
- Em produção, reveja configurações de `ddl-auto`, credenciais e parâmetros de criação automática de banco — essas opções são práticas para desenvolvimento, mas podem ser perigosas em produção.

Se quiser, posso:
- Adicionar instruções para rodar com Docker (MySQL + app)
- Incluir um script `docker-compose.yml` para facilitar a execução local
- Documentar endpoints principais com exemplos de requisições curl

---

README gerado automaticamente pelo assistente para o projeto.
