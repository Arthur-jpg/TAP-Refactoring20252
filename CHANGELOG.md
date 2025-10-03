# Changelog - Refatoração Clean Code

Este arquivo documenta todas as mudanças realizadas no código original do professor seguindo os princípios de Clean Code (Capítulos 1-8).

## [1.0.0] - 2025-10-03

### 🔄 **Refatoração Completa - Classes Entity**

#### **Classe Aluno** (`src/main/java/br/edu/ibmec/entity/Aluno.java`)

##### ✨ **Adicionado**
- Documentação JavaDoc da classe
- Validações nos setters:
  - `setMatricula()`: Valida números positivos
  - `setNome()`: Valida nomes não nulos/vazios e remove espaços
  - `setIdade()`: Valida idades não negativas
- Métodos utilitários:
  - `temInscricaoAtiva()`: Verifica se há inscrições ativas
  - `getQuantidadeInscricoes()`: Retorna quantidade de inscrições
- Proteção de encapsulamento:
  - `getInscricoes()`: Retorna cópia defensiva
  - `getTelefones()`: Retorna cópia defensiva
  - `setInscricoes()`: Aceita e cria cópia defensiva
  - `setTelefones()`: Aceita e cria cópia defensiva

##### 🔧 **Modificado**
- Melhorada formatação geral da classe
- Removidos espaços em branco desnecessários
- Renomeados métodos para português:
  - `addInscricao()` → `adicionarInscricao()`
  - `removeInscricao()` → `removerInscricao()`
- Campo `matriculaAtiva` → `isMatriculaAtiva` (convenção boolean)
- Uso do diamond operator (`<>`) nas inicializações
- Validações para prevenir elementos nulos e duplicados

##### 🛡️ **Segurança**
- Prevenção de `NullPointerException` em todos os métodos
- Validação de argumentos com `IllegalArgumentException`
- Proteção contra modificação externa de collections

---

#### **Classe Curso** (`src/main/java/br/edu/ibmec/entity/Curso.java`)

##### ✨ **Adicionado**
- Documentação JavaDoc da classe
- Validações nos setters:
  - `setCodigo()`: Valida códigos positivos
  - `setNome()`: Valida nomes não nulos/vazios e remove espaços
- Métodos utilitários:
  - `getQuantidadeAlunos()`: Retorna número de alunos
  - `getQuantidadeDisciplinas()`: Retorna número de disciplinas
  - `possuiAluno()`: Verifica se aluno está matriculado
  - `possuiDisciplina()`: Verifica se disciplina está no curso
- Proteção de encapsulamento:
  - `getAlunos()`: Retorna cópia defensiva
  - `getDisciplinas()`: Retorna cópia defensiva

##### 🔧 **Modificado**
- Melhorada formatação geral da classe
- Removidos espaços em branco desnecessários
- Renomeados métodos para português:
  - `addAluno()` → `adicionarAluno()`
  - `removeAluno()` → `removerAluno()`
  - `addDisciplina()` → `adicionarDisciplina()`
  - `removeDisciplina()` → `removerDisciplina()`
- Uso do diamond operator (`<>`) nas inicializações
- Validações para prevenir elementos nulos e duplicados

##### 🛡️ **Segurança**
- Prevenção de `NullPointerException` em todos os métodos
- Validação de argumentos com `IllegalArgumentException`
- Proteção contra modificação externa de collections

---

## 📋 **Princípios Clean Code Aplicados**

### **Capítulo 1 - Código Limpo**
- ✅ Removido código morto e espaços desnecessários
- ✅ Melhorada legibilidade geral

### **Capítulo 2 - Nomes Significativos**
- ✅ Métodos renomeados para português (mais claro no contexto)
- ✅ Variáveis com nomes mais descritivos
- ✅ Convenções boolean aplicadas (`isMatriculaAtiva`)

### **Capítulo 3 - Funções**
- ✅ Métodos pequenos e focados
- ✅ Responsabilidade única por método
- ✅ Métodos utilitários adicionados

### **Capítulo 4 - Comentários**
- ✅ JavaDoc adicionado onde necessário
- ✅ Comentários desnecessários removidos

### **Capítulo 5 - Formatação**
- ✅ Formatação consistente
- ✅ Espaçamento padronizado
- ✅ Organização lógica dos métodos

### **Capítulo 6 - Objetos e Estruturas de Dados**
- ✅ Encapsulamento melhorado
- ✅ Cópias defensivas implementadas
- ✅ Proteção de estado interno

