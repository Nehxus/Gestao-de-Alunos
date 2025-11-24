# 🚨 Correção Imediata - Erro Dockerfile

## ⚡ Ação Imediata Necessária

O erro "failed to read dockerfile" geralmente significa que o **Dockerfile não está commitado no Git** ou o **Render não está encontrando ele**.

---

## ✅ Passo 1: Verificar e Commitar Dockerfile

Execute estes comandos:

```bash
# 1. Verificar se Dockerfile existe
ls -la Dockerfile

# 2. Verificar se está no Git
git status Dockerfile

# 3. Se não estiver, adicionar e commitar
git add Dockerfile .dockerignore
git commit -m "Adicionar Dockerfile para deploy no Render"
git push origin main
```

**IMPORTANTE**: O Render só vê arquivos que estão no repositório Git remoto!

---

## ✅ Passo 2: Configurar no Render

### Opção A: Usar Docker

1. **Criar/Editar Web Service**
2. **Runtime**: Escolha `Docker`
3. **Root Directory**: **(DEIXE VAZIO)**
4. **Build Command**: (deixe vazio)
5. **Start Command**: (deixe vazio)

### Opção B: NÃO Usar Docker (MAIS FÁCIL)

1. **Criar/Editar Web Service**
2. **NÃO escolha Runtime Docker**
3. **Build Command**: `mvn clean package -DskipTests`
4. **Start Command**: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`

O Render detectará automaticamente através do `pom.xml` e usará o `Procfile`.

---

## ✅ Passo 3: Variáveis de Ambiente

Configure estas variáveis no Render:

```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://[HOST]:[PORT]/gestaoalunos
DATABASE_USERNAME=[usuario]
DATABASE_PASSWORD=[senha]
```

---

## 🎯 Resumo Rápido

**Problema**: Dockerfile não encontrado

**Solução 1** (Recomendado):
- Não use Docker
- Use Build Command: `mvn clean package -DskipTests`
- Use Start Command: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`

**Solução 2** (Se precisar de Docker):
- Certifique-se de que Dockerfile está commitado: `git add Dockerfile && git commit -m "Add Dockerfile" && git push`
- Configure Root Directory como vazio
- Runtime: Docker

---

**Execute o Passo 1 primeiro!** Isso resolve 90% dos casos.


