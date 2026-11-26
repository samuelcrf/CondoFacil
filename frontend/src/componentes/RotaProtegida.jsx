import { Navigate, Outlet, useLocation } from "react-router-dom";

import { useAutenticacao } from "../contexto/useAutenticacao.js";
import PaginaDeAcessoNegado from "../paginas/PaginaDeAcessoNegado.jsx";

export default function RotaProtegida({ perfisPermitidos }) {
  const { usuarioLogado, carregandoSessao } = useAutenticacao();
  const localizacaoAtual = useLocation();

  // Enquanto o servidor não responde se existe o login, se mantem carregando
  if (carregandoSessao) {
    return <p className="text-muted">Carregando...</p>;
  }

  if (!usuarioLogado) {
    // state guarda a página pedida, para a tela de login levar o usuário de volta a ela
    return (
      <Navigate
        to="/login"
        replace
        state={{ origem: localizacaoAtual.pathname }}
      />
    );
  }

  if (perfisPermitidos && !perfisPermitidos.includes(usuarioLogado.perfil)) {
    return <PaginaDeAcessoNegado />;
  }

  return <Outlet />;
}
