# MultiPay

## Descrição
MultiPay é um backend que simula um sistema de pagamentos baseado em arquitetura de microsserviços. Ele foi desenvolvido em Java, utilizando PostgreSQL para o banco de dados, Kafka para a comunicação entre serviços e Docker para facilitar o ambiente de desenvolvimento e implantação.

O sistema tem como objetivo demonstrar conceitos de microsserviços, processamento assíncrono e integração entre componentes distribuídos em uma solução de pagamentos.

O serviço central é o `payment-process`, responsável por receber a solicitação de pagamento, persistir no banco e encaminhar a requisição para um dos processadores simulados (`pagpay` ou `pagsafe`) via Kafka ou requisições HTTP. Esses processadores representam gateways de pagamento externos, que apenas recebem e registram os dados.

## Tecnologias Utilizadas
- Java com Spring Boot – linguagem principal do backend
- PostgreSQL – banco de dados relacional
- Apache Kafka – sistema de mensagens para comunicação entre microsserviços
- Docker – containerização para facilitar a execução dos serviços

## Funcionalidades
- Simulação de processamento de pagamentos
- Comunicação assíncrona entre serviços via Kafka
- Persistência dos dados de pagamento no PostgreSQL
- Ambiente de execução isolado usando Docker

### 1. Payment Process (`payment-process`)

| Método | Rota               | Descrição                              | Corpo da Requisição                  | Resposta                         |
|--------|--------------------|--------------------------------------|------------------------------------|---------------------------------|
| POST   | `/process-payments` | Processa um pagamento (processamento principal). | JSON `PaymentDto` com dados do pagamento | HTTP 200 OK com mensagem de sucesso |
| GET    | `/get-payments`     | Recupera todos os pagamentos processados.      | —                                  | Lista JSON de objetos `Payment` |
| DELETE | `/delete-all`       | Deleta todos os registros de pagamento.        | —                                  | HTTP 204 No Content (sem corpo)  |

---

### 2. PagPay (`payment-pagpay`)

| Método | Rota       | Descrição                                    | Corpo da Requisição                  | Resposta                       |
|--------|------------|----------------------------------------------|------------------------------------|-------------------------------|
| POST   | `/payments`| Recebe e processa uma requisição de pagamento PagPay. | JSON `PaymentDto` com dados do pagamento | HTTP 202 Accepted (sem corpo)  |
| GET    | `/payments`| Recupera todos os registros de pagamento PagPay.      | —                                  | Lista JSON de objetos `Pagpay`  |
| DELETE | `/payments`| Deleta todos os registros de pagamento PagPay.        | —                                  | HTTP 204 No Content (sem corpo) |

---

### 3. PagSafe (`payment-pagsafe`)

| Método | Rota       | Descrição                                      | Corpo da Requisição                  | Resposta                       |
|--------|------------|------------------------------------------------|------------------------------------|-------------------------------|
| POST   | `/payments`| Recebe e processa uma requisição de pagamento PagSeguro. | JSON `PaymentDto` com dados do pagamento | HTTP 202 Accepted (sem corpo)  |
| GET    | `/payments`| Recupera todos os registros de pagamento PagSeguro.      | —                                  | Lista JSON de objetos `Pagsafe` |
| DELETE | `/payments`| Deleta todos os registros de pagamento PagSeguro.        | —                                  | HTTP 204 No Content (sem corpo) |

---

### Campos do PaymentDto

| Campo      | Tipo           | Descrição                                      | Obrigatório | Valores Possíveis                |
|------------|----------------|------------------------------------------------|-------------|--------------------------------|
| processId  | UUID           | Identificador único do processo (gerado pelo sistema) | Não         | UUID                           |
| username   | String         | Nome do usuário que realizou o pagamento        | Sim         | Qualquer string                |
| name       | String         | Nome do processador (ex: PagPay, PagSafe)       | Não         | Strings definidas no sistema   |
| method     | String         | Método de pagamento                              | Sim         | "credit", "debit", "pix", etc.|
| amount     | Decimal (BigDecimal) | Valor do pagamento                              | Sim         | Número positivo                |
| tax        | Decimal (BigDecimal) | Taxa aplicada ao pagamento                       | Não         | Número decimal                |
| total      | Decimal (BigDecimal) | Valor total descontando a taxa                    | Não         | Número decimal                |
| status     | String         | Status do pagamento                              | Não         | "PENDING", "SUCCESS", "FAILED" |



### Exemplo PaymentDto JSON Body

```json
{
  "username": "user123",
  "amount": 88.32,
  "method": "CREDIT"
}
```

### Exemplo de resposta
```json
{
  "processId": "992dfc6a-6762-4c29-9551-b6ce30ef7fb0",
  "username": "user123",
  "name": "PagPay",
  "methods": "CREDIT",
  "amount": 88.32,
  "tax": 3.53,
  "total": 84.79,
  "status": "SUCCESS"
}
```

## Como Rodar
1. Certifique-se de ter o Docker instalado na sua máquina.

2. Clone o repositório:
```bash
  git clone https://github.com/Aegdae/MultiPay.git
```
3. Navegue até a pasta do projeto e execute o Docker Compose para subir todos os serviços:
```bash
  docker-compose up --build  
```
4. Os microsserviços estarão disponíveis nas portas configuradas no docker-compose.yml.

## Considerações Finais

MultiPay foi criado como um estudo prático de microsserviços e comunicação assíncrona com Kafka.  
O sistema simula um fluxo realista de pagamentos com orquestração de serviços e persistência em banco de dados.

Este projeto ainda está em evolução e poderá receber melhorias ao longo do tempo.  
Fique à vontade para testar, explorar e contribuir!
