# Sistema de Gestão de Alunos

Microserviço desenvolvido com Spring Boot para gerenciamento de alunos e cursos, implementando uma API RESTful completa com validações, testes unitários e documentação Swagger/OpenAPI.

## 📋 Descrição

Este projeto é um microserviço que permite o gerenciamento completo de alunos e cursos, oferecendo operações CRUD (Create, Read, Update, Delete) e funcionalidades de filtro. O sistema foi desenvolvido seguindo os princípios de Programação Orientada a Objetos (POO) e as melhores práticas do Spring Boot.

## 🎯 Propósito do Sistema

O **Sistema de Gestão de Alunos** foi desenvolvido com o objetivo de gerenciar de forma eficiente e organizada informações sobre alunos e cursos em uma instituição de ensino. O sistema permite:

- **Gerenciamento Completo de Alunos**: Cadastro, consulta, atualização e exclusão de informações de alunos, incluindo dados pessoais, matrícula, email, curso vinculado, semestre atual e média geral.

- **Gerenciamento de Cursos**: Administração de cursos, permitindo criar, listar, buscar, atualizar e excluir informações sobre os cursos disponíveis.

- **Relacionamento entre Entidades**: Estabelece e mantém o relacionamento entre alunos e cursos, garantindo que cada aluno esteja vinculado a um curso válido.

- **Consultas e Filtros**: Oferece funcionalidades de busca e filtro para facilitar a localização de alunos por curso, semestre ou desempenho acadêmico (média mínima).

- **Validação de Dados**: Garante a integridade dos dados através de validações robustas, como campos obrigatórios, formatos válidos e regras de unicidade.

- **Documentação e Testes**: Prover documentação completa da API e testes unitários abrangentes para garantir a qualidade e confiabilidade do sistema.

### Benefícios:
- ✅ **Automatização**: Reduz trabalho manual na gestão de alunos e cursos
- ✅ **Organização**: Centraliza informações de alunos e cursos em um único sistema
- ✅ **Rastreabilidade**: Permite rastrear histórico e desempenho dos alunos
- ✅ **Eficiência**: Facilita buscas e consultas através de filtros
- ✅ **Integridade**: Garante consistência dos dados através de validações
- ✅ **Escalabilidade**: Arquitetura de microserviço permite crescimento futuro
- ✅ **Manutenibilidade**: Código bem estruturado e testado facilita manutenção

## 🚀 Funcionalidades

### Alunos
- ✅ Criar novo aluno
- ✅ Listar todos os alunos
- ✅ Buscar aluno por ID
- ✅ Atualizar dados do aluno
- ✅ Deletar aluno
- ✅ Buscar alunos por curso, semestre ou média mínima

### Cursos
- ✅ Criar novo curso
- ✅ Listar todos os cursos
- ✅ Buscar curso por ID
- ✅ Atualizar dados do curso
- ✅ Deletar curso

## 🛠️ Tecnologias Utilizadas

- **Java 17**
- **Spring Boot 3.2.0**
- **Spring Data JPA**
- **H2 Database** (desenvolvimento)
- **PostgreSQL** (produção)
- **JUnit 5** (testes)
- **Mockito** (mocks para testes)
- **JaCoCo** (cobertura de código)
- **Springdoc OpenAPI** (Swagger)
- **Maven** (gerenciamento de dependências)
- **Lombok** (redução de boilerplate)

## 📦 Pré-requisitos

- Java 17 ou superior
- Maven 3.6+ 
- PostgreSQL (para produção)
- Git

## 🔧 Instalação e Configuração

### 1. Clone o repositório

```bash
git clone <url-do-repositorio>
cd gestao-alunos
```

### 2. Configuração do Banco de Dados

#### Desenvolvimento (H2 - Banco em memória)

O projeto está configurado para usar H2 em memória no perfil de desenvolvimento. Não é necessária nenhuma configuração adicional.

#### Produção (PostgreSQL)

1. Crie um banco de dados PostgreSQL:
```sql
CREATE DATABASE gestaoalunos;
```

2. Configure as variáveis de ambiente (ou arquivo `application-prod.properties`):
```properties
DATABASE_URL=jdbc:postgresql://localhost:5432/gestaoalunos
DATABASE_USERNAME=seu_usuario
DATABASE_PASSWORD=sua_senha
```

