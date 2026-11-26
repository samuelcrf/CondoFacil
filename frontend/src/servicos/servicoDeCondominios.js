import { chamarApi } from "./clienteDeApi.js";

export function cadastrarCondominio(dadosDoCadastro) {
  return chamarApi("/api/condominios", {
    metodo: "POST",
    corpoJson: dadosDoCadastro,
    mensagemPadraoDeErro: "Não foi possível concluir o cadastro.",
  });
}
