# Documentação de Testes - Sistema de Gestão de Alunos

## 📊 Cobertura de Testes

Este projeto garante **90% de cobertura de código** através de testes unitários utilizando JUnit 5 e Mockito. A cobertura é verificada automaticamente pelo JaCoCo durante o build do projeto.

## 🧪 Estrutura de Testes

### Camadas Testadas

1. **Services** (`AlunoService`, `CursoService`)
   - Testes de todas as operações CRUD
   - Testes de validações de negócio
   - Testes de exceções

2. **Controllers** (`AlunoController`, `CursoController`)
   - Testes de endpoints REST
   - Testes de validações de entrada
   - Testes de códigos de status HTTP

3. **Exception Handler** (`GlobalExceptionHandler`)
   - Testes de tratamento de exceções
   - Testes de respostas de erro padronizadas

## 📈 Executar Testes

### Executar todos os testes
```bash
mvn test
```

### Executar testes com cobertura
```bash
mvn clean test jacoco:report
```

### Verificar relatório de cobertura
Após executar os testes, o relatório HTML estará disponível em:
```
target/site/jacoco/index.html
```

### Verificar se a cobertura mínima foi atingida
```bash
mvn clean test jacoco:check
```

## ✅ Testes Implementados

### AlunoServiceTest (20 testes)

#### Testes de Criação
- ✅ `criarAluno_Sucesso` - Criação bem-sucedida
- ✅ `criarAluno_MatriculaJaExiste_DeveLancarException` - Validação de matrícula duplicada
- ✅ `criarAluno_EmailJaExiste_DeveLancarException` - Validação de email duplicado
- ✅ `criarAluno_CursoNaoEncontrado_DeveLancarException` - Validação de curso inexistente
- ✅ `criarAluno_MediaGeralNull_DeveDefinirZero` - Tratamento de média nula

#### Testes de Leitura
- ✅ `listarTodosAlunos_Sucesso` - Listagem de todos os alunos
- ✅ `buscarAlunoPorId_Sucesso` - Busca por ID bem-sucedida
- ✅ `buscarAlunoPorId_NaoEncontrado_DeveLancarException` - Aluno não encontrado
- ✅ `buscarAlunosPorCurso_Sucesso` - Busca por curso
- ✅ `buscarAlunosPorSemestre_Sucesso` - Busca por semestre
- ✅ `buscarAlunosPorMediaMinima_Sucesso` - Busca por média mínima

#### Testes de Atualização
- ✅ `atualizarAluno_Sucesso` - Atualização bem-sucedida
- ✅ `atualizarAluno_NaoEncontrado_DeveLancarException` - Aluno não encontrado
- ✅ `atualizarAluno_MatriculaJaExiste_DeveLancarException` - Matrícula duplicada
- ✅ `atualizarAluno_EmailJaExiste_DeveLancarException` - Email duplicado
- ✅ `atualizarAluno_CursoNaoEncontrado_DeveLancarException` - Curso inexistente
- ✅ `atualizarAluno_MediaGeralNull_DeveManterValorAnterior` - Média nula mantém valor anterior

#### Testes de Exclusão
- ✅ `deletarAluno_Sucesso` - Exclusão bem-sucedida
- ✅ `deletarAluno_NaoEncontrado_DeveLancarException` - Aluno não encontrado

### CursoServiceTest (13 testes)

#### Testes de Criação
- ✅ `criarCurso_Sucesso` - Criação bem-sucedida
- ✅ `criarCurso_NomeJaExiste_DeveLancarException` - Validação de nome duplicado

#### Testes de Leitura
- ✅ `listarTodosCursos_Sucesso` - Listagem de todos os cursos
- ✅ `buscarCursoPorId_Sucesso` - Busca por ID bem-sucedida
- ✅ `buscarCursoPorId_NaoEncontrado_DeveLancarException` - Curso não encontrado
- ✅ `buscarOuCriarCurso_CursoExistente_DeveRetornarExistente` - Busca ou criação (existente)
- ✅ `buscarOuCriarCurso_CursoNaoExistente_DeveCriarNovo` - Busca ou criação (novo)

#### Testes de Atualização
- ✅ `atualizarCurso_Sucesso` - Atualização bem-sucedida
- ✅ `atualizarCurso_NaoEncontrado_DeveLancarException` - Curso não encontrado
- ✅ `atualizarCurso_NomeJaExiste_DeveLancarException` - Nome duplicado
- ✅ `atualizarCurso_MesmoNome_DevePermitir` - Mesmo nome permite atualização

#### Testes de Exclusão
- ✅ `deletarCurso_Sucesso` - Exclusão bem-sucedida
- ✅ `deletarCurso_NaoEncontrado_DeveLancarException` - Curso não encontrado

### AlunoControllerTest (11 testes)

