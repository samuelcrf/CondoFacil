import { chamarApi } from "./clienteDeApi.js";

export function listarUsuariosDoCondominio() {
  return chamarApi("/api/usuarios", {
    mensagemPadraoDeErro: "Não foi possível carregar a lista de usuários.",
  });
}
