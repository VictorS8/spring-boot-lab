# Roteiro de Estudo: Backend com Java e Spring Boot, do Console à API

## Resumo

Este roteiro tem **8 fases e 31 módulos** (do 0 ao 30), mais uma **trilha paralela de lógica e estruturas de dados**. O formato é **híbrido**: cada módulo traz objetivo, conceitos em ordem atômica (do mais simples ao mais composto), um exemplo, 4 exercícios graduais e um desafio, como nos roteiros de SQL, TypeScript e Playwright. A diferença é que, a partir da Fase 2, **os exercícios fazem evoluir um único projeto**: a **API de cobranças para clínicas** (`clinic-billing-api`), que no fim vira o projeto de portfólio. Ao fim de cada fase há **questões estilo entrevista**. Não há gabarito: você manda as soluções no chat (ou pede ao Claude Code, apêndice F) e recebe correções e alternativas.

O código fica **em inglês** (classes, métodos, tabelas, endpoints); as explicações ficam em português. O banco é **MySQL 8.4** no Docker, o build é **Maven**, a IDE é o **IntelliJ IDEA** e a base é **Java 25** com **Spring Boot 4.1**.

| Fase | Módulos | Foco |
|------|---------|------|
| 0. Ambiente | 0 | JDK, Maven, IntelliJ e MySQL no Omarchy |
| 1. Java para aplicações | 1 a 6 | Sair do código de teste: `main`, Maven, records, BigDecimal, datas, coleções, exceções e JUnit |
| 2. HTTP e Spring | 7 a 12 | HTTP do lado de quem responde, servidor sem framework, injeção de dependência, API REST em memória, validação e testes da camada web |
| 3. Persistência com MySQL | 13 a 18 | MySQL para quem estudou PostgreSQL, Flyway, JPA, relacionamentos, transações e testes de repositório |
| 4. Regras de negócio | 19 a 22 | Status derivado, pagamentos, testes com Mockito e validação de CPF |
| 5. Entrega profissional | 23 a 26 | Paginação, documentação OpenAPI, Docker e integração contínua |
| 6. Evoluções | 27 a 29 | Idempotência, testes da API com Playwright e segurança básica |
| 7. Projeto final | 30 | A API completa no GitHub, pronta para o currículo |
| Trilha paralela | L1 a L6 | Lógica e estruturas de dados em Java, em paralelo às fases |

A ordem segue a ideia de **caminho de entendimento**: primeiro você escreve Java fora dos testes, depois vê o HTTP "cru" (sem framework), e só então deixa o Spring esconder esses detalhes. Assim, quando o Spring fizer algo "mágico", você sabe o que ele está fazendo por baixo.

> **Trilha expressa (para o teste prático chegar logo):** módulos 0 a 6 de forma rápida (você já conhece orientação a objetos), 7, 9 a 12, 13 a 17, 19 a 21 e 24 (só o README). Isso entrega o que um teste prático de backend júnior costuma cobrar: API REST com CRUD, banco relacional, validação, tratamento de erros, organização em camadas e testes. Os módulos 8, 18, 22, 23, 25 a 29 entram depois, sem pressa.

---

## Como usar este roteiro

1. **Leia os "Conceitos" em ordem.** Cada item depende do anterior.
2. **Digite os exemplos** em vez de copiar e colar. O IntelliJ completa muita coisa sozinho; mesmo assim, digitar fixa a sintaxe.
3. **Antes de rodar, preveja o resultado.** Que status HTTP vai voltar? Quantas linhas a consulta grava? Esse hábito você já tem de QA: é escrever o resultado esperado antes do teste.
4. **Um commit por exercício ou por módulo**, com mensagens em inglês (`feat: add patient endpoints`). Na Fase 2 em diante, cada módulo pode virar uma issue e um pull request (apêndice E).
5. **Quebre de propósito pelo menos uma vez por módulo.** Ler o erro do Java, do Spring e do MySQL é metade do trabalho de backend.
6. **Mande as soluções no chat** para correção. O roteiro não tem gabarito de propósito.
7. **Responda as questões de entrevista** por escrito ou em voz alta, como se explicasse para o líder técnico da vaga.

### Onde fica cada coisa

```
~/Projects/spring-boot-lab/
  playground/            exercícios curtos de linguagem, um arquivo por exercício (java Ex1.java)
    modulo-02/Ex1.java
  billing-core/          projeto Maven da Fase 1 (domínio em Java puro, com testes)
  raw-http/              servidor HTTP sem framework (módulo 8)
  clinic-billing-api/    o projeto principal, da Fase 2 até o fim (repositório no GitHub)
  logica/                trilha paralela (Beecrowd e LeetCode)
```

> **Fundamento que vale para o roteiro inteiro:** um backend é um programa que fica rodando, **recebe uma requisição HTTP**, **aplica regras de negócio**, **lê ou grava no banco** e **devolve uma resposta HTTP**. Todo módulo deste roteiro é um pedaço dessa frase. Quando se perder, volte a ela e pergunte: "em qual pedaço eu estou?".

---

# FASE 0: AMBIENTE

## Módulo 0: JDK, Maven, IntelliJ e MySQL no Omarchy

**Objetivo:** ter Java 25, Maven, IntelliJ IDEA e um MySQL 8.4 rodando no Docker, e saber o papel de cada um.

**Conceitos:**
1. **JDK (Java Development Kit)**: o kit para **desenvolver**: compilador (`javac`), a JVM e as bibliotecas padrão.
2. **JVM (Java Virtual Machine)**: o programa que **executa** o bytecode (`.class`). O mesmo `.class` roda em qualquer sistema que tenha uma JVM.
3. **LTS (Long Term Support)**: versões do Java com suporte longo. As LTS atuais são a 21 e a **25**. Versões intermediárias (26, 27) recebem atualizações só por seis meses.
4. **Distribuição**: o Java é aberto, e várias empresas empacotam o JDK. A **Eclipse Temurin** é uma das mais usadas e é a que o mise instala por padrão nas versões recentes.
5. **mise**: o gerenciador de versões de linguagens que já vem no Omarchy. Ele instala o JDK na sua pasta de usuário e troca a versão por projeto.
6. **Maven**: a ferramenta de **build**: baixa dependências, compila, roda testes e empacota. Ele lê o arquivo `pom.xml`.
7. **IDE**: o IntelliJ IDEA. Desde a versão 2025.3 ele é um produto único, gratuito para o essencial de Java, Maven e o assistente de projetos Spring Boot. Recursos avançados (como o HTTP Client embutido e as ferramentas profundas de Spring) são da assinatura Ultimate.
8. **Docker e o `sudo`**: como no roteiro de SQL, o Omarchy **não** coloca seu usuário no grupo `docker`, por segurança. Por isso os comandos usam `sudo docker`. Ferramentas que conversam com o Docker sozinhas (Testcontainers, o suporte a Docker Compose do Spring Boot) **não** funcionam nesse modo sem ajustes; o roteiro evita depender delas.

### Passo a passo

**1. Instale o Java 25 (LTS) com o mise:**

```bash
mise use --global java@temurin-25
java -version
javac -version
```

O menu do Omarchy (`Install > Development > Java`) também instala Java, mas ele roda `mise use --global java@latest`, que pega a versão mais nova, **mesmo que não seja LTS**. Para estudar e para o mercado, prefira a LTS.

**2. Instale o Maven com o mise:**

```bash
mise use --global maven@3
mvn -v
```

A saída do `mvn -v` deve mostrar o Java 25 em "Java version". Se mostrar outra versão, abra um terminal novo (o mise ajusta o `PATH` ao abrir o shell).

**3. Instale o IntelliJ IDEA.** Escolha um caminho:
- **JetBrains Toolbox** (recomendado, porque atualiza a IDE sozinho): instale pelo AUR com `yay -S jetbrains-toolbox`, abra o Toolbox e instale o "IntelliJ IDEA".
- **Arquivo `.tar.gz`** do site da JetBrains: extraia em `~/.local/share/` e rode `bin/idea`.

Sobre o Omarchy: ele usa o Hyprland, que é Wayland. A partir da versão 2026.1, as IDEs da JetBrains rodam em **Wayland nativo** por padrão, o que resolve as letras borradas e os problemas de escala das versões antigas. O Omarchy também já traz uma regra de janela para as IDEs da JetBrains (arquivo `default/hypr/apps/jetbrains.lua` do repositório). Se alguma janela de diálogo abrir em lugar estranho, é limitação conhecida do Wayland (quem posiciona a janela é o compositor, não a IDE).

**4. Aponte o IntelliJ para o JDK do mise.** Descubra onde ele está:

```bash
mise where java
```

No IntelliJ: `File > Project Structure > SDKs > + > Add JDK from disk` e escolha essa pasta.

**5. Crie a pasta de estudos e o arquivo do banco.** O `compose.yaml` vai morar dentro do projeto principal, que você cria na Fase 2. Por enquanto, crie a pasta e o arquivo para testar o MySQL:

```bash
mkdir -p ~/Projects/spring-boot-lab/clinic-billing-api/docker/mysql-init
cd ~/Projects/spring-boot-lab/clinic-billing-api
```

Crie `compose.yaml`:

```yaml
services:
  mysql:
    image: mysql:8.4
    container_name: billing-mysql
    environment:
      MYSQL_ROOT_PASSWORD: root
      MYSQL_DATABASE: clinic_billing
      MYSQL_USER: billing
      MYSQL_PASSWORD: billing
    ports:
      - "127.0.0.1:3306:3306"
    volumes:
      - mysql-data:/var/lib/mysql
      - ./docker/mysql-init:/docker-entrypoint-initdb.d:ro
    healthcheck:
      test: ["CMD", "mysqladmin", "ping", "-h", "localhost", "-uroot", "-proot"]
      interval: 5s
      timeout: 3s
      retries: 20

volumes:
  mysql-data:
```

E `docker/mysql-init/01-create-test-database.sql` (o banco separado que os testes vão usar na Fase 3):

```sql
-- Roda só na primeira vez que o volume do MySQL é criado.
CREATE DATABASE IF NOT EXISTS clinic_billing_test;
GRANT ALL PRIVILEGES ON clinic_billing_test.* TO 'billing'@'%';
```

Leitura atômica do `compose.yaml`:
- `image: mysql:8.4`: a versão LTS do MySQL. É a mesma que o menu `Install > Development > Docker DB > MySQL` do Omarchy usa.
- `environment`: na primeira subida, a imagem cria o banco `clinic_billing` e o usuário `billing` com senha `billing`. Senhas de estudo local, nunca para nada exposto.
- `ports: "127.0.0.1:3306:3306"`: publica a porta só para a sua máquina, não para a rede.
- `volumes`: `mysql-data` guarda os dados entre reinícios; a pasta `mysql-init` roda os scripts `.sql` da primeira subida.
- `healthcheck`: o Docker pergunta ao MySQL se ele está pronto. Útil no módulo 25.

**6. Suba o MySQL:**

```bash
sudo docker compose up -d
sudo docker compose ps
```

Espere o status `healthy`. Se a porta 3306 estiver ocupada, você provavelmente instalou o MySQL pelo menu do Omarchy (container `mysql8`). Pare ele com `sudo docker stop mysql8`.

**7. Conecte no banco:**

```bash
sudo docker exec -it billing-mysql mysql -u billing -pbilling clinic_billing
```

O prompt vira `mysql>`. Rode `SHOW DATABASES;` e confira que `clinic_billing` e `clinic_billing_test` aparecem. Saia com `exit`.

### Comandos do dia a dia (dentro de `~/Projects/spring-boot-lab/clinic-billing-api`)

| Situação | Comando |
|----------|---------|
| Ligar o MySQL | `sudo docker compose up -d` |
| Desligar | `sudo docker compose stop` |
| Ver os logs | `sudo docker compose logs -f mysql` |
| Abrir o cliente `mysql` | `sudo docker exec -it billing-mysql mysql -u billing -pbilling clinic_billing` |
| **Resetar o banco** (apaga tudo) | `sudo docker compose down -v` e depois `sudo docker compose up -d` |

**Exercício 0.1:** rode `java -version`, `mvn -v` e `mise where java`. Anote a versão do Java e o caminho. Por que o `mvn -v` também mostra uma versão de Java?

**Exercício 0.2:** abra o IntelliJ, crie um projeto Java vazio, confira em `Project Structure` que o SDK é o 25 do mise e rode o `main` que a IDE gera.

**Exercício 0.3:** conecte no MySQL e rode `SELECT VERSION(), CURRENT_USER(), DATABASE();`. Depois tente `USE clinic_billing_test;`. Funcionou? Qual arquivo deu essa permissão?

**Desafio:** rode `sudo docker compose down` (sem `-v`) e `up -d` de novo. Os bancos continuam lá? Agora rode `down -v` e `up -d`. Explique, com suas palavras, o papel do volume e por que o script de `mysql-init` só roda "na primeira vez".

