# Cobertura de Testes - Documentação

## ✅ Garantia de 90% de Cobertura

Este projeto **garante 90% de cobertura de código** através de testes unitários abrangentes. A cobertura é verificada automaticamente durante o build usando o JaCoCo.

## 📊 Estatísticas Atuais

- **Total de Testes**: 54 testes
- **Cobertura Mínima Exigida**: 90%
- **Status**: ✅ Todos os testes passando
- **Frameworks Utilizados**: JUnit 5, Mockito
- **Ferramenta de Cobertura**: JaCoCo 0.8.11

## 🧪 Distribuição de Testes

### AlunoServiceTest
- **Total**: 19 testes
- **Cenários de Sucesso**: 8 testes
- **Cenários de Erro**: 11 testes

### CursoServiceTest
- **Total**: 13 testes
- **Cenários de Sucesso**: 7 testes
- **Cenários de Erro**: 6 testes

### AlunoControllerTest
- **Total**: 10 testes
- **Testes de Endpoints**: 10 testes

### CursoControllerTest
- **Total**: 7 testes
- **Testes de Endpoints**: 7 testes

### GlobalExceptionHandlerTest
- **Total**: 5 testes
- **Testes de Tratamento de Exceções**: 5 testes

## 📈 Como Verificar a Cobertura

### Método 1: Via Script (Recomendado)
```bash
./test-coverage.sh
```

### Método 2: Via Maven
```bash
mvn clean test jacoco:report
```

### Método 3: Verificar Relatório HTML
Após executar os testes, abra o relatório:
```bash
# Linux
xdg-open target/site/jacoco/index.html

# macOS
open target/site/jacoco/index.html

# Windows
start target/site/jacoco/index.html
```

## 🎯 Cobertura por Camada

### Services (AlunoService, CursoService)
- ✅ Criação de recursos
- ✅ Listagem de recursos
- ✅ Busca por ID
- ✅ Atualização de recursos
- ✅ Exclusão de recursos
- ✅ Validações de negócio
- ✅ Tratamento de exceções

### Controllers (AlunoController, CursoController)
- ✅ Endpoints REST
- ✅ Validações de entrada
- ✅ Códigos de status HTTP
- ✅ Filtros e buscas

### Exception Handler (GlobalExceptionHandler)
- ✅ Tratamento de ResourceNotFoundException
- ✅ Tratamento de BusinessException
- ✅ Tratamento de validações
- ✅ Tratamento de erros genéricos

## 📝 Configuração do JaCoCo

A configuração no `pom.xml` garante que:

1. **Agente JaCoCo** é preparado antes dos testes
2. **Relatório HTML** é gerado após os testes
3. **Verificação de cobertura** é executada (configurada para 90%)

```xml
<plugin>
    <groupId>org.jacoco</groupId>
    <artifactId>jacoco-maven-plugin</artifactId>
    <version>0.8.11</version>
    <executions>
        <execution>
            <goals>
                <goal>prepare-agent</goal>
            </goals>
        </execution>
        <execution>
            <id>report</id>
            <phase>test</phase>
            <goals>
                <goal>report</goal>
            </goals>
        </execution>
        <execution>
            <id>check</id>
            <goals>
                <goal>check</goal>
            </goals>
            <configuration>
                <rules>
                    <rule>
                        <element>PACKAGE</element>
                        <limits>
                            <limit>
                                <counter>LINE</counter>
                                <value>COVEREDRATIO</value>
                                <minimum>0.90</minimum>
                            </limit>
                        </limits>
                    </rule>
                </rules>
            </configuration>
        </execution>
    </executions>
</plugin>
```

## ✅ Cenários Testados

### Cenários de Sucesso
- ✅ Criação de alunos e cursos
- ✅ Listagem de todos os recursos
- ✅ Busca por ID
- ✅ Atualização de recursos
- ✅ Exclusão de recursos
- ✅ Filtros e buscas (curso, semestre, média)

### Cenários de Erro
- ✅ Recursos não encontrados (404)
- ✅ Validações de entrada (400)
- ✅ Regras de negócio violadas (400)
- ✅ Dados duplicados (matrícula, email, nome de curso)
- ✅ Relacionamentos inválidos (curso inexistente)
- ✅ Erros internos do servidor (500)

## 🔍 Como Manter a Cobertura Acima de 90%

1. **Execute os testes antes de cada commit**
   ```bash
   mvn test
   ```

2. **Verifique a cobertura após adicionar novo código**
   ```bash
   mvn clean test jacoco:report
   ```

3. **Adicione testes para novas funcionalidades**
   - Para cada novo método, adicione testes de sucesso e erro
   - Teste validações e regras de negócio
   - Teste tratamento de exceções

4. **Revise testes existentes quando modificar código**
   - Atualize testes se a lógica mudar
   - Adicione novos testes se novos cenários surgirem

## 📚 Documentação Adicional

Para mais detalhes sobre os testes, consulte:
- [TESTES.md](TESTES.md) - Documentação completa dos testes
- [README.md](README.md) - Documentação geral do projeto

## 🎓 Boas Práticas Seguidas

1. **Isolamento**: Cada teste é independente
2. **Mocking**: Uso de mocks para isolar unidades
3. **Nomenclatura**: Nomes descritivos dos testes
4. **Cobertura**: Testes cobrem sucesso e erro
5. **Manutenibilidade**: Código de teste limpo e legível

## ✅ Checklist de Cobertura

- [x] Services testados (AlunoService, CursoService)
- [x] Controllers testados (AlunoController, CursoController)
- [x] Exception Handler testado (GlobalExceptionHandler)
- [x] Cenários de sucesso testados
- [x] Cenários de erro testados
- [x] Validações testadas
- [x] Regras de negócio testadas
- [x] Cobertura mínima de 90% garantida
- [x] Relatório de cobertura gerado
- [x] Documentação completa

## 🚀 Conclusão

O projeto **garante 90% de cobertura de código** através de **54 testes unitários** que cobrem todos os cenários críticos do sistema. A cobertura é verificada automaticamente durante o build, garantindo que novos códigos mantenham o padrão de qualidade estabelecido.

