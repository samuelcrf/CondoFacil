# CondoFácil

Sistema web de administração para condomínios (projeto da disciplina de Gerência de Projeto, UEPB).

O sistema permite que o síndico cadastre o condomínio e seus moradores e prestadores, e que cada perfil acesse somente o que lhe cabe. Os dados de cada condomínio ficam isolados dos demais.

## Tecnologias

- **Back-end:** Java 21, Spring Boot 3.5.7 (Web, Validation, Data JPA, Security), Flyway
- **Banco de dados:** PostgreSQL
- **Front-end:** React 19, Vite, React Router e Bootstrap
- **Testes:** JUnit e Mockito (back-end); oxlint e build (front-end)
- **Integração contínua:** GitHub Actions

## Como executar

Pré-requisitos: Java 21, Node.js 22 e PostgreSQL.

1. Crie o usuário e os bancos de dados:

   ```sql
   CREATE USER condofacil WITH PASSWORD 'condofacil';
   CREATE DATABASE condofacil OWNER condofacil;
   CREATE DATABASE condofacil_teste OWNER condofacil;
   ```

2. Inicie o back-end (API em http://localhost:8080):

   ```bash
   cd backend
   ./mvnw spring-boot:run
   ```

   No Windows, use `mvnw.cmd spring-boot:run`.

3. Em outro terminal, inicie o front-end (http://localhost:5173):

   ```bash
   cd frontend
   npm ci
   npm run dev
   ```

O banco pode ser configurado pelas variáveis `BANCO_URL`, `BANCO_USUARIO` e `BANCO_SENHA`. Sem elas, o sistema usa o banco local `condofacil`.

## Como testar

```bash
# Back-end (compila e roda todos os testes)
cd backend
./mvnw -B verify

# Front-end (análise estática e compilação)
cd frontend
npm run lint
npm run build
```

O mesmo é executado automaticamente a cada push pelo GitHub Actions (`.github/workflows/integracao-continua.yml`).

## Estrutura do repositório

| Pasta | Conteúdo |
|---|---|
| `backend/` | API em Spring Boot |
| `frontend/` | Aplicação React |
| `docs/sprints/` | Atas e métricas de cada sprint |
| `docs/releases/` | Atas e relatórios de cada release |
| `.github/workflows/` | Pipeline de integração contínua |

## Status

- **Release 2 (v0.2.0):** fundação técnica, cadastro de condomínio e síndico, login e logout, controle de acesso por perfil e isolamento entre condomínios.
- **Próxima release:** cadastro de moradores e prestadores, abertura e triagem de chamados e fluxo de status.


