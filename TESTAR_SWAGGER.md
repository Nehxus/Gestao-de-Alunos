# 🚀 Como Testar a Aplicação no Swagger

## ⚡ Início Rápido

### 1. Iniciar a Aplicação

Abra um terminal e execute:

```bash
cd /home/nba_rudeboybr/Documentos/SAMUEL/gestao-alunos
mvn spring-boot:run
```

### 2. Aguardar a Inicialização

Aguarde até ver a mensagem:
```
Started GestaoAlunosApplication in X.XXX seconds
```

### 3. Acessar o Swagger

Abra seu navegador e acesse:

**🌐 Swagger UI**: http://localhost:8080/swagger-ui.html

## 📋 Passo a Passo para Testar

### Passo 1: Criar um Curso

1. No Swagger, encontre a seção **"Cursos"**
2. Clique em `POST /api/cursos`
3. Clique no botão **"Try it out"**
4. No campo **Request body**, cole o JSON:
```json
{
  "nome": "Ciência da Computação",
  "descricao": "Curso de Ciência da Computação"
}
```
5. Clique em **"Execute"**
6. Anote o `id` retornado na resposta (geralmente será `1`)

### Passo 2: Criar um Aluno

1. No Swagger, encontre a seção **"Alunos"**
2. Clique em `POST /api/alunos`
3. Clique no botão **"Try it out"**
4. No campo **Request body**, cole o JSON (use o `cursoId` do curso criado):
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
5. Clique em **"Execute"**
6. Verifique a resposta - deve retornar o aluno criado com um `id`

### Passo 3: Listar Todos os Alunos

1. Clique em `GET /api/alunos`
2. Clique em **"Try it out"**
3. Clique em **"Execute"**
4. Veja a lista de alunos retornada

### Passo 4: Buscar Aluno por ID

1. Clique em `GET /api/alunos/{id}`
2. Clique em **"Try it out"**
3. No campo `id`, digite `1` (ou o ID do aluno criado)
4. Clique em **"Execute"**
5. Veja os dados do aluno retornado

### Passo 5: Filtrar Alunos

1. Clique em `GET /api/alunos/filtro`
2. Clique em **"Try it out"**
3. Teste os filtros:
   - **Por Curso**: Preencha `cursoId` com `1`
   - **Por Semestre**: Preencha `semestre` com `3`
   - **Por Média Mínima**: Preencha `mediaMinima` com `8.0`
4. Clique em **"Execute"**
5. Veja os alunos filtrados

### Passo 6: Atualizar um Aluno

1. Clique em `PUT /api/alunos/{id}`
2. Clique em **"Try it out"**
3. No campo `id`, digite `1`
4. No campo **Request body**, cole o JSON atualizado:
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
5. Clique em **"Execute"**
6. Veja o aluno atualizado na resposta

### Passo 7: Deletar um Aluno

1. Clique em `DELETE /api/alunos/{id}`
2. Clique em **"Try it out"**
3. No campo `id`, digite `1`
4. Clique em **"Execute"**
5. Verifique que retorna `204 No Content`

## 🎯 Exemplos de JSON para Testar

### Criar Mais Cursos

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

### Criar Mais Alunos

```json
{
  "nome": "Maria Santos",
  "matricula": "2024002",
  "email": "maria@email.com",
  "cursoId": 1,
  "semestre": 2,
  "mediaGeral": 9.0
}
```

```json
{
  "nome": "Pedro Oliveira",
  "matricula": "2024003",
  "email": "pedro@email.com",
  "cursoId": 2,
  "semestre": 4,
  "mediaGeral": 7.8
}
```

## ⚠️ Validações que Você Pode Testar

### Teste de Validação - Nome Vazio
```json
{
  "nome": "",
  "matricula": "2024001",
  "email": "joao@email.com",
  "cursoId": 1,
  "semestre": 3,
  "mediaGeral": 8.5
}
```
**Resultado esperado**: Erro 400 - Nome é obrigatório

### Teste de Validação - Email Inválido
```json
{
  "nome": "João Silva",
  "matricula": "2024001",
  "email": "email-invalido",
  "cursoId": 1,
  "semestre": 3,
  "mediaGeral": 8.5
}
```
**Resultado esperado**: Erro 400 - Email deve ser válido

### Teste de Validação - Matrícula Duplicada
Tente criar dois alunos com a mesma matrícula:
```json
{
  "nome": "Aluno 1",
  "matricula": "2024001",
  "email": "aluno1@email.com",
  "cursoId": 1,
  "semestre": 3,
  "mediaGeral": 8.5
}
```
Depois tente criar outro com a mesma matrícula:
```json
{
  "nome": "Aluno 2",
  "matricula": "2024001",
  "email": "aluno2@email.com",
  "cursoId": 1,
  "semestre": 3,
  "mediaGeral": 8.5
}
```
**Resultado esperado**: Erro 400 - Matrícula já existe

### Teste de Recurso Não Encontrado
Tente buscar um aluno com ID inexistente:
- `GET /api/alunos/999`
**Resultado esperado**: Erro 404 - Aluno não encontrado

## 🔍 Verificar Dados no Banco H2

Se quiser ver os dados diretamente no banco:

1. Acesse: http://localhost:8080/h2-console
2. Preencha:
   - **JDBC URL**: `jdbc:h2:mem:gestaoalunosdb`
   - **Username**: `sa`
   - **Password**: (deixe vazio)
3. Clique em **Connect**
4. Execute queries como:
   ```sql
   SELECT * FROM alunos;
   SELECT * FROM cursos;
   ```

## 🛑 Parar a Aplicação

Para parar a aplicação, volte ao terminal onde ela está rodando e pressione:
```
Ctrl + C
```

## 📚 Links Úteis

- **Swagger UI**: http://localhost:8080/swagger-ui.html
- **API Docs (JSON)**: http://localhost:8080/api-docs
- **H2 Console**: http://localhost:8080/h2-console
- **Health Check**: http://localhost:8080/actuator/health (se configurado)

## 💡 Dicas

1. **Use o botão "Try it out"** para testar cada endpoint
2. **Verifique as respostas** para entender os formatos
3. **Teste validações** para ver como a API trata erros
4. **Use filtros** para buscar dados específicos
5. **Crie cursos primeiro** antes de criar alunos
6. **Anote os IDs** retornados para usar em outras operações

## 🎉 Pronto!

Agora você pode testar todos os endpoints da API através do Swagger UI. Divirta-se testando! 🚀

