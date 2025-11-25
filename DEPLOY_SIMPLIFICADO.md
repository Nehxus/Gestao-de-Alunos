# 🚀 Deploy Simplificado no Render

## ⚡ Método Mais Simples (RECOMENDADO)

### Passo 1: Preparar o Repositório

```bash
# Certifique-se de que tudo está commitado
git add .
git commit -m "Preparar para deploy"
git push origin main
```

### Passo 2: Criar Banco de Dados PostgreSQL no Render

1. Acesse [Render Dashboard](https://dashboard.render.com)
2. Clique em **"New +"** > **"PostgreSQL"**
3. Configure:
   - **Name**: `gestao-alunos-db`
   - **Database**: `gestaoalunos`
   - **User**: (gerado automaticamente)
   - **Region**: Escolha a região mais próxima
   - **Plan**: Free
4. Clique em **"Create Database"**
5. **COPIE as credenciais** (você vai precisar delas!)

### Passo 3: Criar Web Service (SEM DOCKER)

1. No Render, clique em **"New +"** > **"Web Service"**
2. Conecte seu repositório Git (GitHub/GitLab/Bitbucket)
3. Configure:

#### Configurações Básicas:
- **Name**: `gestao-alunos`
- **Region**: Mesma região do banco de dados
- **Branch**: `main` (ou sua branch principal)
- **Root Directory**: **(DEIXE VAZIO)**
- **Runtime**: **`Java`** (NÃO escolha Docker!)
- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`

#### Variáveis de Ambiente:
Clique em **"Add Environment Variable"** e adicione:

```
SPRING_PROFILES_ACTIVE = prod
DATABASE_URL = jdbc:postgresql://[HOST]:[PORT]/gestaoalunos
DATABASE_USERNAME = [seu_usuario]
DATABASE_PASSWORD = [sua_senha]
```

**IMPORTANTE**: 
- Substitua `[HOST]`, `[PORT]`, `[seu_usuario]` e `[sua_senha]` pelos valores do banco criado no Passo 2
- O `DATABASE_URL` deve estar no formato: `jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos`

### Passo 4: Deploy

1. Clique em **"Create Web Service"**
2. Aguarde o build (5-10 minutos na primeira vez)
3. Verifique os logs para garantir que não há erros

### Passo 5: Verificar

Após o deploy, você terá uma URL como:
```
https://gestao-alunos.onrender.com
```

Teste:
- **API**: `https://gestao-alunos.onrender.com/api/alunos`
- **Swagger**: `https://gestao-alunos.onrender.com/swagger-ui.html`

---

## 🔧 Se Precisar Usar Docker

Se você realmente quiser usar Docker:

1. **Certifique-se de que o Dockerfile está commitado:**
```bash
git add Dockerfile
git commit -m "Adicionar Dockerfile"
git push
```

2. **No Render:**
   - **Runtime**: `Docker`
   - **Root Directory**: **(DEIXE VAZIO)**
   - **Build Command**: (deixe vazio)
   - **Start Command**: (deixe vazio)

3. Configure as mesmas variáveis de ambiente do método sem Docker

---

## ❌ Problemas Comuns

### Erro: "Build failed"
- Verifique os logs do build no Render
- Certifique-se de que o `pom.xml` está correto
- Verifique se todas as dependências estão disponíveis

### Erro: "Connection refused" ou erro de banco
- Verifique se as variáveis de ambiente estão corretas
- Certifique-se de que o banco está na mesma região
- Verifique se o banco está rodando (Status: Available)

### Aplicação não inicia
- Verifique os logs do serviço
- Certifique-se de que a variável `PORT` está sendo usada
- Verifique se `SPRING_PROFILES_ACTIVE=prod` está configurado

### "failed to read dockerfile"
- Use o método **SEM Docker** (Runtime: Java)
- Ou certifique-se de que o Dockerfile está commitado e na raiz

---

## ✅ Checklist Final

Antes de fazer deploy, verifique:

- [ ] Código commitado e enviado para o repositório
- [ ] Banco de dados PostgreSQL criado no Render
- [ ] Variáveis de ambiente configuradas corretamente
- [ ] Runtime configurado como **Java** (não Docker)
- [ ] Build Command: `mvn clean package -DskipTests`
- [ ] Start Command: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`

---

## 🆘 Ainda com Problemas?

1. **Verifique os logs** no dashboard do Render
2. **Teste localmente** primeiro:
   ```bash
   mvn clean package -DskipTests
   java -jar target/gestao-alunos-1.0.0.jar
   ```
3. **Verifique as variáveis de ambiente** no Render
4. **Certifique-se de que o banco está acessível**

---

**Dica**: O método **SEM Docker** (Runtime: Java) é mais simples e geralmente funciona melhor no Render para aplicações Spring Boot! 🎯

