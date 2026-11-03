# AGENTS.md

Instruções para agentes de IA (Claude Code, Copilot, Codex e outros) que trabalham nesta pasta de estudos.

## Sobre o projeto

Este é um projeto **de estudo**. O dono está aprendendo backend com **Java 25** e **Spring Boot 4.1**, seguindo o roteiro em `docs/roteiro-java-spring-boot.md` (8 fases, módulos 0 a 30, mais a trilha de lógica L1 a L6). O intuito do estudo, as decisões e a especificação do projeto final estão em `docs/projeto-final-java-spring-boot.md`.

- Responda sempre em **português do Brasil**, com tom didático e direto.
- O **código do dono é em inglês**: classes, métodos, tabelas, endpoints e mensagens de commit. As explicações são em português.
- O dono é QA. Já escreveu Java para automação (Cucumber, JUnit, Page Objects), Playwright com TypeScript e SQL com PostgreSQL. Aproveite isso nas explicações: compare com Given/When/Then, classes de equivalência e o roteiro de SQL. Não explique do zero o que ele já domina, como classes, herança e laços.

| Fase | Módulos | O que o dono pratica | Onde |
|------|---------|----------------------|------|
| 0 | 0 | JDK, Maven, IntelliJ, MySQL no Docker | `clinic-billing-api/compose.yaml` |
| 1 | 1 a 6 | Java fora dos testes: `main`, Maven, records, BigDecimal, `java.time`, streams, exceções, JUnit | `playground/` e `billing-core/` |
| 2 | 7 | HTTP com `curl` | `playground/modulo-07/notas.md` |
| 2 | 8 | Servidor HTTP sem framework | `raw-http/` |
| 2 | 9 a 12 | Spring, API em memória, validação, testes da camada web | `clinic-billing-api/` |
| 3 | 13 | MySQL para quem estudou PostgreSQL | `sql/modulo-13/` |
| 3 | 14 a 18 | Flyway, JPA, N+1, transações, testes de repositório | `clinic-billing-api/` |
| 4 | 19 a 22 | Status derivado, pagamentos, Mockito, CPF | `clinic-billing-api/` |
| 5 | 23 a 26 | Paginação, OpenAPI, Docker, CI | `clinic-billing-api/` |
| 6 | 27 e 29 | Idempotência, Spring Security | `clinic-billing-api/` |
| 6 | 28 | Testes da API com Playwright | **outro repositório** (o do roteiro de Playwright) |
| 7 | 30 | Projeto final | `clinic-billing-api/` |
| Trilha | L1 a L6 | Lógica e estruturas de dados | `logica/` |

O objetivo é **aprender**, não só ter código que compila. Não existe gabarito: o dono resolve e o agente corrige. Isso muda o que um agente deve fazer (veja "Limites para agentes").

A partir do módulo 9, os exercícios **fazem evoluir um único projeto** (`clinic-billing-api`). Lá não existe "um arquivo por exercício": a unidade de entrega é o **commit** ou o **branch** (veja "Organização dos arquivos").

## Estrutura

```
~/Projects/spring-boot-lab/             esta pasta (repositório Git de estudo, público)
  AGENTS.md, CLAUDE.md
  docs/roteiro-java-spring-boot.md      o roteiro de estudos
  docs/projeto-final-java-spring-boot.md  intuito, decisões e especificação do projeto final
  playground/modulo-XX/ExN.java         exercícios curtos de linguagem, um arquivo por exercício
  playground/modulo-XX/notas.md         respostas em texto pedidas pelo enunciado
  billing-core/                         projeto Maven da Fase 1 (domínio em Java puro e testes)
  raw-http/                             servidor HTTP sem framework (módulo 8)
  sql/modulo-13/exN.sql                 exercícios de MySQL do módulo 13
  entrevistas/fase-N.md                 respostas às questões de entrevista de cada fase
  logica/beecrowd/<número>/Main.java    trilha de lógica
  logica/leetcode/<número>-<nome>.java
  clinic-billing-api/                   projeto principal: repositório Git próprio (público, portfólio),
                                        listado no .gitignore desta pasta
```

