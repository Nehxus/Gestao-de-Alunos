# Estrutura do Projeto

## 📁 Estrutura de Diretórios

```
gestao-alunos/
├── src/
│   ├── main/
│   │   ├── java/br/com/gestao/alunos/
│   │   │   ├── config/              # Configurações (Swagger)
│   │   │   ├── controller/          # Controllers REST
│   │   │   ├── dto/                 # Data Transfer Objects
│   │   │   ├── exception/           # Tratamento de exceções
│   │   │   ├── model/               # Entidades JPA
│   │   │   ├── repository/          # Repositórios JPA
│   │   │   └── service/             # Camada de serviço
│   │   └── resources/
│   │       ├── application.properties
│   │       ├── application-dev.properties
│   │       ├── application-prod.properties
│   │       └── data.sql
│   └── test/
│       └── java/br/com/gestao/alunos/
│           ├── controller/          # Testes dos controllers
│           └── service/             # Testes dos services
├── pom.xml                          # Configuração Maven
├── Dockerfile                       # Configuração Docker
├── Procfile                         # Configuração Heroku
├── README.md                        # Documentação
└── .gitignore                       # Arquivos ignorados pelo Git
```

## 🎯 Rotas da API

### Alunos
- `POST /api/alunos` - Criar aluno
- `GET /api/alunos` - Listar todos os alunos
- `GET /api/alunos/{id}` - Buscar aluno por ID
- `PUT /api/alunos/{id}` - Atualizar aluno
- `DELETE /api/alunos/{id}` - Deletar aluno
- `GET /api/alunos/filtro?cursoId=X` - Buscar por curso
- `GET /api/alunos/filtro?semestre=X` - Buscar por semestre
- `GET /api/alunos/filtro?mediaMinima=X` - Buscar por média mínima

### Cursos
- `POST /api/cursos` - Criar curso
- `GET /api/cursos` - Listar todos os cursos
- `GET /api/cursos/{id}` - Buscar curso por ID
- `PUT /api/cursos/{id}` - Atualizar curso
- `DELETE /api/cursos/{id}` - Deletar curso

## 🗄️ Modelo de Dados

### Entidade: Aluno
- id (Long)
- nome (String)
- matricula (String, único)
- email (String, único)
- curso (Curso - ManyToOne)
- semestre (Integer)
- mediaGeral (Double)
- dataMatricula (LocalDate)

### Entidade: Curso
- id (Long)
- nome (String, único)
- descricao (String)
- alunos (List<Aluno> - OneToMany)

## ✅ Requisitos Atendidos

- [x] Spring Boot 3.2.0
- [x] Banco de dados H2 (dev) e PostgreSQL (prod)
- [x] Mínimo 6 rotas HTTP (temos 11 rotas)
- [x] Mínimo 4 atributos na entidade (temos 6 atributos)
- [x] Validações com @NotNull e @Size
- [x] Spring Data JPA
- [x] Relacionamentos JPA (@ManyToOne, @OneToMany)
- [x] Profiles do Spring (dev e prod)
- [x] Swagger/OpenAPI (Springdoc)
- [x] Tratamento de exceções centralizado
- [x] Testes unitários (JUnit 5 e Mockito)
- [x] JaCoCo configurado (90% cobertura)
- [x] README.md completo
- [x] Arquivos para deploy (Dockerfile, Procfile)

