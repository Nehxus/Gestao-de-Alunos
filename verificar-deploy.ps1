# Script para verificar se tudo está pronto para deploy no Render

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Verificação para Deploy no Render" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

$erros = 0

# 1. Verificar Dockerfile
Write-Host "[1/5] Verificando Dockerfile..." -ForegroundColor Yellow
if (Test-Path "Dockerfile") {
    $dockerfileContent = Get-Content "Dockerfile" -Raw
    if ($dockerfileContent -match "FROM.*maven" -and $dockerfileContent -match "FROM.*eclipse-temurin") {
        Write-Host "  [OK] Dockerfile existe e está correto" -ForegroundColor Green
    } else {
        Write-Host "  [ERRO] Dockerfile existe mas pode estar incompleto" -ForegroundColor Red
        $erros++
    }
} else {
    Write-Host "  [ERRO] Dockerfile NAO encontrado!" -ForegroundColor Red
    $erros++
}

# 2. Verificar Procfile
Write-Host "[2/5] Verificando Procfile..." -ForegroundColor Yellow
if (Test-Path "Procfile") {
    $procfileContent = Get-Content "Procfile" -Raw
    if ($procfileContent -match "java.*-jar.*target/gestao-alunos") {
        Write-Host "  [OK] Procfile existe e está correto" -ForegroundColor Green
    } else {
        Write-Host "  [ERRO] Procfile existe mas pode estar incorreto" -ForegroundColor Red
        $erros++
    }
} else {
    Write-Host "  [ERRO] Procfile NAO encontrado!" -ForegroundColor Red
    $erros++
}

# 3. Verificar pom.xml
Write-Host "[3/5] Verificando pom.xml..." -ForegroundColor Yellow
if (Test-Path "pom.xml") {
    $pomContent = Get-Content "pom.xml" -Raw
    if ($pomContent -match "gestao-alunos" -and $pomContent -match "spring-boot") {
        Write-Host "  [OK] pom.xml existe e está correto" -ForegroundColor Green
    } else {
        Write-Host "  [ERRO] pom.xml pode estar incorreto" -ForegroundColor Red
        $erros++
    }
} else {
    Write-Host "  [ERRO] pom.xml NAO encontrado!" -ForegroundColor Red
    $erros++
}

# 4. Verificar .dockerignore
Write-Host "[4/5] Verificando .dockerignore..." -ForegroundColor Yellow
if (Test-Path ".dockerignore") {
    $dockerignoreContent = Get-Content ".dockerignore" -Raw
    if ($dockerignoreContent -match "`nDockerfile`n" -or $dockerignoreContent -match "^Dockerfile`n") {
        Write-Host "  [ERRO] .dockerignore está EXCLUINDO o Dockerfile!" -ForegroundColor Red
        Write-Host "     Remova a linha 'Dockerfile' do .dockerignore" -ForegroundColor Yellow
        $erros++
    } else {
        Write-Host "  [OK] .dockerignore não exclui o Dockerfile" -ForegroundColor Green
    }
} else {
    Write-Host "  [AVISO] .dockerignore não existe (opcional)" -ForegroundColor Yellow
}

# 5. Verificar se está no Git
Write-Host "[5/5] Verificando Git..." -ForegroundColor Yellow
$gitInstalled = $false
try {
    $null = git --version 2>&1
    if ($LASTEXITCODE -eq 0) {
        $gitInstalled = $true
    }
} catch {
    $gitInstalled = $false
}

if ($gitInstalled) {
    try {
        $gitStatusOutput = git status --porcelain Dockerfile 2>&1
        $gitExitCode = $LASTEXITCODE
        
        if ($gitExitCode -eq 0) {
            if ($gitStatusOutput -match "^\?\?") {
                Write-Host "  [ERRO] Dockerfile NAO está no Git!" -ForegroundColor Red
                Write-Host "     Execute: git add Dockerfile" -ForegroundColor Yellow
                Write-Host "     Execute: git commit -m 'Add Dockerfile'" -ForegroundColor Yellow
                Write-Host "     Execute: git push" -ForegroundColor Yellow
                $erros++
            } elseif ($gitStatusOutput -match "^M") {
                Write-Host "  [AVISO] Dockerfile foi modificado mas não commitado" -ForegroundColor Yellow
                Write-Host "     Execute: git add Dockerfile" -ForegroundColor Yellow
                Write-Host "     Execute: git commit -m 'Update Dockerfile'" -ForegroundColor Yellow
                Write-Host "     Execute: git push" -ForegroundColor Yellow
            } else {
                Write-Host "  [OK] Dockerfile está no Git" -ForegroundColor Green
            }
        } else {
            Write-Host "  [AVISO] Não foi possível verificar status do Git" -ForegroundColor Yellow
        }
    } catch {
        Write-Host "  [AVISO] Erro ao verificar Git: $_" -ForegroundColor Yellow
    }
} else {
    Write-Host "  [AVISO] Git não está instalado ou não está no PATH" -ForegroundColor Yellow
    Write-Host "     Instale o Git ou adicione ao PATH para verificar status" -ForegroundColor Yellow
}

Write-Host ""
Write-Host "========================================" -ForegroundColor Cyan
if ($erros -eq 0) {
    Write-Host "[SUCESSO] Tudo parece estar correto!" -ForegroundColor Green
    Write-Host ""
    Write-Host "Próximos passos:" -ForegroundColor Yellow
    Write-Host "1. Certifique-se de que Dockerfile está commitado:" -ForegroundColor White
    Write-Host "   git add Dockerfile" -ForegroundColor Gray
    Write-Host "   git commit -m 'Add Dockerfile'" -ForegroundColor Gray
    Write-Host "   git push" -ForegroundColor Gray
    Write-Host ""
    Write-Host "2. No Render, configure:" -ForegroundColor White
    Write-Host "   - Runtime: Docker (ou deixe vazio para usar Procfile)" -ForegroundColor Gray
    Write-Host "   - Root Directory: (VAZIO)" -ForegroundColor Gray
    Write-Host "   - Build Command: (VAZIO ou 'mvn clean package -DskipTests')" -ForegroundColor Gray
} else {
    Write-Host "[ERRO] Encontrados $erros problema(s)!" -ForegroundColor Red
    Write-Host ""
    Write-Host "Corrija os problemas acima antes de fazer deploy." -ForegroundColor Yellow
}
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
