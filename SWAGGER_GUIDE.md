# Guia de Uso do Swagger - Sistema de Gestão de Alunos

## 🚀 Como Acessar o Swagger

### 1. Iniciar a Aplicação

```bash
cd gestao-alunos
mvn spring-boot:run
```

### 2. Acessar o Swagger UI

Após a aplicação iniciar, acesse:

**Swagger UI**: http://localhost:8080/swagger-ui.html

**API Docs (JSON)**: http://localhost:8080/api-docs

## 📚 Endpoints Disponíveis no Swagger

### 🔵 Alunos (API para gerenciamento de alunos)

#### POST /api/alunos
Criar um novo aluno

**Exemplo de Request Body:**
```json
{
  "nome": "João Silva",
  "matricula": "2024001",
  "email": "joao@email.com",
  "cursoId": 1,
  "semestre": 3,
  "mediaGeral": 8.5
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "matricula": "2024001",
  "email": "joao@email.com",
  "cursoId": 1,
  "cursoNome": "Ciência da Computação",
  "semestre": 3,
  "mediaGeral": 8.5
}
```

#### GET /api/alunos
Listar todos os alunos

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "João Silva",
    "matricula": "2024001",
    "email": "joao@email.com",
    "cursoId": 1,
    "cursoNome": "Ciência da Computação",
    "semestre": 3,
    "mediaGeral": 8.5
  }
]
```

#### GET /api/alunos/{id}
Buscar aluno por ID

**Parâmetros:**
- `id` (path): ID do aluno

**Resposta (200 OK):**
```json
{
  "id": 1,
  "nome": "João Silva",
  "matricula": "2024001",
  "email": "joao@email.com",
  "cursoId": 1,
  "cursoNome": "Ciência da Computação",
  "semestre": 3,
  "mediaGeral": 8.5
}
```

#### PUT /api/alunos/{id}
Atualizar um aluno existente

**Parâmetros:**
- `id` (path): ID do aluno

**Exemplo de Request Body:**
```json
{
  "nome": "João Silva Atualizado",
  "matricula": "2024001",
  "email": "joao.novo@email.com",
  "cursoId": 1,
  "semestre": 4,
  "mediaGeral": 9.0
}
```

#### DELETE /api/alunos/{id}
Deletar um aluno

**Parâmetros:**
- `id` (path): ID do aluno

**Resposta (204 No Content)**

#### GET /api/alunos/filtro
Buscar alunos com filtros

**Parâmetros de Query:**
- `cursoId` (opcional): Filtrar por ID do curso
- `semestre` (opcional): Filtrar por semestre
- `mediaMinima` (opcional): Filtrar por média mínima

**Exemplos:**
- `GET /api/alunos/filtro?cursoId=1`
- `GET /api/alunos/filtro?semestre=3`
- `GET /api/alunos/filtro?mediaMinima=8.0`

### 🟢 Cursos (API para gerenciamento de cursos)

#### POST /api/cursos
Criar um novo curso

**Exemplo de Request Body:**
```json
{
  "nome": "Ciência da Computação",
  "descricao": "Curso de Ciência da Computação"
}
```

**Resposta (201 Created):**
```json
{
  "id": 1,
  "nome": "Ciência da Computação",
  "descricao": "Curso de Ciência da Computação"
}
```

#### GET /api/cursos
Listar todos os cursos

**Resposta (200 OK):**
```json
[
  {
    "id": 1,
    "nome": "Ciência da Computação",
    "descricao": "Curso de Ciência da Computação"
  }
]
```

#### GET /api/cursos/{id}
Buscar curso por ID

**Parâmetros:**
- `id` (path): ID do curso

#### PUT /api/cursos/{id}
Atualizar um curso existente

**Parâmetros:**
- `id` (path): ID do curso

**Exemplo de Request Body:**
```json
{
  "nome": "Ciência da Computação",
  "descricao": "Descrição atualizada"
}
```

#### DELETE /api/cursos/{id}
Deletar um curso

**Parâmetros:**
- `id` (path): ID do curso

**Resposta (204 No Content)**

## 🧪 Fluxo de Teste Recomendado

### 1. Criar um Curso Primeiro
```
POST /api/cursos
{
  "nome": "Ciência da Computação",
  "descricao": "Curso de Ciência da Computação"
}
```
⚠️ **Importante**: Você precisa criar um curso antes de criar um aluno, pois o aluno precisa de um `cursoId` válido.

### 2. Listar Cursos
```
GET /api/cursos
```
Anote o `id` do curso criado.

### 3. Criar um Aluno
```
POST /api/alunos
{
  "nome": "João Silva",
  "matricula": "2024001",
  "email": "joao@email.com",
  "cursoId": 1,  // Use o ID do curso criado
  "semestre": 3,
  "mediaGeral": 8.5
}
```

### 4. Listar Alunos
```
GET /api/alunos
```

### 5. Buscar Aluno por ID
```
GET /api/alunos/1
```

### 6. Buscar Alunos com Filtros
```
GET /api/alunos/filtro?cursoId=1
GET /api/alunos/filtro?semestre=3
GET /api/alunos/filtro?mediaMinima=8.0
```

### 7. Atualizar Aluno
```
PUT /api/alunos/1
{
  "nome": "João Silva Atualizado",
  "matricula": "2024001",
  "email": "joao.novo@email.com",
  "cursoId": 1,
  "semestre": 4,
  "mediaGeral": 9.0
}
```

### 8. Deletar Aluno
```
DELETE /api/alunos/1
```

## 📋 Validações

### Aluno
- **nome**: Obrigatório, entre 3 e 100 caracteres
- **matricula**: Obrigatório, entre 5 e 20 caracteres, único
- **email**: Obrigatório, formato válido, único
- **cursoId**: Obrigatório, deve existir
- **semestre**: Obrigatório, entre 1 e 20
- **mediaGeral**: Opcional, entre 0.0 e 10.0

### Curso
- **nome**: Obrigatório, entre 3 e 100 caracteres, único
- **descricao**: Opcional, máximo 255 caracteres

## 🚨 Códigos de Status HTTP

- **200 OK**: Requisição bem-sucedida
- **201 Created**: Recurso criado com sucesso
- **204 No Content**: Recurso deletado com sucesso
- **400 Bad Request**: Dados inválidos ou erro de validação
- **404 Not Found**: Recurso não encontrado
- **500 Internal Server Error**: Erro interno do servidor

## 🔍 Dados de Exemplo

### Cursos de Exemplo
```json
{
  "nome": "Ciência da Computação",
  "descricao": "Curso de Ciência da Computação"
}
```

```json
{
  "nome": "Engenharia de Software",
  "descricao": "Curso de Engenharia de Software"
}
```

```json
{
  "nome": "Sistemas de Informação",
  "descricao": "Curso de Sistemas de Informação"
}
```

### Alunos de Exemplo
```json
{
  "nome": "João Silva",
  "matricula": "2024001",
  "email": "joao.silva@email.com",
  "cursoId": 1,
  "semestre": 3,
  "mediaGeral": 8.5
}
```

```json
{
  "nome": "Maria Santos",
  "matricula": "2024002",
  "email": "maria.santos@email.com",
  "cursoId": 1,
  "semestre": 2,
  "mediaGeral": 9.0
}
```

## 💡 Dicas de Uso

1. **Use o botão "Try it out"** no Swagger para testar os endpoints
2. **Preencha todos os campos obrigatórios** antes de enviar a requisição
3. **Verifique as respostas** para entender os formatos esperados
4. **Use os filtros** para buscar alunos específicos
5. **Crie cursos primeiro** antes de criar alunos
6. **Verifique os códigos de status** para entender o resultado da operação

## 🔗 Links Úteis

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs**: http://localhost:8080/api-docs
- **H2 Console** (dev): http://localhost:8080/h2-console
  - JDBC URL: `jdbc:h2:mem:gestaoalunosdb`
  - Username: `sa`
  - Password: (vazio)

## 📝 Notas

- A aplicação usa H2 (banco em memória) no perfil de desenvolvimento
- Os dados são persistidos enquanto a aplicação estiver rodando
- Ao reiniciar a aplicação, os dados são perdidos (exceto se usar data.sql)
- O Swagger está configurado para ser acessível em http://localhost:8080/swagger-ui.html

