import { useEffect, useState } from "react";

import { consultarMeuCondominio } from "../servicos/servicoDeCondominios.js";
import { rotuloDoPerfil } from "../utilitarios/rotulosDePerfil.js";

export default function CartaoDoMeuCondominio({ usuarioLogado }) {
  //dados do condominio (null se ainda estiver em espera)
  const [condominio, setCondominio] = useState(null);
  const [mensagemDeErro, setMensagemDeErro] = useState("");

  useEffect(() => {
    let componenteAindaExiste = true;
    consultarMeuCondominio()
      .then((dados) => {
        if (componenteAindaExiste) {
          setCondominio(dados);
        }
      })
      .catch((erro) => {
        if (componenteAindaExiste) {
          setMensagemDeErro(erro.message);
        }
      });
    return () => {
      componenteAindaExiste = false;
    };
  }, []);

  return (
    <div className="card mb-4" style={{ maxWidth: "24rem" }}>
      <div className="card-body">
        <h2 className="h6 card-title">Seu condomínio</h2>

        {mensagemDeErro && <p className="text-danger mb-0">{mensagemDeErro}</p>}
        {!mensagemDeErro && !condominio && (
          <p className="text-muted mb-0">Carregando...</p>
        )}
        {condominio && (
          <>
            <p className="mb-1">
              <strong>{condominio.nome}</strong>
            </p>
            <p className="text-muted mb-2">{condominio.endereco}</p>
          </>
        )}
        <span className="badge text-bg-primary">
          {rotuloDoPerfil(usuarioLogado.perfil)}
        </span>
      </div>
    </div>
  );
}
