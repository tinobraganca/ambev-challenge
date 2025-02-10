# Read Me First
## Order Management Service

Este projeto é um serviço de gerenciamento de pedidos desenvolvido com Spring Boot. Ele possui integração assíncrona com o RabbitMQ para processamento de pedidos e utiliza o PostgreSQL para persistência dos dados. Além disso, conta com uma excelente documentação da API utilizando o Springdoc OpenAPI (Swagger) e logs estruturados com SLF4J.
## Recursos

    Criação de Pedidos de Forma Assíncrona:
    Ao enviar uma requisição de criação de pedido, o serviço publica uma mensagem em uma fila do RabbitMQ e um consumidor processa essa mensagem para persistir o pedido junto aos produtos relacionados (usando cascade ALL).

    Persistência de Dados:
    Utiliza Spring Data JPA para salvar e consultar os pedidos e produtos no banco PostgreSQL.

    Documentação da API com Swagger:
    Toda a API está documentada com Swagger (Springdoc OpenAPI), permitindo testes e visualização interativa dos endpoints.

    Logs com SLF4J:
    Registra mensagens de log para acompanhar o fluxo da aplicação e auxiliar na depuração.

## Arquitetura

1.Todos os projetos contam com sua observability integradados Grafana:
- Usado Prometheus para receber os dados colotados gerar graficos e dashboards em cima de metricas da app.
- Usado o Tempo para receber tracers em Tempo real e cruzar com os demais informações
- Usado o Loki para receber os logs da app e cruzar com os demais indicadores do Grafana
- Usado o Jaeger All in one para efetuar o tracerID de salto de microserviços.

O projeto possui os seguintes módulos principais:

    OrderController: Responsável por expor os endpoints REST para criação e consulta de pedidos.
    OrderService: Contém a lógica de negócio para processar pedidos, incluindo validação de duplicidade e associação de produtos.
    OrderProducer & OrderConsumer: Gerenciam a comunicação com o RabbitMQ para envio e processamento assíncrono das mensagens de pedido.
    Persistência com JPA: As entidades OrderEntity e ProductEntity estão mapeadas para persistir os dados no PostgreSQL (com relacionamento 1:N e cascade ALL).

Tecnologias Utilizadas

    Java 21
    Spring Boot
    Spring Data JPA
    RabbitMQ (Spring AMQP)
    PostgreSQL
    Docker & Docker Compose
    Springdoc OpenAPI (Swagger)
    SLF4J para Logging

Pré-requisitos

    Java 21 ou superior instalado.
    Docker e Docker Compose instalados.
    (Opcional) IDE de sua preferência (Eclipse, IntelliJ, VS Code, etc.).

# Como Executar

O projeto já possui um arquivo docker-compose.yml que configura:

    PostgreSQL – Banco de dados para persistência dos pedidos.
    RabbitMQ – Broker de mensageria.
    Order Service – A aplicação Spring Boot.

Para iniciar todos os serviços, execute no diretório do projeto:

    docker-compose up --build

    A aplicação ficará disponível em http://localhost:8080.

Documentação Interativa

Após iniciar a aplicação, acesse a documentação interativa em:

http://localhost:8080/swagger-ui/index.html

Esta interface permite visualizar todos os endpoints, parâmetros e executar requisições diretamente pelo navegador.
Testes

# Testes

O projeto inclui testes unitários e de integração utilizando JUnit 5 e Mockito:

    OrderServiceTest: Testa a lógica de negócio para processamento de pedidos.
    OrderControllerTest: Testa os endpoints REST utilizando MockMvc.

# Observações Minhas
Considerando que o volume diário de pedidos está na faixa de 150.000 a 200.000, vamos converter esses números para requisições por minuto e por segundo para avaliar a necessidade de um processamento assíncrono.

    Para 150.000 pedidos/dia:
        Por minuto:
        150.000 ÷ 1.440 ≈ 104 pedidos por minuto.
        Por segundo:
        150.000 ÷ 86.400 ≈ 1,74 pedidos por segundo.

    Para 200.000 pedidos/dia:
        Por minuto:
        200.000 ÷ 1.440 ≈ 139 pedidos por minuto.
        Por segundo:
        200.000 ÷ 86.400 ≈ 2,31 pedidos por segundo.