### 3. Compilar o projeto

```bash
mvn clean install
```

### 4. Executar a aplicação

#### Modo Desenvolvimento (H2)
```bash
mvn spring-boot:run
```

Ou com perfil explícito:
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=dev
```

#### Modo Produção (PostgreSQL)
```bash
mvn spring-boot:run -Dspring-boot.run.profiles=prod
```

### 5. Acessar a aplicação

- **API Base URL**: http://localhost:8080
- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs
- **H2 Console** (dev): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:gestaoalunosdb`
  - Username: `sa`
  - Password: (vazio)

## 📚 Endpoints da API

### Alunos

#### Criar Aluno
```http
POST /api/alunos
Content-Type: application/json

{
  "nome": "João Silva",
  "matricula": "2024001",
  "email": "joao@email.com",
  "cursoId": 1,
  "semestre": 3,
  "mediaGeral": 8.5
}
```

#### Listar Todos os Alunos
```http
GET /api/alunos
```

#### Buscar Aluno por ID
```http
GET /api/alunos/{id}
```

#### Atualizar Aluno
```http
PUT /api/alunos/{id}
Content-Type: application/json

{
  "nome": "João Silva Atualizado",
  "matricula": "2024001",
  "email": "joao.novo@email.com",
  "cursoId": 1,
  "semestre": 4,
  "mediaGeral": 9.0
}
```

#### Deletar Aluno
```http
DELETE /api/alunos/{id}
```

#### Buscar Alunos com Filtro
```http
GET /api/alunos/filtro?cursoId=1
GET /api/alunos/filtro?semestre=3
GET /api/alunos/filtro?mediaMinima=8.0
```

### Cursos

#### Criar Curso
```http
POST /api/cursos
Content-Type: application/json

{
  "nome": "Ciência da Computação",
  "descricao": "Curso de Ciência da Computação"
}
```

#### Listar Todos os Cursos
```http
GET /api/cursos
```

#### Buscar Curso por ID
```http
GET /api/cursos/{id}
```

#### Atualizar Curso
```http
PUT /api/cursos/{id}
Content-Type: application/json

{
  "nome": "Ciência da Computação",
  "descricao": "Descrição atualizada"
}
```

#### Deletar Curso
```http
DELETE /api/cursos/{id}
```

## 🧪 Testes

### Executar todos os testes
```bash
mvn test
```

### Executar testes com cobertura
```bash
mvn clean test jacoco:report
```

O relatório de cobertura estará disponível em: `target/site/jacoco/index.html`

### Cobertura Mínima
O projeto está configurado para exigir **90% de cobertura de código** nas classes de serviço e controladores.

### Estatísticas de Testes
- **Total de Testes**: 54 testes
- **Cobertura Mínima**: 90%
- **Frameworks**: JUnit 5, Mockito
- **Ferramenta de Cobertura**: JaCoCo

### Documentação de Testes
Para mais detalhes sobre os testes, consulte o arquivo [TESTES.md](TESTES.md) que contém:
- Lista completa de todos os testes implementados
- Descrição de cada cenário testado
- Instruções para executar e verificar a cobertura
- Configuração do JaCoCo

## 📖 Exemplos de Uso com cURL

### Criar um Curso
```bash
curl -X POST http://localhost:8080/api/cursos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "Ciência da Computação",
    "descricao": "Curso de Ciência da Computação"
  }'
```

### Criar um Aluno
```bash
curl -X POST http://localhost:8080/api/alunos \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva",
    "matricula": "2024001",
    "email": "joao@email.com",
    "cursoId": 1,
    "semestre": 3,
    "mediaGeral": 8.5
  }'
```

### Listar Todos os Alunos
```bash
curl -X GET http://localhost:8080/api/alunos
```

### Buscar Aluno por ID
```bash
curl -X GET http://localhost:8080/api/alunos/1
```

### Buscar Alunos por Curso
```bash
curl -X GET "http://localhost:8080/api/alunos/filtro?cursoId=1"
```