**Leitura:** [mise: Java](https://mise.jdx.dev/lang/java.html), [IntelliJ IDEA unificado](https://www.jetbrains.com/help/idea/intellij-idea-single-distribution.html), [Imagem oficial do MySQL](https://hub.docker.com/_/mysql), [Omarchy no GitHub](https://github.com/basecamp/omarchy)

---

# FASE 1: JAVA PARA APLICAÇÕES

> Você escreve Java há anos, mas sempre **dentro** de um framework de testes: o Cucumber ou o JUnit chamavam o seu código. Nesta fase o seu código é quem manda. Você vai modelar o domínio de cobranças em **Java puro**, sem Spring, com testes. Esse código vira a base das entidades da Fase 3.
>
> Como você já conhece classes, herança e métodos, os módulos 1 a 6 podem andar rápido. O foco é no que o código de teste raramente usa: `main`, Maven por dentro, records, BigDecimal, `java.time`, streams e exceções de domínio.

## Módulo 1: Do código de teste ao programa

**Objetivo:** entender o caminho do arquivo `.java` até o programa rodando, com e sem Maven.

**Conceitos:**
1. **Compilar**: o `javac` transforma `.java` (texto) em `.class` (bytecode).
2. **Executar**: o `java` inicia a JVM e chama o método `main` da classe que você indicar.
3. **`static void main(String[] args)`**: o ponto de entrada. `static` porque a JVM chama sem criar objeto; `args` são os argumentos da linha de comando. Até o Java 24 ele precisava ser `public`; no Java 25 (JEP 512) pode ficar sem.
4. **Pacote (`package`)**: o "endereço" da classe. `com.clinicbilling.core.App` precisa estar em `com/clinicbilling/core/App.java`.
5. **Classpath**: a lista de pastas e `.jar` onde a JVM procura as classes.
6. **Execução direta de arquivo**: `java Arquivo.java` compila na memória e roda. Ótimo para exercícios curtos (a pasta `playground/`).
7. **Estrutura padrão do Maven**: `src/main/java` (código), `src/test/java` (testes), `src/main/resources` (configurações), `target/` (o que o Maven gera).
8. **`pom.xml`**: identidade do projeto (`groupId`, `artifactId`, `version`), versão do Java, dependências e plugins.
9. **Ciclo de vida do Maven**: `compile` → `test` → `package`. Rodar uma fase roda todas as anteriores. `mvn test` compila e testa; `mvn package` compila, testa e gera o `.jar`.
10. **Dependência com escopo `test`**: só existe na hora dos testes, não vai para o `.jar` final.

**Exemplo:** crie o projeto `~/Projects/spring-boot-lab/billing-core` (no IntelliJ: `New Project > Java > Build system: Maven`, ou à mão). Deixe o `pom.xml` assim:

```xml
<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0"
         xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
         xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
    <modelVersion>4.0.0</modelVersion>

    <groupId>com.clinicbilling</groupId>
    <artifactId>billing-core</artifactId>
    <version>0.1.0-SNAPSHOT</version>

    <properties>
        <maven.compiler.release>25</maven.compiler.release>
        <project.build.sourceEncoding>UTF-8</project.build.sourceEncoding>
    </properties>

    <dependencyManagement>
        <dependencies>
            <dependency>
                <groupId>org.junit</groupId>
                <artifactId>junit-bom</artifactId>
                <version>6.0.0</version>
                <type>pom</type>
                <scope>import</scope>
            </dependency>
        </dependencies>
    </dependencyManagement>

    <dependencies>
        <dependency>
            <groupId>org.junit.jupiter</groupId>
            <artifactId>junit-jupiter</artifactId>
            <scope>test</scope>
        </dependency>
    </dependencies>

    <build>
        <plugins>
            <plugin>
                <groupId>org.apache.maven.plugins</groupId>
                <artifactId>maven-surefire-plugin</artifactId>
                <version>3.5.2</version>
            </plugin>
        </plugins>
    </build>
</project>
```

Leitura atômica do que é novo:
- `maven.compiler.release`: compila para o Java 25.
- `junit-bom` em `dependencyManagement`: um **BOM** (Bill of Materials) é uma lista de versões que combinam entre si. Importando o BOM, a dependência `junit-jupiter` não precisa de versão. Se houver uma versão 6.x mais nova no [site do JUnit](https://junit.org/), pode trocar.
- `maven-surefire-plugin`: o plugin que roda os testes na fase `test`.

E `src/main/java/com/clinicbilling/core/App.java`:

```java
package com.clinicbilling.core;

public class App {

    static void main(String[] args) {
        System.out.println("Clinic billing core is running");
        System.out.println("Java " + System.getProperty("java.version"));
        for (String arg : args) {
            System.out.println("arg: " + arg);
        }
    }
}
```

Rode de três jeitos:

```bash
cd ~/Projects/spring-boot-lab/billing-core
mvn compile
java -cp target/classes com.clinicbilling.core.App --verbose

mvn package
java -cp target/billing-core-0.1.0-SNAPSHOT.jar com.clinicbilling.core.App
```

E pelo botão ▶ ao lado do `main` no IntelliJ.

**Exercícios:**
1. Na pasta `playground/modulo-01`, crie `Ex1.java` com um `main` que imprime a soma de dois números recebidos em `args`. Rode com `java Ex1.java 10 32`. O que acontece se você não passar argumentos?
2. No `billing-core`, rode `mvn compile` e explore a pasta `target/classes`. Onde está o `App.class`? Por que nessa pasta?
3. Rode `mvn package` e abra o `.jar` com `unzip -l target/*.jar`. O que tem dentro? Tem algum teste?
4. Mude a `maven.compiler.release` para `8` e rode `mvn compile`. Leia o erro (ou o aviso). Volte para `25`.

**Desafio:** tente rodar `java -jar target/billing-core-0.1.0-SNAPSHOT.jar`. Por que falha? Pesquise o que é o `Main-Class` do arquivo `MANIFEST.MF` e explique em um comentário no `pom.xml` o que faltaria para o `java -jar` funcionar. (No Spring Boot isso já vem pronto; você vai ver no módulo 25.)

**Leitura:** [Maven in 5 Minutes](https://maven.apache.org/guides/getting-started/maven-in-five-minutes.html), [Introduction to the Build Lifecycle](https://maven.apache.org/guides/introduction/introduction-to-the-lifecycle.html), [JEP 330: Launch Single-File Source-Code Programs](https://openjdk.org/jeps/330)

---

## Módulo 2: Modelando o domínio: classes, records e enums

**Objetivo:** representar paciente e status de cobrança com os tipos certos do Java moderno.

**Conceitos:**
1. **Classe**: molde com estado (campos) e comportamento (métodos). Você já usa nos Page Objects.
2. **Encapsulamento**: campos `private` e acesso por métodos. Quem usa o objeto não mexe direto no estado.
3. **Imutabilidade**: campos `final` não mudam depois do construtor. Objetos imutáveis são mais fáceis de entender e de compartilhar.
4. **Igualdade de objetos**: `==` compara se é **o mesmo objeto** na memória. `equals` compara **o conteúdo**, mas só se a classe sobrescrever `equals` (e junto o `hashCode`). Sem isso, `equals` se comporta como `==`.
5. **Record**: uma classe imutável e enxuta para **dados**. O Java gera construtor, métodos de acesso (`name()`, sem o `get`), `equals`, `hashCode` e `toString`.
6. **Construtor compacto**: em um record, um bloco `public NomeDoRecord { ... }` roda antes de atribuir os campos. Lugar ideal para validar e normalizar.
7. **Enum**: um tipo com um conjunto fechado de valores. Diferente do TypeScript (em que o `enum` dava problema no Node), em Java o `enum` é o jeito certo e pode ter **métodos**.
8. **Quando usar cada um**: record para **dados** sem identidade própria (um pedido de cadastro, uma resposta da API); classe para algo **com ciclo de vida e regras** (uma cobrança que muda de status); enum para **listas fechadas** (status, forma de pagamento).

**Exemplo** (no `billing-core`, pacote `com.clinicbilling.core`):

```java
// Patient.java
package com.clinicbilling.core;

public record Patient(String name, String cpf, String email) {

    public Patient {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("name is required");
        }
        if (cpf == null || !cpf.matches("\\d{11}")) {
            throw new IllegalArgumentException("cpf must have exactly 11 digits");
        }
        name = name.strip();
    }

    public String maskedCpf() {
        return "***." + cpf.substring(3, 6) + "." + cpf.substring(6, 9) + "-**";
    }
}
```

```java
// ChargeStatus.java
package com.clinicbilling.core;

public enum ChargeStatus {
    PENDING,
    OVERDUE,
    PAID,
    CANCELED;

    public boolean acceptsPayment() {
        return this == PENDING || this == OVERDUE;
    }
}
```

```java
// M02Demo.java
package com.clinicbilling.core;

public class M02Demo {

    static void main(String[] args) {
        Patient a = new Patient("  Ana Souza ", "12345678909", "ana@email.com");
        Patient b = new Patient("Ana Souza", "12345678909", "ana@email.com");
        System.out.println(a);
        System.out.println("records equal? " + a.equals(b));
        System.out.println("masked: " + a.maskedCpf());

        for (ChargeStatus status : ChargeStatus.values()) {
            System.out.println(status + " accepts payment? " + status.acceptsPayment());
        }

        try {
            new Patient("Bia", "123", "bia@email.com");
        } catch (IllegalArgumentException e) {
            System.out.println("error: " + e.getMessage());
        }
    }
}
```

Saída esperada (confira antes de rodar):

```
Patient[name=Ana Souza, cpf=12345678909, email=ana@email.com]
records equal? true
masked: ***.456.789-**
PENDING accepts payment? true
OVERDUE accepts payment? true
PAID accepts payment? false
CANCELED accepts payment? false
error: cpf must have exactly 11 digits
```

O CPF `12345678909` é um número fictício que passa no cálculo dos dígitos verificadores. Use sempre CPFs fictícios nos estudos, nunca o de alguém real (é dado pessoal, protegido pela LGPD).

**Exercícios:**
1. Crie uma classe comum `PatientClass` com os mesmos campos (e getters), sem `equals`. Compare duas instâncias iguais com `equals` e com `==`. Depois gere `equals` e `hashCode` pelo IntelliJ (`Alt + Insert`) e compare de novo.
2. Coloque dois pacientes iguais em um `HashSet`, primeiro com a `PatientClass` sem `equals` e `hashCode`, depois com o record. Quantos elementos o conjunto tem em cada caso? Por quê?
3. Crie o enum `PaymentMethod` com `PIX`, `BOLETO` e `CARD`, e um método `boolean isInstant()` que é verdadeiro só para o Pix.
4. Faça o record `Patient` validar o e-mail de forma simples (precisa ter um `@` e algo depois dele). Teste com um e-mail inválido.

**Desafio:** tente criar um método `setName` no record `Patient`. O que o compilador diz? Escreva em um comentário por que a imutabilidade é uma vantagem para os dados que trafegam entre camadas (pense em duas partes do código usando o mesmo objeto ao mesmo tempo).

**Leitura:** [Record Classes (Oracle)](https://docs.oracle.com/en/java/javase/25/language/records.html), [Enum Types (Java Tutorial)](https://docs.oracle.com/javase/tutorial/java/javaOO/enum.html), [Object.equals e hashCode](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/Object.html#equals(java.lang.Object))

---

## Módulo 3: Dinheiro e datas

**Objetivo:** fazer contas com dinheiro sem perder centavos e trabalhar com datas de vencimento de forma testável.

**Conceitos:**
1. **`double` é aproximado**: ele guarda números em base 2, e `0.1` não tem representação exata. `0.1 + 0.2` vira `0.30000000000000004`. Para dinheiro, isso é inaceitável. É o mesmo motivo do `numeric` no roteiro de SQL.
2. **`BigDecimal`**: número decimal **exato**. Cada valor tem um **valor sem escala** e uma **escala** (quantas casas depois da vírgula).
3. **Crie a partir de `String`**: `new BigDecimal("0.1")` é exato. `new BigDecimal(0.1)` herda o erro do `double`.
4. **`BigDecimal` é imutável**: `a.add(b)` **devolve** um novo valor; `a` não muda. Esquecer de guardar o retorno é um bug clássico.
5. **`equals` x `compareTo`**: `new BigDecimal("2.0").equals(new BigDecimal("2.00"))` é `false`, porque a escala é diferente. Para comparar **valor**, use `compareTo(...) == 0`. Para sinal, use `signum()`.
6. **Divisão precisa de regra de arredondamento**: `100 / 3` não tem fim. Informe a escala e o modo: `divide(divisor, 2, RoundingMode.HALF_EVEN)`. O `HALF_EVEN` (arredondamento bancário) reduz o viés em muitas operações.
7. **`java.time`**: `LocalDate` (só data, ideal para vencimento), `LocalDateTime` (data e hora sem fuso), `Instant` (um instante absoluto, em UTC), `ZoneId` (fuso horário).
8. **`Clock`**: a "fonte do agora". `LocalDate.now(clock)` em vez de `LocalDate.now()`. Em produção o clock é o do sistema; nos testes é um **clock fixo**. Sem isso, uma regra como "vencida se a data passou" fica impossível de testar de forma estável.
9. **Diferença entre datas**: `ChronoUnit.DAYS.between(inicio, fim)`.

**Exemplo** (`M03Demo.java`):

```java
package com.clinicbilling.core;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

public class M03Demo {

    static void main(String[] args) {
        System.out.println("double: " + (0.1 + 0.2));
        System.out.println("BigDecimal(String): " + new BigDecimal("0.1").add(new BigDecimal("0.2")));
        // Errado de propósito: mostra o erro que o double passa para o BigDecimal
        //noinspection UnpredictableBigDecimalConstructorCall
        System.out.println("BigDecimal(double): " + new BigDecimal(0.1));

        BigDecimal two = new BigDecimal("2.0");
        BigDecimal twoCents = new BigDecimal("2.00");
        System.out.println("equals: " + two.equals(twoCents));
        System.out.println("compareTo: " + two.compareTo(twoCents));

        BigDecimal third = new BigDecimal("100.00").divide(new BigDecimal("3"), 2, RoundingMode.HALF_EVEN);
        System.out.println("100 / 3 = " + third);

        LocalDate dueDate = LocalDate.of(2026, 11, 30);
        Clock clock = Clock.fixed(Instant.parse("2026-12-05T12:00:00Z"), ZoneId.of("America/Sao_Paulo"));
        LocalDate today = LocalDate.now(clock);
        System.out.println("today: " + today);
        System.out.println("overdue? " + today.isAfter(dueDate));
        System.out.println("days late: " + ChronoUnit.DAYS.between(dueDate, today));
    }
}
```

Saída:

```
double: 0.30000000000000004
BigDecimal(String): 0.3
BigDecimal(double): 0.1000000000000000055511151231257827021181583404541015625
equals: false
compareTo: 0
100 / 3 = 33.33
today: 2026-12-05
overdue? true
days late: 5
```

**Exercícios:**
1. Some 0.10 dez vezes com `double` e com `BigDecimal`. Compare os resultados.
2. Escreva `BigDecimal applyDiscount(BigDecimal amount, BigDecimal percent)` que aplica um desconto percentual e devolve com 2 casas. Teste com 199.99 e 15%.
3. Divida uma cobrança de R$ 100,00 em 3 parcelas. A soma das parcelas dá 100,00? Se não, ajuste a última parcela para fechar a conta (é o que sistemas de pagamento fazem).
4. Com um `Clock` fixo em 2026-12-31, calcule a data de vencimento de uma cobrança "para daqui a 30 dias" e "para o último dia do mês seguinte" (dica: `with(TemporalAdjusters.lastDayOfMonth())`).

**Desafio:** um `Clock` fixo em `2026-12-01T01:00:00Z` com o fuso `America/Sao_Paulo` dá qual `LocalDate`? E com o fuso `UTC`? Explique por que uma regra de vencimento precisa decidir **em qual fuso** o "hoje" é calculado, e qual fuso faz sentido para clínicas no Brasil.

**Leitura:** [BigDecimal (Javadoc)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/math/BigDecimal.html), [Date Time Trail (Java Tutorial)](https://docs.oracle.com/javase/tutorial/datetime/), [Clock (Javadoc)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/time/Clock.html)

---

## Módulo 4: Coleções, lambdas e streams

**Objetivo:** guardar, filtrar e resumir listas de cobranças, primeiro com laços e depois com streams.

**Conceitos:**
1. **`List`**: sequência ordenada, aceita repetidos. A implementação comum é o `ArrayList`.
2. **`Set`**: sem repetidos (usa `equals` e `hashCode`, por isso o exercício 2 do módulo 2). `HashSet` não garante ordem.
3. **`Map`**: chave e valor. `HashMap` não garante ordem; `LinkedHashMap` mantém a ordem de inserção; `TreeMap` ordena pela chave.
4. **Interface x implementação**: declare pelo tipo da interface (`List<Charge> charges = new ArrayList<>()`). Assim você troca a implementação sem mudar o resto.
5. **Generics**: o `<Charge>` diz o que a coleção guarda e evita conversões erradas.
6. **Lambda**: uma função curta passada como valor: `charge -> charge.getAmount()`. **Referência de método** é o atalho: `Charge::getAmount`.
7. **Stream**: um "fluxo" sobre os elementos, com operações encadeadas: `filter` (mantém), `map` (transforma), `reduce` (acumula), `collect` (junta o resultado). Não altera a lista original.
8. **`Collectors.groupingBy`**: agrupa, como o `GROUP BY` do SQL.
9. **`Optional`**: um "talvez tenha valor". Força quem chama a tratar o caso vazio (`orElse`, `orElseThrow`, `map`). É o que os repositórios do Spring Data devolvem em `findById`.

**Exemplo** (`M04Demo.java`, usando a classe `Charge` do módulo 5; se preferir, faça o módulo 5 primeiro):

```java
package com.clinicbilling.core;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class M04Demo {

    static void main(String[] args) {
        Patient ana = new Patient("Ana Souza", "12345678909", "ana@email.com");
        Patient bruno = new Patient("Bruno Lima", "98765432100", "bruno@email.com");
        LocalDate today = LocalDate.of(2026, 12, 5);

        List<Charge> charges = new ArrayList<>();
        charges.add(new Charge(1, ana, "Consulta", new BigDecimal("250.00"), LocalDate.of(2026, 11, 30)));
        charges.add(new Charge(2, ana, "Exame de sangue", new BigDecimal("80.50"), LocalDate.of(2026, 12, 10)));
        charges.add(new Charge(3, bruno, "Retorno", new BigDecimal("150.00"), LocalDate.of(2026, 12, 1)));
        charges.get(2).pay(new BigDecimal("150.00"), today);

        // Loop "na mão"
        BigDecimal openTotal = BigDecimal.ZERO;
        for (Charge charge : charges) {
            if (charge.statusOn(today).acceptsPayment()) {
                openTotal = openTotal.add(charge.getAmount());
            }
        }
        System.out.println("open total (loop): " + openTotal);

        // A mesma conta com stream
        BigDecimal openTotalStream = charges.stream()
                .filter(charge -> charge.statusOn(today).acceptsPayment())
                .map(Charge::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        System.out.println("open total (stream): " + openTotalStream);

        Map<ChargeStatus, Long> countByStatus = charges.stream()
                .collect(Collectors.groupingBy(charge -> charge.statusOn(today), Collectors.counting()));
        System.out.println("count by status: " + countByStatus);

        Map<String, BigDecimal> totalByPatient = new HashMap<>();
        for (Charge charge : charges) {
            totalByPatient.merge(charge.getPatient().name(), charge.getAmount(), BigDecimal::add);
        }
        System.out.println("total by patient: " + totalByPatient);

        Optional<Charge> firstOverdue = charges.stream()
                .filter(charge -> charge.statusOn(today) == ChargeStatus.OVERDUE)
                .findFirst();
        System.out.println("first overdue: " + firstOverdue.map(Charge::getDescription).orElse("none"));
    }
}
```

Saída (a ordem das chaves dos mapas pode variar, porque `HashMap` não garante ordem):

```
open total (loop): 330.50
open total (stream): 330.50
count by status: {OVERDUE=1, PENDING=1, PAID=1}
total by patient: {Ana Souza=330.50, Bruno Lima=150.00}
first overdue: Consulta
```

**Exercícios:**
1. Liste as descrições das cobranças em aberto (`PENDING` ou `OVERDUE`) ordenadas pelo vencimento (`sorted(Comparator.comparing(Charge::getDueDate))`).
2. Calcule o total pago por paciente usando só stream (`groupingBy` com `Collectors.reducing`).
3. Escreva `Optional<Charge> findById(List<Charge> charges, long id)` e use com um id que existe e com um que não existe. Trate o vazio com `orElseThrow`.
4. Troque o `HashMap` do exemplo por `TreeMap` e por `LinkedHashMap`. O que muda na saída?

**Desafio:** reescreva o exercício 2 com um laço `for` e compare legibilidade. Em um comentário, diga quando você prefere o laço e quando prefere o stream. (Não existe resposta única; entrevistadores gostam de ver o critério.)

**Leitura:** [Collections Trail (Java Tutorial)](https://docs.oracle.com/javase/tutorial/collections/), [java.util.stream (Javadoc)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/stream/package-summary.html), [Optional (Javadoc)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Optional.html)

---

## Módulo 5: Exceções e regras de negócio

**Objetivo:** colocar as regras da cobrança **dentro** da classe `Charge` e recusar operações inválidas com exceções próprias.

**Conceitos:**
1. **Exceção**: um objeto que interrompe o fluxo normal e sobe pela pilha de chamadas até alguém tratar.
2. **Checked** (`extends Exception`): o compilador obriga a tratar ou declarar com `throws`. Usada para falhas externas e recuperáveis (arquivo, rede).
3. **Unchecked** (`extends RuntimeException`): o compilador não obriga. É a escolha comum para **violação de regra de negócio** e para erros de programação. O Spring trabalha quase só com unchecked.
4. **Exceção de domínio**: uma classe sua, como `BusinessRuleException`, com mensagem clara. Na Fase 2 ela vira um status HTTP.
5. **Fail fast**: valide na entrada (construtor, início do método) e falhe cedo, antes de alterar o estado.
6. **Domínio rico**: a regra mora no objeto que tem os dados (`charge.pay(...)`), e não espalhada em quem usa. Assim ninguém consegue "pular" a regra mudando o status direto.
7. **Status guardado x status derivado**: a cobrança **guarda** `PENDING`, `PAID` ou `CANCELED`. O `OVERDUE` é **calculado** a partir do vencimento e de "hoje". Isso evita um processo que precise atualizar as cobranças todo dia (o módulo 19 discute as alternativas).

**Exemplo:**

```java
// BusinessRuleException.java
package com.clinicbilling.core;

public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
```

```java
// Charge.java
package com.clinicbilling.core;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public class Charge {

    private final long id;
    private final Patient patient;
    private final String description;
    private final BigDecimal amount;
    private final LocalDate dueDate;
    private ChargeStatus status = ChargeStatus.PENDING;

    public Charge(long id, Patient patient, String description, BigDecimal amount, LocalDate dueDate) {
        this.id = id;
        this.patient = Objects.requireNonNull(patient, "patient is required");
        this.description = Objects.requireNonNull(description, "description is required");
        this.dueDate = Objects.requireNonNull(dueDate, "dueDate is required");
        Objects.requireNonNull(amount, "amount is required");
        if (amount.signum() <= 0) {
            throw new BusinessRuleException("amount must be positive");
        }
        if (amount.scale() > 2) {
            throw new BusinessRuleException("amount must have at most 2 decimal places");
        }
        this.amount = amount;
    }

    public ChargeStatus statusOn(LocalDate today) {
        if (status == ChargeStatus.PENDING && today.isAfter(dueDate)) {
            return ChargeStatus.OVERDUE;
        }
        return status;
    }

    public void pay(BigDecimal paidAmount, LocalDate today) {
        if (!statusOn(today).acceptsPayment()) {
            throw new BusinessRuleException("charge " + id + " is " + status + " and cannot be paid");
        }
        if (paidAmount.compareTo(amount) != 0) {
            throw new BusinessRuleException("paid amount must be equal to " + amount);
        }
        status = ChargeStatus.PAID;
    }

    public void cancel() {
        if (status == ChargeStatus.PAID) {
            throw new BusinessRuleException("a paid charge cannot be canceled");
        }
        status = ChargeStatus.CANCELED;
    }

    public long getId() { return id; }
    public Patient getPatient() { return patient; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDueDate() { return dueDate; }
}
```

```java
// M05Demo.java
package com.clinicbilling.core;

import java.math.BigDecimal;
import java.time.LocalDate;

public class M05Demo {

    static void main(String[] args) {
        Patient ana = new Patient("Ana Souza", "12345678909", "ana@email.com");
        LocalDate today = LocalDate.of(2026, 12, 5);
        Charge charge = new Charge(1, ana, "Consulta", new BigDecimal("250.00"), LocalDate.of(2026, 11, 30));

        System.out.println("status: " + charge.statusOn(today));
        charge.cancel();
        System.out.println("status: " + charge.statusOn(today));

        try {
            charge.pay(new BigDecimal("250.00"), today);
        } catch (BusinessRuleException e) {
            System.out.println("refused: " + e.getMessage());
        }
    }
}
```

Saída:

```
status: OVERDUE
status: CANCELED
refused: charge 1 is CANCELED and cannot be paid
```

Repare no `paidAmount.compareTo(amount) != 0`: pagar `250` numa cobrança de `250.00` é aceito, porque o valor é o mesmo (módulo 3, conceito 5).

**Exercícios:**
1. Tente criar uma cobrança com valor `0`, com valor negativo e com `10.999`. Leia cada mensagem.
2. Cancelar uma cobrança já cancelada deveria ser permitido? Decida a regra, implemente e justifique em um comentário.
3. Crie `NotFoundException extends RuntimeException` e um método que busca uma cobrança por id em uma lista e lança essa exceção se não achar.
4. Use `try/catch` com dois `catch` (um para `BusinessRuleException`, outro para `IllegalArgumentException`) e veja qual é chamado em cada caso.

**Desafio:** crie a classe `Payment` (cobrança, forma de pagamento, valor, data e hora) e mova a criação do pagamento para dentro da cobrança: `Payment payment = charge.pay(method, amount, now)`. Que vantagem há em só a cobrança saber criar um pagamento?

**Leitura:** [Exceptions Trail (Java Tutorial)](https://docs.oracle.com/javase/tutorial/essential/exceptions/), [Unchecked Exceptions: the Controversy](https://docs.oracle.com/javase/tutorial/essential/exceptions/runtime.html)

---

## Módulo 6: Testes com JUnit

**Objetivo:** provar as regras da cobrança com testes automatizados, usando sua experiência de QA para escolher os casos.

**Conceitos:**
1. **Teste de unidade**: testa uma unidade (uma classe, um método) isolada, rápido e sem banco ou rede.
2. **JUnit Jupiter**: a API de testes do JUnit (`@Test`, `Assertions`). O Spring Boot 4 usa o **JUnit 6**; a API que você escreve é a mesma Jupiter que o JUnit 5 já usava.
3. **Arrange, Act, Assert**: preparar, executar, verificar. É o Given/When/Then do Gherkin com outro nome.
4. **`@BeforeEach`**: roda antes de cada teste. Cada teste recebe objetos novos, então um não interfere no outro.
5. **`assertEquals`, `assertThrows`**: verificar valor e verificar que uma exceção foi lançada (e devolver a exceção para conferir a mensagem).
6. **`@ParameterizedTest` com `@CsvSource`**: o mesmo teste com várias linhas de dados. O JUnit converte o texto para `LocalDate` e para enums sozinho.
7. **Classes de equivalência e valor limite**: em vez de testar 30 datas, teste um representante de cada classe (antes, no dia, depois do vencimento) e as fronteiras. É exatamente o que você faz como QA.
8. **Nome do teste como especificação**: o nome deve dizer a regra (`canceledChargeCannotBePaid`), não o método (`testPay`).

**Exemplo** (`src/test/java/com/clinicbilling/core/ChargeTest.java`):

```java
package com.clinicbilling.core;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ChargeTest {

    private static final LocalDate DUE_DATE = LocalDate.of(2026, 11, 30);

    private Charge charge;

    @BeforeEach
    void setUp() {
        Patient ana = new Patient("Ana Souza", "12345678909", "ana@email.com");
        charge = new Charge(1, ana, "Consulta", new BigDecimal("250.00"), DUE_DATE);
    }

    @Test
    @DisplayName("a canceled charge cannot be paid")
    void canceledChargeCannotBePaid() {
        charge.cancel();

        BusinessRuleException error = assertThrows(BusinessRuleException.class,
                () -> charge.pay(new BigDecimal("250.00"), DUE_DATE));

        assertEquals("charge 1 is CANCELED and cannot be paid", error.getMessage());
    }

    @Test
    void paidAmountIsComparedByValueNotByScale() {
        charge.pay(new BigDecimal("250"), DUE_DATE);

        assertEquals(ChargeStatus.PAID, charge.statusOn(DUE_DATE));
    }

    @ParameterizedTest(name = "on {0} the status is {1}")
    @CsvSource({
            "2026-11-29, PENDING",
            "2026-11-30, PENDING",
            "2026-12-01, OVERDUE"
    })
    void statusDependsOnTheDueDate(LocalDate today, ChargeStatus expected) {
        assertEquals(expected, charge.statusOn(today));
    }
}
```

Rode com `mvn test` ou pelo ▶ ao lado da classe no IntelliJ. Repare nas três linhas do `@CsvSource`: o dia anterior, **o próprio dia do vencimento** (fronteira) e o dia seguinte.

**Exercícios:**
1. Escreva testes para as validações do construtor da `Charge` (valor zero, negativo, com 3 casas), usando um `@ParameterizedTest` com `@ValueSource(strings = {...})`.
2. Teste que uma cobrança paga não pode ser cancelada.
3. Teste que pagar com valor diferente é recusado, incluindo a fronteira (R$ 249,99 e R$ 250,01).
4. Escreva os testes do `Patient` (nome em branco, CPF com 10 e 12 dígitos, nome com espaços sendo normalizado).

**Desafio:** escreva em Gherkin (só o texto, num arquivo `charge-rules.feature` em `src/test/resources`) os cenários das regras de pagamento e cancelamento, como você faria no trabalho. Depois compare com os testes JUnit: cada cenário virou um teste? Algum cenário ficou sem teste, ou algum teste não tem cenário?

**Leitura:** [JUnit User Guide](https://docs.junit.org/current/user-guide/), [Parameterized Tests](https://docs.junit.org/current/user-guide/#writing-tests-parameterized-tests)

---

## Questões de entrevista da Fase 1

1. Qual é a diferença entre JDK, JRE e JVM?
2. Qual é a diferença entre `==` e `equals`? Por que, ao sobrescrever `equals`, também se sobrescreve `hashCode`?
3. Por que não usar `double` para dinheiro? Como você compararia dois `BigDecimal`?
4. Quando você usaria um record e quando usaria uma classe comum?
5. Qual é a diferença entre exceção checked e unchecked? Qual você usaria para uma regra de negócio violada?
6. Qual é a diferença entre `List`, `Set` e `Map`? Quando usar `HashMap` e quando `TreeMap`?
7. Como você testaria uma regra que depende da data de hoje?
8. O que são classes de equivalência e análise de valor limite? Dê um exemplo com o vencimento de uma cobrança.

---

# FASE 2: HTTP E SPRING

> Até aqui o seu código rodava e terminava. Um backend **fica rodando** e espera pedidos. Esta fase vai do HTTP "cru" (você monta e lê cada pedaço) até uma API REST com Spring Boot, ainda guardando os dados em memória. O banco entra só na Fase 3, para você aprender uma coisa de cada vez.

## Módulo 7: HTTP do lado de quem responde

**Objetivo:** montar requisições HTTP você mesmo, com `curl`, e entender o que cada parte significa para quem **responde**.

**Conceitos:**
1. **Cliente e servidor**: o cliente (navegador, app, `curl`, Postman, outro backend) **pede**; o servidor **responde**. No Charles você via essa conversa de fora. Agora você vai ser o servidor.
2. **Requisição**: linha inicial (**método**, **caminho**, versão), **headers** e, às vezes, um **corpo**.
3. **Resposta**: linha de status (**código** e frase), headers e corpo.
4. **Métodos**: `GET` (ler), `POST` (criar ou disparar uma ação), `PUT` (substituir), `PATCH` (alterar parte), `DELETE` (remover).
5. **Seguro e idempotente**: um método **seguro** não altera nada no servidor (`GET`). Um método **idempotente** pode ser repetido sem mudar o resultado além da primeira vez (`GET`, `PUT`, `DELETE`). O `POST` não é idempotente: repetir pode criar duas cobranças. Isso volta no módulo 27.
6. **Classes de status**: `2xx` deu certo, `3xx` redirecionamento, `4xx` **o cliente errou**, `5xx` **o servidor errou**.
7. **Status mais usados em APIs**: `200 OK`, `201 Created` (com o header `Location` apontando para o recurso novo), `204 No Content`, `400 Bad Request` (entrada inválida), `404 Not Found`, `405 Method Not Allowed`, `409 Conflict` (conflito com o estado atual, como pagar uma cobrança cancelada), `415 Unsupported Media Type`, `500 Internal Server Error`.
8. **Headers importantes**: `Content-Type` (o formato do corpo **que está sendo enviado**), `Accept` (o formato que o cliente **quer receber**), `Location`, `Authorization`.
9. **REST**: um estilo de API em que as URLs representam **recursos** (substantivos no plural: `/patients`, `/patients/1/charges`) e os métodos dizem a ação. `GET /patients/1` em vez de `/getPatient?id=1`.
10. **Parâmetros**: de **caminho** (`/patients/1`, identifica o recurso) e de **consulta** (`/charges?status=PAID`, filtra, ordena, pagina).

**Exemplo** (a mesma API pública usada nos roteiros de TypeScript e Playwright):

```bash
# GET com a resposta completa (-i mostra a linha de status e os headers)
curl -i https://jsonplaceholder.typicode.com/posts/1

# Parâmetro de consulta
curl -i "https://jsonplaceholder.typicode.com/posts?userId=1"

# POST com corpo JSON: -X escolhe o método, -H adiciona header, -d envia o corpo
curl -i -X POST https://jsonplaceholder.typicode.com/posts \
  -H "Content-Type: application/json" \
  -d '{"title": "Estudando", "body": "HTTP", "userId": 1}'

# Ver também o que o curl ENVIOU (as linhas com > são a requisição)
curl -v https://jsonplaceholder.typicode.com/posts/1 -o /dev/null
```

Leitura atômica do último comando: `-v` (verbose) mostra a requisição (linhas com `>`) e a resposta (linhas com `<`); `-o /dev/null` joga o corpo fora para a saída ficar limpa.

**Exercícios:**
1. Faça um `GET` para `/posts/9999`. Qual status voltou? Ele é da classe 4xx ou 5xx, e por quê?
2. Faça um `PUT` em `/posts/1` com um corpo novo e um `DELETE` em `/posts/1`. Anote os status.
3. Repita o `POST` do exemplo **sem** o header `Content-Type`. O que mudou na resposta? Por que o servidor precisa desse header?
4. Desenhe (em texto) as rotas REST da API de cobranças: cadastrar paciente, buscar paciente, criar cobrança para um paciente, listar cobranças por status, cancelar cobrança, registrar pagamento. Para cada rota, escolha o método e o status de sucesso.

**Desafio:** explique por que "cancelar uma cobrança" é difícil de encaixar em `GET/POST/PUT/DELETE`. Compare três desenhos: `DELETE /charges/1`, `PATCH /charges/1` com `{"status": "CANCELED"}` e `POST /charges/1/cancel`. Qual você escolheria e por quê? (O roteiro usa o terceiro; não existe resposta única.)

**Leitura:** [MDN: Visão geral do HTTP](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Overview), [MDN: Métodos HTTP](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Methods), [MDN: Códigos de status](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Status), [RFC 9110, seção 9.2 (métodos seguros e idempotentes)](https://www.rfc-editor.org/rfc/rfc9110#section-9.2), [curl: tutorial](https://curl.se/docs/tutorial.html)

---

## Módulo 8: Um servidor HTTP sem framework

**Objetivo:** escrever um servidor HTTP mínimo só com o JDK, para ver tudo o que o Spring vai fazer por você.

**Conceitos:**
1. **Servidor**: um programa que **escuta** uma porta (aqui, `8000`) e responde a cada conexão.
2. **`HttpServer`**: um servidor HTTP simples que já vem no JDK (pacote `com.sun.net.httpserver`). Não é para produção, é ótimo para aprender.
3. **Contexto**: o caminho que um "handler" atende (`/hello`).
4. **Handler**: a função chamada a cada requisição. Recebe um `HttpExchange`, que dá acesso à requisição e à resposta.
5. **Ler a requisição**: método, URI (caminho e query string), headers e corpo (um fluxo de bytes).
6. **Escrever a resposta**: headers, status e tamanho (`sendResponseHeaders`), e por fim o corpo.
7. **O que fica faltando**: roteamento por método, conversão de JSON para objeto, validação, tratamento de erros, threads. É exatamente a lista do que o Spring resolve.

**Exemplo** (`~/Projects/spring-boot-lab/raw-http/src/com/clinicbilling/rawhttp/RawHttpServer.java`):

```java
package com.clinicbilling.rawhttp;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

public class RawHttpServer {

    static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/hello", RawHttpServer::hello);
        server.start();
        System.out.println("Listening on http://localhost:8000");
    }

    private static void hello(HttpExchange exchange) throws IOException {
        // 1. Tudo o que chegou: linha inicial, headers e corpo
        System.out.println(exchange.getRequestMethod() + " " + exchange.getRequestURI());
        exchange.getRequestHeaders().forEach((name, values) -> System.out.println("  " + name + ": " + values));
        String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
        if (!body.isEmpty()) {
            System.out.println("  body: " + body);
        }

        // 2. Roteamento por método: só GET é permitido aqui
        if (!exchange.getRequestMethod().equals("GET")) {
            exchange.getResponseHeaders().set("Allow", "GET");
            send(exchange, 405, "{\"error\":\"method not allowed\"}");
            return;
        }

        // 3. Query string "na mão": ?name=Ana
        String name = "world";
        String query = exchange.getRequestURI().getQuery();
        if (query != null && query.startsWith("name=")) {
            name = query.substring("name=".length());
        }
        send(exchange, 200, "{\"message\":\"Hello, " + name + "\"}");
    }

    private static void send(HttpExchange exchange, int status, String json) throws IOException {
        byte[] bytes = json.getBytes(StandardCharsets.UTF_8);
        exchange.getResponseHeaders().set("Content-Type", "application/json");
        exchange.sendResponseHeaders(status, bytes.length);
        try (OutputStream out = exchange.getResponseBody()) {
            out.write(bytes);
        }
    }
}
```

Rode e teste em outro terminal:

```bash
cd ~/Projects/spring-boot-lab/raw-http
java src/com/clinicbilling/rawhttp/RawHttpServer.java

curl -i "http://localhost:8000/hello?name=Ana"
curl -i -X POST -H "Content-Type: application/json" -d '{"name":"Ana"}' http://localhost:8000/hello
curl -i http://localhost:8000/nada
```

As respostas esperadas são `200` com `{"message":"Hello, Ana"}`, `405` com o header `Allow: GET` e `404` (este último gerado pelo próprio `HttpServer`, porque não existe contexto para `/nada`). No terminal do servidor aparecem o método, o caminho, os headers e o corpo de cada requisição. Pare o servidor com `Ctrl + C`.

**Exercícios:**
1. Adicione o contexto `/time` que devolve a data e a hora atuais em JSON.
2. Faça o `/hello` aceitar `POST` com o corpo `{"name": "Ana"}` e extrair o nome **sem biblioteca de JSON** (só com métodos de `String`). Perceba o quanto isso é frágil.
3. Chame `/hello?name=Ana%20Souza`. O nome veio com `%20`? Corrija com `URLDecoder.decode(texto, StandardCharsets.UTF_8)`.
4. Faça o servidor responder `415` quando um `POST` chegar sem `Content-Type: application/json`.

**Desafio:** faça uma lista, em um comentário no topo do arquivo, de **tudo** o que você precisaria implementar para transformar esse servidor numa API de pacientes de verdade (rotas com `{id}`, JSON, validação, erros, concorrência, banco...). Guarde a lista: no fim da Fase 2, marque o que o Spring resolveu.

**Leitura:** [HttpServer (Javadoc)](https://docs.oracle.com/en/java/javase/25/docs/api/jdk.httpserver/com/sun/net/httpserver/HttpServer.html), [MDN: Mensagens HTTP](https://developer.mozilla.org/pt-BR/docs/Web/HTTP/Messages)

---

## Módulo 9: Spring Boot por dentro: injeção de dependência e auto-configuração

**Objetivo:** criar o projeto `clinic-billing-api` e entender os conceitos que fazem o Spring funcionar, antes de escrever a API.

**Conceitos:**
1. **Framework x biblioteca**: você **chama** uma biblioteca; um framework **chama você**. É a mesma relação que o Cucumber tinha com seus steps.
2. **Inversão de controle (IoC)**: quem cria e liga os objetos é o framework, não o seu código.
3. **Bean**: um objeto criado e gerenciado pelo Spring. Por padrão existe **um** de cada (singleton).
4. **Container (ApplicationContext)**: o lugar onde os beans vivem.
5. **Estereótipos**: `@Component` marca uma classe como bean. `@Service`, `@Repository` e `@RestController` são variações com significado (camada de regra, de dados, de web).
6. **Injeção de dependência (DI)**: um bean recebe os beans de que precisa **pelo construtor**. O Spring vê o parâmetro do tipo `GreetingService` e entrega o bean. Com um único construtor, não precisa de `@Autowired`.
7. **Por que injeção pelo construtor**: os campos podem ser `final`, a dependência fica explícita e você testa a classe com `new`, sem Spring (módulo 12).
8. **`@SpringBootApplication`**: junta três coisas: `@Configuration` (a classe pode declarar beans), `@ComponentScan` (procura beans **no pacote dela e nos subpacotes**) e `@EnableAutoConfiguration`.
9. **Auto-configuração**: o Spring Boot olha o que está no classpath e configura sozinho. Viu o starter web? Sobe um Tomcat na porta 8080 e configura o Jackson para JSON.
10. **Starter**: uma dependência que traz um conjunto coerente de bibliotecas. No Boot 4 o starter web se chama `spring-boot-starter-webmvc`, e cada starter tem um par de teste (`spring-boot-starter-webmvc-test`).
11. **`application.properties`**: configurações (porta, banco, propriedades suas). `@Value("${app.greeting}")` injeta um valor de lá.
12. **Profiles**: conjuntos de configuração por ambiente. `application-test.properties` só vale quando o profile `test` está ativo (Fase 3).
13. **O caminho de uma requisição**: Tomcat recebe → `DispatcherServlet` (o "porteiro" do Spring MVC) escolhe o método do controller pelo caminho e pelo método HTTP → converte o JSON de entrada em objeto → chama seu método → converte o retorno em JSON.

### Criando o projeto

No [Spring Initializr](https://start.spring.io) (ou no IntelliJ: `New Project > Spring Boot`, que é gratuito desde a 2025.3):

| Campo | Valor |
|-------|-------|
| Project | Maven |
| Language | Java |
| Spring Boot | a versão estável 4.1.x oferecida (sem `SNAPSHOT` ou `M`) |
| Group | `com.clinicbilling` |
| Artifact | `clinic-billing-api` |
| Package name | `com.clinicbilling` |
| Packaging | Jar |
| Java | 25 |
| Dependencies | **Spring Web** e **Validation** |

Não adicione **Docker Compose Support** (ele chama o Docker sem `sudo`, conceito 8 do módulo 0) nem **Lombok** (os records já resolvem o que o Lombok resolveria para DTOs, e para aprender é melhor ver o código). JPA, MySQL e Flyway entram na Fase 3, de propósito: se você adicionar agora, a aplicação nem sobe sem banco configurado.

Extraia o projeto em `~/Projects/spring-boot-lab/clinic-billing-api` (a pasta onde já estão o `compose.yaml` e `docker/`). Abra o `pom.xml` gerado e localize:
- o `parent` `spring-boot-starter-parent`, que traz as versões de tudo (como o BOM do módulo 1);
- `spring-boot-starter-webmvc` e `spring-boot-starter-validation`;
- os starters de teste que o Initializr adicionou (como `spring-boot-starter-webmvc-test`);
- o plugin `spring-boot-maven-plugin`.

O projeto vem com o **Maven Wrapper** (`mvnw`): um script que baixa a versão certa do Maven para o projeto. Use `./mvnw` em vez de `mvn` daqui em diante.

**Exemplo:**

```java
// src/main/java/com/clinicbilling/hello/GreetingService.java
package com.clinicbilling.hello;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class GreetingService {

    private final String template;

    public GreetingService(@Value("${app.greeting:Hello, %s!}") String template) {
        this.template = template;
    }

    public String greet(String name) {
        return template.formatted(name);
    }
}
```

```java
// src/main/java/com/clinicbilling/hello/HelloController.java
package com.clinicbilling.hello;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
public class HelloController {

    private final GreetingService greetingService;

    public HelloController(GreetingService greetingService) {
        this.greetingService = greetingService;
    }

    @GetMapping("/hello")
    public Map<String, String> hello(@RequestParam(defaultValue = "world") String name) {
        return Map.of("message", greetingService.greet(name));
    }
}
```

```properties
# src/main/resources/application.properties
spring.application.name=clinic-billing-api
app.greeting=Hi, %s! Welcome to Clinic Billing.
```

```bash
./mvnw spring-boot:run
curl -i "http://localhost:8080/hello?name=Ana"
```

Compare com o módulo 8: você não leu a query string, não montou o JSON, não escreveu o `Content-Type`. O `@RequestParam` e o Jackson fizeram isso.

**Exercícios:**
1. Tire o `@Service` do `GreetingService` e suba a aplicação. Leia o erro ("required a bean of type ... that could not be found") e explique o que ele quer dizer.
2. Mova o `HelloController` para um pacote **fora** de `com.clinicbilling` (por exemplo, `com.outro`). A rota `/hello` ainda funciona? Por quê? (Conceito 8.)
3. Mude a porta para `8081` pelo `application.properties` (`server.port`) e confirme no log de inicialização.
4. Faça um `POST` para `/hello`. Qual status voltou e por quê? Compare com o `405` que você escreveu à mão no módulo 8.

**Desafio:** crie a interface `Greeter` com duas implementações (`FormalGreeter` e `CasualGreeter`, ambas `@Component`) e faça o controller depender de `Greeter`. Leia o erro de inicialização. Resolva de dois jeitos: com `@Primary` e com `@Qualifier`. Explique em um comentário o que o Spring estava tentando decidir.

**Leitura:** [Spring Framework: The IoC Container](https://docs.spring.io/spring-framework/reference/core/beans.html), [Spring Boot: Spring Beans and Dependency Injection](https://docs.spring.io/spring-boot/reference/using/spring-beans-and-dependency-injection.html), [Spring Boot: Auto-configuration](https://docs.spring.io/spring-boot/reference/using/auto-configuration.html), [Guia oficial: Building a RESTful Web Service](https://spring.io/guides/gs/rest-service)

---

## Módulo 10: API REST de pacientes em memória

**Objetivo:** construir o CRUD de pacientes em três camadas, com os dados numa coleção em memória.

**Conceitos:**
1. **Camadas**: **controller** (fala HTTP: caminhos, status, JSON), **service** (regras de negócio), **repository** (guarda e busca dados). Cada camada só conversa com a de baixo.
2. **Pacote por funcionalidade**: em vez de pastas `controllers/` e `services/`, um pacote por assunto: `com.clinicbilling.patient` com tudo de paciente dentro. Fica mais fácil achar as coisas e controlar a visibilidade.
3. **DTO (Data Transfer Object)**: o formato do que **entra** e **sai** pela API. Use **records**. Separar o DTO do modelo evita expor campos internos e permite mudar um sem quebrar o outro.
4. **Mapeamentos**: `@RequestMapping("/patients")` na classe define o prefixo; `@GetMapping`, `@PostMapping`, `@PutMapping` e `@DeleteMapping` nos métodos.
5. **Entradas**: `@PathVariable` (do caminho), `@RequestParam` (da query string, com `required = false` quando opcional), `@RequestBody` (o JSON do corpo convertido para o record).
6. **Saídas**: retornar o objeto dá `200`; `ResponseEntity.created(uri).body(...)` dá `201` com `Location`; `@ResponseStatus(HttpStatus.NO_CONTENT)` em um método `void` dá `204`.
7. **`@ResponseStatus` na exceção**: o jeito mais simples de transformar `NotFoundException` em `404`. O módulo 11 troca por um tratamento centralizado.
8. **Concorrência**: o Tomcat atende várias requisições ao mesmo tempo, em threads diferentes, usando o **mesmo** bean. Por isso o repositório em memória usa `ConcurrentHashMap` e `AtomicLong`, e não `HashMap` e `long`.
9. **Nomes iguais aos do Spring Data**: o repositório em memória usa os mesmos nomes de método que o Spring Data JPA (`save`, `findById`, `findAll`, `deleteById`, `existsByCpf`). Na Fase 3 a troca vai ser quase só a declaração.

**Exemplo** (pacote `com.clinicbilling.patient`, mais o pacote `com.clinicbilling.shared` para as exceções):

```java
// shared/NotFoundException.java
package com.clinicbilling.shared;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class NotFoundException extends RuntimeException {

    public NotFoundException(String message) {
        super(message);
    }
}
```

```java
// shared/BusinessRuleException.java
package com.clinicbilling.shared;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class BusinessRuleException extends RuntimeException {

    public BusinessRuleException(String message) {
        super(message);
    }
}
```

```java
// patient/Patient.java
package com.clinicbilling.patient;

public class Patient {

    private Long id;
    private String name;
    private final String cpf;
    private String email;

    public Patient(String name, String cpf, String email) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }

    void setId(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
}
```

O `setId` não é `public`: só classes do mesmo pacote (o repositório) podem definir o id.

```java
// patient/CreatePatientRequest.java
package com.clinicbilling.patient;

public record CreatePatientRequest(String name, String cpf, String email) {
}
```

```java
// patient/UpdatePatientRequest.java
package com.clinicbilling.patient;

public record UpdatePatientRequest(String name, String email) {
}
```

```java
// patient/PatientResponse.java
package com.clinicbilling.patient;

public record PatientResponse(Long id, String name, String cpf, String email) {

    public static PatientResponse from(Patient patient) {
        return new PatientResponse(patient.getId(), patient.getName(), patient.getCpf(), patient.getEmail());
    }
}
```

```java
// patient/InMemoryPatientRepository.java
package com.clinicbilling.patient;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Repository
public class InMemoryPatientRepository {

    private final Map<Long, Patient> patients = new ConcurrentHashMap<>();
    private final AtomicLong sequence = new AtomicLong();

    public Patient save(Patient patient) {
        if (patient.getId() == null) {
            patient.setId(sequence.incrementAndGet());
        }
        patients.put(patient.getId(), patient);
        return patient;
    }

    public Optional<Patient> findById(Long id) {
        return Optional.ofNullable(patients.get(id));
    }

    public List<Patient> findAll() {
        return new ArrayList<>(patients.values());
    }

    public boolean existsByCpf(String cpf) {
        return patients.values().stream().anyMatch(patient -> patient.getCpf().equals(cpf));
    }

    public void deleteById(Long id) {
        patients.remove(id);
    }
}
```

```java
// patient/PatientService.java
package com.clinicbilling.patient;

import com.clinicbilling.shared.BusinessRuleException;
import com.clinicbilling.shared.NotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PatientService {

    private final InMemoryPatientRepository repository;

    public PatientService(InMemoryPatientRepository repository) {
        this.repository = repository;
    }

    public Patient create(CreatePatientRequest request) {
        if (repository.existsByCpf(request.cpf())) {
            throw new BusinessRuleException("a patient with this CPF already exists");
        }
        return repository.save(new Patient(request.name(), request.cpf(), request.email()));
    }

    public Patient findById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new NotFoundException("patient " + id + " not found"));
    }

    public List<Patient> findAll() {
        return repository.findAll();
    }

    public Patient update(Long id, UpdatePatientRequest request) {
        Patient patient = findById(id);
        patient.update(request.name(), request.email());
        return repository.save(patient);
    }

    public void delete(Long id) {
        findById(id);
        repository.deleteById(id);
    }
}
```

```java
// patient/PatientController.java
package com.clinicbilling.patient;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/patients")
public class PatientController {

    private final PatientService service;

    public PatientController(PatientService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PatientResponse> create(@RequestBody CreatePatientRequest request) {
        Patient patient = service.create(request);
        URI location = URI.create("/patients/" + patient.getId());
        return ResponseEntity.created(location).body(PatientResponse.from(patient));
    }

    @GetMapping("/{id}")
    public PatientResponse findById(@PathVariable Long id) {
        return PatientResponse.from(service.findById(id));
    }

    @GetMapping
    public List<PatientResponse> findAll() {
        return service.findAll().stream().map(PatientResponse::from).toList();
    }

    @PutMapping("/{id}")
    public PatientResponse update(@PathVariable Long id, @RequestBody UpdatePatientRequest request) {
        return PatientResponse.from(service.update(id, request));
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(@PathVariable Long id) {
        service.delete(id);
    }
}
```

Teste com `curl`:

```bash
curl -i -X POST http://localhost:8080/patients \
  -H "Content-Type: application/json" \
  -d '{"name": "Ana Souza", "cpf": "12345678909", "email": "ana@email.com"}'

curl -i http://localhost:8080/patients/1
curl -i http://localhost:8080/patients
curl -i http://localhost:8080/patients/99
curl -i -X DELETE http://localhost:8080/patients/1
```

Repare: o `GET /patients/99` devolve `404`, mas o corpo é o erro genérico do Spring. Isso melhora no módulo 11. Reinicie a aplicação e liste os pacientes: sumiram, porque estavam na memória. Isso melhora na Fase 3.

**Exercícios:**
1. Cadastre o mesmo CPF duas vezes. Que status voltou e de onde ele veio?
2. Implemente o filtro `GET /patients?name=ana`, que devolve os pacientes cujo nome contém o texto, ignorando maiúsculas.
3. Faça o `PUT` em um paciente que não existe. Que status voltou? Siga a exceção desde o service até a resposta.
4. Envie um `POST` com JSON quebrado (falte uma aspa). Que status voltou? Quem gerou esse erro: seu código ou o Spring?

**Desafio:** volte à lista do desafio do módulo 8 e marque o que o Spring resolveu. O que ainda falta? (Dica: validação da entrada, formato dos erros, persistência.)

**Leitura:** [Spring MVC: Annotated Controllers](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller.html), [Spring MVC: Handler Methods (argumentos e retornos)](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-methods.html), [ConcurrentHashMap (Javadoc)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/concurrent/ConcurrentHashMap.html)

---

## Módulo 11: Validação e erros padronizados

**Objetivo:** recusar entradas inválidas com `400` e devolver todos os erros num formato padrão, o ProblemDetail.

**Conceitos:**
1. **Validar na fronteira**: a API recebe dados de fora, e dado de fora não é confiável. Valide na entrada e **também** proteja o domínio e o banco (constraints, módulo 14). São linhas de defesa diferentes.
2. **Bean Validation (Jakarta Validation)**: anotações nos campos do record: `@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@Pattern`, `@Positive`, `@FutureOrPresent`, `@Digits`.
3. **`@Valid`**: ao lado do `@RequestBody`, manda o Spring validar antes de chamar o seu método. Se falhar, o seu método **nem é chamado**, e o Spring lança `MethodArgumentNotValidException` (resultado: `400`).
4. **Mensagens**: cada anotação aceita `message = "..."`.
5. **ProblemDetail**: um formato padrão de erro em JSON (RFC 9457), com os campos `type`, `title`, `status`, `detail` e `instance`, e espaço para campos extras. O Spring já suporta nativamente.
6. **`@RestControllerAdvice`**: uma classe que trata exceções de **todos** os controllers num lugar só. Cada método `@ExceptionHandler(TipoDaExcecao.class)` cuida de um tipo.
7. **`ResponseEntityExceptionHandler`**: uma classe base do Spring que já transforma as exceções do próprio Spring MVC (JSON quebrado, método não suportado, validação) em ProblemDetail. Você estende e sobrescreve o que quiser.
8. **Não vazar detalhes**: o erro devolvido ao cliente nunca deve trazer stack trace, SQL ou nome de classe interna.

**Exemplo:** acrescente as validações aos records:

```java
// patient/CreatePatientRequest.java
package com.clinicbilling.patient;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record CreatePatientRequest(
        @NotBlank @Size(max = 120) String name,
        @NotBlank @Pattern(regexp = "\\d{11}", message = "must have exactly 11 digits") String cpf,
        @NotBlank @Email @Size(max = 160) String email) {
}
```

No controller, coloque `@Valid` antes de cada `@RequestBody` (`import jakarta.validation.Valid;`). Tire o `@ResponseStatus` das duas exceções do pacote `shared` e crie o tratamento centralizado:

```java
// shared/ApiExceptionHandler.java
package com.clinicbilling.shared;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.LinkedHashMap;
import java.util.Map;

@RestControllerAdvice
public class ApiExceptionHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler(NotFoundException.class)
    public ProblemDetail handleNotFound(NotFoundException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.NOT_FOUND, ex.getMessage());
        problem.setTitle("Resource not found");
        return problem;
    }

    @ExceptionHandler(BusinessRuleException.class)
    public ProblemDetail handleBusinessRule(BusinessRuleException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT, ex.getMessage());
        problem.setTitle("Business rule violated");
        return problem;
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        Map<String, String> errors = new LinkedHashMap<>();
        ex.getBindingResult().getFieldErrors()
                .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

        ProblemDetail problem = ex.getBody();
        problem.setTitle("Invalid request");
        problem.setProperty("errors", errors);
        return handleExceptionInternal(ex, problem, headers, status, request);
    }
}
```

```bash
curl -i -X POST http://localhost:8080/patients \
  -H "Content-Type: application/json" \
  -d '{"name": "", "cpf": "123", "email": "nao-e-email"}'
```

A resposta é `400` com `Content-Type: application/problem+json` e um corpo parecido com este (a ordem e as mensagens padrão podem variar):

```json
{
  "type": "about:blank",
  "title": "Invalid request",
  "status": 400,
  "detail": "Invalid request content.",
  "instance": "/patients",
  "errors": {
    "name": "must not be blank",
    "cpf": "must have exactly 11 digits",
    "email": "must be a well-formed email address"
  }
}
```

**Exercícios:**
1. Valide o `UpdatePatientRequest` do mesmo jeito e teste o `PUT` com dados inválidos.
2. Chame `GET /patients/99` e `POST` com CPF repetido. Confira que agora os dois voltam como ProblemDetail, com `404` e `409`.
3. Envie um JSON quebrado e um `POST` sem `Content-Type`. Que status e que corpo voltaram? Quem tratou esses casos?
4. Lance uma `RuntimeException` qualquer de propósito no service. O que o cliente recebe? Ele vê a stack trace? E o log do servidor?

**Desafio:** crie um `@ExceptionHandler(Exception.class)` que devolve `500` com uma mensagem genérica **e** registra o erro no log com `LoggerFactory.getLogger(...)` (SLF4J). Explique por que o cliente recebe uma mensagem genérica e o log recebe o detalhe.

**Leitura:** [Spring MVC: Error Responses (ProblemDetail)](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-rest-exceptions.html), [Spring MVC: Exceptions (@ExceptionHandler)](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-controller/ann-exceptionhandler.html), [Spring Boot: Validation](https://docs.spring.io/spring-boot/reference/io/validation.html), [RFC 9457: Problem Details](https://www.rfc-editor.org/rfc/rfc9457)

---

## Módulo 12: Testando a camada web

**Objetivo:** testar o service sem Spring e o controller com `@WebMvcTest`, sem subir servidor nem banco.

**Conceitos:**
1. **Pirâmide de testes**: muitos testes de unidade (rápidos), alguns de "fatia" (uma camada com o Spring), poucos de ponta a ponta. Você conhece a pirâmide do lado do QA; aqui você está na base dela.
2. **Service sem Spring**: com injeção pelo construtor, `new PatientService(new InMemoryPatientRepository())` basta. Nenhuma anotação, teste em milissegundos.
3. **Test slice `@WebMvcTest`**: sobe **só** a camada web (controllers, advices, conversão de JSON, validação). Services e repositórios **não** são criados.
4. **`@MockitoBean`**: coloca no contexto um **dublê** (mock) no lugar de um bean de verdade. Substitui o antigo `@MockBean`, que você ainda vai ver em tutoriais.
5. **Mock e stub**: com o Mockito, `given(service.findById(1L)).willReturn(...)` programa uma resposta; `then(service).should()...` verifica uma chamada.
6. **`MockMvcTester`**: dispara requisições simuladas (sem rede) e verifica com o estilo AssertJ: `assertThat(mvc.get().uri("/patients/1")).hasStatusOk()`.
7. **O que testar no controller**: o "contrato HTTP": status, headers, formato do JSON, validação e tradução de exceções em erros. A regra de negócio é testada no service.
8. **Pacotes do Boot 4**: `@WebMvcTest` vem de `org.springframework.boot.webmvc.test.autoconfigure`, e precisa do `spring-boot-starter-webmvc-test`. Tutoriais da 3.x importam de outro pacote (apêndice A).

**Exemplo:**

```java
// src/test/java/com/clinicbilling/patient/PatientServiceTest.java
package com.clinicbilling.patient;

import com.clinicbilling.shared.BusinessRuleException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class PatientServiceTest {

    private PatientService service;

    @BeforeEach
    void setUp() {
        service = new PatientService(new InMemoryPatientRepository());
    }

    @Test
    void createsPatientWithGeneratedId() {
        Patient patient = service.create(new CreatePatientRequest("Ana Souza", "12345678909", "ana@email.com"));

        assertThat(patient.getId()).isNotNull();
    }

    @Test
    void refusesDuplicatedCpf() {
        service.create(new CreatePatientRequest("Ana Souza", "12345678909", "ana@email.com"));

        assertThatThrownBy(() -> service.create(new CreatePatientRequest("Outra Ana", "12345678909", "outra@email.com")))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("CPF");
    }
}
```

```java
// src/test/java/com/clinicbilling/patient/PatientControllerTest.java
package com.clinicbilling.patient;

import com.clinicbilling.shared.NotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@WebMvcTest(PatientController.class)
class PatientControllerTest {

    @Autowired
    private MockMvcTester mvc;

    @MockitoBean
    private PatientService service;

    @Test
    void createsPatientAndReturnsLocation() {
        Patient patient = new Patient("Ana Souza", "12345678909", "ana@email.com");
        patient.setId(1L);
        given(service.create(any())).willReturn(patient);

        assertThat(mvc.post().uri("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Ana Souza", "cpf": "12345678909", "email": "ana@email.com"}
                        """))
                .hasStatus(HttpStatus.CREATED)
                .hasHeader("Location", "/patients/1")
                .bodyJson()
                .extractingPath("$.name").isEqualTo("Ana Souza");
    }

    @Test
    void rejectsInvalidCpfWithoutCallingTheService() {
        assertThat(mvc.post().uri("/patients")
                .contentType(MediaType.APPLICATION_JSON)
                .content("""
                        {"name": "Ana Souza", "cpf": "123", "email": "ana@email.com"}
                        """))
                .hasStatus(HttpStatus.BAD_REQUEST)
                .bodyJson()
                .extractingPath("$.errors.cpf").isEqualTo("must have exactly 11 digits");

        then(service).shouldHaveNoInteractions();
    }

    @Test
    void returnsProblemDetailWhenPatientDoesNotExist() {
        given(service.findById(99L)).willThrow(new NotFoundException("patient 99 not found"));

        assertThat(mvc.get().uri("/patients/99"))
                .hasStatus(HttpStatus.NOT_FOUND)
                .bodyJson()
                .extractingPath("$.detail").isEqualTo("patient 99 not found");
    }
}
```

Leitura atômica:
- O teste está no **mesmo pacote** (`com.clinicbilling.patient`) que o `Patient`, por isso consegue chamar o `setId`, que não é público.
- O texto entre `"""` é um **text block** (Java 15+): uma `String` de várias linhas, ótima para JSON.
- `then(service).shouldHaveNoInteractions()` prova que a validação barrou a requisição **antes** de chegar ao service.

Rode com `./mvnw test`.

**Exercícios:**
1. Teste o `GET /patients` com dois pacientes devolvidos pelo mock, verificando o tamanho da lista (`extractingPath("$").asArray().hasSize(2)`).
2. Teste o `DELETE` de um paciente que existe (`204`) e de um que não existe (`404`).
3. Teste o CPF duplicado no controller: o mock lança `BusinessRuleException` e a resposta deve ser `409` com o `title` certo.
4. Teste o service: atualizar um paciente que não existe lança `NotFoundException`.

**Desafio:** quebre de propósito o `ApiExceptionHandler` (comente o `@RestControllerAdvice`) e rode os testes. Quais falharam e quais continuaram passando? Explique em um comentário o que isso mostra sobre o que cada tipo de teste cobre.

**Leitura:** [Spring Boot: Testing Spring Boot Applications (Auto-configured Spring MVC Tests)](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html), [Spring Framework: MockMvcTester (AssertJ)](https://docs.spring.io/spring-framework/reference/testing/mockmvc/assertj.html), [Mockito (Javadoc)](https://javadoc.io/doc/org.mockito/mockito-core/latest/org.mockito/org/mockito/Mockito.html), [AssertJ: Core features](https://assertj.github.io/doc/#assertj-core)

---

## Questões de entrevista da Fase 2

1. O que é inversão de controle e injeção de dependência? Por que injetar pelo construtor?
2. O que faz a anotação `@SpringBootApplication`? O que acontece se um controller estiver fora do pacote da classe principal?
3. Qual é a diferença entre `@Component`, `@Service`, `@Repository` e `@RestController`?
4. Explique o caminho de uma requisição `POST /patients` desde o Tomcat até o retorno do JSON.
5. Qual é a diferença entre `PUT` e `PATCH`? E entre `POST` e `PUT` em relação à idempotência?
6. Quando você devolveria `400`, `404`, `409` e `500`? Qual é a diferença entre `4xx` e `5xx`?
7. Por que separar DTO do modelo de domínio?
8. Como você testa um controller sem subir o servidor nem o banco? O que é um mock?
9. O Tomcat atende requisições em paralelo. Que cuidado isso exige de um bean singleton que guarda estado?

---

# FASE 3: PERSISTÊNCIA COM MYSQL

> Agora os dados vão para o MySQL. Você vai escrever o **schema em SQL** (com Flyway), mapear as tabelas para classes (com JPA) e entender o que o Hibernate faz por baixo. O roteiro de SQL foi em PostgreSQL; o primeiro módulo desta fase mostra o que muda no MySQL, para você reaproveitar tudo o que já sabe.

## Módulo 13: MySQL para quem estudou PostgreSQL

**Objetivo:** conhecer as diferenças práticas entre o MySQL e o PostgreSQL que aparecem no dia a dia e em testes práticos.

**Conceitos:**
1. **O SQL é o mesmo, os dialetos mudam**: `SELECT`, `JOIN`, `GROUP BY`, CTEs e window functions funcionam quase igual (o MySQL tem CTEs e window functions desde a versão 8.0).
2. **Database e schema**: no MySQL, "database" e "schema" são sinônimos. Não existe a camada de schemas dentro de um database, como o `public` do PostgreSQL.
3. **Chave automática**: `BIGINT NOT NULL AUTO_INCREMENT` no lugar de `GENERATED ALWAYS AS IDENTITY`. O id gerado se lê com `LAST_INSERT_ID()`.
4. **Sem `RETURNING`**: o MySQL não devolve as linhas inseridas. O driver JDBC pega o id gerado por outro caminho; o Hibernate faz isso por você.
5. **Dinheiro**: `DECIMAL(10,2)`, o equivalente ao `numeric(10,2)`.
6. **Datas**: `DATE` (vencimento), `DATETIME(6)` (data e hora com microssegundos, sem fuso) e `TIMESTAMP` (guardado em UTC, mas só até o ano de 2038). Não existe `timestamptz`.
7. **Booleano**: `BOOLEAN` é um apelido de `TINYINT(1)`; `TRUE` vale `1`.
8. **Divisão**: `7 / 2` dá `3.5000` no MySQL (no PostgreSQL dá `3`). Para divisão inteira, `7 DIV 2`.
9. **Concatenação**: use `CONCAT('a', 'b')`. No MySQL, `||` é um "OU" lógico (obsoleto): `'a' || 'b'` dá `0`.
10. **Comparação de texto e collation**: a collation padrão do MySQL 8 (`utf8mb4_0900_ai_ci`) ignora maiúsculas **e acentos**. `'João' = 'joao'` é verdadeiro e `LIKE 'ana%'` acha "Ana". Não existe `ILIKE`.
11. **`CHECK`**: aplicado de verdade desde o MySQL 8.0.16.
12. **Upsert**: `INSERT ... ON DUPLICATE KEY UPDATE` no lugar de `ON CONFLICT`.
13. **InnoDB**: o motor de armazenamento padrão, com transações, chaves estrangeiras e locks por linha. O MySQL cria um índice automaticamente para cada chave estrangeira.

**Exemplo** (no cliente `mysql`, dentro do banco `clinic_billing`; use uma tabela de rascunho que você apaga no fim):

```sql
CREATE TABLE scratch_patient (
    id    BIGINT       NOT NULL AUTO_INCREMENT,
    name  VARCHAR(120) NOT NULL,
    cpf   VARCHAR(11)  NOT NULL,
    CONSTRAINT pk_scratch_patient PRIMARY KEY (id),
    CONSTRAINT uk_scratch_patient_cpf UNIQUE (cpf),
    CONSTRAINT ck_scratch_patient_cpf CHECK (cpf REGEXP '^[0-9]{11}$')
);

INSERT INTO scratch_patient (name, cpf) VALUES ('Ana Souza', '12345678909');
SELECT LAST_INSERT_ID();

INSERT INTO scratch_patient (name, cpf) VALUES ('Bruno', '123');          -- viola o CHECK
INSERT INTO scratch_patient (name, cpf) VALUES ('Ana 2', '12345678909');  -- viola o UNIQUE

INSERT INTO scratch_patient (name, cpf) VALUES ('João Pereira', '98765432100');
SELECT name FROM scratch_patient WHERE name = 'joao pereira';

SELECT 7 / 2 AS division, 7 DIV 2 AS integer_division, 'a' || 'b' AS pipes, CONCAT('a', 'b') AS concat_fn;

INSERT INTO scratch_patient (name, cpf) VALUES ('Ana Clara', '12345678909') AS new
ON DUPLICATE KEY UPDATE name = new.name;
SELECT * FROM scratch_patient;

DROP TABLE scratch_patient;
```

Resultados que você deve ver: o `CHECK` falha com `ERROR 3819 ... Check constraint 'ck_scratch_patient_cpf' is violated`; o `UNIQUE` falha com `ERROR 1062 ... Duplicate entry`; a busca por `'joao pereira'` encontra "João Pereira"; a divisão dá `3.5000` e `3`; `'a' || 'b'` dá `0`; o upsert troca o nome da Ana para "Ana Clara". Repare também que o id do João não é `2`: as tentativas que falharam "gastaram" valores do `AUTO_INCREMENT`, como as sequências do PostgreSQL.

**Exercícios:**
1. Crie uma tabela de rascunho `scratch_charge` com `amount DECIMAL(10,2)` e `due_date DATE`. Insira `80.5` e leia de volta. Que valor aparece?
2. Calcule os dias de atraso com `DATEDIFF('2026-12-05', due_date)` e formate a data como `30/11/2026` com `DATE_FORMAT(due_date, '%d/%m/%Y')`.
3. Faça a busca do exemplo distinguir acentos, com `COLLATE utf8mb4_0900_as_cs`. Explique o que significam `ai`, `as`, `ci` e `cs` no nome da collation.
4. Reescreva uma consulta com window function do roteiro de SQL (por exemplo, o ranking com `DENSE_RANK`) numa tabela de rascunho sua. Funcionou sem mudanças?

**Desafio:** a regra "nome de paciente não diferencia acento nem maiúscula" é boa para **busca**, mas pode ser ruim para uma coluna `UNIQUE` (dois e-mails que só diferem por acento seriam considerados iguais). Pesquise como definir a collation de **uma coluna** no `CREATE TABLE` e decida qual collation você usaria para `email` e para `name`. Justifique.

**Leitura:** [MySQL 8.4: Reference Manual](https://dev.mysql.com/doc/refman/8.4/en/), [CHECK constraints](https://dev.mysql.com/doc/refman/8.4/en/create-table-check-constraints.html), [INSERT ... ON DUPLICATE KEY UPDATE](https://dev.mysql.com/doc/refman/8.4/en/insert-on-duplicate.html), [Character sets e collations](https://dev.mysql.com/doc/refman/8.4/en/charset-general.html), [The DATE, DATETIME, and TIMESTAMP Types](https://dev.mysql.com/doc/refman/8.4/en/datetime.html)

---

## Módulo 14: Flyway: o schema versionado

**Objetivo:** criar as tabelas com migrations versionadas que rodam sozinhas quando a aplicação sobe.

**Conceitos:**
1. **Migration**: um arquivo SQL que leva o banco de uma versão para a próxima. O schema passa a ser **código**, com histórico no Git.
2. **Nome do arquivo**: `V1__create_patient.sql`. `V` + número da versão + **dois** sublinhados + descrição.
3. **Pasta padrão**: `src/main/resources/db/migration`.
4. **`flyway_schema_history`**: a tabela onde o Flyway anota quais migrations já rodaram, com um **checksum** de cada arquivo.
5. **Regra de ouro**: **nunca edite** uma migration que já rodou. O checksum muda e o Flyway se recusa a subir. Para mudar algo, crie uma migration nova (`V3__add_charge_version.sql`).
6. **Quem manda no schema**: o Flyway **cria**; o Hibernate só **confere** (`spring.jpa.hibernate.ddl-auto=validate`). Nunca use `update` ou `create` fora de experimentos: o Hibernate não sabe migrar dados nem dar nome às suas constraints.
7. **Dependências no Spring Boot 4**: `spring-boot-starter-flyway` e, para MySQL, `org.flywaydb:flyway-mysql`. Sem o `flyway-mysql`, o erro é `Unsupported Database: MySQL 8.4`.
8. **DataSource**: a "fonte de conexões" com o banco. O Spring Boot cria uma a partir de `spring.datasource.url`, `username` e `password`, com um pool de conexões (HikariCP).

**Exemplo:** adicione ao `pom.xml` (sem versões: o `spring-boot-starter-parent` já sabe quais usar):

```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-flyway</artifactId>
</dependency>
<dependency>
    <groupId>org.flywaydb</groupId>
    <artifactId>flyway-mysql</artifactId>
</dependency>
<dependency>
    <groupId>com.mysql</groupId>
    <artifactId>mysql-connector-j</artifactId>
    <scope>runtime</scope>
</dependency>
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa-test</artifactId>
    <scope>test</scope>
</dependency>
```

Se quiser conferir os nomes, gere um projeto de teste no Initializr com **Spring Data JPA**, **MySQL Driver** e **Flyway Migration** e compare o `pom.xml`.

Configure o `application.properties`:

```properties
spring.application.name=clinic-billing-api

spring.datasource.url=jdbc:mysql://localhost:3306/clinic_billing
spring.datasource.username=billing
spring.datasource.password=billing

spring.jpa.hibernate.ddl-auto=validate
spring.jpa.open-in-view=false
spring.jpa.show-sql=true
spring.jpa.properties.hibernate.format_sql=true
```

E crie as duas primeiras migrations:

```sql
-- src/main/resources/db/migration/V1__create_patient.sql
CREATE TABLE patient (
    id         BIGINT       NOT NULL AUTO_INCREMENT,
    name       VARCHAR(120) NOT NULL,
    cpf        VARCHAR(11)  NOT NULL,
    email      VARCHAR(160) NOT NULL,
    created_at DATETIME(6)  NOT NULL DEFAULT CURRENT_TIMESTAMP(6),
    CONSTRAINT pk_patient PRIMARY KEY (id),
    CONSTRAINT uk_patient_cpf UNIQUE (cpf),
    CONSTRAINT ck_patient_cpf_digits CHECK (cpf REGEXP '^[0-9]{11}$')
);
```

```sql
-- src/main/resources/db/migration/V2__create_charge.sql
CREATE TABLE charge (
    id          BIGINT        NOT NULL AUTO_INCREMENT,
    patient_id  BIGINT        NOT NULL,
    description VARCHAR(200)  NOT NULL,
    amount      DECIMAL(10,2) NOT NULL,
    due_date    DATE          NOT NULL,
    status      VARCHAR(20)   NOT NULL,
    CONSTRAINT pk_charge PRIMARY KEY (id),
    CONSTRAINT fk_charge_patient FOREIGN KEY (patient_id) REFERENCES patient (id),
    CONSTRAINT ck_charge_amount_positive CHECK (amount > 0),
    CONSTRAINT ck_charge_status CHECK (status IN ('PENDING', 'PAID', 'CANCELED'))
);

CREATE INDEX idx_charge_status_due_date ON charge (status, due_date);
```

Repare no `CHECK` de status: só `PENDING`, `PAID` e `CANCELED`. O `OVERDUE` nunca é gravado, porque é calculado (módulo 5, conceito 7). E o índice composto `(status, due_date)` existe para a consulta "pendentes com vencimento antes de hoje" (módulo 20 do roteiro de SQL: a ordem das colunas importa).

Suba a aplicação (com o MySQL no ar) e confira no log as linhas do Flyway (`Migrating schema ... to version "1 - create patient"`). Depois, no cliente `mysql`:

```sql
SHOW TABLES;
SELECT version, description, success FROM flyway_schema_history;
```

Atenção: com JPA no classpath e `ddl-auto=validate`, a aplicação só sobe se as **entidades** baterem com as tabelas. Como as entidades ainda não existem (módulo 15), ela sobe; o `validate` só confere as entidades que existem.

**Exercícios:**
1. Rode a aplicação duas vezes. As migrations rodaram de novo na segunda vez? Como o Flyway sabe?
2. Edite um espaço no `V1__create_patient.sql` e suba a aplicação. Leia o erro de checksum. Desfaça a edição.
3. Pare o MySQL (`sudo docker compose stop`) e suba a aplicação. Leia o erro de conexão e anote a mensagem principal. Ligue de novo.
4. Tente inserir pelo cliente `mysql` uma cobrança com status `OVERDUE`. Que linha de defesa barrou?

**Desafio:** imagine que você vai criar um `V3__seed_demo_data.sql` com pacientes e cobranças de exemplo. **Não crie** (qualquer arquivo nessa pasta roda sozinho, e o `V3` já tem dono no módulo 17). Pense: dados de demonstração deveriam ser uma migration que vai para produção? Pesquise "Flyway locations" e proponha como ter dados de exemplo **só** no ambiente local (dica: um profile `dev` com uma `spring.flyway.locations` extra). Registre a decisão no README.

**Leitura:** [Spring Boot: Database Initialization (Flyway)](https://docs.spring.io/spring-boot/how-to/data-initialization.html), [Flyway: Migrations (conceitos)](https://documentation.red-gate.com/fd/migrations-271585107.html), [Spring Boot: Data Access (DataSource)](https://docs.spring.io/spring-boot/reference/data/sql.html)

---

## Módulo 15: JPA e Spring Data: entidades e repositórios

**Objetivo:** trocar o repositório em memória por um repositório JPA ligado ao MySQL.

**Conceitos:**
1. **ORM (Object-Relational Mapping)**: uma camada que traduz objetos em linhas de tabela e vice-versa.
2. **JPA (Jakarta Persistence)**: a **especificação** (as anotações e interfaces). O **Hibernate** é a implementação que o Spring Boot usa.
3. **Entidade**: uma classe com `@Entity` que representa uma tabela. Precisa de um `@Id` e de um construtor sem argumentos (pode ser `protected`), que o Hibernate usa para criar objetos ao ler do banco.
4. **`@GeneratedValue(strategy = GenerationType.IDENTITY)`**: o id vem do `AUTO_INCREMENT`.
5. **`@Column`**: detalhes da coluna (nome, tamanho, se aceita nulo). Nomes em camelCase viram snake_case sozinhos (`dueDate` → `due_date`).
6. **Contexto de persistência**: dentro de uma transação, o Hibernate guarda as entidades carregadas. Buscar a mesma entidade duas vezes devolve o **mesmo** objeto, sem ir ao banco de novo.
7. **Dirty checking**: se você altera uma entidade carregada dentro de uma transação, o Hibernate gera o `UPDATE` sozinho no fim. Não precisa chamar `save`.
8. **Spring Data JPA**: você escreve só a **interface** `PatientRepository extends JpaRepository<Patient, Long>`, e o Spring gera a implementação com `save`, `findById`, `findAll`, `deleteById`, `count`...
9. **Consultas derivadas do nome**: `existsByCpf(String cpf)` e `findByNameContainingIgnoreCase(String name)` viram SQL pelo nome do método.
10. **`show-sql`**: imprime no log cada SQL gerado. Leia sempre: é a ponte com o seu roteiro de SQL.
11. **Entidade não sai pela API**: o controller continua devolvendo DTOs. Expor a entidade acopla o JSON às tabelas e pode vazar campos ou disparar consultas inesperadas.

**Exemplo:** transforme o `Patient` em entidade:

```java
// patient/Patient.java
package com.clinicbilling.patient;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "patient")
public class Patient {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 120)
    private String name;

    @Column(nullable = false, length = 11, unique = true)
    private String cpf;

    @Column(nullable = false, length = 160)
    private String email;

    protected Patient() {
        // usado pelo Hibernate
    }

    public Patient(String name, String cpf, String email) {
        this.name = name;
        this.cpf = cpf;
        this.email = email;
    }

    public void update(String name, String email) {
        this.name = name;
        this.email = email;
    }

    void setId(Long id) {
        this.id = id;
    }

    public Long getId() { return id; }
    public String getName() { return name; }
    public String getCpf() { return cpf; }
    public String getEmail() { return email; }
}
```

O `setId` continua lá só para os testes do controller (módulo 12); o Hibernate preenche o id direto no campo.

```java
// patient/PatientRepository.java
package com.clinicbilling.patient;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PatientRepository extends JpaRepository<Patient, Long> {

    boolean existsByCpf(String cpf);

    List<Patient> findByNameContainingIgnoreCase(String name);
}
```

No `PatientService`, troque o tipo do campo e do parâmetro do construtor de `InMemoryPatientRepository` para `PatientRepository`. Apague o `InMemoryPatientRepository`. Os métodos chamados são os mesmos (módulo 10, conceito 9).

Suba a aplicação, cadastre um paciente com `curl` e observe o SQL no log:

```
insert into patient (cpf, email, name) values (?, ?, ?)
```

Os `?` são **parâmetros**: os valores vão separados do SQL. É a mesma proteção contra SQL injection que você viu no módulo 22 do roteiro de SQL.

Reinicie a aplicação e liste os pacientes: agora eles continuam lá.

**Exercícios:**
1. Atualize um paciente com `PUT` e encontre no log o `select` e o `update`. Por que o Hibernate fez um `select` antes?
2. Tire o `save` do método `update` do service e teste de novo. O `UPDATE` ainda acontece? (Spoiler do módulo 17: depende de haver uma transação; anote o que observou e volte aqui depois.)
3. Mude o tamanho do `name` na entidade para `length = 200` e suba a aplicação. O `validate` reclamou? Pesquise o que o `validate` confere e o que ele não confere.
4. Implemente o filtro `?name=` do módulo 10 com `findByNameContainingIgnoreCase` e veja o SQL gerado. Ele usa `LIKE`? Lembre-se da collation do módulo 13.

**Desafio:** mapeie a coluna `created_at` na entidade (`LocalDateTime createdAt`) de forma que o banco preencha o valor e o Hibernate **nunca** tente gravar nela (dica: `insertable = false, updatable = false`). Devolva `createdAt` no `PatientResponse`. Depois do `POST`, o `createdAt` veio preenchido na resposta? Explique por quê (pense em quando o Hibernate lê o valor de volta).

**Leitura:** [Spring Boot: Spring Data JPA](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data), [Spring Data JPA: Query Methods](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html), [Guia oficial: Accessing data with MySQL](https://spring.io/guides/gs/accessing-data-mysql), [Hibernate ORM: Introduction](https://docs.jboss.org/hibernate/orm/7.1/introduction/html_single/Hibernate_Introduction.html)

---

## Módulo 16: Cobranças, relacionamentos e o problema N+1

**Objetivo:** criar a entidade `Charge` ligada ao paciente e enxergar (e corrigir) o problema N+1.

**Conceitos:**
1. **`@ManyToOne`**: muitas cobranças para um paciente. A chave estrangeira fica na tabela `charge` (lado "muitos"), como no roteiro de SQL.
2. **`@JoinColumn(name = "patient_id")`**: a coluna da chave estrangeira.
3. **`fetch = FetchType.LAZY`**: o paciente só é buscado quando você acessa algo dele. O padrão do `@ManyToOne` é `EAGER` (buscar sempre), que costuma trazer dados demais. Prefira `LAZY` e busque junto quando precisar.
4. **Proxy**: com `LAZY`, o campo `patient` guarda um "substituto" que vai ao banco no primeiro acesso.
5. **`LazyInitializationException`**: acessar o proxy **depois** que a transação terminou. Acontece muito ao montar o DTO fora do service.
6. **`spring.jpa.open-in-view=false`**: desliga o padrão do Spring Boot que deixa a conexão aberta até o fim da requisição. Desligado, os erros de carga preguiçosa aparecem cedo, em vez de virarem consultas escondidas na camada web.
7. **Problema N+1**: listar N cobranças (1 consulta) e, para cada uma, buscar o paciente (N consultas). Com 100 cobranças, 101 consultas. É uma das perguntas mais comuns de entrevista sobre JPA.
8. **Correção**: buscar junto, com `@EntityGraph(attributePaths = "patient")` no método do repositório ou com `join fetch` numa consulta JPQL.
9. **Enum como texto**: `@Enumerated(EnumType.STRING)` grava o nome (`PENDING`). Nunca use o padrão `ORDINAL` (grava a posição: reordenar o enum corrompe os dados).
10. **Enum e o `validate` no MySQL**: o Hibernate espera uma coluna do tipo `ENUM` do MySQL para enums. Como a sua coluna é `VARCHAR(20)`, declare isso na entidade com `columnDefinition = "varchar(20)"`. Sem isso, o erro é `Schema-validation: wrong column type encountered in column [status]`.

**Exemplo:** o `ChargeStatus` (com `OVERDUE`) e a `BusinessRuleException` já existem; a `Charge` reaproveita as regras do módulo 5.

```java
// charge/ChargeStatus.java
package com.clinicbilling.charge;

public enum ChargeStatus {
    PENDING,
    OVERDUE,
    PAID,
    CANCELED;

    public boolean acceptsPayment() {
        return this == PENDING || this == OVERDUE;
    }
}
```

```java
// charge/Charge.java
package com.clinicbilling.charge;

import com.clinicbilling.patient.Patient;
import com.clinicbilling.shared.BusinessRuleException;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

@Entity
@Table(name = "charge")
public class Charge {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patient_id")
    private Patient patient;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "due_date", nullable = false)
    private LocalDate dueDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private ChargeStatus status;

    protected Charge() {
        // usado pelo Hibernate
    }

    public Charge(Patient patient, String description, BigDecimal amount, LocalDate dueDate) {
        this.patient = Objects.requireNonNull(patient, "patient is required");
        this.description = Objects.requireNonNull(description, "description is required");
        this.dueDate = Objects.requireNonNull(dueDate, "dueDate is required");
        Objects.requireNonNull(amount, "amount is required");
        if (amount.signum() <= 0) {
            throw new BusinessRuleException("amount must be positive");
        }
        if (amount.scale() > 2) {
            throw new BusinessRuleException("amount must have at most 2 decimal places");
        }
        this.amount = amount;
        this.status = ChargeStatus.PENDING;
    }

    public ChargeStatus statusOn(LocalDate today) {
        if (status == ChargeStatus.PENDING && today.isAfter(dueDate)) {
            return ChargeStatus.OVERDUE;
        }
        return status;
    }

    public void cancel() {
        if (status == ChargeStatus.PAID) {
            throw new BusinessRuleException("a paid charge cannot be canceled");
        }
        status = ChargeStatus.CANCELED;
    }

    public Long getId() { return id; }
    public Patient getPatient() { return patient; }
    public String getDescription() { return description; }
    public BigDecimal getAmount() { return amount; }
    public LocalDate getDueDate() { return dueDate; }
}
```

O método `pay` volta no módulo 20, junto com a entidade `Payment`.

```java
// charge/ChargeRepository.java
package com.clinicbilling.charge;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface ChargeRepository extends JpaRepository<Charge, Long> {

    List<Charge> findAllByOrderByDueDateAsc();

    @EntityGraph(attributePaths = "patient")
    List<Charge> findWithPatientByOrderByDueDateAsc();

    @EntityGraph(attributePaths = "patient")
    List<Charge> findByStatusAndDueDateBefore(ChargeStatus status, LocalDate date);
}
```

Os dois primeiros métodos devolvem a mesma lista; o segundo traz o paciente junto. (O `With` no nome é ignorado pelo Spring Data: palavras entre `find` e `By` são só descrição.)

Os DTOs e o endpoint de criação (no pacote `charge`):

```java
public record CreateChargeRequest(
        @NotBlank @Size(max = 200) String description,
        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal amount,
        @NotNull LocalDate dueDate) {
}

public record ChargeResponse(Long id, Long patientId, String patientName, String description,
                             BigDecimal amount, LocalDate dueDate, ChargeStatus status) {

    public static ChargeResponse from(Charge charge, LocalDate today) {
        return new ChargeResponse(charge.getId(), charge.getPatient().getId(), charge.getPatient().getName(),
                charge.getDescription(), charge.getAmount(), charge.getDueDate(), charge.statusOn(today));
    }
}
```

O `ChargeResponse` recebe o "hoje" para mostrar `OVERDUE` quando for o caso. De onde vem esse "hoje"? De um `Clock` injetado, que o módulo 19 configura; por enquanto, crie o bean assim:

```java
// shared/ClockConfig.java
package com.clinicbilling.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Clock;
import java.time.ZoneId;

@Configuration
public class ClockConfig {

    @Bean
    public Clock clock() {
        return Clock.system(ZoneId.of("America/Sao_Paulo"));
    }
}
```

Agora escreva você o `ChargeService` e o `ChargeController` com:
- `POST /patients/{patientId}/charges` → `201` com `Location: /charges/{id}`;
- `GET /charges/{id}`;
- `GET /charges` → lista ordenada pelo vencimento;
- `POST /charges/{id}/cancel` → `200` com a cobrança atualizada.

Faça o service montar os `ChargeResponse` (ele recebe o `Clock` pelo construtor e calcula `LocalDate.now(clock)`). Coloque `@Transactional(readOnly = true)` nos métodos de leitura do service e `@Transactional` nos que alteram dados (`import org.springframework.transaction.annotation.Transactional;`). O porquê completo vem no módulo 17; o exercício 3 mostra o que acontece sem a anotação.

**Exercícios:**
1. Implemente os quatro endpoints. Cadastre 2 pacientes e 5 cobranças.
2. Faça o `GET /charges` usar `findAllByOrderByDueDateAsc` e conte no log quantos `select` aparecem. Troque por `findWithPatientByOrderByDueDateAsc` e conte de novo. Explique a diferença com o nome do problema. (Repare que as consultas extras são uma por **paciente diferente**, não uma por cobrança: o contexto de persistência, conceito 6 do módulo 15, não busca o mesmo paciente duas vezes. Com 100 pacientes diferentes, seriam 100.)
3. Tire o `@Transactional(readOnly = true)` do método que busca uma cobrança por id e chame `GET /charges/1`. Leia o erro. Por que ele acontece com `open-in-view=false`? Devolva a anotação e, agora, monte o `ChargeResponse` **no controller**, fora do service. O erro volta? Por quê?
4. Tente criar uma cobrança para um paciente que não existe. Que status deveria voltar? Implemente.

**Desafio:** apague um paciente que tem cobranças. Que exceção o Spring lança, e qual status sai hoje? Trate a `DataIntegrityViolationException` no `ApiExceptionHandler` devolvendo `409`, ou, melhor, verifique no service antes de apagar e lance uma `BusinessRuleException` com mensagem clara. Qual das duas abordagens você prefere e por quê? (Dica: pense em quem lê a mensagem de erro.)

**Leitura:** [Hibernate ORM: Associations](https://docs.jboss.org/hibernate/orm/7.1/userguide/html_single/Hibernate_User_Guide.html#associations), [Spring Data JPA: Entity Graphs](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.entity-graph), [Spring Boot: Open EntityManager in View](https://docs.spring.io/spring-boot/reference/data/sql.html#data.sql.jpa-and-spring-data.open-entity-manager-in-view), [Vlad Mihalcea: N+1 query problem](https://vladmihalcea.com/n-plus-1-query-problem/)

---

## Módulo 17: Transações e concorrência

**Objetivo:** controlar onde começa e onde termina uma transação e impedir que duas requisições simultâneas estraguem a mesma cobrança.

**Conceitos:**
1. **Transação**: o mesmo conceito do módulo 18 do roteiro de SQL: tudo ou nada (ACID).
2. **`@Transactional`**: anotado num método público de um bean, o Spring abre uma transação antes e faz `COMMIT` no fim, ou `ROLLBACK` se sair uma exceção **unchecked**.
3. **Onde colocar**: no **service**, em volta de uma operação de negócio inteira (por exemplo, "registrar pagamento" altera a cobrança **e** cria o pagamento). Não no controller e não no repositório.
4. **`@Transactional(readOnly = true)`**: para leituras. Avisa o Hibernate de que não precisa procurar alterações.
5. **Como funciona: proxy**: o Spring entrega aos outros beans um "embrulho" do seu service, que abre e fecha a transação. Por isso uma chamada **de dentro da própria classe** (`this.outroMetodo()`) **não** passa pelo embrulho e ignora o `@Transactional`.
6. **Os métodos do `JpaRepository` já são transacionais**, um por chamada. O `@Transactional` do service junta várias chamadas numa só transação, e é isso que liga o dirty checking (módulo 15, conceito 7).
7. **Concorrência**: duas pessoas cancelam e pagam a mesma cobrança ao mesmo tempo. Cada requisição lê `PENDING`, cada uma decide que pode, e a última gravação "vence". É o *lost update*.
8. **Lock otimista (`@Version`)**: uma coluna `version` que o Hibernate incrementa a cada `UPDATE`, com `WHERE id = ? AND version = ?`. Se outra transação mudou a linha antes, o `UPDATE` não afeta nenhuma linha e o Spring lança `ObjectOptimisticLockingFailureException`. A API responde `409`.
9. **Lock pessimista**: travar a linha na leitura (`SELECT ... FOR UPDATE`), como os locks que você viu nos dois terminais do roteiro de SQL. Mais pesado; o otimista é o padrão quando conflitos são raros.

**Exemplo:** uma migration nova (nunca edite a V2):

```sql
-- src/main/resources/db/migration/V3__add_charge_version.sql
ALTER TABLE charge ADD COLUMN version BIGINT NOT NULL DEFAULT 0;
```

Na entidade `Charge`:

```java
import jakarta.persistence.Version;

    @Version
    private Long version;
```

No service:

```java
@Service
public class ChargeService {

    // campos e construtor omitidos

    @Transactional
    public ChargeResponse cancel(Long chargeId) {
        Charge charge = chargeRepository.findById(chargeId)
                .orElseThrow(() -> new NotFoundException("charge " + chargeId + " not found"));
        charge.cancel();
        // sem save: o dirty checking gera o UPDATE no commit
        return ChargeResponse.from(charge, LocalDate.now(clock));
    }

    @Transactional(readOnly = true)
    public List<ChargeResponse> findAll() {
        LocalDate today = LocalDate.now(clock);
        return chargeRepository.findWithPatientByOrderByDueDateAsc().stream()
                .map(charge -> ChargeResponse.from(charge, today))
                .toList();
    }
}
```

`@Transactional` vem de `org.springframework.transaction.annotation.Transactional`. Cancele uma cobrança e procure no log um `update` parecido com este (com o `format_sql` ligado, ele aparece quebrado em várias linhas):

```
update charge set amount=?, description=?, due_date=?, patient_id=?, status=?, version=? where id=? and version=?
```

O `and version=?` é o lock otimista em ação.

E no `ApiExceptionHandler`:

```java
    @ExceptionHandler(ObjectOptimisticLockingFailureException.class)
    public ProblemDetail handleConcurrentUpdate(ObjectOptimisticLockingFailureException ex) {
        ProblemDetail problem = ProblemDetail.forStatusAndDetail(HttpStatus.CONFLICT,
                "the resource was changed by another request, reload it and try again");
        problem.setTitle("Concurrent update");
        return problem;
    }
```

(`import org.springframework.orm.ObjectOptimisticLockingFailureException;`)

**Exercícios:**
1. Volte ao exercício 2 do módulo 15: agora, com `@Transactional` no `update` do `PatientService`, o `save` é necessário?
2. Coloque `throw new RuntimeException("boom")` **depois** do `charge.cancel()` no método `cancel`. A cobrança ficou cancelada no banco? E se a exceção for checked (`throws Exception`)? Pesquise o atributo `rollbackFor`.
3. Crie um método `private` com `@Transactional` e chame de outro método da mesma classe. Use o log (`logging.level.org.springframework.transaction=DEBUG`) para ver se a transação abriu.
4. Simule o conflito: no cliente `mysql`, rode `UPDATE charge SET version = version + 1 WHERE id = 1;` **enquanto** um breakpoint no IntelliJ segura o método `cancel` logo depois do `findById`. Solte o breakpoint. Que resposta a API deu?

**Desafio:** escreva, em um comentário no `ChargeService`, a diferença entre lock otimista e pessimista, e em qual situação da API de cobranças você usaria cada um. Relacione com o exercício de dois terminais do roteiro de SQL.

**Leitura:** [Spring Framework: Declarative Transaction Management](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative.html), [Spring Framework: Using @Transactional](https://docs.spring.io/spring-framework/reference/data-access/transaction/declarative/annotations.html), [Jakarta Persistence: @Version](https://jakarta.ee/specifications/persistence/3.2/apidocs/jakarta.persistence/jakarta/persistence/version), [MySQL: InnoDB Locking](https://dev.mysql.com/doc/refman/8.4/en/innodb-locking.html)

---

## Módulo 18: Testando a persistência

**Objetivo:** testar consultas do repositório contra um MySQL de verdade, num banco separado só para testes.

**Conceitos:**
1. **Por que testar com o banco real**: as consultas derivadas, os `CHECK`, a collation e as migrations só se comportam de verdade no MySQL. Um banco em memória (H2) imita, mas não é igual.
2. **Banco de teste separado**: `clinic_billing_test`, criado pelo script do módulo 0. Os testes nunca tocam no banco que você usa no dia a dia.
3. **Profile `test`**: `src/test/resources/application-test.properties` aponta para o banco de teste (fica na pasta de testes, então não vai para o `.jar`). `@ActiveProfiles("test")` liga o profile na classe de teste.
4. **`@DataJpaTest`**: test slice que sobe só a parte de JPA (entidades, repositórios). Por padrão, cada teste roda numa transação que sofre **rollback** no fim: o banco volta limpo.
5. **`@AutoConfigureTestDatabase(replace = Replace.NONE)`**: por padrão o `@DataJpaTest` tenta trocar o banco por um embutido. `NONE` diz "use o banco configurado".
6. **Pacotes do Boot 4**: `@DataJpaTest` vem de `org.springframework.boot.data.jpa.test.autoconfigure` e o `@AutoConfigureTestDatabase` de `org.springframework.boot.jdbc.test.autoconfigure` (no Boot 3 eram outros; apêndice A).
7. **Testcontainers**: a alternativa profissional (um MySQL descartável num container por execução). No seu Omarchy ele não funciona sem o "Sudoless Docker", porque fala com o Docker sem `sudo`. Fica como evolução no módulo 28.

**Exemplo:**

```properties
# src/test/resources/application-test.properties
spring.datasource.url=jdbc:mysql://localhost:3306/clinic_billing_test
spring.jpa.show-sql=false
```

O usuário e a senha vêm do `application.properties` principal; o profile só sobrescreve o que muda.

```java
// src/test/java/com/clinicbilling/charge/ChargeRepositoryTest.java
package com.clinicbilling.charge;

import com.clinicbilling.patient.Patient;
import com.clinicbilling.patient.PatientRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ActiveProfiles("test")
class ChargeRepositoryTest {

    @Autowired
    private ChargeRepository charges;

    @Autowired
    private PatientRepository patients;

    @Test
    void findsOnlyPendingChargesBeforeTheGivenDate() {
        Patient ana = patients.save(new Patient("Ana Souza", "12345678909", "ana@email.com"));
        charges.save(new Charge(ana, "Consulta", new BigDecimal("250.00"), LocalDate.of(2026, 11, 30)));
        charges.save(new Charge(ana, "Exame", new BigDecimal("80.50"), LocalDate.of(2026, 12, 10)));

        List<Charge> overdue = charges.findByStatusAndDueDateBefore(ChargeStatus.PENDING, LocalDate.of(2026, 12, 5));

        assertThat(overdue).extracting(Charge::getDescription).containsExactly("Consulta");
    }
}
```

Rode com `./mvnw test`. Depois, no cliente `mysql`, consulte `clinic_billing_test.charge`: está vazia, por causa do rollback. E a tabela `flyway_schema_history` desse banco mostra que o Flyway criou o schema de teste.

> Se o teste falhar com "Table 'clinic_billing_test.patient' doesn't exist", o Flyway não rodou dentro do slice. Confira se o `spring-boot-starter-flyway` está no `pom.xml`; se o problema continuar, troque `@DataJpaTest` por `@SpringBootTest` + `@Transactional` (sobe a aplicação inteira, mais lento, mas com o Flyway garantido) e anote o que descobriu. O apêndice C tem esse erro.

**Exercícios:**
1. Teste o `existsByCpf` com um CPF que existe e com um que não existe.
2. Teste que salvar um segundo paciente com o mesmo CPF falha (`assertThatThrownBy(...)` com `patients.saveAndFlush(...)`). Por que o `saveAndFlush` e não o `save`?
3. Teste o `@EntityGraph`: carregue as cobranças com `findWithPatientByOrderByDueDateAsc` e verifique que o paciente já veio carregado (`Hibernate.isInitialized(charge.getPatient())`).
4. O teste `contextLoads` gerado pelo Initializr agora sobe a aplicação inteira, que precisa de banco. Ele usa o banco de dev ou o de teste? Faça ele usar o de teste.

**Desafio:** escreva um teste que prova o lock otimista: carregue a mesma cobrança em **duas** variáveis com `EntityManager`s diferentes (ou use `TestEntityManager` e `detach`), altere e grave as duas, e verifique que a segunda gravação lança exceção. Se ficar difícil, escreva no teste, como comentário, o roteiro manual do exercício 4 do módulo 17. (Testar concorrência é difícil até para profissionais; tentar já ensina muito.)

**Leitura:** [Spring Boot: Auto-configured Data JPA Tests](https://docs.spring.io/spring-boot/reference/testing/spring-boot-applications.html), [Spring Boot: Profiles](https://docs.spring.io/spring-boot/reference/features/profiles.html), [Spring Framework: Testing transactions](https://docs.spring.io/spring-framework/reference/testing/testcontext-framework/tx.html)

---

## Questões de entrevista da Fase 3

1. Qual é a diferença entre JPA, Hibernate e Spring Data JPA?
2. O que é o problema N+1? Como você o identifica e como resolve?
3. Qual é a diferença entre `FetchType.LAZY` e `EAGER`? O que causa uma `LazyInitializationException`?
4. Para que serve o Flyway? O que acontece se alguém editar uma migration que já rodou em produção?
5. Por que não usar `ddl-auto=update` em produção?
6. O que faz o `@Transactional`? Por que uma chamada interna (`this.metodo()`) ignora a anotação?
7. O que é dirty checking?
8. Explique lock otimista e pessimista. Como o `@Version` funciona?
9. Por que gravar enum como `STRING` e não como `ORDINAL`?
10. Cite três diferenças entre MySQL e PostgreSQL que podem pegar alguém de surpresa.

---

# FASE 4: REGRAS DE NEGÓCIO

> As fases anteriores montaram a estrutura. Esta fase é o **coração** do projeto e o que mais aparece numa conversa com o líder técnico: regras de status, pagamento, testes com dublês e validação de CPF. É também onde a sua experiência de QA mais conta.

## Módulo 19: Status derivado, `Clock` e filtros

**Objetivo:** listar cobranças por status, incluindo `OVERDUE`, que não está gravado no banco, de forma testável.

**Conceitos:**
1. **Três desenhos para "vencida"**:
   - **(a) Calcular na leitura** (o escolhido): grava `PENDING`; quem lê compara o vencimento com "hoje". Nunca fica desatualizado e não precisa de processo agendado.
   - **(b) Job agendado** (`@Scheduled`): todo dia, um processo troca `PENDING` vencidas para `OVERDUE` no banco. Facilita relatórios por SQL, mas fica errado entre uma execução e outra e cria um processo a mais para monitorar.
   - **(c) View no banco**: uma `VIEW` com um `CASE` que calcula o status (como no módulo 19 do roteiro de SQL). Bom para relatórios, mas espalha a regra entre Java e SQL.
2. **Regra num lugar só**: com o desenho (a), a regra "vencida = pendente e hoje depois do vencimento" mora no `Charge.statusOn`. A consulta do repositório repete a mesma ideia em forma de filtro (`status = PENDING and dueDate < today`), e um teste garante que as duas concordam.
3. **`Clock` como bean**: o `ClockConfig` do módulo 16 fornece o "agora" da aplicação. Nos testes, você passa um `Clock.fixed(...)`. Nenhuma classe chama `LocalDate.now()` sem clock.
4. **Fuso da regra**: o clock usa `America/Sao_Paulo` (o desafio do módulo 3). Uma cobrança que vence "hoje" não pode virar vencida às 21h por causa do UTC.
5. **Enum como parâmetro**: `@RequestParam(required = false) ChargeStatus status` converte `?status=OVERDUE` sozinho. Um valor inválido vira `400` pelo `ResponseEntityExceptionHandler`.
6. **`switch` com enum**: uma expressão `switch` sobre um enum precisa cobrir todos os valores; se você adicionar um status novo, o compilador aponta onde falta tratar. É o mesmo princípio da verificação exaustiva do roteiro de TypeScript (módulo 9).

**Exemplo:** no `ChargeRepository`, acrescente:

```java
    @EntityGraph(attributePaths = "patient")
    List<Charge> findByStatusAndDueDateGreaterThanEqual(ChargeStatus status, LocalDate date);

    @EntityGraph(attributePaths = "patient")
    List<Charge> findByStatus(ChargeStatus status);
```

No `ChargeService`:

```java
    @Transactional(readOnly = true)
    public List<ChargeResponse> findByStatus(ChargeStatus status) {
        LocalDate today = LocalDate.now(clock);
        List<Charge> charges = switch (status) {
            case OVERDUE -> chargeRepository.findByStatusAndDueDateBefore(ChargeStatus.PENDING, today);
            case PENDING -> chargeRepository.findByStatusAndDueDateGreaterThanEqual(ChargeStatus.PENDING, today);
            case PAID, CANCELED -> chargeRepository.findByStatus(status);
        };
        return charges.stream()
                .map(charge -> ChargeResponse.from(charge, today))
                .toList();
    }
```

No `ChargeController`:

```java
    @GetMapping
    public List<ChargeResponse> findAll(@RequestParam(required = false) ChargeStatus status) {
        if (status == null) {
            return service.findAll();
        }
        return service.findByStatus(status);
    }
```

```bash
curl -i "http://localhost:8080/charges?status=OVERDUE"
curl -i "http://localhost:8080/charges?status=VENCIDA"
```

**Exercícios:**
1. Cadastre cobranças com vencimento ontem, hoje e amanhã. Confira o resultado de cada filtro. A de hoje aparece como `PENDING` ou `OVERDUE`?
2. Acrescente o filtro `patientId` (`GET /charges?patientId=1&status=PENDING`). Crie os métodos de repositório que precisar.
3. Escreva um teste de repositório (módulo 18) que cria cobranças vencidas e não vencidas e confere que `findByStatusAndDueDateBefore` concorda com `Charge.statusOn` para a mesma data.
4. Troque o `ZoneId` do `ClockConfig` para `UTC`, crie uma cobrança vencendo hoje e consulte à noite (ou use um `Clock.fixed` num teste). Volte para `America/Sao_Paulo`.

**Desafio:** implemente o desenho (b) num branch separado: um `@Scheduled(cron = "0 5 0 * * *")` que marca as vencidas (habilitado com `@EnableScheduling`). Para isso o banco precisa aceitar `OVERDUE` (nova migration alterando o `CHECK`). Compare os dois desenhos em um parágrafo no README, pensando em: consistência, relatórios em SQL, testes e operação. Depois decida qual fica na `main`.

**Leitura:** [Spring Framework: Task Execution and Scheduling](https://docs.spring.io/spring-framework/reference/integration/scheduling.html), [Spring Data JPA: Query Creation (palavras-chave)](https://docs.spring.io/spring-data/jpa/reference/jpa/query-methods.html#jpa.query-methods.query-creation), [JEP 361: Switch Expressions](https://openjdk.org/jeps/361)

---

## Módulo 20: Pagamentos

**Objetivo:** registrar o pagamento de uma cobrança respeitando as regras, numa única transação.

**Conceitos:**
1. **Regras de pagamento deste projeto**: só cobranças `PENDING` ou `OVERDUE` aceitam pagamento; o valor pago deve ser **igual** ao da cobrança (sem pagamento parcial); cada cobrança tem no máximo **um** pagamento; formas aceitas: `PIX`, `BOLETO`, `CARD`.
2. **Dados de cartão**: `CARD` só registra **que** foi cartão. A aplicação **nunca** recebe nem guarda número de cartão, validade ou CVV. Quem lida com isso são as adquirentes e os gateways certificados (é a razão de existir a PCI DSS, citada na vaga).
3. **Agregado**: `Charge` e `Payment` mudam juntos. Quem cria o pagamento é a cobrança (`charge.pay(...)`), e o service só orquestra: busca, chama a regra, salva.
4. **`@OneToOne`**: um pagamento para uma cobrança. A chave estrangeira fica em `payment.charge_id`, com `UNIQUE` no banco como segunda linha de defesa.
5. **Uma transação, duas tabelas**: o `UPDATE` da cobrança (dirty checking) e o `INSERT` do pagamento acontecem no mesmo commit. Se o `INSERT` falhar, a cobrança não fica `PAID`.
6. **Sub-recurso**: `POST /charges/{id}/payments` cria o pagamento **da** cobrança. A resposta é `201` com `Location: /charges/{id}/payment`.

**Exemplo:** a migration (de novo: arquivo novo, nunca editar os antigos):

```sql
-- src/main/resources/db/migration/V4__create_payment.sql
CREATE TABLE payment (
    id        BIGINT        NOT NULL AUTO_INCREMENT,
    charge_id BIGINT        NOT NULL,
    method    VARCHAR(20)   NOT NULL,
    amount    DECIMAL(10,2) NOT NULL,
    paid_at   DATETIME(6)   NOT NULL,
    CONSTRAINT pk_payment PRIMARY KEY (id),
    CONSTRAINT fk_payment_charge FOREIGN KEY (charge_id) REFERENCES charge (id),
    CONSTRAINT uk_payment_charge UNIQUE (charge_id),
    CONSTRAINT ck_payment_amount_positive CHECK (amount > 0),
    CONSTRAINT ck_payment_method CHECK (method IN ('PIX', 'BOLETO', 'CARD'))
);
```

No pacote `charge` (o pagamento faz parte do agregado da cobrança):

```java
// charge/PaymentMethod.java
package com.clinicbilling.charge;

public enum PaymentMethod {
    PIX,
    BOLETO,
    CARD
}
```

```java
// charge/Payment.java
package com.clinicbilling.charge;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "payment")
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "charge_id", unique = true)
    private Charge charge;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "varchar(20)")
    private PaymentMethod method;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @Column(name = "paid_at", nullable = false)
    private LocalDateTime paidAt;

    protected Payment() {
        // usado pelo Hibernate
    }

    Payment(Charge charge, PaymentMethod method, BigDecimal amount, LocalDateTime paidAt) {
        this.charge = charge;
        this.method = method;
        this.amount = amount;
        this.paidAt = paidAt;
    }

    public Long getId() { return id; }
    public Charge getCharge() { return charge; }
    public PaymentMethod getMethod() { return method; }
    public BigDecimal getAmount() { return amount; }
    public LocalDateTime getPaidAt() { return paidAt; }
}
```

O construtor não é `public`: fora do pacote `charge`, ninguém cria um pagamento sem passar pela cobrança.

Na `Charge`, o método `pay` volta, agora criando o pagamento:

```java
    public Payment pay(PaymentMethod method, BigDecimal paidAmount, LocalDateTime paidAt) {
        if (!statusOn(paidAt.toLocalDate()).acceptsPayment()) {
            throw new BusinessRuleException("charge " + id + " is " + status + " and cannot be paid");
        }
        if (paidAmount.compareTo(amount) != 0) {
            throw new BusinessRuleException("paid amount must be equal to " + amount);
        }
        status = ChargeStatus.PAID;
        return new Payment(this, method, amount, paidAt);
    }
```

O pagamento guarda o `amount` da própria cobrança (que já tem 2 casas), e não o `paidAmount` recebido: os dois têm o mesmo valor, mas a escala da cobrança é a confiável.

```java
// charge/PaymentRepository.java
package com.clinicbilling.charge;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
```

```java
// charge/RegisterPaymentRequest.java
package com.clinicbilling.charge;

import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

import java.math.BigDecimal;

public record RegisterPaymentRequest(
        @NotNull PaymentMethod method,
        @NotNull @Positive @Digits(integer = 8, fraction = 2) BigDecimal amount) {
}
```

```java
// charge/PaymentResponse.java
package com.clinicbilling.charge;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record PaymentResponse(Long id, Long chargeId, PaymentMethod method, BigDecimal amount, LocalDateTime paidAt) {

    public static PaymentResponse from(Payment payment) {
        return new PaymentResponse(payment.getId(), payment.getCharge().getId(), payment.getMethod(),
                payment.getAmount(), payment.getPaidAt());
    }
}
```

```java
// charge/PaymentService.java
package com.clinicbilling.charge;

import com.clinicbilling.shared.NotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Clock;
import java.time.LocalDateTime;

@Service
public class PaymentService {

    private final ChargeRepository chargeRepository;
    private final PaymentRepository paymentRepository;
    private final Clock clock;

    public PaymentService(ChargeRepository chargeRepository, PaymentRepository paymentRepository, Clock clock) {
        this.chargeRepository = chargeRepository;
        this.paymentRepository = paymentRepository;
        this.clock = clock;
    }

    @Transactional
    public PaymentResponse register(Long chargeId, RegisterPaymentRequest request) {
        Charge charge = chargeRepository.findById(chargeId)
                .orElseThrow(() -> new NotFoundException("charge " + chargeId + " not found"));
        Payment payment = charge.pay(request.method(), request.amount(), LocalDateTime.now(clock));
        return PaymentResponse.from(paymentRepository.save(payment));
    }
}
```

O endpoint fica com você: `POST /charges/{id}/payments`, `@Valid`, `201` e `Location: /charges/{id}/payment`.

```bash
curl -i -X POST http://localhost:8080/charges/1/payments \
  -H "Content-Type: application/json" \
  -d '{"method": "PIX", "amount": 250.00}'
```

Observe no log o `select` da cobrança, o `insert` do pagamento e o `update` da cobrança, todos antes de um único commit.

**Exercícios:**
1. Implemente o endpoint e pague uma cobrança. Tente pagar de novo: que status voltou e qual linha de defesa barrou (a regra do domínio ou o `UNIQUE` do banco)?
2. Tente pagar uma cobrança cancelada, com valor diferente e com `"method": "DINHEIRO"`. Anote status e corpo de cada resposta.
3. Implemente `GET /charges/{id}/payment`, que devolve o pagamento ou `404`.
4. Mostre que a transação funciona: lance uma exceção no `register` **depois** do `paymentRepository.save` e confira no banco que a cobrança **não** ficou `PAID` nem o pagamento foi gravado.

**Desafio:** mude a regra para aceitar pagamento **parcial** (vários pagamentos até completar o valor). O que muda no banco (a `UNIQUE`), na entidade (um `@OneToMany`?), no cálculo do status e na API? Não precisa implementar: escreva o plano num arquivo `docs/partial-payments.md`, como se fosse propor a mudança para o time.

**Leitura:** [Hibernate ORM: OneToOne](https://docs.jboss.org/hibernate/orm/7.1/userguide/html_single/Hibernate_User_Guide.html#associations-one-to-one), [PCI Security Standards Council](https://www.pcisecuritystandards.org/), [Martin Fowler: DDD Aggregate](https://martinfowler.com/bliki/DDD_Aggregate.html)

---

## Módulo 21: Testes de unidade com Mockito e casos de borda

**Objetivo:** testar o `PaymentService` e as regras da `Charge` sem banco, escolhendo os casos como um QA.

**Conceitos:**
1. **Dublê (test double)**: um objeto no lugar do real. **Stub** devolve respostas programadas; **mock** também verifica se foi chamado. O Mockito faz os dois.
2. **`@ExtendWith(MockitoExtension.class)`**: liga o Mockito no JUnit. `@Mock` cria o dublê. Sem Spring: o teste é só Java.
3. **O que mockar**: as **fronteiras** (repositórios, clientes HTTP, o relógio). **Não** mocke entidades e objetos de valor: use os reais, porque são eles que têm as regras.
4. **`Clock.fixed`**: o "agora" do teste. Com ele, "vencida" e "no dia" são situações reproduzíveis.
5. **`willAnswer`**: uma resposta calculada, como "devolva o próprio objeto que foi salvo".
6. **AssertJ**: `assertThat(...)` e `assertThatThrownBy(...)` dão mensagens de falha mais claras que as asserções básicas do JUnit.
7. **Matriz de casos**: para cada regra, liste as classes (válido e cada tipo de inválido) e as fronteiras. O exercício 1 pede essa matriz antes do código.

**Exemplo:**

```java
// src/test/java/com/clinicbilling/charge/PaymentServiceTest.java
package com.clinicbilling.charge;

import com.clinicbilling.patient.Patient;
import com.clinicbilling.shared.BusinessRuleException;
import com.clinicbilling.shared.NotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.Clock;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    private static final Clock DEC_5_2026 =
            Clock.fixed(Instant.parse("2026-12-05T15:00:00Z"), ZoneId.of("America/Sao_Paulo"));

    @Mock
    private ChargeRepository chargeRepository;

    @Mock
    private PaymentRepository paymentRepository;

    private PaymentService service;

    @BeforeEach
    void setUp() {
        service = new PaymentService(chargeRepository, paymentRepository, DEC_5_2026);
    }

    @Test
    void paysAnOverdueChargeWithTheExactAmount() {
        Charge charge = chargeDueOn(LocalDate.of(2026, 11, 30));
        given(chargeRepository.findById(1L)).willReturn(Optional.of(charge));
        given(paymentRepository.save(any(Payment.class))).willAnswer(invocation -> invocation.getArgument(0));

        PaymentResponse response = service.register(1L,
                new RegisterPaymentRequest(PaymentMethod.PIX, new BigDecimal("250")));

        assertThat(response.amount()).isEqualByComparingTo("250.00");
        assertThat(charge.statusOn(LocalDate.of(2026, 12, 5))).isEqualTo(ChargeStatus.PAID);
    }

    @Test
    void refusesPaymentForACanceledCharge() {
        Charge charge = chargeDueOn(LocalDate.of(2026, 12, 10));
        charge.cancel();
        given(chargeRepository.findById(1L)).willReturn(Optional.of(charge));

        assertThatThrownBy(() -> service.register(1L,
                new RegisterPaymentRequest(PaymentMethod.PIX, new BigDecimal("250.00"))))
                .isInstanceOf(BusinessRuleException.class)
                .hasMessageContaining("CANCELED");

        then(paymentRepository).shouldHaveNoInteractions();
    }

    @Test
    void returnsNotFoundForAnUnknownCharge() {
        given(chargeRepository.findById(99L)).willReturn(Optional.empty());

        assertThatThrownBy(() -> service.register(99L,
                new RegisterPaymentRequest(PaymentMethod.PIX, new BigDecimal("250.00"))))
                .isInstanceOf(NotFoundException.class);
    }

    private Charge chargeDueOn(LocalDate dueDate) {
        Patient ana = new Patient("Ana Souza", "12345678909", "ana@email.com");
        return new Charge(ana, "Consulta", new BigDecimal("250.00"), dueDate);
    }
}
```

Leitura atômica:
- `isEqualByComparingTo("250.00")` compara `BigDecimal` por valor (o `compareTo` do módulo 3). Um `isEqualTo` falharia, porque `250` e `250.00` têm escalas diferentes.
- O teste não sobe Spring: roda em milissegundos. Rode só ele com `./mvnw test -Dtest=PaymentServiceTest`.

**Exercícios:**
1. Antes de escrever código, monte em comentário a **matriz de casos** do `register`: estado da cobrança (pendente, vencida, paga, cancelada), valor (igual, menor, maior, igual com outra escala), cobrança inexistente. Marque quais já estão cobertos.
2. Complete a matriz com um `@ParameterizedTest` para os valores diferentes (249.99 e 250.01).
3. Teste a fronteira de data: uma cobrança que vence em 5/12 paga em 5/12 é aceita, e o status final é `PAID`. E uma vencida em 4/12?
4. Rode `./mvnw test` e anote o tempo de cada classe de teste no relatório do Surefire (`target/surefire-reports`). Compare a unidade com a de `@WebMvcTest` e a de `@DataJpaTest`.

**Desafio:** mutation testing é uma técnica que altera o seu código de propósito (troca `>` por `>=`, por exemplo) e verifica se algum teste falha. Faça uma "mutação manual": troque `today.isAfter(dueDate)` por `!today.isBefore(dueDate)` no `statusOn` e rode os testes. Algum falhou? Se nenhum falhou, falta um caso de fronteira. Pesquise a ferramenta PIT (pitest) e descreva em um comentário como ela automatiza isso.

**Leitura:** [Mockito: documentação](https://site.mockito.org/), [MockitoExtension (Javadoc)](https://javadoc.io/doc/org.mockito/mockito-junit-jupiter/latest/org.mockito.junit.jupiter/org/mockito/junit/jupiter/MockitoExtension.html), [AssertJ: BigDecimal assertions](https://assertj.github.io/doc/#assertj-core-bigdecimal-assertions), [Martin Fowler: Mocks Aren't Stubs](https://martinfowler.com/articles/mocksArentStubs.html), [PIT Mutation Testing](https://pitest.org/)

---

## Módulo 22: Validação de CPF e dados pessoais

**Objetivo:** criar uma validação própria de Bean Validation para CPF e proteger o CPF nas respostas.

**Conceitos:**
1. **Constraint customizada**: uma anotação sua (`@Cpf`) ligada a uma classe que faz a verificação (`CpfValidator implements ConstraintValidator<Cpf, String>`).
2. **Três elementos obrigatórios**: toda anotação de constraint tem `message`, `groups` e `payload`.
3. **`null` é válido**: por convenção, a constraint customizada aceita `null` e deixa o "obrigatório" para o `@NotBlank`. Cada anotação verifica uma coisa só.
4. **Dígitos verificadores do CPF**: os dois últimos dígitos são calculados a partir dos nove primeiros:
   - **Primeiro dígito**: multiplique os 9 primeiros dígitos pelos pesos 10, 9, 8, ..., 2 e some. Calcule o resto da divisão da soma por 11. Se o resto for menor que 2, o dígito é 0; senão, é 11 menos o resto.
   - **Segundo dígito**: mesma conta com os 10 primeiros dígitos (incluindo o primeiro dígito calculado) e os pesos 11, 10, ..., 2.
   - O CPF é válido se os dois dígitos calculados forem iguais aos dois últimos.
5. **Classes de equivalência do CPF**: válido; dígito verificador errado; tamanho errado; com letras; e o caso traiçoeiro dos **dígitos todos iguais** (`11111111111`), que passa no cálculo mas não é um CPF válido.
6. **LGPD**: CPF é dado pessoal. Princípio da **necessidade**: só exponha o que é preciso. Na listagem, o CPF pode sair mascarado (`***.456.789-**`); logs nunca devem trazer o CPF inteiro.

**Exemplo** (o esqueleto; o algoritmo é o desafio):

```java
// shared/validation/Cpf.java
package com.clinicbilling.shared.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Documented
@Constraint(validatedBy = CpfValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface Cpf {

    String message() default "must be a valid CPF";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
```

```java
// shared/validation/CpfValidator.java
package com.clinicbilling.shared.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class CpfValidator implements ConstraintValidator<Cpf, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true; // o @NotBlank cuida do obrigatório
        }
        // Desafio: implemente aqui o cálculo dos dígitos verificadores (conceito 4)
        return value.matches("\\d{11}");
    }
}
```

No `CreatePatientRequest`, troque o `@Pattern` do CPF por `@Cpf`.

**Exercícios:**
1. Escreva os testes do `CpfValidator` **antes** do algoritmo (TDD), cobrindo as classes do conceito 5. Use `12345678909` e `98765432100` como válidos e `12345678901` como dígito errado. Rode e veja os testes do "dígito errado" falharem.
2. Crie a classe `CpfFormatter` com `mask(String cpf)` e use no `PatientResponse` da listagem (`GET /patients`). Decida se o `GET /patients/{id}` mostra o CPF inteiro e justifique.
3. Procure no log da aplicação (com `show-sql` ligado) se o CPF aparece em algum lugar. Aparece nos parâmetros do SQL? Pesquise como o Hibernate registra os valores dos parâmetros e por que isso fica desligado por padrão.
4. Teste no controller (`@WebMvcTest`) que um CPF inválido devolve `400` com a mensagem `must be a valid CPF` no campo `errors.cpf`.

**Desafio:** implemente o algoritmo do conceito 4 no `CpfValidator` até todos os testes do exercício 1 passarem, incluindo a recusa de `11111111111`. Depois, escreva um gerador de CPFs válidos para usar nos seus testes (sorteie 9 dígitos e calcule os dois últimos). Por que um gerador é melhor do que copiar CPFs da internet?

**Leitura:** [Jakarta Validation: Custom Constraints](https://jakarta.ee/specifications/bean-validation/3.1/jakarta-validation-spec-3.1#constraintsdefinitionimplementation), [Hibernate Validator: Creating custom constraints](https://docs.jboss.org/hibernate/stable/validator/reference/en-US/html_single/#validator-customconstraints), [Receita Federal: CPF](https://www.gov.br/receitafederal/pt-br/assuntos/meu-cpf), [LGPD (Lei 13.709/2018)](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709.htm)

---

## Questões de entrevista da Fase 4

1. Uma cobrança "vencida" deve ser um status gravado ou calculado? Defenda uma posição e cite a desvantagem dela.
2. Como você testa uma regra que depende do horário atual?
3. Qual é a diferença entre mock e stub? O que você mockaria num teste de service, e o que não mockaria?
4. Por que o valor pago é comparado com `compareTo` e não com `equals`?
5. Uma requisição de pagamento chega duas vezes por causa de uma falha de rede. O que acontece hoje na sua API? Como evitar? (Gancho para o módulo 27.)
6. Onde ficam as regras de negócio na sua aplicação? Por que não no controller?
7. Como você protegeria dados pessoais, como o CPF, numa API?
8. O que diz a PCI DSS, em linhas gerais, e por que uma aplicação comum não deve guardar número de cartão?

---

# FASE 5: ENTREGA PROFISSIONAL

> A API já funciona. Esta fase faz ela parecer um projeto de verdade: listagens que não explodem com muitos dados, documentação navegável, um jeito de qualquer pessoa rodar com um comando e testes rodando sozinhos a cada push.

## Módulo 23: Paginação e ordenação

**Objetivo:** listar pacientes e cobranças em páginas, com ordenação escolhida pelo cliente.

**Conceitos:**
1. **Por que paginar**: `GET /charges` devolvendo 100 mil linhas pesa no banco, na rede e no cliente. A página é o `LIMIT`/`OFFSET` do roteiro de SQL, vindo da URL.
2. **`Pageable`**: um parâmetro que o Spring monta a partir de `?page=0&size=20&sort=name,asc`. A página começa em **0**.
3. **`Page<T>`**: o resultado: o conteúdo e os metadados (total de elementos, total de páginas). Para saber o total, o Spring Data roda um `count` extra.
4. **`@PageableDefault`**: o padrão quando o cliente não informa (`size = 20, sort = "name"`).
5. **Limite de tamanho**: `spring.data.web.pageable.max-page-size` impede um cliente de pedir `size=1000000`.
6. **DTO de página**: devolva um record seu (`PageResponse`) em vez do `Page` do Spring. O formato do JSON fica estável e documentado, e você evita o aviso que o Spring Data registra ao serializar o `PageImpl` direto.
7. **Paginação com `join fetch` de coleções**: buscar uma **lista** filha junto (`@OneToMany`) e paginar faz o Hibernate paginar **na memória**. Com `@ManyToOne` (o seu caso) não há esse problema.

**Exemplo:**

```java
// shared/PageResponse.java
package com.clinicbilling.shared;

import org.springframework.data.domain.Page;

import java.util.List;

public record PageResponse<T>(List<T> content, int page, int size, long totalElements, int totalPages) {

    public static <T> PageResponse<T> from(Page<T> page) {
        return new PageResponse<>(page.getContent(), page.getNumber(), page.getSize(),
                page.getTotalElements(), page.getTotalPages());
    }
}
```

No `PatientService`:

```java
    @Transactional(readOnly = true)
    public Page<PatientResponse> findAll(Pageable pageable) {
        return repository.findAll(pageable).map(PatientResponse::from);
    }
```

No `PatientController` (substituindo o `findAll` antigo):

```java
    @GetMapping
    public PageResponse<PatientResponse> findAll(@PageableDefault(size = 20, sort = "name") Pageable pageable) {
        return PageResponse.from(service.findAll(pageable));
    }
```

Imports: `org.springframework.data.domain.Page`, `org.springframework.data.domain.Pageable` e `org.springframework.data.web.PageableDefault`.

```bash
curl -s "http://localhost:8080/patients?page=0&size=2&sort=name,desc"
```

**Exercícios:**
1. Configure `spring.data.web.pageable.max-page-size=100` e peça `size=500`. Quantos itens vieram?
2. Pagine o `GET /charges`, mantendo os filtros de status e paciente. Os métodos do repositório passam a receber `Pageable` e devolver `Page<Charge>`.
3. Observe no log o `select ... limit ?` e o `select count(...)`. Quando o Spring Data pula o `count`?
4. Atualize os testes de `@WebMvcTest` do `GET /patients` para o novo formato da resposta.

**Desafio:** a paginação por `OFFSET` fica lenta em páginas muito altas (o banco lê e descarta tudo antes). Pesquise **paginação por cursor** (keyset pagination) e escreva no README quando você a usaria nesta API. Não precisa implementar.

**Leitura:** [Spring Data: Paging and Sorting](https://docs.spring.io/spring-data/commons/reference/repositories/query-methods-details.html#repositories.special-parameters), [Spring Data: Web support](https://docs.spring.io/spring-data/commons/reference/repositories/core-extensions.html#core.web), [Use The Index, Luke: Paging Through Results](https://use-the-index-luke.com/sql/partial-results/fetch-next-page)

---

## Módulo 24: Documentação: OpenAPI, Swagger UI e README

**Objetivo:** gerar a documentação da API a partir do código e escrever um README que deixa qualquer pessoa rodar o projeto.

**Conceitos:**
1. **OpenAPI**: uma especificação padrão (um JSON ou YAML) que descreve os endpoints, parâmetros, corpos e respostas de uma API.
2. **Swagger UI**: uma página que lê a especificação e deixa testar a API pelo navegador.
3. **springdoc-openapi**: a biblioteca que examina os seus controllers em tempo de execução e gera a especificação. Para o Spring Boot 4 é a **linha 3.x**.
4. **Anotações opcionais**: `@Tag` (agrupar), `@Operation(summary = "...")` (descrever) e `@ApiResponse` (documentar status). Sem nenhuma anotação já sai uma documentação básica; as Bean Validations aparecem como regras do schema.
5. **README como produto**: quem avalia um teste prático lê o README antes do código. Ele precisa responder: o que é, como rodar, como testar, quais decisões foram tomadas.

**Exemplo:** adicione ao `pom.xml`:

```xml
<dependency>
    <groupId>org.springdoc</groupId>
    <artifactId>springdoc-openapi-starter-webmvc-ui</artifactId>
    <version>3.0.x</version> <!-- troque pela versão 3.x mais recente -->
</dependency>
```

Esta dependência **precisa** de versão, porque não faz parte do Spring Boot. Escolha a versão 3.x mais recente nas [releases do springdoc](https://github.com/springdoc/springdoc-openapi/releases) e confira, nas notas, que ela suporta a sua versão do Spring Boot. Se a aplicação não subir depois de adicionar a dependência, a primeira suspeita é essa compatibilidade.

Suba a aplicação e abra:
- `http://localhost:8080/swagger-ui.html` (a interface);
- `http://localhost:8080/v3/api-docs` (a especificação em JSON).

Um esqueleto de README (em inglês, como o código):

````markdown
# Clinic Billing API

REST API for clinics to register patients, issue charges and record payments.
Personal study project (Java 25, Spring Boot 4, MySQL 8.4).

## Running locally

Requirements: Java 25, Docker.

```bash
sudo docker compose up -d     # MySQL on localhost:3306
./mvnw spring-boot:run        # API on http://localhost:8080
```

API docs: http://localhost:8080/swagger-ui.html

## Running the tests

```bash
./mvnw test
```

The repository and integration tests use the `clinic_billing_test` database created by `docker/mysql-init`.

## Main endpoints

| Method | Path | Description |
|--------|------|-------------|
| POST | /patients | Register a patient |
| ... | ... | ... |

## Business rules

- A charge is PENDING, PAID or CANCELED. OVERDUE is derived from the due date (America/Sao_Paulo).
- ...

## Design decisions

- Flyway owns the schema; Hibernate only validates it.
- ...
````

**Exercícios:**
1. Adicione o springdoc, abra o Swagger UI e faça um `POST /patients` por lá. As regras de validação aparecem no schema do `CreatePatientRequest`?
2. Use `@Tag` e `@Operation` no `ChargeController` para descrever cada endpoint em uma frase.
3. Documente com `@ApiResponse` os status `201`, `400`, `404` e `409` do `POST /charges/{id}/payments`.
4. Escreva o README completo, com a tabela de endpoints e as regras de negócio.

**Desafio:** peça a uma pessoa (ou ao Claude, sem contexto do projeto) para seguir **só** o README e rodar a API do zero. Anote cada ponto em que ela travou e corrija o README. É o mesmo raciocínio de um caso de teste: o README é o "procedimento" e a pessoa é o "executor".

**Leitura:** [springdoc-openapi](https://springdoc.org/), [springdoc-openapi no GitHub](https://github.com/springdoc/springdoc-openapi), [OpenAPI Specification](https://spec.openapis.org/oas/latest.html), [GitHub Docs: About READMEs](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-readmes)

---

## Módulo 25: Empacotando e rodando com Docker

**Objetivo:** gerar o `.jar` executável, criar a imagem Docker da API e subir API e banco juntos com o Compose.

**Conceitos:**
1. **Fat jar**: o `spring-boot-maven-plugin` gera um `.jar` com a aplicação **e** todas as dependências dentro. Roda com `java -jar` (o desafio do módulo 1, resolvido).
2. **Configuração por variável de ambiente**: o Spring Boot lê `SPRING_DATASOURCE_URL` como se fosse `spring.datasource.url` ("relaxed binding"). O mesmo `.jar` roda em qualquer ambiente, mudando só as variáveis.
3. **Dockerfile em dois estágios**: o primeiro estágio tem o JDK e o Maven para **compilar**; o segundo tem só o JRE para **rodar**. A imagem final fica menor e sem ferramentas de build.
4. **Rede do Compose**: dentro do Compose, a API chega no banco pelo **nome do serviço** (`mysql:3306`), não por `localhost` (dentro do container, `localhost` é o próprio container).
5. **`depends_on` com `condition: service_healthy`**: a API só sobe quando o healthcheck do MySQL passar.
6. **Profiles do Compose**: `profiles: ["app"]` deixa o serviço da API desligado por padrão. `sudo docker compose up -d` sobe só o banco (para o dia a dia no IntelliJ); `--profile app` sobe os dois.
7. **`.dockerignore`**: evita copiar `target/` e `.git/` para dentro da imagem.

**Exemplo:**

```bash
./mvnw package
java -jar target/clinic-billing-api-0.0.1-SNAPSHOT.jar
```

O `package` roda os testes, que precisam do MySQL no ar. Para empacotar sem testar (só quando souber o porquê), `./mvnw package -DskipTests`.

`Dockerfile` (na raiz do projeto):

```dockerfile
FROM eclipse-temurin:25-jdk AS build
WORKDIR /app
COPY .mvn .mvn
COPY mvnw pom.xml ./
RUN ./mvnw -q dependency:go-offline
COPY src src
RUN ./mvnw -q package -DskipTests

FROM eclipse-temurin:25-jre
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
```

`.dockerignore`:

```
target/
.git/
.idea/
```

No `compose.yaml`, acrescente o serviço `app` (dentro de `services:`, no mesmo nível do `mysql`):

```yaml
  app:
    build: .
    profiles: ["app"]
    depends_on:
      mysql:
        condition: service_healthy
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/clinic_billing
      SPRING_DATASOURCE_USERNAME: billing
      SPRING_DATASOURCE_PASSWORD: billing
      SPRING_JPA_SHOW_SQL: "false"
    ports:
      - "127.0.0.1:8080:8080"
```

```bash
sudo docker compose --profile app up -d --build
sudo docker compose logs -f app
curl -i http://localhost:8080/patients
sudo docker compose --profile app down
```

Leitura atômica do `Dockerfile`: copiar o `pom.xml` e baixar as dependências **antes** de copiar o `src` aproveita o cache do Docker. Se só o código mudou, a camada das dependências não é refeita e o build fica muito mais rápido.

**Exercícios:**
1. Rode o `.jar` com `--server.port=9090` na linha de comando e confirme a porta no log. Qual tem prioridade: a linha de comando ou o `application.properties`?
2. Rode o `.jar` com a variável `SPRING_JPA_SHOW_SQL=false` e confirme que o SQL sumiu do log.
3. Suba o Compose com `--profile app` e confira, no log da API, as linhas do Flyway. Em qual banco ele rodou?
4. Mude uma linha do controller e rode o `--build` de novo. Observe quais etapas do `Dockerfile` usaram cache (`CACHED`).

**Desafio:** a imagem roda a aplicação como `root` dentro do container. Pesquise como criar um usuário sem privilégios no `Dockerfile` (instrução `USER`) e aplique. Depois, meça o tamanho da imagem (`sudo docker images`) e compare com uma versão de um estágio só, baseada no JDK.

**Leitura:** [Spring Boot: Externalized Configuration](https://docs.spring.io/spring-boot/reference/features/external-config.html), [Spring Boot: Container Images](https://docs.spring.io/spring-boot/reference/packaging/container-images/index.html), [Docker: Multi-stage builds](https://docs.docker.com/build/building/multi-stage/), [Docker Compose: profiles](https://docs.docker.com/compose/how-tos/profiles/), [Eclipse Temurin no Docker Hub](https://hub.docker.com/_/eclipse-temurin)

---

## Módulo 26: Integração contínua com GitHub Actions

**Objetivo:** rodar build e testes, com um MySQL de verdade, a cada push e pull request.

**Conceitos:**
1. **CI**: o mesmo do módulo 21 do roteiro de Playwright: um servidor roda os testes a cada mudança.
2. **Service container**: um container que o GitHub sobe **ao lado** do job, como o `mysql` do seu Compose. Os testes enxergam o banco em `localhost:3306`.
3. **`actions/setup-java`**: instala o JDK e, com `cache: maven`, guarda as dependências entre execuções.
4. **`./mvnw -B verify`**: `verify` roda até depois dos testes; `-B` é o modo "batch", sem cores e sem perguntas, próprio para CI.
5. **Profile no CI**: `SPRING_PROFILES_ACTIVE=test` faz **todos** os testes (inclusive o `contextLoads`) usarem o banco de teste.
6. **Por que não Testcontainers aqui**: no GitHub Actions o Docker funciona sem `sudo`, então o Testcontainers rodaria. Mas no seu Omarchy não (módulo 0). Usar o service container mantém o CI e a sua máquina parecidos.

**Exemplo** (`.github/workflows/ci.yml`):

```yaml
name: CI

on:
  push:
    branches: [main]
  pull_request:

jobs:
  build:
    runs-on: ubuntu-latest

    services:
      mysql:
        image: mysql:8.4
        env:
          MYSQL_ROOT_PASSWORD: root
          MYSQL_DATABASE: clinic_billing_test
          MYSQL_USER: billing
          MYSQL_PASSWORD: billing
        ports:
          - 3306:3306
        options: >-
          --health-cmd="mysqladmin ping -h localhost -uroot -proot"
          --health-interval=5s
          --health-timeout=3s
          --health-retries=20

    steps:
      - uses: actions/checkout@v5
      - uses: actions/setup-java@v5
        with:
          distribution: temurin
          java-version: '25'
          cache: maven
      - name: Build and test
        run: ./mvnw -B verify
        env:
          SPRING_PROFILES_ACTIVE: test
```

Se o `mvnw` não estiver executável no Git, o job falha com "Permission denied". Corrija com `git update-index --chmod=+x mvnw` e um commit.

**Exercícios:**
1. Suba o workflow e acompanhe a execução na aba **Actions**. Quanto tempo levou? E na segunda vez, com o cache?
2. Quebre um teste de propósito, abra um pull request e veja o check vermelho. Conserte no mesmo branch.
3. Proteja a `main` nas configurações do repositório: exija o check do CI verde antes do merge.
4. Coloque o badge do workflow no topo do README.

**Desafio:** acrescente um segundo job que constrói a imagem Docker (`docker build .`) só quando o primeiro passar (`needs: build`). Explique em um comentário no workflow por que construir a imagem no CI pega erros que os testes não pegam.

**Leitura:** [GitHub Docs: Building and testing Java with Maven](https://docs.github.com/en/actions/use-cases-and-examples/building-and-testing/building-and-testing-java-with-maven), [GitHub Docs: About service containers](https://docs.github.com/en/actions/use-cases-and-examples/using-containerized-services/about-service-containers), [actions/setup-java](https://github.com/actions/setup-java)

---

## Questões de entrevista da Fase 5

1. Como você paginaria uma listagem com milhões de registros? Qual o problema do `OFFSET` alto?
2. O que é OpenAPI e para que serve no dia a dia de um time?
3. Como a mesma aplicação roda em desenvolvimento, teste e produção com configurações diferentes?
4. Por que usar um Dockerfile com dois estágios?
5. Dentro de um Compose, por que a API não acessa o banco por `localhost`?
6. O que o seu pipeline de CI verifica? O que você acrescentaria?

---

# FASE 6: EVOLUÇÕES

> Módulos opcionais, para depois do teste prático ou para quando o essencial estiver sólido. Cada um é um bom assunto de conversa com o líder técnico de uma fintech.

## Módulo 27: Idempotência em pagamentos

**Objetivo:** garantir que o mesmo pedido de pagamento, enviado duas vezes, não gere dois pagamentos nem um erro confuso.

**Conceitos:**
1. **O problema**: o cliente envia `POST /charges/1/payments`, a rede cai antes da resposta e o cliente tenta de novo. Hoje a segunda tentativa volta `409` ("já paga"), e o cliente não sabe se o **seu** pagamento entrou.
2. **Chave de idempotência**: o cliente gera um identificador único por **intenção** de pagamento e manda no header `Idempotency-Key`. Repetir com a mesma chave devolve **o mesmo resultado** da primeira vez.
3. **Onde guardar**: uma coluna `idempotency_key` com `UNIQUE` na tabela `payment`. O banco garante a unicidade mesmo com duas requisições simultâneas.
4. **Fluxo**: se já existe pagamento com a chave, devolve ele (`200`); senão, executa e grava a chave junto (`201`).
5. **Mesma chave, corpo diferente**: é um erro do cliente (`422` ou `409`). Guardar um hash do corpo permite detectar.
6. **Prática de mercado**: provedores de pagamento usam esse header, e há uma proposta de padrão no IETF.

**Exemplo** (o ponto de partida; o resto é o exercício):

```sql
-- V5__add_payment_idempotency_key.sql
ALTER TABLE payment ADD COLUMN idempotency_key VARCHAR(64) NULL;
ALTER TABLE payment ADD CONSTRAINT uk_payment_idempotency_key UNIQUE (idempotency_key);
```

```java
    @PostMapping("/{id}/payments")
    public ResponseEntity<PaymentResponse> pay(
            @PathVariable Long id,
            @RequestHeader(name = "Idempotency-Key", required = false) String idempotencyKey,
            @Valid @RequestBody RegisterPaymentRequest request) {
        // 1. se idempotencyKey != null e já existe pagamento com ela: 200 com o pagamento existente
        // 2. senão: registra o pagamento gravando a chave e devolve 201
        throw new UnsupportedOperationException("TODO: módulo 27");
    }
```

**Exercícios:**
1. Implemente o fluxo do conceito 4 com `Optional<Payment> findByIdempotencyKey(String key)` no repositório.
2. Teste com `curl` enviando a mesma chave duas vezes (gere uma com `uuidgen`). A segunda resposta é `200` com o mesmo `id`?
3. Escreva os testes de unidade do fluxo (chave nova, chave repetida, sem chave).
4. Envie a mesma chave com outra forma de pagamento. O que a sua API faz? Decida e implemente.

**Desafio:** duas requisições com a mesma chave chegam **ao mesmo tempo**. As duas não encontram a chave, as duas tentam gravar, e uma falha no `UNIQUE`. Trate a `DataIntegrityViolationException` buscando de novo o pagamento pela chave e devolvendo ele. Explique por que a verificação no Java sozinha não basta e o `UNIQUE` do banco é indispensável.

**Leitura:** [IETF: The Idempotency-Key HTTP Header Field (draft)](https://datatracker.ietf.org/doc/draft-ietf-httpapi-idempotency-key-header/), [Stripe: Idempotent requests](https://docs.stripe.com/api/idempotent_requests), [RFC 9110: Idempotent Methods](https://www.rfc-editor.org/rfc/rfc9110#section-9.2.2)

---

## Módulo 28: Testando a API de ponta a ponta (Playwright e Testcontainers)

**Objetivo:** testar a API rodando de verdade, de fora, com a ferramenta que você já domina, e conhecer o Testcontainers.

**Conceitos:**
1. **Teste de ponta a ponta da API**: chama os endpoints por HTTP, como um cliente real, com a aplicação e o banco no ar.
2. **Playwright sem navegador**: a fixture `request` do módulo 16 do roteiro de Playwright faz requisições direto, sem abrir página.
3. **Dados de teste isolados**: testes de ponta a ponta gravam no banco de verdade. Rodar duas vezes com o mesmo CPF quebra o segundo (`409`). Cada teste deve gerar os próprios dados (CPF aleatório e válido, o desafio do módulo 22) ou limpar o que criou.
4. **Rede do container do Playwright**: se você roda o Playwright no container (`pw-docker`), `localhost` dentro dele **não** é a sua máquina. Rode esse container com `--network host` para alcançar a API em `localhost:8080`.
5. **Testcontainers**: biblioteca que sobe um MySQL descartável num container para cada execução de testes, dentro do próprio JUnit. É o padrão de mercado para testes de integração em Java.
6. **Testcontainers no Omarchy**: ele fala com o Docker como o seu usuário, sem `sudo`. Só funciona com a opção `Setup > Security > Sudoless Docker` do Omarchy, que **equivale a dar root sem senha** a qualquer programa rodando como você. É uma troca consciente de segurança por conveniência; no CI (módulo 26) não há essa troca.

**Exemplo** (`tests/clinic-billing-api/patients.spec.ts`, no projeto do roteiro de Playwright):

```ts
import { test, expect } from '@playwright/test';

const api = 'http://localhost:8080';

test('creates a patient and reads it back', async ({ request }) => {
  const cpf = '12345678909'; // troque por um CPF gerado (desafio)
  const created = await request.post(`${api}/patients`, {
    data: { name: 'Ana Souza', cpf, email: 'ana@email.com' },
  });
  expect(created.status()).toBe(201);

  const location = created.headers()['location'];
  const fetched = await request.get(`${api}${location}`);
  await expect(fetched).toBeOK();
  expect(await fetched.json()).toMatchObject({ name: 'Ana Souza' });
});
```

**Exercícios:**
1. Rode o teste com a API no ar. Rode de novo. Por que a segunda execução falha?
2. Escreva em TypeScript uma função que gera CPFs válidos (o mesmo algoritmo do módulo 22) e use no teste. Agora ele passa sempre?
3. Escreva a jornada completa: cadastrar paciente, criar cobrança, pagar, conferir o status `PAID` e tentar pagar de novo (`409`).
4. Escreva um teste parametrizado (módulo 12 do roteiro de Playwright) para as validações do `POST /patients`.

**Desafio:** leia a página do Testcontainers para Spring Boot (`@ServiceConnection`) e escreva, num arquivo `docs/testcontainers.md`, como ficaria o `ChargeRepositoryTest` com Testcontainers e o que você precisaria mudar no Omarchy para rodar. Decida, com o argumento de segurança do conceito 6, se vale a pena ligar o Sudoless Docker na sua máquina. Não existe resposta certa; existe decisão justificada.

**Leitura:** [Playwright: API testing](https://playwright.dev/docs/api-testing), [Spring Boot: Testcontainers](https://docs.spring.io/spring-boot/reference/testing/testcontainers.html), [Testcontainers for Java](https://java.testcontainers.org/), roteiro de Playwright (módulo 16)

---

## Módulo 29: Segurança básica com Spring Security

**Objetivo:** exigir autenticação nos endpoints e entender como o Spring Security se encaixa no caminho da requisição.

**Conceitos:**
1. **Autenticação x autorização**: autenticação responde "quem é você?"; autorização responde "você pode fazer isto?".
2. **Filtros**: o Spring Security é uma corrente de filtros que roda **antes** do `DispatcherServlet` (módulo 9, conceito 13). Uma requisição sem credencial é barrada antes de chegar ao controller.
3. **Comportamento padrão**: só de adicionar o `spring-boot-starter-security`, **tudo** passa a exigir login, com um usuário `user` e uma senha gerada no log da inicialização.
4. **`SecurityFilterChain`**: o bean onde você declara as regras: o que é público, o que exige login, que tipo de login.
5. **HTTP Basic**: usuário e senha codificados em Base64 no header `Authorization` a cada requisição. Simples, e só aceitável com HTTPS.
6. **`401` x `403`**: `401 Unauthorized` é "não sei quem você é"; `403 Forbidden` é "sei quem você é, mas você não pode".
7. **CSRF**: proteção para aplicações com sessão em navegador. Uma API sem estado, usada por outros sistemas, costuma desligar.
8. **JWT**: um token assinado que o cliente recebe ao logar e envia nas próximas requisições. É o próximo passo natural, e o assunto merece um estudo próprio depois deste roteiro.

**Exemplo:** adicione `spring-boot-starter-security` (e `spring-boot-starter-security-test` com escopo `test`) e crie:

```java
// shared/SecurityConfig.java
package com.clinicbilling.shared;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/swagger-ui.html", "/swagger-ui/**", "/v3/api-docs/**").permitAll()
                        .anyRequest().authenticated())
                .httpBasic(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable());
        return http.build();
    }
}
```

```properties
spring.security.user.name=clinic
spring.security.user.password=change-me
```

```bash
curl -i http://localhost:8080/patients
curl -i -u clinic:change-me http://localhost:8080/patients
```

**Exercícios:**
1. Rode os testes. Os de `@WebMvcTest` começaram a falhar com `401`? Corrija com `@WithMockUser` (de `org.springframework.security.test.context.support`) e explique o que ele faz.
2. Crie dois usuários em memória (`InMemoryUserDetailsManager`) com papéis `RECEPTION` e `FINANCE`, e faça só `FINANCE` poder registrar pagamentos. Teste o `403`.
3. Com as credenciais erradas, que status e que corpo voltam? Estão no formato ProblemDetail?
4. Mova a senha do `application.properties` para uma variável de ambiente. Por que senha no repositório é um problema mesmo num projeto de estudo?

**Desafio:** pesquise o suporte do Spring Security a **Resource Server com JWT** e escreva, em `docs/jwt-plan.md`, o plano para trocar o HTTP Basic por JWT: quem emite o token, como a API valida a assinatura, o que vai dentro do token e o que nunca deve ir (dica: CPF).

**Leitura:** [Spring Security: Servlet Architecture](https://docs.spring.io/spring-security/reference/servlet/architecture.html), [Spring Security: Authorize HTTP Requests](https://docs.spring.io/spring-security/reference/servlet/authorization/authorize-http-requests.html), [Spring Boot: Security](https://docs.spring.io/spring-boot/reference/web/spring-security.html), [OWASP API Security Top 10](https://owasp.org/API-Security/)

---

## Questões de entrevista da Fase 6

1. O que é idempotência? Como você tornaria um endpoint de pagamento idempotente?
2. Por que a verificação de duplicidade precisa de uma constraint no banco, e não só de um `if` no Java?
3. Qual é a diferença entre teste de unidade, de integração e de ponta a ponta numa API? Onde cada um pega erros?
4. Qual é a diferença entre autenticação e autorização? E entre `401` e `403`?
5. Como funciona um JWT, em linhas gerais? Que dados você nunca colocaria nele?

---

# FASE 7: PROJETO FINAL

## Módulo 30: A API de cobranças no GitHub

**Objetivo:** fechar o projeto `clinic-billing-api` como um repositório de portfólio: completo, testado, documentado e fácil de rodar.

A especificação completa (entidades, regras, endpoints, requisitos técnicos e o checklist de conclusão) está no documento **`projeto-final-java-spring-boot.md`**. Em resumo, o projeto está pronto quando:

1. **Funciona**: pacientes, cobranças, cancelamento e pagamentos, com os status `PENDING`, `OVERDUE` (derivado), `PAID` e `CANCELED` e todas as regras da Fase 4.
2. **Persiste direito**: MySQL 8.4, schema só por Flyway, `ddl-auto=validate`, constraints no banco, `@Version` na cobrança, sem N+1 nas listagens.
3. **Responde direito**: status HTTP coerentes, erros em ProblemDetail, validação com mensagens por campo, paginação.
4. **Está testado**: unidade (domínio e services), `@WebMvcTest` (contrato HTTP) e repositório contra MySQL, com os casos de borda da matriz do módulo 21.
5. **Roda com poucos comandos**: README completo, `compose.yaml`, Dockerfile e CI verde.
6. **Foi feito como num time**: issues, branches e pull requests por funcionalidade (apêndice E), commits pequenos com mensagens claras.

**Conceitos aplicados por módulo:**

| Parte do projeto | Módulos |
|------------------|---------|
| Domínio, BigDecimal, datas e testes de unidade | 2 a 6, 21 |
| Endpoints, DTOs e camadas | 9, 10 |
| Validação e ProblemDetail | 11, 22 |
| Testes da camada web | 12 |
| Flyway, JPA, relacionamentos | 13 a 16 |
| Transações e lock otimista | 17 |
| Testes de repositório | 18 |
| Status derivado e pagamentos | 19, 20 |
| Paginação, OpenAPI, Docker, CI | 23 a 26 |
| Evoluções (opcionais) | 27 a 29 |

**Revisão final:** peça uma revisão do repositório inteiro (no chat ou com o Claude Code, apêndice F) e registre no README as melhorias aplicadas.

---

# TRILHA PARALELA: LÓGICA E ESTRUTURAS DE DADOS

> A vaga pede lógica, algoritmos e estruturas de dados, e testes práticos às vezes incluem um problema desse tipo. Esta trilha anda **em paralelo** às fases, em ritmo fixo: **3 problemas por semana**, sempre em Java. Faça um módulo da trilha a cada fase do roteiro principal (L1 com a Fase 1, L2 com a Fase 2, e assim por diante).

### Como praticar

- **Beecrowd** (em português, problemas curtos): a classe precisa se chamar `Main` e ler da entrada padrão. Guarde em `~/Projects/spring-boot-lab/logica/beecrowd/<número>/Main.java` e teste com `java Main.java < entrada.txt`.
- **LeetCode** (em inglês, estilo entrevista): use a categoria **Easy**. Guarde em `~/Projects/spring-boot-lab/logica/leetcode/<número>-<nome>.java`.
- **Antes de codar**, escreva em um comentário: a ideia, um exemplo feito à mão e a complexidade esperada. Isso é o que um entrevistador quer ouvir em voz alta.
- **Teste os casos de borda** como QA: entrada vazia, um elemento, todos iguais, valores no limite.
- Os números dos problemas abaixo são do catálogo de cada site; confira o enunciado antes de começar.

### L1: Entrada, saída e aritmética (com a Fase 1)

**Conceitos:** `Scanner` x `BufferedReader` (o segundo é mais rápido para entradas grandes); divisão inteira e resto (`/` e `%`); por que somar dinheiro em `double` dá errado também aqui (trabalhe em **centavos**, como `int`).

**Problemas:** Beecrowd 1001 (Extremamente Básico), 1018 (Cédulas), 1021 (Notas e Moedas: faça em centavos), 1036 (Fórmula de Bhaskara).

### L2: Arrays e strings (com a Fase 2)

**Conceitos:** índice e tamanho; dois ponteiros (um no começo, outro no fim); `StringBuilder` para montar texto em laço (concatenar `String` em laço cria um objeto novo a cada volta); `char` e código numérico.

**Problemas:** LeetCode 344 (Reverse String), 125 (Valid Palindrome), 26 (Remove Duplicates from Sorted Array); Beecrowd 1024 (Criptografia).

### L3: HashMap e HashSet (com a Fase 3)

**Conceitos:** como uma tabela hash encontra um elemento em tempo médio constante; por que isso depende de `hashCode` e `equals` (módulo 2); contagem de frequência com `merge`; trocar um laço duplo (O(n²)) por um mapa (O(n)).

**Problemas:** LeetCode 1 (Two Sum), 217 (Contains Duplicate), 242 (Valid Anagram), 387 (First Unique Character in a String).

### L4: Pilha e fila (com a Fase 4)

**Conceitos:** pilha (último a entrar, primeiro a sair) e fila (primeiro a entrar, primeiro a sair); `ArrayDeque` para as duas (a classe `Stack` é antiga e não é recomendada); onde aparecem: desfazer, parênteses balanceados, processar pedidos em ordem.

**Problemas:** LeetCode 20 (Valid Parentheses), 232 (Implement Queue using Stacks); Beecrowd 1068 (Balanço de Parênteses I), 1069 (Diamantes e Areia), 1110 (Jogando Cartas Fora).

### L5: Ordenação e busca binária (com a Fase 5)

**Conceitos:** `Arrays.sort` e `List.sort` com `Comparator` (inclusive `thenComparing` para desempate); ordenação estável; busca binária em O(log n) e o cuidado com o cálculo do meio; quando ordenar antes facilita o resto.

**Problemas:** LeetCode 704 (Binary Search), 35 (Search Insert Position), 88 (Merge Sorted Array); Beecrowd 1259 (Pares e Ímpares), 1244 (Ordenação por Tamanho).

### L6: Big O, recursão e listas ligadas (com as Fases 6 e 7)

**Conceitos:** O(1), O(log n), O(n), O(n log n), O(n²) e como estimar olhando os laços; recursão (caso base e passo); memoização; lista ligada (cada nó aponta para o próximo) e por que `LinkedList` raramente é melhor que `ArrayList` na prática.

**Problemas:** LeetCode 509 (Fibonacci Number, primeiro recursivo, depois com memoização), 70 (Climbing Stairs), 206 (Reverse Linked List), 21 (Merge Two Sorted Lists).

**Questões de entrevista da trilha:**
1. Qual é a complexidade de buscar um elemento num `ArrayList`, num `HashSet` e numa lista ordenada com busca binária?
2. Por que um `HashMap` pode ficar lento se o `hashCode` for ruim?
3. Quando você usaria uma pilha? E uma fila? Dê um exemplo de backend.
4. Explique a solução do Two Sum em O(n) e por que ela funciona.

**Leitura:** [Beecrowd](https://judge.beecrowd.com/), [LeetCode](https://leetcode.com/problemset/), [Collections Trail (Java Tutorial)](https://docs.oracle.com/javase/tutorial/collections/), [ArrayDeque (Javadoc)](https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/ArrayDeque.html)

---

# APÊNDICES

## A. Tutorial da versão 3.x → Spring Boot 4

Muito material na internet (inclusive cursos recentes) ainda usa o Spring Boot 3. A lógica é a mesma; os nomes mudaram. Use esta tabela para "traduzir":

| Você viu no tutorial (3.x) | Use no Spring Boot 4 |
|----------------------------|----------------------|
| `spring-boot-starter-web` | `spring-boot-starter-webmvc` |
| só `spring-boot-starter-test` | também o par de teste de cada starter: `spring-boot-starter-webmvc-test`, `spring-boot-starter-data-jpa-test`, `spring-boot-starter-security-test` |
| `org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest` | `org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest` |
| `org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc` | `org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc` |
| `org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest` | `org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest` |
| `org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager` | `org.springframework.boot.jpa.test.autoconfigure.TestEntityManager` |
| `org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase` | `org.springframework.boot.jdbc.test.autoconfigure.AutoConfigureTestDatabase` |
| `@MockBean` | `@MockitoBean` (`org.springframework.test.context.bean.override.mockito`) |
| `@SpyBean` | `@MockitoSpyBean` |
| `mvc.perform(...).andExpect(...)` | ainda funciona; o recomendado é o `MockMvcTester` |
| `TestRestTemplate` | `RestTestClient` (novo no Spring Boot 4) |
| `flyway-core` | `spring-boot-starter-flyway` (e `flyway-mysql` para MySQL) |
| springdoc-openapi 2.x | springdoc-openapi 3.x |
| "JUnit 5" | JUnit 6 (a API Jupiter que você escreve é a mesma) |
| `com.fasterxml.jackson.databind.ObjectMapper` (Jackson 2) | `tools.jackson.databind.ObjectMapper` (Jackson 3); as anotações como `@JsonProperty` continuam em `com.fasterxml.jackson.annotation` |
| `javax.persistence`, `javax.validation` (tutoriais do Boot 2) | `jakarta.persistence`, `jakarta.validation` |

## B. PostgreSQL → MySQL, resumo

| Assunto | PostgreSQL (roteiro de SQL) | MySQL 8.4 |
|---------|-----------------------------|-----------|
| Chave automática | `GENERATED ALWAYS AS IDENTITY` | `AUTO_INCREMENT` |
| Id gerado | `RETURNING id` | `LAST_INSERT_ID()` (sem `RETURNING`) |
| Dinheiro | `numeric(10,2)` | `DECIMAL(10,2)` |
| Data e hora | `timestamptz` | `DATETIME(6)` (sem fuso) ou `TIMESTAMP` (UTC, até 2038) |
| Texto | `text`, `varchar(n)` | `VARCHAR(n)`, `TEXT` |
| Concatenar | `'a' \|\| 'b'` | `CONCAT('a', 'b')` (`\|\|` é "OU") |
| `7 / 2` | `3` | `3.5000` (inteira: `7 DIV 2`) |
| Busca sem maiúsculas | `ILIKE` | `LIKE` (a collation padrão já ignora maiúsculas e acentos) |
| Upsert | `ON CONFLICT ... DO UPDATE` | `ON DUPLICATE KEY UPDATE` |
| Schemas | database > schema > tabela | database = schema > tabela |
| Cliente | `psql` | `mysql` |
| Descrever tabela | `\d tabela` | `DESCRIBE tabela;` ou `SHOW CREATE TABLE tabela;` |

## C. Erros comuns e o que significam

| Mensagem (resumida) | Causa mais comum |
|---------------------|------------------|
| `Communications link failure` | O MySQL não está no ar ou a URL está errada. `sudo docker compose ps` |
| `Access denied for user 'billing'` | Usuário ou senha errados, ou o volume foi criado com outra senha (reset: `down -v`) |
| `Public Key Retrieval is not allowed` | Conexão sem SSL com o MySQL 8. Para estudo local, acrescente `?allowPublicKeyRetrieval=true&useSSL=false` à URL |
| `Unsupported Database: MySQL 8.4` | Falta a dependência `org.flywaydb:flyway-mysql` |
| `Validate failed: Migrations have failed validation` / `checksum mismatch` | Uma migration que já rodou foi editada. Desfaça a edição e crie uma migration nova |
| `Schema-validation: missing table [x]` | A migration da tabela não rodou ou tem outro nome |
| `Schema-validation: wrong column type encountered in column [status]` | Enum em coluna `VARCHAR` sem `columnDefinition = "varchar(20)"` (módulo 16, conceito 10) |
| `LazyInitializationException: could not initialize proxy` | Acesso a uma associação `LAZY` fora da transação (módulo 16) |
| `Table 'clinic_billing_test.x' doesn't exist` no `@DataJpaTest` | O Flyway não rodou no slice de teste (veja a nota do módulo 18) |
| `No qualifying bean of type` | Classe sem `@Service`/`@Component`, ou fora do pacote da classe principal |
| `Parameter 0 of constructor ... required a bean of type` | O mesmo caso acima, visto pelo lado de quem depende do bean |
| `Name for argument of type [java.lang.Long] not specified` | Compilação sem a opção `-parameters` (o `spring-boot-starter-parent` liga; confira se o IntelliJ está usando o Maven para compilar) ou use `@PathVariable("id")` |
| `415 Unsupported Media Type` | Faltou `-H "Content-Type: application/json"` no `curl` |
| `400` com "JSON parse error" | JSON malformado ou tipo errado (texto onde se esperava número) |
| `Whitelabel Error Page` / `404` sem corpo da API | Caminho errado ou controller fora do `@ComponentScan` |
| `Port 8080 was already in use` | Outra instância da aplicação ainda rodando (IntelliJ ou `java -jar`) |
| `ObjectOptimisticLockingFailureException` | Duas transações alteraram a mesma linha (módulo 17); trate com `409` |
| `DataIntegrityViolationException` | O banco recusou: `UNIQUE`, chave estrangeira ou `CHECK` |
| `permission denied while trying to connect to the Docker daemon socket` | Faltou o `sudo` no `docker`, ou uma ferramenta (Testcontainers) tentando usar o Docker sem ele |
| `Permission denied: ./mvnw` | O arquivo perdeu a permissão de execução: `chmod +x mvnw` |

As mensagens padrão da Bean Validation (`must not be blank`) podem aparecer em português (`não deve estar em branco`), conforme o idioma do sistema e o header `Accept-Language` da requisição. As mensagens que você escreve em `message = "..."` não mudam.

## D. Cola rápida

**Maven:**

```bash
./mvnw spring-boot:run                 # sobe a aplicação
./mvnw test                            # roda os testes
./mvnw test -Dtest=PaymentServiceTest  # uma classe de teste
./mvnw package                         # gera o .jar (roda os testes antes)
./mvnw verify                          # o que o CI roda
```

**curl:**

```bash
curl -i URL                                            # GET mostrando status e headers
curl -i -X POST URL -H "Content-Type: application/json" -d '{"campo": "valor"}'
curl -i -X DELETE URL
curl -i -u usuario:senha URL                           # HTTP Basic (módulo 29)
curl -i -H "Idempotency-Key: $(uuidgen)" ...           # módulo 27
```

**Anotações por camada:**

| Camada | Anotações |
|--------|-----------|
| Aplicação | `@SpringBootApplication`, `@Configuration`, `@Bean` |
| Web | `@RestController`, `@RequestMapping`, `@GetMapping` e cia., `@PathVariable`, `@RequestParam`, `@RequestBody`, `@RequestHeader`, `@Valid`, `@ResponseStatus`, `@RestControllerAdvice`, `@ExceptionHandler` |
| Regra | `@Service`, `@Transactional` |
| Dados | `@Entity`, `@Table`, `@Id`, `@GeneratedValue`, `@Column`, `@ManyToOne`, `@OneToOne`, `@JoinColumn`, `@Enumerated`, `@Version`, `@EntityGraph` |
| Validação | `@NotBlank`, `@NotNull`, `@Size`, `@Email`, `@Pattern`, `@Positive`, `@Digits`, `@Cpf` (sua) |
| Testes | `@Test`, `@ParameterizedTest`, `@WebMvcTest`, `@DataJpaTest`, `@MockitoBean`, `@ExtendWith(MockitoExtension.class)`, `@Mock`, `@ActiveProfiles` |

**Endpoints do projeto:**

| Método | Caminho | Sucesso |
|--------|---------|---------|
| POST | `/patients` | 201 |
| GET | `/patients?page=&size=&sort=` | 200 |
| GET | `/patients/{id}` | 200 |
| PUT | `/patients/{id}` | 200 |
| DELETE | `/patients/{id}` | 204 |
| POST | `/patients/{patientId}/charges` | 201 |
| GET | `/charges?status=&patientId=&page=&size=` | 200 |
| GET | `/charges/{id}` | 200 |
| POST | `/charges/{id}/cancel` | 200 |
| POST | `/charges/{id}/payments` | 201 (200 com `Idempotency-Key` repetida) |
| GET | `/charges/{id}/payment` | 200 |

## E. Trabalhando como num time (e o diferencial "metodologias ágeis")

A vaga cita metodologias ágeis como diferencial. Dá para mostrar isso no próprio repositório, sem custo:

1. **GitHub Project** (quadro Kanban) com as colunas `Todo`, `In progress`, `Review` e `Done`.
2. **Uma issue por módulo** a partir da Fase 2 (por exemplo, "Module 16: charges and N+1"), com critérios de aceite escritos em Gherkin, que você já domina.
3. **Um branch por issue** (`feature/charges`), **um pull request** que fecha a issue (`Closes #12` na descrição) e o CI verde antes do merge.
4. **Commits pequenos** no padrão Conventional Commits: `feat:`, `fix:`, `test:`, `refactor:`, `docs:`.
5. **Revisão**: antes do merge, releia o diff do PR como se fosse de outra pessoa (ou peça a revisão no chat).

## F. Usando o Claude Code neste projeto

O objetivo é **entender**, então use o Claude Code como professor, não como gerador de código:

- Peça **dicas e o conceito** antes da solução: "qual conceito do módulo 16 resolve este erro?" em vez de "corrija".
- Peça **revisão** do que você escreveu, no formato dos outros roteiros: Resumo, achados por gravidade, fundamento e fonte.
- Peça para **explicar um log ou uma stack trace** linha por linha.
- Evite pedir o código de um exercício que você ainda não tentou.

Se quiser, dá para criar um `AGENTS.md` e um `CLAUDE.md` para o repositório, no mesmo padrão do projeto de SQL (o agente corrige e dá dicas, roda os testes, não resolve exercícios e nunca roda `sudo` nem apaga o volume do banco). É só pedir.

## G. Checklist de progresso

- [x] Módulo 0: Ambiente
- [x] Módulo 1: Do código de teste ao programa
- [ ] Módulo 2: Classes, records e enums
- [ ] Módulo 3: Dinheiro e datas
- [ ] Módulo 4: Coleções, lambdas e streams
- [ ] Módulo 5: Exceções e regras de negócio
- [ ] Módulo 6: Testes com JUnit
- [ ] Questões da Fase 1
- [ ] Módulo 7: HTTP do lado de quem responde
- [ ] Módulo 8: Servidor HTTP sem framework
- [ ] Módulo 9: Injeção de dependência e auto-configuração
- [ ] Módulo 10: API REST em memória
- [ ] Módulo 11: Validação e ProblemDetail
- [ ] Módulo 12: Testando a camada web
- [ ] Questões da Fase 2
- [ ] Módulo 13: MySQL para quem estudou PostgreSQL
- [ ] Módulo 14: Flyway
- [ ] Módulo 15: JPA e Spring Data
- [ ] Módulo 16: Relacionamentos e N+1
- [ ] Módulo 17: Transações e concorrência
- [ ] Módulo 18: Testando a persistência
- [ ] Questões da Fase 3
- [ ] Módulo 19: Status derivado e filtros
- [ ] Módulo 20: Pagamentos
- [ ] Módulo 21: Mockito e casos de borda
- [ ] Módulo 22: CPF e dados pessoais
- [ ] Questões da Fase 4
- [ ] Módulo 23: Paginação
- [ ] Módulo 24: OpenAPI e README
- [ ] Módulo 25: Docker
- [ ] Módulo 26: CI
- [ ] Questões da Fase 5
- [ ] Módulo 27: Idempotência
- [ ] Módulo 28: Testes de ponta a ponta
- [ ] Módulo 29: Spring Security
- [ ] Questões da Fase 6
- [ ] Módulo 30: Projeto final
- [ ] Trilha: L1 [ ] L2 [ ] L3 [ ] L4 [ ] L5 [ ] L6

---

## Fontes

- [Documentação do Spring Boot](https://docs.spring.io/spring-boot/): estrutura das Fases 2 a 5, profiles, configuração externa, testes (pacotes do Boot 4 conferidos na página "Testing Spring Boot Applications" e no Javadoc da versão 4.1).
- [Documentação do Spring Framework](https://docs.spring.io/spring-framework/reference/): IoC, Spring MVC, ProblemDetail, transações, `MockMvcTester`.
- [Spring Boot 4.0.0 available now (spring.io)](https://spring.io/blog/2025/11/20/spring-boot-4-0-0-available-now/) e [Spring Boot Versions, EOL Dates (HeroDevs)](https://www.herodevs.com/blog-posts/spring-boot-versions-eol-dates-and-latest-releases-april-2026): versões (4.1 estável desde junho de 2026, 4.2 prevista para novembro de 2026).
- [Spring Boot 4 migration (relato do Ministry of Justice)](https://ministryofjustice.github.io/laa-provider-data-platform/pdp-docs/spring-boot-4-migration.html) e [OpenRewrite: modular starters](https://docs.openrewrite.org/recipes/java/spring/boot4/migratetomodularstarters-community-edition): apêndice A (starters renomeados, starters de teste, Flyway).
- [Spring Boot PR #41574](https://github.com/spring-projects/spring-boot/pull/41574): necessidade do `flyway-mysql`.
- [Hibernate HHH-16498](https://hibernate.atlassian.net/browse/HHH-16498) e [discussão no fórum do Hibernate](https://discourse.hibernate.org/t/hibernate-validate-fails-on-enum/7535): o problema de enum com `validate` no MySQL (módulo 16).
- [springdoc-openapi](https://github.com/springdoc/springdoc-openapi): linha 3.x para o Spring Boot 4.
- [MySQL 8.4 Reference Manual](https://dev.mysql.com/doc/refman/8.4/en/): módulo 13 e apêndice B.
- [JUnit User Guide](https://docs.junit.org/current/user-guide/), [Mockito](https://site.mockito.org/) e [AssertJ](https://assertj.github.io/doc/): testes.
- [RFC 9110 (HTTP Semantics)](https://www.rfc-editor.org/rfc/rfc9110) e [RFC 9457 (Problem Details)](https://www.rfc-editor.org/rfc/rfc9457): módulos 7, 11 e 27.
- [Omarchy no GitHub](https://github.com/basecamp/omarchy): instalação de Java pelo menu (`bin/omarchy-install-dev-env`, que usa `java@latest`), MySQL 8.4 do "Docker DB" (`bin/omarchy-install-docker-dbs`), regra de janela das IDEs JetBrains (`default/hypr/apps/jetbrains.lua`) e o aviso do Sudoless Docker (`bin/omarchy-setup-security-sudoless-docker`).
- [mise: Java](https://mise.jdx.dev/lang/java.html) e [notas da versão 2026.10.5 do mise](https://newreleases.io/project/github/jdx/mise/release/v2026.10.5): Temurin como distribuição padrão.
- [IntelliJ IDEA unificado (JetBrains)](https://www.jetbrains.com/help/idea/intellij-idea-single-distribution.html), [IntelliJ IDEA 2025.3: recursos gratuitos de Spring](https://tech.yahoo.com/computing/articles/intellij-idea-2025-3-arrives-160549394.html) e [Wayland nativo a partir da 2026.1 (OMG! Ubuntu)](https://www.omgubuntu.co.uk/2026/02/intellij-native-wayland-support-2026-1): módulo 0.
- [Playwright: API testing](https://playwright.dev/docs/api-testing): módulo 28.
- [PCI Security Standards Council](https://www.pcisecuritystandards.org/) e [LGPD](https://www.planalto.gov.br/ccivil_03/_ato2015-2018/2018/lei/l13709.htm): módulos 20 e 22.
- Documentos do projeto: `roteiro-sql.md` (transações, índices, locks), `roteiro-typescript.md` (verificação exaustiva) e `roteiro-playwright.md` (testes de API, CI).

> **Como os exemplos foram verificados:** os exemplos em Java puro da Fase 1 (módulos 1 a 5) e o servidor HTTP do módulo 8 foram compilados e executados com Java 21; as saídas mostradas são as reais, e o servidor foi testado com `curl`. A lógica do `ChargeTest` do módulo 6 foi executada. As migrations dos módulos 14, 17 e 20, o script de `mysql-init` e todos os comandos do módulo 13 foram executados num MySQL 8.0.46 (o roteiro usa a imagem 8.4; os recursos usados são os mesmos). O código **Spring** (Fases 2 a 6) **não** pôde ser compilado no ambiente em que o roteiro foi escrito, porque o repositório Maven Central estava bloqueado. Ele foi escrito contra a documentação oficial do Spring Boot 4.1 (pacotes de teste, `MockMvcTester`, `@MockitoBean`) e revisado à mão. Se algo não compilar, o erro e a correção são um ótimo exercício, e você pode mandar no chat.
