import { useEffect, useState } from "react";

import {
  consultarUsuarioLogado,
  entrar,
  sair,
} from "../servicos/servicoDeAutenticacao.js";
import { ContextoDeAutenticacao } from "./ContextoDeAutenticacao.js";

export default function ProvedorDeAutenticacao({ children }) {
  // Dados do usuário logado (null = ninguém logado).
  const [usuarioLogado, setUsuarioLogado] = useState(null);

  const [carregandoSessao, setCarregandoSessao] = useState(true);

  useEffect(() => {
    let componenteAindaExiste = true;
    consultarUsuarioLogado().then((usuario) => {
      if (componenteAindaExiste) {
        setUsuarioLogado(usuario);
        setCarregandoSessao(false);
      }
    });

    return () => {
      componenteAindaExiste = false;
    };
  }, []);

  async function entrarNoSistema(email, senha) {
    const usuario = await entrar(email, senha);
    setUsuarioLogado(usuario);
    return usuario;
  }

  async function sairDoSistema() {
    try {
      await sair();
    } finally {
      setUsuarioLogado(null);
    }
  }

  return (
    <ContextoDeAutenticacao.Provider
      value={{
        usuarioLogado,
        carregandoSessao,
        entrarNoSistema,
        sairDoSistema,
      }}
    >
      {children}
    </ContextoDeAutenticacao.Provider>
  );
}
