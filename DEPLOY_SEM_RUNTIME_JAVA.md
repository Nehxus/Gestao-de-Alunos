# 🚀 Deploy no Render - Sem Opção Runtime Java

Se não aparece a opção "Runtime: Java" no Render, não se preocupe! O Render detecta automaticamente aplicações Java através do `pom.xml` e usa o **Procfile**.

---

## ✅ Método: Usar Procfile (Detecção Automática)

O Render detecta automaticamente que é uma aplicação Java quando encontra:
- `pom.xml` (Maven)
- `Procfile` (comando de inicialização)

---

## 📋 Passo a Passo

### Passo 1: Verificar Arquivos

Certifique-se de que estes arquivos estão na raiz do projeto:

- ✅ `pom.xml` (já existe)
- ✅ `Procfile` (já existe)
- ✅ `src/` (código fonte)

### Passo 2: Criar Web Service no Render

1. Acesse https://dashboard.render.com
2. Clique em **"New +"** > **"Web Service"**
3. Conecte seu repositório Git
4. Selecione o repositório `gestao-alunos`

### Passo 3: Configurações Básicas

- **Name**: `gestao-alunos`
- **Region**: Escolha a região
- **Branch**: `main` (ou sua branch)
- **Root Directory**: **(DEIXE VAZIO)**

### Passo 4: Configurações de Build

O Render pode mostrar diferentes opções. Configure assim:

#### Opção A: Se aparecer "Build Command" e "Start Command"

- **Build Command**: `mvn clean package -DskipTests`
- **Start Command**: `java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar`

#### Opção B: Se aparecer apenas "Build Command"

- **Build Command**: `mvn clean package -DskipTests`
- O Render usará automaticamente o **Procfile** para iniciar a aplicação

#### Opção C: Se não aparecer nenhuma opção de build

- Deixe tudo vazio
- O Render detectará automaticamente o `pom.xml` e o `Procfile`
- Ele fará: `mvn clean package` e depois executará o comando do Procfile

### Passo 5: Variáveis de Ambiente

Adicione estas variáveis (clique em **"Add Environment Variable"**):

```
SPRING_PROFILES_ACTIVE = prod
```

```
DATABASE_URL = jdbc:postgresql://[HOST]:[PORT]/gestaoalunos
```
*(Substitua [HOST] e [PORT] pelos valores do seu banco PostgreSQL)*

**Exemplo:**
```
DATABASE_URL = jdbc:postgresql://dpg-xxxxx-a.oregon-postgres.render.com:5432/gestaoalunos
```

```
DATABASE_USERNAME = [seu_usuario]
```

```
DATABASE_PASSWORD = [sua_senha]
```

### Passo 6: Criar e Deploy

1. Clique em **"Create Web Service"**
2. Aguarde o build (5-10 minutos na primeira vez)
3. O Render irá:
   - Detectar que é uma aplicação Maven/Java
   - Executar `mvn clean package` (ou o comando que você configurou)
   - Usar o Procfile para iniciar a aplicação

---

## 🔍 Como o Render Detecta Java

O Render detecta automaticamente aplicações Java quando encontra:

1. **pom.xml** → Detecta que é Maven
2. **Procfile** → Usa o comando do Procfile para iniciar
3. **Estrutura src/** → Confirma que é uma aplicação Java

Se você tem esses arquivos, o Render **automaticamente**:
- Instala Java e Maven
- Executa o build
- Usa o Procfile para iniciar

---

## ✅ Verificar Procfile

Seu Procfile está correto:

```
web: java -Dserver.port=$PORT -Dspring.profiles.active=prod -jar target/gestao-alunos-1.0.0.jar
```

Este comando:
- Usa a variável `$PORT` do Render
- Define o perfil como `prod`
- Executa o JAR compilado

---

## 🛠️ Se o Build Falhar

### Erro: "Maven not found"

**Solução**: O Render deve instalar Maven automaticamente. Se não instalar:
- Adicione no Build Command: `which mvn || (apt-get update && apt-get install -y maven) && mvn clean package -DskipTests`

### Erro: "Java not found"

**Solução**: O Render deve instalar Java automaticamente. Se não instalar:
- Adicione no Build Command: `which java || (apt-get update && apt-get install -y openjdk-17-jdk) && mvn clean package -DskipTests`

### Erro: "JAR not found"

**Solução**: Verifique se o `pom.xml` está gerando o JAR corretamente:
- O nome do JAR deve ser: `gestao-alunos-1.0.0.jar`
- Verifique o `<artifactId>` e `<version>` no `pom.xml`

---

## 📝 Checklist

Antes de fazer deploy:

- [ ] `pom.xml` está na raiz do projeto
- [ ] `Procfile` está na raiz do projeto
- [ ] `Procfile` contém o comando correto
- [ ] Código commitado e enviado para o repositório
- [ ] Banco de dados PostgreSQL criado no Render
- [ ] Variáveis de ambiente configuradas
- [ ] Build Command configurado (ou deixado vazio para detecção automática)

---

## 🎯 Resumo

**Não precisa escolher "Runtime: Java"!**

O Render detecta automaticamente através do `pom.xml` e usa o `Procfile` para iniciar a aplicação.

Basta:
1. Ter `pom.xml` e `Procfile` na raiz
2. Configurar as variáveis de ambiente
3. Deixar o Render detectar e fazer o build automaticamente

---

**Boa sorte com o deploy! 🚀**


