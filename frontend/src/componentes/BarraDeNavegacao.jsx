import { useState } from "react";
import { Link, NavLink, useNavigate } from "react-router-dom";

import { useAutenticacao } from "../contexto/useAutenticacao.js";
export default function BarraDeNavegacao() {
  const [menuEstaAberto, setMenuEstaAberto] = useState(false);

  const { usuarioLogado, carregandoSessao, sairDoSistema } = useAutenticacao();
  const navegarPara = useNavigate();

  function alternarMenu() {
    setMenuEstaAberto(!menuEstaAberto);
  }

  function fecharMenu() {
    setMenuEstaAberto(false);
  }

  async function aoClicarEmSair() {
    fecharMenu();
    await sairDoSistema();
    navegarPara("/");
  }

  return (
    <nav className="navbar navbar-expand-md navbar-dark bg-dark">
      <div className="container">
        <Link className="navbar-brand" to="/" onClick={fecharMenu}>
          CondoFácil
        </Link>

        <button
          className="navbar-toggler"
          type="button"
          aria-label="Abrir ou fechar o menu"
          aria-expanded={menuEstaAberto}
          onClick={alternarMenu}
        >
          <span className="navbar-toggler-icon"></span>
        </button>

        <div
          className={
            menuEstaAberto
              ? "collapse navbar-collapse show"
              : "collapse navbar-collapse"
          }
        >
          <ul className="navbar-nav me-auto">
            <li className="nav-item">
              <NavLink className="nav-link" to="/" end onClick={fecharMenu}>
                Início
              </NavLink>
            </li>
          </ul>

          {!carregandoSessao && (
            <ul className="navbar-nav">
              {usuarioLogado ? (
                <>
                  <li className="nav-item">
                    <span className="navbar-text me-md-3">
                      {usuarioLogado.nome}
                    </span>
                  </li>
                  <li className="nav-item">
                    <button
                      type="button"
                      className="btn btn-outline-light btn-sm"
                      onClick={aoClicarEmSair}
                    >
                      Sair
                    </button>
                  </li>
                </>
              ) : (
                <>
                  <li className="nav-item">
                    <NavLink
                      className="nav-link"
                      to="/cadastro"
                      onClick={fecharMenu}
                    >
                      Cadastrar condomínio
                    </NavLink>
                  </li>
                  <li className="nav-item">
                    <NavLink
                      className="nav-link"
                      to="/login"
                      onClick={fecharMenu}
                    >
                      Entrar
                    </NavLink>
                  </li>
                </>
              )}
            </ul>
          )}
        </div>
      </div>
    </nav>
  );
}