Dentro do `clinic-billing-api`, a estrutura esperada está na seção 5.6 de `docs/projeto-final-java-spring-boot.md`: pacote por funcionalidade (`com.clinicbilling.patient`, `charge` e `shared`), migrations em `src/main/resources/db/migration` e o profile de teste em `src/test/resources/application-test.properties`.

## Ambiente

O sistema é o **Omarchy** (baseado em Arch). Java 25 (Temurin) e Maven 3 foram instalados com o **mise**. O IntelliJ IDEA é a IDE do dono.

### O banco roda no Docker, e o agente não mexe no Docker

O MySQL 8.4 roda no container `billing-mysql`, criado pelo `clinic-billing-api/compose.yaml`, com a porta publicada só em `127.0.0.1:3306`.

| Banco | Uso | Pode ser alterado pelo agente? |
|-------|-----|-------------------------------|
| `clinic_billing` | desenvolvimento: o que o dono usa com a aplicação e o `curl` | **não** |
| `clinic_billing_test` | testes automatizados (profile `test`) | só pelos testes |

- Usuário e senha `billing` / `billing` (credenciais de estudo local; não reutilize nem copie para outros lugares).
- **O Omarchy não coloca o usuário no grupo `docker`**, então todo comando `docker` pede `sudo` e senha. **Um agente nunca roda `sudo` nem `docker`.** Se o banco estiver fora do ar, peça ao dono para rodar `sudo docker compose up -d` dentro de `clinic-billing-api/`.
- Verificar se o banco está no ar, sem precisar de cliente MySQL:

  ```bash
  timeout 2 bash -c '</dev/tcp/127.0.0.1/3306' && echo "MySQL no ar" || echo "MySQL fora do ar"
  ```

- Consultar o banco: só se existir um cliente `mysql` no sistema (`command -v mysql`). Se não existir, **não instale**; peça ao dono para rodar a consulta com `sudo docker exec -it billing-mysql mysql -u billing -pbilling clinic_billing`. Quando existir, use TCP explícito (com `localhost`, o cliente tenta um socket local que não existe fora do container) e **apenas** `SELECT`, `SHOW`, `DESCRIBE` e `EXPLAIN`:

  ```bash
  mysql -h 127.0.0.1 -P 3306 --protocol=TCP -u billing -pbilling clinic_billing -e "SELECT version, description, success FROM flyway_schema_history;"
  ```

### Por que aqui não existe o "executar e desfazer" do projeto de SQL

No projeto de SQL (PostgreSQL), o agente rodava os arquivos do dono dentro de `BEGIN` e `ROLLBACK`. **No MySQL isso não funciona para DDL**:

1. No MySQL, `CREATE TABLE`, `ALTER TABLE`, `DROP` e outros comandos de definição fazem um **commit implícito**: confirmam a transação aberta antes de rodar.
2. Por isso, um `ROLLBACK` no fim não desfaz a criação de uma tabela, e também não desfaz os `INSERT` que vieram antes dela.
3. Conclusão: **arquivos `.sql` do dono não são executados pelo agente.** Leia e analise. Para provar um ponto, rode só uma expressão que não toca em tabela (por exemplo, `SELECT 7 / 2, 7 DIV 2;`), se houver cliente `mysql`.

