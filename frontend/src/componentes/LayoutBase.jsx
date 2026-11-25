import { Outlet } from "react-router-dom";

import BarraDeNavegacao from "./BarraDeNavegacao.jsx";
import RodapeDoSistema from "./RodapeDoSistema.jsx";

export default function LayoutBase() {
  return (
    <div className="d-flex flex-column min-vh-100">
      <BarraDeNavegacao />

      <main className="container py-4 flex-grow-1">
        <Outlet />
      </main>

      <RodapeDoSistema />
    </div>
  );
}
