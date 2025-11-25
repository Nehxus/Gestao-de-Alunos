#!/bin/bash

# Script para executar testes e verificar cobertura

echo "=========================================="
echo "  Sistema de Gestão de Alunos - Testes"
echo "=========================================="
echo ""

echo "📦 Limpando e compilando o projeto..."
mvn clean compile

echo ""
echo "🧪 Executando testes..."
mvn test

echo ""
echo "📊 Gerando relatório de cobertura..."
mvn jacoco:report

echo ""
echo "✅ Testes concluídos!"
echo ""
echo "📄 Relatório de cobertura disponível em:"
echo "   target/site/jacoco/index.html"
echo ""
echo "Para abrir o relatório no navegador, execute:"
echo "   xdg-open target/site/jacoco/index.html"
echo ""

# Verificar se o relatório foi gerado
if [ -f "target/site/jacoco/index.html" ]; then
    echo "✅ Relatório de cobertura gerado com sucesso!"
else
    echo "❌ Erro ao gerar relatório de cobertura"
    exit 1
fi

