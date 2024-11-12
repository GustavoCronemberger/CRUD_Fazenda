# CRUD Fazenda

## Índice
1. [Introdução](#introdução)
2. [Tecnologias Utilizadas](#tecnologias-utilizadas)
3. [Estrutura do Projeto](#estrutura-do-projeto)
4. [Classes](#classes)
    - [Farm](#farm)
    - [Producer](#producer)
5. [Repository](#repository)
6. [Service](#service)
7. [Banco de Dados PostgreSQL](#banco-de-dados-postgresql)
8. [Testes](#testes)
    - [Testes de Repositório](#testes-de-repositório)
    - [Testes de Serviço](#testes-de-serviço)
9. [Operações CRUD](#operações-crud)

## Introdução
Este é meu primeiro projeto CRUD (Create, Read, Update, Delete), com o objetivo de treinar o conhecimento obtido em Java.<br> 
No projeto é possível criar, atualizar, salvar e deletar uma lista de fazendas e produtores (rurais), via jdbc com o banco de dados.

## Tecnologias Utilizadas
- Java
- Maven
- JUnit 5
- PostgreSQL
- Lombok
- Log4j2

## Estrutura do Projeto
O projeto é dividido nas classes Farm e Producer, com repository e service para melhor acesso ao banco de dados e lógica de negócios.<br>
Foi utilizado a classe Optional para evitar NullPointerException, deixando o código mais legível e de fácil manutenção.<br>
Todo o CRUD foi implementado manualmente, utilizando anotações Lombok e Log4j2 para melhorar as validações e métodos mais básicos das classes.<br>
Por fim, temos testes unitários e de integração para testar a funcionalidade do sistema com o banco de dados.

## Classes

### Farm
A classe Farm possui os seguintes atributos:
- `id`: Identificador único da fazenda
- `name`: Nome da fazenda
- `values`: Valor da fazenda
- `producer`: Produtor associado à fazenda

### Producer
A classe Producer possui os seguintes atributos:
- `id`: Identificador único do produtor
- `name`: Nome do produtor

## Repository
As classes de repository são responsáveis por interagir com o banco de dados. Elas contêm métodos para realizar operações CRUD nas tabelas `farm` e `producer`.

## Service
As classes de service contêm a lógica de negócios e utilizam os repositories para acessar o banco de dados. 

## Banco de Dados PostgreSQL
O banco de dados PostgreSQL é utilizado para armazenar as informações de fazendas e produtores. As tabelas são definidas conforme abaixo:

### Códigos SQL

CREATE TABLE farm (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    values INT NOT NULL,
    producer_id INT NOT NULL,
    FOREIGN KEY (producer_id) REFERENCES producer(id)
);

CREATE TABLE producer (
    id SERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

SELECT f.id AS farm_id, f.name AS farm_name, f.values, p.id AS producer_id, p.name AS producer_name
FROM farm_catalog.farm f
JOIN farm_catalog.producer p ON f.producer_id = p.id;

## Testes
Os testes são realizados para garantir que todas as operações CRUD funcionem corretamente.<br>
É possível testar método por método, assim como rodar todos os testes ao mesmo tempo.<br>
As principais funcionalidades são testadas para as entidades Farm e Producer de forma integral.

### Testes de Repositório
Os testes de repositório verificam as operações de acesso a dados para as entidades Farm e Producer Repository.

### Testes de Serviço
Os testes de serviço verificam a lógica de negócios para as operações de CRUD das entidades Farm e Producer Service.<br>
No projeto, é o CrudTest01, onde é possível rodar localmente junto ao banco de dados criando, salvando, modificando e deletando dados para Farm e Producer.