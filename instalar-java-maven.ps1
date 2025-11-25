# Script de Instalação do Java 17 e Maven
# Execute este script como Administrador (clique com botão direito > Executar como administrador)

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Instalador Java 17 e Maven" -ForegroundColor Cyan
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""

# Verificar se está executando como administrador
$isAdmin = ([Security.Principal.WindowsPrincipal] [Security.Principal.WindowsIdentity]::GetCurrent()).IsInRole([Security.Principal.WindowsBuiltInRole]::Administrator)
if (-not $isAdmin) {
    Write-Host "ERRO: Este script precisa ser executado como Administrador!" -ForegroundColor Red
    Write-Host "Clique com botão direito no arquivo e selecione 'Executar como administrador'" -ForegroundColor Yellow
    pause
    exit 1
}

# Caminhos
$javaZip = "$env:TEMP\OpenJDK17.zip"
$mavenZip = "$env:TEMP\apache-maven.zip"
$javaDir = "C:\Program Files\Java"
$mavenDir = "C:\Program Files\Apache"

# Verificar se os arquivos foram baixados
if (-not (Test-Path $javaZip)) {
    Write-Host "ERRO: Arquivo Java não encontrado em: $javaZip" -ForegroundColor Red
    Write-Host "Por favor, baixe o Java 17 primeiro." -ForegroundColor Yellow
    pause
    exit 1
}

if (-not (Test-Path $mavenZip)) {
    Write-Host "ERRO: Arquivo Maven não encontrado em: $mavenZip" -ForegroundColor Red
    Write-Host "Por favor, baixe o Maven primeiro." -ForegroundColor Yellow
    pause
    exit 1
}

# 1. Instalar Java
Write-Host "[1/4] Extraindo Java 17..." -ForegroundColor Yellow
if (-not (Test-Path $javaDir)) {
    New-Item -ItemType Directory -Path $javaDir -Force | Out-Null
}

# Extrair Java
Expand-Archive -Path $javaZip -DestinationPath $javaDir -Force
$javaExtracted = Get-ChildItem -Path $javaDir -Directory | Where-Object { $_.Name -like "jdk*" } | Select-Object -First 1

if ($javaExtracted) {
    $javaHome = $javaExtracted.FullName
    Write-Host "Java extraído para: $javaHome" -ForegroundColor Green
} else {
    Write-Host "ERRO: Não foi possível encontrar o diretório do Java após extração" -ForegroundColor Red
    pause
    exit 1
}

# 2. Instalar Maven
Write-Host "[2/4] Extraindo Maven..." -ForegroundColor Yellow
if (-not (Test-Path $mavenDir)) {
    New-Item -ItemType Directory -Path $mavenDir -Force | Out-Null
}

# Extrair Maven
Expand-Archive -Path $mavenZip -DestinationPath $mavenDir -Force
$mavenExtracted = Get-ChildItem -Path $mavenDir -Directory | Where-Object { $_.Name -like "apache-maven*" } | Select-Object -First 1

if ($mavenExtracted) {
    $mavenHome = $mavenExtracted.FullName
    Write-Host "Maven extraído para: $mavenHome" -ForegroundColor Green
} else {
    Write-Host "ERRO: Não foi possível encontrar o diretório do Maven após extração" -ForegroundColor Red
    pause
    exit 1
}

# 3. Configurar Variáveis de Ambiente
Write-Host "[3/4] Configurando variáveis de ambiente..." -ForegroundColor Yellow

# Configurar JAVA_HOME
[Environment]::SetEnvironmentVariable("JAVA_HOME", $javaHome, "Machine")
Write-Host "JAVA_HOME configurado: $javaHome" -ForegroundColor Green

# Configurar MAVEN_HOME
[Environment]::SetEnvironmentVariable("MAVEN_HOME", $mavenHome, "Machine")
Write-Host "MAVEN_HOME configurado: $mavenHome" -ForegroundColor Green

# Adicionar ao PATH
$currentPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
$javaBin = "$javaHome\bin"
$mavenBin = "$mavenHome\bin"

if ($currentPath -notlike "*$javaBin*") {
    [Environment]::SetEnvironmentVariable("Path", "$currentPath;$javaBin", "Machine")
    Write-Host "Java adicionado ao PATH" -ForegroundColor Green
}

if ($currentPath -notlike "*$mavenBin*") {
    $newPath = [Environment]::GetEnvironmentVariable("Path", "Machine")
    [Environment]::SetEnvironmentVariable("Path", "$newPath;$mavenBin", "Machine")
    Write-Host "Maven adicionado ao PATH" -ForegroundColor Green
}

# 4. Verificar Instalação
Write-Host "[4/4] Verificando instalação..." -ForegroundColor Yellow
Write-Host ""

# Atualizar PATH na sessão atual
$env:JAVA_HOME = $javaHome
$env:MAVEN_HOME = $mavenHome
$env:Path = "$env:Path;$javaBin;$mavenBin"

Write-Host "========================================" -ForegroundColor Cyan
Write-Host "Instalação Concluída!" -ForegroundColor Green
Write-Host "========================================" -ForegroundColor Cyan
Write-Host ""
Write-Host "Java 17:" -ForegroundColor Yellow
& "$javaBin\java.exe" -version 2>&1 | Select-Object -First 1
Write-Host ""
Write-Host "Maven:" -ForegroundColor Yellow
& "$mavenBin\mvn.cmd" -version 2>&1 | Select-Object -First 3
Write-Host ""
Write-Host "NOTA: Você precisa fechar e reabrir o terminal para usar os comandos 'java' e 'mvn' em qualquer lugar." -ForegroundColor Cyan
Write-Host ""
Write-Host "Para testar, abra um novo terminal e execute:" -ForegroundColor Yellow
Write-Host "  java -version" -ForegroundColor White
Write-Host "  mvn -version" -ForegroundColor White
Write-Host ""

pause

