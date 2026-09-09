# Treino API Matrícula + API Gateway

Projeto desenvolvido para estudo e prática de desenvolvimento **Backend Java com Spring Boot**, incluindo criação de API REST, persistência de dados, regras de negócio, autenticação JWT e implementação de API Gateway.

## 🏗️ Arquitetura

O projeto é composto por duas aplicações:

```text
                         CLIENTE
                            │
                            │
                            ▼
                 ┌─────────────────────┐
                 │     API GATEWAY      │
                 │       :8080         │
                 │                     │
                 │  Spring Security    │
                 │  JWT Authentication │
                 └──────────┬──────────┘
                            │
                            │ HTTP
                            ▼
                 ┌─────────────────────┐
                 │   API MATRÍCULA     │
                 │       :8081         │
                 │                     │
                 │ Controller          │
                 │ Service             │
                 │ Repository          │
                 └──────────┬──────────┘
                            │
                            ▼
                         BANCO
```

O cliente realiza as requisições através do **API Gateway**, que é responsável por validar o JWT antes de encaminhar as requisições para a API de Matrículas.

---

## 🚀 Tecnologias utilizadas

### API Gateway

* Java 17
* Spring Boot
* Spring Cloud Gateway
* Spring Security
* JWT
* JJWT
* Nimbus JWT
* Maven

### API de Matrículas

* Java 17
* Spring Boot
* Spring Web
* Spring Data JPA
* Hibernate
* Banco de dados
* Maven

### Ferramentas

* Git
* GitHub
* Postman
* IntelliJ IDEA 

---

# 🔐 Autenticação JWT

O API Gateway utiliza **Spring Security** como Resource Server para validar tokens JWT.

O fluxo de autenticação funciona da seguinte maneira:

```text
POST /api/login
       │
       ▼
Validação de usuário e senha
       │
       ▼
JWT gerado utilizando HS256
       │
       ▼
Cliente recebe o token
       │
       ▼
Authorization: Bearer <token>
       │
       ▼
API Gateway
       │
       ▼
JWT validado pelo Spring Security
       │
       ├── Token inválido → 401 Unauthorized
       │
       └── Token válido
              │
              ▼
       API de Matrículas
```

O algoritmo utilizado para assinatura e validação do token é:

```text
HS256 (HMAC SHA-256)
```

Por utilizar uma chave simétrica, a mesma chave é utilizada para assinar e validar o JWT.

> **Observação:** a chave utilizada neste projeto é destinada exclusivamente para fins de estudo.

---

# 🌐 Rotas

## API Gateway

Todas as requisições para a API de Matrículas devem passar pelo Gateway.

Base URL:

```text
http://localhost:8080
```

### Login

```http
POST /api/login
```

Endpoint público utilizado para gerar o JWT.

Request:

```json
{
  "username": "admin",
  "password": "123456"
}
```

Response:

```text
JWT_TOKEN
```

---

## Matrículas

As rotas abaixo exigem autenticação JWT.

### Cadastrar matrícula

```http
POST /api/matriculas
```

Header:

```text
Authorization: Bearer <token>
```

Body:

```json
{
  "nome": "Christian Alves",
  "idade": 25,
  "cpf": "12345678900",
  "email": "christian@email.com",
  "plano": "BLACK",
  "laudo": false
}
```

---

### Listar matrículas

```http
GET /api/matriculas
```

Header:

```text
Authorization: Bearer <token>
```

---

### Buscar matrícula por CPF

```http
GET /api/matriculas/{cpf}
```

Exemplo:

```http
GET /api/matriculas/12345678900
```

Header:

```text
Authorization: Bearer <token>
```

---

# 📋 Regras de negócio

A API possui regras relacionadas à idade do aluno.

### Idade mínima

O aluno deve possuir pelo menos **16 anos**.

```text
idade < 16
     ↓
cadastro rejeitado
```

### Laudo médico

Alunos com **60 anos ou mais** precisam informar um laudo médico.

```text
idade >= 60
     │
     ├── laudo = true  → cadastro permitido
     │
     └── laudo = false → cadastro rejeitado
```

---

# 🔒 Proteção das rotas

