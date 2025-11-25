#!/bin/bash

# Script para executar a aplicação

echo "=== Sistema de Gestão de Alunos ==="
echo ""
echo "Escolha o perfil:"
echo "1) Desenvolvimento (H2)"
echo "2) Produção (PostgreSQL)"
read -p "Opção (1 ou 2): " opcao

case $opcao in
    1)
        echo "Iniciando aplicação em modo DESENVOLVIMENTO..."
        mvn spring-boot:run -Dspring-boot.run.profiles=dev
        ;;
    2)
        echo "Iniciando aplicação em modo PRODUÇÃO..."
        mvn spring-boot:run -Dspring-boot.run.profiles=prod
        ;;
    *)
        echo "Opção inválida. Iniciando em modo DESENVOLVIMENTO..."
        mvn spring-boot:run -Dspring-boot.run.profiles=dev
        ;;
esac

