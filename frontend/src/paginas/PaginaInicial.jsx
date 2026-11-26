import { useEffect, useState } from "react";

import CartaoDoMeuCondominio from "../componentes/CartaoDoMeuCondominio.jsx";
import { useAutenticacao } from "../contexto/useAutenticacao.js";

export default function PaginaInicial() {
  const { usuarioLogado } = useAutenticacao();

  const [situacaoDaApi, setSituacaoDaApi] = useState("carregando");

  useEffect(() => {
    async function consultarSaudeDaApi() {
      try {
        const resposta = await fetch("/api/saude");
        if (!resposta.ok) {
          throw new Error("A API respondeu com erro");
        }
        const dados = await resposta.json();
        setSituacaoDaApi(dados.situacao === "ONLINE" ? "online" : "offline");
      } catch {
        setSituacaoDaApi("offline");
      }
    }

    consultarSaudeDaApi();
  }, []);

  return (
    <section>
      <h1 className="h3">Bem-vindo ao CondoFácil</h1>
      <p className="text-muted">
        Sistema de chamados de manutenção para condomínios.
      </p>

      {usuarioLogado && <CartaoDoMeuCondominio usuarioLogado={usuarioLogado} />}

      <div className="card" style={{ maxWidth: "24rem" }}>
        <div className="card-body">
          <h2 className="h6 card-title">Situação da API</h2>

          {situacaoDaApi === "carregando" && (
            <span className="badge text-bg-secondary">Consultando...</span>
          )}
          {situacaoDaApi === "online" && (
            <span className="badge text-bg-success">Online</span>
          )}
          {situacaoDaApi === "offline" && (
            <span className="badge text-bg-danger">Offline</span>
          )}
        </div>
      </div>
    </section>
  );
}
