# Correção do Erro - Swagger não estava abrindo

## ❌ Erro Encontrado

```
Caused by: java.lang.IllegalArgumentException: scale has no meaning for floating point numbers
```

## 🔍 Causa do Problema

O erro ocorria porque na entidade `Aluno.java`, o campo `mediaGeral` estava definido com:

```java
@Column(name = "media_geral", precision = 4, scale = 2)
private Double mediaGeral;
```

O problema é que `precision` e `scale` **não podem ser usados com tipos de ponto flutuante** como `Double` ou `Float` no Hibernate. Essas anotações só funcionam com tipos decimais como `BigDecimal`.

## ✅ Solução Aplicada

Removi as anotações `precision` e `scale` do campo `mediaGeral`:

```java
@Column(name = "media_geral")
private Double mediaGeral;
```

## 📝 Explicação Técnica

- **Double/Float**: São tipos de ponto flutuante binário (IEEE 754) e não têm precisão decimal fixa
- **BigDecimal**: É um tipo decimal que permite precisão e escala definidas
- **Hibernate**: Não permite usar `scale` com tipos de ponto flutuante porque não faz sentido conceitualmente

## 🚀 Status

✅ **Erro corrigido!** A aplicação agora deve iniciar corretamente.

## 📚 Como Testar

1. Inicie a aplicação:
```bash
mvn spring-boot:run
```

2. Aguarde a mensagem:
```
Started GestaoAlunosApplication in X.XXX seconds
```

3. Acesse o Swagger:
```
http://localhost:8080/swagger-ui.html
```

## 💡 Nota

Se no futuro você precisar de precisão decimal exata para a média, considere usar `BigDecimal` em vez de `Double`:

```java
@Column(name = "media_geral", precision = 4, scale = 2)
private BigDecimal mediaGeral;
```

Mas para o caso de uso atual (médias de alunos), `Double` é suficiente e mais simples de trabalhar.

