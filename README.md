# AutoBots - Gestão de Manutenção Veicular (ATVII)

Projeto desenvolvido para a disciplina de **Desenvolvimento Web III**, sob orientação do Prof. Dr. Eng. Gerson Penha. 

Esta atividade prática (**ATVII**) tem como objetivo principal a implementação dos quatro níveis de maturidade do **Richardson Maturity Model (RMM)** no microsserviço `automanager`, incluindo navegação por hipermídia (**HATEOAS**) através do **Spring HATEOAS**.

---

## 🐣 Easter Egg

* **EasterEgg:** `"EasterEgg: Oi Prof. Gerson :) "`
* **Localização no Código:** Declarado no arquivo `AutomanagerApplication.java` e exibido no console do servidor durante a inicialização do Spring Boot.

---

## 🏛️ Richardson Maturity Model (RMM) Implementado

A arquitetura RESTful da aplicação atende rigorosamente aos quatro níveis de maturidade:

1. **Nível 0 - O Pântano do POX (Plain Old XML/HTTP):**
   * Utilização do protocolo HTTP como mecanismo de comunicação e transporte entre cliente e servidor.

2. **Nível 1 - Recursos (URI única por recurso):**
   * Identificação de recursos individuais por URIs bem estruturadas e previsíveis (`/clientes`, `/cliente/{id}`, `/documentos`, `/documento/{id}`, `/enderecos`, `/endereco/{id}`, `/telefones`, `/telefone/{id}`).

3. **Nível 2 - Métodos HTTP e suas Semânticas:**
   * Utilização apropriada dos verbos HTTP padrão:
     * `GET`: Leitura e recuperação de recursos.
     * `POST`: Criação e cadastro de novos recursos.
     * `PUT`: Atualização de recursos existentes.
     * `DELETE`: Exclusão de recursos.
   * Respostas utilizando `ResponseEntity<T>` com códigos de status HTTP semânticos:
     * `200 OK`: Operações de atualização e exclusão bem-sucedidas.
     * `201 CREATED`: Criação de novo recurso com sucesso.
     * `302 FOUND`: Recursos localizados e retornados nas consultas.
     * `400 BAD REQUEST`: Requisições com parâmetros ou payload inválidos.
     * `404 NOT FOUND`: Recurso não localizado.
     * `409 CONFLICT`: Conflito no estado da requisição (ex.: id já fornecido no cadastro).

4. **Nível 3 - HATEOAS (Hypermedia As The Engine Of Application State):**
   * Implementação com **Spring HATEOAS**:
     * Entidades estendem `RepresentationModel<T>`.
     * Uso do padrão de design com a interface genérica `AdicionadorLink<T>` e componentes especializados (`AdicionadorLinkCliente`, `AdicionadorLinkDocumento`, `AdicionadorLinkEndereco`, `AdicionadorLinkTelefone`).
     * Inclusão de links hipermídia (`_links`) com relações `self` e de coleções (`clientes`, `documentos`, `enderecos`, `telefones`), permitindo navegação dinâmica e desacoplada pelos estados da API.

---

## 🛠️ Tecnologias Utilizadas

* **Java**: JDK 17
* **Framework**: Spring Boot 2.6.4
* **Hipermídia**: Spring Boot Starter HATEOAS
* **Persistência de Dados**: Spring Data JPA / H2 Database (Banco em memória)
* **Gerenciador de Dependências**: Maven
* **Testes Automatizados**: Spring Boot Starter Test / JUnit 5 / MockMvc

---

## 🚀 Como Executar o Projeto

### Pré-requisitos
* Java JDK 17 instalado e configurado (`JAVA_HOME`).
* Git instalado.

### Executar a Aplicação

**Via Terminal (Maven Wrapper):**

```bash
cd automanager
./mvnw spring-boot:run
```

*(No Windows PowerShell: `cd automanager; .\mvnw.cmd spring-boot:run`)*

**Executar os Testes Automatizados:**

```bash
cd automanager
./mvnw test
```

*(No Windows PowerShell: `cd automanager; .\mvnw.cmd test`)*

**Via IDE (Eclipse / STS / VS Code / IntelliJ):**
1. Importe a pasta `automanager` como **Existing Maven Project**.
2. Localize a classe principal `com.autobots.automanager.AutomanagerApplication.java`.
3. Execute como **Spring Boot App** ou **Java Application**.

---

## 📌 Endpoints da API

Endereço base: `http://localhost:8080`
Console do Banco H2: `http://localhost:8080/h2-console` (JDBC URL: `jdbc:h2:mem:automanagerdb`)

### 1. Cliente (`/cliente`, `/clientes`)