Ou seja, a carga média gira em torno de 104 a 139 requisições por minuto ou aproximadamente 1,7 a 2,3 requisições por segundo.

Esse nível de demanda é relativamente baixo e, normalmente, pode ser atendido de forma síncrona por um sistema bem dimensionado. Utilizar processamento assíncrono, neste contexto, pode não ser necessário e, na verdade, pode introduzir complexidades adicionais, tais como:

- Pontos de Falha Adicionais:
  O uso de filas (como RabbitMQ) traz a necessidade de monitorar e gerenciar o estado das mensagens, o que pode resultar em atrasos ou perda de mensagens se não for feito corretamente.

- Complexidade na Arquitetura:
  Sistemas assíncronos exigem mecanismos extras para garantir a consistência dos dados, tratamento de erros e reprocessamento de mensagens, aumentando o esforço de desenvolvimento e manutenção.

- Overhead de Integração:
  A configuração e integração de serviços de mensageria podem demandar mais recursos e aumentar a latência em casos específicos.

# Observações 2

O projeto ficou com algumas pendencia por questão de tempo:
- O Script de db não está sendo versionado ou criado pelo FlyWay
- Alguns momento OTLP está com erro de envio nas metricas, acredito ser alguma diferença na versão da dependecia do micrometer


### Reference Documentation
For further reference, please consider the following sections:

* [Official Gradle documentation](https://docs.gradle.org)
* [Spring Boot Gradle Plugin Reference Guide](https://docs.spring.io/spring-boot/3.4.3-SNAPSHOT/gradle-plugin)
* [Create an OCI image](https://docs.spring.io/spring-boot/3.4.3-SNAPSHOT/gradle-plugin/packaging-oci-image.html)
* [Spring Web](https://docs.spring.io/spring-boot/3.4.3-SNAPSHOT/reference/web/servlet.html)
* [Spring for RabbitMQ](https://docs.spring.io/spring-boot/3.4.3-SNAPSHOT/reference/messaging/amqp.html)
* [OTLP for metrics](https://docs.spring.io/spring-boot/3.4.3-SNAPSHOT/reference/actuator/metrics.html#actuator.metrics.export.otlp)
* [codecentric's Spring Boot Admin (Client)](https://codecentric.github.io/spring-boot-admin/current/#getting-started)
* [codecentric's Spring Boot Admin (Server)](https://codecentric.github.io/spring-boot-admin/current/#getting-started)
* [Spring Boot DevTools](https://docs.spring.io/spring-boot/3.4.3-SNAPSHOT/reference/using/devtools.html)
* [Spring Data JPA](https://docs.spring.io/spring-boot/3.4.3-SNAPSHOT/reference/data/sql.html#data.sql.jpa-and-spring-data)
* [Flyway Migration](https://docs.spring.io/spring-boot/3.4.3-SNAPSHOT/how-to/data-initialization.html#howto.data-initialization.migration-tool.flyway)

### Guides
The following guides illustrate how to use some features concretely:

* [Building a RESTful Web Service](https://spring.io/guides/gs/rest-service/)
* [Serving Web Content with Spring MVC](https://spring.io/guides/gs/serving-web-content/)
* [Building REST services with Spring](https://spring.io/guides/tutorials/rest/)
* [Messaging with RabbitMQ](https://spring.io/guides/gs/messaging-rabbitmq/)
* [Accessing Data with JPA](https://spring.io/guides/gs/accessing-data-jpa/)

### Additional Links
These additional references should also help you:

* [Gradle Build Scans – insights for your project's build](https://scans.gradle.com#gradle)

