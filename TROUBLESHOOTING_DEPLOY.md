# 🔧 Troubleshooting - Deploy no Render

## Erro: "failed to read dockerfile: open Dockerfile: no such file or directory"

Este erro geralmente ocorre por um dos seguintes motivos:

### ✅ Solução 1: Verificar se o Dockerfile está commitado no Git

O Render precisa que o Dockerfile esteja no repositório Git.

**Verificar:**
```bash
git status Dockerfile
```

**Se não estiver commitado:**
```bash
git add Dockerfile
git commit -m "Adicionar Dockerfile para deploy"
git push
```

### ✅ Solução 2: Verificar Root Directory no Render

No dashboard do Render, ao criar/editar o Web Service:

1. Vá em **Settings** > **Build & Deploy**
2. Verifique o campo **Root Directory**
3. **Deixe vazio** se o Dockerfile está na raiz do projeto
4. Se seu projeto está em uma subpasta, especifique o caminho (ex: `backend/`)

### ✅ Solução 3: Verificar se o Dockerfile está na raiz

O Dockerfile **deve estar na raiz** do repositório, no mesmo nível que:
- `pom.xml`
- `src/`
- `README.md`

**Estrutura correta:**
```
gestao-alunos/
├── Dockerfile          ← DEVE ESTAR AQUI
├── pom.xml
├── src/
├── README.md
└── ...
```

### ✅ Solução 4: Verificar .dockerignore

O arquivo `.dockerignore` **NÃO deve excluir o Dockerfile**.

**Verifique se NÃO tem esta linha no .dockerignore:**
```
Dockerfile  ← REMOVER ESTA LINHA SE EXISTIR
```

### ✅ Solução 5: Usar Build Command explícito

Se ainda não funcionar, tente especificar o Dockerfile explicitamente:

No Render, em **Settings** > **Build & Deploy**:
- **Build Command**: `docker build -f Dockerfile -t gestao-alunos .`

Mas geralmente não é necessário se o Dockerfile está na raiz.

### ✅ Solução 6: Verificar se o repositório está conectado corretamente

1. No Render, vá em **Settings** > **Build & Deploy**
2. Verifique se o **Repository** está conectado corretamente
3. Verifique se a **Branch** está correta (geralmente `main` ou `master`)
4. Clique em **Manual Deploy** > **Deploy latest commit** para forçar um novo build

### ✅ Solução 7: Verificar logs do build

1. No Render, vá em **Logs**
2. Procure por mensagens de erro relacionadas ao Dockerfile
3. Verifique se há mensagens sobre "context" ou "build"

### ✅ Solução 8: Testar localmente

Antes de fazer deploy, teste o Dockerfile localmente:

```bash
# Build local
docker build -t gestao-alunos .

# Se funcionar localmente, o problema está na configuração do Render
```

## 🔍 Checklist Rápido

Antes de tentar fazer deploy novamente, verifique:

- [ ] Dockerfile está na raiz do projeto
- [ ] Dockerfile está commitado no Git (`git status Dockerfile`)
- [ ] Dockerfile foi enviado para o repositório remoto (`git push`)
- [ ] `.dockerignore` NÃO exclui o Dockerfile
- [ ] Root Directory no Render está vazio (ou correto)
- [ ] Repositório está conectado corretamente no Render
- [ ] Branch está correta no Render

## 📝 Comandos Úteis

```bash
# Verificar se Dockerfile existe
ls -la Dockerfile

# Verificar conteúdo do Dockerfile
cat Dockerfile

# Verificar se está no Git
git ls-files | grep Dockerfile

# Adicionar e commitar
git add Dockerfile .dockerignore
git commit -m "Corrigir Dockerfile para deploy no Render"
git push
```

## 🆘 Se Nada Funcionar

1. **Delete o serviço no Render** e crie novamente
2. **Use o Procfile** ao invés do Dockerfile (alternativa):
   - No Render, escolha **Runtime**: `Java`
   - Configure **Build Command**: `mvn clean package -DskipTests`
   - Configure **Start Command**: `java -jar target/gestao-alunos-1.0.0.jar`
   - Configure variáveis de ambiente normalmente

## 📞 Suporte

Se o problema persistir:
1. Verifique os logs completos do build no Render
2. Copie a mensagem de erro completa
3. Verifique se o Dockerfile funciona localmente
4. Entre em contato com o suporte do Render ou verifique a documentação

---

**Última atualização**: Após corrigir o `.dockerignore` que estava excluindo o Dockerfile.

