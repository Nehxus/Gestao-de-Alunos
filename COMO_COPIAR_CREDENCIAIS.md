# 🔑 Como Copiar as Credenciais do Banco no Render

## Passo 1: Acessar o Banco de Dados

1. Entre no [Render Dashboard](https://dashboard.render.com)
2. Clique no seu banco PostgreSQL (ex: `gestao-alunos-db`)

## Passo 2: Copiar a Internal Database URL

Na página do banco, você verá uma seção chamada **"Connections"** ou **"Info"**.

Procure por **"Internal Database URL"** - ela vai estar assim:

```
postgres://gestaoalunos_user:ABC123xyz@dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

**COPIE ESSA URL COMPLETA!** (Ctrl+C ou Cmd+C)

## Passo 3: Configurar no Web Service

1. No Render, vá para seu Web Service (`gestao-de-alunos`)
2. Clique em **"Environment"** (ou **"Settings"** > **"Environment"**)
3. Clique em **"Add Environment Variable"**
4. Adicione:

**Variável 1:**
- Key: `SPRING_PROFILES_ACTIVE`
- Value: `prod`

**Variável 2:**
- Key: `DATABASE_URL`
- Value: **[COLE AQUI A URL QUE VOCÊ COPIOU NO PASSO 2]**

Exemplo do que colar:
```
postgres://gestaoalunos_user:ABC123xyz@dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

## Passo 4: Salvar

1. Clique em **"Save Changes"**
2. O Render vai fazer deploy automaticamente
3. Aguarde alguns minutos
4. Verifique os logs

## ✅ Pronto!

A aplicação vai extrair automaticamente o usuário e senha da URL!

---

**Dica:** Se não encontrar "Internal Database URL", procure por:
- "Connection String"
- "Database URL"
- Ou use as informações individuais (Host, Port, Database, User, Password) para montar a URL

