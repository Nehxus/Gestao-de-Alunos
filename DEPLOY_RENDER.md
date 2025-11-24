# 🚀 Guia de Deploy no Render

Este guia explica como fazer o deploy da aplicação **Sistema de Gestão de Alunos** no Render.

## 📋 Pré-requisitos

1. Conta no Render (https://render.com)
2. Repositório Git (GitHub, GitLab ou Bitbucket)
3. Banco de dados PostgreSQL (pode ser criado no Render)

## 🔧 Passo a Passo

### 1. Preparar o Repositório

Certifique-se de que todos os arquivos estão commitados e enviados para o repositório:

```bash
git add .
git commit -m "Preparar para deploy no Render"
git push origin main
```

### 2. Criar Banco de Dados PostgreSQL no Render

1. Acesse o dashboard do Render
2. Clique em **"New +"** > **"PostgreSQL"**
3. Configure:
   - **Name**: `gestao-alunos-db`
   - **Database**: `gestaoalunos`
   - **User**: (será gerado automaticamente)
   - **Region**: Escolha a região mais próxima
   - **Plan**: Free (ou pago, conforme necessário)
4. Clique em **"Create Database"**
5. **Anote as credenciais** (URL, usuário e senha)

### 3. Criar o Web Service

1. No dashboard do Render, clique em **"New +"** > **"Web Service"**
2. Conecte seu repositório Git
3. Configure o serviço:

#### Configurações Básicas:
- **Name**: `gestao-alunos`
- **Region**: Mesma região do banco de dados
- **Branch**: `main` (ou sua branch principal)
- **Root Directory**: (deixe vazio se o projeto está na raiz)
- **Runtime**: `Docker`
- **Build Command**: (deixe vazio - o Dockerfile faz o build)
- **Start Command**: (deixe vazio - o Dockerfile define o comando)

#### Variáveis de Ambiente:
Adicione as seguintes variáveis de ambiente:

```
SPRING_PROFILES_ACTIVE=prod
DATABASE_URL=jdbc:postgresql://[HOST]:[PORT]/gestaoalunos
DATABASE_USERNAME=[seu_usuario]
DATABASE_PASSWORD=[sua_senha]
```

**Importante**: Substitua `[HOST]`, `[PORT]`, `[seu_usuario]` e `[sua_senha]` pelos valores do banco de dados criado no passo 2.

#### Exemplo de DATABASE_URL:
```
jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

### 4. Deploy

1. Clique em **"Create Web Service"**
2. O Render irá:
   - Clonar o repositório
   - Executar o build do Docker (compilar o projeto)
   - Criar a imagem Docker
   - Iniciar o container
3. Aguarde o build completar (pode levar 5-10 minutos na primeira vez)

### 5. Verificar o Deploy

Após o deploy, você verá uma URL como:
```
https://gestao-alunos.onrender.com
```

Acesse:
- **API Base**: `https://gestao-alunos.onrender.com`
- **Swagger UI**: `https://gestao-alunos.onrender.com/swagger-ui.html`
- **API Docs**: `https://gestao-alunos.onrender.com/api-docs`

## 🔍 Troubleshooting

### Erro: "failed to read dockerfile"

**Causa**: O Dockerfile não está no diretório raiz do repositório ou não foi commitado.

**Solução**:
1. Verifique se o `Dockerfile` está na raiz do projeto
2. Certifique-se de que foi commitado:
   ```bash
   git add Dockerfile
   git commit -m "Adicionar Dockerfile"
   git push
   ```

### Erro: "Build failed"

**Causa**: Problemas durante a compilação do projeto.

**Solução**:
1. Verifique os logs do build no Render
2. Certifique-se de que o `pom.xml` está correto
3. Verifique se todas as dependências estão disponíveis

### Erro de Conexão com Banco de Dados

**Causa**: Variáveis de ambiente incorretas ou banco não acessível.

**Solução**:
1. Verifique se as variáveis de ambiente estão corretas
2. Certifique-se de que o banco de dados está na mesma região
3. Verifique se o banco está rodando (Status: Available)

### Aplicação não inicia

**Causa**: Porta incorreta ou variáveis de ambiente faltando.

**Solução**:
1. O Render define automaticamente a variável `PORT`
2. O Spring Boot deve usar `server.port=${PORT:8080}` no `application.properties`
3. Verifique os logs do serviço no dashboard do Render

## 📝 Notas Importantes

1. **Plano Free**: O Render suspende serviços gratuitos após 15 minutos de inatividade. A primeira requisição pode demorar alguns segundos para "acordar" o serviço.

2. **Banco de Dados**: O plano free do PostgreSQL tem limitações. Para produção, considere um plano pago.

3. **Variáveis de Ambiente**: Nunca commite credenciais no código. Use sempre variáveis de ambiente.

4. **Logs**: Acesse os logs em tempo real no dashboard do Render para debug.

5. **Health Check**: O Render verifica a saúde do serviço. Certifique-se de que a aplicação responde em `/actuator/health` (se configurado) ou na rota raiz.

## 🔗 Links Úteis

- [Documentação do Render](https://render.com/docs)
- [Render Dashboard](https://dashboard.render.com)
- [Spring Boot no Render](https://render.com/docs/deploy-spring-boot)

## ✅ Checklist de Deploy

- [ ] Repositório Git configurado e código commitado
- [ ] Dockerfile na raiz do projeto
- [ ] Banco de dados PostgreSQL criado no Render
- [ ] Web Service criado no Render
- [ ] Variáveis de ambiente configuradas
- [ ] Build concluído com sucesso
- [ ] Aplicação acessível via URL
- [ ] Swagger UI funcionando
- [ ] Testes de API realizados

---

**Boa sorte com o deploy! 🚀**

