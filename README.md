# Cinema Booking API

Uma API RESTful robusta desenvolvida em **Spring Boot** para o gerenciamento completo de um ecossistema de cinema. O
sistema abrange desde o backoffice (gerenciamento de filmes, salas e sessões) até a jornada do cliente final (escolha de
assentos, reservas e pagamentos integrados).

## Principais Funcionalidades

* **Autenticação e Segurança:** * Autenticação via **JWT (JSON Web Token)**.
    * Armazenamento seguro do token no cliente através de **Cookies HttpOnly**.
    * Controle de Acesso Baseado em Cargos (**RBAC**): Separação estrita entre rotas públicas, rotas de clientes (
      `STANDARD`) e rotas administrativas (`ADMINISTRADOR`).
* **Catálogo (Backoffice):** * CRUD completo de Filmes, Salas de Cinema e Sessões.
    * Validação de capacidade máxima das salas.
* **Sistema de Reservas:**
    * Mapeamento real de assentos (ex: "A1", "B5") validando as dimensões da sala.
    * Bloqueio de assentos simultâneos para evitar *overbooking*.
    * Suporte a diferentes tipos de ingressos (`INTEIRA` e `MEIA_ENTRADA`) com cálculo automático de preço.
    * **Cron Job (Scheduled):** Rotina automatizada de faxina que libera assentos de reservas pendentes não pagas após
      10 minutos.
* **Pagamentos Integrados:**
    * Integração nativa com o checkout do **Stripe**.
    * Implementação de **Webhooks** para escutar e processar eventos do Stripe (`checkout.session.completed`),
      atualizando o status da reserva automaticamente de forma assíncrona.

## Tecnologias Utilizadas

* **Java 21**
* **Spring Boot 3** (Web, Security, Data JPA, Validation)
* **PostgreSQL** (Banco de dados relacional)
* **Flyway** (Versionamento e migração do banco de dados)
* **Stripe Java SDK** (Gateway de pagamento)
* **JJWT** (Geração e validação de tokens JWT)
* **Swagger / OpenAPI 3** (Documentação interativa da API)
* **Docker & Docker Compose** (Containerização do ambiente)

## ⚙️ Variáveis de Ambiente

Para rodar o projeto, é necessário configurar as seguintes variáveis de ambiente, copie o arquivo env.example e siga o
preenchimento das variaveis abaixo:

| Variável                | Descrição                                                                               |
|:------------------------|:----------------------------------------------------------------------------------------|
| `DB_USER`               | Usuário do PostgreSQL                                                                   |
| `DB_PASSWORD`           | Senha do PostgreSQL                                                                     |
| `ADMIN_PASSWORD_HASH`   | Hash BCrypt da senha do usuário root administrador inicial (Flyway V5)                  |
| `JWT_SECRET`            | Chave secreta em Base64 para assinatura dos tokens JWT                                  |
| `STRIPE_SECRET_KEY`     | Chave secreta da API do Stripe para geração de pagamentos                               |
| `STRIPE_WEBHOOK_SECRET` | Segredo de assinatura do Webhook do Stripe (para garantir que o request veio do Stripe) |

## Pré-requisitos

1. **Docker e Docker Compose** instalados.
2. **Java 21+** (caso deseje rodar a aplicação fora dos containers).
3. **PostgresSQL** (caso deseje rodar a aplicação fora dos containers).
4. **STRIPE CLI**
5. Cliente HTTP para testar as requisições locais (ex: **Insomnia** ou **Postman**).

## Como executar localmente

1. **Clone o repositório:**
   ```bash
   git clone https://github.com/ThalesSilva67/Cinema-Booking.git
   cd cinema-booking-api

2. **Configure o ambiente:**

   Certifique-se de que o arquivo .env foi criado e preenchido corretamente com base no passo anterior.


3. **Suba os containers (Primeira execução):**

   Execute o comando abaixo na raiz do projeto. Ele fará o download das dependências, construirá a imagem da API e
   iniciará os containers do Spring Boot e do PostgreSQL em segundo plano:
   ```bash
    docker compose up -d --build
   ```

4. Execuções posteriores:
   ```bash
   docker compose up -d
   ```
   (Ou utilize a interface gráfica do Docker Desktop, caso prefira).

## Testando Pagamentos Localmente (Stripe CLI)

Como o sistema utiliza Webhooks para confirmar o pagamento das reservas, você precisará da **Stripe CLI** rodando na sua
máquina para escutar os eventos do Stripe e redirecioná-los para o seu `localhost`

1. Instale a [Stripe CLI](https://stripe.com/docs/stripe-cli).

2. Faça login na sua conta do Stripe(ou crie uma antes, caso não tenha) pelo terminal:

      ```bash
        stripe login
      ```
3. Inicie o redirecionamento de webhooks para a sua API:
 - Se estiver rodando via docker(porta 8081):
    ```bash
    stripe listen --forward-to localhost:8081/api/payments/webhook
    ```
- Se estiver rodando pela IDE(porta 8080):
    ```bash
    stripe listen --forward-to localhost:8080/api/payments/webhook
    ```
Ao rodar o comando acima, o terminal exibirá um segredo de webhook (algo como whsec_...). Copie esse valor e substitua na variável STRIPE_WEBHOOK_SECRET dentro do seu arquivo .env. Sem isso, a API rejeitará o pagamento por falha de assinatura de segurança!

## Documentação da API (Swagger)

Com os containers em execução, a documentação interativa da API estará disponível através do seu localhost na porta 8081

- http://localhost:8081/swagger-ui.html

Ou porta 8080 caso esteja pela IDE
- http://localhost:8080/swagger-ui.html

### Ambiente de Produção:

Caso não queira configurar e rodar o projeto localmente, a API está hospedada e disponível para testes neste endereço:

- http://163.176.32.105:8081/swagger-ui.html

(Nota: sujeito à disponibilidade do servidor).