| Método | Endpoint | Status | Descrição |
| --- | --- | --- | --- |
| `GET` | `/clientes` | `302 FOUND` / `404 NOT_FOUND` | Lista todos os clientes com links HATEOAS individuais (`self`). |
| `GET` | `/cliente/{id}` | `302 FOUND` / `404 NOT_FOUND` | Obtém um cliente por ID com links para a coleção (`clientes`) e sub-recursos. |
| `POST` | `/cliente/cadastro` | `201 CREATED` / `409 CONFLICT` | Cadastra um novo cliente com dados relacionais. |
| `PUT` | `/cliente/atualizar` | `200 OK` / `400 BAD_REQUEST` | Atualiza dados de um cliente existente. |
| `DELETE` | `/cliente/excluir` | `200 OK` / `400 BAD_REQUEST` | Remove um cliente informando ID no corpo. |
| `DELETE` | `/cliente/excluir/{id}` | `200 OK` / `400 BAD_REQUEST` | Remove um cliente pelo ID na URI. |

### 2. Documento (`/documento`, `/documentos`)

| Método | Endpoint | Status | Descrição |
| --- | --- | --- | --- |
| `GET` | `/documentos` | `302 FOUND` / `404 NOT_FOUND` | Lista todos os documentos com links HATEOAS. |
| `GET` | `/documento/{id}` | `302 FOUND` / `404 NOT_FOUND` | Obtém um documento por ID com links HATEOAS. |
| `GET` | `/documentos/{clienteId}` | `302 FOUND` / `404 NOT_FOUND` | Lista todos os documentos pertencentes ao cliente especificado. |
| `POST` | `/documento/cadastro/{clienteId}` | `201 CREATED` / `404 NOT_FOUND` | Cadastra novos documentos para o cliente. |
| `PUT` | `/documento/atualizar` | `200 OK` / `400 BAD_REQUEST` | Atualiza dados de um documento existente. |
| `DELETE` | `/documento/excluir` | `200 OK` / `400 BAD_REQUEST` | Exclui documento informando ID no corpo. |
| `DELETE` | `/documento/excluir/{id}` | `200 OK` / `400 BAD_REQUEST` | Exclui documento pelo ID na URI. |

### 3. Endereço (`/endereco`, `/enderecos`)

| Método | Endpoint | Status | Descrição |
| --- | --- | --- | --- |
| `GET` | `/enderecos` | `302 FOUND` / `404 NOT_FOUND` | Lista todos os endereços com links HATEOAS. |
| `GET` | `/endereco/{id}` | `302 FOUND` / `404 NOT_FOUND` | Obtém endereço por ID com links HATEOAS. |
| `GET` | `/endereco/cliente/{clienteId}` | `302 FOUND` / `404 NOT_FOUND` | Obtém o endereço do cliente especificado. |
| `PUT` | `/endereco/atualizar` | `200 OK` / `400 BAD_REQUEST` | Atualiza um endereço existente. |
| `PUT` | `/endereco/atualizar/{clienteId}` | `200 OK` / `404 NOT_FOUND` | Atualiza ou define o endereço de um cliente. |
| `DELETE` | `/endereco/excluir` | `200 OK` / `400 BAD_REQUEST` | Exclui endereço informado no corpo. |
| `DELETE` | `/endereco/excluir/{id}` | `200 OK` / `400 BAD_REQUEST` | Exclui endereço pelo ID na URI. |

### 4. Telefone (`/telefone`, `/telefones`)

| Método | Endpoint | Status | Descrição |
| --- | --- | --- | --- |
| `GET` | `/telefones` | `302 FOUND` / `404 NOT_FOUND` | Lista todos os telefones com links HATEOAS. |
| `GET` | `/telefone/{id}` | `302 FOUND` / `404 NOT_FOUND` | Obtém telefone por ID com links HATEOAS. |
| `GET` | `/telefones/{clienteId}` | `302 FOUND` / `404 NOT_FOUND` | Lista todos os telefones do cliente especificado. |
| `POST` | `/telefone/cadastro/{clienteId}` | `201 CREATED` / `404 NOT_FOUND` | Cadastra novos telefones para o cliente. |
| `PUT` | `/telefone/atualizar` | `200 OK` / `400 BAD_REQUEST` | Atualiza um telefone existente. |
| `DELETE` | `/telefone/excluir` | `200 OK` / `400 BAD_REQUEST` | Exclui telefone informado no corpo. |
| `DELETE` | `/telefone/excluir/{id}` | `200 OK` / `400 BAD_REQUEST` | Exclui telefone pelo ID na URI. |
