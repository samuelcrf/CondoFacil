import { useState } from "react";
import { Link, NavLink } from "react-router-dom";

export default function BarraDeNavegacao() {
  const [menuEstaAberto, setMenuEstaAberto] = useState(false);

  function alternarMenu() {
    setMenuEstaAberto(!menuEstaAberto);
  }

  function fecharMenu() {
    setMenuEstaAberto(false);
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
        </div>
      </div>
    </nav>
  );
}
