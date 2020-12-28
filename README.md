# Minhas Finanças

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
```

## Como executar os testes
```
./mvnw test
```

## TODO
```
- Criar exception handler e remover lógica da camada de resource
- Adicionar controle com jwt
- Criar logs e rastreabilidade com ELK
```

#### Link do curso
```
https://www.udemy.com/course/desenvolva-aplicacoes-completas-com-spring-boot-e-react-js/
```