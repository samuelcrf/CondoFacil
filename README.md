# CondoFácil

Sistema web de administração para condomínios (projeto da disciplina de Gerência de Projeto).

## Estrutura (monorepo)

- `backend/`  → API em Java 21 + Spring Boot (Maven)
- `frontend/` → Aplicação em React + Vite + Bootstrap

## Como rodar em desenvolvimento

Abra **dois terminais**.

**Terminal 1 – API (porta 8080)**

```
cd backend
./mvnw spring-boot:run        # no Windows: mvnw.cmd spring-boot:run
```

**Terminal 2 – Front-end (porta 5173)**

```
cd frontend
npm install                   # só na primeira vez
npm run dev
```

Abra http://localhost:5173 — a página mostra "Online" quando consegue falar com a API.
O front-end repassa os pedidos que começam com `/api` para a API (proxy do Vite).

## Testes

```
cd backend && ./mvnw test
cd frontend && npm run build
```

Requisitos: Java 21 e Node.js 20 ou superior.
