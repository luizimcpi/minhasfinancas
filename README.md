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
2 - Esta aplicação utiliza sendgrid para enviar os emails , configure uma conta para obter o SENDGRID_API_KEY * DISABLED *
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

### Sonarqube

Rodar esses comandos no terminal  para ajustar a quantidade de memória virtual do host (isso pq a imagem do sonar utiliza um elasticsearch e exige que isso seja adequado -> https://hub.docker.com/_/sonarqube/)
```
sudo sysctl -w vm.max_map_count=262144
sudo sysctl -w fs.file-max=65536
ulimit -n 65536
ulimit -u 4096
```
Depois basta subir o docker-compose (diretorio sonarqube do projeto)
ai acessando pelo browser localhost:9000 vc vai ter que alterar a senha inicial
usuario inicial: admin
senha inicial: admin
Ai cria um projeto manualmente, ele vai te dar um comando parecido com esse aqui pra rodar na raiz do projeto que quiser analisar:

```
./mvnw clean verify sonar:sonar \
  -Dsonar.projectKey=minhasfinancas \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=YOUR_SONAR_GENERATED_TOKEN
```

Feita a análise ele vai jogar o relatório pro sonar ai vai ta la disponivel os dados pra analise ai só verificar no localhost:9000