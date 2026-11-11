import { useEffect, useState } from 'react'

/**
 * Página inicial.
 *
 * Também serve de prova de que o front-end consegue falar com a API:
 * ao abrir, ela consulta GET /api/saude (o proxy do Vite repassa ao Spring Boot).
 */
export default function PaginaInicial() {
  // Estado da consulta: 'carregando', 'online' ou 'offline'.
  const [situacaoDaApi, setSituacaoDaApi] = useState('carregando')

  // useEffect com [] roda uma única vez, quando a página aparece na tela.
  useEffect(() => {
    async function consultarSaudeDaApi() {
      try {
        const resposta = await fetch('/api/saude')
        if (!resposta.ok) {
          throw new Error('A API respondeu com erro')
        }
        const dados = await resposta.json()
        setSituacaoDaApi(dados.situacao === 'ONLINE' ? 'online' : 'offline')
      } catch {
        // Caiu aqui se a API estiver desligada ou inacessível.
        setSituacaoDaApi('offline')
      }
    }

    consultarSaudeDaApi()
  }, [])

  return (
    <section>
      <h1 className="h3">Bem-vindo ao CondoFácil</h1>
      <p className="text-muted">
        Sistema de chamados de manutenção para condomínios.
      </p>

      <div className="card" style={{ maxWidth: '24rem' }}>
        <div className="card-body">
          <h2 className="h6 card-title">Situação da API</h2>

          {situacaoDaApi === 'carregando' && (
            <span className="badge text-bg-secondary">Consultando...</span>
          )}
          {situacaoDaApi === 'online' && (
            <span className="badge text-bg-success">Online</span>
          )}
          {situacaoDaApi === 'offline' && (
            <span className="badge text-bg-danger">Offline</span>
          )}
        </div>
      </div>
    </section>
  )
}
