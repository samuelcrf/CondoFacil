import { useContext } from "react";

import { ContextoDeAutenticacao } from "./ContextoDeAutenticacao.js";

export function useAutenticacao() {
  const conteudo = useContext(ContextoDeAutenticacao);
  if (conteudo === null) {
    throw new Error(
      "useAutenticacao precisa ser usado dentro do ProvedorDeAutenticacao.",
    );
  }
  return conteudo;
}
