import { Route, Routes } from "react-router-dom";

import LayoutBase from "./componentes/LayoutBase.jsx";
import RotaProtegida from "./componentes/RotaProtegida.jsx";
import PaginaDeLogin from "./paginas/PaginaDeLogin.jsx";
import PaginaDeAcessoNegado from "./paginas/PaginaDeAcessoNegado.jsx";
import PaginaDeCadastroDeCondominio from "./paginas/PaginaDeCadastroDeCondominio.jsx";
import PaginaDeUsuariosDoCondominio from "./paginas/PaginaDeUsuariosDoCondominio.jsx";
import PaginaInicial from "./paginas/PaginaInicial.jsx";
import PaginaNaoEncontrada from "./paginas/PaginaNaoEncontrada.jsx";

export default function App() {
  return (
    <Routes>
      <Route element={<LayoutBase />}>
        <Route path="/" element={<PaginaInicial />} />

        <Route path="/cadastro" element={<PaginaDeCadastroDeCondominio />} />

        <Route path="/login" element={<PaginaDeLogin />} />

        <Route path="/acesso-negado" element={<PaginaDeAcessoNegado />} />

        <Route element={<RotaProtegida perfisPermitidos={["SINDICO"]} />}>
          <Route path="/usuarios" element={<PaginaDeUsuariosDoCondominio />} />
        </Route>

        <Route path="*" element={<PaginaNaoEncontrada />} />
      </Route>
    </Routes>
  );
}