### **Capítulo 7 - Tratamento de Erro**
- ✅ Validações com exceções apropriadas
- ✅ Prevenção de estados inválidos
- ✅ Mensagens de erro claras

### **Capítulo 8 - Limites**
- ✅ Uso apropriado de Collections
- ✅ APIs bem definidas
- ✅ Interfaces claras

---

## [2.0.0] - 2025-10-03

### 🚀 **IMPLEMENTAÇÃO DE PERSISTÊNCIA COM MYSQL**

#### **🗄️ Migração para Arquitetura em Camadas com Banco de Dados**

##### ✨ **Adicionado**
- **Anotações JPA** nas entidades:
  - `Aluno`: `@Entity`, `@Table`, `@Id`, `@Column`, `@ManyToOne`, `@OneToMany`, `@Embedded`, `@ElementCollection`
  - `Curso`: `@Entity`, `@Table`, `@Id`, `@Column`, `@OneToMany`
  - `Data`: `@Embeddable`, `@Column`
- **Interfaces DAO**:
  - `AlunoDAO`: Interface para operações de persistência de alunos
  - `CursoDAO`: Interface para operações de persistência de cursos
- **Implementações DAO com JPA**:
  - `AlunoDAOImpl`: Implementação JPA com `EntityManager`
  - `CursoDAOImpl`: Implementação JPA com `EntityManager`
- **Configuração MySQL**:
  - Conexão com banco `universidade_db`
  - Pool de conexões Hikari
  - Hibernate DDL auto-update
  - Logging SQL habilitado
- **Configuração JPA**:
  - `JpaConfig`: Configuração de repositórios e transações
  - `@EnableTransactionManagement`

##### 🔧 **Modificado**
- **AlunoService**:
  - Migrado de `EscolaDAO` para `AlunoDAO` + `CursoDAO`
  - Adicionado `@Service` e `@Autowired`
  - Validações melhoradas (matrícula até 99999, nome até 100 chars)
  - Tratamento de erro aprimorado
- **CursoService**:
  - Migrado de `EscolaDAO` para `CursoDAO`
  - Adicionado `@Service` e `@Autowired`
  - Validações melhoradas (código até 99999, nome até 100 chars)
  - Tratamento de erro aprimorado

##### 🛡️ **Segurança e Validações**
- Transações ACID garantidas pelo JPA
- Validações de integridade referencial
- Prevenção de remoção de cursos com alunos
- Verificação de existência antes de operações
- Pool de conexões configurado para performance

##### 📋 **Estrutura do Banco**
- Tabela `alunos`: Dados principais + campos de data embedded
- Tabela `cursos`: Código e nome do curso
- Tabela `aluno_telefones`: Telefones como collection
- Relacionamentos:
  - `Aluno` ManyToOne `Curso`
  - `Curso` OneToMany `Aluno`

---

## [1.0.1] - 2025-10-03

### 🛠️ **Correções de Compatibilidade**

#### **Correção de Métodos Refatorados**
- ✅ **AlunoService.java**: Substituído `curso.getAlunos().add(aluno)` → `curso.adicionarAluno(aluno)`
- ✅ **EscolaDAO.java**: Substituído `curso.getAlunos().add(aluno)` → `curso.adicionarAluno(aluno)`

**Motivo**: Com a implementação de cópias defensivas, o método `getAlunos()` agora retorna uma cópia da lista. Adicionar elementos diretamente na lista retornada não afeta a lista original da classe. A correção garante que os alunos sejam adicionados corretamente ao curso.

---

## 🎯 **Próximos Passos**

### **Pendente**
- [ ] Revisar outras classes entity (Data, EstadoCivil, Turma, Disciplina)
- [x] ✅ Atualizar classes de service que usam os métodos renomeados
- [x] ✅ Verificar impacto nas classes DAO e Resource  
- [ ] Refatorar classes Service seguindo Clean Code
- [ ] Migrar para Spring Boot completo
- [ ] Implementar persistência MySQL
- [ ] Testes unitários para validar as mudanças

---

## 📝 **Notas**

- **Construtores mantidos**: Conforme solicitação, todos os construtores originais foram preservados
- **Compatibilidade**: Getters e setters básicos mantêm compatibilidade
- **Métodos renomeados**: Métodos add/remove foram renomeados mas mantêm funcionalidade equivalente
- **Validações**: Novas validações podem quebrar código que passava valores inválidos

---

*Última atualização: 03/10/2025*