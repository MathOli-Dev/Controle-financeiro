# Sistema de Controle Financeiro

API REST em **Java 21 + Spring Boot 3** com um dashboard web que consome a API. Permite registrar **receitas e despesas**, organizar por **categorias**, acompanhar **metas** e consultar **relatórios mensais**.

## Funcionalidades

- Lançamentos de receitas e despesas (CRUD), com filtro por mês e por tipo
- Categorias separadas por tipo (receita ou despesa); o sistema impede usar uma categoria de despesa em uma receita
- Metas com valor alvo, prazo e depósitos, com cálculo de progresso
- Relatório mensal: total de receitas, despesas, saldo e totais por categoria
- Dashboard: saldo do mês, comparativo dos últimos 6 meses, despesas por categoria e progresso das metas
- Validação de entrada e respostas de erro padronizadas (400, 404, 409, 422)
- Dados de exemplo carregados na primeira execução

## Tecnologias

Java 21 · Spring Boot 3.3 (Web, Data JPA, Validation) · H2 (padrão) / PostgreSQL · Maven · HTML, CSS e JavaScript puro com Chart.js · Docker

## Como executar

Requisitos: JDK 21 e Maven 3.9+.

```bash
git clone https://github.com/MathOli-Dev/controle-financeiro.git
cd controle-financeiro
mvn spring-boot:run
```

Abra **http://localhost:8080** para ver o dashboard. O banco H2 grava em `./data` e o console fica em `/h2-console`.

### Com PostgreSQL (Docker)

```bash
docker compose up --build
```

Ou localmente, apontando para seu banco:

```bash
SPRING_PROFILES_ACTIVE=postgres DB_URL=jdbc:postgresql://localhost:5432/financeiro mvn spring-boot:run
```

Para iniciar sem dados de exemplo: `--app.seed-demo-data=false`.

### Testes

```bash
mvn test
```

## Endpoints

| Método | Rota | Descrição |
|---|---|---|
| GET / POST | `/api/categories` | Lista / cria categoria |
| PUT / DELETE | `/api/categories/{id}` | Atualiza / remove categoria |
| GET | `/api/transactions?month=2026-11&type=EXPENSE` | Lista lançamentos (filtros opcionais) |
| GET / POST | `/api/transactions` , `/api/transactions/{id}` | Consulta / cria lançamento |
| PUT / DELETE | `/api/transactions/{id}` | Atualiza / remove lançamento |
| GET / POST | `/api/goals` | Lista / cria meta |
| PUT / DELETE | `/api/goals/{id}` | Atualiza / remove meta |
| POST | `/api/goals/{id}/deposit` | Registra depósito na meta |
| GET | `/api/reports/monthly?month=2026-11` | Relatório do mês |
| GET | `/api/reports/dashboard?month=2026-11` | Dados completos do dashboard |

Exemplo de lançamento:

```bash
curl -X POST http://localhost:8080/api/transactions \
  -H "Content-Type: application/json" \
  -d '{"description":"Mercado","amount":250.90,"date":"2026-11-08","type":"EXPENSE","categoryId":3}'
```

## Estrutura

```
src/main/java/br/com/controlefinanceiro
├── controller   # endpoints REST
├── service      # regras de negócio e relatórios
├── repository   # Spring Data JPA (inclui consultas agregadas)
├── model        # entidades: Category, Transaction, Goal
├── dto          # records de entrada e saída
├── exception    # erros de negócio e handler global
└── config       # CORS e carga de dados de exemplo
src/main/resources/static/index.html   # dashboard
```

## Próximos passos

- Autenticação (Spring Security + JWT) e dados por usuário
- Migrações com Flyway
- Lançamentos recorrentes e exportação do relatório em CSV/PDF

## Licença

MIT
