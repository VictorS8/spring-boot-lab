# Notas do Módulo 0

## Exercício 0.1

Ao rodar o `java -version` aparece:
```shell
openjdk version "25.0.5" 2026-10-20 LTS
OpenJDK Runtime Environment Temurin-25.0.5+8 (build 25.0.5+8-LTS)
OpenJDK 64-Bit Server VM Temurin-25.0.5+8 (build 25.0.5+8-LTS, mixed mode, sharing)
```

Ao rodar o `mvn -v` aparece:
```shell
Apache Maven 3.10.0 (c43a36b8d67be7e0805a411bc0898af1a51f5472)
Maven home: /home/powarch/.local/share/mise/installs/maven/3/apache-maven-3.10.0
Java version: 25.0.5, vendor: Eclipse Adoptium, runtime: /home/powarch/.local/share/mise/installs/java/temurin-25.0.5+8.0.LTS
Default locale: en_US, platform encoding: UTF-8, time zone: Brazil/DeNoronha
OS name: "linux", version: "7.2.5-3-omarchy", arch: "amd64", family: "unix"
```

Ao rodar o `mise where java` aparece:
```shell
/home/powarch/.local/share/mise/installs/java/temurin-25.0.5+8.0.LTS
```

Logo Java está na versão **temurin 25 LTS** e no caminho acima

Tive que pesquisar essa parte:
O maven precisa de uma JVM para ser executado, pois foi escrito em Java. Logo ele irá usar a JAVA_HOME como padrão e, se ela não existir, o java do PATH. Esse mesmo JDK é o que compila o projeto

Como extra é possível entender a importância da versão do Java usado no projeto ser a mesma do Java usado no Maven para que o resultado seja a mesmo independente do local que será rodado, não levando a incompatibilidades entre IDE e execução do Maven no terminal. Portanto, ao rodar o projeto no IntelliJ e ao rodar o maven no terminal, podem aparecer problemas que o IntelliJ não pegou

## Exercício 0.2

Veja o Ex2.java, a saída do programa mostra a versão do Java que está o SDK do projeto, conforme o Mise

Nome do projeto: spring-boot-lab
SDK: temurin-25
Language level: SDK default
Compiler output: spring-boot-lab/out

## Exercício 0.3

Resposta ao comando:

| VERSION() | CURRENT_USER() |   DATABASE()   |
|:---------:|:--------------:|:--------------:|
|  8.4.13   |   billing@%    | clinic_billing |
1 row in set (0.00 sec)

Funcionou.
Entendo que ele mudou qual banco de dados será usado.

Revendo os outros exercícios, é possível notar que o arquivo do @01-create-test-database.sql que é o inicializador, é o responsável por dar permissão de garantir privilégios ao usuário usado e criar o banco caso não exista

MYSQL_USER e MYSQL_DATABASE fazem o entrypoint dar ao billing todos os privilégios, mas somente para o inicial clinic_billing, com o script de inicialização cria o clinic_billing_test, sendo que sem o GRANT, o billing não teria acesso, logo sem o GRANT geraria erro

## Desafio

Sim, os bancos continuam lá.

Ao incluir o `-v` no `down -v`, é possível notar que o volume, logo o espaço usado no clinic-billing-api_mysql-data foi removido, levando a ser criado novamente quando rodou o `up -d`

Ao pesquisar é possível entender que o `-v` é uma forma de referenciar volumes, então deleta realmente os dados, fazendo não persistir e nem preservar informações como volumes nomeados declarados e anônimos no `volumes:`, `down` sem o `-v` já remove os containers e a rede, o `-v` apenas acrescenta os volumes

O `mysql-init` só roda na primeira vez, pois o entrypoint da imagem só executa os scripts quando o diretório de dados (/var/lib/mysql, que é o volume mysql-data) está vazio, o banco mora nele, não no container. Com o `down`, o volume fica e o script é pulado, mas com o `down -v`, o volume é apagado, o MySQL inicializa do zero e o script roda de novo

