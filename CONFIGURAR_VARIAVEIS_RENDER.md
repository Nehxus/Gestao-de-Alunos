# 🔧 Configurar Variáveis de Ambiente no Render

## ⚠️ Erro: "Connection to localhost:5432 refused"

Este erro ocorre quando as variáveis de ambiente do banco de dados **não estão configuradas corretamente** no Render.

---

## ✅ Solução: Configurar Variáveis de Ambiente

### Passo 1: Obter Credenciais do Banco de Dados

No Render Dashboard:

1. Vá no seu **PostgreSQL Database**
2. Na seção **"Connections"**, você verá:
   - **Internal Database URL** (formato: `postgres://user:password@host:port/database`)
   - **Host**
   - **Port**
   - **Database**
   - **User**
   - **Password**

### Passo 2: Configurar no Web Service

No seu **Web Service**, vá em **Environment** e adicione estas variáveis:

#### Opção A: Usar DATABASE_URL (Recomendado)

```
DATABASE_URL = jdbc:postgresql://[HOST]:[PORT]/[DATABASE]
```

**Exemplo:**
```
DATABASE_URL = jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

E também adicione:

```
DATABASE_USERNAME = [seu_usuario]
DATABASE_PASSWORD = [sua_senha]
```

#### Opção B: Usar Variáveis Individuais

Se preferir, use variáveis individuais:

```
DATABASE_HOST = dpg-xxxxx-a.oregon-postgres.render.com
DATABASE_PORT = 5432
DATABASE_NAME = gestaoalunos
DATABASE_USERNAME = gestaoalunos_user
DATABASE_PASSWORD = sua_senha_aqui
```

### Passo 3: Variável de Perfil

Certifique-se de ter:

```
SPRING_PROFILES_ACTIVE = prod
```

---

## 🔍 Verificar Configuração

### Como Verificar se Está Correto:

1. **No Render Dashboard:**
   - Vá em **Environment** do seu Web Service
   - Verifique se todas as variáveis estão configuradas
   - **IMPORTANTE**: Use o **Internal Database URL** (não o External)

2. **Formato da DATABASE_URL:**
   - ✅ Correto: `jdbc:postgresql://host:port/database`
   - ❌ Errado: `postgres://user:pass@host:port/db` (formato do Render)
   - ❌ Errado: `localhost:5432` (não funciona no Render)

### Converter Internal Database URL do Render:

O Render fornece no formato:
```
postgres://user:password@host:port/database
```

Converta para:
```
jdbc:postgresql://host:port/database
```

E configure separadamente:
```
DATABASE_USERNAME = user
DATABASE_PASSWORD = password
```

---

## 📝 Exemplo Completo

### No Render Dashboard:

**Environment Variables:**

```
SPRING_PROFILES_ACTIVE = prod
DATABASE_URL = jdbc:postgresql://dpg-abc123-a.oregon-postgres.render.com:5432/gestaoalunos
DATABASE_USERNAME = gestaoalunos_user
DATABASE_PASSWORD = sua_senha_secreta_aqui
```

---

## 🚨 Erros Comuns

### Erro: "Connection to localhost:5432 refused"

**Causa**: Variáveis de ambiente não configuradas ou DATABASE_URL incorreta.

**Solução**:
1. Verifique se `DATABASE_URL` está configurada
2. Certifique-se de usar o **host do Render** (não localhost)
3. Use o formato `jdbc:postgresql://host:port/database`

### Erro: "Authentication failed"

**Causa**: Usuário ou senha incorretos.

**Solução**:
1. Verifique `DATABASE_USERNAME` e `DATABASE_PASSWORD`
2. Use as credenciais exatas do banco de dados no Render

### Erro: "Database does not exist"

**Causa**: Nome do banco incorreto.

**Solução**:
1. Verifique `DATABASE_NAME` na URL
2. Confirme o nome do banco no dashboard do Render

---

## ✅ Checklist

Antes de fazer deploy, verifique:

- [ ] Banco de dados PostgreSQL criado no Render
- [ ] Credenciais anotadas (Host, Port, Database, User, Password)
- [ ] `SPRING_PROFILES_ACTIVE=prod` configurado
- [ ] `DATABASE_URL` configurada no formato correto: `jdbc:postgresql://host:port/database`
- [ ] `DATABASE_USERNAME` configurado
- [ ] `DATABASE_PASSWORD` configurado
- [ ] Web Service e Database na **mesma região**
- [ ] Usando **Internal Database URL** (não External)

---

## 🔗 Links Úteis

- [Render Database Documentation](https://render.com/docs/databases)
- [Spring Boot Externalized Configuration](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.external-config)

---

**Após configurar as variáveis, faça um novo deploy!**

