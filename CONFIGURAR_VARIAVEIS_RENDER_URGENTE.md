# 🚨 CONFIGURAR VARIÁVEIS DE AMBIENTE NO RENDER (URGENTE)

## ⚠️ Problema Atual

A aplicação está falhando porque as variáveis de ambiente do banco de dados não estão configuradas.

## ✅ Solução Rápida

### Passo 1: Acessar as Configurações do Serviço

1. No Render Dashboard, vá para seu serviço `gestao-alunos`
2. Clique em **"Environment"** (ou **"Settings"** > **"Environment"**)

### Passo 2: Adicionar Variáveis de Ambiente

Clique em **"Add Environment Variable"** e adicione **TODAS** estas variáveis:

#### Variável 1:
- **Key**: `SPRING_PROFILES_ACTIVE`
- **Value**: `prod`

#### Variável 2:
- **Key**: `DATABASE_URL`
- **Value**: `jdbc:postgresql://[HOST]:[PORT]/[DATABASE]`
  - **Substitua** `[HOST]`, `[PORT]` e `[DATABASE]` pelos valores do seu banco PostgreSQL no Render
  - **Exemplo**: `jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos`

#### Variável 3:
- **Key**: `DATABASE_USERNAME`
- **Value**: `[seu_usuario]`
  - Substitua pelo usuário do seu banco PostgreSQL

#### Variável 4:
- **Key**: `DATABASE_PASSWORD`
- **Value**: `[sua_senha]`
  - Substitua pela senha do seu banco PostgreSQL

### Passo 3: Onde Encontrar as Credenciais do Banco

1. No Render Dashboard, vá para seu banco de dados PostgreSQL
2. Na seção **"Connections"** ou **"Info"**, você verá:
   - **Internal Database URL** ou **Host**
   - **Port** (geralmente 5432)
   - **Database Name**
   - **User**
   - **Password**

### Passo 4: Formato da DATABASE_URL

Se o Render fornecer a URL no formato `postgres://`, você pode usar diretamente:

**Opção A - Formato JDBC (Recomendado):**
```
jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

**Opção B - Formato postgres:// (também funciona):**
```
postgres://usuario:senha@dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

### Passo 5: Salvar e Fazer Redeploy

1. Clique em **"Save Changes"**
2. O Render irá automaticamente fazer um novo deploy
3. Aguarde o deploy completar
4. Verifique os logs para confirmar que a aplicação iniciou corretamente

---

## 🔍 Verificar se Funcionou

Após configurar as variáveis e fazer o redeploy, verifique os logs. Você deve ver:

✅ **Sucesso:**
```
INFO  --- [gestao-alunos] [main] b.c.gestao.alunos.config.DatabaseConfig : DATABASE_URL (original): jdbc:postgresql://...
INFO  --- [gestao-alunos] [main] b.c.gestao.alunos.config.DatabaseConfig : DATABASE_USERNAME: ***
INFO  --- [gestao-alunos] [main] o.s.b.w.embedded.tomcat.TomcatWebServer : Tomcat started on port(s): 10000 (http)
```

❌ **Erro (se ainda aparecer):**
```
ERROR --- DATABASE_USERNAME não configurada
```

---

## 📝 Checklist

Antes de fazer o redeploy, verifique:

- [ ] `SPRING_PROFILES_ACTIVE=prod` está configurado
- [ ] `DATABASE_URL` está configurado com o formato correto
- [ ] `DATABASE_USERNAME` está configurado
- [ ] `DATABASE_PASSWORD` está configurado
- [ ] Todas as variáveis foram salvas
- [ ] O banco de dados PostgreSQL está rodando (Status: Available)

---

## 🆘 Se Ainda Não Funcionar

1. **Verifique os logs completos** no Render
2. **Confirme que o banco está na mesma região** do serviço
3. **Teste a conexão** manualmente (se possível)
4. **Verifique se não há espaços extras** nas variáveis de ambiente

---

## 💡 Dica Importante

Se você criou o banco de dados PostgreSQL no Render, as credenciais estão na página do banco. Se você está usando um banco externo, certifique-se de que:
- O banco permite conexões externas
- O firewall está configurado corretamente
- As credenciais estão corretas

---

**Depois de configurar as variáveis, a aplicação deve iniciar corretamente!** 🚀

