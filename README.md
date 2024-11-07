# CRUD Fazenda

## Índice
1. [Introdução](#introdução)
2. [Tecnologias Utilizadas](#tecnologias-utilizadas)
3. [Estrutura do Projeto](#estrutura-do-projeto)
4. [Classes](#classes)
   - [Farm](#farm)
   - [Producer](#producer)
5. [Operações CRUD](#operações-crud)
6. [Execução do Projeto](#execução-do-projeto)

## Introdução
Este é meu primeiro projeto básico de CRUD (Create, Read, Update, Delete), com o objetivo de treinar o conhecimento obtido em Java. No projeto é possível criar, atualizar, salvar e deletar uma lista de fazendas e produtores (rurais). 

## Tecnologias Utilizadas
- Java
- Maven
- PostgreSQL

## Estrutura do Projeto
O projeto é dividido nas classes Farm e Producer, com repository e service para melhor acesso ao banco de dados e à lógica de negócios. Todo o CRUD foi implementado manualmente, sem o uso de ferramentas adicionais.

## Classes

### Farm
A classe `Farm` possui os seguintes atributos:
- `id`: Identificador único da fazenda
- `name`: Nome da fazenda
- `value`: Valor da fazenda

### Producer
A classe `Producer` possui os seguintes atributos:
- `id`: Identificador único do produtor
- `name`: Nome do produtor

## Operações CRUD
O projeto implementa todas as operações CRUD para as entidades `Farm` e `Producer`:
- `Create`: Adição de novas fazendas e produtores
- `Read`: Leitura de fazendas e produtores existentes
- `Update`: Atualização de informações de fazendas e produtores
- `Delete`: Exclusão de fazendas e produtores