#### Testes de Endpoints
- ✅ `criarAluno_Sucesso` - POST /api/alunos (sucesso)
- ✅ `criarAluno_DadosInvalidos_DeveRetornarBadRequest` - POST /api/alunos (validação)
- ✅ `listarTodosAlunos_Sucesso` - GET /api/alunos
- ✅ `buscarAlunoPorId_Sucesso` - GET /api/alunos/{id}
- ✅ `atualizarAluno_Sucesso` - PUT /api/alunos/{id}
- ✅ `deletarAluno_Sucesso` - DELETE /api/alunos/{id}
- ✅ `buscarAlunosComFiltro_PorCurso_Sucesso` - GET /api/alunos/filtro?cursoId=X
- ✅ `buscarAlunosComFiltro_PorSemestre_Sucesso` - GET /api/alunos/filtro?semestre=X
- ✅ `buscarAlunosComFiltro_PorMediaMinima_Sucesso` - GET /api/alunos/filtro?mediaMinima=X
- ✅ `buscarAlunosComFiltro_SemFiltro_DeveListarTodos` - GET /api/alunos/filtro

### CursoControllerTest (7 testes)

#### Testes de Endpoints
- ✅ `criarCurso_Sucesso` - POST /api/cursos (sucesso)
- ✅ `criarCurso_DadosInvalidos_DeveRetornarBadRequest` - POST /api/cursos (validação)
- ✅ `listarTodosCursos_Sucesso` - GET /api/cursos
- ✅ `buscarCursoPorId_Sucesso` - GET /api/cursos/{id}
- ✅ `atualizarCurso_Sucesso` - PUT /api/cursos/{id}
- ✅ `atualizarCurso_DadosInvalidos_DeveRetornarBadRequest` - PUT /api/cursos/{id} (validação)
- ✅ `deletarCurso_Sucesso` - DELETE /api/cursos/{id}

### GlobalExceptionHandlerTest (5 testes)

#### Testes de Tratamento de Exceções
- ✅ `handleResourceNotFoundException_DeveRetornar404` - Tratamento de recurso não encontrado
- ✅ `handleBusinessException_DeveRetornar400` - Tratamento de erro de negócio
- ✅ `handleMethodArgumentNotValidException_DeveRetornar400ComDetalhes` - Tratamento de validação
- ✅ `handleConstraintViolationException_DeveRetornar400ComDetalhes` - Tratamento de violação de restrições
- ✅ `handleGlobalException_DeveRetornar500` - Tratamento de erro genérico

## 📊 Estatísticas de Testes

- **Total de Testes**: 56 testes
- **Cobertura Mínima**: 90%
- **Frameworks**: JUnit 5, Mockito
- **Ferramenta de Cobertura**: JaCoCo

## 🎯 Cenários Testados

### Cenários de Sucesso
- ✅ Criação de recursos
- ✅ Listagem de recursos
- ✅ Busca por ID
- ✅ Atualização de recursos
- ✅ Exclusão de recursos
- ✅ Filtros e buscas

### Cenários de Erro
- ✅ Recursos não encontrados (404)
- ✅ Validações de entrada (400)
- ✅ Regras de negócio violadas (400)
- ✅ Erros internos do servidor (500)

### Validações Testadas
- ✅ Campos obrigatórios
- ✅ Formato de email
- ✅ Valores mínimos e máximos
- ✅ Unicidade de dados (matrícula, email, nome de curso)
- ✅ Relacionamentos (curso existe)

## 📝 Configuração do JaCoCo

O projeto está configurado para:
- Gerar relatório de cobertura após cada execução de testes
- Verificar automaticamente se a cobertura mínima de 90% foi atingida
- Falhar o build se a cobertura estiver abaixo de 90%

### Configuração no pom.xml

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

## 🔍 Verificação de Cobertura

Para verificar se a cobertura está acima de 90%:

```bash
mvn clean test jacoco:check
```

Se a cobertura estiver abaixo de 90%, o build falhará com uma mensagem indicando a cobertura atual.

## 📖 Boas Práticas Seguidas

1. **Isolamento**: Cada teste é independente e não depende de outros testes
2. **Mocking**: Uso de mocks para isolar unidades de teste
3. **Nomenclatura**: Nomes descritivos que indicam o que está sendo testado
4. **Cobertura**: Testes cobrem tanto cenários de sucesso quanto de erro
5. **Manutenibilidade**: Código de teste limpo e fácil de entender

## 🚀 Próximos Passos

Para manter a cobertura acima de 90%:
1. Execute os testes antes de cada commit
2. Verifique o relatório de cobertura após adicionar novo código
3. Adicione testes para novas funcionalidades
4. Revise testes existentes quando modificar código

## 📚 Referências

- [JUnit 5 Documentation](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [JaCoCo Documentation](https://www.jacoco.org/jacoco/trunk/doc/)

