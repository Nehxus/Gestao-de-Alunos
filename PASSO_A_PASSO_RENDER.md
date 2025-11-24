# 📋 Passo a Passo - Deploy no Render (SOLUÇÃO DEFINITIVA)

## 🎯 Método Recomendado: Usar Java Runtime (SEM Docker)

Este método é **mais simples** e **mais confiável** para aplicações Spring Boot no Render.

---

## ✅ Passo 1: Preparar o Repositório

Certifique-se de que todos os arquivos estão commitados:

```bash
git add .
git commit -m "Preparar para deploy no Render"
git push
```

---

## ✅ Passo 2: Criar Banco de Dados PostgreSQL

1. Acesse https://dashboard.render.com
2. Clique em **"New +"** > **"PostgreSQL"**
3. Configure:
   - **Name**: `gestao-alunos-db`
   - **Database**: `gestaoalunos`
   - **User**: (será gerado)
   - **Region**: Escolha a mais próxima
   - **Plan**: Free (ou pago)
4. Clique em **"Create Database"**
5. **ANOTE as credenciais**:
   - Internal Database URL
   - Host
   - Port
   - Database
   - User
   - Password

---

## ✅ Passo 3: Criar Web Service (IMPORTANTE!)

### 3.1. Conectar Repositório

1. Clique em **"New +"** > **"Web Service"**
2. Conecte seu repositório Git (GitHub/GitLab/Bitbucket)
3. Selecione o repositório `gestao-alunos`

### 3.2. Configurações Básicas

- **Name**: `gestao-alunos`
- **Region**: Mesma região do banco de dados
- **Branch**: `main` (ou sua branch principal)
- **Root Directory**: **(DEIXE VAZIO)**

### 3.3. Configurações de Build (MUITO IMPORTANTE!)

⚠️ **NÃO use Docker!** Use Java Runtime:

- **Runtime**: Selecione **`Java`** (NÃO Docker!)
- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`

### 3.4. Variáveis de Ambiente

Adicione estas variáveis (clique em **"Add Environment Variable"**):

```
SPRING_PROFILES_ACTIVE = prod
```

```
DATABASE_URL = jdbc:postgresql://[HOST]:[PORT]/gestaoalunos
```
*(Substitua [HOST] e [PORT] pelos valores do banco criado no Passo 2)*

**Exemplo:**
```
DATABASE_URL = jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

```
DATABASE_USERNAME = [seu_usuario]
```
*(Use o User do banco criado no Passo 2)*

```
DATABASE_PASSWORD = [sua_senha]
```
*(Use o Password do banco criado no Passo 2)*

### 3.5. Criar Serviço

1. Clique em **"Create Web Service"**
2. Aguarde o build (5-10 minutos na primeira vez)

---

## ✅ Passo 4: Verificar Deploy

Após o build completar, você verá uma URL como:
```
https://gestao-alunos.onrender.com
```

### Testar:

1. **API Base**: `https://gestao-alunos.onrender.com/api/alunos`
2. **Swagger UI**: `https://gestao-alunos.onrender.com/swagger-ui.html`
3. **API Docs**: `https://gestao-alunos.onrender.com/api-docs`

---

## 🔧 Se Der Erro

### Erro: "Build failed"

**Solução:**
1. Verifique os logs do build no Render
2. Certifique-se de que o `pom.xml` está correto
3. Verifique se todas as dependências estão disponíveis

### Erro: "Application failed to start"

**Solução:**
1. Verifique as variáveis de ambiente (especialmente DATABASE_URL)
2. Verifique se o banco de dados está rodando
3. Verifique os logs da aplicação

### Erro: "Port already in use"

**Solução:**
- O Render define automaticamente a variável `PORT`
- Certifique-se de que `application-prod.properties` usa `${PORT:8080}`

---

## 📝 Checklist Final

Antes de fazer deploy, verifique:

- [ ] Código commitado e enviado para o repositório
- [ ] Banco de dados PostgreSQL criado no Render
- [ ] Credenciais do banco anotadas
- [ ] Web Service criado com **Runtime: Java** (NÃO Docker)
- [ ] Build Command configurado: `mvn clean package -DskipTests`
- [ ] Start Command configurado: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`
- [ ] Variáveis de ambiente configuradas corretamente
- [ ] Root Directory está vazio
- [ ] Build completou com sucesso
- [ ] Aplicação está acessível via URL

---

## 🎉 Pronto!

Sua aplicação deve estar rodando! Se ainda tiver problemas, verifique os logs no dashboard do Render.

---

**Dica**: Se você preferir usar Docker, siga o guia `SOLUCAO_DEPLOY_RENDER.md`, mas o método Java é mais simples e recomendado.