O `/api/login` é uma rota pública:

```text
/api/login → permitAll()
```

As demais requisições exigem autenticação:

```text
/api/matriculas/** → authenticated()
```

Caso uma requisição protegida seja realizada sem um JWT válido:

```text
401 Unauthorized
```

---

# 🔀 API Gateway

O Gateway realiza o roteamento das requisições para a API de Matrículas.

A rota:

```text
/api/matriculas/**
```

é encaminhada para:

```text
http://localhost:8081
```

O Gateway utiliza o filtro `StripPrefix` para remover o prefixo `/api`.

Exemplo:

```text
Cliente:

GET /api/matriculas
        │
        ▼
Gateway
        │
        ▼
API de Matrículas:

GET /matriculas
```

Também é adicionado um header para identificar que a requisição passou pelo Gateway:

```text
X-Gateway: api-gateway
```

---

# 📁 Estrutura do projeto

```text
Treino_Api_Matricula/
│
├── api-gateway/
│   ├── src/
│   │   └── main/
│   │       ├── java/
│   │       │   └── com.example.apigateway/
│   │       │       ├── config/
│   │       │       │   └── SecurityConfig.java
│   │       │       └── controller/
│   │       │           └── LoginController.java
│   │       │
│   │       └── resources/
│   │           └── application.yml
│   │
│   └── pom.xml
│
└── api-matricula/
    ├── src/
    │   └── main/
    │       ├── java/
    │       │   └── com.example.treino_matricula_apis/
    │       │       ├── controller/
    │       │       │   └── MatriculaController.java
    │       │       ├── service/
    │       │       │   └── MatriculaService.java
    │       │       ├── repository/
    │       │       │   └── MatriculaRepository.java
    │       │       └── model/
    │       │           └── Matricula.java
    │       │
    │       └── resources/
    │           └── application.properties
    │
    └── pom.xml
```

---

# ▶️ Como executar

## 1. Clonar o projeto

```bash
git clone https://github.com/christian-a579/Treino_Api_Matricula.git
```

## 2. Executar a API de Matrículas

Entre na pasta da API:

```bash
cd api-matricula
```

Execute:

```bash
./mvnw spring-boot:run
```

A API será executada em:

```text
http://localhost:8081
```

---

## 3. Executar o API Gateway

Entre na pasta:

```bash
cd api-gateway
```

Execute:

```bash
./mvnw spring-boot:run
```

O Gateway será executado em:

```text
http://localhost:8080
```

---

# 🧪 Testando com Postman

### 1. Gerar token

```http
POST http://localhost:8080/api/login
```

Body:

```json
{
  "username": "admin",
  "password": "123456"
}
```

Copie o JWT retornado.

### 2. Acessar rota protegida

```http
GET http://localhost:8080/api/matriculas
```

Em **Authorization**:

```text
Type: Bearer Token
Token: <JWT>
```

Com um token válido, o Gateway valida a autenticação e encaminha a requisição para a API de Matrículas.

Sem token ou com token inválido:

```text
401 Unauthorized
```

---

# 🎯 Objetivos do projeto

Este projeto foi desenvolvido com o objetivo de praticar conceitos importantes do desenvolvimento Backend Java:

* Desenvolvimento de APIs REST
* Arquitetura Controller → Service → Repository
* Injeção de dependências
* Spring Boot
* Spring Data JPA
* Persistência de dados
* Regras de negócio
* API Gateway
* Spring Cloud Gateway
* Spring Security
* Autenticação JWT
* Bearer Token
* Validação de tokens
* Roteamento de requisições
* Filters
* Debugging e tratamento de erros HTTP

---

# 📚 Aprendizados

Durante o desenvolvimento foram explorados conceitos de autenticação e segurança de APIs, incluindo a implementação de um fluxo completo de login com JWT e a validação dos tokens no API Gateway antes do acesso aos serviços protegidos.

O projeto também serviu para praticar debugging de problemas reais relacionados à autenticação, como falhas de validação de assinatura e incompatibilidade de algoritmo JWT.

---

## 👨‍💻 Autor

**Christian Alves**

Desenvolvedor | Java Backend

