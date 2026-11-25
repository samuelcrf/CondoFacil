import { Route, Routes } from "react-router-dom";

import LayoutBase from "./componentes/LayoutBase.jsx";
import PaginaInicial from "./paginas/PaginaInicial.jsx";
import PaginaNaoEncontrada from "./paginas/PaginaNaoEncontrada.jsx";
import PaginaDeCadastroDeCondominio from "./paginas/PaginaDeCadastroDeCondominio.jsx";

export default function App() {
  return (
    <Routes>
      <Route element={<LayoutBase />}>
        <Route path="/" element={<PaginaInicial />} />
        <Route path="/cadastro" element={<PaginaDeCadastroDeCondominio />} />
        <Route path="*" element={<PaginaNaoEncontrada />} />
      </Route>
    </Routes>
  );
}