### Atualizar Aluno
```bash
curl -X PUT http://localhost:8080/api/alunos/1 \
  -H "Content-Type: application/json" \
  -d '{
    "nome": "João Silva Atualizado",
    "matricula": "2024001",
    "email": "joao.novo@email.com",
    "cursoId": 1,
    "semestre": 4,
    "mediaGeral": 9.0
  }'
```

### Deletar Aluno
```bash
curl -X DELETE http://localhost:8080/api/alunos/1
```

## 🏗️ Arquitetura

O projeto segue o padrão arquitetural em camadas do Spring:

```
gestao-alunos/
├── controller/     # Camada de controle (endpoints REST)
├── service/        # Camada de serviço (lógica de negócio)
├── repository/     # Camada de persistência (acesso ao banco)
├── model/          # Entidades JPA
├── dto/            # Data Transfer Objects
├── exception/      # Tratamento de exceções
└── config/         # Configurações (Swagger, etc.)
```

## 📝 Validações

O projeto implementa validações utilizando Bean Validation:

- **Nome**: Obrigatório, entre 3 e 100 caracteres
- **Matrícula**: Obrigatória, entre 5 e 20 caracteres, única
- **Email**: Obrigatório, formato válido, único
- **Curso**: Obrigatório (referência ao ID do curso)
- **Semestre**: Obrigatório, entre 1 e 20
- **Média Geral**: Opcional, entre 0.0 e 10.0

## 🔒 Tratamento de Exceções

O projeto implementa um tratamento centralizado de exceções utilizando `@RestControllerAdvice`:

- **ResourceNotFoundException**: Recurso não encontrado (404)
- **BusinessException**: Erros de regra de negócio (400)
- **MethodArgumentNotValidException**: Erros de validação (400)
- **Exception**: Erros genéricos (500)

## 👥 Divisão de Tarefas

### Membros do Grupo

1. **Membro 1**: Estrutura do projeto, entidades e repositórios
2. **Membro 2**: Services e lógica de negócio
3. **Membro 3**: Controllers e endpoints REST
4. **Membro 4**: Validações e tratamento de exceções
5. **Membro 5**: Testes unitários e integração
6. **Membro 6**: Documentação e deploy

## 🚀 Deploy em Produção

### Render

1. Crie uma conta no [Render](https://render.com)
2. Conecte seu repositório GitHub
3. Crie um novo serviço Web
4. Configure as variáveis de ambiente:
   - `SPRING_PROFILES_ACTIVE=prod`
   - `DATABASE_URL=jdbc:postgresql://...`
   - `DATABASE_USERNAME=...`
   - `DATABASE_PASSWORD=...`
5. Configure o build command: `mvn clean install`
6. Configure o start command: `java -jar target/gestao-alunos-1.0.0.jar`

### Heroku

1. Instale o [Heroku CLI](https://devcenter.heroku.com/articles/heroku-cli)
2. Faça login: `heroku login`
3. Crie a aplicação: `heroku create gestao-alunos`
4. Adicione o addon PostgreSQL: `heroku addons:create heroku-postgresql:hobby-dev`
5. Configure as variáveis de ambiente:
   ```bash
   heroku config:set SPRING_PROFILES_ACTIVE=prod
   ```
6. Faça o deploy: `git push heroku main`

### Link da API em Produção

**Link público**: [Adicione aqui o link da sua API em produção]

## 📄 Licença

Este projeto está sob a licença Apache 2.0.

## 👨‍💻 Desenvolvido por

Equipe de Desenvolvimento - Gestão de Alunos

## 📋 Checklist do Projeto

Para verificar o status completo do projeto, incluindo todos os requisitos atendidos, funcionalidades implementadas e testes realizados, consulte o arquivo [CHECKLIST_PROJETO.md](CHECKLIST_PROJETO.md).

O checklist inclui:
- ✅ Requisitos obrigatórios do projeto
- ✅ Funcionalidades implementadas
- ✅ Testes unitários (54 testes, 90%+ de cobertura)
- ✅ Documentação completa
- ✅ Configuração de deploy
- ✅ Estatísticas do projeto

## 📞 Contato

Para dúvidas ou sugestões, entre em contato através do email: contato@gestaoalunos.com

#   G e s t - o - d e - A l u n o s  
 