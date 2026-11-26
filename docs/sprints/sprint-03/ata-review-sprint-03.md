# Ata da Sprint Review: Sprint 3 (Release 2)

## 1. Identificação

| Campo | Informação |
|---|---|
| Projeto | CondoFácil |
| Sprint | Sprint 3, Release 2 (17/11/2026 a 26/11/2026) |
| Data da Review | 26/11/2026 |
| Participantes e papéis | Samuel: PO, Scrum Master, QA e DevOps |
| Repositório | https://github.com/samuelcrf/condofacil |

## 2. Sprint Goal

**Meta:** entregar a autenticação e o controle de acesso (cadastro de condomínio e síndico, login e logout, isolamento por condomínio e cadastro de moradores e prestadores).

**Resultado: parcialmente atingido.** O cadastro de condomínio e síndico, o login e logout e o modelo de dados foram concluídos, e o controle de acesso está pronto e testado. O cadastro de moradores e prestadores não foi iniciado.

## 3. Resultado por US

| US | PBI | Pontos | Situação | Decisão do PO |
|---|---|---|---|---|
| US05 | Modelo de dados de condomínio, usuário e perfil | 3 | Concluída | Aceita |
| US06 | Cadastrar condomínio e síndico | 3 | Concluída | Aceita |
| US07 | Login e logout | 3 | Concluída | Aceita |
| US08 | Controle de acesso por perfil e isolamento por condomínio | 5 | Concluída | Aceita |
| US09 | Cadastrar moradores e prestadores | 3 | Não iniciada | Volta ao backlog, Sprint 4 |


## 4. Métricas da sprint

| Métrica | Valor |
|---|---|
| Pontos planejados | 17 |
| Pontos concluídos (velocity) | 9 (14 se a US08 fechar com o CI verde) |
| US concluídas | 3 de 5 (4 de 5 com a US08) |
| Velocity da Sprint 2 | 11 de 11 |

## 5. Definition of Done da sprint

Verificada na planilha "Definition of Done – Sprint e Release", coluna Sprint 3.

- Critérios respondidos: Sprint Goal (Parcialmente), US atendem à DoD de US (Parcialmente), regressão executada (Sim), riscos atualizados (Sim) e critérios da próxima sprint prontos (Sim).
- Principal desvio: as US da Sprint 3 foram integradas direto na `main`, sem Pull Request. A partir da Sprint 4 haverá uma branch e um PR por US, com CI verde antes do merge.

## 6. Bugs e impedimentos

- **Bugs encontrados na sprint:** nenhum bug
- **Impedimentos:** Impossibilidade de concluir a US-09 a tempo. Passará para a Sprint 4 e será prioridade.

## 7. Riscos

A planilha de riscos foi atualizada em 26/11/2026 com 3 riscos de Segurança: isolamento entre condomínios (15, Evitar), ex-morador com acesso (12, Mitigar) e perfil executando ação de outro (12, Mitigar).

## 8. Backlog repriorizado para a Sprint 4

- A US09 (3 pontos) volta ao backlog e entra na Sprint 4.
- Sprint 4 prevista: US09, US10, US11, US13, US14, US15 e US16 (20 pontos), mais os PBIs de driver DA-SEG-I1-01 e DA-SEG-I1-02 (3 pontos cada), se a capacidade permitir.
- O driver DA-SEG-I1-03 fica na Sprint 5.
- Todos os PBIs da Sprint 4 têm critérios em Gherkin e estimativa.

## 9. Ações

| Ação | Responsável | Prazo |
|---|---|---|
| Fazer o push da US08 e confirmar o CI verde | Samuel | 26/11/2026 |
| Mover US05 a US07 para Done e a US09 para a Sprint 4 no Azure Boards | Samuel | 26/11/2026 |
| Salvar as capturas de burndown e velocity em `docs/sprints/sprint-03/` | Samuel | 26/11/2026 |
| Passar a usar branch e Pull Request por US | Samuel | Início da Sprint 4 (01/12/2026) |

Assinatura de ciência: Samuel (PO, SM, QA e DevOps) | Data: 26/11/2026
