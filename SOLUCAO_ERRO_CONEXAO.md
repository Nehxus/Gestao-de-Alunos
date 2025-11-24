# 🔧 Solução: Erro de Conexão com Banco de Dados

## ⚠️ Erro: "The connection attempt failed"

Este erro ocorre porque as **variáveis de ambiente do banco de dados não estão configuradas** no Render.

---

## ✅ Solução: Configurar Variáveis de Ambiente no Render

### Passo 1: Obter Credenciais do Banco

No Render Dashboard:

1. Vá no seu **PostgreSQL Database**
2. Na seção **"Connections"**, você verá:
   - **Internal Database URL** (formato: `postgres://user:password@host:port/database`)
   - **Host**
   - **Port** (geralmente 5432)
   - **Database**
   - **User**
   - **Password**

### Passo 2: Configurar no Web Service

No seu **Web Service**, vá em **Environment** e adicione estas variáveis:

#### Opção A: Usar Internal Database URL do Render (MAIS FÁCIL)

O Render fornece uma URL no formato:
```
postgres://user:password@host:port/database
```

Configure assim:

```
DATABASE_URL = postgres://user:password@host:port/database
```

**Exemplo:**
```
DATABASE_URL = postgres://gestaoalunos_user:abc123@dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

E também adicione:

```
DATABASE_USERNAME = gestaoalunos_user
DATABASE_PASSWORD = abc123
```

#### Opção B: Usar Formato JDBC Direto

Se preferir, converta para formato JDBC:

```
DATABASE_URL = jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
DATABASE_USERNAME = gestaoalunos_user
DATABASE_PASSWORD = abc123
```

### Passo 3: Variável de Perfil

Certifique-se de ter:

```
SPRING_PROFILES_ACTIVE = prod
```

---

## 🔍 Verificar se Está Funcionando

Após configurar as variáveis:

1. **Faça um novo deploy** no Render
2. **Verifique os logs** - você deve ver:
   ```
   === Configuração do Banco de Dados ===
   DATABASE_URL (original): postgres://...
   JDBC URL (convertida): jdbc:postgresql://...
   DATABASE_USERNAME: ***
   DATABASE_PASSWORD: ***
   ```

3. Se ainda der erro, verifique:
   - As variáveis estão configuradas corretamente?
   - O banco de dados está na mesma região do Web Service?
   - Está usando **Internal Database URL** (não External)?

---

## 📝 Exemplo Completo de Configuração

No Render Dashboard → Web Service → Environment:

```
SPRING_PROFILES_ACTIVE = prod
DATABASE_URL = postgres://gestaoalunos_user:senha123@dpg-abc123-a.oregon-postgres.render.com:5432/gestaoalunos
DATABASE_USERNAME = gestaoalunos_user
DATABASE_PASSWORD = senha123
```

---

## 🚨 Erros Comuns

### "DATABASE_URL não configurada"

**Solução**: Adicione a variável `DATABASE_URL` no Render

### "DATABASE_USERNAME não configurada"

**Solução**: Adicione a variável `DATABASE_USERNAME` no Render

### "The connection attempt failed"

**Causas possíveis**:
1. Variáveis não configuradas
2. Host/port incorretos
3. Banco de dados em região diferente
4. Usando External URL ao invés de Internal

**Solução**:
- Verifique todas as variáveis
- Use **Internal Database URL** (não External)
- Certifique-se de que Web Service e Database estão na **mesma região**

---

## ✅ Checklist

Antes de fazer deploy, verifique:

- [ ] Banco de dados PostgreSQL criado no Render
- [ ] Credenciais anotadas (Host, Port, Database, User, Password)
- [ ] `SPRING_PROFILES_ACTIVE=prod` configurado
- [ ] `DATABASE_URL` configurada (formato postgres:// ou jdbc:postgresql://)
- [ ] `DATABASE_USERNAME` configurado
- [ ] `DATABASE_PASSWORD` configurado
- [ ] Web Service e Database na **mesma região**
- [ ] Usando **Internal Database URL** (não External)

---

## 🎯 O Que Foi Corrigido no Código

1. ✅ Criada classe `DatabaseConfig` que lê variáveis de ambiente diretamente
2. ✅ Conversão automática de `postgres://` para `jdbc:postgresql://`
3. ✅ Logs detalhados para debug
4. ✅ Validação de variáveis obrigatórias
5. ✅ Removido warning do `hibernate.dialect`

---

**Após configurar as variáveis, faça um novo deploy!**

A aplicação agora mostrará logs detalhados sobre a configuração do banco, facilitando o debug.

