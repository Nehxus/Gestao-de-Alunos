# ⚡ Guia Rápido - Deploy no Render

## 🎯 Solução Rápida (3 Passos)

### 1️⃣ Criar Web Service

1. Render Dashboard → **"New +"** → **"Web Service"**
2. Conecte seu repositório
3. **Name**: `gestao-alunos`
4. **Root Directory**: **(VAZIO)**

### 2️⃣ Configurar Build

**Se aparecer campos de Build/Start Command:**

- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`

**Se NÃO aparecer esses campos:**

- Deixe tudo vazio
- O Render detectará automaticamente (usa `pom.xml` + `Procfile`)

### 3️⃣ Variáveis de Ambiente

Adicione:

```
SPRING_PROFILES_ACTIVE = prod
DATABASE_URL = jdbc:postgresql://[HOST]:[PORT]/gestaoalunos
DATABASE_USERNAME = [usuario]
DATABASE_PASSWORD = [senha]
```

### 4️⃣ Deploy!

Clique em **"Create Web Service"** e aguarde.

---

## ✅ O Que o Render Faz Automaticamente

Quando você tem `pom.xml` e `Procfile`:

1. ✅ Detecta que é aplicação Java/Maven
2. ✅ Instala Java e Maven
3. ✅ Executa `mvn clean package`
4. ✅ Usa o `Procfile` para iniciar

**Você não precisa escolher "Runtime: Java"!**

---

## 🔧 Se Der Erro

### "Build failed"
→ Verifique os logs
→ Certifique-se de que `pom.xml` está correto

### "JAR not found"
→ Verifique se o nome do JAR no Procfile corresponde ao gerado pelo Maven
→ Nome esperado: `gestao-alunos-1.0.0.jar`

### "Port already in use"
→ O Render define `$PORT` automaticamente
→ Seu `application-prod.properties` já usa `${PORT:8080}` ✅

---

## 📋 Arquivos Importantes

- ✅ `pom.xml` → Detecta Maven
- ✅ `Procfile` → Comando de inicialização
- ✅ `src/` → Código fonte

**Todos esses arquivos já existem no seu projeto!**

---

**Pronto! Siga esses 3 passos e faça deploy! 🚀**