Fonte: [MySQL 8.4: Statements That Cause an Implicit Commit](https://dev.mysql.com/doc/refman/8.4/en/implicit-commit.html).

### Comandos por parte do estudo

| Parte | Como verificar | Observações |
|-------|----------------|-------------|
| `playground/` | `java playground/modulo-02/Ex1.java` (com argumentos, se o exercício pedir) | Execução direta de arquivo (JEP 330). Alguns exercícios **devem** falhar (por exemplo, sem argumentos); isso não é erro do dono. |
| `billing-core/` | `cd billing-core && mvn -q test` | Maven do sistema (mise). Não tem banco. |
| `raw-http/` | veja o bloco abaixo | O servidor fica rodando para sempre: nunca deixe sem `timeout`. |
| `clinic-billing-api/` (Fase 2) | `cd clinic-billing-api && ./mvnw -q test` | Antes do módulo 14 não há banco. |
| `clinic-billing-api/` (Fase 3 em diante) | `cd clinic-billing-api && SPRING_PROFILES_ACTIVE=test ./mvnw -q test` | **Sempre com o profile `test`** (veja abaixo). Precisa do MySQL no ar. |
| Uma classe de teste | acrescente `-Dtest=PaymentServiceTest` | |
| Entrega final | `SPRING_PROFILES_ACTIVE=test ./mvnw -B verify` | É o mesmo comando do CI (módulo 26). |
| `logica/beecrowd/` | `java logica/beecrowd/1018/Main.java < entrada.txt` | Crie a entrada de teste numa pasta temporária, não na pasta do dono. |

Servidor do módulo 8, sempre com tempo limite:

```bash
cd raw-http
timeout 20 java src/com/clinicbilling/rawhttp/RawHttpServer.java & sleep 3
curl -si "http://localhost:8000/hello?name=Ana"
kill $! 2>/dev/null
```

#### Por que os testes rodam com `SPRING_PROFILES_ACTIVE=test`

1. Um teste com `@SpringBootTest` (como o `contextLoads` que o Initializr gera) sobe a aplicação inteira.
2. Subir a aplicação roda o **Flyway**, que aplica as migrations pendentes no banco configurado.
3. Sem o profile `test`, esse banco é o `clinic_billing`, o de desenvolvimento do dono. O agente estaria alterando o schema dele.
4. Com `SPRING_PROFILES_ACTIVE=test`, todos os testes usam o `clinic_billing_test`, que é descartável. É o mesmo que o CI faz.

### A aplicação e o `curl`

- **Não suba a aplicação** (`./mvnw spring-boot:run` ou `java -jar`) sem pedido do dono. Ela usa o banco de desenvolvimento, aplica migrations nele e ocupa a porta 8080, que o dono provavelmente já está usando.
- Se a aplicação **já estiver rodando**, o agente pode fazer requisições `GET` com `curl -i` para conferir um comportamento. `POST`, `PUT` e `DELETE` gravam no banco do dono: só com pedido explícito.
- `sudo docker compose --profile app up` (módulo 25) e `docker build` exigem `sudo`: peça ao dono.

## O código Spring do roteiro não foi compilado

O roteiro avisa, na nota final, que os exemplos **Spring** (Fases 2 a 6) foram escritos contra a documentação do Spring Boot 4.1 e revisados à mão, mas **não compilados**. Os exemplos de Java puro e o SQL foram executados.

Se um erro do dono vier de um trecho copiado do roteiro (import errado, nome de método, comportamento diferente do descrito), **diga isso claramente**, explique a causa com a documentação oficial e mostre a correção. Não culpe o dono por um erro do material.

## Ao corrigir um exercício

1. **Identifique o exercício** equivalente no roteiro.
   - Pelo caminho: `playground/modulo-03/Ex2.java` é o Módulo 3, Exercício 2; `sql/modulo-13/desafio.sql` é o Desafio do Módulo 13.
   - No `clinic-billing-api`: pelo escopo da mensagem de commit ou pelo nome do branch (`feat(m16): ...`, `feature/m16-charges`). Use `git log --oneline -15` e `git diff main...HEAD` (ou `git show <commit>`).
   - Se nada indicar, deduza pelo conteúdo e diga qual exercício você acha que é.
   - Leia a seção do módulo em `docs/roteiro-java-spring-boot.md` (Objetivo, Conceitos, Exemplo e o enunciado exato) antes de corrigir.
2. **Verifique.**
   - Compile e rode os testes com o comando da tabela "Comandos por parte do estudo" (ou explique por que não rodou).
   - Confira se o resultado responde ao enunciado: status HTTP, headers (`Location`), formato do JSON, mensagem de erro, saída do `main`.
   - Confira se a solução usa o **conceito que o módulo quer ensinar** (exemplo: o exercício pede `@ParameterizedTest`, ou pede `@EntityGraph`).
   - Quando fizer sentido, leia o SQL que o Hibernate gerou no log dos testes para provar um ponto (N+1, `where ... and version=?`).
3. **Respeite o módulo.** Não cobre o que ainda não foi ensinado. Exemplos: JPA antes do módulo 15, ProblemDetail antes do 11, `@WebMvcTest` antes do 12, `@Version` antes do 17, `Clock` como bean antes do 16, paginação antes do 23, segurança antes do 29. Se a solução usar algo de módulo futuro, aceite e marque como "spoiler do Módulo N".
4. **Não edite o código do dono**, a menos que ele peça. Mostre a correção na resposta para que ele digite (o roteiro recomenda digitar em vez de copiar).

### O que procurar, por nível

- **🔴 Errado** (resultado incorreto, não compila ou é perigoso):
  - **Dinheiro e datas:** `double` ou `float` para dinheiro; `new BigDecimal(0.1)` em vez de `new BigDecimal("0.1")`; `BigDecimal.equals` para comparar valor (deveria ser `compareTo`); retorno de `add`/`subtract` ignorado (`amount.add(x);`); `divide` sem escala e `RoundingMode`; `LocalDate.now()` ou `LocalDateTime.now()` sem `Clock`.
  - **Java:** `==` para comparar `String`, `BigDecimal` ou `Long`; `equals` sobrescrito sem `hashCode`; `Optional.get()` sem verificar; exceção capturada e engolida (`catch` vazio).
  - **Spring e API:** `@RequestBody` sem `@Valid`; entidade JPA devolvida direto pelo controller; regra de negócio que devolve `500` em vez de `409`; resposta de erro com stack trace ou SQL; `POST` de criação sem `201` e `Location`; imports do Spring Boot 3 que não existem no 4 (veja o apêndice A do roteiro: `@MockBean`, pacotes antigos de `@WebMvcTest` e `@DataJpaTest`).
  - **Persistência:** `ddl-auto=update` ou `create`; migration que **já rodou** editada (quebra o checksum); `@Enumerated` sem `EnumType.STRING` (o padrão é `ORDINAL`); enum em coluna `VARCHAR` sem `columnDefinition = "varchar(20)"`; `OVERDUE` gravado no banco (é status derivado); N+1 numa listagem; `@Transactional` em método `private` ou chamado pela própria classe (`this.metodo()`); `LazyInitializationException` contornada ligando `open-in-view`.
  - **Concorrência:** estado mutável num bean singleton com `HashMap` ou `long` em vez de `ConcurrentHashMap` e `AtomicLong`.
  - **Dados sensíveis:** CPF completo em log; número de cartão, validade ou CVV recebidos ou guardados (só a forma de pagamento `CARD`); senha real no repositório.
  - **Testes:** teste sem assertion; teste que depende de outro ou da ordem de execução; teste que grava no banco de desenvolvimento; `assertEquals` com `BigDecimal` de escalas diferentes (deveria ser `isEqualByComparingTo`).
- **🟡 Frágil** (funciona hoje, quebra fácil): injeção por campo com `@Autowired` em vez de construtor; `FetchType.EAGER`; regra de negócio no controller; setters públicos que deixam pular a regra da entidade; `catch (Exception e)` genérico fora do handler global; `Thread.sleep` em teste; `@SpringBootTest` onde um slice (`@WebMvcTest`, `@DataJpaTest`) ou um teste de unidade bastaria; fronteira faltando nos testes (o próprio dia do vencimento, R$ 249,99 e R$ 250,01); constraint do banco faltando quando a regra existe só no Java; `System.out.println` na aplicação Spring em vez de logger; `findAll()` sem paginação a partir do módulo 23; dependência adicionada sem versão compatível com o Boot 4 (springdoc 2.x).
- **🟢 Estilo** (legibilidade): nomes em português no código (o dono escolheu inglês); nome de teste que descreve o método (`testPay`) e não a regra (`canceledChargeCannotBePaid`); `import x.*`; método longo que pede extração; pacote por camada (`controllers/`) em vez de por funcionalidade; mensagem de commit fora do Conventional Commits.

### Formato da resposta

```
## Resumo
Exercício identificado · compilou e testou ou não · atende ao enunciado? · 🔴 X  🟡 Y  🟢 Z

## Resultado
O que rodou (comando), o que voltou (testes que passaram ou falharam, trecho relevante do log
ou da resposta HTTP, não a saída inteira) e se responde ao enunciado.

## Correções
Para cada achado: nível, arquivo e linha, o que está errado, por que (fundamento em passos atômicos),
trecho atual e trecho corrigido. Corrija o mínimo necessário, sem reescrever a classe inteira.

## Alternativas
De 1 a 3 outras formas de resolver, com o trade-off de cada uma.
Só conceitos do módulo atual ou anteriores; algo mais avançado vai marcado como "spoiler do Módulo N".

## Conceitos para revisar
O item exato do roteiro (exemplo: "Módulo 16, conceito 7: problema N+1") e o link de Leitura do módulo.

## Organização dos arquivos
O lembrete da seção abaixo.

## Fontes
Links da documentação oficial usados na resposta.
```

## Ao avaliar respostas de entrevista

Quando o dono mandar `entrevistas/fase-N.md` (ou respostas no chat):

1. Para cada questão: o que está correto, o que faltou e como um entrevistador veria a resposta (completa, parcial ou incompleta).
2. Se a resposta tiver um conceito errado, demonstre com um exemplo curto (um trecho de Java rodado com `java Arquivo.java` numa pasta temporária, ou o SQL que o Hibernate gera) em vez de só afirmar.
3. Depois que o dono respondeu, pode mostrar uma versão mais completa da resposta, curta o bastante para falar em voz alta.
4. Aponte o conceito do roteiro que sustenta cada questão.

## Organização dos arquivos

Sempre lembre o dono de manter o padrão abaixo.

**Fora do `clinic-billing-api`** (Fase 1, módulos 7, 8 e 13, trilha): uma pasta por módulo e um arquivo por exercício, com o número do roteiro.

```
playground/
  modulo-01/
    Ex1.java
  modulo-02/
    Ex1.java
    ...
    Desafio.java
  modulo-07/
    notas.md
sql/
  modulo-13/
    ex1.sql
    ...
    desafio.sql
entrevistas/
  fase-1.md
  ...
```

- `modulo-XX` sempre com dois dígitos.
- Exercícios que mexem no domínio da Fase 1 (`Patient`, `Charge`, testes do módulo 6) ficam no `billing-core`, no pacote `com.clinicbilling.core` e em `src/test/java`. Os que são só de linguagem ficam no `playground`.
- Respostas em texto pedidas pelo enunciado ("explique em um comentário") ficam no próprio arquivo, em comentários `/* */`, junto do código.
- Se o arquivo estiver fora do padrão (nome genérico como `Teste.java`, pasta errada, vários exercícios no mesmo arquivo), diga exatamente para onde movê-lo. Exemplo: "mova `Streams.java` para `playground/modulo-04/Ex2.java`".

**Dentro do `clinic-billing-api`** (módulo 9 em diante): o exercício é um commit, e o módulo é um branch e um pull request (apêndice E do roteiro).

- Branch por módulo ou funcionalidade, com o número: `feature/m16-charges`.
- Commits no Conventional Commits, com o módulo no escopo: `feat(m16): add charge entity`, `test(m21): cover payment boundaries`, `docs(m24): write README`.
- Um commit por exercício quando der. Isso permite pedir "corrija o commit `abc123`" e o agente achar o exercício pelo escopo.
- Decisões escritas nos desafios ficam em `clinic-billing-api/docs/` (`partial-payments.md`, `testcontainers.md`, `jwt-plan.md`).
- Migrations seguem `V<n>__<descricao>.sql`, com dois sublinhados, e **nunca** são editadas depois de rodar: mudança é migration nova.

Se o padrão já estiver certo, confirme em uma linha.

### Projeto final (módulo 30)

- Rode `SPRING_PROFILES_ACTIVE=test ./mvnw -B verify` e confira cada item da seção 5.8 de `docs/projeto-final-java-spring-boot.md`.
- Confira as regras de negócio do documento uma a uma, procurando o teste que cobre cada regra. Regra sem teste é achado 🟡.
- Leia o README como quem nunca viu o projeto: dá para rodar seguindo só ele?

## Limites para agentes

- **Não resolva exercícios** que o dono ainda não tentou, nem crie arquivos em `playground/`, `billing-core/`, `raw-http/`, `sql/`, `logica/` ou `clinic-billing-api/src/` sem pedido explícito. O aprendizado depende de o dono escrever.
- Quando pedirem ajuda com um exercício, **dê dicas e aponte o conceito do roteiro** antes de mostrar código. Na trilha de lógica, comece pela ideia e pela complexidade, não pela solução.
- **Nunca rode** `sudo`, `docker`, `docker compose down -v` (apaga o banco), `flyway clean`, `DROP`, `TRUNCATE`, nem instale pacotes (`pacman`, `yay`, `mise install`), a menos que o dono peça com essas palavras.
- **Nunca altere o banco `clinic_billing`.** Testes só com o profile `test`.
- **Git:** não faça commit, push, rebase, reset nem troque de branch sem pedido. Pode sugerir a mensagem de commit.
- Não edite migrations que já rodaram, o `compose.yaml`, o `pom.xml` (dependências e versões) nem `docs/` sem pedido.
- As senhas do `compose.yaml` e do Spring Security (`billing`, `root`, `change-me`) são de estudo local. Não as reutilize nem as copie para outros lugares.
- Módulo 28 (Playwright): os testes ficam no repositório do roteiro de Playwright. Se o dono pedir correção deles aqui, siga o `AGENTS.md` daquele projeto (testes pelo container Docker, com `--network host` para alcançar a API em `localhost:8080`).

## Estilo de escrita

- Comece com uma seção **"Resumo"** (nunca "TL;DR").
- Não use travessão nem hífen duplo no texto corrido. Prefira vírgula, ponto final ou duas frases.
- Explique o fundamento de forma atômica: o que é, por que acontece, o que muda.
- Ao explicar um erro, leia a mensagem com o dono: qual exceção, qual linha da stack trace é do código dele, o que a frase principal quer dizer. O apêndice C do roteiro lista os erros comuns.
- Termine com as fontes em links.

## Fontes de referência

- Spring Boot: https://docs.spring.io/spring-boot/
- Spring Boot, testes (pacotes do Boot 4): https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html
- Spring Framework (IoC, MVC, ProblemDetail, transações, `MockMvcTester`): https://docs.spring.io/spring-framework/reference/
- Spring Data JPA: https://docs.spring.io/spring-data/jpa/reference/
- Hibernate ORM: https://hibernate.org/orm/documentation/
- Flyway: https://documentation.red-gate.com/fd/
- Java SE 25 API: https://docs.oracle.com/en/java/javase/25/docs/api/
- JUnit: https://docs.junit.org/current/user-guide/
- Mockito: https://site.mockito.org/
- AssertJ: https://assertj.github.io/doc/
- MySQL 8.4: https://dev.mysql.com/doc/refman/8.4/en/
- MySQL, commit implícito: https://dev.mysql.com/doc/refman/8.4/en/implicit-commit.html
- RFC 9457 (Problem Details): https://www.rfc-editor.org/rfc/rfc9457
