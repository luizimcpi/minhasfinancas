# Minhas Finanças
[![Maven CI/CD](https://github.com/luizimcpi/minhasfinancas/actions/workflows/main.yml/badge.svg?branch=master)](https://github.com/luizimcpi/minhasfinancas/actions/workflows/main.yml)

```
Projeto para controle de finanças pessoais utilizando as seguintes tecnologias:
```
- [LOMBOK](https://projectlombok.org/)
- [SPRING BOOT](https://spring.io/projects/spring-boot)
- [JAVA 11](https://www.oracle.com/br/java/technologies/javase-jdk11-downloads.html)
- [FLYWAY MIGRATION TOOL](https://flywaydb.org/)
- [OTJ-PG-EMBEDDED](https://github.com/opentable/otj-pg-embedded)
- [SENDGRID](https://sendgrid.com/)


## Como Executar a aplicação

```
*Necessário configurar uma base de dados postgreSQL
docker run --name local-postgres -e "POSTGRES_PASSWORD=123456" -p 5432:5432 -v /home/SEU_USUARIO/Desenvolvimento/PostgreSQL:/var/lib/postgresql/data -d postgres

1 - Alterar as credenciais da base de dados no arquivo application.properties
2 - Esta aplicação utiliza sendgrid para enviar os emails , configure uma conta para obter o SENDGRID_API_KEY
3 - Executar o arquivo MinhasfinancasApplication.java

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

### Versões Geradas seguindo semver.org

```
https://semver.org/
```
