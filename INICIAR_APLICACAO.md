# Como Iniciar a Aplicação para Testar no Swagger

## 🚀 Iniciar a Aplicação

### Método 1: Usando Maven (Recomendado)
```bash
cd gestao-alunos
mvn spring-boot:run
```

### Método 2: Usando o JAR
```bash
cd gestao-alunos
java -jar target/gestao-alunos-1.0.0.jar
```

### Método 3: Usando o Script
```bash
cd gestao-alunos
./run.sh
```

## ⏱️ Aguarde a Inicialização

A aplicação demora alguns segundos para iniciar. Aguarde até ver a mensagem:
```
Started GestaoAlunosApplication in X.XXX seconds
```

## 🌐 Acessar o Swagger

Após a aplicação iniciar, abra seu navegador e acesse:

**Swagger UI**: http://localhost:8080/swagger-ui.html

**API Docs (JSON)**: http://localhost:8080/api-docs

## ✅ Verificar se a Aplicação Está Rodando

Você pode verificar se a aplicação está rodando fazendo uma requisição:

```bash
curl http://localhost:8080/api-docs
```

Se retornar JSON, a aplicação está rodando!

## 📝 Fluxo de Teste Rápido

1. **Acesse o Swagger**: http://localhost:8080/swagger-ui.html

2. **Crie um Curso**:
   - Clique em `POST /api/cursos`
   - Clique em "Try it out"
   - Preencha o JSON:
   ```json
   {
     "nome": "Ciência da Computação",
     "descricao": "Curso de CC"
   }
   ```
   - Clique em "Execute"
   - Anote o `id` retornado

3. **Crie um Aluno**:
   - Clique em `POST /api/alunos`
   - Clique em "Try it out"
   - Preencha o JSON (use o `cursoId` do curso criado):
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
   - Clique em "Execute"

4. **Teste outros Endpoints**:
   - `GET /api/alunos` - Listar alunos
   - `GET /api/alunos/{id}` - Buscar aluno por ID
   - `GET /api/alunos/filtro?cursoId=1` - Filtrar alunos
   - `PUT /api/alunos/{id}` - Atualizar aluno
   - `DELETE /api/alunos/{id}` - Deletar aluno

## 🛑 Parar a Aplicação

Para parar a aplicação, pressione `Ctrl + C` no terminal onde ela está rodando.

## 📚 Documentação Completa

Para mais detalhes, consulte:
- [SWAGGER_GUIDE.md](SWAGGER_GUIDE.md) - Guia completo do Swagger
- [README.md](README.md) - Documentação geral do projeto

## 🔧 Troubleshooting

### Porta 8080 já está em uso
Se a porta 8080 já estiver em uso, você pode alterar a porta no arquivo `application.properties`:
```properties
server.port=8081
```

### Aplicação não inicia
Verifique se:
- Java 17+ está instalado
- As dependências foram baixadas (`mvn clean install`)
- Não há erros de compilação

### Swagger não abre
Verifique se:
- A aplicação está rodando
- A porta está correta (8080)
- Não há firewall bloqueando

