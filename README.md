# Projeto de Inserção de Dados via CSV

Este projeto permite a inserção de dados em uma tabela de banco de dados PostgreSQL a partir de um arquivo CSV ou exportar os dados de toda tabela escolhida.

## ⚙️ Funcionalidades
- Selecionar a tabela de destino no banco de dados.
- Carregar dados de um arquivo CSV.
- Inserir os dados na tabela especificada.

## 📋 Pré-requisitos
- Java 17
- PostgreSQL
- Biblioteca JDBC para PostgreSQL

## 🚀 Como Executar
1. Clone o repositório:
    ```bash
    git clone https://github.com/brunomourasoares/projeto-csv
    cd projeto-csv
    ```

2. Compile o projeto:
    ```bash
    javac -cp .:postgresql-42.5.0.jar Main.java
    ```

3. Execute o programa:
    ```bash
    java -cp .:postgresql-42.5.0.jar Main
    ```

4. Informe o nome da tabela:
    ```
    Escolha a tabela na lista (Mostra todas as tabelas do banco de dados)
    ```

5. Selecione o arquivo CSV:
    ```
    Clique no botão procurar e escolha o arquivo
    ```

6. Inicie:
    ```
    Clique no botão Inserir Dados
    ```
7. Exportar:
    ```
    Localize o arquivo CSV:
    ```

## 📂 Estrutura do Projeto
```
|-- src
|   |-- main
|      |-- java
|         |-- br
|            |-- com
|               |-- CSVReader.java
|               |-- DAO.java
|               |-- Main.java 
|-- data
|   |-- lista.csv
|   |-- query.sql
|   |-- app.jpg
|-- .gitignore
|-- pom.xml
|-- README.md
```

## 📸 Screenshot
![App](https://raw.githubusercontent.com/brunomourasoares/projeto-csv/refs/heads/main/data/app.jpg)

## 🔧 Configuração do Banco de Dados
Certifique-se de configurar a conexão com o banco de dados no arquivo `DAO.java`:
```java
String url = "jdbc:postgresql://localhost:5432/cadastrocsv";
String user = "root";
String password = "SuaSenhaAqui";
```

## 📑 Formato do CSV
O arquivo CSV deve seguir o seguinte formato:
```
NOME;E-MAIL;TELEFONE
Lucas;lucas@accenture.com;11 984576287
Camila;camila@accenture.com;11 895768421
Andre;andre@accenture.com;11 577817822
Giulia;giulia@accenture.com;11 487975796
```

## 🐞 Tratamento de Erros
- Verifica se o arquivo está em branco
- Aceita somente o formato do .CSV

## 🤝 Contribuições
Contribuições são bem-vindas! Sinta-se à vontade para abrir issues e pull requests.

