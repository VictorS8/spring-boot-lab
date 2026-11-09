# Exercício 1
## Na pasta `playground/modulo-01`, crie `Ex1.java` com um `main` que imprime a soma de dois números recebidos em `args`. Rode com `java Ex1.java 10 32`. O que acontece se você não passar argumentos?

Aparece um aviso de Too few arguments, pois eu tentei prever mais que 2 argumentos e menos que 2 argumentos (Para código inicial)

Erro que apareceu se não tiver argumentos, mostrando que não foi possível pegar um index de um array que não existe
Exception in thread "main" java.lang.ArrayIndexOutOfBoundsException: Index 0 out of bounds for length 0

# Exercício 2
## No `billing-core`, rode `mvn compile` e explore a pasta `target/classes`. Onde está o `App.class`? Por que nessa pasta?

A App.class esta no `target/classes/com/clinicbilling/core/`. Para seguir o mesmo caminho do package onde a App.class esta

`target/` é a pasta onde o Maven grava o que gera, e o `target/classes` é o classpath do `java -cp`

# Exercício 3
## Rode `mvn package` e abra o `.jar` com `unzip -l target/*.jar`. O que tem dentro? Tem algum teste?

| Length    | Date       | Time  | Name                                                         |
|-----------|------------|-------|--------------------------------------------------------------|
| 0         | 1980-02-01 | 00:00 | META-INF/                                                    |
| 99        | 1980-02-01 | 00:00 | META-INF/MANIFEST.MF                                         |
| 0         | 1980-02-01 | 00:00 | com/                                                         |
| 0         | 1980-02-01 | 00:00 | com/clinicbilling/                                           |
| 0         | 1980-02-01 | 00:00 | com/clinicbilling/core/                                      |
| 0         | 1980-02-01 | 00:00 | org/                                                         |
| 0         | 1980-02-01 | 00:00 | org/example/                                                 |
| 0         | 1980-02-01 | 00:00 | META-INF/maven/                                              |
| 0         | 1980-02-01 | 00:00 | META-INF/maven/com.clinicbilling/                            |
| 0         | 1980-02-01 | 00:00 | META-INF/maven/com.clinicbilling/billing-core/               |
| 1267      | 1980-02-01 | 00:00 | com/clinicbilling/core/App.class                             |
| 1035      | 1980-02-01 | 00:00 | org/example/Main.class                                       |
| 1452      | 1980-02-01 | 00:00 | META-INF/maven/com.clinicbilling/billing-core/pom.xml        |
| 73        | 1980-02-01 | 00:00 | META-INF/maven/com.clinicbilling/billing-core/pom.properties |
| --------- | --------   | ----  | -------                                                      |
| 3926      | 14 files   |       |                                                              |

Essas são os arquivos dentro do .jar. E não possui nenhum teste

# Exercício 4
## Mude a `maven.compiler.release` para `8` e rode `mvn compile`. Leia o erro (ou o aviso). Volte para `25`.

Teve que rodar com o `clean` no `mvn compile` e mudar alguma .java

Apareceu um aviso
```shell
[WARNING] COMPILATION WARNING :
[INFO] -------------------------------------------------------------
[WARNING] source value 8 is obsolete and will be removed in a future release
[WARNING] target value 8 is obsolete and will be removed in a future release
[WARNING] To suppress warnings about obsolete options, use -Xlint:-options.
```
E apareceu erros
```shell
[ERROR] COMPILATION ERROR : 
[INFO] -------------------------------------------------------------
[ERROR] /home/powarch/Projects/spring-boot-lab/billing-core/src/main/java/org/example/Main.java:[9,9] cannot find symbol
  symbol:   variable IO
  location: class org.example.Main
[ERROR] /home/powarch/Projects/spring-boot-lab/billing-core/src/main/java/org/example/Main.java:[14,13] cannot find symbol
  symbol:   variable IO
  location: class org.example.Main
```

# Desafio
## Tente rodar `java -jar target/billing-core-0.1.0-SNAPSHOT.jar`. Por que falha? Pesquise o que é o `Main-Class` do arquivo `MANIFEST.MF` e explique em um comentário no `pom.xml` o que faltaria para o `java -jar` funcionar. (No Spring Boot isso já vem pronto; você vai ver no módulo 25.)

Aparece este erro `no main manifest attribute, in target/billing-core-0.1.0-SNAPSHOT.jar`.
O `Main-Class` especifica a classe Java que contém o método main quando roda o `java -jar`
Coloquei o comentário na pom.xml
