import { Route, Routes } from "react-router-dom";

import BarraDeNavegacao from "./componentes/BarraDeNavegacao.jsx";
import PaginaInicial from "./paginas/PaginaInicial.jsx";
import PaginaNaoEncontrada from "./paginas/PaginaNaoEncontrada.jsx";

export default function App() {
  return (
    <>
      <BarraDeNavegacao />

      <main className="container py-4">
        <Routes>
          {/* Endereço "/" mostra a página inicial */}
          <Route path="/" element={<PaginaInicial />} />

          {/* O asterisco captura qualquer endereço que não exista */}
          <Route path="*" element={<PaginaNaoEncontrada />} />
        </Routes>
      </main>
    </>
  );
}
