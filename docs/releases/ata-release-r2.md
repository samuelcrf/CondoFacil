# Ata da Release 2 (Sprints 2 e 3)


## 1. Identificação

| Campo | Informação |
|---|---|
| Projeto | CondoFácil |
| Release | Release 2 |
| Período | 03/11/2026 a 26/11/2026 |
| Data da Review de release | 26/11/2026 |
| Participantes e papéis | Samuel: PO, Scrum Master, QA e DevOps |
| Repositório | https://github.com/samuelcrf/condofacil |
| Versão / tag | [v0.2.0, a criar no GitHub] |

## 2. Objetivo da release

Entregar a fundação técnica do sistema e o acesso seguro: projetos de API e de front-end, banco de dados com migrações, pipeline de integração contínua, layout base, cadastro de condomínio e síndico, login e logout e controle de acesso por perfil com isolamento entre condomínios.

## 3. Escopo planejado × entregue

| Sprint | Planejado | Concluído |
|---|---|---|
| Sprint 2 | US01, US02, US30, US03, US04 (11 pontos) | Todas (11 pontos) |
| Sprint 3 | US05, US06, US07, US08, US09 (17 pontos) | US05, US06, US07 e US08 (14 pontos); US09 não iniciada |
| **Release 2** | **28 pontos** | **25 pontos** |

Cumprimento do escopo: 89% dos pontos. A US09 passa para a Release 3, Sprint 4.

A Release 1 foi ignorada pois foi necessário iniciar um novo projeto a partir da Sprint 3, de acordo com o calendário da disciplina.

## 4. Versão demonstrada

- **O que está funcionando:** API em Spring Boot, aplicação React, banco PostgreSQL com Flyway, cadastro de condomínio e síndico, login e logout com sessão e proteção CSRF, e controle de acesso por perfil com 404 para dados de outro condomínio.
- **Ambiente de homologação:** ainda não existe. A publicação entra na Release 3. A versão foi demonstrada executando localmente.

## 5. Qualidade (plano de qualidade, resultados da R2)

| Item | Resultado |
|---|---|
| Q-01 Cobertura de testes | Não medido (JaCoCo ainda não configurado) |
| Q-02 Controle de acesso e isolamento | 100% (3 de 3 perfis com teste de acesso negado) |
| Q-03 Dependências vulneráveis | Front-end: 0 (npm audit). Back-end: não medido |
| Q-04 Bugs críticos e altos em aberto | Nenhum bug crítico] |
| Q-05 Qualidade dos critérios de aceite | 100% (30 de 30 PBIs com Gherkin) |
| Q-06 Estabilidade do pipeline | CI Verde |

Regressão: suíte completa do back-end com todos os testes sem falhas, em execução local; a repetição no GitHub Actions está pendente.


## 6. Riscos

A planilha de riscos foi atualizada com 3 riscos de Segurança, tratados na Feature "Risco" do Azure: isolamento entre condomínios (15, Evitar), ex-morador com acesso (12, Mitigar) e perfil executando ação de outro (12, Mitigar).

## 7. Retrospectiva da release

**O que foi bem**
- Testes automatizados cobrindo cadastro, login e acesso por perfil.
- Backlog completo com critérios em Gherkin e plano de qualidade com métricas definidas.

**O que melhorar**
- A Sprint 1 não teve entrega e a Sprint 3 faltou a US-09.
- A Sprint 3 foi integrada direto na `main`, sem Pull Request.

**Ações**

| Ação | Responsável | Prazo |
|---|---|---|
| Usar branch e Pull Request por US, com CI verde | Samuel | 01/12/2026 |
| Criar `docs/sprints/` e `docs/releases/` com as atas e relatórios | Samuel | 26/11/2026 |
| Completar o README (como executar e como testar) | Samuel |Pendente |
| Configurar o JaCoCo e ativar o Dependabot | Samuel | Sprint 4 |
| Criar a tag e a release v0.2.0 no GitHub | Samuel | Pendente |

## 8. Gestão e papéis para a próxima release

- **Backlog repriorizado:** a Release 3 abrange as Sprints 4 e 5 (01/12 a 22/12/2026) e inclui a US09, os chamados, a triagem, o fluxo de status e a publicação.
- **Papéis da Release 3:** Samuel continua como PO, Scrum Master, QA e DevOps. Como o projeto é individual, não há passagem de bastão nem avaliação entre pares.

Assinatura de ciência: Samuel (PO, SM, QA e DevOps) | Data: 26/11/2026
