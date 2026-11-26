import { StrictMode } from "react";
import { createRoot } from "react-dom/client";
import { BrowserRouter } from "react-router-dom";

// Estilos do Bootstrap
import "bootstrap/dist/css/bootstrap.min.css";

import App from "./App.jsx";
import ProvedorDeAutenticacao from "./contexto/ProvedorDeAutenticacao.jsx";

createRoot(document.getElementById("root")).render(
  <StrictMode>
    <BrowserRouter>
      <ProvedorDeAutenticacao>
        <App />
      </ProvedorDeAutenticacao>
    </BrowserRouter>
  </StrictMode>,
);
