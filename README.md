# spring-boot-lab

Meu laboratório de estudos de backend com **Java 25** e **Spring Boot 4.1**, seguindo o roteiro em [`docs/roteiro-java-spring-boot.md`](docs/roteiro-java-spring-boot.md).

## Intenção

- Sair do Java de automação de testes (Cucumber, JUnit, Page Objects) e escrever **aplicações** de backend.
- Entender o que o Spring faz por baixo antes de usá-lo: primeiro Java puro, depois HTTP sem framework, e só então o Spring.
- Terminar com um projeto de portfólio: a **API de cobranças para clínicas** (`clinic-billing-api`), num repositório próprio.

## O que é abordado

| Fase | Módulos | Tema |
|------|---------|------|
| 0 | 0 | Ambiente: JDK, Maven, IntelliJ e MySQL no Docker |
| 1 | 1 a 6 | Java para aplicações: records, BigDecimal, `java.time`, streams, exceções e JUnit |
| 2 | 7 a 12 | HTTP, servidor sem framework, injeção de dependência, API REST, validação e testes web |
| 3 | 13 a 18 | MySQL, Flyway, JPA, N+1, transações e testes de repositório |
| 4 | 19 a 22 | Regras de negócio: status derivado, pagamentos, Mockito e CPF |
| 5 | 23 a 26 | Paginação, OpenAPI, Docker e CI |
| 6 | 27 a 29 | Idempotência, testes com Playwright e Spring Security |
| 7 | 30 | Projeto final |
| Trilha | L1 a L6 | Lógica e estruturas de dados (Beecrowd e LeetCode) |

O progresso fica no checklist do apêndice G do roteiro.

## Estrutura

```
playground/modulo-XX/   exercícios curtos de linguagem (ExN.java) e respostas em texto (notas.md)
billing-core/           projeto Maven da Fase 1 (domínio em Java puro, com testes)
raw-http/               servidor HTTP sem framework (módulo 8)
sql/modulo-13/          exercícios de MySQL
entrevistas/            respostas às questões de entrevista de cada fase
logica/                 trilha de lógica
clinic-billing-api/     projeto principal, com repositório Git próprio (fora deste repositório)
```

## Comandos do dia a dia

### Ambiente

```bash
java -version          # Java 25 (Temurin, instalado pelo mise)
mvn -v                 # Maven 3, rodando no mesmo Java
mise where java        # pasta do JDK, para configurar o SDK no IntelliJ
```

### Banco (dentro de `clinic-billing-api/`)

| Situação | Comando |
|----------|---------|
| Ligar o MySQL | `sudo docker compose up -d` |
| Ver se está pronto | `sudo docker compose ps` (espere `healthy`) |
| Desligar | `sudo docker compose stop` |
| Ver os logs | `sudo docker compose logs -f mysql` |
| Abrir o cliente `mysql` | `sudo docker exec -it billing-mysql mysql -u billing -pbilling clinic_billing` |
| Resetar o banco (**apaga tudo**) | `sudo docker compose down -v` e depois `sudo docker compose up -d` |

As credenciais são só para estudo local. O banco `clinic_billing` é o de desenvolvimento, e o `clinic_billing_test` é o dos testes automatizados.

### Rodar e testar cada parte

| Parte | Comando |
|-------|---------|
| Exercício do playground | `java playground/modulo-02/Ex1.java` |
| `billing-core` | `cd billing-core && mvn -q test` |
| `raw-http` (módulo 8) | `cd raw-http && java src/com/clinicbilling/rawhttp/RawHttpServer.java` (Ctrl+C para parar) |
| Beecrowd | `java logica/beecrowd/1018/Main.java < entrada.txt` |
| API: subir | `cd clinic-billing-api && ./mvnw spring-boot:run` (porta 8080) |
| API: testes (Fase 2) | `./mvnw -q test` |
| API: testes (Fase 3 em diante) | `SPRING_PROFILES_ACTIVE=test ./mvnw -q test` |
| API: uma classe de teste | `SPRING_PROFILES_ACTIVE=test ./mvnw -q test -Dtest=PaymentServiceTest` |
| API: o que o CI roda | `SPRING_PROFILES_ACTIVE=test ./mvnw -B verify` |

A partir da Fase 3, os testes rodam **sempre** com `SPRING_PROFILES_ACTIVE=test`. Sem esse profile, o Flyway aplica as migrations no banco de desenvolvimento.

### curl

```bash
curl -i URL                                                          # GET com status e headers
curl -i -X POST URL -H "Content-Type: application/json" -d '{"name": "Ana"}'
curl -i -X DELETE URL
```

### Git

```bash
git status
git add <arquivos>
git commit -m "docs(m01): record maven exercises"   # Conventional Commits, módulo no escopo
git push
```

Fora do `clinic-billing-api`, um commit por exercício ou por módulo. Dentro dele, um branch por módulo (`feature/m16-charges`) e um pull request (apêndice E do roteiro).

## Como estudo

1. Leio os conceitos do módulo em ordem e digito os exemplos em vez de copiar.
2. Antes de rodar, escrevo o resultado esperado, como num caso de teste.
3. Quebro o código de propósito pelo menos uma vez por módulo, para ler o erro.
4. Peço a correção ao Claude Code (por exemplo, "corrija playground/modulo-03/Ex2.java"). As regras que o agente segue estão no [`AGENTS.md`](AGENTS.md).
