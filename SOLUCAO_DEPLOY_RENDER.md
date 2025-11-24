# 🚀 Solução Definitiva - Deploy no Render

## ⚠️ Problema: "failed to read dockerfile"

Se você está recebendo este erro, siga estas soluções **na ordem**:

## ✅ Solução 1: Usar Procfile (MAIS FÁCIL - RECOMENDADO)

O Render suporta deploy de aplicações Java sem Docker! Use o **Procfile** que já existe.

### Passos:

1. **No Render Dashboard:**
   - Crie um novo **Web Service**
   - Conecte seu repositório
   - **IMPORTANTE**: Escolha **Runtime: `Java`** (NÃO Docker!)

2. **Configurações:**
   - **Build Command**: `mvn clean package -DskipTests`
   - **Start Command**: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`
   - **Root Directory**: (deixe vazio)

3. **Variáveis de Ambiente:**
   ```
   SPRING_PROFILES_ACTIVE=prod
   DATABASE_URL=jdbc:postgresql://[HOST]:[PORT]/gestaoalunos
   DATABASE_USERNAME=[usuario]
   DATABASE_PASSWORD=[senha]
   PORT=8080
   ```

4. **Deploy!**

Esta é a forma mais simples e geralmente funciona melhor no Render para aplicações Spring Boot.

---

## ✅ Solução 2: Usar Docker (se preferir)

Se você realmente quer usar Docker, siga estes passos **exatamente**:

### Passo 1: Verificar se Dockerfile está commitado

```bash
# Verificar se está no Git
git ls-files | grep Dockerfile

# Se não estiver, adicionar:
git add Dockerfile
git commit -m "Adicionar Dockerfile"
git push
```

### Passo 2: Configurar no Render

1. **Criar Web Service:**
   - Runtime: **Docker**
   - **Root Directory**: (DEIXE VAZIO - muito importante!)
   - Build Command: (deixe vazio)
   - Start Command: (deixe vazio)

2. **Verificar estrutura do repositório:**
   O Dockerfile DEVE estar na raiz:
   ```
   seu-repositorio/
   ├── Dockerfile    ← AQUI
   ├── pom.xml
   ├── src/
   └── ...
   ```

3. **Se o Dockerfile está em subpasta:**
   - Configure **Root Directory** com o caminho (ex: `backend/`)
   - OU mova o Dockerfile para a raiz

### Passo 3: Verificar .dockerignore

Certifique-se de que o `.dockerignore` **NÃO exclui** o Dockerfile:
```bash
# Verificar conteúdo
cat .dockerignore

# Se tiver "Dockerfile" na lista, remover essa linha
```

---

## ✅ Solução 3: Deploy Manual com Build Local

Se nada funcionar, faça o build localmente e faça deploy do JAR:

1. **Build local:**
   ```bash
   mvn clean package -DskipTests
   ```

2. **No Render:**
   - Runtime: **Java**
   - Build Command: (deixe vazio)
   - Start Command: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`
   - Faça upload do JAR compilado (não recomendado, mas funciona)

---

## 🔍 Checklist de Verificação

Antes de tentar deploy, verifique:

- [ ] Dockerfile existe na raiz do projeto
- [ ] Dockerfile está commitado no Git (`git ls-files | grep Dockerfile`)
- [ ] Dockerfile foi enviado para o repositório remoto (`git push`)
- [ ] `.dockerignore` NÃO exclui o Dockerfile
- [ ] Root Directory no Render está **VAZIO** (se Dockerfile está na raiz)
- [ ] Runtime está configurado corretamente (Docker OU Java)

---

## 🎯 RECOMENDAÇÃO FINAL

**Use a Solução 1 (Procfile com Runtime Java)** - É mais simples, mais rápido e geralmente funciona melhor no Render para Spring Boot!

O Render tem suporte nativo para Maven/Java e não precisa de Docker para aplicações Spring Boot simples.

---

## 📞 Se Ainda Não Funcionar

1. **Verifique os logs do build no Render:**
   - Dashboard > Seu Serviço > Logs
   - Procure por mensagens de erro específicas

2. **Teste o Dockerfile localmente:**
   ```bash
   docker build -t gestao-alunos .
   docker run -p 8080:8080 gestao-alunos
   ```
   Se funcionar localmente, o problema está na configuração do Render.

3. **Entre em contato com suporte do Render** ou use a Solução 1 (mais confiável).

---

**Última atualização**: Criado guia com múltiplas soluções, priorizando o uso do Procfile.

