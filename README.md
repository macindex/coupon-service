Outforce Coupon Service
Um sistema de gerenciamento de cupons desenvolvido em Java Spring Boot para o teste técnico da Outforce.

📋 Descrição
Este projeto é uma API RESTful para gerenciamento de cupons de desconto, implementando todas as regras de negócio especificadas no teste técnico. A aplicação utiliza Spring Boot, JPA/Hibernate e banco de dados H2 em memória.

🚀 Funcionalidades
✅ Criação de cupons com validações de negócio

✅ Listagem de cupons ativos

✅ Busca de cupons por ID e código

✅ Exclusão lógica (soft delete) de cupons

✅ Validação de dados e formatação automática de código

✅ Testes unitários abrangentes

✅ Documentação da API via Postman

🛠 Tecnologias Utilizadas
Java 11

Spring Boot 2.7.0

Spring Data JPA

H2 Database (em memória)

Maven

JUnit 5 + Mockito

Validation API

📦 Estrutura do Projeto
text
src/main/java/com/outforce/coupon/
├── controller/          # Controladores REST
├── model/              # Entidades JPA
├── repository/         # Interfaces de repositório
├── service/           # Lógica de negócio
├── dto/               # Objetos de transferência de dados
└── exception/         # Exceções customizadas

🔧 Configuração e Execução
Pré-requisitos
Java 11 ou superior

Maven 3.6+

Postman (para testes da API)

Executando a Aplicação
1. Clone o repositório:

git clone <url-do-repositorio>
cd outforce-coupon-service

2. Compile o projeto:

Execute a aplicação:
mvn clean compile

bash
mvn spring-boot:run

4. Acesse os endpoints:

API: http://localhost:8080

Health Check: http://localhost:8080/health

H2 Console: http://localhost:8080/h2-console

Configuração do Banco H2
URL JDBC: jdbc:h2:mem:testdb

Usuário: sa

Senha: (vazia)

📡 Endpoints da API
Cupons
Método	Endpoint	Descrição
POST	/api/coupons	Cria um novo cupom
GET	/api/coupons	Lista todos os cupons ativos
GET	/api/coupons/{id}	Busca cupom por ID
GET	/api/coupons/code/{code}	Busca cupom por código
DELETE	/api/coupons/{id}	Exclui um cupom (soft delete)

Utilitários
Método	Endpoint	Descrição
GET	/	Status da aplicação
GET	/health	Health check da API

📝 Regras de Negócio Implementadas
Criação de Cupons
✅ Código alfanumérico com 6 caracteres

✅ Remoção automática de caracteres especiais

✅ Valor mínimo de desconto: 0.5

✅ Data de expiração não pode ser no passado

✅ Campos obrigatórios: code, description, discountValue, expirationDate

Exclusão de Cupons
✅ Soft delete (exclusão lógica)

✅ Impedir exclusão de cupom já excluído

✅ Preservação dos dados no banco

Validações
✅ Formatação automática do código

✅ Validação de dados de entrada

✅ Tratamento de exceções

🧪 Testes
Executando os Testes
bash
# Executar todos os testes
mvn test

# Executar testes com relatório de cobertura
mvn jacoco:report

Cobertura de Testes
Os testes cobrem:

✅ Criação de cupons com dados válidos

✅ Validação de regras de negócio

✅ Formatação de código

✅ Exclusão lógica

✅ Casos de erro e exceções

📋 Exemplos de Uso
Criar um Cupom
Request:

bash
curl -X POST http://localhost:8080/api/coupons \
  -H "Content-Type: application/json" \
  -d '{
    "code": "WELCOME@25#",
    "description": "Welcome discount",
    "discountValue": 25.5,
    "expirationDate": "2025-12-31T23:59:59",
    "published": true
  }'

  Response:

json
{
  "id": 1,
  "code": "WELCOM1",
  "description": "Welcome discount",
  "discountValue": 25.5,
  "expirationDate": "2025-12-31T23:59:59",
  "published": true,
  "deleted": false,
  "createdAt": "2024-01-15T10:30:00",
  "updatedAt": "2024-01-15T10:30:00"
}

Listar Cupons Ativos
Request:

bash
curl http://localhost:8080/api/coupons
🔍 Collection Postman
Uma collection completa do Postman está disponível no diretório postman/ com:

Todos os endpoints configurados

Casos de teste automatizados

Exemplos de requests e responses

Validações de resposta

📊 Estrutura do Banco
Tabela: coupons
Campo	Tipo	Descrição
id	BIGINT	Chave primária
code	VARCHAR(6)	Código do cupom (6 caracteres)
description	VARCHAR	Descrição do cupom
discount_value	DOUBLE	Valor do desconto (≥ 0.5)
expiration_date	TIMESTAMP	Data de expiração
published	BOOLEAN	Status de publicação
deleted	BOOLEAN	Flag de exclusão lógica
created_at	TIMESTAMP	Data de criação
updated_at	TIMESTAMP	Data de atualização
🐛 Solução de Problemas
Erro Comum: "Whitelabel Error Page"
Causa: Acesso à raiz sem endpoint mapeado

Solução: Use /health ou /api/coupons

Erro: "Coupon not found"
Causa: ID inválido ou cupom excluído

Solução: Verifique o ID e se o cupom não foi deletado

Erro de Validação
Causa: Dados inválidos no request

Solução: Verifique o formato da data e valor do desconto

👨‍💻 Desenvolvimento
Próximas Melhorias
Documentação Swagger/OpenAPI

Paginação na listagem de cupons

Filtros avançados

Autenticação e autorização

Cache de cupons frequentes

Métricas e monitoramento

Padrões de Código
bash
# Formatar código
mvn spotless:apply

# Verificar qualidade
mvn checkstyle:check
