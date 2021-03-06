# Minhas Finanças
[![Build Status](https://travis-ci.org/luizimcpi/minhasfinancas.svg?branch=master)](https://travis-ci.org/luizimcpi/minhasfinancas)
```
Projeto para controle de finanças pessoais utilizando as seguintes tecnologias:
```
- [LOMBOK](https://www.testcontainers.org/)
- [SPRING BOOT](https://spring.io/projects/spring-boot)
- [JAVA 11](https://www.oracle.com/br/java/technologies/javase-jdk11-downloads.html)
- [FLYWAY MIGRATION TOOL](https://flywaydb.org/)
- [TEST CONTAINERS PG](https://www.testcontainers.org/)


## Como Executar a aplicação

```
*Necessário configurar uma base de dados postgreSQL

1 - Alterar as credenciais da base de dados no arquivo application.properties
2 - Executar o arquivo MinhasfinancasApplication.java

ou 

2 - Executar o comando no diretorio raiz -> ./mvnw spring-boot:run

ou 

3 - via docker 

./mvnw clean package

docker build -t minhasfinancas .

**docker-compose rm (case exists old builds, you need remove)
docker-compose up
```

## Como executar os testes
```
./mvnw test
```

### Como gerar pacote para deploy 
```
./mvnw clean package
```

## TODO
```
- Criar logs e rastreabilidade com ELK
```

### Versões Geradas seguindo semver.org

```
https://semver.org/
```
